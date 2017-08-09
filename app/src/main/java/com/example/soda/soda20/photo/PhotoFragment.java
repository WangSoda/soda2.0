package com.example.soda.soda20.photo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soda.soda20.R;
import com.example.soda.soda20.R2;
import com.example.soda.soda20.activity.frag.ChatBaseFragment;
import com.example.soda.soda20.contract.ChatConstract;
import com.example.soda.soda20.data.local.bean.LocalMsg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by soda on 2017/8/8.
 */

public class PhotoFragment extends ChatBaseFragment {

    private List<String> mPathList;
    private PhotoAdapter mAdapter;
    @BindView(R2.id.recycler_photos)
    RecyclerView mRecycler;
    GridLayoutManager mLayoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photos,container,false);
        ButterKnife.bind(this,root);
        initData();
        return root;
    }

    private void initData() {
        mLayoutManager = new GridLayoutManager(getContext(),2);
        mPathList = new ArrayList<>();
        mAdapter = new PhotoAdapter(getContext(),mPathList);

        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);
    }

    Handler handleGetPhotos = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    };
    @Override
    public void onGetPhotosPathList(List<String> allPathList) {
        for (int i = 0; i < allPathList.size(); i++) {
            mPathList.add(allPathList.get(i));
        }
        handleGetPhotos.sendEmptyMessage(0);
    }

    private List<String> resultPathList;

    /**
     * 当用户点击发送按钮时，将数据添加到该对象中，由activity进行获取
     * @return
     */
    public List<String> getResultPathList() {
        return resultPathList;
    }

    @Override
    public void onSendImageList() {
        resultPathList = mAdapter.getResultPathList();
    }
}
