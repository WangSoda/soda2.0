package com.example.soda.utils;

/**
 * Created by soda on 2017/8/6.
 */

public class RexUtils {
    public static boolean rexCheckPassWord(String input){
        String regStr = "^([a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]){6,20}$";
        return input.matches(regStr);
    }
}
