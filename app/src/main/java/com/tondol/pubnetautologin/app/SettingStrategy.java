package com.tondol.pubnetautologin.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hosaka on 2014/05/28.
 */
public abstract class SettingStrategy {

    private Context context;

    public SettingStrategy(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    abstract public void setUsername(String username);
    abstract public void setPassword(String password);

    abstract public String getUsername();
    abstract public String getPassword();
}
