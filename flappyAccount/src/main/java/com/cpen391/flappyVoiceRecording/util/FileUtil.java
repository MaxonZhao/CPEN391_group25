package com.cpen391.flappyVoiceRecording.util;

import com.cpen391.appbase.application.FlappyBirdApp;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static final String filePath = FlappyBirdApp.getApplication().getBaseContext().getFilesDir().getPath() + File.separator;
    public static final String path = filePath + "SoundMeter" + File.separator;

    static {
        File dirRootFile = new File(filePath);
        if (!dirRootFile.exists()) {
            dirRootFile.mkdirs();
        }
        File recFile = new File(path);
        if (!recFile.exists()) {
            recFile.mkdirs();
        }
    }

    public static File createFile(String fileName) {

        File file = new File(path + fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


}
