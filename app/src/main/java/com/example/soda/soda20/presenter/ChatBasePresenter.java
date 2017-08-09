package com.example.soda.soda20.presenter;

import android.util.Log;

import com.example.soda.soda20.app.AppInit;
import com.example.soda.soda20.broad_rec.CallBroadCaskReceiver;
import com.example.soda.soda20.contract.ChatConstract;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.data.local.bean.LocalMsg;
import com.example.soda.soda20.utils.MessageUtil;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soda on 2017/8/7.
 */

public class ChatBasePresenter implements ChatConstract.Presenter,EMConnectionListener,
        EMMessageListener,EMContactListener{
    private final String TAG = "ChatBasePresenter";

    protected ModelResponse mModel;
    protected ChatConstract.View mView;

    private CallBroadCaskReceiver mCallReceiver = new CallBroadCaskReceiver();
    public ChatBasePresenter(ModelResponse model, ChatConstract.View view){
        mModel = model;
        mView = view;
    }
    @Override
    public void start() {
        //监听链接状态
        mModel.addConnectionListener(this);
        //监听消息状态
        mModel.addMessageListener(this);
        //监听好友状态事件
        mModel.addContactListener(this);

    }



    @Override
    public void stop() {
        //移除链接状态监听
        mModel.removeConnectionListener(this);
        //移除消息监听
        mModel.removeMessageListener(this);
        //移除好友状态事件监听
        mModel.removeContactListener(this);
    }

    /**
     * 用户主动与被动退出都回调此方法
     */
    @Override
    public void userLogout() {
        mModel.logOut();
        mView.onLogOut();
    }


    /**
     * 用户登录状态的监听
     */
    @Override
    public void onConnected() {
        AppInit.getInstance().userGloble.upDateConnected(true);
    }

    @Override
    public void onDisconnected(int i) {
        //此方法不在主线程中运行
        AppInit instance = AppInit.getInstance();
        instance.userGloble.upDateConnected(false);

        if (i == EMError.USER_REMOVED){
            //账号已经被移除
            instance.userGloble.updateWhenLogOut();
            //通知 调用主线程返回登录页面
        }else if (i == EMError.USER_LOGIN_ANOTHER_DEVICE){
            //用户在其他设备登录
            instance.userGloble.updateWhenLogOut();
            //通知 调用主线程返回登录页面
        }else {
            //链接不到服务器，请检查网络
            //通知 在主线程中
        }
    }

    /**
     * 以下方法为消息的回调方法，只重写必须重写的方法
     * @param list
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        mModel.importMessage(list);
        List<LocalMsg> localMsgs = MessageUtil.readMessage(list);
        Log.d(TAG,"onMessageReceived");
        mView.onGetMessage(localMsgs);
        mView.aleartUser();

    }



    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        Log.d(TAG,"onMessageReceived");
    }

    @Override
    public void onMessageRead(List<EMMessage> list) {
        Log.d(TAG,"onCmdMessageReceived");
    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {
        Log.d(TAG,"onMessageDelivered");
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {
        Log.d(TAG,"onMessageChanged");
    }
    /**
     * 好友状态的监听事件
     */
    @Override
    public void onContactAdded(String s) {
        //请求添加
    }

    @Override
    public void onContactDeleted(String s) {
        //被删除
    }

    @Override
    public void onContactInvited(String s, String s1) {
        //收到好友邀请
    }

    @Override
    public void onFriendRequestAccepted(String s) {
        //请求被同意
    }

    @Override
    public void onFriendRequestDeclined(String s) {
        //请求没有被同意
    }

    public void sendImageMessage(String imgPath, boolean isComPress, String chatWithUserId) {
        EMMessage message = mModel.sendImageMessage(imgPath,isComPress,chatWithUserId);
        List<EMMessage> list = new ArrayList<>();
        list.add(message);
        onMessageReceived(list);
    }
}
