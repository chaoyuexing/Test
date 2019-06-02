package com.homework.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.ClazzAdapter;
import com.homework.teacher.adapter.GradeAdapter;
import com.homework.teacher.adapter.SubjectAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.Clazz;
import com.homework.teacher.data.Grade;
import com.homework.teacher.data.Subject;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我任教的班级
 * <p>
 * Created by zhangkaichao on 2019/2/17.
 */

public class SubjectClassActivity extends Activity {
    private final static String TAG = SubjectClassActivity.class.getName();
    private GridView mSubjectGirdView;
    private List<Subject> mSubjectList;
    private SubjectAdapter mSubjectAdapter;

    private GridView mGradeGirdView;
    private List<Grade> mGradeList;
    private GradeAdapter mGradeAdapter;

    private GridView mClassGirdView;
    private List<Clazz> mClazzList;
    private ClazzAdapter mClazzAdapter;

    private Button mOkBtn;
    private String subjectCode;// 学科编码
    private int gradeID;// 年级ID
    private List<Integer> classIds = new ArrayList<>();// 班级ID数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_class);

        ((Button) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.title)).setText("我任教的班级");
        ((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

        mSubjectGirdView = (GridView) findViewById(R.id.subjectGridView);
        if (mSubjectList == null) {
            mSubjectList = new ArrayList<Subject>();
        }
        mSubjectAdapter = new SubjectAdapter(SubjectClassActivity.this, mSubjectList);
        mSubjectGirdView.setAdapter(mSubjectAdapter);
        mSubjectGirdView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        mSubjectAdapter = new SubjectAdapter(
                                SubjectClassActivity.this, position,
                                mSubjectList);
                        mSubjectAdapter.notifyDataSetChanged();
                        mSubjectGirdView.setAdapter(mSubjectAdapter);
                        subjectCode = mSubjectList.get(position).getCode();
                        Log.i(TAG, "选择的subjectCode: " + subjectCode);
                        getGradeBySubject(subjectCode);
                    }
                });

        mGradeGirdView = (GridView) findViewById(R.id.gradeGridView);
        if (mGradeList == null) {
            mGradeList = new ArrayList<Grade>();
        }
        mGradeAdapter = new GradeAdapter(SubjectClassActivity.this, mGradeList);
        mGradeGirdView.setAdapter(mGradeAdapter);
        mGradeGirdView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        mGradeAdapter = new GradeAdapter(
                                SubjectClassActivity.this, position,
                                mGradeList);
                        mGradeAdapter.notifyDataSetChanged();
                        mGradeGirdView.setAdapter(mGradeAdapter);
                        gradeID = mGradeList.get(position).getId();
                        Log.i(TAG, "选择的gradeID: " + gradeID);
                        getClassByGrade(gradeID);
                    }
                });

        mClassGirdView = (GridView) findViewById(R.id.classGridView);
        if (mClazzList == null) {
            mClazzList = new ArrayList<Clazz>();
        }
        mClazzAdapter = new ClazzAdapter(SubjectClassActivity.this, mClazzList);
        mClassGirdView.setAdapter(mClazzAdapter);
        mClassGirdView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
//                        mClazzAdapter = new ClazzAdapter(
//                                SubjectClassActivity.this, position,
//                                mClazzList);
//                        mClazzAdapter.notifyDataSetChanged();
//                        mClassGirdView.setAdapter(mClazzAdapter);
                        int classId = mClazzList.get(position).getNumber();// 班级ID
                        Log.i(TAG, "选择的classId: " + classId);
                        classIds.add(classId);
                        ((TextView) view.findViewById(R.id.subjectTv)).setBackgroundResource(R.drawable.subject_click);
                        ((TextView) view.findViewById(R.id.subjectTv)).setTextColor(Color.WHITE);
                        mOkBtn.setVisibility(View.VISIBLE);
                    }
                });
        mOkBtn = (Button) findViewById(R.id.okBtn);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectApply();
            }
        });
        getSubject();
    }

    private void getSubject() {
        String url = Consts.SERVER_getSubject;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";

        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = "";// 响应体中的code
                            JSONArray data = null; // 响应体中的data
                            if (jsonObject != null) {
                                code = jsonObject.getString("code");
                                if ("0".equals(code)) {
                                    data = new JSONArray(
                                            jsonObject.getString("data"));
                                    if (data != null) {
                                        mSubjectList.clear();
                                        mSubjectList.addAll(Subject.parseJson(data));
                                        mSubjectAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0,
                        SubjectClassActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void getGradeBySubject(String subjectCode) {
        String url = Consts.SERVER_getGradeBySubject + "/" + subjectCode;// 学科编码
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";

        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = "";// 响应体中的code
                            JSONArray data = null; // 响应体中的data
                            if (jsonObject != null) {
                                code = jsonObject.getString("code");
                                if ("0".equals(code)) {
                                    data = new JSONArray(
                                            jsonObject.getString("data"));
                                    if (data != null) {
                                        mGradeList.clear();
                                        mGradeList.addAll(Grade.parseJson(data));
                                        mGradeAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0,
                        SubjectClassActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void getClassByGrade(int gradeID) {
        String url = Consts.SERVER_getClassByGrade + "/" + gradeID;// 年级ID
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";

        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = "";// 响应体中的code
                            JSONArray data = null; // 响应体中的data
                            if (jsonObject != null) {
                                code = jsonObject.getString("code");
                                if ("0".equals(code)) {
                                    data = new JSONArray(
                                            jsonObject.getString("data"));
                                    if (data != null) {
                                        mClazzList.clear();
                                        mClazzList.addAll(Clazz.parseJson(data));
                                        mClazzAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0,
                        SubjectClassActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void subjectApply() {
        JSONObject jsonObject = new JSONObject();
        try { // TeacherSubjectApplyDto
            JSONArray classIdsJsonArray = new JSONArray();
            for (int i = 0; i < classIds.size(); i++) {
                classIdsJsonArray.put(classIds.get(i));
            }
            jsonObject.put("classIds", classIdsJsonArray);// 班级ID数组
            jsonObject.put("subjectCode", subjectCode);// 学科编码
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = WDStringRequest.getUrl(Consts.SERVER_subjectApply,
                jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();

        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url,
                relative_url, sign_body, true, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = "";// 响应体中的code
                    if (jsonObject != null) {
                        code = jsonObject.getString("code");
                        if ("0".equals(code)) {
                            Toast.makeText(SubjectClassActivity.this,
                                    "申请成功", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent();
                            intent.setClass(SubjectClassActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SubjectClassActivity.this,
                                    jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0,
                        SubjectClassActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

}
