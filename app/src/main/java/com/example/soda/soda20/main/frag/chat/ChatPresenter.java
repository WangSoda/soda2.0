package com.example.soda.soda20.main.frag.chat;

import android.util.Log;

import com.example.soda.soda20.contract.ChatConstract;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.data.interf.NotInUIThreadCallback;
import com.example.soda.soda20.presenter.ChatBasePresenter;
import com.example.soda.soda20.utils.ReadConversUtil;
import com.hyphenate.chat.EMConversation;

import java.util.Map;

/**
 * Created by soda on 2017/8/7.
 */

public class ChatPresenter extends ChatBasePresenter implements NotInUIThreadCallback.GetConversationsCallBack {
    private final String TAG = "ChatPresenter";
    public ChatPresenter(ModelResponse model, ChatConstract.View view) {
        super(model, view);
    }

    @Override
    public void start() {
        super.start();
        mModel.getAllConversations(this);
        Log.d(TAG,"getAllConversations");
    }


    /**
     * @param conversationMap
     * TODO 应该将此方法得到的信息并入正常截获的信息回调方法中
     */
    @Override
    public void onGetConversationsSuccess(Map<String, EMConversation> conversationMap) {
        ReadConversUtil.readConverMap(conversationMap);
    }

}
