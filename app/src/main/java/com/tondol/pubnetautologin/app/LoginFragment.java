package com.tondol.pubnetautologin.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by hosaka on 2014/05/28.
 */
public class LoginFragment extends Fragment {
    private LoginUtil loginUtil;
    private SettingUtil settingUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        v.findViewById(R.id.login_button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = settingUtil.getUsername();
                String password = settingUtil.getPassword();

                if (username == null || password == null) {
                    Toast.makeText(getActivity(), getString(R.string.login_toast_empty), Toast.LENGTH_SHORT).show();
                } else {
                    loginUtil.login(username, password);
                }
            }
        });
        v.findViewById(R.id.login_button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUtil.logout();
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

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        loginUtil = new LoginUtil(getActivity(), new LoginUtil.Listener() {
            private String getStringForRequestType(LoginUtil.RequestType type) {
                if (type == LoginUtil.RequestType.Login) {
                    return getString(R.string.login_toast_login);
                } else {
                    return getString(R.string.login_toast_logout);
                }
            }

            @Override
            public void onResponse(LoginUtil loginUtil, LoginUtil.RequestType type, String response) {
                android.util.Log.d("com.tondol.pubnetautologin", "onResponse: " + response);
                Toast.makeText(getActivity(), getStringForRequestType(type), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onErrorResponse(LoginUtil loginUtil, LoginUtil.RequestType type, Exception e) {
                android.util.Log.d("com.tondol.pubnetautologin", "onErrorResponse: " + e.getLocalizedMessage());
                Toast.makeText(getActivity(), getString(R.string.login_toast_error), Toast.LENGTH_SHORT).show();
            }
        });
        loginUtil.start();

        settingUtil = new SettingUtil(getActivity());
    }

    @Override
    public void onPause() {
        loginUtil.stop();

        super.onPause();
    }
}
