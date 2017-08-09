package com.example.soda.soda20.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.soda.soda20.activity.BaseActivity;
import com.example.soda.soda20.R;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.presenter.LoginPresenter;
import com.example.soda.utils.ActivityUtils;

import butterknife.ButterKnife;

/**
 * Created by soda on 2017/8/6.
 */

public class LoginActivity extends BaseActivity {
    LoginFragment loginView;
    LoginPresenter mLoginPresenter;
    ModelResponse mModel;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initViews() {
        loginView = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.login_framelayout);
        if (loginView == null){
            loginView = new LoginFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loginView,R.id.login_framelayout);

            mModel = ModelResponse.getInstance();
            mLoginPresenter = new LoginPresenter(loginView,mModel);
            loginView.setPresenter(mLoginPresenter);
        }
    }

    /**
     * 当登录页面结束时 自动弹栈
     */
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    /**
     * 外部启动LoginActivity的静态 方法
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
}
