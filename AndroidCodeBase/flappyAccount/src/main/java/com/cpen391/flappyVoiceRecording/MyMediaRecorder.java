package com.cpen391.flappyVoiceRecording;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

/**
 *  MyMediaRecorder
 *
 *  @note: Recording logic settings and configuration
 *
 *  @author Robin Lai
 */

public class MyMediaRecorder {
    public File audioFile;
    private MediaRecorder recorder;
    public boolean isRecording = false;

    public float getMaxAmplitude() {
        if (recorder != null) {
            try {
                return recorder.getMaxAmplitude();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 5;
        }
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    public boolean startRecorder() {
        if (audioFile == null) {
            return false;
        }
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(audioFile.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            isRecording = true;
            return true;
        } catch (IOException exception) {
            recorder.reset();
            recorder.release();
            recorder = null;
            isRecording = false;
            exception.printStackTrace();
            exception.printStackTrace();
        } catch (IllegalStateException e) {
            stopRecording();
            e.printStackTrace();
            isRecording = false;
        }
        return false;
    }

    public void stopRecording() {
        if (recorder != null) {
            if (isRecording) {
                try {
                    recorder.stop();
                    recorder.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            recorder = null;
            isRecording = false;
        }
    }

    public void delete() {
        stopRecording();
        if (audioFile != null) {
            audioFile.delete();
            audioFile = null;
        }
    }
}
