package com.tondol.pubnetautologin.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WifiReceiver extends BroadcastReceiver {
    private void processForSSID(final Context context, String ssid) {
        if (!ssid.equals("titech-pubnet")) {
            return;
        }

        LoginUtil loginUtil = new LoginUtil(context, new LoginUtil.Listener() {
            @Override
            public void onResponse(LoginUtil loginUtil, LoginUtil.RequestType type, String response) {
                android.util.Log.d("com.tondol.pubnetautologin", "onResponse: " + response);
                Toast.makeText(context, context.getString(R.string.login_toast_login), Toast.LENGTH_SHORT).show();

                loginUtil.stop();
            }
            @Override
            public void onErrorResponse(LoginUtil loginUtil, LoginUtil.RequestType type, Exception e) {
                android.util.Log.d("com.tondol.pubnetautologin", "onErrorResponse: " + e.getLocalizedMessage());
                Toast.makeText(context, context.getString(R.string.login_toast_error), Toast.LENGTH_SHORT).show();

                loginUtil.stop();
            }
        });
        SettingUtil settingUtil = new SettingUtil(context);

        loginUtil.start();
        loginUtil.login(settingUtil.getUsername(), settingUtil.getPassword());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        android.util.Log.d("com.tondol.pubnetautologin", "onReceive");

        if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            android.util.Log.d("com.tondol.pubnetautologin", "SUPPLICANT_STATE_CHANGED_ACTION");
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);

            if (state == SupplicantState.COMPLETED) {
                WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                String ssid = wm.getConnectionInfo().getSSID().replace("\"", "");

                android.util.Log.d("com.tondol.pubnetautologin", "SupplicantState.COMPLETED: " + ssid);
                processForSSID(context, ssid);
            }
        }
    }
}
