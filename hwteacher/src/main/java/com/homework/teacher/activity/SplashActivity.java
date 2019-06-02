package com.homework.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;

/**
 * @author zhangkc
 * @date 2017-10-30
 */
public class SplashActivity extends Activity {

    private final static String TAG = SplashActivity.class.getName();
    private static final long SPLASH_DELAY_MILLIS = 1000;// 延迟1秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 使用Handler的postDelayed方法，1秒后执行跳转到SplashActivity
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goHome();
            }
        }, SPLASH_DELAY_MILLIS);
    }

    private void goHome() {
        int firstInstall = BaseApplication.getInstance().getFirstInstall();
        Intent intent = new Intent();
//        if (firstInstall == 0) {
//            intent.setClass(this, UserGuideActivity.class);
//            intent.putExtra(UserGuideActivity.FROM, 0);
//        } else {
            intent = new Intent(SplashActivity.this, LoginActivity1.class);//为避免调试 每次登录 暂时注释 直接跳转MainActivity
//            intent = new Intent(SplashActivity.this, MainActivity.class);
//        }
        startActivity(intent);
        finish();
    }

}