package com.homework.teacher.teacher.videoCurriculum;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.activity.LoginActivity;
import com.homework.teacher.utils.CompareStringArrayUtil;
import com.tencent.liteav.demo.videorecord.TCVideoRecordActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author：xing Data：2019/6/15
 * brief:
 **/
public class VideoHomeActivity extends AppCompatActivity implements  EasyPermissions.PermissionCallbacks  {

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


    private String[] permissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int num = 123;//用于验证获取的权限

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_home);
        mContext = this;
        ButterKnife.bind(this);
        mAddVedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPerm();
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(num)
    private void checkPerm() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            startActivity(new Intent(mContext, TCVideoRecordActivity.class));
        } else {
            EasyPermissions.requestPermissions(this, "申请权限", num, permissions);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        String[] strings = new String[perms.size()];
        if (CompareStringArrayUtil.differenceSet(perms.toArray(strings), permissions).length == 0) {
        } else {
            Log.d(TAG, "没有获取到全部的权限");
            finish();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    }
}
