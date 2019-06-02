package com.homework.teacher.teacher.Adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.datepicker.DateFormatUtils;
import com.homework.teacher.R;
import com.homework.teacher.data.GradeSubject;
import com.homework.teacher.widget.CustomDatePicker;

import java.util.List;

/**
 * Created by xing
 * on 2019/5/25
 */
public class ClassAdapter extends BaseQuickAdapter<GradeSubject.GradeSubjectData, BaseViewHolder> {

    private CustomDatePicker mTimerPicker;
    private TextView tvStartTime, tvOverTime;
    private List<GradeSubject.GradeSubjectData> mDataList;
    private static final int START_TIME_TYPE = 0;
    private static final int OVER_TIME_TYPE = 1;

    public ClassAdapter(int layoutResId, @Nullable List<GradeSubject.GradeSubjectData> data) {
        super(layoutResId, data);
        mDataList = data;
    }

//
//    public ClassAdapter(Context context, int layoutId, List<GradeSubject.GradeSubjectData> datas) {
//        super(context, layoutId, datas);
//    }

    @Override
    protected void convert(BaseViewHolder helper, GradeSubject.GradeSubjectData item) {
        helper.setIsRecyclable(false);
        tvStartTime = helper.getView(R.id.start_time);
        tvOverTime = helper.getView(R.id.over_time);
        final CheckBox perpaerLessons = helper.getView(R.id.cb_prepare_lessons);
        helper.setText(R.id.cb_prepare_lessons, item.getGradeName() + item.getSubjectName());

        if (item.getStartTime() != null) {
            tvStartTime.setText(item.getStartTime());
        }

        if (item.getOverTime() != null) {
            tvOverTime.setText(item.getOverTime());
        }
        perpaerLessons.setChecked(item.getSelected());
        helper.addOnClickListener(R.id.start_time);
        helper.addOnClickListener(R.id.over_time);
        helper.addOnClickListener(R.id.cb_prepare_lessons);


        this.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mDataList = getData();
                int type = START_TIME_TYPE;
                TextView textView;
                switch (view.getId()) {
                    case R.id.start_time:
                        textView = (TextView) view;
                        initTimerPicker(position, type);
                        mTimerPicker.show((textView.getText().toString()));
                        break;
                    case R.id.over_time:
                        type = OVER_TIME_TYPE;
                        textView = (TextView) view;
                        initTimerPicker(position, type);
                        mTimerPicker.show((textView.getText().toString()));
                        break;
                    case R.id.cb_prepare_lessons:
                        CheckBox mCheckBox = (CheckBox) view;
                        mDataList.get(position).setSelected(mCheckBox.isChecked());
                        Log.d(TAG, "onItemChildClick: "+    mDataList.get(position).getSelected());
                        setNewData(mDataList);
                        break;
                }
            }
        });
    }

//    private TextWatcher watcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//            try {
//                String trim = tvStartTime.getText().toString();
//                item.setStartTime(trim);
//            } catch (Exception e) {
//            }
//        };


    private void initTimerPicker(final int po, final int type) {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(mContext, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                if (type == START_TIME_TYPE) {
                    mDataList.get(po).setStartTime(DateFormatUtils.long2Str(timestamp, true));
                } else {
                    mDataList.get(po).setOverTime(DateFormatUtils.long2Str(timestamp, true));
                }
                setNewData(mDataList);
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }


}
