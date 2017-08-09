package com.example.soda.soda20.presenter;

import android.util.Log;

import com.example.soda.soda20.app.AppInit;
import com.example.soda.soda20.contract.LoginConstract;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.login.LoginFragment;
import com.hyphenate.EMCallBack;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by soda on 2017/8/6.
 */

public class LoginPresenter implements LoginConstract.Presenter, EMCallBack {
    private final String TAG = "LoginPresenter";
    LoginConstract.View mView;
    ModelResponse mModel;

    public LoginPresenter(LoginFragment view, ModelResponse model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }


    @Override
    public void requestSignIn(String userId, String pwd) {
        mModel.Login(userId,pwd,this);
        mView.showLoginProgressBar();
    }

    @Override
    public void createAccount(String userId, String pwd) {
        try {
            mModel.createAccount(userId,pwd);
            mView.onCreateAccountSuccess(userId,pwd);
        } catch (HyphenateException e) {
            e.printStackTrace();
            mView.onCreateAccountError(e.getDescription());
        }
    }

    @Override
    public void onSuccess() {
        mView.onLoginSuccess();
        Log.d(TAG,"success ");
    }

    @Override
    public void onError(int i, String s) {
        if (i == 200){//在登出失败的情况下 用户认为自己已经登出 并尝试登录其他账号
            Log.d(TAG,"用户登出");
            mModel.logOut();
        }
        Log.d(TAG,"onError i = " + i + " String = " + s);
    }

    @Override
    public void onProgress(int i, String s) {
        Log.d(TAG,"onProgress i = " + i + " String = " + s);
    }
}
