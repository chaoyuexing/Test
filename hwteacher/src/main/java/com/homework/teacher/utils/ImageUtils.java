package com.homework.teacher.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class ImageUtils {


    public static void loadImage(Context mContext, ImageView imageView,String url) {
        Glide.with(mContext).load(url).into(imageView);
    }

    public static Bitmap CreateBitmap(int width, int height, ByteBuffer data) {
        Bitmap bm = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        data.position(0);
        bm.copyPixelsFromBuffer(data);
        return bm;
    }

    public static Bitmap CreateBitmap(int width, int height, byte[] data) {
        Bitmap bm = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        bm.copyPixelsFromBuffer(ByteBuffer.wrap(data));
        return bm;
    }


    public static void saveBitmap(Bitmap bm, String fileName) {
        File f = new File("/sdcard/", fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static String saveBitmapUri(Bitmap bm,String fileName) {
        //新建文件夹用于存放裁剪后的图片
        String facetureFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"teacher/";
        try {
            //打开文件输出流
            File file = new File(facetureFilePath);
            if (!file.exists()) {
                file.mkdir();
            }
            File pngFile =  new File(facetureFilePath,fileName);
            FileOutputStream fos = new FileOutputStream(pngFile);
            //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            //返回File类型的Uri
            return pngFile.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static Bitmap readImage(String file, boolean isAssets) {
        //String assetsFile= "photo/s2.png";
        Bitmap bitmap = null;
        if (isAssets) {
            InputStream is = null;
//            try {
//                is = OpenGLTextureUtils.mContext.getAssets().open(file);
//            } catch (IOException ioe) {
//                is = null;
//            }

            if (is == null)
                return null;

            bitmap = BitmapFactory.decodeStream(is);
        } else
            bitmap = BitmapFactory.decodeFile(file);
        //bitmap.recycle();
        return bitmap;
    }

//    public static Bitmap readImage(int rDrawableId) {
//        Resources res = OpenGLTextureUtils.mContext.getResources();
//        Bitmap bmp = BitmapFactory.decodeResource(res, rDrawableId);

//        return bmp;
//    }

    public static byte[] Bitmap2Bytes(Bitmap bitmap) {
        int bytes = bitmap.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buf);
        byte[] data = buf.array();

        return data;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        return baos.toByteArray();
    }


    /**
     *
     */
    public static Uri PhotosUri(Context mContext) {
        Uri outPutUri, tempUri;
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        outPutUri = Uri.fromFile(file);
        if (Build.VERSION.SDK_INT >= 23) {
            tempUri = getTempUri(mContext);
        } else {
            tempUri = outPutUri;
        }
        return tempUri;
    }


    /**
     * 获取一个临时的Uri, 文件名随机生成
     *
     * @param context
     * @return
     */
    public static Uri getTempUri(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File file = new File(Environment.getExternalStorageDirectory(), "/images/" + timeStamp + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return getUriForFile(context, file);
    }

    /**
     * 创建一个用于拍照图片输出路径的Uri (FileProvider)
     *
     * @param context
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, getFileProviderName(context), file);
    }

    public final static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }


}
