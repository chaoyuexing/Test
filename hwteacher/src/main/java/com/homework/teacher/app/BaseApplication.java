package com.homework.teacher.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.homework.teacher.R;
import com.homework.teacher.data.ClassRoom;
import com.homework.teacher.datasource.DataHelper;
import com.homework.teacher.utils.NativeImageLoader;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.rtmp.TXLiveBase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application {

	public final String TAG = "BaseApplication";
	private static BaseApplication mInstance;
	private SharedPreferences sp;
	private DataHelper mHelper;
	private final static String SESSION_COOKIE = "SESSION";
	private final static String CLIENTIP = "clientIP";
	private final static String TOKEN = "token";
	private final static String SESSIONID = "sessionID";
	private final static String LOGINFLAG = "loginFlag";// 登录状态 0：未登录，1：已登录
	private final static String CSTID = "cstId";
	private final static String ORDID = "ordId";
	private final static String TAKEPOINTNAME = "takePointName";
	private final static String TAKEPOINTID = "takePointId";
	private final static String SALT = "salt";
	private final static String BGNDATE = "bgnDate";
	private final static String ENDDATE = "endDate";
	private final static String FIRSTINSTALL = "firstInstall";
	public ImageLoader imageLoader;
	public DisplayImageOptions defaultOptions;
	public File cacheDir;
	private RequestQueue mRequestQueue;
	private com.android.volley.toolbox.ImageLoader mNetworkImageLoader;

	private static OSS oss = null;

	String ugcLicenceUrl = "http://license.vod2.myqcloud.com/license/v1/79d8cb8f23dc3d6d24d341da8974813a/TXUgcSDK.licence"; //您从控制台申请的 licence url
	String ugcKey = "2dfaa38eab5118e02cbd08f08b6efd6e";

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		MultiDex.install(this);
//		TXLiveBase.getInstance().setLicence(this, ugcLicenceUrl, ugcKey);
		mHelper = DataHelper.getHelper(getApplicationContext());
		sp = getSharedPreferences("VSICHU", Context.MODE_PRIVATE);
		imageLoader = ImageLoader.getInstance();
		defaultOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_image)// 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_image)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_image)// 设置图片加载、解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成

		cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
				"vsichu/imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				// 缓存的文件数量
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.defaultDisplayImageOptions(defaultOptions)
				.imageDownloader(
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
				.writeDebugLogs() // Remove for release app
				.build();
		imageLoader.init(config);
		getOssInstance();
		TXLiveBase.getInstance().setLicence(mInstance, ugcLicenceUrl, ugcKey);

		// AliVcMediaPlayer.init(getApplicationContext(), Consts.businessId,
		// new AccessKeyCallback() {
		// public AccessKey getAccessToken() {
		// return new AccessKey(Consts.AccessKeyId,
		// Consts.AccessKeySecret);
		// }
		// });
	}

	public static OSS getOssInstance() {
		if (oss == null) {
			String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
			OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider("LTAI9KQTGihkambl", "wl4rJcKckND1PfzFRBaLhHjgQRurX4", "");
			//该配置类如果不设置，会有默认配置，具体可看该类
			ClientConfiguration conf = new ClientConfiguration();
			conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
			conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
			conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
			conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
//        OSSLog.disableLog();
			oss = new OSSClient(mInstance, endpoint, credentialProvider);
		}
		return oss;
	}


	public SharedPreferences getSp() {
		return sp;
	}

	public String getSessionCookie() {
		return getSp().getString(SESSION_COOKIE, "");
	}

	public String getClientIP() {
		return getSp().getString(CLIENTIP, "");
	}

	public String getToken() {
		return getSp().getString(TOKEN, "");
	}

	public String getSessionID() {
		return getSp().getString(SESSIONID, "");
	}

	public int getLoginFlag() {
		return getSp().getInt(LOGINFLAG, 0);
	}

	public int getCstID() {
		return getSp().getInt(CSTID, 0);
	}

	public int getORDID() {
		return getSp().getInt(ORDID, 0);
	}

	public String getLastMealPlaceName() {
		return getSp().getString(TAKEPOINTNAME, "");
	}

	public int getLastMealPlaceID() {
		return getSp().getInt(TAKEPOINTID, 0);
	}

	public String getSalt() {
		return getSp().getString(SALT, "");
	}

	public String getBgnDate() {
		return getSp().getString(BGNDATE, "");
	}

	public String getEndDate() {
		return getSp().getString(ENDDATE, "");
	}

	public int getFirstInstall() {
		return getSp().getInt(FIRSTINSTALL, 0);
	}

	public static synchronized BaseApplication getInstance() {
		return mInstance;
	}

	public List<ClassRoom> getAllClassRoom() {
		List<ClassRoom> classrooms = new ArrayList<ClassRoom>();
		try {
			QueryBuilder<ClassRoom, Integer> contactBuilder = mHelper
					.getClassRoomData().queryBuilder();
			contactBuilder.where().eq("loginName", "loginName");
			classrooms = contactBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classrooms;
	}

	public com.android.volley.toolbox.ImageLoader getNetworkImageLoader() {
		if (mNetworkImageLoader == null) {
			mNetworkImageLoader = new com.android.volley.toolbox.ImageLoader(
					getRequestQueue(), NativeImageLoader.getInstance());
		}
		return mNetworkImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		VolleyLog.d("添加请求至: %s", TextUtils.isEmpty(tag) ? TAG : tag);
		VolleyLog.d("添加请求至队列: %s", req.getUrl());
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		VolleyLog.d(tag.toString(), "从队列里移除请求");
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// mHttpClient = new DefaultHttpClient();
			// PoolingClientConnectionManager
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}
}