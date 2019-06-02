package com.homework.teacher.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xing
 * on 2019/5/21
 */
public class MediumCatalog extends BaseData<MediumCatalog> {

    /**
     * code : 0
     * data : [{"id":1,"name":"2018期中物理试卷1","pid":0},{"id":15,"name":"12231","pid":0},{"id":16,"name":"11111","pid":0},{"id":17,"name":"11111","pid":0},{"id":2,"name":"第一题","pid":1},{"id":3,"name":"第二题","pid":1},{"id":14,"name":"第三题","pid":1},{"id":5,"name":"一","pid":3},{"id":6,"name":"二","pid":3},{"id":7,"name":"三","pid":3}]
     * message : Success
     */

    private String code;
    private String message;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }


    public List<DataBean> getData() {
        return data;
    }

    @Override
    Type getType() {
        return  new TypeToken<MediumCatalog>(){}.getType();
    }

    @Override
    public MediumCatalog getFromGson(String str) {
        return super.getFromGson(str);
    }

    public static class DataBean {
        /**
         * id : 1
         * name : 2018期中物理试卷1
         * pid : 0
         */

        private int id;
        private String name;
        private int pid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }
    }
}
