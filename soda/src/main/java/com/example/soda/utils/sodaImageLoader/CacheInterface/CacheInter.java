package com.example.soda.utils.sodaImageLoader.CacheInterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;


import com.example.soda.utils.sodaImageLoader.CacheInterface.cache_manager.DiskLruCacheManager;
import com.example.soda.utils.sodaImageLoader.CacheInterface.cache_manager.LruCacheManager;
import com.example.soda.utils.sodaImageLoader.CacheInterface.cacheutil.ImageResizer;
import com.example.soda.utils.sodaImageLoader.CacheInterface.cacheutil.InternetUtil;
import com.example.soda.utils.sodaImageLoader.CacheInterface.cacheutil.LocalUtil;
import com.example.soda.utils.sodaImageLoader.CacheInterface.cacheutil.MD5Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

/**
 * Created by soda on 2017/8/8.
 */

public class CacheInter {
    private final String TAG = "CacheInter";

    private DiskLruCacheManager diskLruCacheManager;
    private LruCacheManager lruCacheManager;
    private MD5Util md5Util;
    private ImageResizer mImageResizer;
    private static Context mContext;

    private CacheInter() {
        diskLruCacheManager = DiskLruCacheManager.getInstance(mContext);
        lruCacheManager = LruCacheManager.getInstance();
        md5Util = new MD5Util();
        mImageResizer = new ImageResizer();
    }

    private static CacheInter instance;

    public static CacheInter getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (instance == null) {
            synchronized (CacheInter.class) {
                if (instance == null) {
                    instance = new CacheInter();
                }
            }
        }
        return instance;
    }

    public interface SaveCallback {
        void onSaveSuccess();

        void onFail();
    }

    public void saveBitmapFromInternetUrl(final String imgUrlPath, final SaveCallback callback) {

        String key = md5Util.getHashKeyByUrl(imgUrlPath);

        OutputStream outputStream = null;
        if (outputStream == null) {
            synchronized (CacheInter.class) {
                if (outputStream == null) {
                    outputStream = diskLruCacheManager.getOutPutStreamByHaskKey(key);
                    if (outputStream != null) {
                        if (InternetUtil.downloadUrlToStream(imgUrlPath, outputStream)) {
                            diskLruCacheManager.commit();
                            callback.onSaveSuccess();
                        } else {
                            diskLruCacheManager.abort();
                            Log.d(TAG, "图片放弃提交");
                            callback.onFail();
                        }
                    }
                }
            }
        }


    }

    public void saveBitmapfromDiskPath(final String imgLocalPath, final SaveCallback callback) {
        Log.d(TAG, "图片本地路径" + imgLocalPath);
        String key = md5Util.getHashKeyByUrl(imgLocalPath);
        File localFile = new File(imgLocalPath);
        if (localFile.exists() && localFile.isFile()) {
            try {
                OutputStream outputStream = null;
                if (outputStream == null) {
                    synchronized (CacheInter.class) {
                        if (outputStream == null) {
                            outputStream = diskLruCacheManager.getOutPutStreamByHaskKey(key);
                            FileInputStream inputStream = new FileInputStream(imgLocalPath);
                            if (inputStream != null && outputStream != null) {
                                if (LocalUtil.saveLocalBitMap(inputStream, outputStream)) {

                                    diskLruCacheManager.commit();
                                    Log.d(TAG, "本地图片提交" + imgLocalPath);
                                    callback.onSaveSuccess();
                                } else {
                                    diskLruCacheManager.abort();
                                    callback.onFail();
                                    Log.d(TAG, "本地图片放弃提交" + imgLocalPath);
                                }
                            }
                        }
                    }
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 通过url同步获取bitmap
     *
     * @param url
     * @return
     */
    public Bitmap getImageFromDiskLruCacheByUrl(String url) {
        String key = md5Util.getHashKeyByUrl(url);
        Bitmap bitmap = diskLruCacheManager.getBitMapByHashKey(key, mImageResizer, 640, 640);
        Log.d(TAG, "加载本地bitmap bitmap" + (bitmap != null));

        //每次从本地缓存中获取一个bitmap 就将这个对象加入lruCache
        if (key != null && bitmap != null)
            lruCacheManager.putBitmapIntoLruCacheByKey(key, bitmap);

        return bitmap;
    }

    public Bitmap getImageFromLruCacheByUrl(String url) {
        String key = md5Util.getHashKeyByUrl(url);
        Bitmap bitmap = lruCacheManager.getBitmapFromLruCacheByKey(key);
        return bitmap;
    }
}
