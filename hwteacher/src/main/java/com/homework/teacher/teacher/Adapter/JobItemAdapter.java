package com.homework.teacher.teacher.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Request;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.InteractQue;
import com.homework.teacher.data.Simple;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.teacher.answer.AnswerActivity;
import com.homework.teacher.teacher.videoCurriculum.VideoHomeActivity;
import com.homework.teacher.utils.StatusUtils;

import java.util.List;

/**
 * Created by xing
 * on 2019/5/7
 */
public class JobItemAdapter extends BaseQuickAdapter<InteractQue.DataBean, BaseViewHolder> {


    private static final int delFlag = 0;  //删除标识位，0：未删除，1：已删除

    private Context mContext;
    private List<InteractQue.DataBean> datas;

    public JobItemAdapter(Context context, int layoutId, List<InteractQue.DataBean> datas) {
        super(layoutId, datas);
        this.mContext = context;
        this.datas = datas;
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
        holder.setChecked(R.id.add_and_remove,problems.getDelFlag() == delFlag);
        this.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.add_and_remove:
                    CheckBox checkBox = (CheckBox) view;
                    if (checkBox.isChecked()) {
                        hiddenInteract(position, 0, checkBox);
                    } else {
                        hiddenInteract(position, 1, checkBox);
                    }
                    break;
                case R.id.answer_tv:
                    Intent intent = new Intent(mContext, AnswerActivity.class);
                    intent.putExtra("catalogID", getData().get(position).getQueID());
                    mContext.startActivity(intent);
                    break;
                case R.id.curriculum_tv:
                    Intent tvIntent = new Intent(mContext, VideoHomeActivity.class);
                    tvIntent.putExtra("catalogID", getData().get(position).getQueID());
                    mContext.startActivity(tvIntent);
                    break;
            }

        });
    }


    private void hiddenInteract(int position, final int delFlag, final CheckBox checkBox) {
        String url = Consts.SERVER_INTERACT_QUE + datas.get(position).getId() + "/" + delFlag;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        final WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url, relative_url, sign_body, false, response -> {
            Simple data = new Simple().getFromGson(response);
            if (data != null) {
                if (data.getCode() == Consts.REQUEST_SUCCEED_CODE) {
                    datas.get(position).setDelFlag(delFlag);
                    checkBox.setChecked(delFlag == 0);
                } else {
                    Log.e(TAG, "onResponse: " + data.message);
                }
            }

        }, arg0 -> StatusUtils.handleError(arg0, mContext));
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }


//    private void addAnswer(final int catalogID) {
//        final EditText et = new EditText(this.mContext);
//        new AlertDialog.Builder(this.mContext).setTitle("请输入答案名称")
//                .setIcon(android.R.drawable.sym_def_app_icon)
//                .setView(et)
//                .setPositiveButton("确定", (dialogInterface, i) ->
//                        addAnswer(catalogID, et.getText().toString(), 1)).setNegativeButton("取消", null).show();
//    }


}
