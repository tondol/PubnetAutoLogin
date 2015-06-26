package com.tondol.pubnetautologin.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WifiReceiver extends BroadcastReceiver {

    private LoginStrategy getLoginStrategy(final Context context, String ssid) {
        LoginStrategy.Listener listener = new LoginStrategy.Listener() {
            @Override
            public void onResponse(LoginStrategy loginUtil, LoginStrategy.RequestType type, String response) {
                android.util.Log.d("pubnetautologin", "onResponse: " + response);
                Toast.makeText(context, context.getString(R.string.login_toast_login), Toast.LENGTH_SHORT).show();

                loginUtil.stop();
            }
            @Override
            public void onErrorResponse(LoginStrategy loginUtil, LoginStrategy.RequestType type, Exception e) {
                android.util.Log.d("pubnetautologin", "onErrorResponse: " + e.getLocalizedMessage());
                Toast.makeText(context, context.getString(R.string.login_toast_error), Toast.LENGTH_SHORT).show();

                loginUtil.stop();
            }
        };

        if (ssid.equals("titech-pubent") || ssid.equals("TokyoTech")) {
            return new TitechPubnetLoginStrategy(context, listener);
        } else {
            throw new RuntimeException("The SSID is not supported: " + ssid);
        }
    }

    private void processForSSID(Context context, String ssid) {
        try {
            LoginStrategy loginStrategy = getLoginStrategy(context, ssid);
            SettingStrategy settingStrategy = new PreferenceSettingStrategy(context);

            loginStrategy.start();
            loginStrategy.login(settingStrategy.getUsername(), settingStrategy.getPassword());
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(context, "The SSID is not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        android.util.Log.d("pubnetautologin", "onReceive");

        if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            android.util.Log.d("pubnetautologin", "SUPPLICANT_STATE_CHANGED_ACTION");
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);

            if (state == SupplicantState.COMPLETED) {
                WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                String ssid = wm.getConnectionInfo().getSSID().replace("\"", "");

                android.util.Log.d("pubnetautologin", "SupplicantState.COMPLETED: " + ssid);
                processForSSID(context, ssid);
            }
        }
    }
}
