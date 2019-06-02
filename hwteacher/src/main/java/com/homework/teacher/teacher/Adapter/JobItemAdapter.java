package com.homework.teacher.teacher.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.InteractQue;
import com.homework.teacher.data.MediumAnswer;
import com.homework.teacher.data.Simple;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.utils.StatusUtils;
import com.homework.teacher.utils.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xing
 * on 2019/5/7
 */
public class JobItemAdapter extends BaseQuickAdapter<InteractQue.DataBean, BaseViewHolder> {

    private Context mContext;

    public JobItemAdapter(Context context, int layoutId, List<InteractQue.DataBean> datas) {
        super(layoutId, datas);
        this.mContext = context;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder holder, InteractQue.DataBean problems) {
        holder.setIsRecyclable(false);
        holder.setText(R.id.item_job_item_sort, problems.getQueName() + ":");
        TextView answerTv = holder.getView(R.id.answer_tv);
        if (problems.getAnswerNum() > 0) {
            answerTv.setTextColor(R.color.text_color_blue);
        } else {
            answerTv.setTextColor(R.color.common_tab_text_color);
        }
        TextView curriculum_Tv = holder.getView(R.id.curriculum_tv);
        if (problems.getVcNum() > 0) {
            curriculum_Tv.setTextColor(R.color.text_color_blue);
        } else {
            curriculum_Tv.setTextColor(R.color.common_tab_text_color);
        }
        holder.setText(R.id.answer_tv, "答案  " + problems.getAnswerNum());
        holder.setText(R.id.curriculum_tv, "微课  " + problems.getVcNum());
        holder.addOnClickListener(R.id.add_and_remove);
        holder.addOnClickListener(R.id.answer_tv);
        holder.addOnClickListener(R.id.curriculum_tv);
        this.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.add_and_remove:
                        CheckBox checkBox = (CheckBox) view;
                        if (checkBox.isChecked()) {
                            hiddenInteract(getData().get(position).getId(), 0, checkBox);
                        } else {
                            hiddenInteract(getData().get(position).getId(), 1, checkBox);
                        }
                        break;
                    case R.id.answer_tv:
                        addAnswer(getData().get(position).getId());
                        break;
                    case R.id.curriculum_tv:
                        break;
                }

            }
        });
    }


    private void hiddenInteract(int iqID, final int delFlag, final CheckBox checkBox) {
        String url = Consts.SERVER_INTERACT_QUE + iqID + "/" + delFlag;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        final WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Simple data = new Simple().getFromGson(response);
                        if (data != null && data.getCode() == Consts.REQUEST_SUCCEED_CODE) {
                            checkBox.setChecked(delFlag == 0);
                        } else {
                            Log.e(TAG, "onResponse: " + data.message);
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


    private void addAnswer(final int catalogID) {
        final EditText et = new EditText(this.mContext);
        new AlertDialog.Builder(this.mContext).setTitle("请输入答案名称")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addAnswer(catalogID,et.getText().toString(),1);
                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     *
     * @param catalogID
     * @param content
     * @param type 答案类型，1：文本，2：图片
     */
    private void addAnswer(int catalogID,String content,int type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("catalogID", catalogID);
            jsonObject.put("content", content);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WDStringRequest.getUrl(Consts.SERVER_ADD_ANSWER, jsonObject);
        String relative_url = WDStringRequest.getRelativeUrl();
        String sign_body = WDStringRequest.getSignBody();
        WDStringRequest mRequest = new WDStringRequest(Request.Method.POST, url, relative_url, sign_body, true, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MediumAnswer data = new MediumAnswer().getFromGson(response);
                if (data.getCode() == Consts.REQUEST_SUCCEED) {
                    Toast.showLong(mContext, "添加成功");
                }else {
                    Toast.showLong(mContext, data.getMessage());
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

}