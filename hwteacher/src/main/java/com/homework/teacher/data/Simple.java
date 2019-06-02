package com.homework.teacher.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by xing
 * on 2019/5/19
 */
public class Simple extends BaseData<Simple> {


    public int code;
    public String message;
    public String data;

    @Override
    Type getType() {
        return new TypeToken<Simple>(){}.getType();
    }

    @Override
    public Simple getFromGson(String str) {
        return super.getFromGson(str);
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
