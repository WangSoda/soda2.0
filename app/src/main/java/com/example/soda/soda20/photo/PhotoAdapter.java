package com.example.soda.soda20.photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.soda.soda20.R;
import com.example.soda.utils.sodaImageLoader.SodaImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soda on 2017/8/8.
 */

public class PhotoAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<String> mData;
    List<String> resultPathList;

    public List<String> getResultPathList() {
        return resultPathList;
    }

    public PhotoAdapter(Context context, List<String> mPathList) {
        mContext = context;
        mData = mPathList;

        resultPathList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo,parent,false);
        PhotoViewHolder holder = new PhotoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PhotoViewHolder viewHolder = (PhotoViewHolder) holder;
        ImageView imageView = viewHolder.photoItem;
        final String path = mData.get(position);
//        SodaImageLoader.getInstance(5, SodaImageLoader.Type.FIFO,mContext).loadImage(path,imageView);
        Glide.with(mContext).load(path).into(imageView);
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.checkBox.isChecked()){
                    if (!resultPathList.contains(path)){

                        resultPathList.add(path);
                    }
                }else {
                    if (resultPathList.contains(path)){

                        resultPathList.remove(path);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder{
        ImageView photoItem;
        CheckBox checkBox;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            photoItem = itemView.findViewById(R.id.img_photos_item);
            checkBox = itemView.findViewById(R.id.img_check);
        }
    }
}
