package com.tencent.liteav.demo.common.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

public class OssUploadUtils {

    private static final String TAG = "OssUploadUtils";
    private static OSS oss = null;

    private static final String OSS_BUKET_NAME = "homework-test";
    private OssBackRequestListener ossListener;



    public static void getOssInstance(Context mInstance) {
        if (oss == null) {
            String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider("LTAI9KQTGihkambl", "wl4rJcKckND1PfzFRBaLhHjgQRurX4", "");
            //该配置类如果不设置，会有默认配置，具体可看该类
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
//        OSSLog.disableLog();
            oss = new OSSClient(mInstance, endpoint, credentialProvider);
        }
    }

    public OssUploadUtils(OssBackRequestListener ossListener) {
        this.ossListener = ossListener;
    }

    /**
     * @param filePath  文件在本地的路径
     * @param name
     */
    public void getPhoto( String objectKey, String filePath, String name) {
        // 构造上传请求
        Log.d(TAG, "url: " + filePath);
        Log.d(TAG, TimeUtil.getNowTime("yyyyMMdd"));
        name = FileUtils.UUIDFileName(name);
        PutObjectRequest put = new PutObjectRequest(OSS_BUKET_NAME, objectKey + "/"+TimeUtil.getNowTime("yyyyMMdd") + "/" + name
                , filePath);

        // 异步上传时可以设置进度回调
        put.setCRC64(OSSRequest.CRC64Config.YES);

        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                ossListener.onProgress(request.getObjectKey(), currentSize, totalSize);
            }
        });
        OSSAsyncTask<PutObjectResult> task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                request.getObjectKey();
                ossListener.onSuccess(request.getObjectKey(), result.getStatusCode());

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
                ossListener.onFailure(request.getObjectKey(), clientExcepion.getMessage(), serviceException.getMessage());
            }
        });
    }



    public interface OssBackRequestListener {
        void onProgress(String requestKy, long currentSize, long totalSize);

        void onSuccess(String  requestKy, int resultStatusCode);

        void onFailure(String request, String clientExcepion, String serviceException);
    }



}
