package com.homework.teacher.teacher.answer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MediumAnswer;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xing
 * on 2019/6/8 添加文字答案页面
 */
public class AddAnswerActivity extends AppCompatActivity {

    private static final String TAG = AddAnswerActivity.class.getSimpleName();

    @BindView(R.id.back)
    Button mBack;
    @BindView(R.id.select_team_group)
    Button mSelectTeamGroup;
    @BindView(R.id.answer_et)
    EditText mAnswerEt;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        mContext = this;
        ButterKnife.bind(this);
    }

    /**
     * @param catalogID
     * @param content
     * @param type      答案类型，1：文本，2：图片
     */
    private void addAnswer(int catalogID, String content, int type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("catalogID", catalogID);
            jsonObject.put("content", content);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WDStringRequest.getUrl(Consts.SERVER_ADD_ANSWER, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url, relative_url, sign_body, true, response -> {
            MediumAnswer data = new MediumAnswer().getFromGson(response);
            if (data.getCode() == Consts.REQUEST_SUCCEED) {
                Toast.showLong(mContext, "添加成功");
            } else {
                Toast.showLong(mContext, data.getMessage());
            }
        }, arg0 -> StatusUtils.handleError(arg0, mContext));
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

}
