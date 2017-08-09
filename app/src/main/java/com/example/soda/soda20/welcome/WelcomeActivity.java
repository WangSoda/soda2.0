package com.example.soda.soda20.welcome;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.soda.soda20.R;
import com.example.soda.soda20.app.AppInit;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.globle.User;
import com.example.soda.soda20.login.LoginActivity;
import com.example.soda.soda20.main.MainActivity;
import com.example.soda.utils.PermissionUtils;
import com.hyphenate.EMCallBack;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        checkPermissionAndInitGloble();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG,"result" + requestCode + " " + (grantResults[0] == PackageManager.PERMISSION_GRANTED));
        switch (requestCode){
            case PermissionUtils.WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    checkPermissionAndInitGloble();
                }else {
                    Toast.makeText(this,"该应用需要读写权限才能更好的工作",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private final int LOGIN_SUCCESS = 1;
    private final int LOGIN_FAILSE = 0;
    private Handler mLoginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOGIN_SUCCESS:
                    MainActivity.startActivity(WelcomeActivity.this);
                    break;
                case LOGIN_FAILSE:
                    LoginActivity.startActivity(WelcomeActivity.this);
                    break;
            }
        }
    };
    /**
     * 检查读写权限，若不能读写则无法存储账号密码
     */
    private void checkPermissionAndInitGloble() {
        if (!PermissionUtils.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new PermissionUtils().requestPermissions(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,PermissionUtils.WRITE_EXTERNAL_STORAGE);
        }else {
            AppInit appInitInstance = AppInit.getInstance();
            appInitInstance.initGloble();
            User user = appInitInstance.userGloble;
            String account = user.getmAccount();
            String password = user.getmPassword();
            boolean isLogin = user.ismIsLogin();
            boolean rememberPwd = user.ismRememberPwd();

            if (isLogin && account != null && password != null && rememberPwd){
                ModelResponse.getInstance().Login(account, password, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Message msg = new Message();
                        msg.what = LOGIN_SUCCESS;
                        mLoginHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Message msg = new Message();
                        msg.what = LOGIN_FAILSE;
                        mLoginHandler.sendMessage(msg);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }else {
                LoginActivity.startActivity(WelcomeActivity.this);
            }
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
