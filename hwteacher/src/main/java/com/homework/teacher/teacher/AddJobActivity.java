package com.homework.teacher.teacher;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.CreateJob;
import com.homework.teacher.data.GradeSubject;
import com.homework.teacher.data.Simple;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.teacher.Adapter.JobAdapter;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.ViewWrapper;
import com.linkage.lib.util.MoblieUtils;
import com.multilevel.treelist.Node;
import com.tencent.liteav.demo.common.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xing
 * on 2019/4/29
 */
public class AddJobActivity extends Activity implements View.OnClickListener {


    private static final int DEFAULT_PROBLEMS_NUM = 5;
    private static final int DEFAULT_GS_ID = 0;
    public static final int DEFAULT_JOB_ID = 0;

    private static final String TAG = AddJobActivity.class.getSimpleName();


    @BindView(R.id.add_job_toolbar)
    Toolbar mAddJobToolbar;
    @BindView(R.id.job_name_et)
    EditText mJobNameEt;
    @BindView(R.id.self_study_job_et)
    EditText mSelfStudyJobEt;
    @BindView(R.id.add_job_iv)
    ImageView mAddJobIv;
    @BindView(R.id.job_recyle_view)
    RecyclerView mJobRecyleView;
    @BindView(R.id.select_team_group)
    Button mSelectTeamGroup;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.NestedScrollView)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.linearLayout)
    LinearLayout mLinearLayout;

    private Context mContext;
    private JobAdapter mJobAdapter;
    private int groupClassID;
    private int jobID;
    private String gradeSubjectName;
    private List<GradeSubject.GradeSubjectData> mSubjectList = new ArrayList<>();
    public ArrayList<Node> nodeLinkedList = new ArrayList<>();
    private boolean isShow = true;
    private int layoutHeight ;

    @SuppressLint("ObjectAnimatorBinding")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        mContext = this;
        ButterKnife.bind(this);
        groupClassID = getIntent().getIntExtra("gsID", DEFAULT_GS_ID);
        gradeSubjectName = getIntent().getStringExtra("gradeSubjectName");
        mSubjectList = (List<GradeSubject.GradeSubjectData>) getIntent().getSerializableExtra("mSubjectList");
        initToolbar();
        mJobRecyleView.setLayoutManager(new LinearLayoutManager(this));
        mJobAdapter = new JobAdapter(this, R.layout.item_interaction_job, nodeLinkedList);
        mJobRecyleView.setAdapter(mJobAdapter);
        mJobRecyleView.setNestedScrollingEnabled(false);
        mSelectTeamGroup.setOnClickListener(this);
        mAddJobIv.setOnClickListener(this);
        jobID = getIntent().getIntExtra("jobID", DEFAULT_JOB_ID);
        layoutHeight = MoblieUtils.dp2px(this,60);
        if (jobID == DEFAULT_JOB_ID) {
            initData(groupClassID);
        }
        ViewWrapper viewWrapper = new ViewWrapper(mLinearLayout);
        mNestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (view, i, i1, i2, i3) -> {
            if (i1 - i3 > 0 && isShow) {
                Log.d(TAG, "下");
                isShow = false;
                ObjectAnimator.ofInt(viewWrapper, "height", layoutHeight, 0).setDuration(500).start();
            } else if (i1 - i3 < 0 && !isShow) {
                Log.d(TAG, "上");
                isShow = true;
                ObjectAnimator.ofInt(viewWrapper, "height", 0, layoutHeight).setDuration(500).start();
            }
        });
    }

    private void initToolbar() {
        mTvTitle.setText(gradeSubjectName + "作业");
        mAddJobToolbar.setNavigationIcon(R.mipmap.common_ic_back);
        mAddJobToolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData(int gsID) {
        JSONObject jsonObject = new JSONObject();
        String url = WDStringRequest.getUrl(Consts.SERVER_workSheetCreate + gsID, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url, relative_url, sign_body, true, response -> {
            CreateJob createJob = new CreateJob().getFromGson(response);
            if (createJob != null) {
                if (createJob.getCode() == Consts.REQUEST_SUCCEED_CODE) {
                    jobID = createJob.getData().getId();
                    initView();
                } else {
                    Toast.makeText(mContext, createJob.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, arg0 -> StatusUtils.handleError(arg0, mContext));
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void initView() {
        mSelfStudyJobEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Log.d(TAG, "onFocusChange: mSelfStudyJobEt" + "失去焦点");
                saveJob();
            }
        });
        mJobNameEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Log.d(TAG, "onFocusChange: mJobNameEt" + "失去焦点");
                saveJob();
            }
        });
    }

    /**
     * 保存作业
     */
    private void saveJob() {
        JSONObject jsonObject = new JSONObject();
        try { // SchoolGradeListDto
            jsonObject.put("freedomContent", mSelfStudyJobEt.getText().toString());// 自主作业内容
            jsonObject.put("id", jobID);// 作业单ID
            jsonObject.put("wsName", mJobNameEt.getText().toString());// 作业单ID
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WDStringRequest.getUrl(Consts.SERVER_save, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.PUT, url, relative_url, sign_body, false, response -> {
            Simple simple = new Simple().getFromGson(response);
            if (simple != null) {
                if (simple.getCode() == Consts.REQUEST_SUCCEED_CODE) {
                    ToastUtils.showToast(mContext, "保存成功");
                } else {
                    Toast.makeText(mContext, simple.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, arg0 -> StatusUtils.handleError(arg0, mContext));
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.select_team_group:
                intent.setClass(this, SelectTeamGroupActivity.class);
                intent.putExtra("mSubjectList", (Serializable) mSubjectList);
                startActivity(intent);
                break;
            case R.id.add_job_iv:
                intent.setClass(this, JobContentActivity.class);
                intent.putExtra("mSubjectList", (Serializable) mSubjectList);
                intent.putExtra("gradeSubjectName", gradeSubjectName);
                intent.putExtra("groupClassID", groupClassID);
                startActivityForResult(intent, 1001);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mJobAdapter.setModuleID(data.getIntExtra("moduleID", 0));
            mJobAdapter.setTitle(data.getStringExtra("title"));
            nodeLinkedList.clear();
            notifyListData((ArrayList<Node>) data.getSerializableExtra("list"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void notifyListData(List<Node> dataBeanList) {
        for (int i = 0; i < dataBeanList.size(); i++) {
            if (dataBeanList.get(i).get_childrenList() == null || dataBeanList.get(i).get_childrenList().size() == 0) {
                nodeLinkedList.add(dataBeanList.get(i));
            } else {
                notifyListData(dataBeanList.get(i).get_childrenList());
            }
        }
        mJobAdapter.setNewData(nodeLinkedList);
    }
}
