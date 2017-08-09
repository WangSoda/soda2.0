package com.example.soda.soda20.Chatting;

import com.example.soda.soda20.contract.ChatConstract;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.data.local.bean.LocalMsg;
import com.example.soda.soda20.presenter.ChatBasePresenter;
import com.example.soda.soda20.utils.MessageUtil;
import com.example.soda.ui.AudioRecorderButton;
import com.hyphenate.chat.EMMessage;
import com.superrtc.call.StatsReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soda on 2017/8/8.
 */

public class ChattingPresenter extends ChatBasePresenter implements AudioRecorderButton.AudioFinishRecorderListener {
    public ChattingPresenter(ModelResponse model, ChatConstract.View view) {
        super(model, view);
    }

    @Override
    public void start() {
        super.start();
        mModel.getAllMessageWithUser(this,mView.getChatWithUserId());
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        List<LocalMsg> localMsgs = MessageUtil.readMessage(list);
        mView.onGetMessage(localMsgs);
    }

    public void SendTXTMessage(final String chatContent) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                EMMessage message = mModel.SendTxtMessage(chatContent, mView.getChatWithUserId());
                List<EMMessage> list = new ArrayList<>();
                list.add(message);
                onMessageReceived(list);
            }
        }).start();

    }

    @Override
    public void onAudioRecorderFinish(final float seconds, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMMessage message = mModel.sendAudioMessage(filePath, (int) seconds, mView.getChatWithUserId());
                List<EMMessage> list = new ArrayList<>();
                list.add(message);
                onMessageReceived(list);
            }
        }).start();
    }
}
