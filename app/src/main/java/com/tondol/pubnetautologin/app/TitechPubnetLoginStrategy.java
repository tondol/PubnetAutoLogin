package com.tondol.pubnetautologin.app;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hosaka on 2015/06/26.
 */
public class TitechPubnetLoginStrategy extends LoginStrategy {

    public TitechPubnetLoginStrategy(Context context, LoginStrategy.Listener listener) {
        super(context, listener);
    }

    @Override
    protected StringRequest getLoginRequest(final String username, final String password) {
        return new StringRequest(Request.Method.POST, "https://wlanauth.noc.titech.ac.jp/login.html", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.indexOf("<title>Logged In</title>") < 0) {
                    getListener().onErrorResponse(TitechPubnetLoginStrategy.this, RequestType.Login, new RuntimeException("login error"));
                } else {
                    android.util.Log.d("pubnetautologin", response);
                    getListener().onResponse(TitechPubnetLoginStrategy.this, RequestType.Login, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getListener().onErrorResponse(TitechPubnetLoginStrategy.this, RequestType.Login, error);
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
    @Override
    protected StringRequest getLogoutRequest() {
        return new StringRequest(Request.Method.POST, "https://wlanauth.noc.titech.ac.jp/logout.html", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.indexOf("<title>Web Authentication</title>") < 0) {
                    getListener().onErrorResponse(TitechPubnetLoginStrategy.this, RequestType.Logout, new RuntimeException("logout error"));
                } else {
                    getListener().onResponse(TitechPubnetLoginStrategy.this, RequestType.Logout, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getListener().onErrorResponse(TitechPubnetLoginStrategy.this, RequestType.Logout, error);
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
