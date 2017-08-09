package com.example.soda.soda20.data.remote.conn;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.chat.EMClient;

/**
 * Created by soda on 2017/8/6.
 * 链接状态的监听方法
 * 该方法除欢迎界面和注册登录界面，都需要进行监听
 */

public class ConnectionState {
    public static void addConnectionListener(EMConnectionListener listener){
        EMClient.getInstance().addConnectionListener(listener);

    }
    public static void removeConnectionListener(EMConnectionListener listener){
        EMClient.getInstance().removeConnectionListener(listener);
    }
}
