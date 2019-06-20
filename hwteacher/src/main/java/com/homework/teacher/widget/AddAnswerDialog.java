package com.homework.teacher.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.teacher.answer.AddAnswerActivity;
import com.homework.teacher.utils.DensityUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.CropOptions;

import java.io.File;


/**
 * Created by xing
 * on 2019/6/8
 */
public class AddAnswerDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView mAnswerInput,mAnswerPhoto;
    private ImageView mAnswerCancle;
    private TakePhoto mTakePhoto;

    public AddAnswerDialog(Context context) {
        super(context);
    }

    public AddAnswerDialog(Context context, int theme, TakePhoto takePhoto) {
        super(context, theme);
        this.mContext = context;
        this.mTakePhoto = takePhoto;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_add_answer,null);
        setContentView(view);
        mAnswerInput = (TextView) view.findViewById(R.id.dialog_answer_input);
        mAnswerPhoto = (TextView) view.findViewById(R.id.dialog_answer_photo);
        mAnswerCancle = (ImageView) view.findViewById(R.id.dialog_answer_cancle);
        mAnswerInput.setOnClickListener(this);
        mAnswerPhoto.setOnClickListener(this);
        mAnswerCancle.setOnClickListener(this);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.height = DensityUtil.dip2px(mContext,250);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        win.setGravity(Gravity.BOTTOM);
        win.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_answer_input:
                mContext.startActivity(new Intent(mContext,AddAnswerActivity.class));
                break;
            case R.id.dialog_answer_photo:
                File file = new File(mContext.getExternalCacheDir(), System.currentTimeMillis() + ".png");
                Uri uri = Uri.fromFile(file);
                int size = Math.min(mContext.getResources().getDisplayMetrics().widthPixels, mContext.getResources().getDisplayMetrics().heightPixels);
                CropOptions cropOptions = new CropOptions.Builder().setOutputX(size).setOutputX(size).setWithOwnCrop(false).create();
                mTakePhoto.onPickFromGalleryWithCrop(uri, cropOptions);
                break;
            case R.id.dialog_answer_cancle:
                dismiss();
                break;
        }
    }
}
