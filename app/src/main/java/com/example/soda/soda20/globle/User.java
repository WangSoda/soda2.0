package com.example.soda.soda20.globle;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by soda on 2017/8/6.
 */

public class User {
    private static User instance;
    private static Context mContext;

    /**
     * mIsLogin和RememberPwd 是独立存在的，用户可以登出但保持密码
     */
    //用户是否处于登录状态
    private boolean mIsLogin;
    private String mPassword;
    //用户是否选择保存密码
    private boolean mRememberPwd;
    private String mAccount;
    private User(String account, String password, boolean rememberPwd, boolean isLogin){
        mAccount = account;
        mPassword = password;
        mRememberPwd = rememberPwd;
        //若用户上次登录时选择记住密码，并未进行登出，则可以认为用户本次处于登录状态
        mIsLogin = isLogin;
    }
    public static User getInstance(Context context){
        mContext = context;
        if (instance == null){
            synchronized (User.class){
                if (instance == null){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    String account = preferences.getString("account", "");
                    String password = preferences.getString("password", "");
                    boolean rememberPwd = preferences.getBoolean("isPwdChecked", false);
                    boolean isLogin = preferences.getBoolean("isLogin",false);
                    instance = new User(account,password,rememberPwd,isLogin);
                }
            }
        }
        return instance;
    }

    /**
     * 当用户信息发生改变时，将用户信息保存到本地
     * @param account
     * @param password
     * @param isPassWordChecked
     */
    public void updateOnLogin(String account, String password, boolean isPassWordChecked){
        if (instance != null){
            if (!mAccount.equals(account) || !mPassword.equals(password) || mIsLogin != true || isPassWordChecked != mRememberPwd){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("account",account);
                if (isPassWordChecked){
                    edit.putString("password", password);
                }
                edit.putBoolean("isLogin",true);
                edit.putBoolean("isPwdChecked",isPassWordChecked);
                edit.apply();
                upDateConnected(true);

                mIsLogin = true;
                mAccount = account;
                mPassword = password;
                mRememberPwd = isPassWordChecked;
            }
        }
    }

    /**
     * 当用户登出时，将信息更新到本地，并在内存中更新状态
     * @param
     */
    public void updateWhenLogOut(){
        if (instance != null){
            if (mIsLogin == true){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putBoolean("isLogin",false);
                edit.putBoolean("isPwdChecked",ismRememberPwd());
                edit.putString("account",mAccount);
                if (ismRememberPwd()){
                    edit.putString("password",mPassword);
                }
                mIsLogin = false;
                edit.apply();

                upDateConnected(false);
            }
        }
    }



    public String getmAccount() {
        return mAccount;
    }

    public String getmPassword() {
        return mPassword;
    }

    public boolean ismIsLogin() {
        return mIsLogin;
    }



    public boolean ismRememberPwd() {
        return mRememberPwd;
    }

    public void setmRememberPwd(boolean mRememberPwd) {
        this.mRememberPwd = mRememberPwd;
    }

    //客户端实时的链接状态
    private boolean isConnected;

    public boolean isConnected() {
        return isConnected;
    }

    public void upDateConnected(boolean connecstate) {
        isConnected = connecstate;
    }
}
