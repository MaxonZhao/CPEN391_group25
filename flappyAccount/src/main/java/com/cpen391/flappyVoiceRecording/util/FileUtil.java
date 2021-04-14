package com.cpen391.flappyVoiceRecording.util;

import android.os.Environment;

import com.cpen391.appbase.application.FlappyBirdApp;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static final String LOCAL = "SoundMeter";

    public static final String LOCAL_PATH = FlappyBirdApp.getApplication().getBaseContext().getFilesDir().getPath() + File.separator;

    public static final String REC_PATH = LOCAL_PATH + LOCAL + File.separator;

    static {
        File dirRootFile = new File(LOCAL_PATH);
        if (!dirRootFile.exists()) {
            dirRootFile.mkdirs();
        }
        File recFile = new File(REC_PATH);
        if (!recFile.exists()) {
            recFile.mkdirs();
        }
    }

    private FileUtil() {
    }

    public static boolean isExitSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    private static boolean hasFile(String fileName) {
        File f = createFile(fileName);
        return null != f && f.exists();
    }

    public static File createFile(String fileName) {

        File myCaptureFile = new File(REC_PATH + fileName);
        if (myCaptureFile.exists()) {
            myCaptureFile.delete();
        }
        try {
            myCaptureFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }


}
