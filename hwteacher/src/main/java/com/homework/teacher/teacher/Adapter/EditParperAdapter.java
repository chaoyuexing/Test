package com.homework.teacher.teacher.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.teacher.R;
import com.multilevel.treelist.Dept;
import com.multilevel.treelist.Node;

import java.util.List;

/**
 * Created by xing
 * on 2019/5/26
 */
public class EditParperAdapter extends BaseQuickAdapter<Node,BaseViewHolder> {

    public EditParperAdapter(int layoutResId, @Nullable List<Node> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Node item) {
        helper.setText(R.id.name,item.get_label());
        for (int i = 0; i < item.get_childrenList().size(); i++) {
            Dept dept = (Dept) item.get_childrenList().get(i);
            helper.setText(R.id.name,dept.get_label());
        }
        helper.addOnClickListener(R.id.paper_delete);
        helper.addOnClickListener(R.id.paper_edit);
        helper.addOnClickListener(R.id.paper_order);
    }
}
