package com.example.soda.utils.sodaImageLoader.CacheInterface.cache_manager;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by soda on 2017/8/8.
 */

public class LruCacheManager {
    private final String TAG = "LruCacheManager";

    private static LruCacheManager instance;
    public static LruCacheManager getInstance(){
        if (instance == null){
            synchronized (LruCacheManager.class){
                if (instance == null){
                    instance = new LruCacheManager();
                }
            }
        }
        return instance;
    }
    private LruCache<String,Bitmap> mLruCache;
    private LruCacheManager(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getHeight() * value.getHeight() / 1024);
            }
        };
    }
    public void putBitmapIntoLruCacheByKey(String key,Bitmap bitmap){
        if (key != null && bitmap != null)
        mLruCache.put(key,bitmap);
        Log.d(TAG,"图片加入缓存");
    }
    public Bitmap getBitmapFromLruCacheByKey(String key){
        Log.d(TAG,"尝试从缓存获取图片" + (mLruCache.get(key) != null));
        return mLruCache.get(key);
    }
}
