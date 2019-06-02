package com.linkage.lib.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 弹出窗工具类
 * 
 */
public class DialogUtils {
	private Context context;

	public DialogUtils(Context context) {
		this.context = context;
	}

	/**
	 * @param title
	 *            ConfirmDialog的标题
	 * @param message
	 *            ConfirmDialog的提示消息
	 * @param positive
	 *            ConfirmDialog确认按钮上面的文字
	 * @param dialogListener
	 *            ConfirmDialog 的监听器
	 */
	public Dialog createConfirmDialog(String title, String message,
			String positive, final ConfirmDialogListener confirmDialogListener) {
		AlertDialog alert = null;
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
				.setMessage(message)
				.setCancelable(true)
				.setPositiveButton(positive,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// 点击确认按钮的回调的方法
								confirmDialogListener.positive();
							}
						});
		alert = builder.create();
		return alert;
	}

	/**
	 * @param title
	 *            ConfirmDialog的标题
	 * @param message
	 *            ConfirmDialog的提示消息
	 * @param positive
	 *            ConfirmDialog确认按钮上面的文字
	 * @param negative
	 *            ConfirmDialog取消按钮上面的文字
	 * @param dialogListener
	 *            ConfirmDialog 的监听器
	 */
	public Dialog createConfirmDialog(String title, String message,
			String positive, String negative,
			final ConfirmDialogListener confirmDialogListener) {
		AlertDialog alert = null;
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
				.setMessage(message)
				.setCancelable(true)
				.setPositiveButton(positive,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// 点击确认按钮的回调的方法
								confirmDialogListener.positive();
							}
						})
				.setNegativeButton(negative,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// 点击取消按钮的回调方法
								confirmDialogListener.negative();
							}
						});
		alert = builder.create();
		return alert;
	}

	/**
	 * @param title
	 *            ConfirmDialog的标题
	 * @param message
	 *            ConfirmDialog的提示消息
	 * @param positive
	 *            ConfirmDialog确认按钮上面的文字
	 * @param neutral
	 *            ConfirmDialog中立按钮上面的文字
	 * @param negative
	 *            ConfirmDialog取消按钮上面的文字
	 * @param dialogListener
	 *            ConfirmDialog 的监听器
	 */
	public Dialog createConfirmDialog(String title, String message,
			String positive, String negative, String neutral,
			final ConfirmDialogListener confirmDialogListener) {
		AlertDialog alert = null;
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
				.setMessage(message)
				.setCancelable(true)
				.setPositiveButton(positive,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// 点击确认按钮的回调的方法
								confirmDialogListener.positive();
							}
						})
				.setNeutralButton(neutral,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// 中立确认按钮的回调的方法
								confirmDialogListener.neutral();
							}
						})
				.setNegativeButton(negative,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// 点击取消按钮的回调方法
								confirmDialogListener.negative();
							}
						});
		alert = builder.create();
		return alert;
	}

	/**
	 * 这个是一个默认的弹出对话框
	 * 
	 * @param message
	 * @param dialogListener
	 * @return
	 */
	public Dialog createConfirmDialog(String message,
			final ConfirmDialogListener confirmDialogListener) {
		return createConfirmDialog("提示", message, "确定", "取消",
				confirmDialogListener);
	}

	public interface ConfirmDialogListener {
		/**
		 * 确认按钮的回调方法 一个Activity可能有多个AlertDialog，这个id是来区别我们点击的是哪个AlertDialog
		 */
		public void positive();

		/**
		 * 中立按钮的回调方法 一个Activity可能有多个AlertDialog，这个id是来区别我们点击的是哪个AlertDialog
		 */
		public void neutral();

		/**
		 * 取消按钮的回调方法，我们一般点取消都是关闭AlertDialog,这里我预留一个回调方法在这里
		 */
		public void negative();
	}
}
