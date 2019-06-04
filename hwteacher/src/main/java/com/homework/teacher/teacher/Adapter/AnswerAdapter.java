package com.homework.teacher.teacher.Adapter;

import android.support.annotation.Nullable;

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


    public AnswerAdapter(int layoutResId, @Nullable List<MediumAnswer.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MediumAnswer.DataBean item) {
        helper.setText(R.id.title, item.getContent());
        helper.setText(R.id.name, item.getCreTeacherName());
        helper.setText(R.id.time, item.getAddTime());

    }
}
