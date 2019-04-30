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
import android.widget.TextView;
import android.widget.Toast;

import com.example.findmefood.utility.TextToSpeechHandler;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class FoodDialogFragment extends DialogFragment {
    private static final int REQUEST_CODE = 10;


    public interface OnInputSelected{
        void yes(String input);
        void no();
    }

    private static String TAG = DialogFragment.class.getName();
    public OnInputSelected mOnInputSelected;
    private TextView mCategory;
    private TextView mActionYes, mActionNo;
    private float x1,x2;
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
        mCategory.setText(title);
        TextToSpeechHandler ttsh = new TextToSpeechHandler(getContext(), title);
        ttsh.speak();

        final GestureDetector gesture = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return super.onDown(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(Math.abs(e1.getY()- e2.getY()) > MAX_OFFPATH){
                    return false;
                }
                if(e1.getX()- e2.getX() > MIN_DISTANCE && Math.abs(velocityX)>SWIPE_THRESHOLD_VELOCITY){
                    Log.d(TAG,"RIGHT TO LEFT");
                }
                else if(e2.getX()- e1.getX() > MIN_DISTANCE && Math.abs(velocityX)>SWIPE_THRESHOLD_VELOCITY){
                    Log.d(TAG,"LEFT TO RIGHT");
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getActionMasked();
//                switch (action){
//                    case(MotionEvent.ACTION_DOWN):
//                        x1 = event.getX();
//                        break;
//                    case (MotionEvent.ACTION_UP):
//                        x2 = event.getX();
//                        float diff_x = x2-x1;
//
//                        if (Math.abs(diff_x) > MIN_DISTANCE){
//                            if(x2>x1){
//                                Log.d(TAG, "Yes swiped (Swiped right)" );
//                                mOnInputSelected.yes(category);
//                                getDialog().dismiss();
//                            }
//                            else{
//                                Log.d(TAG, "No swiped (Swiped left)" );
//                                mOnInputSelected.no();
//                                getDialog().dismiss();
//                            }
//                        }
//                }
//
//                return v.onTouchEvent(event);
//            }
//        });


        mActionNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "No clicked" );
                mOnInputSelected.no();
                getDialog().dismiss();
            }
        });

        mActionYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "Yes clicked" );
                Log.d(TAG, "Category : " + category);
                mOnInputSelected.yes(category);
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
