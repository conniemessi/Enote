package com.dscientia.global;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class BaseFragActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication)getApplication()).getActivityCollector().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication)getApplication()).getActivityCollector().removeActivity(this);
    }
}
