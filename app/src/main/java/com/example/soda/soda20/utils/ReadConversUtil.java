package com.example.soda.soda20.utils;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.Map;
import java.util.Set;

/**
 * Created by soda on 2017/8/7.
 */

public class ReadConversUtil {
    public static void readConverMap(Map<String, EMConversation> conversationMap) {
        Set<String> keySet = conversationMap.keySet();
        for (String fromUserId:
                keySet) {
            EMConversation conversation = conversationMap.get(fromUserId);
            String conversationId = conversation.conversationId();
            EMMessage messageFromOthers = conversation.getLatestMessageFromOthers();
            if (messageFromOthers == null)continue;//若联系人无消息返回，此处会出现null对象

            int unreadMsgCount = conversation.getUnreadMsgCount();
            EMMessage lastMessage = conversation.getLastMessage();
            EMMessage.Type lastMessageType = lastMessage.getType();



            //此方法不清除数据库消息
            conversation.clear();
        }

    }
}
