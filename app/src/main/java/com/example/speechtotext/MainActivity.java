package com.example.speechtotext;

import android.Manifest;
import android.arch.core.util.Function;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    private TextView returnedText;
    ImageButton recordbtn;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    static final int REQUEST_PERMISSION_KEY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        returnedText = findViewById(R.id.textview1);
        progressBar = findViewById(R.id.progressBar1);
        recordbtn = findViewById(R.id.mainButton);

        String[] PERMISSION = {Manifest.permission.RECORD_AUDIO};
        if (!Function.hasPermission(this, PERMISSION)){
            ActivityCompat.requestPermissions(this, PERMISSION, REQUEST_PERMISSION_KEY);
        }

        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
        recognizerIntent.putExtra("android.speech.extra.DICTAION_MODE", true);

        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                speech.startListening(recognizerIntent);
                recordbtn.setEnabled(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null){
            speech.destroy();
            Log.d("Log","destroy");
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d("Log", "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("Log","onBeginningOgSpeech");
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d("Log", "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int)rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d("Log", "onBufferReceiverd "+ buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.d("Log", "onEndOfSpeech");
        progressBar.setVisibility(View.VISIBLE);
        recordbtn.setEnabled(true);

    }

    @Override
    public void onError(int error) {
        String errorMessage = getErrorText(errorCode);
        Log.d("Log", "FAILED" + errorMessage);
        progressBar.setVisibility(View.INVISIBLE);
        returnedText.setText(errorMessage);
        recordbtn.setEnabled(true);
    }

    @Override
    public void onResults(Bundle results) {
        Log.d("Log", "onResults")
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d("Log", "onPartialResults");
        ArrayList<String> matches = arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        text = matches.get(0);
        returnedText.setText(text);
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d("Log", "onEvent");
    }

    public static String getErrorText(int errorCode){
        String message;
        switch (errorCode){
            case SpeechRecognizer.ERROR_AUDIO;
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client error side";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Netwotk timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "recognizer is busy";
                break;
            case SpeechRecognizer.ERROR_SERVER;
                message = " server error";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "no speech input";
                break;
            default:
                message = "Didn't understand, please try again";
                break;
        }
        return message;
    }
}
