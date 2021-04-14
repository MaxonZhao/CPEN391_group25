package com.cpen391.flappyVoiceRecording;

public class World {

    public static float dbCount = 40;

    private static float lastDbCount = dbCount;
    private static float min = 0.5f;
    private static float value = 0;

    public static double setDbCount(float dbValue) {
        if (dbValue > lastDbCount) {
            value = dbValue - lastDbCount > min ? dbValue - lastDbCount : min;
        } else {
            value = dbValue - lastDbCount < -min ? dbValue - lastDbCount : -min;
        }
        dbCount = lastDbCount + value * 0.2f;
        lastDbCount = dbCount;
        return lastDbCount;
    }
}
