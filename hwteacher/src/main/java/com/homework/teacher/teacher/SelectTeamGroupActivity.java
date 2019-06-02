package com.homework.teacher.teacher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.datepicker.DateFormatUtils;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.GradeSubject;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.teacher.Adapter.ClassAdapter;
import com.homework.teacher.teacher.Adapter.prepareLessonsAdapter;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.Toast;
import com.homework.teacher.widget.CustomDatePicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xing
 * on 2019/4/29
 */
public class SelectTeamGroupActivity extends Activity {

    private final static String TAG = SelectTeamGroupActivity.class.getSimpleName();
    public static final int CLASS_TYPE = 1;
    private static final int PREPARE_LESSONS_TYPE = 2;
    private static final int START_TIME_TYPE = 0;
    private static final int OVER_TIME_TYPE = 1;


    @BindView(R.id.back)
    Button mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.tvSet)
    TextView mTvSet;
    @BindView(R.id.prepare_lessons_recycler_view)
    RecyclerView mPrepareLessonsRecyclerView;
    @BindView(R.id.my_class_recycler_view)
    RecyclerView mMyClassRecyclerView;
    @BindView(R.id.select_team_group)
    Button mSelectTeamGroup;

    private List<GradeSubject.GradeSubjectData> mSubjectList = new ArrayList<>();
    private List<GradeSubject.GradeSubjectData> mClasstList = new ArrayList<>();
    private prepareLessonsAdapter mPrepareLessonsAdapter;
    private ClassAdapter mClassAdapter;
    private CustomDatePicker mTimerPicker;
    private TextView tvStartTime, tvOverTime;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team_group);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initData();

    }

    private void initView() {
        mSubjectList = (List<GradeSubject.GradeSubjectData>) getIntent().getSerializableExtra("mSubjectList");
        mPrepareLessonsAdapter = new prepareLessonsAdapter(R.layout.item_select_tean_group, mSubjectList, PREPARE_LESSONS_TYPE);
        mPrepareLessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPrepareLessonsRecyclerView.setAdapter(mPrepareLessonsAdapter);
        mPrepareLessonsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        mClassAdapter = new ClassAdapter(R.layout.item_class_team, mClasstList);
        mMyClassRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mMyClassRecyclerView.setAdapter(mClassAdapter);
        getTeacherGradeSubject();
    }

    private void initData() {
    }


    private void getTeacherGradeSubject() {
        String url = Consts.SERVER_CLASS_SUBJECT;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GradeSubject subjectClass = new GradeSubject().getFromGson(response);
                        if (subjectClass != null && subjectClass.code == Consts.REQUEST_SUCCEED_CODE) {
                            mClasstList = subjectClass.data;
                            mClassAdapter.setNewData(mClasstList);
                        } else {
                            Toast.showLong(mContext, subjectClass.message);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0, SelectTeamGroupActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }


    private void showSubject(final List<GradeSubject> list) {


    }


    private void initTimerPicker(final TextView textView, final int po, final int type) {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        textView.setText(endTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(mContext, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                if (type == START_TIME_TYPE) {
                    mClasstList.get(po).setStartTime(DateFormatUtils.long2Str(timestamp, true));
                } else {
                    mClasstList.get(po).setOverTime(DateFormatUtils.long2Str(timestamp, true));
                }
                mClassAdapter.notifyDataSetChanged();
//                textView.setText();
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
