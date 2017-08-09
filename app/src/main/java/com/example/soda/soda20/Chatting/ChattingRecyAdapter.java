package com.example.soda.soda20.Chatting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.soda.audio.MediaManager;
import com.example.soda.soda20.R;
import com.example.soda.soda20.app.AppInit;
import com.example.soda.soda20.data.local.bean.LocalMsg;
import com.example.soda.utils.sodaImageLoader.SodaImageLoader;

import java.util.List;

/**
 * Created by soda on 2017/8/8.
 */

public class ChattingRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TXT_OTHERS = 1;
    private static final int IMG_OTHERS = 2;
    private static final int AUDIO_OTHERS = 3;
    private static final int TXT_USER = 4;
    private static final int IMG_USER = 5;
    private static final int AUDIO_USER = 6;

    private List<LocalMsg> mData;
    private Context mContext;

    public ChattingRecyAdapter(List<LocalMsg> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resourceId = 0;
        switch (viewType){
            case TXT_OTHERS:
                resourceId = R.layout.item_others_txt;
                break;
            case IMG_OTHERS:
                resourceId = R.layout.item_others_img;
                break;
            case AUDIO_OTHERS:
                resourceId = R.layout.item_others_audio;
                break;
            case TXT_USER:
                resourceId = R.layout.item_user_txt;
                break;
            case IMG_USER:
                resourceId = R.layout.item_user_img;
                break;
            case AUDIO_USER:
                resourceId = R.layout.item_user_audio;
                break;
        }
        View root = LayoutInflater.from(mContext).inflate(resourceId,parent,false);
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case TXT_OTHERS:
                holder = new othersTXTHolder(root);
                break;
            case IMG_OTHERS:
                holder = new othersIMAGEHolder(root);
                break;
            case AUDIO_OTHERS:
                holder = new othersAUDIOHolder(root);
                break;
            case TXT_USER:
                holder = new userTXTHolder(root);
                break;
            case IMG_USER:
                holder = new userIMAGEHolder(root);
                break;
            case AUDIO_USER:
                holder = new userAUDIOHolder(root);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LocalMsg localMsg = mData.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case TXT_OTHERS:
                othersTXTHolder othersTXTHolder = (othersTXTHolder) holder;
                othersTXTHolder.chatContent.setText(localMsg.getTxtContent());
                break;
            case TXT_USER:
                userTXTHolder userTXTHolder = (userTXTHolder) holder;
                userTXTHolder.chatContent.setText(localMsg.getTxtContent());
                break;
            case AUDIO_OTHERS:
                othersAUDIOHolder othersAUDIOHolder = (othersAUDIOHolder) holder;
                othersAUDIOHolder.audioImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MediaManager.palySound(localMsg.getAudioPath(),null);
                    }
                });
                break;
            case AUDIO_USER:
                userAUDIOHolder userAUDIOHolder = (userAUDIOHolder) holder;
                userAUDIOHolder.audioImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MediaManager.palySound(localMsg.getAudioPath(),null);
                    }
                });
                break;
            case IMG_OTHERS:
                String url = localMsg.getThumbnailUrl();
                othersIMAGEHolder audioHolder = (ChattingRecyAdapter.othersIMAGEHolder) holder;
                ImageView picMessage = audioHolder.picMessage;
//                SodaImageLoader.getInstance(5, SodaImageLoader.Type.FIFO,mContext).loadImage(url,picMessage);
                Glide.with(mContext).load(url).into(picMessage);
                break;
            case IMG_USER:
                String urlUser = localMsg.getThumbnailUrl();
                Log.d("chattingTest","url path" + urlUser);
                userIMAGEHolder imageHolder = (userIMAGEHolder) holder;
//                SodaImageLoader.getInstance(5, SodaImageLoader.Type.FIFO,mContext).loadImage(urlUser,imageHolder.picMessage);
                Glide.with(mContext).load(urlUser).into(imageHolder.picMessage);
                break;

        }

    }

    @Override
    public int getItemViewType(int position) {
        LocalMsg localMsg = mData.get(position);
        String fromUserId = localMsg.getFromUserId();
        String account = AppInit.getInstance().userGloble.getmAccount();
        int type = 0;
        if (account.equals(fromUserId)) {
            //若消息为用户自己发出的
            switch (localMsg.getType()) {
                case LocalMsg.LOCALTXT:
                    type = TXT_USER;
                    break;
                case LocalMsg.LOCALIMAGE:
                    type = IMG_USER;
                    break;
                case LocalMsg.LOCALAUDIO:
                    type = AUDIO_USER;
                    break;
            }
        } else {
            switch (localMsg.getType()) {
                case LocalMsg.LOCALTXT:
                    type = TXT_OTHERS;
                    break;
                case LocalMsg.LOCALIMAGE:
                    type = IMG_OTHERS;
                    break;
                case LocalMsg.LOCALAUDIO:
                    type = AUDIO_OTHERS;
                    break;
            }
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class othersTXTHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView chatContent;
        public othersTXTHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.img_avatar_other_txt);
            chatContent = itemView.findViewById(R.id.txt_content_other_txt);
        }
    }
    class othersIMAGEHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        ImageView picMessage;
        public othersIMAGEHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.img_avatar_other_img);
            picMessage.findViewById(R.id.img_picmessage_other_img);
        }
    }
    class othersAUDIOHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        ImageView audioImg;
        public othersAUDIOHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.img_avatar_other_audio);
            audioImg = itemView.findViewById(R.id.img_audioimg_other_audio);
        }
    }

    class userTXTHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView chatContent;
        public userTXTHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.img_avatar_user_txt);
            chatContent = itemView.findViewById(R.id.txt_content_user_txt);
        }
    }
    class userIMAGEHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        ImageView picMessage;
        public userIMAGEHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.img_avatar_user_img);
            picMessage = itemView.findViewById(R.id.img_picmessage_user_img);
        }
    }
    class userAUDIOHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        ImageView audioImg;
        public userAUDIOHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.img_avatar_user_audio);
            audioImg = itemView.findViewById(R.id.img_audioimg_user_audio);
        }
    }


}
