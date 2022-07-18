package com.example.marcin.learnenglishapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

public class RangeActib extends AppCompatActivity {
    RangeSeekBar<Integer> mRangeSeekBar;
    MediaPlayer mMediaPlayer;
    Button playButton;
    TextView mEndTextView;
    static int max;
    SeekBar mSeekBar;

    Runnable mRunnable;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_range);

        mRangeSeekBar = findViewById(R.id.rangeSeekBar);
        mSeekBar = findViewById(R.id.seekBar);
        mRangeSeekBar.setNotifyWhileDragging(true);
        playButton = findViewById(R.id.play);
        mEndTextView = findViewById(R.id.end_duration);
        mHandler = new Handler();

        mMediaPlayer = MediaPlayer.create(this, R.raw.song);
        max = mMediaPlayer.getDuration();

        mRangeSeekBar.setRangeValues(0, mMediaPlayer.getDuration());
        mSeekBar.setMax(mMediaPlayer.getDuration());

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();
                playProgress();
            }
        });

        mRangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                mMediaPlayer.seekTo(minValue);
                max = maxValue;

            }
        });
    }

    private void playProgress() {
        if (mMediaPlayer.getCurrentPosition() == max) {
            mMediaPlayer.stop();
        }
        if (mMediaPlayer.isPlaying()) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    playProgress();
                }
            };
            mHandler.postDelayed(mRunnable, 0);
        }
    }


}