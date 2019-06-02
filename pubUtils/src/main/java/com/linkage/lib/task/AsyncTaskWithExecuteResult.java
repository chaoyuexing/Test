package com.linkage.lib.task;

import com.linkage.lib.util.LogUtils;

import android.os.AsyncTask;

public abstract class AsyncTaskWithExecuteResult<Params, Progress, Result>
		extends AsyncTask<Params, Progress, Result> {

	private static final String TAG = AsyncTaskWithExecuteResult.class.getSimpleName();
	private Exception mException = null;
	private Object mExtraInfo = null;
	private boolean mSucceed = true;

	protected void clearError() {
		this.mException = null;
		this.mSucceed = true;
	}

	@Override
	protected Result doInBackground(Params... params) {
		try {
			return innerRun(params);
		} catch (Exception e) {
			this.mException = e;
			LogUtils.e(TAG, e);
			this.mSucceed = false;	
		}
		return null;
	}

	public Exception getException() {
		return mException;
	}

	public Object getExtraErrorInfo() {
		return mExtraInfo;
	}

	protected abstract Result innerRun(Params[] params) throws Exception;

	public boolean isSucceed() {
		return mSucceed;
	}

	protected abstract void onFailed(Exception e);

	@Override
	protected void onPostExecute(Result result) {
		if (isCancelled())
			return;
		if (result == null || !mSucceed) {
			onFailed(mException);
			return;
		}
		onSucceed(result);
	}

	protected abstract void onSucceed(Result result);

	public void setExecuteResult(boolean succeed) {
		mSucceed = succeed;
	}

	public void setExtraErrorInfo(Object info) {
		mExtraInfo = info;
	}

	protected abstract Result syncExecute();
	
}
