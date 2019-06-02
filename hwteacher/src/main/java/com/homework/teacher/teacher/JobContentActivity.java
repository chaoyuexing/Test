package com.homework.teacher.teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.homework.teacher.R;
import com.homework.teacher.data.GradeSubject;
import com.homework.teacher.teacher.Adapter.FmPagerAdapter;
import com.homework.teacher.teacher.fragment.FragmentFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xing
 * on 2019/5/19 作业内容页面
 */
public class JobContentActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.job_tab_layout)
    TabLayout mJobTabLayout;
    @BindView(R.id.job_view_page)
    ViewPager mJobViewPage;

    private FmPagerAdapter pagerAdapter;

    private String[] tabName = new String[]{"习题册","试卷","其他"};
    private List<Fragment> tabFragments;
    private  List<GradeSubject> mSubjectList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_centent);
        ButterKnife.bind(this);
        mSubjectList = (List<GradeSubject>) getIntent().getSerializableExtra("mSubjectList");
        mIvBack.setOnClickListener(this);
        initView();
    }

    private void initView() {
        tabFragments = new ArrayList<>();
        pagerAdapter = new FmPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < tabName.length; i++) {
            mJobTabLayout.addTab(mJobTabLayout.newTab().setText(tabName[i]));
            tabFragments.add(FragmentFactory.creatFragment(i));
        }
        pagerAdapter.setTitles(tabName);
        pagerAdapter.setFragments(tabFragments);
        mJobViewPage.setAdapter(pagerAdapter);
        mJobTabLayout.setupWithViewPager(mJobViewPage);
        mJobTabLayout.getTabAt(2).select();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
