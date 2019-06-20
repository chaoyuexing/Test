package com.homework.teacher.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.widget.MyCommonDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 后厨直播
 *
 * @author zhangkc
 * @date 2017-8-8
 */
public class LiveFragment extends Fragment {
    private final static String TAG = "LiveFragment";
    private View view;
    private TextView mTitleTv, mSetTv, mVideoPlayTv;
    private ImageView mBgIv;
    private ImageLoader mImageLoader;
    private MyCommonDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.live_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Button) view.findViewById(R.id.back)).setVisibility(View.GONE);
        mTitleTv = (TextView) view.findViewById(R.id.title);
        mTitleTv.setText("后厨直播");
        mSetTv = (TextView) view.findViewById(R.id.tvSet);
        mSetTv.setVisibility(View.GONE);

        mBgIv = (ImageView) view.findViewById(R.id.bgIv);
        mImageLoader = BaseApplication.getInstance().imageLoader;
        mImageLoader.displayImage(Consts.IMAGE_LIVE_BG_URL, mBgIv);

        mVideoPlayTv = (TextView) view.findViewById(R.id.videoPlayTv);
        mVideoPlayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
//                intent.setClass(getActivity(), NEVideoPlayerActivity.class);

                if (isWifi(getActivity())) {
                    startActivity(intent);
                } else {
                    dialog = new MyCommonDialog(getActivity(), "提示消息",
                            "非wifi环境，您确认继续播放吗？", "取消", "确定");
                    dialog.setOkListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            startActivity(intent);
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.setCancelListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
