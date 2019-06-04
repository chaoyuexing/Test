package com.homework.teacher.teacher.answer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.Constants;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MediumAnswer;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.teacher.Adapter.AnswerAdapter;
import com.homework.teacher.utils.SpUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xing
 * on 2019/6/3
 */
public class AnswerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AnswerActivity.class.getSimpleName();

    @BindView(R.id.grade_name)
    TextView mGradeName;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.add_answer)
    TextView mAddAnswer;
    @BindView(R.id.answer_list)
    RecyclerView mAnswerList;

    private AnswerAdapter mAnswerAdapter;
    private List<MediumAnswer.DataBean> mMediumAnswerList = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        ButterKnife.bind(this);
        mContext = this;
        initToolbar();
        mAddAnswer.setOnClickListener(this);
        mGradeName.setText(SpUtils.get(this,Constants.SP_KEY_GRADE_NAME,"").toString());
        mAnswerList.setLayoutManager(new LinearLayoutManager(this));
        mAnswerAdapter = new AnswerAdapter(R.layout.item_answer,mMediumAnswerList);
        mAnswerList.setAdapter(mAnswerAdapter);
        initData();
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.arrow_left);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initData() {
        String url = Consts.SERVER_ANSWER_LIST;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MediumAnswer mediumAnswer = new MediumAnswer().getFromGson(response);
                        if (mediumAnswer != null && mediumAnswer.getCode().equals(Consts.REQUEST_SUCCEED)) {
                            mMediumAnswerList = mediumAnswer.getData();
                            mAnswerAdapter.setNewData(mMediumAnswerList);
                        } else {
                            Toast.showLong(mContext, mediumAnswer.getMessage());
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
        switch (v.getId()) {
            case R.id.add_answer:
                break;
        }
    }
}
