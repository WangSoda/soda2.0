package com.example.soda.soda20.data.local.bean;

/**
 * Created by soda on 2017/8/7.
 */

public class LocalMsg {
    public static final int LOCALTXT = 1;
    public static final int LOCALIMAGE = 2;
    public static final int LOCALAUDIO = 3;
    public LocalMsg(String fromUserId, int type, long msgTime, String txtOrImgPath) {
        this.fromUserId = fromUserId;
        this.type = type;
        this.msgTime = msgTime;
        if (type == LOCALTXT){
            txtContent = txtOrImgPath;
        }else if (type == LOCALIMAGE){
            thumbnailUrl = txtOrImgPath;
        }
    }

    public LocalMsg(String fromUserId, int type, long msgTime, String audioPath, int audioLength) {
        this.fromUserId = fromUserId;
        this.type = type;
        this.msgTime = msgTime;
        this.audioPath = audioPath;
        this.audioLength = audioLength;
    }

    private String fromUserId;

    private int type;
    private long msgTime;

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    private String txtContent;
    //image
    private String thumbnailUrl;
    //audio
    private String audioPath;
    private int audioLength;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTxtContent() {
        return txtContent;
    }

    public void setTxtContent(String txtContent) {
        this.txtContent = txtContent;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public int getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(int audioLength) {
        this.audioLength = audioLength;
    }
}
