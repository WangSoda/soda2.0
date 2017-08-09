package com.example.soda.soda20.main.frag.friend;

import com.example.soda.soda20.contract.ChatConstract;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.data.interf.NotInUIThreadCallback;
import com.example.soda.soda20.presenter.ChatBasePresenter;
import com.hyphenate.chat.EMConversation;

import java.util.List;
import java.util.Map;

/**
 * Created by soda on 2017/8/7.
 */

public class FriendPresenter extends ChatBasePresenter implements NotInUIThreadCallback.GetAllFriendsCallBack{
    public FriendPresenter(ModelResponse model, ChatConstract.View view) {
        super(model, view);
    }

    @Override
    public void start() {
        super.start();
        mModel.getAllFriends(this);
    }


    /**
     * 该方法不在主线程中工作
     * @param friends
     */
    @Override
    public void onGetFriendsListSuccess(List<String> friends) {
        mView.onGetFriendsList(friends);
    }
}
