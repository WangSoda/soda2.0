package com.example.soda.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by soda on 2017/8/6.
 */

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";

    public static final int WRITE_EXTERNAL_STORAGE = 1;
    public static final int RECORD_AUDIO = 2;

    public static boolean checkPermission(Context context, String permissionType) {
        return ContextCompat.checkSelfPermission(context, permissionType) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(Activity activity,String permissionType,int requestCode){
        ActivityCompat.requestPermissions(activity,new String[]{permissionType},requestCode);
    }


}
