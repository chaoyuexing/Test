package com.homework.teacher.http;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.homework.teacher.Consts;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.utils.HmacSHA1Utils;
import com.homework.teacher.utils.RSAUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A canned request for retrieving the response body at a given URL as a String.
 */
public class WDStringRequest extends Request<String> {

    private static final String TAG = "HttpLogger";

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "SESSION";
    private SharedPreferences sp;

    private final String mRelativeUrl;// 相对地址 URX = (""==queryString) ? URI : (URI?queryString)
    private final String mSignBody;// 请求体
    private final boolean mIsPostRequest;// 是否Post请求,Post需防重放
    private final Listener<String> mListener;

    private static String relative_url;
    private static String sign_body;

    /**
     * 检查返回的Response header中有没有session
     *
     * @param responseHeaders Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> responseHeaders) {
        if (responseHeaders.containsKey(SET_COOKIE_KEY)
                && responseHeaders.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = responseHeaders.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                Log.e("sessionid", cookie);
                sp = BaseApplication.getInstance().getSp();
                sp.edit().putString(SESSION_COOKIE, cookie).commit();
            }
        }
    }

    /**
     * 添加session到Request header中
     */
    public final void addSessionCookie(Map<String, String> requestHeaders) {
        sp = BaseApplication.getInstance().getSp();
        String sessionId = BaseApplication.getInstance().getSessionCookie();
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (requestHeaders.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(requestHeaders.get(COOKIE_KEY));
            }
            requestHeaders.put(COOKIE_KEY, builder.toString());
        }
    }

    /**
     * Creates a new request with the given method.
     *
     * @param method        the request {@link Method} to use
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public WDStringRequest(int method, String url, String relative_url,
                           String sign_body, boolean isPostRequest, Listener<String> listener,
                           ErrorListener errorListener) {
        super(method, url, errorListener);
        mRelativeUrl = relative_url;
        mSignBody = sign_body;
        mIsPostRequest = isPostRequest;
        mListener = listener;
    }

    /**
     * Creates a new GET request.
     *
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public WDStringRequest(String url, String relative_url, String sign_body,
                           boolean isAntiReplay, Listener<String> listener,
                           ErrorListener errorListener) {
        this(Method.GET, url, relative_url, sign_body, isAntiReplay, listener,
                errorListener);
    }

    //将已经解析成想要的类型的内容传递给它们的监听回调。
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    //将网络返回的字节数据转换成想要的类型（如String，Onject或者其他）
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            checkSessionCookie(response.headers);
            JSONObject reponseJsonObject = new JSONObject();// 定义响应JSONObject
            String body = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG, "body：" + body);
//            String bodyJson = new String(Base64.decode(body, Base64.NO_WRAP),
//                    "utf-8");
//            Log.e("body base64 decode", bodyJson);
//            reponseJsonObject.put("body", bodyJson);// 响应体中的body
//
//            Map<String, String> headers = new HashMap<String, String>();
//            headers = response.headers;
//            Log.e("reponse headers", headers.toString());
//            if (headers != null) {
//                if (headers.get("X-Ca-Msg") != null) {
//                    String msg = new String(Base64.decode(
//                            headers.get("X-Ca-Msg"), Base64.NO_WRAP), "utf-8");
//                    // NO_WRAP 这个参数意思是略去所有的换行符
//                    Log.e("X-Ca-Msg", msg);
//                    reponseJsonObject.put("msg", msg);// 响应头中的msg
//                }
//                if (headers.get("X-Ca-Code") != null) {
//                    int code = Integer.valueOf(headers.get("X-Ca-Code"))
//                            .intValue();
//                    Log.e("X-Ca-Code", code + "");
//                    if (code == 0) {// code==0响应成功
//                        reponseJsonObject.put("code", code);// 响应头中的code
//                    } else if (code == 10403) {// 错误码：010403，请选择综合评分
//                        reponseJsonObject.put("code", code);
//                    } else if (code == 10404) {// 错误码：010404，请选择配送评分
//                        reponseJsonObject.put("code", code);
//                    } else if (code == 10405) {// 错误码：010405，请选择产品评分
//                        reponseJsonObject.put("code", code);
//                    } else if (code == 10501) {// 错误码：010501 ，一周食谱未设置，请设置您的一周食谱
//                        reponseJsonObject.put("code", code);
//                    } else if (code == 10502) {// 错误码：010502 ，用餐计划缺失，请完善您的用餐计划
//                        reponseJsonObject.put("code", code);
//                    } else if (code == 10503) {// 错误码：010503 ，订餐周期不允许超过36天
//                        reponseJsonObject.put("code", code);
//                    } else if (code == 10406) {// 错误码：010406 ，抱歉，已过退订截止时间，无法退订
//                        reponseJsonObject.put("code", code);
//                    } else if (code == 10407) {// 错误码：010407 ，退订失败，可联系客服协助处理
//                        reponseJsonObject.put("code", code);
//                    } else if (code == 10107) {// 错误码：010107 ，登录名已存在
//                        reponseJsonObject.put("code", code);
//                    } else if (code == 10303) {// 错误码：010303 ，原密码错误
//                        reponseJsonObject.put("code", code);
//                    } else {
//                        // code!=0 && code!=010403 && code!=010404 &&
//                        // code!=010405
//                        // && code!=010501 && code!=010502 && code!=010503 &&
//                        // code!=010406 && code!=010407 && code!=010107 &&
//                        // code!=010303
//                        // 响应错误，错误码:10202验证码错误;10203验证码重复获取;10301未登录;10302账户名或密码错误;10105参数缺失;10106参数类型或格式错误;
//                        reponseJsonObject.put("code", 1);// 响应头中的code
//                    }
//                } else {
//                    StatusUtils.handleStatus(reponseJsonObject, null);
//                }
//            }
//            Log.e("reponse", reponseJsonObject.toString());
//
//            return Response.success(reponseJsonObject.toString(),
//                    HttpHeaderParser.parseCacheHeaders(response));
            return Response.success(body, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

//    public Map<String, String> getHeaders() throws AuthFailureError {
//        String sign = mRelativeUrl;
//        Map<String, String> headers = new HashMap<String, String>();
//        String clientIp = BaseApplication.getInstance().getClientIP();
//        if (TextUtils.isEmpty(clientIp)) {
//            throw new IllegalStateException("need a clientIp, but now is null");
//        }
//        headers.put("X-Ca-ClientIP", clientIp);// 客户机IP
//        sign = sign + clientIp;
//        String timeStamp = String.valueOf(System.currentTimeMillis());
//        headers.put("X-Ca-Timestamp", timeStamp);// 时间戳
//        sign = sign + timeStamp;
//        headers.put("X-Ca-AppKey", "101");// Android-101
//        sign = sign + "101";
//        String token;
//        if (mIsPostRequest) {
//            token = BaseApplication.getInstance().getToken();
//            if (TextUtils.isEmpty(token)) {
//                throw new IllegalStateException("need a token, but now is null");
//            }
//        } else {
//            token = UUID.randomUUID().toString().trim();
//        }
//        headers.put("X-Ca-Token", token);// 接口访问令牌
//        sign = sign + token;
//        String sessionId = BaseApplication.getInstance().getSessionID();
//        if (TextUtils.isEmpty(sessionId)) {
//            sessionId = UUID.randomUUID().toString().trim();
//        }
//        headers.put("X-Ca-SessionID", sessionId);// 会话凭证
//        sign = sign + sessionId;
//        headers.put("X-Ca-Version", "1.0");// 接口版本号
//        sign = sign + "1.0";
//        sign = sign + MD5Utils.getMd5Value(mSignBody);
//        Log.e("signature before HmacSHA1", sign);
//        String signature = "";
//        try {
//            signature = Hex.encodeHexStr(HmacSHA1Utils.getHmacSHA1(sign));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        headers.put("X-Ca-Signature", signature);// 签名，用于报文防篡改
//        Log.e("request headers", headers.toString());
//        return headers;
//    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        String sign = mRelativeUrl;
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Ca-Version", "1.0.0");// 接口版本号
        sign = sign + "1.0.0";
        headers.put("X-Ca-AppKey", "TeacherAndroid");// AppKey
        sign = sign + "TeacherAndroid";
        if (mIsPostRequest) {
            String nonce = UUID.randomUUID().toString().trim();
            headers.put("X-Ca-Nonce", nonce);// UUID
            sign = sign + nonce;
            String timeStamp = String.valueOf(System.currentTimeMillis());
            headers.put("X-Ca-Timestamp", timeStamp);// 时间戳
            sign = sign + timeStamp;
        } else { //必传字段, 不需要的场景可传"", 但不能不传;
            headers.put("X-Ca-Nonce", "");// UUID
            headers.put("X-Ca-Timestamp", "");// 时间戳
        }
        sign = sign + mSignBody;// bodyStr
        Log.d(TAG, "signature before HmacSHA1 :" + sign);
        String signature = "";
        try {
//			signature = Hex.encodeHexStr(HmacSHA1Utils.getHmacSHA1(sign));
            signature = Base64.encodeToString(HmacSHA1Utils.hwGetHmacSHA1(sign, HmacSHA1Utils.APPSecret), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        headers.put("X-Ca-Signature", signature);// 签名，用于报文防篡改
        headers.put("Content-Type", "application/json");
        addSessionCookie(headers);
        Log.d(TAG, "request headers：" + headers.toString());
        return headers;
    }

    public byte[] getBody() throws AuthFailureError {
        return mSignBody == null ? super.getBody() : mSignBody.getBytes();
    }

    /**
     * @param request_url 请求地址
     * @param jsonObject  jsonObject请求体
     * @return
     */
    public static String getUrl(String request_url, JSONObject jsonObject) {
        relative_url = request_url.replace(Consts.SERVER_IP, "");

        if (jsonObject != null) {
            String jsonString = jsonObject.toString();
            Log.d(TAG, "param：" + jsonString);
            try {
//				jsonString = Base64.encodeToString(
//						jsonString.getBytes("utf-8"), Base64.NO_WRAP);
                // NO_WRAP 这个参数意思是略去所有的换行符
//                Log.e("param base64", jsonString);
                sign_body = jsonString;
            } catch (Exception e) {
                e.printStackTrace();
            }
//            request_url = request_url + "?" + jsonString;
            Log.d(TAG, "request_url：" + request_url);
        } else {
            sign_body = "Single spark can start a prairie fire.";
            Log.d(TAG, "request_url：" + request_url);
        }
        Log.d(TAG, "body：" + sign_body);
        return request_url;
    }

    /**
     * @param request_url 请求地址
     * @param jsonArray   jsonArray请求体
     * @return
     */
    public static String getUrlForArray(String request_url, JSONArray jsonArray) {
        relative_url = request_url.replace(Consts.SERVER_IP, "");

        if (jsonArray != null) {
            String jsonString = jsonArray.toString();
            Log.d(TAG, "param：" + jsonString);
            try {
//				jsonString = Base64.encodeToString(
//						jsonString.getBytes("utf-8"), Base64.NO_WRAP);
                // NO_WRAP 这个参数意思是略去所有的换行符
//                Log.e("param base64", jsonString);
                sign_body = jsonString;
            } catch (Exception e) {
                e.printStackTrace();
            }
//            request_url = request_url + "?" + jsonString;
            Log.d(TAG, "request_url：" + request_url);
        } else {
            sign_body = "Single spark can start a prairie fire.";
            Log.d(TAG, "request_url：" + request_url);
        }
        Log.d(TAG, "body：" + sign_body);
        return request_url;
    }

    public static String getRelativeUrl() {
        return relative_url;
    }

    public static String getSignBody() {
        return sign_body;
    }

    public static String getUrlDiscarded(String request_url,
                                         JSONObject jsonObject, Boolean isNeedSessionId) {
        String jsonString = "";
        try {
            String clientIp = BaseApplication.getInstance().getClientIP();
            if (TextUtils.isEmpty(clientIp)) {
                throw new IllegalStateException(
                        "need a clientIp, but now is null");
            }
            jsonObject.put("clientIP", clientIp);
            if (isNeedSessionId) {
                String sessionId = BaseApplication.getInstance().getSessionID();
                if (TextUtils.isEmpty(sessionId)) {
                    throw new IllegalStateException(
                            "need a sessionId, but now is null");
                }
                jsonObject.put("sessionID", sessionId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            jsonString = jsonObject.toString();
            Log.d("param", jsonString);
            try {
                jsonString = Base64
                        .encodeToString(
                                RSAUtils.encryptByPublicKey(
                                        jsonString.getBytes("utf-8"),
                                        RSAUtils.PUB_KEY), Base64.NO_WRAP);
                // NO_WRAP 这个参数意思是略去所有的换行符
                Log.d("param rsa,base64", jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            request_url = request_url + "?" + jsonString;
            Log.e("request_url", request_url);
        } else {
            Log.e("request_url", request_url);
        }
        return request_url;
    }
}
