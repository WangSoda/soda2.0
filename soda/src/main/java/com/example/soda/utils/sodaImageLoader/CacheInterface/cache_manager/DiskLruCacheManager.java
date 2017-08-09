package com.example.soda.utils.sodaImageLoader.CacheInterface.cache_manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;


import com.example.soda.utils.sodaImageLoader.CacheInterface.cache_manager.DiskLruCache.DiskLruCache;
import com.example.soda.utils.sodaImageLoader.CacheInterface.cacheutil.ImageResizer;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by soda on 2017/8/8.
 */

public class DiskLruCacheManager {
    private static final int VALUE_COUNT = 1;
    private static final long MAX_SIZE = 10 * 1024 * 1024;
    private static final int APPVERSION = 1;
    private static final int OUTPUTSTREAM_INDEX = 0;

    private String TAG = "DiskLruCacheManager";
    private DiskLruCache mDiskLruCache;
    private static Context mContext;

    /**
     * 强引用单例模式存在
     * @param context
     * @return
     */
    public static DiskLruCacheManager getInstance(Context context){
        mContext = context.getApplicationContext();
        if (instance == null){
            synchronized(DiskLruCacheManager.class){
                if (instance == null){
                    instance = new DiskLruCacheManager();
                }
            }
        }
        return instance;
    }
    public Bitmap getBitMapByHashKey(String key, ImageResizer resizer, int reqWith, int reqHeight){
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(OUTPUTSTREAM_INDEX);
                FileDescriptor fd = fileInputStream.getFD();
                bitmap = resizer.decodeSampledBitmapFromFileDescriptor(fd,reqWith,reqHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private DiskLruCache.Editor edit;
    public OutputStream getOutPutStreamByHaskKey(String key){
        try {
            edit = mDiskLruCache.edit(key);
            if (edit != null){
                OutputStream outputStream = edit.newOutputStream(OUTPUTSTREAM_INDEX);
                return outputStream;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void commit(){
        if (edit != null){
            try {
                edit.commit();
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void abort(){
        if (edit != null){
            try {
                edit.abort();
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private DiskLruCacheManager(){
        File cachePath = getDiskCacheDir(mContext,"DiskBitmapLrudir");
        if (!cachePath.exists()){
            cachePath.mkdirs();
        }

        try {
            mDiskLruCache = DiskLruCache.open(cachePath,APPVERSION,VALUE_COUNT,MAX_SIZE);
            Log.d(TAG,"本地缓存对象创建成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"本地缓存对象创建失败" + e.getMessage());
        }
    }


    private static DiskLruCacheManager instance;

    /**
     * 获得本地文件存储的缓存路径，若外部存储不存在，则使用内部存储
     * @param context
     * @param fileName
     * @return
     */
    private File getDiskCacheDir(Context context, String fileName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()){
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }
        Log.d(TAG,"获取到磁盘路径为" + cachePath);
        return new File(cachePath + File.separator + fileName);
    }
}
