package com.example.soda.soda20.utils;

import android.util.Log;

import com.example.soda.soda20.app.AppInit;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.data.local.bean.LocalMsg;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by soda on 2017/8/7.
 */

public class MessageUtil {
    private static final String TAG = "MessageUtil";

    /**
     * 将获取的信息本地化，简化编程难度
     * @param list
     * @return
     */
    public static List<LocalMsg> readMessage(List<EMMessage> list) {
        List<LocalMsg> localMsgList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            EMMessage message = list.get(i);
            String fromUserId = message.getFrom();
            LocalMsg localMsg = null;
            long msgTime = message.getMsgTime();//msgTime/1000为秒

            switch (message.getType()) {
                case TXT:
                    String txtContent = getMessageTxtContent(message);
//                    Log.d(TAG,"message from" + fromUserId + "获得文字消息 content = " + txtContent);
                    localMsg = new LocalMsg(fromUserId, LocalMsg.LOCALTXT, msgTime, txtContent);
                    break;
                case IMAGE:
                    String thumbnailUrl = getImageUrl(message);
//                    Log.d(TAG, "message from" + fromUserId + "获得图片消息 path = " + thumbnailUrl);

                    localMsg = new LocalMsg(fromUserId, localMsg.LOCALIMAGE, msgTime, thumbnailUrl);
                    break;
                case VOICE:
                    String audioPath = getMessageAudioPath(message);
                    int audioLength = getMessageAudioLength(message);
//                    Log.d(TAG, "message from" + fromUserId + "获得语音消息 path = " + audioPath);
                    localMsg = new LocalMsg(fromUserId, localMsg.LOCALAUDIO, msgTime, audioPath, audioLength);
                    break;
            }
            if (localMsg != null) {
                localMsgList.add(localMsg);
            }
        }

        return localMsgList;
    }

    public static List<LocalMsg> getLastChatContentList(List<LocalMsg> localMsgList){
        LinkedHashMap<String,LocalMsg> chatMap = new LinkedHashMap<>();
        for (int i = 0; i < localMsgList.size(); i++) {
            LocalMsg localMsg = localMsgList.get(i);
            String userId = localMsg.getFromUserId();
            //若用户自己发的消息则不进行记录
            String localUserId = AppInit.getInstance().userGloble.getmAccount();
            if (localUserId.equals(localMsg.getFromUserId())){
                continue;
            }

            if (chatMap.containsKey(userId)){
                LocalMsg msg = chatMap.get(userId);
                if (msg.getMsgTime() < localMsg.getMsgTime()){
                    chatMap.put(userId,localMsg);
                }
            }else {
                chatMap.put(userId,localMsg);
            }
        }
        Set<String> keySet = chatMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        List<LocalMsg> lastChatContentList = new ArrayList<>();
        while (iterator.hasNext()){
            lastChatContentList.add(chatMap.get(iterator.next()));
        }
        return lastChatContentList;
    }

    public static String getMessageTxtContent(EMMessage message) {
        return ((EMTextMessageBody) message.getBody()).getMessage();
    }

    public static String getImageUrl(EMMessage message){
        String url = ((EMImageMessageBody) message.getBody()).getThumbnailUrl();
        if (url.length() == 0){
            url = ((EMImageMessageBody) message.getBody()).getLocalUrl();
        }
        return url;
    }

    public static String getMessageAudioPath(EMMessage message) {
        return ((EMVoiceMessageBody) message.getBody()).getLocalUrl();
    }

    public static int getMessageAudioLength(EMMessage message) {
        return ((EMVoiceMessageBody) message.getBody()).getLength();
    }
}
