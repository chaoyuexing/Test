package com.homework.teacher.teacher.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.InteractQue;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.multilevel.treelist.Node;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xing
 * on 2019/5/1
 */
public class JobAdapter extends BaseQuickAdapter<Node, BaseViewHolder> {

    private Context mContext;
    private String title;
    private List<Node> datas;
    private int moduleID;
    private List<InteractQue.DataBean> mDataBeanList = new ArrayList<>();
    private List<JobItemAdapter> mJobItemAdapters = new ArrayList<>();

    public JobAdapter(Context context, int layoutId, List<Node> datas) {
        super(layoutId, datas);
        mContext = context;
        this.datas = datas;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    @Override
    protected void convert(BaseViewHolder holder, final Node data) {
        holder.setIsRecyclable(false);
        holder.setText(R.id.job_item_name, getTitle()+data.get_label());
        RecyclerView recyclerView = holder.getView(R.id.job_item_list_view);
        JobItemAdapter JobItemAdapter = new JobItemAdapter(mContext, R.layout.item_job_item, mDataBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(JobItemAdapter);
        mJobItemAdapters.add(JobItemAdapter);
        holder.addOnClickListener(R.id.add_que_interact);
        this.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.add_que_interact:
                        datas.get(position).setCurrMaxQueNum(datas.get(position).getCurrMaxQueNum() + 5);
                        addQue(datas.get(position).getCurrMaxQueNum(), (Integer) datas.get(position).get_id(), position);
                        break;
                }
            }
        });
    }

    private void addQue(int currMaxQueNum, int interactID, final int position) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("currMaxQueNum", currMaxQueNum);
            jsonObject.put("interactID", interactID);
            jsonObject.put("moduleID", getModuleID());
            jsonObject.put("num", 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WDStringRequest.getUrl(Consts.SERVER_INTERACT_QUE_ADD, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url,
                relative_url, sign_body, true, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                InteractQue data = new InteractQue().getFromGson(response);
                if (data != null && data.getCode().equals(Consts.REQUEST_SUCCEED)) {
                    mJobItemAdapters.get(position).setNewData(data.getData());
                } else {
                    Toast.makeText(mContext, data.getMessage(), Toast.LENGTH_SHORT).show();
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

}
