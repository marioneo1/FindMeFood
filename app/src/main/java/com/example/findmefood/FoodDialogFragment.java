package com.example.findmefood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.findmefood.utility.TextToSpeechHandler;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class FoodDialogFragment extends DialogFragment {
    private static final int REQUEST_CODE = 10;


    public interface OnInputSelected{
        void yes(String category, Boolean blacklisted, String title);
        void no(String category, Boolean blacklisted, String title);
    }

    private static String TAG = DialogFragment.class.getName();
    public OnInputSelected mOnInputSelected;
    private TextView mCategory;
    private TextView mActionYes, mActionNo;
    private ToggleButton blacklistToggle;
    private float x1,x2;
    private static Boolean blacklist_toggled = false;
    static final int MIN_DISTANCE = 150;
    static final int MAX_OFFPATH = 250;
    static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ffood_dialogfrag,container,false);
        Bundle mArgs = getArguments();
        final String title = mArgs.getString("title");
        final String category = mArgs.getString("category");
        mActionYes = view.findViewById(R.id.action_yes);
        mActionNo = view.findViewById(R.id.action_no);
        mCategory = view.findViewById(R.id.category);
        blacklistToggle = view.findViewById(R.id.toggleButton);
        mCategory.setText(title);
        TextToSpeechHandler ttsh = new TextToSpeechHandler(getContext(), title);
        ttsh.speak();

        blacklistToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    blacklist_toggled = true;
                }
                else{
                    blacklist_toggled = false;
                }
            }
        });

        mActionNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "No clicked" );
                mOnInputSelected.no(category, blacklist_toggled, title);
                getDialog().dismiss();
            }
        });

        mActionYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "Yes clicked" );
                Log.d(TAG, "Category : " + category);
                mOnInputSelected.yes(category, blacklist_toggled, title);
                getDialog().dismiss();
            }
        });

        return view;
    }

//    public void getSpeechInput(){
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
//            startActivityForResult(intent, REQUEST_CODE);
//        }
//        else{
//            Toast.makeText(getContext(), "Your device doesn't support speech input", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Log.d(TAG, "Voice " + result.get(0));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }
        catch (ClassCastException e){
            Log.e(TAG, "OnAttach:Class Exception");
        }
    }
}
