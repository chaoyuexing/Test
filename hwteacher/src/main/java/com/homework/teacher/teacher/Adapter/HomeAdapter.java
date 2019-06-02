package com.homework.teacher.teacher.Adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.teacher.R;
import com.homework.teacher.data.HomeListJob;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by xing
 * on 2019/5/18
 */
public class HomeAdapter extends BaseQuickAdapter<HomeListJob.HomeListJobData,BaseViewHolder> {

    private Context mContext;

    public HomeAdapter(Context context, int layoutId, List datas) {
        super( layoutId, datas);
        mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder holder, HomeListJob.HomeListJobData data) {
        holder.setText(R.id.home_item_name,data.getAddPerson());
        holder.setText(R.id.home_item_time,new SimpleDateFormat("yyyy-MM-dd HH:mm").format(data.getAddTime()));
        if (data.getState() == 0) {
            holder.getView(R.id.home_item_draft).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.home_item_draft).setVisibility(View.GONE);
        }

    }
}
