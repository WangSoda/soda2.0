package com.example.soda.soda20.utils;

import com.example.soda.soda20.data.local.bean.LocalMsg;

import java.util.List;

/**
 * Created by soda on 2017/8/9.
 */

public class DataCheckUtil {
    public static boolean notHaveThisLocalMsg(List<LocalMsg> mData, LocalMsg checkedData) {
        long msgTime = checkedData.getMsgTime();
        for (int i = 0; i < mData.size(); i++) {
            LocalMsg localMsg = mData.get(i);
            long time = localMsg.getMsgTime();
            if (msgTime == time){
                return false;
            }
        }
        return true;
    }
}
