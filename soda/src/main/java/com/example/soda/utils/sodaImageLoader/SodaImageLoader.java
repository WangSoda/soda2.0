package com.example.soda.utils.sodaImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.soda.utils.sodaImageLoader.CacheInterface.CacheInter;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by soda on 2017/8/8.
 */

public class SodaImageLoader {

    private static final String TAG = "SodaImageLoader";

    private CacheInter mCacheInter;

    private static SodaImageLoader mInstance;

    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;
    private Type mType = Type.LIFO;
    private LinkedList<Runnable> mTaskQueue;

    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    private Handler mUIHandler;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;


    public enum Type{
        FIFO,LIFO
    }
    private SodaImageLoader(int threadCount, Type type, Context context){
        init(threadCount,type);
        mCacheInter = CacheInter.getInstance(context);
    }

    private void init(int threadCount, Type type) {
        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {

                        try {
                            mSemaphoreThreadPool.acquire();
                            Log.d(TAG,"请求了一个信号量");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG,"获取到添加的消息，开始进行任务获取");
                        mThreadPool.execute(getTask());

                    }
                };
                //释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Log.d(TAG,"当线程池创建完毕，释放一个信号量");
                Looper.loop();
            }
        };
        mPoolThread.start();

        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        mType = type;

        mSemaphoreThreadPool = new Semaphore(threadCount);//创建信号量用于阻塞任务的获取，从而从策略提高效率
        Log.d(TAG,"初始化信号量为"  + threadCount);
    }

    /**
     * 从任务队列取出一个方法
     * @return
     */
    private Runnable getTask() {
        if (mType == Type.FIFO){
            return mTaskQueue.removeFirst();
        }else if (mType == Type.LIFO){
            return mTaskQueue.removeLast();
        }
        return null;
    }

    public static SodaImageLoader getInstance(int threadCount, Type type, Context context)
    {
        if (mInstance == null){
            synchronized (SodaImageLoader.class){
                if (mInstance == null){
                    mInstance = new SodaImageLoader(threadCount,type,context);
                }
            }
        }
        return mInstance;
    }

    class ImageBeanHolder {
        String path;
        Bitmap bitmap;
        ImageView imageView;


        public ImageBeanHolder(String path, ImageView imageView, Bitmap bitmap) {
            this.path = path;
            this.imageView = imageView;
            this.bitmap = bitmap;
        }
    }

    public void loadImage(final String path, final ImageView imageView){
        imageView.setTag(path);
        if (mUIHandler == null){
            mUIHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    ImageBeanHolder beanHolder = (ImageBeanHolder) msg.obj;
                    Bitmap bitmap = beanHolder.bitmap;
                    ImageView view = beanHolder.imageView;
                    String url = beanHolder.path;

                    if (view.getTag().toString().equals(url)){
                        view.setImageBitmap(bitmap);
                    }

                    //obj中获取的imageiew的tag 如果和当前的path相同 则设定图片
                }
            };
        }

        //通过各种途径获得bitmap
        Bitmap bitmap = null;
        bitmap = mCacheInter.getImageFromLruCacheByUrl(path);
        if (bitmap == null){
            Log.d(TAG,"尝试从硬盘获取");
            bitmap = mCacheInter.getImageFromDiskLruCacheByUrl(path);
        }else {
            Log.d(TAG,"从缓存获取图片成功");
        }

        if (bitmap != null){
            Log.d(TAG,"从本地获取图片成功");
            sendMessageToUIThread(path,imageView,bitmap);

        }else {
            addTask(new Runnable() {
                @Override
                public void run() {

                        mCacheInter.saveBitmapfromDiskPath(path, new CacheInter.SaveCallback() {
                            @Override
                            public void onSaveSuccess() {
                                Bitmap bitmap = mCacheInter.getImageFromDiskLruCacheByUrl(path);

                                if (bitmap != null){
                                    sendMessageToUIThread(path,imageView,bitmap);
                                }

                                mSemaphoreThreadPool.release();//释放一个信号量
                            }

                            @Override
                            public void onFail() {
                                mSemaphoreThreadPool.release();//释放一个信号量
                            }
                        });


                }
            });
        }

    }

    private void sendMessageToUIThread(String path, ImageView imageView, Bitmap bitmap) {
        Message message = Message.obtain();
        ImageBeanHolder holder = new ImageBeanHolder(path,imageView,bitmap);

        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);

        //要判断mPoolThreadHandler
        try {
            if (mPoolThread == null)
                mSemaphorePoolThreadHandler.acquire();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0x110);
        Log.d(TAG,"任务添加，消息发出");
    }
}
