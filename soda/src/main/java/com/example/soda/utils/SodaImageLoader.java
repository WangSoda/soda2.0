package com.example.soda.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by soda on 2017/8/8.
 */

public class SodaImageLoader {

    private static final String TAG = "SodaImageLoader";

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
    private SodaImageLoader(int threadCount,Type type){
        init(threadCount,type);
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
            Log.d(TAG,"使用先进先出策略获取到任务");
            return mTaskQueue.removeFirst();
        }else if (mType == Type.LIFO){
            Log.d(TAG,"使用后进先出策略获取到任务");
            return mTaskQueue.removeLast();
        }
        return null;
    }

    public static SodaImageLoader getInstance(int threadCount,Type type)
    {
        if (mInstance == null){
            synchronized (SodaImageLoader.class){
                if (mInstance == null){
                    mInstance = new SodaImageLoader(threadCount,type);
                }
            }
        }
        return mInstance;
    }

    public void loadImage(String path, ImageView imageView){
        imageView.setTag(path);
        if (mUIHandler == null){

            mUIHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    String message = (String) msg.obj;

                    Log.d(TAG,"UI线程中的handler 得到信息 " + message);
                    //obj中获取的imageiew的tag 如果和当前的path相同 则设定图片
                }
            };

        }

        //通过各种途径获得bitmap
        Bitmap bitmap = null;


        if (bitmap != null){
            Message message = Message.obtain();
            message.obj = "这里的信息是可以携带对象的（本地获得）";
            mUIHandler.sendMessage(message);
        }else {
            addTask(new Runnable() {
                @Override
                public void run() {
                    //加载图片
                    //压缩图片
                    //图片加入缓存
                    //图片加入本地缓存

                    Message message = Message.obtain();
                    message.obj = "通过缓存或者网络获取到了图片";
                    mUIHandler.sendMessage(message);

                    mSemaphoreThreadPool.release();//释放一个信号量
                    Log.d(TAG,"释放了一个任务结束的信号量");
                }
            });
        }

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
