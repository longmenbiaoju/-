package com.zzs.meizitu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * @author zzstar
 * @data 2018/2/6
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> titles;
    private FragmentManager fm;

    public ViewPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> mFragments, ArrayList<String> titles) {
        super(fragmentManager);
        this.mFragments = mFragments;
        this.titles = titles;
        fm = fragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
