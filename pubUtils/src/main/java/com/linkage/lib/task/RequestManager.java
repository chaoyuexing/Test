package com.linkage.lib.task;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.linkage.lib.util.LogUtils;

public class RequestManager {
	
	private Set<IManageableTask> runningTask = new HashSet<IManageableTask>();
	
	public void manageTask(IManageableTask task) {
		try {
			this.runningTask.add(task);
			task.setRequestManager(this);
			LogUtils.d("One task added, total task size is " + this.runningTask.size());
		} finally {
			
		}
	}
	
	public void removeTask(IManageableTask task) {
		try {
			this.runningTask.remove(task);
			LogUtils.d("One task remove, total task size is " + this.runningTask.size());
		} finally {
			
		}
	}
	
	public void stopAll() {
		Iterator<IManageableTask> iterator = runningTask.iterator();
		while(iterator.hasNext()) {
			IManageableTask task = iterator.next();
			LogUtils.d("Cancle result is " + task.stop(true));
		}
		
		this.runningTask.clear();
		LogUtils.d("All request task stoped");
	}
	
	public <T extends IManageableTask> void stopTask(Class<T> clss) {
		Iterator<IManageableTask> iterator = runningTask.iterator();
		while(iterator.hasNext()) {
			IManageableTask task = iterator.next();
			if(task.getClass().equals(clss)) {
				LogUtils.d("Cancle result is " + task.stop(true));
				iterator.remove();
			}
		}
	}
	
	public <T extends IManageableTask> boolean contains(Class<T> clss)
	{
		Iterator<IManageableTask> iterator = runningTask.iterator();
		while(iterator.hasNext()) {
			IManageableTask task = iterator.next();
			
			if(task.getClass()==clss) {
				LogUtils.d("runningTask contains " + clss.getClass().getSimpleName());
				return true;
			}
		}
		return false;
	}
}
