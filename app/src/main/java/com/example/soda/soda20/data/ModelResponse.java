package com.example.soda.soda20.data;

import android.content.ContentResolver;

import com.example.soda.soda20.Chatting.ChattingPresenter;
import com.example.soda.soda20.data.interf.NotInUIThreadCallback;
import com.example.soda.soda20.data.local.LocalImageDataSource;
import com.example.soda.soda20.data.remote.account.CreateAccount;
import com.example.soda.soda20.data.remote.conn.ConnectionState;
import com.example.soda.soda20.data.remote.friends.FriendsResponse;
import com.example.soda.soda20.data.remote.login_out.LoginOrOut;
import com.example.soda.soda20.data.remote.message.MessagesResponse;
import com.example.soda.soda20.main.frag.chat.ChatPresenter;
import com.example.soda.soda20.main.frag.friend.FriendPresenter;
import com.example.soda.soda20.photo.PhotoPresenter;
import com.example.soda.soda20.presenter.ChatBasePresenter;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by soda on 2017/8/6.
 */

public class ModelResponse {
    private static ModelResponse mInstance;
    private ModelResponse(){}

    /**
     * 以单例模式对model层的对外接口进行封装
     * @return model的对外接口响应实体
     */
    public static ModelResponse getInstance(){
        if (mInstance == null){
            synchronized (ModelResponse.class){
                if (mInstance == null){
                    mInstance = new ModelResponse();
                }
            }
        }
        return mInstance;
    }

    /**
     * 创建账号
     * @param userId
     * @param userPwd
     * @throws HyphenateException
     */
    public void createAccount(String userId,String userPwd) throws HyphenateException {
        CreateAccount.createAccount(userId, userPwd);
    }

    /**
     * 尝试登录
     * @param userName
     * @param password
     * @param loginCallback
     */
    public void Login(String userName, String password, EMCallBack loginCallback){
        LoginOrOut.userLogin(userName, password, loginCallback);
    }
    //用户登出
    public void logOut(){
        LoginOrOut.userLogout(false);
    }

    public void loadAllConversations(){
        MessagesResponse.loadAllConversations();
    }

    public void addConnectionListener(EMConnectionListener listener){
        ConnectionState.addConnectionListener(listener);
    }
    public void removeConnectionListener(EMConnectionListener listener){
        ConnectionState.removeConnectionListener(listener);
    }

    public void addMessageListener(EMMessageListener listener) {
        MessagesResponse.addMessageListener(listener);
    }

    public void removeMessageListener(EMMessageListener listener) {
        MessagesResponse.removeMessageListener(listener);
    }


    public void addContactListener(EMContactListener listener) {
        FriendsResponse.setContectListener(listener);
    }
    public void removeContactListener(EMContactListener listener) {
        FriendsResponse.removeContactListener(listener);
    }

    /**
     * 获取所有会话消息 回调接口不在UI线程
     * @param callback
     */
    public void getAllConversations(NotInUIThreadCallback.GetConversationsCallBack callback) {
        MessagesResponse.getAllConversation(callback);
    }

    public void getAllFriends(NotInUIThreadCallback.GetAllFriendsCallBack callback) {
        FriendsResponse.getAllFriends(callback);
    }

    /**
     * 将消息导入数据库
     * @param
     */
    public void importMessage(List<EMMessage> list) {
        MessagesResponse.importMessage(list);
    }

    public void getAllMessageWithUser(EMMessageListener listener, String chatWithUserId) {
        MessagesResponse.getAllMessages(listener,chatWithUserId);
    }

    public EMMessage SendTxtMessage(String chatContent, String chatWithUserId) {
        EMMessage message = EMMessage.createTxtSendMessage(chatContent, chatWithUserId);
        MessagesResponse.sendMessage(message);
        return message;
    }

    public EMMessage sendAudioMessage(String filePath, int seconds, String chatWithUserId) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, seconds, chatWithUserId);
        MessagesResponse.sendMessage(message);
        return message;
    }

    /**
     * 要解析本地数据 ContentResolver 必须不为 null
     * @param contentResolver
     */
    ContentResolver mContentResolver;
    public void setContentResolver(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public void getLocalImagesList(PhotoPresenter callback) {
        if (mContentResolver != null){
            LocalImageDataSource localImageDataSource = new LocalImageDataSource();
            localImageDataSource.getList(mContentResolver,callback);
        }
    }

    public EMMessage sendImageMessage(String imgPath, boolean isComPress, String chatWithUserId) {
        EMMessage imageSendMessage = EMMessage.createImageSendMessage(imgPath, isComPress, chatWithUserId);
        MessagesResponse.sendMessage(imageSendMessage);
        return imageSendMessage;
    }
}
