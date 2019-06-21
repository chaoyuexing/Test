package com.homework.teacher.data;

import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xing
 * on 2019/5/18
 */
public abstract class BaseData<T> implements Serializable {

    private final String TAG = BaseData.class.getSimpleName();

    abstract Type getType();

    public List<T> getListFromGson(String str) {
        List<T> result = null;
        try {
            result = new Gson().fromJson(str, getType());
        } catch (Exception e) {
            Log.e(TAG, "getListFromGson: "+e.getMessage());
        }
        return result;
    }

    public  T getFromGson(String str) {
        T result = null;
        try {
            result = new Gson().fromJson(str, getType());
        } catch (Exception e) {
            Log.e(TAG, "getListFromGson: "+e.getMessage());
        }
        return result;
    }

}
