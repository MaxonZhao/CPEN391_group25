package com.cpen391.flappyVoiceRecording.util;

public class DbUtil {

    public static float dbCount = 40;

    private static float lastDbCount = dbCount;

    public static double setDbCount(float dbValue) {
        float value;
        float min = 0.5f;
        if (dbValue > lastDbCount) {
            value = Math.max(dbValue - lastDbCount, min);
        } else {
            value = Math.min(dbValue - lastDbCount, -min);
        }
        dbCount = lastDbCount + value * 0.2f;
        lastDbCount = dbCount;
        return lastDbCount;
    }
}
