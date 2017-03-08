package com.dscientia;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.dscientia.bean.Note;
import com.dscientia.bean.User;
import com.dscientia.global.BaseActivity;
import com.dscientia.global.Conf;
import com.dscientia.global.Constant;
import com.dscientia.global.MyApplication;
import com.dscientia.util.HttpUtil;

import java.util.ArrayList;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class WebActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "WebActivity";

    private WebView webView;
    private Note note;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        initData();
        initWebview();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        note = (Note) getIntent().getSerializableExtra("note");
    }

    private void initWebview() {
        webView.setFocusable(true);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.requestFocusFromTouch();
        webView.setVerticalScrollBarEnabled(true);
        WebSettings settings = webView.getSettings();
        settings.setSupportMultipleWindows(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(note.getDetailUrl());
        Log.i(TAG, "url===============>" + note.getDetailUrl());
    }

    @Override
    public void onClick(View v) {
        if (Conf.isLogin == false) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
        }
    }
}
