package com.linkage.lib.task;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import com.linkage.gson.Gson;
import com.linkage.gson.GsonBuilder;
import com.linkage.lib.exception.ServerException;
import com.linkage.lib.util.HttpExecuter;
import com.linkage.lib.util.LogUtils;

public abstract class AbstractAsyncRequestTask<T> extends
		AsyncTaskWithExecuteResult<String, Integer, T> implements
		IManageableTask {

	private static final boolean HTTP_DEBUG = true;
	
	public static enum RequestMethod {
		GET,
		POST
	}
	
	public Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	protected RequestManager mRequestManager;
	protected HttpGet mGet = null;
	private HttpPost mPost = null;
	protected String mRequestUrl = null;
	protected int mStatusCode = -1;
	protected RequestMethod mMethod;

	public AbstractAsyncRequestTask(String hostUrl, RequestMethod method) {
		this.mRequestUrl = hostUrl;
		this.mMethod = method;
	}

	/* 暂废弃
	protected void dump(HttpPost post) {
		if (post != null) {
			LogUtils.d("发起request url:" + post.getURI());
			if (post.getEntity() instanceof UrlEncodedFormEntity) {
				UrlEncodedFormEntity entity = (UrlEncodedFormEntity) post
						.getEntity();
				try {
					LogUtils.d(IOUtils.toString(entity.getContent()));
					for (Header header : post.getAllHeaders()) {
						LogUtils.d(header.getName() + ":" + header.getValue());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	*/
	
	@Override
	protected void onFailed(Exception e) {
	}
	
	@Override
	protected void onSucceed(T result) {
	}
	
	protected void HandleServerException(int code,String info) throws Exception
	{
		throw new ServerException(code, info);
	}

	protected abstract HttpPost getHttpPost() throws Exception;
	protected abstract HttpGet getHttpGet() throws Exception;

	protected T handleResponse(String res) throws Exception {
		LogUtils.w("handleResponse:"+res);
		return null;
	}
	
	private T handleResponse(HttpResponse response) throws Exception {
		InputStream input = response.getEntity().getContent();
		return handleResponse(IOUtils.toString(input));
	}
	
	@Override
	protected T innerRun(String[] params) throws Exception {
		initialHttpParams();
		HttpResponse response = null;
		
		if(mMethod == RequestMethod.POST) {
			try {
				mPost = getHttpPost();
			} catch (Exception e) {
				if(mRequestManager != null) {
					mRequestManager.removeTask(this);
				}
				throw e;
			}
			final HttpPost post = mPost;
			if(HTTP_DEBUG) 
			{
				if (post != null) {
					LogUtils.d("发起request url:" + post.getURI());
				}
			}
			
			try {
				response = HttpExecuter.executePost(post);
			} catch (Exception e) {
				e.printStackTrace();
				if(mRequestManager != null) {
					mRequestManager.removeTask(this);
				}
				throw e;
			}
		} else if(mMethod == RequestMethod.GET) {
			try {
				mGet = getHttpGet();
			} catch (Exception e) {
				if(mRequestManager != null) {
					mRequestManager.removeTask(this);
				}
				throw e;
			}
			final HttpGet get = mGet;
			try {
				response = HttpExecuter.executeGet(get);
			} catch (Exception e) {
				if(mRequestManager != null) {
					mRequestManager.removeTask(this);
				}
				throw e;
			}
		}
		
		mStatusCode = response.getStatusLine().getStatusCode();
//		LogUtils.e("mStatusCode: " + mStatusCode);
		if (mStatusCode < 200 || mStatusCode >= 300) {
			if(mRequestManager != null) {
				mRequestManager.removeTask(this);
			}
			InputStream input = response.getEntity().getContent();
			HandleServerException(mStatusCode,IOUtils.toString(input));
		}

		try {
			LogUtils.w("接收对应url:" + mRequestUrl);
			T t = handleResponse(response);
			if(mRequestManager != null) {
				mRequestManager.removeTask(this);
			}
			return t;
		} catch (Exception e) {
			if(mRequestManager != null) {
				mRequestManager.removeTask(this);
			}
			throw e;
		}
	}

	/**
	 * @author 方达
	 * 用于重写 实现初始化http请求参数，可选操作包括：加入文件流(addBodyFile)、对LinkList参数列表进行编辑操作等等
	 * @throws Exception
	 */
	protected void initialHttpParams() throws Exception
	{
	}


	public void execute() {
		super.execute("");
		//super.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR,EMPTY_STRINGS);
	}

	@Override
	public T syncExecute() {
		T t = doInBackground("");
		onPostExecute(t);
		return t;
	}
	
	@Override
	public void setRequestManager(RequestManager manager) {
		mRequestManager = manager;
	}
	
	@Override
	public boolean stop(boolean stop) {
		if(mPost != null) {
			mPost.abort();
		}
		if(mGet != null) {
			mGet.abort();
		}
		return super.cancel(stop);
	}

}
