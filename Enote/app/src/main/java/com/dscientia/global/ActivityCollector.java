package com.dscientia.global;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class ActivityCollector {

    private List<Activity> activities;

    public ActivityCollector() {
        activities = new ArrayList<Activity>();
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public void finishAll() {
        for (Activity a : activities) {
            a.finish();
        }
    }
}
