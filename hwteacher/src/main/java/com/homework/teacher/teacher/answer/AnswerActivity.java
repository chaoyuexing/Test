package com.homework.teacher.teacher.answer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Constants;
import com.android.volley.Request;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MediumAnswer;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.teacher.Adapter.AnswerAdapter;
import com.homework.teacher.utils.ImageUtils;
import com.homework.teacher.utils.SpUtils;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.Toast;
import com.homework.teacher.widget.AddAnswerDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.tencent.liteav.demo.common.utils.OssUploadUtils;
import com.tencent.liteav.demo.common.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xing
 * on 2019/6/3
 */
public class AnswerActivity extends AppCompatActivity implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {

    private static final String TAG = AnswerActivity.class.getSimpleName();

    @BindView(R.id.grade_name)
    TextView mGradeName;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.add_answer)
    TextView mAddAnswer;
    @BindView(R.id.answer_list)
    RecyclerView mAnswerList;
    @BindView(R.id.answer_image)
    ImageView mImageView;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;

    private AnswerAdapter mAnswerAdapter;
    private List<MediumAnswer.DataBean> mMediumAnswerList = new ArrayList<>();
    private Context mContext;
    private String businessLicenseFileName, businessLicenseBitmapUri;
    private OssUploadUtils mOssUploadUtils;
    private int catalogID = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        ButterKnife.bind(this);
        mContext = this;
        catalogID = getIntent().getIntExtra("catalogID",0);
        initToolbar();
        mAddAnswer.setOnClickListener(this);
        mGradeName.setText(SpUtils.get(this, Constants.SP_KEY_GRADE_NAME, "").toString());
        mAnswerList.setLayoutManager(new LinearLayoutManager(this));
        mAnswerAdapter = new AnswerAdapter(R.layout.item_answer, mMediumAnswerList, this);
        mAnswerList.setAdapter(mAnswerAdapter);
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.mipmap.common_ic_back);
        mToolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        String url = Consts.SERVER_ANSWER_LIST+catalogID;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url, relative_url, sign_body, false, response -> {
                    MediumAnswer mediumAnswer = new MediumAnswer().getFromGson(response);
                    if (mediumAnswer != null) {
                        if (mediumAnswer.getCode().equals(Consts.REQUEST_SUCCEED)) {
                            mMediumAnswerList = mediumAnswer.getData();
                            mAnswerAdapter.setNewData(mMediumAnswerList);
                        } else {
                            Toast.showLong(mContext, mediumAnswer.getMessage());
                        }
                    }
                }, arg0 -> StatusUtils.handleError(arg0, mContext));
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_answer:
                addAnswer();
                break;
        }
    }

    private void addAnswer() {
        new AddAnswerDialog(this, R.style.Dialog, takePhoto,catalogID).show();
    }


    @Override
    public void takeSuccess(TResult result) {
        String compressPath = result.getImage().getOriginalPath() != null ? result.getImage().getOriginalPath() : result.getImage().getCompressPath();
        Bitmap bitmap = BitmapFactory.decodeFile(compressPath);
        businessLicenseFileName = "faceture_" + TimeUtil.getNowTime() + "_wisdomlife.jpg";
        businessLicenseBitmapUri = ImageUtils.saveBitmapUri(bitmap, businessLicenseFileName);
        updateFactPhoto();
        mOssUploadUtils.getPhoto("answer", businessLicenseBitmapUri, businessLicenseFileName);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    private void updateFactPhoto() {
        mOssUploadUtils = new OssUploadUtils(new OssUploadUtils.OssBackRequestListener() {

            @Override
            public void onProgress(String requestKy, long currentSize, long totalSize) {}

            @Override
            public void onSuccess(String requestKy, int resultStatusCode) {

            }
            @Override
            public void onFailure(String request, String clientExcepion, String serviceException) {

            }
        });
    }
}
