package com.iam.oneom.pages.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iam.oneom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeasonNumberFragment extends Fragment {

    private int number;
    @BindView(R.id.text)
    TextView textView;
    private Refresheable refresheable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.season_number_fragment, container, false);

        ButterKnife.bind(this, view);

        textView.setText(String.format("%02d", number));

        return view;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (refresheable != null) {
//            try {
//                refresheable.refresh();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCallback(Refresheable refresheable) {
        this.refresheable = refresheable;
    }
}
