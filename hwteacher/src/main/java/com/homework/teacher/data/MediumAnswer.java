package com.homework.teacher.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xing
 * on 2019/6/2
 */
public class MediumAnswer extends BaseData<MediumAnswer>{
    /**
     * code :
     * data : [{"addTime":"","catalogID":0,"content":"","creTeacherID":0,"creTeacherName":"","delFlag":0,"id":0,"type":0}]
     * message :
     */

    private String code;
    private String message;
    private List<DataBean> data;

    @Override
    Type getType() {
        return new TypeToken<MediumAnswer>() {
        }.getType();
    }

    @Override
    public MediumAnswer getFromGson(String str) {
        return super.getFromGson(str);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * addTime :
         * catalogID : 0
         * content :
         * creTeacherID : 0
         * creTeacherName :
         * delFlag : 0
         * id : 0
         * type : 0
         */

        private String addTime;
        private int catalogID;
        private String content;
        private int creTeacherID;
        private String creTeacherName;
        private int delFlag;
        private int id;
        private int type;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public int getCatalogID() {
            return catalogID;
        }

        public void setCatalogID(int catalogID) {
            this.catalogID = catalogID;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCreTeacherID() {
            return creTeacherID;
        }

        public void setCreTeacherID(int creTeacherID) {
            this.creTeacherID = creTeacherID;
        }

        public String getCreTeacherName() {
            return creTeacherName;
        }

        public void setCreTeacherName(String creTeacherName) {
            this.creTeacherName = creTeacherName;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
