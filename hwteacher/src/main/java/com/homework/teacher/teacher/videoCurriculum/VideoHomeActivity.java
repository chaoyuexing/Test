package com.homework.teacher.teacher.videoCurriculum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.Constants;
import com.android.volley.Request;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.activity.LoginActivity;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MediumAnswer;
import com.homework.teacher.data.MediumVideoCourseBean;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.teacher.videoCurriculum.adapter.VideoHomeAdapter;
import com.homework.teacher.teacher.videopublish.TCCompressActivity;
import com.homework.teacher.utils.FileUtils;
import com.homework.teacher.utils.SpUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.Toast;
import com.homework.teacher.widget.AddVideoDialog;
import com.tencent.liteav.demo.common.utils.TCConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author：xing Data：2019/6/15
 * brief:
 **/
public class VideoHomeActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_VIDEO_CODE = 10081;
    private static final int UPLOAD_VIDEO = 10082;

    @BindView(R.id.add_vedio)
    TextView mAddVedio;
    @BindView(R.id.home_no_job)
    TextView mHomeNoJob;
    @BindView(R.id.home_job_list)
    RecyclerView mHomeJobList;
    @BindView(R.id.video_home_subject_tv)
    TextView mVideoHomeSubjectTv;
    @BindView(R.id.video_home_toolbar)
    Toolbar mVideoHomeToolbar;
    @BindView(R.id.video_home_list)
    RecyclerView mVideoHomeList;

    private Context mContext;
    private int catalogID;
    private int knowledgeID;
    private String type = "";

    private List<MediumVideoCourseBean.DataBean> mDataBean;
    private VideoHomeAdapter mVideoHomeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_home);
        mContext = this;
        ButterKnife.bind(this);
        catalogID = getIntent().getIntExtra("catalogID", 0);
        knowledgeID = getIntent().getIntExtra("knowledgeID", 0);
        mVideoHomeList.setLayoutManager(new LinearLayoutManager(this));
        mVideoHomeAdapter = new VideoHomeAdapter(R.layout.item_answer, mDataBean, this);
        mVideoHomeList.setAdapter(mVideoHomeAdapter);
        initToolbar();
        mAddVedio.setOnClickListener(v -> {
            new AddVideoDialog(this, R.style.Dialog, (dialog, type) -> {

                if (type.equals("SELECT_PHOTO")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("video/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, REQUEST_VIDEO_CODE);
                } else {

                }
            }).show();
        });
        initData();
    }

    private void initData() {
        String url = Consts.SERVER_MEDIUM_VIDEO_COURSE_LIST + catalogID;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url, relative_url, sign_body, false, response -> {
            MediumVideoCourseBean mediumVideoCourseBean = new MediumVideoCourseBean().getFromGson(response);
            if (mediumVideoCourseBean != null) {
                if (mediumVideoCourseBean.getCode().equals(Consts.REQUEST_SUCCEED)) {
                    mDataBean = mediumVideoCourseBean.getData();
                    mVideoHomeAdapter.setNewData(mDataBean);
//                    mMediumAnswerList = mediumAnswer.getData();
//                    mAnswerAdapter.setNewData(mMediumAnswerList);
                } else {
                    Toast.showLong(mContext, mediumVideoCourseBean.getMessage());
                }
            }
        }, arg0 -> StatusUtils.handleError(arg0, mContext));
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }


    /**
     * 添加微课
     *
     * @param videoUrl 微课URL
     */
    private void addVedioCourse(String videoUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("catalogID", catalogID);
            jsonObject.put("knowledgeID", knowledgeID);
            jsonObject.put("type", type);
            jsonObject.put(" url", videoUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WDStringRequest.getUrl(Consts.SERVER_WORK_ADD_VIDEO_COURSE, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url, relative_url, sign_body, true, response -> {
            MediumAnswer data = new MediumAnswer().getFromGson(response);
            if (data.getCode().equals(Consts.REQUEST_SUCCEED)) {
                Toast.showLong(mContext, "添加成功");
                finish();
            } else {
                Toast.showLong(mContext, data.getMessage());
            }
        }, arg0 -> StatusUtils.handleError(arg0, mContext));
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);

    }

    private void initToolbar() {
        mVideoHomeToolbar.setNavigationIcon(R.mipmap.common_ic_back);
        mVideoHomeToolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String gradeSubjectName = SpUtils.get(mContext, Constants.SP_KEY_GRADE_NAME, "").toString();
        if (gradeSubjectName != null && !gradeSubjectName.equals("")) {
            mVideoHomeSubjectTv.setText(gradeSubjectName + "备课组");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                File file = FileUtils.uriToFile(uri, mContext);
                Log.d(TAG, "uri：" + uri);
                String mVideoPath = file.getAbsolutePath();
                if (mVideoPath != null) {
                    Log.d(TAG, "mVideoPath :" + file.getAbsolutePath());
                    Intent intent = new Intent();
                    intent.setClass(this, TCCompressActivity.class);
                    intent.putExtra(TCConstants.VIDEO_EDITER_PATH, mVideoPath);
                    startActivityForResult(intent, UPLOAD_VIDEO);
                }
            }
        } else if (requestCode == UPLOAD_VIDEO) {
            String videoUrl = data.getStringExtra("videoUrl");
            addVedioCourse(videoUrl);
            Log.d(TAG, "videoUrl：" + videoUrl);
        } else {

        }
    }


}
