package com.homework.teacher.teacher.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.homework.teacher.R;
import com.multilevel.treelist.Dept;
import com.multilevel.treelist.Node;

import java.util.List;

/**
 * Created by xing
 * on 2019/5/25
 */
public class addPaperAdapter extends BaseQuickAdapter<Node,BaseViewHolder> {

    private int pid; //根节点ID

    public addPaperAdapter(int layoutId, List<Node> datas,int pid) {
        super(layoutId, datas);
        this.pid = pid;
    }

    @Override
    protected void convert(BaseViewHolder holder, Node node) {
        for (int i = 0; i < node.get_childrenList().size(); i++) {
            Dept dept = (Dept) node.get_childrenList().get(i);
            holder.setText(R.id.name,dept.get_label());
        }
    }
}
