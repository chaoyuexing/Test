package com.homework.teacher.activity.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;

public class ActivityStack {

	private static ActivityStack instance;
	private List<Node> activityList = new ArrayList<>();
	private Node preNode;

	private LinkedHashMap<String, Map<String, String>> pageLogList = new LinkedHashMap<>();

	public static ActivityStack getInstance() {
		if (null == instance) {
			instance = new ActivityStack();
		}
		return instance;
	}

	// activity管理：从列表中移除activity
	public void removeActivity(Activity activity) {
		if (activityList != null && activityList.size() > 0) {
			for (int i = activityList.size() - 1; i >= 0; i--) {
				Node node = activityList.get(i);
				if (node.getActivity().getClass().getName() != null
						&& activity.getClass().getName() != null
						&& node.getActivity().getClass().getName()
								.equals(activity.getClass().getName())) {
					activityList.remove(i);
					break;
				}
			}
		}
		if (activityList != null && activityList.size() > 0) {
			preNode = activityList.get(activityList.size() - 1);
		}
	}

	// activity管理：添加activity到列表
	public void addActivity(Activity activity, String title, String pageUrl) {
		if (activityList != null && activityList.size() > 0) {
			preNode = activityList.get(activityList.size() - 1);
		}
		Node node = new Node();
		node.setPre(preNode);
		node.setNext(null);
		node.setActivity(activity);
		node.setTitle(title);
		node.setPageUrl(pageUrl);
		activityList.add(node);
	}

	public void clear() {
		if (activityList != null && activityList.size() > 0) {
			for (Node node : activityList) {
				node.getActivity().finish();
			}
			activityList.clear();
		}
		preNode = null;
	}

	/*
	 * public String getPrePageTitle() { if(preNode != null) { return
	 * preNode.getTitle(); } return ""; }
	 * 
	 * public String getPrePageUrl() { if(preNode != null) { return
	 * preNode.getPageUrl(); } return ""; }
	 */

	/**
	 * 将数据封装成结点
	 */
	private final class Node {
		private Node pre, next;

		private Activity activity;

		private String title;
		private String pageUrl;

		public Node getPre() {
			return pre;
		}

		public void setPre(Node pre) {
			this.pre = pre;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}

		public Activity getActivity() {
			return activity;
		}

		public void setActivity(Activity activity) {
			this.activity = activity;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getPageUrl() {
			return pageUrl;
		}

		public void setPageUrl(String pageUrl) {
			this.pageUrl = pageUrl;
		}
	}

	public void add(String pageUrl, String pageTitle) {
		if (pageUrl == null || "".equals(pageUrl))
			return;
		Map<String, String> map = new HashMap<>();
		map.put("pageUrl", pageUrl);
		if (pageTitle != null && !"".equals(pageTitle)) {
			map.put("pageTitle", pageTitle);
		}
		pageLogList.put(pageUrl, map);// 以页面的Controller名称为键，便于查询和删除
	}

	public void remove(String pageUrl) {
		if (pageUrl == null || "".equals(pageUrl))
			return;
		if (pageLogList.containsKey(pageUrl)) {
			pageLogList.remove(pageUrl);
		}
	}

	public String getPrePageTitle() {
		if (pageLogList != null && pageLogList.size() > 1) {
			int j = 0;
			for (Map.Entry<String, Map<String, String>> mapx : pageLogList
					.entrySet()) {
				j++;
				if (j == pageLogList.size() - 1) {
					Map<String, String> map = pageLogList.get(mapx.getKey());
					if (map != null)
						return map.get("pageTitle");
				}
			}
		}
		return "";
	}

	public String getPrePageUrl() {
		if (pageLogList != null && pageLogList.size() > 1) {
			int j = 0;
			for (Map.Entry<String, Map<String, String>> mapx : pageLogList
					.entrySet()) {
				j++;
				if (j == pageLogList.size() - 1) {
					Map<String, String> map = pageLogList.get(mapx.getKey());
					if (map != null)
						return map.get("pageUrl");
				}
			}
		}
		return "";
	}
}
