package com.tondol.pubnetautologin.app;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by hosaka on 2014/05/28.
 */
public abstract class LoginStrategy {

    private Listener listener;
    private RequestQueue queue;

    public static enum RequestType {
        Login,
        Logout
    }

    public LoginStrategy(Context context, Listener listener) {
        queue = Volley.newRequestQueue(context);
//        queue = Volley.newRequestQueue(context, new MockHttpStack(200, "OK", "<title>Logged In</title>"));
        this.listener = listener;
    }

    public static interface Listener {
        public void onResponse(LoginStrategy loginUtil, RequestType type, String response);
        public void onErrorResponse(LoginStrategy loginUtil, RequestType type, Exception e);
    }

    public void start() {
        queue.start();
    }
    public void stop() {
        queue.stop();
    }
    public void login(String username, String password) {
        queue.add(getLoginRequest(username, password));
    }
    public void logout() {
        queue.add(getLogoutRequest());
    }

    abstract protected StringRequest getLoginRequest(String username, String password);
    abstract protected StringRequest getLogoutRequest();

    protected Listener getListener() {
        return listener;
    }
}
