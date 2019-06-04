package com.homework.teacher.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.Constants;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.PaperAdd;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.SpUtils;
import com.homework.teacher.utils.StatusUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xing
 * on 2019/5/20 添加试卷
 */
public class AddPaperActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = AddPaperActivity.class.getSimpleName();
    private static int LAST_SEMESTER = 1; // 上学期
    private static int BELOW_SEMESTER = 2; // 下学期

    @BindView(R.id.back)
    Button mBack;
    @BindView(R.id.job_name_et)
    EditText mJobNameEt;
    @BindView(R.id.last_semester)
    RadioButton mLastSemester;
    @BindView(R.id.below_semester)
    RadioButton mBelowSemester;
    @BindView(R.id.paper_next)
    Button mPaperNext;

    private Context mContext;
    private int term = LAST_SEMESTER;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test_paper);
        ButterKnife.bind(this);
        mBack.setOnClickListener(this);
        mPaperNext.setOnClickListener(this);
        mLastSemester.setOnClickListener(this);
        mBelowSemester.setOnClickListener(this);
        mContext = this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.paper_next:
                addParper();
                break;
            case R.id.last_semester:
                term = LAST_SEMESTER;
                break;
            case R.id.below_semester:
                term = BELOW_SEMESTER;
                break;
        }
    }

    private void addParper() {
        name= mJobNameEt.getText().toString();
        if (name.equals(""))  {
            com.homework.teacher.utils.Toast.showLong(mContext,"试卷名称不能为空");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try { // TeacherLoginDto
            jsonObject.put("gsID",SpUtils.get(mContext,  Constants.SP_KEY_GRADEID, 0));// 手机号or身份证号
            jsonObject.put("name", name);
            jsonObject.put("term", term);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WDStringRequest.getUrl(Consts.SERVER_PAPER_ADD, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url,
                relative_url, sign_body, true, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                PaperAdd paperAdd = new PaperAdd().getFromGson(response);
                if (paperAdd != null && paperAdd.getCode() == Consts.REQUEST_SUCCEED_CODE) {
                    Intent intent = new Intent(mContext,AddPaper2Activity.class);
                    intent.putExtra("paperName",name);
                    intent.putExtra("paperID",paperAdd.getData().getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, paperAdd.getMessage(), Toast.LENGTH_SHORT).show();
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


}
