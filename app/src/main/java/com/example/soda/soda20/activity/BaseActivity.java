package com.example.soda.soda20.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();

        initViews();
    }

    /**
     * 该方法使用前应设定自己的布局
     */
    protected abstract void initLayout();



    protected abstract void initViews();


}
