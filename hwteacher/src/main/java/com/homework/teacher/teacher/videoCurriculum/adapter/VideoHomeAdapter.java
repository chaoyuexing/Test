package com.homework.teacher.teacher.videoCurriculum.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.teacher.data.MediumVideoCourseBean;

import java.util.List;

/**
 * @author：xing Data：2019/6/21
 * brief:
 **/
public class VideoHomeAdapter extends BaseQuickAdapter<MediumVideoCourseBean.DataBean, BaseViewHolder> {

    private Context mContext;

    public VideoHomeAdapter(int layoutResId, @Nullable List<MediumVideoCourseBean.DataBean> data, Context mContext) {
        super(layoutResId, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, MediumVideoCourseBean.DataBean item) {

    }
}
