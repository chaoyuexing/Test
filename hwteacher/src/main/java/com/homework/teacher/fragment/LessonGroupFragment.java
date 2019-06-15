package com.homework.teacher.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.Constants;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.homework.teacher.Consts;
import com.homework.teacher.R;
import com.homework.teacher.adapter.GradeSubjectAdapter;
import com.homework.teacher.app.BaseApplication;
import com.homework.teacher.data.GradeSubject;
import com.homework.teacher.data.HomeListJob;
import com.homework.teacher.http.WDStringRequest;
import com.homework.teacher.teacher.Adapter.HomeAdapter;
import com.homework.teacher.teacher.AddJobActivity;
import com.homework.teacher.utils.SpUtils;
import com.homework.teacher.utils.SpaceItemDecoration;
import com.homework.teacher.utils.StatusUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 备课组
 *
 * @author zhangkc
 */
public class LessonGroupFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "LessonGroupFragment";
    protected BaseApplication mApp;
    @BindView(R.id.chooseSubjectTv)
    TextView mChooseSubjectTv;
    @BindView(R.id.lessongroup_toolbar)
    Toolbar mLessongroupToolbar;
    @BindView(R.id.addHomeWorkLl)
    TextView mAddHomeWorkLl;
    @BindView(R.id.home_no_job)
    TextView mHomeNoJob;
    @BindView(R.id.home_job_list)
    RecyclerView mHomeJobList;

    private Unbinder unbinder;
    private PopupWindow chooseSubjectPop;
    private HomeAdapter mHomeAdapter;

    private int groupClassID;
    private String gradeSubjectName;
    private  List<GradeSubject.GradeSubjectData> mSubjectList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lessongroup_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        mAddHomeWorkLl.setOnClickListener(this);
        mChooseSubjectTv.setOnClickListener(this);
    }

    private void initToolbar() {
        mLessongroupToolbar.setNavigationIcon(R.drawable.navbar_ic_class);
        mLessongroupToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getTeacherGradeSubject() {
        String url = Consts.SERVER_teacherGradeSubject;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GradeSubject subjectClass = new GradeSubject().getFromGson(response);
                        if (subjectClass != null && subjectClass.code == Consts.REQUEST_SUCCEED_CODE) {
                            mSubjectList = subjectClass.data;
                            showSubject(mSubjectList);
                        } else {
                            Toast.makeText(getActivity(), subjectClass.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0,
                        getActivity());
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void getTeacherWorkSheet(int id) {
        String url = Consts.SERVER_workSheet+id;
        String relative_url = url.replace(Consts.SERVER_IP, "");
        String sign_body = "";
        WDStringRequest mRequest = new WDStringRequest(Request.Method.GET, url,
                relative_url, sign_body, false,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HomeListJob homeListJob = new HomeListJob().getFromGson(response);
                        if (homeListJob!= null && homeListJob.getCode() == Consts.REQUEST_SUCCEED_CODE) {
                            List<HomeListJob.HomeListJobData> data = homeListJob.getData();
                            shouHomeJobList(data);
                        } else  {
                            Toast.makeText(getActivity(), homeListJob.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                StatusUtils.handleError(arg0,
                        getActivity());
            }
        });
        BaseApplication.getInstance().addToRequestQueue(mRequest, TAG);
    }

    private void shouHomeJobList(List<HomeListJob.HomeListJobData> data ) {
        if (data.size() > 0) {
            mHomeJobList.setVisibility(View.VISIBLE);
            mHomeNoJob.setVisibility(View.GONE);
            mHomeJobList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mHomeJobList.addItemDecoration(new SpaceItemDecoration(60));
            mHomeAdapter = new HomeAdapter(getActivity(), R.layout.item_hoem_job, data);
            mHomeJobList.setAdapter(mHomeAdapter);
        } else {
            mHomeJobList.setVisibility(View.GONE);
            mHomeNoJob.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 备课组－选择备课组 展示顶部弹出框
     */
    private void showSubject(final List<GradeSubject.GradeSubjectData> list) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.pop_choose_subject, null);        // 将转换的View放置到 新建一个popuwindow对象中
        chooseSubjectPop = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView lv_choose_subject =  view.findViewById(R.id.lv_choose_subject);
        //设置PopupWindow弹出窗体可点击
        chooseSubjectPop.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        chooseSubjectPop.setAnimationStyle(R.style.TopDialog);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xffEEF0F3);
        //设置SelectPicPopupWindow弹出窗体的背景
        chooseSubjectPop.setBackgroundDrawable(dw);

        chooseSubjectPop.showAsDropDown(mChooseSubjectTv);

        final GradeSubjectAdapter gradeSubjectAdapter = new GradeSubjectAdapter(getActivity(), list);
        lv_choose_subject.setAdapter(gradeSubjectAdapter);
        lv_choose_subject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                groupClassID = list.get(position).getId();
                gradeSubjectName = list.get(position).getGradeName() + list.get(position).getSubjectName();
                SpUtils.put(getActivity(), Constants.SP_KEY_GRADEID,groupClassID);
                SpUtils.put(getActivity(), Constants.SP_KEY_GRADE_NAME,gradeSubjectName);
                getTeacherWorkSheet(groupClassID);
                mChooseSubjectTv.setText(gradeSubjectName + "备课组");
                chooseSubjectPop.dismiss();
            }
        });
        chooseSubjectPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                chooseSubjectPop.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addHomeWorkLl:
                Intent intent = new Intent(getActivity(), AddJobActivity.class);
                intent.putExtra("gsID",groupClassID);
                intent.putExtra("gradeSubjectName",gradeSubjectName);
                intent.putExtra("mSubjectList", (Serializable) mSubjectList);
                startActivity(intent);
                break;
            case R.id.chooseSubjectTv:
                getTeacherGradeSubject();
                break;
        }
    }
}