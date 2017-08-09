package com.example.soda.soda20.Chatting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.soda.soda20.R;
import com.example.soda.soda20.R2;
import com.example.soda.soda20.activity.ChatBaseActivity;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.data.local.bean.Constant;
import com.example.soda.soda20.photo.PhotoActivity;
import com.example.soda.ui.AudioRecorderButton;
import com.example.soda.utils.ActivityUtils;
import com.example.soda.utils.PermissionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ChattingActivity extends ChatBaseActivity {


    public static void startActivity(Context context, String fromUserId) {
        Intent intent = new Intent(context,ChattingActivity.class);
        intent.putExtra("fromUserId",fromUserId);
        context.startActivity(intent);
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_chatting);
    }

    private String ChattingWithUserId;

    private ChattingFragment mFragment;
    private ModelResponse mModel;
    private ChattingPresenter mPresenter;
    @Override
    protected void loadData() {
        Intent intent = getIntent();
        ChattingWithUserId = intent.getStringExtra("fromUserId");
        setEnterListener();


        mFragment = (ChattingFragment) getSupportFragmentManager().findFragmentById(R.id.chatting_content_fragment);
        if (mFragment == null){
            mFragment = new ChattingFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mFragment,R.id.chatting_content_fragment);
        }
        if (mModel == null){
            mModel = ModelResponse.getInstance();
        }
        if (mPresenter == null){
            mPresenter = new ChattingPresenter(mModel,mFragment);
            mFragment.setPresenter(mPresenter);
        }
        mFragment.setChattingWithUserId(ChattingWithUserId);

        //为录制按钮设置回调
        userEnterAudio.setAudioFinishRecorderListener(mPresenter);
    }

    @BindView(R2.id.edit_user_enter)
    EditText userEnterEdit;
    /**
     * 文字和语音输入按钮的切换
     */
    @BindView(R2.id.audio_user_enter)
    AudioRecorderButton userEnterAudio;
    private void showOrHideAudioButton() {
        int visibility = userEnterEdit.getVisibility();
        if (visibility == View.VISIBLE){
            userEnterEdit.setVisibility(View.GONE);
            userEnterAudio.setVisibility(View.VISIBLE);
        }else {
            userEnterEdit.setVisibility(View.VISIBLE);
            userEnterAudio.setVisibility(View.GONE);
        }
    }

    /**
     * 当用户输入回车后进行消息的发送
     */
    private void setEnterListener() {
        userEnterEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.KEYCODE_ENTER == i){
                    String chatContent = userEnterEdit.getText().toString().trim();

                    if (chatContent.length() == 0){
                        Toast.makeText(ChattingActivity.this,"您好像忘了输入内容，我还没学会读心术哦！",Toast.LENGTH_SHORT).show();
                    }else {
                        userEnterEdit.setText("");
                        mPresenter.SendTXTMessage(chatContent);
                    }
                    return true;
                }
                return false;
            }
        });
    }
    @BindView(R2.id.media_message_layout)
    RelativeLayout mediaLayout;
    @BindView(R2.id.media_message)
    ImageView btnmediaMessage;

    /**
     * 媒体消息列表的展示
     * @param view
     */
    @OnClick(R2.id.media_message)
    void hideOrShowMediaLayout(View view){
        int visibility = mediaLayout.getVisibility();
        if (visibility == View.VISIBLE){
            mediaLayout.setVisibility(View.GONE);
            btnmediaMessage.setImageResource(R.mipmap.btn_add);

        }else {
            mediaLayout.setVisibility(View.VISIBLE);
            btnmediaMessage.setImageResource(R.mipmap.btn_minus);
        }
    }

    /**
     * 用户进行文字和语音的窃权之前 先检查权限是否符合
     * @param view
     */
    @OnClick(R2.id.btn_inputmethod)
    void inputMethod(View view){
        if ( !PermissionUtils.checkPermission(ChattingActivity.this, Manifest.permission.RECORD_AUDIO) ){
            new PermissionUtils().requestPermissions(ChattingActivity.this,Manifest.permission.RECORD_AUDIO,PermissionUtils.RECORD_AUDIO);
        }else {
            showOrHideAudioButton();
        }
    }
    @OnClick(R2.id.image_message)
    void selectLocalPic(View view){
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivityForResult(intent, Constant.REQUEST_GET_LOCAL_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.REQUEST_GET_LOCAL_PIC:
                if (resultCode == RESULT_OK){
                    Bundle pathBundle = data.getBundleExtra("pathBundle");
                    if (pathBundle != null){
                        ArrayList<String> pathList = pathBundle.getStringArrayList("pathList");
                        if (pathList != null){
                            for (int i = 0; i < pathList.size(); i++) {
                                String imgPath = pathList.get(i);
                                mPresenter.sendImageMessage(imgPath,false,mFragment.getChatWithUserId());
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PermissionUtils.RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showOrHideAudioButton();
                }else {
                    Toast.makeText(this,"该应用需要读写权限才能更好的工作",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

}
