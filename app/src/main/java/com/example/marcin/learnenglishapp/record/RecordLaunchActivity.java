package com.example.marcin.learnenglishapp.record;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.marcin.learnenglishapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class RecordLaunchActivity extends AppCompatActivity {

    private ConstraintLayout parent;
    private Button record, stop, btnPlay, btnStopPlay;
    private TextView status;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;


    private static String fileName = null;
    public static final int RECORD_AUDIO_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        //set views id
        parent = findViewById(R.id.parent);
        status = findViewById(R.id.status);
        record = findViewById(R.id.record);
        stop = findViewById(R.id.stop);
        btnPlay = findViewById(R.id.btn_play_rec);
        btnStopPlay = findViewById(R.id.btn_stop_playing);

        //start audio recording
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start audio recording.
                startRecording();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopRecording();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // play the recorded audio
                playAudio();
            }
        });
        btnStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stop playing the recorded audio
                stopPlaying();
            }
        });

    }

    private void playAudio() {
        // using media player class for playing recorded audio
        mediaPlayer = new MediaPlayer();
        try {

            // set the data source which will be our file name
            mediaPlayer.setDataSource(fileName);

            //prepare media player
            mediaPlayer.prepare();

            // start media player.
            mediaPlayer.start();
            status.setText("Recording Started Playing");

        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    private void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
        status.setText("Recording Play Stopped");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //ta metoda jest wywoływana, gdy użytkownik chce
        //udzielić zgody na nagrywanie dźwięku.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RECORD_AUDIO_PERMISSION:
                if (grantResults.length > 0) {
                    boolean recordPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (recordPermission && storePermission) {
                        Toast.makeText(getApplicationContext(), "Pozwolenie przyznane", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Pozwolenie oddalone", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }



    private void startRecording() {
        // sprawdza metodę pozwolenia czu użytkownik ma pozwolenie na nagrywanie i przechowywanie dzwięku
        if(CheckPermissions()){

            //inicjalizuję zmienną pliku ze ścieżka nagrywanego pliku audio
            fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            fileName += "/audioRecording.3gp";

            //inicializacja klasy media recorder
            mediaRecorder = new MediaRecorder();
            //ustawia żródło dzwięku aby było używane do nagrywania
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //wybierz format wyjściowy dla dzwięku
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //wybierz encoder dzwięku dla nagranego dzwięku
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //wybierz lokację pliku wyjściowego dla nagranego audio
            mediaRecorder.setOutputFile(fileName);
            try{
                //przygotowuje rejestr aby rozpoczął nagrywanie i kodowanie danych
                mediaRecorder.prepare();
            } catch (IOException e){
                Log.e("TAG", "prepare() failed");

            }
            //zacznij nagrywanie audio
            mediaRecorder.start();
            status.setText("Nagrywam dzwięk");
            startRecording();
        }else{
            //gdy pozwolenie na nagrywanie nie jest przydzielone
            // przez użytkownika ta metoda poprośi o pozwolenie na działanie mikrofonu i pamięci.
            RequestPermission();
        }
    }

    private void stopRecording() {

        //zatrzymaj nagrywanie dzwięku
        mediaRecorder.stop();

        //uwolnij obiekt rejestratora dzwięku
        mediaRecorder.release();
        mediaRecorder = null;

        status.setText("Nagrywanie zatrzymane");
        showSnackBar();
    }



    //ta metoda jest stosowana do sprawdzania pozwoleń
    private boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    //ta metoda jest używana do żądania
    //pozwolenie na nagrywanie i przechowywanie dźwięku.
    private void RequestPermission() {
        ActivityCompat.requestPermissions(RecordLaunchActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, RECORD_AUDIO_PERMISSION);
    }

    private void showSnackBar(){
        Snackbar.make(parent,"Dźwięk jest nagrywany",Snackbar.LENGTH_INDEFINITE)
                .setAction("Zamknij", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RecordLaunchActivity.this, "Zamknij przycisnięte", Toast.LENGTH_SHORT).show();
                    }
                })
                .setActionTextColor(Color.CYAN)
        .show();

    }

}