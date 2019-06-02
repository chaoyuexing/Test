package com.homework.teacher.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.GradeSubject;

import java.util.List;

/**
 * Created by zhangkaichao on 2019/2/21.
 */

public class GradeSubjectAdapter extends BaseAdapter {

    private final static String TAG = "GradeSubjectAdapter";
    private List<GradeSubject.GradeSubjectData> mData;
    private Context mContext;
    private int itemId = 1000000;// 点击item的位置id

    public GradeSubjectAdapter(Context context, List<GradeSubject.GradeSubjectData> list) {
        this.mContext = context;
        this.mData = list;
    }

    public GradeSubjectAdapter(Context context, int itemId, List<GradeSubject.GradeSubjectData> list) {
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
        final GradeSubjectAdapter.ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.adapter_grade_subject_item, parent, false);
            holder = new GradeSubjectAdapter.ViewHolder();
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GradeSubjectAdapter.ViewHolder) convertView.getTag();
        }
        final GradeSubject.GradeSubjectData gradeSubject = (GradeSubject.GradeSubjectData) getItem(position);
        if (gradeSubject != null) {
            holder.mGradeSubjectTv.setText(gradeSubject.getGradeName() + gradeSubject.getSubjectName() + "备课组");
        }
        return convertView;
    }

    class ViewHolder {
        private TextView mGradeSubjectTv;

        void init(View convertView) {
            mGradeSubjectTv = (TextView) convertView.findViewById(R.id.gradeSubjectTv);
        }
    }
}
