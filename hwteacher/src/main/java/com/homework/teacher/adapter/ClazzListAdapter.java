package com.homework.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.Clazz;

import java.util.List;

/**
 * Created by zhangkaichao on 2019/2/22.
 */

public class ClazzListAdapter extends BaseAdapter {

    private final static String TAG = "ClazzListAdapter";
    private List<Clazz> mData;
    private Context mContext;
    private int itemId = 1000000;// 点击item的位置id

    public ClazzListAdapter(Context context, List<Clazz> list) {
        this.mContext = context;
        this.mData = list;
    }

    public ClazzListAdapter(Context context, int itemId, List<Clazz> list) {
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
//        Log.e(TAG, "position: " + position);
        final ClazzListAdapter.ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.adapter_class_list_item, parent, false);
            holder = new ClazzListAdapter.ViewHolder();
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ClazzListAdapter.ViewHolder) convertView.getTag();
        }
        final Clazz clazz = (Clazz) getItem(position);
        if (clazz != null) {
            holder.classTv.setText(clazz.getNumber() + "班");
            if (clazz.getApplyState() == 1) {
                holder.applyStateTv.setVisibility(View.VISIBLE);
            } else {
                holder.applyStateTv.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        private TextView classTv;
        private TextView applyStateTv;

        void init(View convertView) {
            classTv = (TextView) convertView.findViewById(R.id.classTv);
            applyStateTv = (TextView) convertView.findViewById(R.id.applyStateTv);
        }
    }
}
