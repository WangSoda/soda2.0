package com.example.soda.cheat_sheets;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by soda on 2017/8/6.
 * 隐藏软键盘的方法
 */

public class SoftKeyBoardHide {
    public void hideSoftKeyBoard(Context context, EditText editText) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //获取软键盘的显示状态
        boolean active = inputMethodManager.isActive();
        if (active)
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }
}
