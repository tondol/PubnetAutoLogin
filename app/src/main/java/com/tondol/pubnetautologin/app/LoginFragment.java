package com.tondol.pubnetautologin.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hosaka on 2014/05/28.
 */
public class LoginFragment extends Fragment {
    private LoginStrategy loginStorategy;
    private SettingStrategy settingStrategy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        v.findViewById(R.id.login_button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = settingStrategy.getUsername();
                String password = settingStrategy.getPassword();

                if (username == null || password == null) {
                    Toast.makeText(getActivity(), getString(R.string.login_toast_empty), Toast.LENGTH_SHORT).show();
                } else {
                    loginStorategy.login(username, password);
                }
            }
        });
        v.findViewById(R.id.login_button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginStorategy.logout();
            }
        });
        v.findViewById(R.id.login_button_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new SettingFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // デバッグ時はVISIBLEにする
        v.findViewById(R.id.login_debug).setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        loginStorategy = new TitechPubnetLoginStrategy(getActivity(), new LoginStrategy.Listener() {
            private String getStringForRequestType(LoginStrategy.RequestType type) {
                if (type == LoginStrategy.RequestType.Login) {
                    return getString(R.string.login_toast_login);
                } else {
                    return getString(R.string.login_toast_logout);
                }
            }

            @Override
            public void onResponse(LoginStrategy loginUtil, LoginStrategy.RequestType type, String response) {
                App.getInstance().log("LoginFragment$LoginStrategy.Listener#onResponse");
                Toast.makeText(getActivity(), getStringForRequestType(type), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onErrorResponse(LoginStrategy loginUtil, LoginStrategy.RequestType type, Exception e) {
                App.getInstance().log("LoginFragment$LoginStrategy.Listener#onErrorResponse: " + e.getLocalizedMessage());
                Toast.makeText(getActivity(), getString(R.string.login_toast_error), Toast.LENGTH_SHORT).show();
            }
        });
        loginStorategy.start();

        settingStrategy = new PreferenceSettingStrategy(getActivity());

        App.getInstance().setLoggingListener(new App.LoggingListener() {
            @Override
            public void onLog(String s) {
                TextView tv = (TextView) getView().findViewById(R.id.login_debug);
                tv.append(s + "\n");
            }
        });
    }

    @Override
    public void onPause() {
        App.getInstance().setLoggingListener(null);

        loginStorategy.stop();

        super.onPause();
    }
}
