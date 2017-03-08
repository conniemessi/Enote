package com.dscientia.global;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class BaseActivity extends Activity {


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
