package com.example.soda.soda20.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by soda on 2017/8/7.
 */

public class VPFragListAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragList;
    public VPFragListAdapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        mFragList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragList.get(position);
    }

    @Override
    public int getCount() {
        return mFragList.size();
    }

    /**
     * 设定tablayout的方法
     * @param position
     * @return
     */

    private String[] title = new String[]{"聊天列表","好友列表","个人设置"};
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
