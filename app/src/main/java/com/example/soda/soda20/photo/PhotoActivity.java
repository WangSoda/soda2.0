package com.example.soda.soda20.photo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.soda.soda20.R;
import com.example.soda.soda20.activity.ChatBaseActivity;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.utils.ActivityUtils;

import java.util.ArrayList;

import butterknife.OnClick;

public class PhotoActivity extends ChatBaseActivity {
    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_photo);
    }

    private ModelResponse mModel;
    private PhotoFragment mFragment;
    private PhotoPresenter mPresenter;

    Intent mIntent;
    @Override
    protected void loadData() {
        mIntent = getIntent();
        if (mModel == null){
            mModel = ModelResponse.getInstance();
            mModel.setContentResolver(getContentResolver());
        }
        mFragment = (PhotoFragment) getSupportFragmentManager().findFragmentById(R.id.frame_photos_show);
        if (mFragment == null){
            mFragment = new PhotoFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mFragment,R.id.frame_photos_show);
        }
        if (mPresenter == null){
            mPresenter = new PhotoPresenter(mModel,mFragment);
        }
        mFragment.setPresenter(mPresenter);
    }
    @OnClick(R.id.btn_send_pic)
    void onSendImageList(View view){
        mPresenter.onSendImageList();
        Bundle bundle = new Bundle();
        ArrayList<String> resultPathList = (ArrayList<String>) mFragment.getResultPathList();
        bundle.putStringArrayList("pathList", resultPathList);
        mIntent.putExtra("pathBundle",bundle);
        this.setResult(RESULT_OK,mIntent);
        finish();
    }
}
