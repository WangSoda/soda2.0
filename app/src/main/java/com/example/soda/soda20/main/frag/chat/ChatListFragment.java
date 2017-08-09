package com.example.soda.soda20.main.frag.chat;

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
import com.example.soda.soda20.data.local.bean.LocalMsg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.soda.soda20.data.local.bean.Constant.ON_GET_MESSAGE;

/**
 * Created by soda on 2017/8/7.
 */

public class ChatListFragment extends ChatBaseFragment {
    @Override
    public void setPresenter(ChatConstract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chatlist,container,false);
        ButterKnife.bind(this,root);
        initRecyclerView();
        return root;
    }

    @BindView(R2.id.recycler_chatlist)
    RecyclerView chatListRecycler;
    LinearLayoutManager layoutManager;
    ChatRecyAdapter chatRecyAdapter;
    List<LocalMsg> mList;

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        mList = new ArrayList<>();
        chatRecyAdapter = new ChatRecyAdapter(mList,getContext());
        chatListRecycler.setLayoutManager(layoutManager);
        chatListRecycler.setAdapter(chatRecyAdapter);
    }

    Handler handleMessage = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ON_GET_MESSAGE:
                    chatRecyAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onGetMessage(List<LocalMsg> localMsgs) {
        mList.clear();
        for (int i = 0; i < localMsgs.size(); i++) {
            mList.add(localMsgs.get(i));
        }

        Message msg = new Message();
        msg.what = ON_GET_MESSAGE;
        handleMessage.sendMessage(msg);
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
