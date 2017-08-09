package com.example.soda.soda20.main.frag.friend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soda.soda20.R;
import com.example.soda.soda20.R2;
import com.example.soda.soda20.activity.frag.ChatBaseFragment;
import com.example.soda.soda20.contract.ChatConstract;
import com.example.soda.soda20.data.local.bean.Constant;
import com.example.soda.soda20.data.local.bean.LocalMsg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by soda on 2017/8/7.
 */

public class FriendListFragment extends ChatBaseFragment {
    @Override
    public void setPresenter(ChatConstract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friendlist,container,false);
        ButterKnife.bind(this,root);
        initRecyclerView();
        return root;
    }
    @BindView(R2.id.recycler_friendlist)
    RecyclerView friendListRecycler;
    LinearLayoutManager mLayoutManager;
    List<String> friendsList;
    FriendRecyAdapter mAdapter;

    private void initRecyclerView() {
        friendsList = new ArrayList<>();
        mAdapter = new FriendRecyAdapter(getContext(),friendsList);
        mLayoutManager = new LinearLayoutManager(getContext());
        friendListRecycler.setAdapter(mAdapter);
        friendListRecycler.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onGetMessage(List<LocalMsg> localMsgs) {
        //仅通知
    }

    Handler handleFriendsList = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constant.ON_GET_FIRENDS){
                mAdapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    public void onGetFriendsList(List<String> friends) {
        friendsList.clear();
        for (int i = 0; i < friends.size(); i++) {
            friendsList.add(friends.get(i));
        }
        Message msg = new Message();
        msg.what = Constant.ON_GET_FIRENDS;
        handleFriendsList.sendMessage(msg);
    }

    @Override
    public String getChatWithUserId() {
        return null;
    }

    @Override
    public void onGetPhotosPathList(List<String> allPathList) {

    }
}
