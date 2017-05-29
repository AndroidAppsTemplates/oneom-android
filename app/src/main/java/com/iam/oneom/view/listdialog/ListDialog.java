package com.iam.oneom.view.listdialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iam.oneom.R;
import com.iam.oneom.databinding.SelectDialogBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iam on 29.05.17.
 */

public class ListDialog<T extends Parcelable & ClickableListItem> extends DialogFragment {

    private static final String DATA_EXTRA = "DATA_EXTRA";

    SelectDialogBinding mBinding;
    ListDialogViewModel<T> model;

    public static <T extends Parcelable & ClickableListItem> void show(FragmentManager manager, List<T> data) {
        ListDialog<T> tListDialog = new ListDialog<>();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(DATA_EXTRA, new ArrayList<>(data));
        tListDialog.setArguments(bundle);
        tListDialog.show(manager, tListDialog.getClass().getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.select_dialog, container, false);
        model = new ListDialogViewModel<>(getArguments().getParcelableArrayList(DATA_EXTRA));
        mBinding.setVm(model);
        return mBinding.getRoot();
    }
}
