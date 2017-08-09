package com.example.soda.soda20.main.frag.friend;

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

import java.util.List;

/**
 * Created by soda on 2017/8/7.
 */

public class FriendRecyAdapter extends RecyclerView.Adapter<FriendRecyAdapter.FriendViewHolder> {
    private Context mContext;
    private List<String> mData;

    public FriendRecyAdapter(Context context, List<String> friendsList) {
        mContext = context;
        mData = friendsList;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friend,parent,false);
        FriendViewHolder holder = new FriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, final int position) {
        String nickName = mData.get(position);
        holder.userNickname.setText(nickName);

        holder.holderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = AppInit.getInstance().userGloble.getmAccount();
                if (!account.equals(mData.get(position))){
                    ChattingActivity.startActivity(mContext,mData.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder{
        ImageView userAvatar;
        TextView userNickname;
        View holderItem;
        public FriendViewHolder(View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar_friend);
            userNickname = itemView.findViewById(R.id.user_nickname_friend);

            holderItem = itemView;
        }
    }
}
