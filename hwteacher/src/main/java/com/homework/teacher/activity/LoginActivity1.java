package com.homework.teacher.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Constants;
import com.android.volley.Request;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.CompareStringArrayUtil;
import com.homework.teacher.utils.HmacSHA1Utils;
import com.homework.teacher.utils.SpUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.StringUtils;
import com.homework.teacher.widget.XEditText;
import com.tencent.liteav.demo.common.utils.TimeUtil;
import com.tencent.liteav.demo.common.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 登录页面
 * <p>
 * Created by zhangkaichao on 2019/2/11.
 */

public class LoginActivity1 extends Activity implements EasyPermissions.PermissionCallbacks {


    private static final String TAG = LoginActivity1.class.getName();
    private static final int REQUST_PERMISSIONS_CODE = 123;//用于验证获取的权

    private EditText mAccountEt;
    private XEditText mPasswordEt;
    private Button mLoginBtn;
    private TextView mRegisterTv;
    private String account, passWord;

    private Context mContext;
    private String[] permissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private boolean requstPermissionResult = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        checkPerm();
        setContentView(R.layout.activity_login2);
        mContext = this;
        initUI();
    }

    private void checkLoginTime() {
        String time = SpUtils.get(mContext, Constants.SP_KEY_LOGIN_TIME, "").toString();
        if (!time.equals("")) {
            if (TimeUtil.isValidDate(time)) {
                String data = SpUtils.get(mContext, Constants.SP_KEY_SALT, "").toString();
                if (!data.equals("")) {
                    account = SpUtils.get(mContext, Constants.SP_KEY_ACCOUNT, "").toString();
                    passWord = SpUtils.get(mContext, Constants.SP_KEY_PASS_WORD, "").toString();
                    encryption(data);
                }
            }
        }
    }

    private void initUI() {
        mAccountEt = findViewById(R.id.accountEt);
        mPasswordEt = findViewById(R.id.passWordEt);
        mLoginBtn = findViewById(R.id.loginBtn);
        mRegisterTv = findViewById(R.id.registerTv);
        mLoginBtn.setOnClickListener(v -> {
            account = mAccountEt.getText().toString().trim();
            passWord = mPasswordEt.getText().toString().trim();
            if (StringUtils.isEmpty(account) || StringUtils.isEmpty(passWord)) {
                ToastUtils.showToast(mContext, "登录信息填写不完整");
            } else {
                if (requstPermissionResult) {
                    getSalt(account);
                } else {
                    ToastUtils.showToast(mContext, "所需要的权限不足，无法登陆");
                    checkPerm();
                }
            }
        });
        mRegisterTv.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(LoginActivity1.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void getSalt(String account) {
        String url = Consts.SERVER_getSalt + "/" + account;// account手机号或身份证号
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String code = "";// 响应体中的code
                String data = null; // 响应体中的data
                if (jsonObject != null) {
                    code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        data = jsonObject.optString("data");
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
        }, arg0 -> StatusUtils.handleError(arg0, mContext));
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void encryption(String data) {
        SpUtils.put(mContext, Constants.SP_KEY_SALT, data);
        SpUtils.put(mContext, Constants.SP_KEY_PASS_WORD, passWord);
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

        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url, relative_url, sign_body, true, response -> {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                String code = "";// 响应体中的code
                if (jsonObject1 != null) {
                    code = jsonObject1.getString("code");
                    if ("0".equals(code)) {
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity1.this, ChooseClassActivity.class);
                        startActivity(intent);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        //获取当前时间日期--nowDate
                        String nowDate = format.format(new Date());
                        SpUtils.put(mContext, Constants.SP_KEY_LOGIN_TIME, nowDate);
                        SpUtils.put(mContext, Constants.SP_KEY_ACCOUNT, account);
                    } else {
                        Toast.makeText(LoginActivity1.this,
                                jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, arg0 -> StatusUtils.handleError(arg0,
                LoginActivity1.this));
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    @AfterPermissionGranted(REQUST_PERMISSIONS_CODE)
    private void checkPerm() {
        if (!EasyPermissions.hasPermissions(this, permissions)) {
            EasyPermissions.requestPermissions(this, "申请权限", REQUST_PERMISSIONS_CODE, permissions);
        } else {
            checkLoginTime();
            requstPermissionResult = true;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        String[] strings = new String[perms.size()];
        requstPermissionResult = CompareStringArrayUtil.differenceSet(perms.toArray(strings), permissions).length == 0;
        if (requstPermissionResult) {
            checkLoginTime();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}