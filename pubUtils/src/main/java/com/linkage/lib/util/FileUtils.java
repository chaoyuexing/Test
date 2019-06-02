package com.linkage.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.webkit.MimeTypeMap;

public class FileUtils
{
	private static char separatorChar = System.getProperty("file.separator", "/").charAt(0);
	private static char pathSeparatorChar = System.getProperty("path.separator", ":").charAt(0);
	private static String separator = String.valueOf(separatorChar);
	private static String pathSeparator = String.valueOf(pathSeparatorChar);
	
	private static final String[][] MIME_MapTable = {
		// {后缀名， MIME类型}
		{ ".3gp", "video/3gpp" },
		{ ".apk", "application/vnd.android.package-archive" },
		{ ".asf", "video/x-ms-asf" },
		{ ".avi", "video/x-msvideo" },
		{ ".bin", "application/octet-stream" },
		{ ".bmp", "image/bmp" },
		{ ".c", "text/plain" },
		{ ".class", "application/octet-stream" },
		{ ".conf", "text/plain" },
		{ ".cpp", "text/plain" },
		{ ".doc", "application/msword" },
		{ ".docx",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
		{ ".xls", "application/vnd.ms-excel" },
		{ ".xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
		{ ".exe", "application/octet-stream" },
		{ ".gif", "image/gif" },
		{ ".gtar", "application/x-gtar" },
		{ ".gz", "application/x-gzip" },
		{ ".h", "text/plain" },
		{ ".htm", "text/html" },
		{ ".html", "text/html" },
		{ ".jar", "application/java-archive" },
		{ ".java", "text/plain" },
		{ ".jpeg", "image/jpeg" },
		{ ".jpg", "image/jpeg" },
		{ ".js", "application/x-javascript" },
		{ ".log", "text/plain" },
		{ ".m3u", "audio/x-mpegurl" },
		{ ".m4a", "audio/mp4a-latm" },
		{ ".m4b", "audio/mp4a-latm" },
		{ ".m4p", "audio/mp4a-latm" },
		{ ".m4u", "video/vnd.mpegurl" },
		{ ".m4v", "video/x-m4v" },
		{ ".mov", "video/quicktime" },
		{ ".mp2", "audio/x-mpeg" },
		{ ".mp3", "audio/x-mpeg" },
		{ ".mp4", "video/mp4" },
		{ ".mpc", "application/vnd.mpohun.certificate" },
		{ ".mpe", "video/mpeg" },
		{ ".mpeg", "video/mpeg" },
		{ ".mpg", "video/mpeg" },
		{ ".mpg4", "video/mp4" },
		{ ".mpga", "audio/mpeg" },
		{ ".msg", "application/vnd.ms-outlook" },
		{ ".ogg", "audio/ogg" },
		{ ".pdf", "application/pdf" },
		{ ".png", "image/png" },
		{ ".pps", "application/vnd.ms-powerpoint" },
		{ ".ppt", "application/vnd.ms-powerpoint" },
		{ ".pptx",
				"application/vnd.openxmlformats-officedocument.presentationml.presentation" },
		{ ".prop", "text/plain" }, { ".rc", "text/plain" },
		{ ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
		{ ".sh", "text/plain" }, { ".tar", "application/x-tar" },
		{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
		{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
		{ ".wmv", "audio/x-ms-wmv" },
		{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
		{ ".z", "application/x-compress" },
		{ ".zip", "application/x-zip-compressed" }, { "", "*/*" } };

	/**
	 * 打开文件
	 */
	public static void openFile(File file, Context context) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取文件file的MIME类型
		String type = getMIMEType(file);
		// 设置intent的data和Type属性。
		intent.setDataAndType(Uri.fromFile(file), type);
		// 跳转
		context.startActivity(intent);
	}
	
	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 */
	private static String getMIMEType(File file) {
		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length());
		if (end == "") {
			return type;
		}
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equalsIgnoreCase(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}
	
	/**
	 * 格式化文件大小
	 */
	public static String FormatFileSize(long fileSize) {
		DecimalFormat df = new DecimalFormat("#0.00");
		String s = "";
		if (fileSize < 1024) {
			s = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			s = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			s = df.format((double) fileSize / 1048576) + "M";
		} else {
			s = df.format((double) fileSize / 1073741824) + "G";
		}
		return s;
	}
	
	public static void delFolder(String folderPath) {
		System.out.println("删除文件夹：" + folderPath);
		File file = new File(folderPath);
		if (!file.exists()) {
			return;
		}
		delAllFile(folderPath); // 删除完里面所有内容
		file.delete(); // 删除空文件夹
	}
	
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				// delAllFile(path + File.separator + tempList[i]);//
				// 先删除文件夹里面的文件
				delFolder(path + File.separator + tempList[i]);// 再删除空文件夹
			}
		}
	}
	
	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径
	 * @param newPath
	 *            String 复制后路径
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}
	
	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		File oldFile = new File(oldPath);
		if (oldFile.exists()) {
			oldFile.delete();
		}
	}
	
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {
	
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
	
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
	
				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
	
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
	
				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
	
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
	
				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };
	
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
	
			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();
	
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
	
		return null;
	}
	
	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
	
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };
	
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}
	
	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}
	
	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}
	
	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}
	
	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
	
    public static String getFileName(String pathName) {
        int separatorIndex = pathName.lastIndexOf(separator);
        return (separatorIndex < 0) ? pathName : pathName.substring(separatorIndex + 1, pathName.length());
    }
    
    /**
     * 通过uri从图片媒体库中获取文件对象
     * @param mContext
     * @param fileUri
     * @return
     */
    public static File fetchImageFilefromUri(Context mContext, Uri fileUri)
    {
    	File file = null;
    	if (fileUri != null) 
    	{
    		LogUtils.e("获取到的Uri：" + fileUri.getPath());
    		Cursor cursor = mContext
                    .getContentResolver()
                    .query(fileUri, null, null, null, null);
    		
            try
            {
                if (cursor != null && cursor.moveToFirst())
                {

                	String filePath = cursor.getString(cursor.getColumnIndexOrThrow(ImageColumns.DATA));
                    file = new File(filePath);
                }
            }
            finally
            {
                if (null != cursor)
                {
                    cursor.close();
                }
            }
		} 
    	if (file != null && file.exists() && file.length() > 0) 
        {
    		LogUtils.e("找到文件路径：" + file);
			return file;
		}
    	//解决小米手机返回uri是个路径的问题
    	else if (fileUri.toString().startsWith("file:" + file.separatorChar  + file.separatorChar  + file.separatorChar)) {
            String tempFilePath = fileUri.getPath();
            tempFilePath = new String(tempFilePath);
            LogUtils.e("找到可能存在的文件路径：" + tempFilePath);
    	    File tempFile = new File(tempFilePath);
    	    if (tempFile.exists())
            {
                return tempFile;
            }
        }
    	LogUtils.e("没有找到文件路径");
		return null;
    }
    
	/**
	 * 读取Assets中的文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param context
	 *            调用的context
	 */
	public static String getStringFromAsset(String filePath, Context context) {
		String result = "";
		try {
			InputStream in = context.getResources().getAssets().open(filePath); // 从Assets中的文件获取输入流
			int length = in.available(); // 获取文件的字节数
			byte[] buffer = new byte[length]; // 创建byte数组
			in.read(buffer); // 将文件中的数据读取到byte数组中
			result = EncodingUtils.getString(buffer, "UTF-8"); // 将byte数组转换成指定格式的字符串
		} catch (Exception e) {
			e.printStackTrace(); // 捕获异常并打印
		}
		return result;
	}
	
	public static String getMimeType(final String fileName) {
        String result = "application/octet-stream";
        int extPos = fileName.lastIndexOf(".");
        if (extPos != -1) {
            String ext = fileName.substring(extPos + 1);
            result = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return result;
    }
	
}
