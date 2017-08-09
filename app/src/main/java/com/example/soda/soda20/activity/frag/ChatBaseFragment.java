package com.example.soda.soda20.activity.frag;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.soda.soda20.contract.ChatConstract;
import com.example.soda.soda20.data.local.bean.LocalMsg;
import com.example.soda.soda20.login.LoginActivity;

import java.io.File;
import java.util.List;

/**
 * Created by soda on 2017/8/7.
 */

public abstract class ChatBaseFragment extends Fragment implements ChatConstract.View{
    protected ChatConstract.Presenter mPresenter;
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

    @Override
    public void onLogOut() {
        Toast.makeText(getContext(),"您已登出，返回登录界面",Toast.LENGTH_LONG).show();
        LoginActivity.startActivity(getContext());
        getActivity().finish();
    }

    @Override
    public String getChatWithUserId() {
        return null;
    }

    @Override
    public void setPresenter(ChatConstract.Presenter presenter) {
        mPresenter = presenter;
    }



    @Override
    public void onGetFriendsList(List<String> friends) {

    }

    @Override
    public void onGetMessage(List<LocalMsg> localMsgs) {

    }

    @Override
    public void onSendImageList() {

    }

    @Override
    public void aleartUser() {
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(getContext())
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[]{0, 1000, 1000, 1000}).build();
        manager.notify(1,notification);


    }
}
