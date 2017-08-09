package com.example.soda.soda20.main;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.example.soda.soda20.R;
import com.example.soda.soda20.R2;
import com.example.soda.soda20.activity.ChatBaseActivity;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.main.frag.chat.ChatListFragment;
import com.example.soda.soda20.main.frag.chat.ChatPresenter;
import com.example.soda.soda20.main.frag.friend.FriendListFragment;
import com.example.soda.soda20.main.frag.friend.FriendPresenter;
import com.example.soda.soda20.main.frag.setting.SettingFragment;
import com.example.soda.soda20.main.frag.setting.SettingPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends ChatBaseActivity {


    public static void startActivity(Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    List<Fragment> fragListMain;
    private ModelResponse mModel;
    private VPFragListAdapter mAdapter;

    @BindView(R2.id.vp_show_fragment_main)
    ViewPager vpShowFragment;
    @BindView(R2.id.tab_menu_main)
    TabLayout tabMenuMain;
    @Override
    protected void loadData() {
        mModel = ModelResponse.getInstance();

        //各种fragment初始化
        ChatListFragment chatListFragment = new ChatListFragment();
        ChatPresenter chatPresenter = new ChatPresenter(mModel,chatListFragment);
        chatListFragment.setPresenter(chatPresenter);

        FriendListFragment friendListFragment = new FriendListFragment();
        FriendPresenter friendPresenter = new FriendPresenter(mModel,friendListFragment);
        friendListFragment.setPresenter(friendPresenter);

        SettingFragment settingFragment = new SettingFragment();
        SettingPresenter settingPresenter = new SettingPresenter(mModel,settingFragment);
        settingFragment.setPresenter(settingPresenter);

        fragListMain = new ArrayList<>();
        //各种add
        fragListMain.add(chatListFragment);
        fragListMain.add(friendListFragment);
        fragListMain.add(settingFragment);

        mAdapter = new VPFragListAdapter(getSupportFragmentManager(),fragListMain);

        vpShowFragment.setAdapter(mAdapter);

        tabMenuMain.setupWithViewPager(vpShowFragment);

        TabLayout.Tab tab1 = tabMenuMain.getTabAt(0);
        TabLayout.Tab tab2 = tabMenuMain.getTabAt(1);
        TabLayout.Tab tab3 = tabMenuMain.getTabAt(2);

        tabMenuMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main);
    }
}
