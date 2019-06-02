package com.homework.teacher.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.Subject;

import java.util.List;

/**
 * Created by zhangkaichao on 2019/2/20.
 */

public class SubjectAdapter extends BaseAdapter {

    private final static String TAG = "SubjectAdapter";
    private List<Subject> mData;
    private Context mContext;
    private int itemId = 1000000;// 点击item的位置id

    public SubjectAdapter(Context context, List<Subject> list) {
        this.mContext = context;
        this.mData = list;
    }

    public SubjectAdapter(Context context, int itemId, List<Subject> list) {
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
        final SubjectAdapter.ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            if (position == itemId) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.adapter_subject_item_onclick, parent, false);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.adapter_subject_item, parent, false);
            }
            holder = new SubjectAdapter.ViewHolder();
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SubjectAdapter.ViewHolder) convertView.getTag();
        }
        final Subject subject = (Subject) getItem(position);
        if (subject != null) {
            holder.subjectTv.setText(subject.getValue());
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
