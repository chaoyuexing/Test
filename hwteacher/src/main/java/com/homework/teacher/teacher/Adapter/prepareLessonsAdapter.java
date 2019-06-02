package com.homework.teacher.teacher.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.teacher.R;
import com.homework.teacher.data.GradeSubject;

import java.util.List;

/**
 * Created by xing
 * on 2019/5/19
 */
public class prepareLessonsAdapter extends BaseQuickAdapter<GradeSubject.GradeSubjectData,BaseViewHolder> {

    private int type;

    public prepareLessonsAdapter(int layoutId, List<GradeSubject.GradeSubjectData> datas,int type) {
        super(layoutId, datas);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, GradeSubject.GradeSubjectData subject) {
//        if (type == SelectTeamGroupActivity.CLASS_TYPE) {

//        } else {
            holder.setText(R.id.cb_prepare_lessons,subject.getGradeName()+subject.getSubjectName());
//        }
    }
}
