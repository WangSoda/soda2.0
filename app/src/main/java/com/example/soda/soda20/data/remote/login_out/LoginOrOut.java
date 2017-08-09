package com.example.soda.soda20.data.remote.login_out;

import com.example.soda.soda20.app.AppInit;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by soda on 2017/8/6.
 */

public class LoginOrOut {
    /**
     * 环信的登录方法
     * @param userName  用户账号
     * @param password  用户密码
     * @param loginCallback 回调接口
     */
    public static void userLogin(String userName, String password, EMCallBack loginCallback){
        EMClient.getInstance().login(userName,password,loginCallback);
    }

    /**
     * 环信的登出同步方法
     * @param unBindToken   是否解绑第三方推送Token
     */
    public static void userLogout(boolean unBindToken){
        EMClient.getInstance().logout(unBindToken);
        AppInit instance = AppInit.getInstance();
        instance.userGloble.updateWhenLogOut();
    }

    /**
     * 环信的异步登出方法
     * @param unBindToken
     * @param logoutCallback
     */
    public static void userLogout(boolean unBindToken,EMCallBack logoutCallback){
        EMClient.getInstance().logout(unBindToken,logoutCallback);
        AppInit instance = AppInit.getInstance();
        instance.userGloble.updateWhenLogOut();
    }
}
