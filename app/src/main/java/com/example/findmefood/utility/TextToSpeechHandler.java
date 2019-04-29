package com.example.findmefood.utility;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;

import static com.example.findmefood.MainActivity.TAG;


public class TextToSpeechHandler {
    private Context mContext;
    private TextToSpeech textToSpeech;
    private String mTextToSpeak;

    public TextToSpeechHandler(Context context, String textToSpeak){
        mContext = context;
        mTextToSpeak = textToSpeak;
    }

    public void speak(){
        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e(TAG, "Text to speech: Language not supported");
                    }
                    else{
                        String utteranceId = this.hashCode() + "";
                        textToSpeech.speak(mTextToSpeak,TextToSpeech.QUEUE_FLUSH,null, utteranceId);
                    }
                }
                else{
                    Log.e(TAG, "Text to speech: Init failed");
                }

            }
        });
    }
}
