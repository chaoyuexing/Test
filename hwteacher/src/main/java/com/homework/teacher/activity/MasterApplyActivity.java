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
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.Clazz;
import com.homework.teacher.data.Grade;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 班主任申请
 * <p>
 * Created by zhangkaichao on 2019/2/17.
 */

public class MasterApplyActivity extends Activity {
    private final static String TAG = MasterApplyActivity.class.getName();
    private GridView mGradeGirdView;
    private List<Grade> mGradeList;
    private GradeAdapter mGradeAdapter;

    private GridView mClassGirdView;
    private List<Clazz> mClazzList;
    private ClazzAdapter mClazzAdapter;

    private Button mOkBtn;
    private int gradeID;// 年级ID
    private List<Integer> ids = new ArrayList<>();// 班级ID数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_apply);

        ((Button) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.title)).setText("班主任申请");
        ((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);

        mGradeGirdView = (GridView) findViewById(R.id.gradeGridView);
        if (mGradeList == null) {
            mGradeList = new ArrayList<Grade>();
        }
        mGradeAdapter = new GradeAdapter(MasterApplyActivity.this, mGradeList);
        mGradeGirdView.setAdapter(mGradeAdapter);
        mGradeGirdView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        mGradeAdapter = new GradeAdapter(
                                MasterApplyActivity.this, position,
                                mGradeList);
                        mGradeAdapter.notifyDataSetChanged();
                        mGradeGirdView.setAdapter(mGradeAdapter);
                        gradeID = mGradeList.get(position).getId();
                        Log.i(TAG, "选择的gradeID: " + gradeID);
                        listCanBeApply(gradeID);
                    }
                });

        mClassGirdView = (GridView) findViewById(R.id.classGridView);
        if (mClazzList == null) {
            mClazzList = new ArrayList<Clazz>();
        }
        mClazzAdapter = new ClazzAdapter(MasterApplyActivity.this, mClazzList);
        mClassGirdView.setAdapter(mClazzAdapter);
        mClassGirdView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
//                        mClazzAdapter = new ClazzAdapter(
//                                MasterApplyActivity.this, position,
//                                mClazzList);
//                        mClazzAdapter.notifyDataSetChanged();
//                        mClassGirdView.setAdapter(mClazzAdapter);
                        int classId = mClazzList.get(position).getNumber();// 班级ID
                        Log.i(TAG, "选择的classId: " + classId);
                        ids.add(classId);
                        ((TextView) view.findViewById(R.id.subjectTv)).setBackgroundResource(R.drawable.subject_click);
                        ((TextView) view.findViewById(R.id.subjectTv)).setTextColor(Color.WHITE);
                        mOkBtn.setVisibility(View.VISIBLE);
                    }
                });
        mOkBtn = (Button) findViewById(R.id.okBtn);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                masterApply();
            }
        });
        getGradeList();
    }

    private void getGradeList() {
        JSONObject jsonObject = new JSONObject();
        try { // SchoolGradeListDto
            jsonObject.put("delFlag", 0);// 是否删除，0：否，1：是
            jsonObject.put("finishFlag", 0);// 是否毕业，0：否，1：是
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = WDStringRequest.getUrl(Consts.SERVER_getGradeList,
                jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();

        WDStringRequest mRequest = new WDStringRequest(Request.Method.PUT, url,
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
                        MasterApplyActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void listCanBeApply(int gradeID) {
        String url = Consts.SERVER_listCanBeApply + "/" + gradeID;// 年级ID
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
                        MasterApplyActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void masterApply() {
        JSONArray classIds = new JSONArray();
        for (int i = 0; i < ids.size(); i++) {
            classIds.put(ids.get(i));
        }

        String url = WDStringRequest.getUrlForArray(Consts.SERVER_masterApply,
                classIds);
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
                            Toast.makeText(MasterApplyActivity.this,
                                    "申请成功", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent();
                            intent.setClass(MasterApplyActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MasterApplyActivity.this,
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
                        MasterApplyActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

}

