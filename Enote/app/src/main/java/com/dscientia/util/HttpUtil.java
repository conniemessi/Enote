package com.dscientia.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class HttpUtil extends Thread {

    public static final String TAG = "HttpUtil";

    public static final int GET = 1;
    public static final int POST = 2;

    private HttpClient httpClient;
    private String url;
    private int method;
    private List<NameValuePair> params;
    private HttpCallBallListener listener;
    private HttpHandler httpHandler;
    private int timeout = 6000;

    public HttpUtil() {
        params = new ArrayList<NameValuePair>();
        httpHandler = new HttpHandler();
    }

    public void create(int method, String url, List<NameValuePair> params, HttpCallBallListener listener) {
        if (method != GET && method != POST) {
            throw new IllegalStateException("param method is illegal");
        }
        this.method = method;
        this.url = url;
        this.params = params;
        this.listener = listener;
        if (listener != null) {
            listener.onStart();
        }
        this.start();
    }


    @Override
    public void run() {
        Log.i(TAG, "Url============>" + url);
        httpClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), timeout);
        try {
            HttpResponse response = null;
            switch (method) {
                case GET:
                    response = httpClient.execute(new HttpGet(url));
                    break;
                case POST:
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    response = httpClient.execute(httpPost);
                    break;
            }
            int httpStatus = response.getStatusLine().getStatusCode();
            Log.i(TAG, "HttpStatus======>" + httpStatus);
            if (httpStatus == HttpStatus.SC_OK) {
                StringBuilder result = new StringBuilder();
                result.append(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
                Log.i(TAG, "entity========>" + result.toString());
                httpHandler.sendMessage(Message.obtain(httpHandler, 0, result.toString()));
            } else {
                httpHandler.sendMessage(Message.obtain(httpHandler, 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpHandler.sendMessage(Message.obtain(httpHandler, 1));
        }
    }

    class HttpHandler extends Handler {
        // 0代表成功，1代表失败
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (listener != null) {
                        listener.onFinish();
                        listener.onSuccess((String) msg.obj);
                    }
                    break;
                case 1:
                    if (listener != null) {
                        listener.onFinish();
                        listener.onError();
                    }
                    break;
            }
        }
    }

    public interface HttpCallBallListener {
        public void onStart();
        public void onFinish();
        public void onError();
        public void onSuccess(String result);
    }


}
