package com.iam.oneom.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.StringRes;
import android.support.v13.app.FragmentPagerAdapter;

import com.iam.oneom.core.util.ResUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iam on 20.05.17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void addFragment(Fragment fragment, @StringRes int title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(ResUtil.getString(title));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
