package com.linkage.lib.task;

public interface IManageableTask {
	
	void setRequestManager(RequestManager manager);
	
	boolean stop(boolean stop);
}
