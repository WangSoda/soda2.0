package com.example.soda.utils.sodaImageLoader.CacheInterface.cacheutil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by soda on 2017/8/8.
 */

public class LocalUtil {
    public static boolean saveBitmapToStream(FileInputStream inputStream, OutputStream outputStream) {
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        out = new BufferedOutputStream(out, 8 * 1024);
        in = new BufferedInputStream(in,8 * 1024);
        int b;
        try {
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean saveLocalBitMap(FileInputStream inputStream, OutputStream outputStream){
        byte[] b = new byte[1024];
        int size = 0;
        try {
            while ((size = inputStream.read(b))!= -1){
                outputStream.write(b,0,size);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
