package com.homework.teacher.teacher.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Constants;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MediumCatalog;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.SpUtils;
import com.homework.teacher.utils.StatusUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xing
 * on 2019/5/26 其他Fragment
 */
public class RestsFragment extends Fragment {

    private static final String TAG = RestsFragment.class.getSimpleName();
    @BindView(R.id.job_name_et)
    EditText mJobNameEt;
    @BindView(R.id.paper_list)
    RecyclerView mPaperList;
    @BindView(R.id.confirm)
    Button mConfirm;
    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rests, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }


    private void initData() {
        JSONObject jsonObject = new JSONObject();
        int[] ints = new int[]{Consts.OTHER};
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < ints.length; i++) {
            jsonArray.put(ints[i]);
        }
        try {
            jsonObject.put("gsID", SpUtils.get(getActivity(),  Constants.SP_KEY_GRADEID, 0));// 备课组ID
            jsonObject.put("typeList", jsonArray);// 1：课本，2：习题册，3：试卷，4：其他
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WDStringRequest.getUrl(Consts.SERVER_catalog, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.PUT, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MediumCatalog mediumCatalog = new MediumCatalog().getFromGson(response);
                        if (mediumCatalog != null && mediumCatalog.getCode().equals(Consts.REQUEST_SUCCEED)) {
//                            addOne(mediumCatalog.getData());
                        } else {
                            Toast.makeText(getActivity(), mediumCatalog.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0, getActivity());
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
