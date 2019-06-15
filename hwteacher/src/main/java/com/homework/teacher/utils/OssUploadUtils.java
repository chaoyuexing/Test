package com.homework.teacher.utils;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.homework.teacher.app.BaseApplication;

public class OssUploadUtils {

    private static final String TAG = "OssUploadUtils";


    private OssBackRequestListener ossListener;

    public interface OssBackRequestListener {
        void onProgress(PutObjectRequest request, long currentSize, long totalSize);

        void onSuccess(PutObjectRequest request, PutObjectResult result);

        void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException);
    }

    public OssUploadUtils(OssBackRequestListener ossListener) {
        this.ossListener = ossListener;
    }

    /**
     * @param objectKey 文件在服务器上的路径 头像 avatars，身份证 idcards，房产证 houseids，广告 ads，
     *                  小区 communitys，银行 banks，企业 companys，问题反馈 feedbacks
     *                  人脸采集 faces/collect/yyyyMMdd/图片，人脸捕捉 faces/catch/yyyyMMdd/图片
     * @param filePath  文件在本地的路径
     * @param name
     */
    public void getPhoto(String bucketName, String objectKey, String filePath, String name) {
        // 构造上传请求
        Log.d(TAG, "url: " + filePath);
        Log.d(TAG, TimeUtil.getNowTime("yyyyMMdd"));
        PutObjectRequest put;
//            put = new PutObjectRequest(bucketName, objectKey + TimeUtil.getNowTime("yyyyMMdd") + "/" + name
//                    , filePath);
            put = new PutObjectRequest(bucketName, objectKey, filePath);
//        }

        // 异步上传时可以设置进度回调
        put.setCRC64(OSSRequest.CRC64Config.YES);

        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                ossListener.onProgress(request, currentSize, totalSize);
            }
        });
        OSSAsyncTask<PutObjectResult> task = BaseApplication.getOssInstance().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                request.getObjectKey();
                ossListener.onSuccess(request, result);

            }

            /**
             *
             * @param request
             * @param clientExcepion        ClientException指SDK内部出现的异常，比如参数错误，网络无法到达，主动取消等等。
             * @param serviceException      服务器端错误，它来自于对服务器错误信息的解析
             */
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                Log.i(TAG, "onFailure: " + request.toString());
                Log.i(TAG, "onFailure--ObjectKey: " + request.getObjectKey());
                Log.i(TAG, "onFailure: " + request.toString());

                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e(TAG, "ErrorCode" + serviceException.getErrorCode());
                    Log.e(TAG, "RequestId" + serviceException.getRequestId());
                    Log.e(TAG, "HostId" + serviceException.getHostId());
                    Log.e(TAG, "RawMessage" + serviceException.getRawMessage());
                }
                ossListener.onFailure(request, clientExcepion, serviceException);
            }
        });
    }
}
