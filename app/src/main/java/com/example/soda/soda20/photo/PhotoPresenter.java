package com.example.soda.soda20.photo;

import com.example.soda.soda20.contract.ChatConstract;
import com.example.soda.soda20.data.ModelResponse;
import com.example.soda.soda20.data.local.LocalDataSourceCallBack;
import com.example.soda.soda20.data.local.bean.ImageFloder;
import com.example.soda.soda20.presenter.ChatBasePresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soda on 2017/8/8.
 */

public class PhotoPresenter extends ChatBasePresenter implements LocalDataSourceCallBack.GetLocalImagesCallBack{

    public PhotoPresenter(ModelResponse model, ChatConstract.View view) {
        super(model, view);
        model.getLocalImagesList(this);
    }

    @Override
    public void onNoExternalStorage() {

    }

    @Override
    public void onGetLocalImageData(List<ImageFloder> mImageFloders, File mImageDir, int totalCount) {
        List<String> allPathList = new ArrayList<>();
        for (int i = 0; i < mImageFloders.size(); i++) {
            String dir = mImageFloders.get(i).getDir();
            File file = new File(dir);
            String dirName = file.getAbsolutePath();
            if (file.isDirectory()){
                String[] list = file.list();
                for (int j = 0; j < list.length; j++) {
                    allPathList.add(dirName + "/" + list[j]);
                }
            }
        }
        mView.onGetPhotosPathList(allPathList);
    }

    @Override
    public void onStartGetImage() {

    }

    public void onSendImageList() {
        mView.onSendImageList();
    }
}
