package com.homework.teacher.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xing
 * on 2019/6/2
 */
public class InteractQue extends BaseData<InteractQue> {


    @Override
    Type getType() {
        return new TypeToken<InteractQue>() {
        }.getType();
    }

    @Override
    public InteractQue getFromGson(String str) {
        return super.getFromGson(str);
    }

    /**
     * code :
     * data : [{"answerNum":0,"delFlag":0,"id":0,"interactID":0,"queID":0,"queName":"","vcNum":0}]
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


    public static class DataBean {
        /**
         * answerNum : 0
         * delFlag : 0
         * id : 0
         * interactID : 0
         * queID : 0
         * queName :
         * vcNum : 0
         */

        private int answerNum;
        private int delFlag;
        private int id;
        private int interactID;
        private int queID;
        private String queName;
        private int vcNum;

        public int getAnswerNum() {
            return answerNum;
        }

        public void setAnswerNum(int answerNum) {
            this.answerNum = answerNum;
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

        public int getInteractID() {
            return interactID;
        }

        public void setInteractID(int interactID) {
            this.interactID = interactID;
        }

        public int getQueID() {
            return queID;
        }

        public void setQueID(int queID) {
            this.queID = queID;
        }

        public String getQueName() {
            return queName;
        }

        public void setQueName(String queName) {
            this.queName = queName;
        }

        public int getVcNum() {
            return vcNum;
        }

        public void setVcNum(int vcNum) {
            this.vcNum = vcNum;
        }
    }
}
