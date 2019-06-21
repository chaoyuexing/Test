package com.homework.teacher.teacher.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.teacher.R;
import com.homework.teacher.data.MediumAnswer;

import java.util.List;

/**
 * Created by xing
 * on 2019/6/3
 */
public class AnswerAdapter  extends BaseQuickAdapter<MediumAnswer.DataBean,BaseViewHolder> {

    private final int TEXT_FLAG = 1;
    private final int IMG_FLAG = 2;
    private Context mContext;


    public AnswerAdapter(int layoutResId, @Nullable List<MediumAnswer.DataBean> data, Context mContext) {
        super(layoutResId, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, MediumAnswer.DataBean item) {
        ImageView imageView =  helper.getView(R.id.image);
        if (item.getType() == IMG_FLAG) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(item.getContent()).into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
        helper.setText(R.id.title, item.getContent());
        helper.setText(R.id.name, item.getCreTeacherName());
        helper.setText(R.id.time, item.getAddTime());

    }
}
