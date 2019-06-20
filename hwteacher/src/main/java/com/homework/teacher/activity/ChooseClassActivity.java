package com.homework.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.ClazzListAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.Clazz;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.widget.MyListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择任职班级
 * <p>
 * Created by zhangkaichao on 2019/2/16.
 */

public class ChooseClassActivity extends Activity {
    private final static String TAG = ChooseClassActivity.class.getName();
    private TextView mMasterClass, mSubjectClass;
    private MyListView mMasterClassListView;
    private List<Clazz> mMasterClazzList;
    private ClazzListAdapter mMasterClazzListAdapter;

    private MyListView mSubjectClassListView;
    private List<Clazz> mSubjectClazzList;
    private ClazzListAdapter mSubjectClazzListAdapter;

    private Button mLaterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_class);

        findViewById(R.id.back).setOnClickListener(v -> finish());
        ((TextView) findViewById(R.id.title)).setText("请选择任职班级");
        ((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
        mMasterClass = (TextView) findViewById(R.id.masterClass);
        mSubjectClass = (TextView) findViewById(R.id.subjectClass);

        mMasterClassListView = (MyListView) findViewById(R.id.masterClassListView);
        if (mMasterClazzList == null) {
            mMasterClazzList = new ArrayList<Clazz>();
        }
        mMasterClazzListAdapter = new ClazzListAdapter(ChooseClassActivity.this, 0,
                mMasterClazzList);
        mMasterClassListView.setAdapter(mMasterClazzListAdapter);
        mMasterClassListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        mMasterClazzListAdapter = new ClazzListAdapter(
                                ChooseClassActivity.this, position, mMasterClazzList);
                        mMasterClazzListAdapter.notifyDataSetChanged();
                        mMasterClassListView.setAdapter(mMasterClazzListAdapter);
                        mMasterClassListView.setSelection(position);
                    }
                });

        mSubjectClassListView = (MyListView) findViewById(R.id.subjectClassListView);
        if (mSubjectClazzList == null) {
            mSubjectClazzList = new ArrayList<Clazz>();
        }
        mSubjectClazzListAdapter = new ClazzListAdapter(ChooseClassActivity.this, 0,
                mSubjectClazzList);
        mSubjectClassListView.setAdapter(mSubjectClazzListAdapter);
        mSubjectClassListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        mSubjectClazzListAdapter = new ClazzListAdapter(
                                ChooseClassActivity.this, position, mSubjectClazzList);
                        mSubjectClazzListAdapter.notifyDataSetChanged();
                        mSubjectClassListView.setAdapter(mSubjectClazzListAdapter);
                        mSubjectClassListView.setSelection(position);
                    }
                });

        mLaterBtn = (Button) findViewById(R.id.laterBtn);
        mMasterClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseClassActivity.this, MasterApplyActivity.class);
                startActivity(intent);
            }
        });
        mSubjectClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseClassActivity.this, SubjectClassActivity.class);
                startActivity(intent);
            }
        });
        mLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent();
                intent.setClass(ChooseClassActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        getClassMaster();
        getClassSubject();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getClassMaster();
        getClassSubject();
    }

    private void getClassMaster() {
        String url = Consts.SERVER_getClassMaster;
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
                                    data = new JSONArray(jsonObject.optString("data"));
                                    mMasterClazzList.clear();
                                    mMasterClazzList.addAll(Clazz
                                            .parseJson(data));
                                    mMasterClazzListAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(ChooseClassActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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
                        ChooseClassActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void getClassSubject() {
        String url = Consts.SERVER_getClassSubject;
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
                                    data = new JSONArray(jsonObject.optString("data"));
                                    mSubjectClazzList.clear();
                                    mSubjectClazzList.addAll(Clazz
                                            .parseJson(data));
                                    mSubjectClazzListAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(ChooseClassActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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
                        ChooseClassActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

}
