package com.example.soda.soda20.data.remote.friends;

import android.util.Log;

import com.example.soda.soda20.data.interf.NotInUIThreadCallback;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;
import java.util.Map;

/**
 * Created by soda on 2017/8/7.
 */

public class FriendsResponse {

    public static void setContectListener(EMContactListener listener) {
        EMClient.getInstance().contactManager().setContactListener(listener);
    }
    public static void removeContactListener(EMContactListener listener) {
        EMClient.getInstance().contactManager().removeContactListener(listener);
    }

    /**
     * 获得好友列表
     * 该方法可能出现异常，因此应该定义一个回调接口进行回传
     */
    public static void getAllFriends(final NotInUIThreadCallback.GetAllFriendsCallBack callBack){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("tryToGetFriends","to try");
                    List<String> friends = null;
                    friends = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Log.d("tryToGetFriends","onGetFriendsListSuccess");
                    callBack.onGetFriendsListSuccess(friends);
                } catch (HyphenateException e) {
                    Log.d("tryToGetFriends","onfailse" + e.getErrorCode());
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
