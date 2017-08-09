package com.example.soda.soda20.main.frag.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soda.soda20.R;
import com.example.soda.soda20.R2;
import com.example.soda.soda20.activity.frag.ChatBaseFragment;
import com.example.soda.soda20.contract.ChatConstract;
import com.example.soda.soda20.data.local.bean.LocalMsg;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by soda on 2017/8/7.
 */

public class SettingFragment extends ChatBaseFragment {
    @Override
    public void setPresenter(ChatConstract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting,container,false);
        ButterKnife.bind(this,root);
        return root;
    }
    @OnClick(R2.id.user_logout)
    void userLogout(View view){
        mPresenter.userLogout();
    }


    @Override
    public void onGetMessage(List<LocalMsg> localMsgs) {
    }

    @Override
    public void onGetFriendsList(List<String> friends) {

    }

    @Override
    public String getChatWithUserId() {
        return null;
    }

    @Override
    public void onGetPhotosPathList(List<String> allPathList) {

    }
}
