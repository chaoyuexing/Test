package com.homework.teacher.teacher.videoCurriculum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.activity.LoginActivity;
import com.homework.teacher.widget.AddVideoDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.tencent.liteav.demo.common.utils.OssUploadUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author：xing Data：2019/6/15
 * brief:
 **/
public class VideoHomeActivity extends AppCompatActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private static final String TAG = LoginActivity.class.getSimpleName();


    @BindView(R.id.chooseSubjectTv)
    TextView mChooseSubjectTv;
    @BindView(R.id.lessongroup_toolbar)
    Toolbar mLessongroupToolbar;
    @BindView(R.id.add_vedio)
    TextView mAddVedio;
    @BindView(R.id.home_no_job)
    TextView mHomeNoJob;
    @BindView(R.id.home_job_list)
    RecyclerView mHomeJobList;

    private InvokeParam invokeParam;
    private TakePhoto takePhoto;



    private Context mContext;
    private String businessLicenseFileName, businessLicenseBitmapUri;
    private OssUploadUtils mOssUploadUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_home);
        mContext = this;
        ButterKnife.bind(this);
        mAddVedio.setOnClickListener(v -> {
            new AddVideoDialog(this, R.style.Dialog, takePhoto).show();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }




    @Override
    public void takeSuccess(TResult result) {
        String compressPath = result.getImage().getOriginalPath() != null ? result.getImage().getOriginalPath() : result.getImage().getCompressPath();
        Bitmap bitmap = BitmapFactory.decodeFile(compressPath);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

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

}
