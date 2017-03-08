package com.dscientia.global;

import android.app.Application;

import com.dscientia.bean.User;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class MyApplication extends Application {

    private ActivityCollector activityCollector;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        activityCollector = new ActivityCollector();
    }

    public ActivityCollector getActivityCollector() {
        return activityCollector;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
