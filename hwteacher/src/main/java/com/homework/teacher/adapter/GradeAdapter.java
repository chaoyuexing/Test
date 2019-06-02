package com.homework.teacher.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.Grade;

import java.util.List;

/**
 * Created by zhangkaichao on 2019/2/21.
 */

public class GradeAdapter extends BaseAdapter {

    private final static String TAG = "GradeAdapter";
    private List<Grade> mData;
    private Context mContext;
    private int itemId = 1000000;// 点击item的位置id

    public GradeAdapter(Context context, List<Grade> list) {
        this.mContext = context;
        this.mData = list;
    }

    public GradeAdapter(Context context, int itemId, List<Grade> list) {
        this.mContext = context;
        this.itemId = itemId;
        this.mData = list;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return mData.size() == 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "position: " + position);
        final GradeAdapter.ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            if (position == itemId) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.adapter_subject_item_onclick, parent, false);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.adapter_subject_item, parent, false);
            }
            holder = new GradeAdapter.ViewHolder();
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GradeAdapter.ViewHolder) convertView.getTag();
        }
        final Grade grade = (Grade) getItem(position);
        if (grade != null) {
            holder.subjectTv.setText(grade.getGradeName());
        }
        return convertView;
    }

    class ViewHolder {
        private TextView subjectTv;

        void init(View convertView) {
            subjectTv = (TextView) convertView.findViewById(R.id.subjectTv);
        }
    }
}
