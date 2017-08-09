package com.example.soda.utils.sodaImageLoader.CacheInterface.cacheutil;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

import static android.graphics.BitmapFactory.Options;
import static android.graphics.BitmapFactory.decodeResource;

/**
 * Created by soda on 2017/8/4.
 * 图片压缩的标准类
 */

public class ImageResizer {
    private static final String TAG = "ImageResizer";

    public ImageResizer(){}

    public Bitmap decodeSampledBitmapFromResource(Resources res,
                                                  int resId,int reqWidth,int reqHeight){
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        decodeResource(res,resId,options);

        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    /**
     * 采样率的计算方法
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        if ((reqWidth == 0) || (reqHeight == 0)) {
            return 1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(TAG,"orgin, width =" + width + "height = " + height);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            inSampleSize *= 2;
            final int halfHeight = height / inSampleSize;
            final int halfWidth = width / inSampleSize;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth){
                inSampleSize *= 2;
            }
        }
        Log.d(TAG,"sampleSize:" + inSampleSize);
        return inSampleSize;
    }


    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fileDescriptor, int reqWith, int reqHeight) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
        options.inSampleSize = calculateInSampleSize(options,reqWith,reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
    }
}
