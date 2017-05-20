package com.iam.oneom.pages.main.search;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.iam.oneom.R;
import com.iam.oneom.databinding.NoSourceActivityBinding;

/**
 * Created by iam on 20.05.17.
 */

public class NoSourceActivity extends AppCompatActivity {

    NoSourceActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.no_source_activity);
    }
}
