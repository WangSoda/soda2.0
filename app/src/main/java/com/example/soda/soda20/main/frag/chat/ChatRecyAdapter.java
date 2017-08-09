package com.example.soda.soda20.main.frag.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soda.soda20.Chatting.ChattingActivity;
import com.example.soda.soda20.R;
import com.example.soda.soda20.app.AppInit;
import com.example.soda.soda20.data.local.bean.LocalMsg;
import com.example.soda.soda20.utils.MessageUtil;

import java.util.List;

/**
 * Created by soda on 2017/8/7.
 */

public class ChatRecyAdapter extends RecyclerView.Adapter<ChatRecyAdapter.ChatViewHolder> {
    List<LocalMsg> mDataList;
    Context mContext;
    public ChatRecyAdapter(List<LocalMsg> dataList,Context context){
        mDataList = dataList;
        mContext = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat,parent,false);
        ChatViewHolder viewHolder = new ChatViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        List<LocalMsg> msgs = MessageUtil.getLastChatContentList(mDataList);
        final LocalMsg localMsg = msgs.get(position);
//        holder.userAvatar//头像在未定义之前暂不设定
        holder.userNickName.setText(localMsg.getFromUserId());
        holder.chatTime.setText(setTime(localMsg.getMsgTime()));
        switch (localMsg.getType()){
            case LocalMsg.LOCALTXT:
                holder.chatContent.setText(localMsg.getTxtContent());
                break;
            case LocalMsg.LOCALIMAGE:
                holder.chatContent.setText("[图片内容]");
                break;
            case LocalMsg.LOCALAUDIO:
                holder.chatContent.setText("[语音消息]");
                break;
            default:
                break;
        }

        holder.itemholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = AppInit.getInstance().userGloble.getmAccount();
                if (!account.equals(localMsg.getFromUserId())){
                    ChattingActivity.startActivity(mContext,localMsg.getFromUserId());
                }
            }
        });
    }

    private String setTime(long msgTime) {
        long l = System.currentTimeMillis() - msgTime;
        int time = (int) (l / 1000 / 60);
        if (time < 60){return time + "分钟前";}
        time /= 60;
        if (time < 60){return time + "小时前";}
        time /= 24;
        return time + "天前";

    }

    @Override
    public int getItemCount() {
        List<LocalMsg> msgs = MessageUtil.getLastChatContentList(mDataList);
        return msgs.size();
    }



    class ChatViewHolder extends RecyclerView.ViewHolder{
        ImageView userAvatar;
        TextView userNickName;
        TextView chatContent;
        TextView chatTime;
        View itemholder;
        public ChatViewHolder(View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar_chat);
            userNickName = itemView.findViewById(R.id.user_nickname_chat);
            chatContent = itemView.findViewById(R.id.chat_content_chat);
            chatTime = itemView.findViewById(R.id.chat_time_chat);
            itemholder = itemView;
        }
    }
}
