package com.homework.teacher.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author：xing Data：2019/5/11
 * brief: 比较两个新旧String字符串数组
 **/
public class CompareStringArrayUtil {

    /**
     * 求两个数组的并集
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] intersection(String[] arr1, String[] arr2) {
        Set<String> set = new HashSet<String>();
        for (String str : arr1) {
            set.add(str);
        }
        for (String str : arr2) {
            set.add(str);
        }
        String[] result = {};
        return set.toArray(result);
    }



    /**
     * 求两个数组的差集
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] differenceSet(String[] arr1, String[] arr2) {
        LinkedList<String> list = new LinkedList<String>();
        LinkedList<String> history = new LinkedList<String>();
        String[] longerArr = arr1;
        String[] shorterArr = arr2;
        //找出较长的数组来减较短的数组
        if (arr1.length > arr2.length) {
            longerArr = arr2;
            shorterArr = arr1;
        }
        for (String str : longerArr) {
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        for (String str : shorterArr) {
            if (list.contains(str)) {
                history.add(str);
                list.remove(str);
            } else {
                if (!history.contains(str)) {
                    list.add(str);
                }
            }
        }

        String[] result = {};
        return list.toArray(result);
    }


}
