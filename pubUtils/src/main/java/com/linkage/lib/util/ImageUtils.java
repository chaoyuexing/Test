package com.linkage.lib.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;

public class ImageUtils {
	
	/**
    * 对大于一定边宽的图片进行压缩，返回被压缩后的图片
    * 
    * @param path
    * @param bitmapZip
    * @return
    */
   public static Bitmap loadBitmapFromPathLimitSiding(String path, int limitWidth, int limitHeight)
   {
       Bitmap bitmap = null;
       String orientation = null;
       if (path != null)
       {
	    	File file = new File(path);
	    	if (!file.exists()) 
	    	{
	    		LogUtils.e("图片文件不存在");
				return null;
			}
	    	//这里可以添加图片处理前置限制大小
//	    	else if (file.length() > 2097152)
//	    	{
//                LogUtils.e("图片太大");
//               return null;
//	    	}
	    	orientation = getExifOrientation(path, "0");
	    	FileInputStream fileinputstream = null;
	        try
	        {
	            fileinputstream = new FileInputStream(file);
	            FileDescriptor filedescriptor = fileinputstream.getFD();
	            BitmapFactory.Options options = new BitmapFactory.Options();
	            options.inPurgeable = true;
	            options.inInputShareable = true;
	            options.inJustDecodeBounds = true;
	            BitmapFactory.decodeFileDescriptor(filedescriptor, null, options);
	            //计算压缩比
	            int be = 1;
	            while (options.outWidth > limitWidth) 
	            {
	            	be *= 2;
	            	limitWidth *= 2;
				}
	            limitHeight = limitHeight * be;
	            while (options.outHeight > limitHeight) 
	            {
	            	be *= 2;
	            	limitHeight *= 2;
				}
	            LogUtils.e("比例为"+be);
	            options.inSampleSize = be;
	            options.inJustDecodeBounds = false;   
	            // 重新读入图片，注意这次要把options.inJustDecodeBounds设为false
	            bitmap = BitmapFactory.decodeFileDescriptor(filedescriptor, null, options);
	        }
	        catch (FileNotFoundException e)
	        {
	            e.printStackTrace();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	        finally
	        {
	        	try 
	        	{
	        		if (fileinputstream != null) 
	        		{
	        			fileinputstream.close();
					}
				} 
	        	catch (IOException e) 
	        	{
					e.printStackTrace();
				}
	        }
	        return standBitmap(orientation, bitmap);
       }
       return null;
   }
   
   /**
    * 加载内存卡中的图片 不压缩 上限2M
    * @param path
    * @return
    */
   public static Bitmap loadImageFromPathWithoutCondense(String path) throws OutOfMemoryError
   {
       Bitmap bitmap = null;
       String orientation = null;
       if (path != null)
       {
    	   orientation = getExifOrientation(path, "0");
           Options options = new Options();
           options.inDither = false;
           options.inPurgeable = true;
           options.inInputShareable = true;
           options.inTempStorage = new byte[32767];
           File file = new File(path);
           if (file.exists())
           {
               if (file.length() > 2097152)
               {
                    LogUtils.e("图片太大");
                   return null;
               }
               else
               {
                   FileInputStream fileinputstream = null;
                   try
                   {
                       fileinputstream = new FileInputStream(file);
                       FileDescriptor filedescriptor = fileinputstream.getFD();
                       bitmap = BitmapFactory.decodeFileDescriptor(filedescriptor,
                               null,
                               options);
                   }
                   catch (FileNotFoundException e)
                   {
                       e.printStackTrace();
                   }
                   catch (IOException e)
                   {
                       e.printStackTrace();
                   }
                   finally
                   {
                   	try 
                   	{
							fileinputstream.close();
						} 
                   	catch (IOException e) 
                   	{
							e.printStackTrace();
						}
                   }
               }
           }
           else
           {
                LogUtils.e("路劲不存在");
           }
           return standBitmap(orientation, bitmap);
       }
       return null;
   }
	
	public static final long MAX_CODE_LENGTH = 2 * 1024 * 1024;
	public static final long MAX_COMPRESS_LENGTH = 1024 * 1024;
//	public static final long MAX_CODE_LENGTH = 200 * 1024;
//	public static final long MAX_COMPRESS_LENGTH = 200 * 1024;
	
	/**
	 * @param tempDirPath 项目内 临时图片文件处理目录
	 * @param nativePath 待处理(压缩、扶正)的本地图片文件路径
	 * @return 党原文件不存在 返回null 当处理失败 均返回原图片文件路径 当处理成功 返回处理后新图片路径
	 */
	public static String handleLocalBitmapFile(String nativePath, String tempDirPath)
    {
    	File nativeFile = new File(nativePath);
    	long nativeSize = nativeFile.length();
    	if (!nativeFile.exists() || nativeSize == 0) 
    	{
    		LogUtils.e("图片文件不存在");
			return null;
		}
    	String orientation = getExifOrientation(nativePath, "0");
    	if ("0".equals(orientation)) 
    	{
    		// TODO 为了静态演示去除判断，待删除
//			if (nativeSize < MAX_CODE_LENGTH) {
//				return nativePath;
//			}
//			else {
				Bitmap resultBitmap = condensationBitmapFile(nativeFile, nativeSize, MAX_CODE_LENGTH);
				if (resultBitmap == null) {
					return nativePath;
				}
				else 
				{
					String aimPath = createFileNameByTime(tempDirPath);
					if (writeImageFile(aimPath, resultBitmap)) 
					{
						return aimPath;
					} else 
					{
						return nativePath;
					}
				}
//			}
		} 
    	else 
		{
    		Bitmap standedBitmap = standBitmap(orientation, condensationBitmapFile(nativeFile, nativeSize, MAX_COMPRESS_LENGTH));
    		if (standedBitmap == null) {
				return nativePath;
			}
			else 
			{
				String standedPath = createFileNameByTime(tempDirPath);
				if (writeImageFile(standedPath, standedBitmap)) 
				{
					return standedPath;
				} else 
				{
					return nativePath;
				}
			}
		}
        
    }
	
	private static Bitmap condensationBitmapFile(File nativeFile, long nativeSize, long aimSize)
	{
		FileInputStream fileinputstream = null;
        Bitmap bitmap = null;
        // 计算缩放比 
        int be = 1;
        while (nativeSize > aimSize) 
        {
        	nativeSize /= 4;
			be *= 2;
		}
		try
        {
            fileinputstream = new FileInputStream(nativeFile);
            FileDescriptor filedescriptor = fileinputstream.getFD();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inJustDecodeBounds = false;
            LogUtils.e("压缩比例为"+be);
            options.inSampleSize = be;
            // 重新读入图片，注意这次要把options.inJustDecodeBounds设为false哦
            bitmap = BitmapFactory.decodeFileDescriptor(filedescriptor, null, options);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
        	try 
        	{
				fileinputstream.close();
			} 
        	catch (IOException e) 
        	{
				e.printStackTrace();
			}
        }
		return bitmap;
	}
	
	private static String getExifOrientation(String path, String orientation) {
		Method exif_getAttribute;
		Constructor<ExifInterface> exif_construct;
		String exifOrientation = "";

		int sdk_int = 0;
		try {
			sdk_int = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (Exception e1) {
			sdk_int = 3; // assume they are on cupcake
		}
		if (sdk_int >= 5) {
			try {
				exif_construct = android.media.ExifInterface.class
						.getConstructor(new Class[] { String.class });
				Object exif = exif_construct.newInstance(path);
				exif_getAttribute = android.media.ExifInterface.class
						.getMethod("getAttribute", new Class[] { String.class });
				try {
					exifOrientation = (String) exif_getAttribute.invoke(exif,
							android.media.ExifInterface.TAG_ORIENTATION);
					if (exifOrientation != null) {
						if (exifOrientation.equals("1")) {
							orientation = "0";
						} else if (exifOrientation.equals("3")) {
							orientation = "180";
						} else if (exifOrientation.equals("6")) {
							orientation = "90";
						} else if (exifOrientation.equals("8")) {
							orientation = "270";
						}
					} else {
						orientation = "0";
					}
				} catch (InvocationTargetException ite) {
					/* unpack original exception when possible */
					orientation = "0";
				} catch (IllegalAccessException ie) {
					System.err.println("unexpected " + ie);
					orientation = "0";
				}
				/* success, this is a newer device */
			} catch (NoSuchMethodException nsme) {
				orientation = "0";
			} catch (IllegalArgumentException e) {
				orientation = "0";
			} catch (InstantiationException e) {
				orientation = "0";
			} catch (IllegalAccessException e) {
				orientation = "0";
			} catch (InvocationTargetException e) {
				orientation = "0";
			}

		}
		return orientation;
	}
	
	private static Bitmap standBitmap(String orientation, Bitmap bitmap)
	{
		if (bitmap == null) {
			return null;
		}
		Matrix matrix = new Matrix();
		matrix.postRotate(Float.valueOf(orientation));
		Bitmap resultBitmap = null;
		try
        {
			resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, false);
        }
		catch (OutOfMemoryError ex)
        {
        	ex.printStackTrace();
        	return bitmap;
            // 如何出现了内存不足异常，最好return 原始的bitmap对象。.
        }
		if (resultBitmap != null) {
			return resultBitmap;
		} else {
			return bitmap;
		}
	}
	
	private static boolean writeImageFile(String aimPath, Bitmap bitmap)
    {
        if (bitmap == null)
        {
            return false;
        }
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try
        {
            fos = new FileOutputStream(aimPath);
            bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
            	if (bos != null)
                {
                    bos.close();
                }
                if (fos != null)
                {
                    fos.close();
                }
            }
            catch (IOException e)
            {
            	e.printStackTrace();
            }
        }
    }
	
	private static String createFileNameByTime(String tempDirPath) {
		String filaName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime()) + ".jpg";
		File aimFile = new File(tempDirPath, filaName);
		return aimFile.getAbsolutePath();
	}
	
	//////////////////////////////
	//图像处理
	
	/**
     * 获得圆角图片 
     * 
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx)
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = bitmap;
        try
        {
            output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        }
        catch (Exception e)
        {
            LogUtils.e("getRoundedCornerBitmap()", e);
        }

        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    
    /**
     * 获得带倒影的图片
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }
    
    /**
	 * 这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param originalBitmap
	 *            要被压缩的位图
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(Bitmap originalBitmap, int width,
			int height) {
		
		if (originalBitmap != null) {
			 // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
			return ThumbnailUtils.extractThumbnail(originalBitmap, width, height,
						ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		} else {
			return null;
		}
		
	}
	
	
	//////////////////////////////////////////
	//图片对象的转换
	
	public static Bitmap drawableToBitmap(Drawable drawable) {// drawable

		int width = drawable.getIntrinsicWidth(); // 取drawable的长宽
		int height = drawable.getIntrinsicHeight();

		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取drawable的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把drawable内容画到画布中

		return bitmap;
	}
	
	public static Bitmap resouceToBitmap(Context context, int drawableResouceId) 
	{
		Resources res = context.getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, drawableResouceId);
		return bmp;
	}
	
	public static byte[] bitmapToByte(Bitmap bitmap) 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
	
	public static Drawable bitmapToDrawable (Context context, Bitmap bitmap) 
	{
		BitmapDrawable bitmapDrawable= new BitmapDrawable(context.getResources(), bitmap);
		return bitmapDrawable;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//图片对象的压缩
	
	/**
	 * bitmap的缩放
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }
	
	/**
	 * drawable的缩放
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // drawable转换成bitmap
        Bitmap oldbmp = drawableToBitmap(drawable);
        // 创建操作图片用的Matrix对象
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        // 设置缩放比例
        matrix.postScale(sx, sy);
        // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(newbmp);
    }

}
