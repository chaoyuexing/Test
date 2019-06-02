package com.homework.teacher.teacher;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.MediumCatalog;
import com.homework.teacher.data.Simple;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.teacher.Adapter.EditParperAdapter;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.Toast;
import com.multilevel.treelist.Dept;
import com.multilevel.treelist.Node;
import com.multilevel.treelist.NodeHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xing
 * on 2019/5/23 试卷添加下级目录
 */
public class AddLowerPaperActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AddLowerPaperActivity.class.getSimpleName();
    private static final int SELECT_NAME = 0;
    private static final int ADD_LOWER = 1;

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.edit_parper_list)
    RecyclerView mEditParperList;
    @BindView(R.id.add_peer_catalog)
    TextView mAddPeerCatalog;

    private Context mContext;
    private int paperID;
    private List<Node> data = new ArrayList<>();
    private LinkedList<Node> mLinkedList = new LinkedList<>();
    private EditParperAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lower_paper);
        ButterKnife.bind(this);
        paperID = getIntent().getIntExtra("paperID", 0);
        mBack.setOnClickListener(this);
        mName.setOnClickListener(this);
        mAddPeerCatalog.setOnClickListener(this);
        mContext = this;
        mEditParperList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EditParperAdapter(R.layout.item_lower_paper,mLinkedList);
        mEditParperList.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.paper_delete:
                        Log.d(TAG, "onItemChildClick: "+"paper_delete");
                        break;
                    case R.id.paper_edit:
                        Log.d(TAG, "onItemChildClick: "+"paper_edit");
                        break;
                    case R.id.paper_order:
                        Log.d(TAG, "onItemChildClick: "+"paper_order");
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.name:
                shouDialog("请输入新的名称", SELECT_NAME);
                break;
            case R.id.add_peer_catalog:
                shouDialog("请输入下级目录名称", ADD_LOWER);
                break;
        }
    }

    private void shouDialog(String text, final int type) {
        final EditText tilteEditText = new EditText(this);
        new AlertDialog.Builder(this).setTitle(text)
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(tilteEditText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String titleName = tilteEditText.getText().toString();
                        if (!titleName.equals("")) {
                            if (type == SELECT_NAME) {
                                updateName(titleName);
                            } else {
                                addSub(titleName,paperID);
                            }
                        } else {
                            com.homework.teacher.utils.Toast.showLong(mContext, "下级目录不能为空");
                        }
                    }
                }).setNegativeButton("取消", null).show();

    }


    private void updateName(String name) {
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("id", Void);// ID
            jsonObject.put("name", name); // 新的名称
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WDStringRequest.getUrl(Consts.SERVER_UPDATE_NAME, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.PUT, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0, mContext);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    /**
     * 添加下级目录
     *
     * @param titleName
     * @param id
     */
    private void addSub(String titleName, int id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", titleName);
            jsonObject.put("pid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WDStringRequest.getUrl(Consts.SERVER_CATALOG_ADD_SUB, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url, relative_url, sign_body, true, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Simple data = new Simple().getFromGson(response);
                if (data.getCode() == Consts.REQUEST_SUCCEED_CODE) {
                    getPaperList();
                    Toast.showLong(mContext, "添加成功");
                }else {
                    Toast.showLong(mContext, data.message);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0, mContext);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void getPaperList() {
        String url = Consts.SERVER_MEDIUM_CATALOG + paperID;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MediumCatalog mediumCatalog = new MediumCatalog().getFromGson(response);
                        if (mediumCatalog != null && mediumCatalog.getCode().equals(Consts.REQUEST_SUCCEED)) {
                            notifyListData(mediumCatalog.getData());
                        } else {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0, mContext);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    /**
     * 删除单个作业介质或目录
     * @param id 介质ID 或 目录ID
     */
    public void delecttPaper(int id) {
        String url = Consts.SERVER_LOGIC_DEL + id;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Simple simple = new Simple().getFromGson(response);
                        if (simple != null && String.valueOf(simple.getCode()).equals(Consts.REQUEST_SUCCEED)) {
                            Log.d(TAG, "onResponse: "+"删除成功");
                            getPaperList();
                        } else {
                            Log.d(TAG, "onResponse: "+simple.message);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0, mContext);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void sqpChangeParper(int upID,int downID) {
        String url = Consts.SERVER_CATALOG_SEQ_CHANGE + upID+"/"+downID;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Simple simple = new Simple().getFromGson(response);
                        if (simple != null && String.valueOf(simple.getCode()).equals(Consts.REQUEST_SUCCEED)) {
                            Log.d(TAG, "onResponse: "+"交换成功");
                            getPaperList();
                        } else {
                            Log.d(TAG, "onResponse: "+simple.message);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0, mContext);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void notifyListData(List<MediumCatalog.DataBean> dataBeanList) {
        for (int i = 0; i < dataBeanList.size(); i++) {
            if (dataBeanList.get(i) != null) {
                data.add(new Dept(dataBeanList.get(i).getId(), dataBeanList.get(i).getPid(), dataBeanList.get(i).getName()));
            }
        }
        data.add(new Dept(0, 1, "5454545"));
        mLinkedList.addAll(NodeHelper.sortNodes(data));
        mAdapter.notifyDataSetChanged();
    }
}
