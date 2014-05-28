package com.tondol.pubnetautologin.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by hosaka on 2014/05/28.
 */
public class SettingFragment extends Fragment {
    private SettingUtil settingUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_setting, container, false);
        v.findViewById(R.id.setting_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) v.findViewById(R.id.setting_edittext_username)).getText().toString();
                String password = ((EditText) v.findViewById(R.id.setting_edittext_password)).getText().toString();

                if (username.length() == 0 ||
                        password.length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.setting_toast_empty), Toast.LENGTH_SHORT).show();
                } else {
                    settingUtil.setUsername(username);
                    settingUtil.setPassword(password);

                    Toast.makeText(getActivity(), getString(R.string.setting_toast_save), Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        settingUtil = new SettingUtil(getActivity());

        ((EditText) getView().findViewById(R.id.setting_edittext_username)).setText(settingUtil.getUsername());
        ((EditText) getView().findViewById(R.id.setting_edittext_password)).setText(settingUtil.getPassword());
    }
}
