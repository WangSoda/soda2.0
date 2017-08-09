package com.example.soda.soda20.contract;


import com.example.soda.soda20.BasePresenter;
import com.example.soda.soda20.BaseView;
import com.example.soda.soda20.data.local.bean.LocalMsg;

import java.util.List;

/**
 * Created by soda on 2017/8/7.
 */

public interface ChatConstract {
    interface Presenter extends BasePresenter {
        void userLogout();

    };
    interface View extends BaseView<Presenter> {

        void onLogOut();


        void onGetMessage(List<LocalMsg> localMsgs);

        void onGetFriendsList(List<String> friends);

        String getChatWithUserId();

        void onGetPhotosPathList(List<String> allPathList);

        void onSendImageList();

        void aleartUser();
    }
}
