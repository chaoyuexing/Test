package com.homework.teacher.teacher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.homework.teacher.teacher.Adapter.addPaperAdapter;
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
 * on 2019/5/21 添加试卷2级目录
 */
public class AddPaper2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AddPaper2Activity.class.getSimpleName();
    private static final int START_ADD_LOWER_PAPER = 1001;

    @BindView(R.id.back)
    Button mBack;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.paper_list_two)
    RecyclerView mPaperListTwo;
    private LinkedList<Node> mLinkedList = new LinkedList<>();
    private List<Node> data = new ArrayList<>();

    private String name;
    private int id;
    private Context mContext;


    private addPaperAdapter mPaperAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_paper);
        ButterKnife.bind(this);
        name = getIntent().getStringExtra("paperName");
        id = getIntent().getIntExtra("paperID", 0);
        mContext = this;
        mPaperListTwo.setLayoutManager(new LinearLayoutManager(this));
        mPaperAdapter = new addPaperAdapter(R.layout.item_paper, mLinkedList, id);
        mPaperListTwo.setAdapter(mPaperAdapter);
        mName.setText(name);
        mBack.setOnClickListener(this);
        mName.setOnClickListener(this);

        mPaperAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.name);
                Intent intent = new Intent();
                intent.putExtra("name", textView.getText().toString());
                startActivity(intent);
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
                addLowenPaper(id);
//                addItem(id);
                break;
        }
    }

    private void addLowenPaper(int paperID) {
        Intent intent = new Intent(mContext, AddLowerPaperActivity.class);
        intent.putExtra("paperID", paperID);
        startActivityForResult(intent, START_ADD_LOWER_PAPER);
    }

    private void addItem(final int id) {
        final EditText tilteEditText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入下级目录名称")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(tilteEditText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String titleName = tilteEditText.getText().toString();
                        if (!titleName.equals("")) {
                            addSub(titleName, id);
                        } else {
                            com.homework.teacher.utils.Toast.showLong(AddPaper2Activity.this, "下级目录不能为空");
                        }
                    }
                }).setNegativeButton("取消", null).show();

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
                    Toast.showLong(mContext, "添加成功");
                    getPaperList();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0,
                        mContext);
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void getPaperList() {
        String url = Consts.SERVER_MEDIUM_CATALOG + id;
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

    private void notifyListData(List<MediumCatalog.DataBean> dataBeanList) {
        for (int i = 0; i < dataBeanList.size(); i++) {
            data.add(new Dept(dataBeanList.get(i).getId(), dataBeanList.get(i).getPid(), dataBeanList.get(i).getName()));
        }
        mLinkedList.addAll(NodeHelper.sortNodes(data));
        mPaperAdapter.notifyDataSetChanged();
    }
}
