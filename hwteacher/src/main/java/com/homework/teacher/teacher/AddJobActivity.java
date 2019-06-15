package com.homework.teacher.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.CreateJob;
import com.homework.teacher.data.GradeSubject;
import com.homework.teacher.data.JobData;
import com.homework.teacher.data.Simple;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.teacher.Adapter.JobAdapter;
import com.homework.teacher.utils.StatusUtils;
import com.multilevel.treelist.Node;

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

    private Context mContext;
    private JobAdapter mJobAdapter;
    private ArrayList<JobData> jobList = new ArrayList<>();
    private int groupClassID;
    private int jobID;
    private String gradeSubjectName;
    private List<GradeSubject.GradeSubjectData> mSubjectList = new ArrayList<>();
    public ArrayList<Node> nodeLinkedList = new ArrayList<>();


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
        mSelectTeamGroup.setOnClickListener(this);
        mAddJobIv.setOnClickListener(this);
        initData(groupClassID);
    }

    private void initToolbar() {
        mTvTitle.setText(gradeSubjectName + "作业");
        mAddJobToolbar.setNavigationIcon(R.mipmap.common_ic_back);
        mAddJobToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(int gsID) {
        JSONObject jsonObject = new JSONObject();
        String url = WDStringRequest.getUrl(Consts.SERVER_workSheetCreate + gsID, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url,
                relative_url, sign_body, true, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CreateJob createJob = new CreateJob().getFromGson(response);
                if (createJob != null && createJob.getCode() == Consts.REQUEST_SUCCEED_CODE) {
                    CreateJob.CreateJobdata data = createJob.getData();
                    jobID = data.getId();
                    initView();
                } else {
                    Toast.makeText(mContext, createJob.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0,
                        mContext);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void initView() {


        mSelfStudyJobEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d(TAG, "onFocusChange: mSelfStudyJobEt" + "失去焦点");
                    saveJob();
                }
            }
        });
        mJobNameEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d(TAG, "onFocusChange: mJobNameEt" + "失去焦点");
                    saveJob();
                }
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
        WDStringRequest mRequest = new WDStringRequest(Request.Method.PUT, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Simple simple = new Simple().getFromGson(response);
                        if (simple != null && simple.getCode() == Consts.REQUEST_SUCCEED_CODE) {
                        } else {
                            Toast.makeText(mContext, simple.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0, mContext);
            }
        });
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

//    private void addJob() {
//        final EditText et = new EditText(this);
//        new AlertDialog.Builder(this).setTitle("请输入互动作业名称")
//                .setIcon(android.R.drawable.sym_def_app_icon)
//                .setView(et)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        JobData data = new JobData();
//                        data.setJobName(et.getText().toString());
//                        List<JobData.Problems> problemsList = new ArrayList<>();
//                        ArrayList<String> answer = new ArrayList<>();
//                        answer.add("1");
//                        ArrayList<String> video = new ArrayList<>();
//                        video.add("1");
//                        for (int j = 0; j < DEFAULT_PROBLEMS_NUM; j++) {
//                            JobData.Problems problems = new JobData.Problems();
//                            problems.setAnswer(answer);
//                            problems.setVideo(video);
//                            problemsList.add(problems);
//                        }
//                        data.setProblemsArrayList(problemsList);
//                        jobList.add(data);
//                        mJobAdapter.notifyDataSetChanged();
//                    }
//                }).setNegativeButton("取消", null).show();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mJobAdapter.setModuleID(data.getIntExtra("moduleID",0));
            mJobAdapter.setTitle(data.getStringExtra("title"));
            notifyListData((ArrayList<Node>) data.getSerializableExtra("list"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    private void getPaperList(int id) {
//        String url = Consts.SERVER_MEDIUM_CATALOG + id;
//        String relative_url = url.replace(Consts.SERVER_IP, "");
//        String sign_body = "";
//        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
//                relative_url, sign_body, false,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        MediumCatalog mediumCatalog = new MediumCatalog().getFromGson(response);
//                        if (mediumCatalog != null && mediumCatalog.getCode().equals(Consts.REQUEST_SUCCEED)) {
//                        } else {
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError arg0) {
//                StatusUtils.handleError(arg0, mContext);
//            }
//        });
//        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
//    }

    private void notifyListData(List<Node> dataBeanList) {
        for (int i = 0; i < dataBeanList.size(); i++) {
            nodeLinkedList.add(dataBeanList.get(i));
            notifyListData(dataBeanList.get(i).get_childrenList());
        }
        mJobAdapter.setNewData(nodeLinkedList);
    }
}
