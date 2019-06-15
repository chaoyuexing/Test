package com.homework.teacher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Constants;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.HmacSHA1Utils;
import com.homework.teacher.utils.SpUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.StringUtils;
import com.homework.teacher.widget.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 登录页面
 * <p>
 * Created by zhangkaichao on 2019/2/11.
 */

public class LoginActivity1 extends Activity {
    private static final String TAG = LoginActivity1.class.getName();
//    private SharedPreferences sp;
    private EditText mAccountEt;
    private XEditText mPasswordEt;
    private Button mLoginBtn;
    private TextView mRegisterTv;
    private String account, passWord;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        String time = SpUtils.get(mContext,Constants.SP_KEY_LOGIN_TIME,"").toString();
        if (!time.equals("")) {
//            if (TimeUtil.isValidDate(time)) {
                String data = SpUtils.get(mContext,Constants.SP_KEY_SALT,"").toString();
                if (!data.equals("")) {
                    account =  SpUtils.get(mContext,Constants.SP_KEY_ACCOUNT,"").toString();
                    passWord =  SpUtils.get(mContext,Constants.SP_KEY_PASS_WORD,"").toString();
                    encryption(data);
//                }
            }
        }
        setContentView(R.layout.activity_login2);
        mContext = this;
        initUI();
    }

    private void initUI() {
//        sp = BaseApplication.getInstance().getSp();
        mAccountEt = ((EditText) findViewById(R.id.accountEt));
        mPasswordEt = ((XEditText) findViewById(R.id.passWordEt));
        mLoginBtn = ((Button) findViewById(R.id.loginBtn));
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = mAccountEt.getText().toString().trim();
                passWord = mPasswordEt.getText().toString().trim();
                if (StringUtils.isEmpty(account) || StringUtils.isEmpty(passWord)) {
                    Toast.makeText(LoginActivity1.this,
                            "登录信息填写不完整", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    getSalt(account);
                }
            }
        });
        mRegisterTv = ((TextView) findViewById(R.id.registerTv));
        mRegisterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity1.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getSalt(String account) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("loginName", phoneNumber);// 登录名
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        String url = WDStringRequest.getUrl(Consts.SERVER_getSalt, jsonObject);
        String url = Consts.SERVER_getSalt + "/" + account;// account手机号或身份证号
//        String relative_url = WDStringRequest.getRelativeUrl();
        String relative_url = url.replace(Consts.SERVER_IP, "");
//        String sign_body = WDStringRequest.getSignBody();
        String sign_body = "";

        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = "";// 响应体中的code
                            String data = null; // 响应体中的data
                            if (jsonObject != null) {
                                code = jsonObject.getString("code");
                                if ("0".equals(code)) {
                                    data = jsonObject.optString("data");
//                                    Toast.makeText(LoginActivity1.this, "salt: " + data, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity1.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (data != null) {
                                encryption(data);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0,
                        LoginActivity1.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void encryption(String data) {
        SpUtils.put(mContext,Constants.SP_KEY_SALT,data);
        passWord = Base64.encodeToString(HmacSHA1Utils.hwGetHmacSHA1(passWord, data), Base64.NO_WRAP);
        login();
    }

    private void login() {
        JSONObject jsonObject = new JSONObject();
        try { // TeacherLoginDto
            jsonObject.put("account", account);// 手机号or身份证号
            jsonObject.put("passWord", passWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = WDStringRequest.getUrl(Consts.SERVER_login,
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
                    JSONObject data = null; // 响应体中的data
                    if (jsonObject != null) {
                        code = jsonObject.getString("code");
                        if ("0".equals(code)) {
                            Intent intent = new Intent();
//                            intent.setClass(LoginActivity1.this, MainActivity.class);
                            intent.setClass(LoginActivity1.this, ChooseClassActivity.class);
                            startActivity(intent);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            //获取当前时间日期--nowDate
                            String nowDate = format.format(new Date());
                            SpUtils.put(mContext,Constants.SP_KEY_LOGIN_TIME,nowDate);
                            SpUtils.put(mContext,Constants.SP_KEY_ACCOUNT,account);
                            SpUtils.put(mContext,Constants.SP_KEY_PASS_WORD,passWord);
                        } else {
                            Toast.makeText(LoginActivity1.this,
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
                        LoginActivity1.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

}