package com.homework.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.HmacSHA1Utils;
import com.homework.teacher.utils.IDCard;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.StringUtils;
import com.homework.teacher.utils.Utils;
import com.homework.teacher.widget.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * 注册页面
 * <p>
 * Created by zhangkaichao on 2019/2/11.
 */

public class RegisterActivity extends Activity {
    private final static String TAG = RegisterActivity.class.getName();
    private EditText mSchoolIDEt, mRealNameEt, mIdentityNumEt, mMobileNoEt, mEmailEt;
    private XEditText mPassWordEt;
    private RadioButton mMaleRb, mFamaleRb;
    private Button mRegisterBtn;
    private String schoolID, realName, identityNum, mobileNo, email, passWord, salt;
    private boolean mIsShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ((ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.title)).setText(R.string.register);
        ((TextView) findViewById(R.id.tvSet)).setVisibility(View.GONE);
        mSchoolIDEt = (EditText) findViewById(R.id.schoolIDEt);
        mRealNameEt = (EditText) findViewById(R.id.realNameEt);
        mIdentityNumEt = (EditText) findViewById(R.id.identityNumEt);
        mMobileNoEt = (EditText) findViewById(R.id.mobileNoEt);
        mEmailEt = (EditText) findViewById(R.id.emailEt);
        mPassWordEt = (XEditText) findViewById(R.id.passWordEt);
        mPassWordEt.setDrawableRightListener(new XEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                if (mIsShowPassword) {
                    mPassWordEt.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.password, 0,
                            R.drawable.password_visiable, 0);
                    mPassWordEt.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIsShowPassword = false;
                } else {
                    mPassWordEt.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.password, 0,
                            R.drawable.password_invisiable, 0);
                    mPassWordEt
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mIsShowPassword = true;
                }
            }
        });
        mMaleRb = (RadioButton) findViewById(R.id.male_rb);
        mFamaleRb = (RadioButton) findViewById(R.id.famale_rb);
        mRegisterBtn = (Button) findViewById(R.id.registerBtn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schoolID = mSchoolIDEt.getText().toString().trim();
                realName = mRealNameEt.getText().toString().trim();
                identityNum = mIdentityNumEt.getText().toString().trim();
                mobileNo = mMobileNoEt.getText().toString().trim();
                email = mEmailEt.getText().toString().trim();
                passWord = mPassWordEt.getText().toString().trim();
                try {
                    if (StringUtils.isEmpty(schoolID) || StringUtils.isEmpty(realName) || StringUtils.isEmpty(identityNum) ||
                            StringUtils.isEmpty(mobileNo) || StringUtils.isEmpty(email) || StringUtils.isEmpty(passWord)) {
                        Toast.makeText(RegisterActivity.this,
                                "注册信息填写不完整", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        if (!IDCard.IDCardValidate(identityNum)) {
                            Toast.makeText(RegisterActivity.this,
                                    R.string.input_correct_identitynum,
                                    Toast.LENGTH_SHORT).show();
                        } else if (!Utils.isPhone(mobileNo)) {
                            Toast.makeText(RegisterActivity.this,
                                    R.string.input_correct_phonenumber,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            register();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void register() {
        JSONObject jsonObject = new JSONObject();
        salt = UUID.randomUUID().toString().trim();
        try { // TeacherRegDto
            jsonObject.put("email", email);// Email

            int gender = 0;
            if (mMaleRb.isChecked()) {
                gender = 1;
            }
            if (mFamaleRb.isChecked()) {
                gender = 2;
            }
            jsonObject.put("gender", gender);// 性别，1：男，2：女
            jsonObject.put("identityNum", identityNum);// 身份证号
            jsonObject.put("mobileNo", mobileNo);// 手机号

            passWord = Base64.encodeToString(
                    HmacSHA1Utils.hwGetHmacSHA1(passWord, salt), Base64.NO_WRAP);
            jsonObject.put("passWord", passWord);// 密码：Base64(HmacSHA1(明文, 盐))

            jsonObject.put("realName", realName);// 真实姓名
            jsonObject.put("salt", salt);// 盐：客户端生成随机UUID
            jsonObject.put("schoolID", schoolID);// 学校号（ID）
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = WDStringRequest.getUrl(Consts.SERVER_register,
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
                            Toast.makeText(RegisterActivity.this,
                                    "注册成功，请等候管理员审核通过", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this, ChooseClassActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this,
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
                        RegisterActivity.this);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

}
