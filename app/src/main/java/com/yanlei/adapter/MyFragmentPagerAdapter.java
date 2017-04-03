package com.yanlei.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.yanlei.fragments.MainFragment;
import com.yanlei.fragments.AllCourseFragment;
import com.yanlei.fragments.AboutMeFragment;
import com.yanlei.mooclike.HomeActivity;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;
    private MainFragment mainFragment = null;
    private AllCourseFragment allCourseFragment = null;
    private AboutMeFragment aboutMeFragment = null;


    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mainFragment = new MainFragment();
        allCourseFragment = new AllCourseFragment();
        aboutMeFragment = new AboutMeFragment();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case HomeActivity.PAGE_ONE:
                fragment = mainFragment;
                break;
            case HomeActivity.PAGE_TWO:
                fragment = allCourseFragment;
                break;
            case HomeActivity.PAGE_THREE:
                fragment = aboutMeFragment;
                break;

        }
        return fragment;
    }


}

