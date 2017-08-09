package com.example.soda.soda20.data.interf;

import com.hyphenate.chat.EMConversation;

import java.util.List;
import java.util.Map;

/**
 * Created by soda on 2017/8/7.
 */

public interface NotInUIThreadCallback {
    interface GetConversationsCallBack{
        void onGetConversationsSuccess(Map<String, EMConversation> conversationMap);
    }

    interface GetAllFriendsCallBack{
        void onGetFriendsListSuccess(List<String> friends);
    }
}
