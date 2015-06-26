package com.tondol.pubnetautologin.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hosaka on 2015/06/26.
 */
public class PreferenceSettingStrategy extends SettingStrategy {
    private static final String USERNAME_FIELD = "com.tondol.pubnetautologin.username";
    private static final String PASSWORD_FIELD = "com.tondol.pubnetautologin.password";

    public PreferenceSettingStrategy(Context context) {
        super(context);
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(USERNAME_FIELD, username);
        editor.commit();
    }
    public void setPassword(String password) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PASSWORD_FIELD, password);
        editor.commit();
    }

    public String getUsername() {
        return getSharedPreferences().getString(USERNAME_FIELD, null);
    }
    public String getPassword() {
        return getSharedPreferences().getString(PASSWORD_FIELD, null);
    }

}
