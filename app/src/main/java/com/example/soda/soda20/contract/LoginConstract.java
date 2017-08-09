package com.example.soda.soda20.contract;


import com.example.soda.soda20.BasePresenter;
import com.example.soda.soda20.BaseView;

/**
 * Created by soda on 2017/8/6.
 */

public interface LoginConstract {
    interface Presenter extends BasePresenter {
        void requestSignIn(String userId,String pwd);

        void createAccount(String userId, String pwd);
    }
    interface View extends BaseView<Presenter>{
        void onCreateAccountError(String description);

        void onCreateAccountSuccess(String userId, String pwd);

        void showLoginProgressBar();

        void onLoginSuccess();
    }
}
