package com.homework.teacher.teacher.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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
import com.homework.teacher.teacher.AddPaperActivity;
import com.homework.teacher.utils.SpUtils;
import com.homework.teacher.utils.StatusUtils;
import com.multilevel.treelist.Dept;
import com.multilevel.treelist.Node;
import com.multilevel.treelist.NodeHelper;
import com.multilevel.treelist.NodeTreeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.homework.teacher.Consts.PAPER;

/**
 * Created by xing
 * on 2019/5/20 试卷
 */
public class PaperFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = PaperFragment.class.getSimpleName();
    private LinkedList<Node> mLinkedList = new LinkedList<>();
    private List<Node> data = new ArrayList<>();
    private NodeTreeAdapter mAdapter;


    @BindView(R.id.paper_list)
    ListView mPaperList;
    @BindView(R.id.add_paper)
    Button mAddPaper;
    @BindView(R.id.confirm)
    Button mConfirm;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paper, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddPaper.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        initData();
        mAdapter = new NodeTreeAdapter(getActivity(), mPaperList, mLinkedList);
        mPaperList.setAdapter(mAdapter);
    }

    private void initData() {
        JSONObject jsonObject = new JSONObject();
        int[] ints = new int[]{PAPER};
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
                            addOne(mediumCatalog.getData());
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

    private void addOne(List<MediumCatalog.DataBean> dataBeanList) {
        for (int i = 0; i < dataBeanList.size(); i++) {
            data.add(new Dept(dataBeanList.get(i).getId(), dataBeanList.get(i).getPid(), dataBeanList.get(i).getName()));
        }
        mLinkedList.addAll(NodeHelper.sortNodes(data));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_paper:
                startActivity(new Intent(getActivity(), AddPaperActivity.class));
                break;
            case R.id.confirm:
                Intent intent = new Intent();
                List<Node> list = new LinkedList<>();
                int moduleID = 0;
                String title = "";
                int a = mAdapter.nodeLinkedList.size();
                for (int i = 0; i < a; i++) {
                    if (mAdapter.nodeLinkedList.get(i).isCheck) {
                        moduleID = (int) mAdapter.nodeLinkedList.get(i).get_id();
                        title = mAdapter.nodeLinkedList.get(i).get_label();
                        for (int j = 0; j < mAdapter.nodeLinkedList.get(i).get_childrenList().size(); j++) {
                            Node node = (Node)mAdapter.nodeLinkedList.get(i).get_childrenList().get(j);
                            if (node.isCheck) {
                                list.add(node);
                            }
                        }
                    }
                }
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("moduleID", moduleID);
                intent.putExtra("title", title);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                break;
        }
    }
}
