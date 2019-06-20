package com.homework.teacher.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.utils.DensityUtil;
import com.jph.takephoto.app.TakePhoto;
import com.tencent.liteav.demo.videorecord.TCVideoRecordActivity;

/**
 * @author：xing Data：2019/6/19
 * brief:
 **/
public class AddVideoDialog extends Dialog implements View.OnClickListener {


    private TextView mDialogTake;
    private TextView mDialogLibSelect;
    private TextView mDialogPhotoSelect;
    private ImageView mDialogAnswerCancle;
    private Context mContext;
    private TakePhoto mTakePhoto;

    public AddVideoDialog(Context context) {
        super(context);
    }

    public AddVideoDialog(Context context, int theme, TakePhoto takePhoto) {
        super(context, theme);
        this.mContext = context;
        this.mTakePhoto = takePhoto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_add_video_curriculum, null);
        setContentView(view);
        mDialogTake = view.findViewById(R.id.dialog_take);
        mDialogLibSelect = view.findViewById(R.id.dialog_lib_select);
        mDialogPhotoSelect = view.findViewById(R.id.dialog_photo_select);
        mDialogAnswerCancle = view.findViewById(R.id.dialog_answer_cancle);
        mDialogTake.setOnClickListener(this);
        mDialogPhotoSelect.setOnClickListener(this);
        mDialogLibSelect.setOnClickListener(this);
        mDialogAnswerCancle.setOnClickListener(this);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.height = DensityUtil.dip2px(mContext, 250);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        win.setGravity(Gravity.BOTTOM);
        win.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_take:
                mContext.startActivity(new Intent(mContext, TCVideoRecordActivity.class));
                break;
            case R.id.dialog_lib_select:
                break;
            case R.id.dialog_photo_select:
                break;
            case R.id.dialog_answer_cancle:
                break;
        }
    }
}
