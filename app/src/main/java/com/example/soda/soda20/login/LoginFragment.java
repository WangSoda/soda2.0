package com.example.soda.soda20.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.soda.soda20.R;
import com.example.soda.soda20.R2;
import com.example.soda.soda20.app.AppInit;
import com.example.soda.soda20.contract.LoginConstract;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.globle.User;
import com.example.soda.soda20.main.MainActivity;
import com.example.soda.soda20.utils.SoftKeyBoardUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by soda on 2017/8/6.
 */

public class LoginFragment extends Fragment implements LoginConstract.View{
    private final String TAG = "LoginFragment";
    private LoginConstract.Presenter mPresenter;
    @BindView(R2.id.login_progress)
    ProgressBar loginProgressBar;

    private final int LOGIN_SUCCESS = 1;
    private final int LOGIN_FAILSE = 0;
    private Handler mLoginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_SUCCESS:

                    MainActivity.startActivity(getContext());
                    break;
            }
        }
    };

    @Override
    public void setPresenter(LoginConstract.Presenter presenter) {
        mPresenter = presenter;
    }

    User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.bind(this,root);
        initData();

        return root;
    }

    private void initData() {
        user = AppInit.getInstance().userGloble;
        /**
         * 若用户不是第一次登陆
         */
        if (user.getmAccount().length() > 0){
            userId.setText(user.getmAccount());
            if (user.ismRememberPwd()){
                userpwd.setText(user.getmPassword());
            }else {
                checkedPassWord.setChecked(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @BindView(R2.id.user_id)
    EditText userId;
    @BindView(R2.id.user_pwd)
    EditText userpwd;
    @BindView(R2.id.remember_pwd)
    CheckBox checkedPassWord;

    private String useridText;
    private String userpwdText;
    private boolean isPwdChecked;


    @OnClick(R2.id.user_sign_in_button)
    void onSignIn(View view){
        checkUserInput();

//        if (RexUtils.rexCheckPassWord(userpwd.getText().toString().trim())){
            mPresenter.requestSignIn(useridText,userpwdText);
//        }else {
//            Toast.makeText(getContext(),"您输入的密码不符合标准哟",Toast.LENGTH_SHORT).show();
//        }

    }

    @OnClick(R2.id.user_register_button)
    void onRegister(View view){
        checkUserInput();

            mPresenter.createAccount(useridText,userpwdText);

    }

    /**
     * 暂存用户的输入数据，防止回调时数据被更改，隐藏虚拟键盘
     */
    private void checkUserInput(){
        //隐藏软键盘
        SoftKeyBoardUtil.hideSoftKeyBoard(getContext(),userpwd);
        useridText = userId.getText().toString().trim();
        userpwdText = userpwd.getText().toString().trim();
        isPwdChecked = checkedPassWord.isChecked();
        //TODO 密码的验证正则表达式
    }


    @Override
    public void onCreateAccountError(String description) {
        Toast.makeText(getContext(),"该id已经被注册",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateAccountSuccess(String userId, String pwd) {
            mPresenter.requestSignIn(userId, pwd);
    }

    @Override
    public void showLoginProgressBar() {
        loginProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoginSuccess() {
        user.updateOnLogin(useridText,userpwdText,isPwdChecked);
        ModelResponse.getInstance().loadAllConversations();

        Message msg = new Message();
        msg.what = LOGIN_SUCCESS;
        mLoginHandler.sendMessage(msg);
    }
}
