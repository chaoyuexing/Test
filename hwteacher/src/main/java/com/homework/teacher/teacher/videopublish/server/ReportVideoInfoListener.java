package com.homework.teacher.teacher.videopublish.server;

/**
 * Created by vinsonswang on 2018/4/8.
 */

public interface ReportVideoInfoListener {
    void onFail(int errCode);

    void onSuccess();
}
