package com.example.findmefood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FoodDialogFragment extends DialogFragment {
    private static String TAG = DialogFragment.class.getName();

    public interface OnInputSelected{
        void yes(String input);
        void no();
    }
    public OnInputSelected mOnInputSelected;


    private TextView mCategory;
    private TextView mActionYes, mActionNo;


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
