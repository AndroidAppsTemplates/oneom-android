package com.iam.oneom.pages.main.serial;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.iam.oneom.R;
import com.iam.oneom.databinding.SerialSearchActivityBinding;

/**
 * Created by iam on 12.05.17.
 */

public class SerialSearchActivity extends AppCompatActivity {

    public SerialSearchViewModel serialSearchViewModel;
    public SerialSearchActivityBinding binding;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SerialSearchActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serialSearchViewModel = new SerialSearchViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.serial_search_activity);
        binding.setVm(serialSearchViewModel);
    }


}
