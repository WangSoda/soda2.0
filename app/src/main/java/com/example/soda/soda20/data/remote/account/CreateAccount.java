package com.example.soda.soda20.data.remote.account;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by soda on 2017/8/6.
 */

public class CreateAccount {
    public static void createAccount(String userId,String uerpwd) throws HyphenateException {
        EMClient.getInstance().createAccount(userId,uerpwd);
    }
}
