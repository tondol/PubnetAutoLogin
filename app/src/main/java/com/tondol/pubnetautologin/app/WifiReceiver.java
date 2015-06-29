package com.tondol.pubnetautologin.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WifiReceiver extends BroadcastReceiver {

    private LoginStrategy getLoginStrategy(final Context context, String ssid) {
        LoginStrategy.Listener listener = new LoginStrategy.Listener() {
            @Override
            public void onResponse(LoginStrategy loginUtil, LoginStrategy.RequestType type, String response) {
                App.getInstance().log("WifiReceiver$LoginStrategy.Listener#onResponse");
                Toast.makeText(context, context.getString(R.string.login_toast_login), Toast.LENGTH_SHORT).show();

                loginUtil.stop();
            }
            @Override
            public void onErrorResponse(LoginStrategy loginStrategy, LoginStrategy.RequestType type, Exception e) {
                App.getInstance().log("WifiReceiver$LoginStrategy.Listener#onErrorResponse: " + e.getLocalizedMessage());
                Toast.makeText(context, context.getString(R.string.login_toast_error), Toast.LENGTH_SHORT).show();

                loginStrategy.stop();
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
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            App.getInstance().log("WifiReceiver#onReceive: SUPPLICANT_STATE_CHANGED_ACTION");
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);

            if (state == SupplicantState.COMPLETED) {
                String ssid = wm.getConnectionInfo().getSSID().replace("\"", "");
                App.getInstance().log("SSID: " + ssid);
                processForSSID(context, ssid);
            }
        } else if (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            App.getInstance().log("WifiReceiver#onReceive: SUPPLICANT_CONNECTION_CHANGE_ACTION");

            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                String ssid = wm.getConnectionInfo().getSSID().replace("\"", "");
                App.getInstance().log("SSID: " + ssid);
                processForSSID(context, ssid);
            }
        }
    }
}
