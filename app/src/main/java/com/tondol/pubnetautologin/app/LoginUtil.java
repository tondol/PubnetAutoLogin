package com.tondol.pubnetautologin.app;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.impl.client.DefaultHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hosaka on 2014/05/28.
 */
public class LoginUtil {
    private Listener listener;
    private RequestQueue queue;

    public static enum RequestType {
        Login,
        Logout
    }

    public LoginUtil(Context context, Listener listener) {
        queue = Volley.newRequestQueue(context);
//        queue = Volley.newRequestQueue(context, new MockHttpStack(200, "OK", "<title>Logged In</title>"));
        this.listener = listener;
    }

    public static interface Listener {
        public void onResponse(LoginUtil loginUtil, RequestType type, String response);
        public void onErrorResponse(LoginUtil loginUtil, RequestType type, Exception e);
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

    private StringRequest getLoginRequest(final String username, final String password) {
        return new StringRequest(Request.Method.POST, "https://wlanauth.noc.titech.ac.jp/login.html", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.indexOf("<title>Logged In</title>") < 0) {
                    listener.onErrorResponse(LoginUtil.this, RequestType.Login, new RuntimeException("login error"));
                } else {
                    listener.onResponse(LoginUtil.this, RequestType.Login, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse(LoginUtil.this, RequestType.Login, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("buttonClicked", "4");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
    }
    private StringRequest getLogoutRequest() {
        return new StringRequest(Request.Method.POST, "https://wlanauth.noc.titech.ac.jp/logout.html", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.indexOf("<title>Web Authentication</title>") < 0) {
                    listener.onErrorResponse(LoginUtil.this, RequestType.Logout, new RuntimeException("logout error"));
                } else {
                    listener.onResponse(LoginUtil.this, RequestType.Logout, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse(LoginUtil.this, RequestType.Logout, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("userStatus", "1");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
    }
}
