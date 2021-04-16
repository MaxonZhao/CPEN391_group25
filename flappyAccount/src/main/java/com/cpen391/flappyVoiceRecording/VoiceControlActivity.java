package com.cpen391.flappyVoiceRecording;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cpen391.flappyUI.EndGamePointActivity;
import com.cpen391.flappyVoiceRecording.util.FileUtil;
import com.cpen391.flappyVoiceRecording.util.DbUtil;
import com.cpen391.flappyVoiceRecording.view.SoundDiscView;
import com.cpen391.flappyaccount.R;
import com.cpen391.flappybluetooth.activity.BluetoothConnectionService;
import com.cpen391.flappybluetooth.util.BluetoothConnectionUtil;

import java.io.File;

import timber.log.Timber;

/**
 *  VoiceControlActivity
 *
 *  @note: Main logic of voice control, send JUMP("1") message to RFS board
 *  when db is greater than threshold value
 *
 *  @author Robin Lai
 */
public class VoiceControlActivity extends AppCompatActivity {
    private final Context context = this;
    float volume = 0;
    private SoundDiscView soundDiscView;
    private MyMediaRecorder recorder;
    private static final int myMsg = 0x1001;
    private static final int refreshTime = 100;
    public double threshold = 60.0;
    public double currentDb = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
        recorder = new MyMediaRecorder();
        initObserver();
    }

    private void initObserver() {
        BluetoothConnectionService.ended.observe(this, integer -> {
            Intent endGame = new Intent(context, EndGamePointActivity.class);
            endGame.putExtra("game_score", BluetoothConnectionService.ended.getValue());
            startActivity(endGame);
            finish();
        });
    }


    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.hasMessages(myMsg)) {
                return;
            }
            volume = recorder.getMaxAmplitude();
            if (volume > 0 && volume < 1000000) {
                currentDb = DbUtil.setDbCount(20 * (float) (Math.log10(volume)));
                if (currentDb > threshold) {
                    BluetoothConnectionUtil.getInstance().sendMessage(context, "1");
                    Timber.d("++++++++++++");
                    Timber.d("1");
                    Timber.d("++++++++++++");
                }
                soundDiscView.refresh();
            }
            handler.sendEmptyMessageDelayed(myMsg, refreshTime);
        }
    };

    private void startListenAudio() {
        handler.sendEmptyMessageDelayed(myMsg, refreshTime);
    }

    public void startRecord(File file) {
        try {
            recorder.setAudioFile(file);
            if (recorder.startRecorder()) {
                startListenAudio();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        soundDiscView = findViewById(R.id.soundDiscView);
        File file = FileUtil.createFile("temp.amr");
        if (file != null) {
            startRecord(file);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        recorder.delete();
        handler.removeMessages(myMsg);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(myMsg);
        recorder.delete();
        super.onDestroy();
    }
}
