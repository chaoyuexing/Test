package com.homework.teacher.teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.InteractQue;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.teacher.answer.AnswerActivity;
import com.homework.teacher.teacher.videoCurriculum.VideoHomeActivity;
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
    private List<JobItemAdapter> mJobItemAdapters = new ArrayList<>();
    private static final int ADD_QUE_LENGTH = 5;

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
        String title = getParentName(new StringBuilder(),data.get_parent()) + data.get_label();
        holder.setText(R.id.job_item_name, title);
        RecyclerView recyclerView = holder.getView(R.id.job_item_list_view);
        recyclerView.setNestedScrollingEnabled(false);
        JobItemAdapter JobItemAdapter = new JobItemAdapter(mContext, R.layout.item_job_item, data.getDataBeanList());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(JobItemAdapter);
        mJobItemAdapters.add(JobItemAdapter);
        holder.addOnClickListener(R.id.add_que_interact);
        holder.addOnClickListener(R.id.add_total_result);
        holder.addOnClickListener(R.id.add_total_curriculum);
        this.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.add_que_interact:
                    Log.d("JobAdapter", "position :" + position);
                    addQue((Integer) datas.get(position).get_id(), position);
                    break;
                case R.id.add_total_result:
                    Intent answerIntent = new Intent();
                    answerIntent.setClass(mContext, AnswerActivity.class);
                    mContext.startActivity(answerIntent);
                    break;
                case R.id.add_total_curriculum:
                    Intent curriculumIntent = new Intent();
                    curriculumIntent.setClass(mContext, VideoHomeActivity.class);
                    mContext.startActivity(curriculumIntent);
                    break;
            }
        });
    }

    private void addQue(int interactID, final int position) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("interactID", interactID);
            jsonObject.put("moduleID", getModuleID());
            jsonObject.put("num", ADD_QUE_LENGTH);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WDStringRequest.getUrl(Consts.SERVER_INTERACT_QUE_ADD, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url, relative_url, sign_body, true, response -> {
            InteractQue data = new InteractQue().getFromGson(response);
            if (data != null) {
                if (data.getCode().equals(Consts.REQUEST_SUCCEED)) {
//                    Log.d("JobAdapter","position :"+ position);
                    this.datas.get(position).setDataBeanList(data.getData());
                    setNewData(datas);
//                    mJobItemAdapters.get(position).setNewData(data.getData());
                } else if (data.getMessage() != null) {
                    Toast.makeText(mContext, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, arg0 -> StatusUtils.handleError(arg0, mContext));
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }


    public String getParentName(StringBuilder parentName,Node node) {
        if (node.get_parent() != null) {
            parentName.insert(0,node.get_label());
            getParentName(parentName,node.get_parent());
        } else {
            parentName.insert(0,node.get_label());
        }
        return parentName.toString();
    }

}
