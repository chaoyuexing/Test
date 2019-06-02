package com.homework.teacher.teacher.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xing
 * on 2019/5/19
 */
public class FragmentFactory {
    /**
     * 习题册
     */
    public static final int TAB_FEED_BACK = 0;
    /**
     * 试卷
     */
    public static final int TAB_SUBMIT_THE_CASE = 1;
    /**
     * 其他
     */
    public static final int TAB_JOB_CONTENT = 2;


    private static Map<Integer,Fragment> mFragmentMap = new HashMap<>();

    public static Fragment creatFragment(int index){
        Fragment fragment = mFragmentMap.get(index);
        //如果之前没有创建，就创建新的
        if (fragment == null){
            switch (index){
                case TAB_FEED_BACK:
                    fragment = new ExerciseBookFragment();
                    break;
                case TAB_SUBMIT_THE_CASE:
                    fragment = new PaperFragment();
                    break;
                case TAB_JOB_CONTENT:
                    fragment = new RestsFragment();
                    break;
            }
            //把创建的fragment存起来
            mFragmentMap.put(index,fragment);
        }
        return fragment;
    }
}
