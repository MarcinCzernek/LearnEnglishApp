package com.example.marcin.learnenglishapp.player;



import static android.media.MediaPlayer.SEEK_CLOSEST;
import static android.media.MediaPlayer.SEEK_CLOSEST_SYNC;
import static android.media.MediaPlayer.SEEK_PREVIOUS_SYNC;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.marcin.learnenglishapp.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.text.BreakIterator;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {


    private TextView textMaxTime;
    private TextView textCurrentPosition;
    private Button buttonPause;
    private Button buttonStart;
    private Button buttonRewind;
    private Button buttonFastForward;
    private Button buttonRepeat;
    private Button buttonShuffle;
    private Button buttonBeginning;
    private Button buttonEnd;
    private Button open;


    private Button buttonSelect;

    private RangeSeekBar rangeSeekBar;
    private SeekBar seekBar;
    private Handler threadHandler = new Handler();
    private MediaPlayer mediaPlayer;
    private boolean isRepeat = false;
    private boolean isShuffle = false;
    boolean isStart = false;
    boolean isEnd = false;
    public static final int PICK_FILE =99;

    boolean isNotLineless; // Set this in constructor or a setter
    boolean isNotThumbless; // Set this in constructor or a setter



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        this.textCurrentPosition = (TextView)this.findViewById(R.id.textView_currentPosition);
        this.textMaxTime=(TextView) this.findViewById(R.id.textView_maxTime);

        this.buttonSelect = (Button) this.findViewById(R.id.button_select);

        this.buttonStart= (Button) this.findViewById(R.id.button_start);
        this.buttonPause= (Button) this.findViewById(R.id.button_pause);
        this.buttonRewind= (Button) this.findViewById(R.id.button_rewind);
        this.buttonFastForward= (Button) this.findViewById(R.id.button_fastForward);
        this.buttonRepeat=(Button) this.findViewById(R.id.buttonRepeat);
        this.buttonShuffle=(Button) this.findViewById(R.id.buttonShuffle);
        this.open=(Button) this.findViewById(R.id.open);

        this.buttonBeginning=(Button) this.findViewById(R.id.buttonBeginning);
        this.buttonEnd=(Button) this.findViewById(R.id.buttonEnd);


        this.buttonStart.setEnabled(false);
        this.buttonPause.setEnabled(false);
        this.buttonRewind.setEnabled(false);
        this.buttonFastForward.setEnabled(false);


        this.seekBar= (SeekBar) this.findViewById(R.id.seekBar);
        this.seekBar.setClickable(false);

        this.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Select new media source.
                selectMediaResource();
            }
        });

        this.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("audio/*");
                startActivityForResult(intent, PICK_FILE);
            }
        });

        this.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStart( );
            }
        });
        this.buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPause( );
            }
        });
        this.buttonRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRewind( );
            }
        });
        this.buttonFastForward .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFastForward( );
            }
        });

        this.buttonRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doRepeat();
            }
        });
        this.buttonBeginning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int onStart = mediaPlayer.getCurrentPosition();
               int mode = SEEK_CLOSEST_SYNC;
         seekTo(onStart,mode );
            }
        });


        // Create MediaPlayer.
        this.mediaPlayer=  new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                doStop(); // Stop current media.
            }
        });
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isRepeat == true){
                    doStart();
                }else{
                    doComplete();
                }
            }
        });



    }



    // When user click "Select Media Source" button.
    private void selectMediaResource()  {
        // this.selectRawMediaSource();
        // this.selectURLMediaSource();
        //this.selectLocalMediaSource();
        this.selectRawMediaSource();
    }

    private void selectRawMediaSource() {
        // "mysong.mp3" ==> resName = "mysong".
        String resName = PlayerFunctions.RAW_MEDIA_SAMPLE;
        PlayerFunctions.playRawMedia(this, this.mediaPlayer, resName);
    }

    private void selectURLMediaSource()  {
        // http://example.coom/mysong.mp3
        String mediaURL = PlayerFunctions.URL_MEDIA_SAMPLE;
        PlayerFunctions.playURLMedia(this, this.mediaPlayer, mediaURL);
    }

    private void selectLocalMediaSource()  {
        // @localPath = "/storage/emulated/0/DCIM/Music/";
        // @localPath = "/storage/emulated/0/DCIM/Music/mysong.mp3"; (For example).
        String localPath = PlayerFunctions.LOCAL_MEDIA_SAMPLE;
        PlayerFunctions.playLocalMedia(this, this.mediaPlayer, localPath);
    }

    // Convert millisecond to string.
    private String millisecondsToString(int milliseconds)  {
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds =  TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) ;
        return minutes + ":"+ seconds;
    }


    private void doStart( )  {
        if(this.mediaPlayer.isPlaying()) {
            return;
        }
        // The duration in milliseconds
        int duration = this.mediaPlayer.getDuration();

        int currentPosition = this.mediaPlayer.getCurrentPosition();
        if(currentPosition== 0)  {
            this.seekBar.setMax(duration);
            String maxTimeString = this.millisecondsToString(duration);
            this.textMaxTime.setText(maxTimeString);
        } else if(currentPosition== duration)  {
            // Resets the MediaPlayer to its uninitialized state.
            this.mediaPlayer.reset();
        }
        this.mediaPlayer.start();
        // Create a thread to update position of SeekBar.
        UpdateSeekBarThread updateSeekBarThread= new UpdateSeekBarThread();
        threadHandler.postDelayed(updateSeekBarThread,50);

        this.buttonPause.setEnabled(true);
        this.buttonStart.setEnabled(false);
        this.buttonRewind.setEnabled(true);
        this.buttonFastForward.setEnabled(true);

    }

    // Called by MediaPlayer.OnCompletionListener
    // When Player cocmplete
    private void doComplete()  {
        buttonStart.setEnabled(true);
        buttonPause.setEnabled(false);
        buttonRewind.setEnabled(true);
        buttonFastForward.setEnabled(false);
    }

    // Called by MediaPlayer.OnPreparedListener.
    // When user select a new media source, then stop current.
    private void doStop()  {
        if(this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
        }
        buttonStart.setEnabled(true);
        buttonPause.setEnabled(false);
        buttonRewind.setEnabled(false);
        buttonFastForward.setEnabled(false);
    }

    // When user click to "Pause".
    private void doPause( )  {
        this.mediaPlayer.pause();
        this.buttonPause.setEnabled(false);
        this.buttonStart.setEnabled(true);
    }

    private void  doRepeat() {
        if(isRepeat){
            isRepeat = false;
            Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
        }else{
            // make repeat to true
            isRepeat = true;
            Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
            // make shuffle to false
            isShuffle = false;
        }
    }

    // When user click to "Rewind".
    private void doRewind( )  {
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        int duration = this.mediaPlayer.getDuration();
        // 5 seconds.
        int SUBTRACT_TIME = 5000;

        if(currentPosition - SUBTRACT_TIME > 0 )  {
            this.mediaPlayer.seekTo(currentPosition - SUBTRACT_TIME);
        }
        this.buttonFastForward.setEnabled(true);
    }

    // When user click to "Fast-Forward".
    private void doFastForward( )  {
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        int duration = this.mediaPlayer.getDuration();
        // 5 seconds.
        int ADD_TIME = 5000;

        if(currentPosition + ADD_TIME < duration)  {
            this.mediaPlayer.seekTo(currentPosition + ADD_TIME);
        }
    }


    @SuppressLint("Range")
    public String getNameFromUri(Uri uri){
        String fileName = "";
        Cursor cursor = null;
        cursor = getContentResolver().query(uri, new String[]{
                MediaStore.Images.ImageColumns.DISPLAY_NAME
        }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
        }
        if (cursor != null) {
            cursor.close();
        }
        return fileName;
    }

    // Thread to Update position for SeekBar.
    class UpdateSeekBarThread implements Runnable {

        private Button start;
        private Button end;



        public void run()  {
            int currentPosition = mediaPlayer.getCurrentPosition();
            String currentPositionStr = millisecondsToString(currentPosition);
            textCurrentPosition.setText(currentPositionStr);

            seekBar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 50);
        }

        public void startMethod(){
            int currentPosition = mediaPlayer.getCurrentPosition();
            int start = currentPosition;
            if (isStart == true){
                start = currentPosition;
            }
            int duration = mediaPlayer.getDuration();
            String currentPositionStr = millisecondsToString(currentPosition);
            textCurrentPosition.setText(currentPositionStr);
            seekBar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 50);
        }

        public void endMethod(){
            int currentPosition = mediaPlayer.getCurrentPosition();
            int end = currentPosition;
            if(isEnd == true){
                end = currentPosition;
            }
            int duration = mediaPlayer.getDuration();
            String currentPositionStr = millisecondsToString(currentPosition);
            textCurrentPosition.setText(currentPositionStr);
            seekBar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 50);
        }

    }

    public void seekTo (long msec, int mode){
        msec = mediaPlayer.getCurrentPosition();
        mode = SEEK_PREVIOUS_SYNC;
    }


}
