package com.tondol.pubnetautologin.app;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hosaka on 2014/07/10.
 */
public class MockHttpStack implements HttpStack {
    private int status;
    private String reason;
    private String body;

    public MockHttpStack(int status, String reason, String body) {
        this.status = status;
        this.reason = reason;
        this.body = body;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, status, reason));
        response.setEntity(new StringEntity(body));
        List<Header> headers = new ArrayList<Header>();
        response.setHeaders(headers.toArray(new Header[headers.size()]));
        return response;
    }
}
