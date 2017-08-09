package com.example.soda.soda20.data.remote.message;

import com.example.soda.soda20.data.interf.NotInUIThreadCallback;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by soda on 2017/8/7.
 */

public class MessagesResponse {
    public static void addMessageListener(EMMessageListener listener){
        EMClient.getInstance().chatManager().addMessageListener(listener);
    }
    public static void removeMessageListener(EMMessageListener listener){
        EMClient.getInstance().chatManager().removeMessageListener(listener);
    }

    /**
     * 获取所有聊天信息的方法
     * @param callBack
     */
    public static void getAllConversation(final NotInUIThreadCallback.GetConversationsCallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                if (conversations.size() > 0){
                    callBack.onGetConversationsSuccess(conversations);
                }
            }
        }).start();

    }

    public static void loadAllConversations(){
        EMClient.getInstance().chatManager().loadAllConversations();
    }

    public static void importMessage(List<EMMessage> list) {
        EMClient.getInstance().chatManager().importMessages(list);
    }

    public static void getAllMessages(final EMMessageListener listener,final String toUserName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toUserName);
                if (conversation == null)return;
                //获取此会话的所有消息
                List<EMMessage> messages = conversation.getAllMessages();
                //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
                //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                //        List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
                listener.onMessageReceived(messages);
                conversation.clear();
            }
        }).start();

    }

    public static void sendMessage(EMMessage message) {
        EMClient.getInstance().chatManager().sendMessage(message);
    }
}
