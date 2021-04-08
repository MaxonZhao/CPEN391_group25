package com.cpen391.flappyVoiceRecording;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;

import com.cpen391.flappyUI.EndGamePointActivity;
import com.cpen391.flappyaccount.R;
import com.cpen391.flappybluetooth.activity.BluetoothConnectionService;
import com.cpen391.flappybluetooth.util.BluetoothConnectionUtil;

import java.io.File;

import timber.log.Timber;


public class VoiceControlActivity extends AppCompatActivity {
    float volume = 10000;
    private SoundDiscView soundDiscView;
    private MyMediaRecorder mRecorder;
    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;
    private final Context context = this;
    private static final int GET_RECODE_AUDIO = 1;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public double currentDb = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        mRecorder = new MyMediaRecorder();
        initObserver();
    }

    private void initObserver() {
        BluetoothConnectionService.ended.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Intent endGame = new Intent(context, EndGamePointActivity.class);
                endGame.putExtra("game_score", BluetoothConnectionService.ended.getValue());
                startActivity(endGame);
                finish();
            }
        });
    }


    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.hasMessages(msgWhat)) {
                return;
            }
            volume = mRecorder.getMaxAmplitude();
            if (volume > 0 && volume < 1000000) {
                currentDb = World.setDbCount(20 * (float) (Math.log10(volume)));
                if (currentDb > 60) {
                    BluetoothConnectionUtil.getInstance().sendMessage(context, "1");
                    Timber.d("++++++++++++");
                    Timber.d("1");
                    Timber.d("++++++++++++");
                }
                soundDiscView.refresh();
            }
            handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
        }
    };

    private void startListenAudio() {
        handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
    }

    public void startRecord(File fFile) {
        try {
            mRecorder.setMyRecAudioFile(fFile);
            if (mRecorder.startRecorder()) {
                startListenAudio();
            } else {
                Toast.makeText(this, "Fail to record", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        soundDiscView = (SoundDiscView) findViewById(R.id.soundDiscView);
        File file = FileUtil.createFile("temp.amr");
        if (file != null) {
            Timber.tag("file").v("file =%s", file.getAbsolutePath());
            startRecord(file);
        } else {
            Toast.makeText(getApplicationContext(), "Fail to create new folder", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecorder.delete();
        handler.removeMessages(msgWhat);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(msgWhat);
        mRecorder.delete();
        super.onDestroy();
    }
}
