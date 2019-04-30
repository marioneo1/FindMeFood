package com.example.findmefood.utility;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import java.util.Locale;

public class TextToSpeechHandler{
    private static final String TAG = TextToSpeechHandler.class.getName();
    private Context mContext;
    private TextToSpeech textToSpeech;
    private String mTextToSpeak;

    public TextToSpeechHandler(Context context, String textToSpeak){
        mContext = context;
        mTextToSpeak = textToSpeak;
        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e(TAG, "Text to speech: Language not supported");
                    }
                    else{
                        speak();
                    }
                }
                else{
                    Log.e(TAG, "Text to speech: Init failed");
                }
            }
        });
    }

    public void speak(){
        String utteranceId = this.hashCode() + "";
        Log.d(TAG,"Utterance id:" + utteranceId);
        textToSpeech.speak(mTextToSpeak,TextToSpeech.QUEUE_FLUSH,null, utteranceId);
    }
}
