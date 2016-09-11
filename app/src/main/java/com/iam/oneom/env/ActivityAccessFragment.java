package com.iam.oneom.env;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class ActivityAccessFragment extends Fragment {

    protected AppCompatActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = ((AppCompatActivity)context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
}
