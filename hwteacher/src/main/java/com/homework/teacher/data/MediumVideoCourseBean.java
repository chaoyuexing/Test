package com.homework.teacher.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author：xing Data：2019/6/21
 * brief:
 **/
public class MediumVideoCourseBean extends BaseData<MediumVideoCourseBean>{


    /**
     * code :
     * data : [{"addTime":"","id":0,"knowledgeID":0,"knowledgeName":"","playNum":0,"quoteTeacherID":0,"quoteTeacherName":"","typeCode":"","typeValue":"","url":"","vcID":0}]
     * message :
     */

    private String code;
    private String message;
    private List<DataBean> data;


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

    @Override
    Type getType() {
        return  new TypeToken<MediumVideoCourseBean>(){}.getType();
    }

    @Override
    public MediumVideoCourseBean getFromGson(String str) {
        return super.getFromGson(str);
    }

    public static class DataBean {
        /**
         * addTime :
         * id : 0
         * knowledgeID : 0
         * knowledgeName :
         * playNum : 0
         * quoteTeacherID : 0
         * quoteTeacherName :
         * typeCode :
         * typeValue :
         * url :
         * vcID : 0
         */

        private String addTime;
        private int id;
        private int knowledgeID;
        private String knowledgeName;
        private int playNum;
        private int quoteTeacherID;
        private String quoteTeacherName;
        private String typeCode;
        private String typeValue;
        private String url;
        private int vcID;


        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getKnowledgeID() {
            return knowledgeID;
        }

        public void setKnowledgeID(int knowledgeID) {
            this.knowledgeID = knowledgeID;
        }

        public String getKnowledgeName() {
            return knowledgeName;
        }

        public void setKnowledgeName(String knowledgeName) {
            this.knowledgeName = knowledgeName;
        }

        public int getPlayNum() {
            return playNum;
        }

        public void setPlayNum(int playNum) {
            this.playNum = playNum;
        }

        public int getQuoteTeacherID() {
            return quoteTeacherID;
        }

        public void setQuoteTeacherID(int quoteTeacherID) {
            this.quoteTeacherID = quoteTeacherID;
        }

        public String getQuoteTeacherName() {
            return quoteTeacherName;
        }

        public void setQuoteTeacherName(String quoteTeacherName) {
            this.quoteTeacherName = quoteTeacherName;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeValue() {
            return typeValue;
        }

        public void setTypeValue(String typeValue) {
            this.typeValue = typeValue;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getVcID() {
            return vcID;
        }

        public void setVcID(int vcID) {
            this.vcID = vcID;
        }
    }
}
