package com.cpen391.flappyaccount;

import android.app.Activity;

import androidx.collection.ArraySet;

import java.util.ArrayList;
import java.util.List;

public class ActivityHolder {
    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity act) {
        activities.add(act);
    }

    public static void clearActivities() {
        activities.clear();
    }

    public static void destroyAllActivities() {
        for(Activity act : activities) {
            act.finish();
        }

        clearActivities();
    }
}
