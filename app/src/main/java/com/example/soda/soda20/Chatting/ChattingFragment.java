package com.example.soda.soda20.Chatting;

import android.app.Notification;
import android.app.NotificationManager;
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
import com.example.soda.soda20.utils.DataCheckUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by soda on 2017/8/8.
 */

public class ChattingFragment extends ChatBaseFragment {
    private String chattingWithUserId;

    public String getChattingWithUserId() {
        return chattingWithUserId;
    }

    public void setChattingWithUserId(String chattingWithUserId) {
        this.chattingWithUserId = chattingWithUserId;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chatting,container,false);
        ButterKnife.bind(this,root);
        initRecycler();
        return root;
    }

    @BindView(R2.id.recycler_chatting)
    RecyclerView mRecycler;
    List<LocalMsg> mData;
    LinearLayoutManager mLayoutManager;
    ChattingRecyAdapter mAdapter;
    private void initRecycler() {
        if (mData == null){
            mData = new ArrayList<>();
        }
        if (mLayoutManager == null){
            mLayoutManager = new LinearLayoutManager(getContext());
        }
        if (mAdapter == null){
            mAdapter = new ChattingRecyAdapter(mData,getContext());
        }
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void setPresenter(ChatConstract.Presenter presenter) {
        mPresenter = presenter;
    }

    Handler handleOnGetMessage = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.ON_GET_MESSAGE:
                    mAdapter.notifyDataSetChanged();
                    mRecycler.smoothScrollToPosition(mData.size());
                    break;
            }
        }
    };
    @Override
    public void onGetMessage(List<LocalMsg> localMsgs) {
        super.onGetMessage(localMsgs);
        for (int i = 0; i < localMsgs.size(); i++) {
            if (DataCheckUtil.notHaveThisLocalMsg(mData,localMsgs.get(i)))
            mData.add(localMsgs.get(i));
        }
        Message msg = new Message();
        msg.what = Constant.ON_GET_MESSAGE;
        handleOnGetMessage.sendMessage(msg);
    }

    @Override
    public void onGetFriendsList(List<String> friends) {

    }

    @Override
    public String getChatWithUserId() {
        return chattingWithUserId;
    }

    @Override
    public void onGetPhotosPathList(List<String> allPathList) {

    }




}
