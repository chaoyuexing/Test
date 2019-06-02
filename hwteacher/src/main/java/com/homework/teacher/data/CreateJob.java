package com.homework.teacher.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by xing
 * on 2019/5/18
 */
public class CreateJob extends BaseData<CreateJob>{

    @Override
    Type getType() {
        return new TypeToken<CreateJob>(){}.getType();
    }

    @Override
    public CreateJob getFromGson(String str) {
        return super.getFromGson(str);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int code;
    public String message;
    public CreateJobdata data;

    public CreateJobdata getData() {
        return data;
    }

    public void setData(CreateJobdata data) {
        this.data = data;
    }

    public class CreateJobdata {
        /**
         * addPerson :
         * addTime :
         * bgnTime :
         * classID : 0
         * creTeacherID : 0
         * delFlag : 0
         * endTime :
         * freedomContent :
         * gsID : 0
         * id : 0
         * state : 0
         * updatePerson :
         * updateTime :
         * wsName :
         */

        private String addPerson;
        private String addTime;
        private String bgnTime;
        private int classID;
        private int creTeacherID;
        private int delFlag;
        private String endTime;
        private String freedomContent;
        private int gsID;
        private int id;
        private int state;
        private String updatePerson;
        private String updateTime;
        private String wsName;

        public String getAddPerson() {
            return addPerson;
        }

        public void setAddPerson(String addPerson) {
            this.addPerson = addPerson;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getBgnTime() {
            return bgnTime;
        }

        public void setBgnTime(String bgnTime) {
            this.bgnTime = bgnTime;
        }

        public int getClassID() {
            return classID;
        }

        public void setClassID(int classID) {
            this.classID = classID;
        }

        public int getCreTeacherID() {
            return creTeacherID;
        }

        public void setCreTeacherID(int creTeacherID) {
            this.creTeacherID = creTeacherID;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getFreedomContent() {
            return freedomContent;
        }

        public void setFreedomContent(String freedomContent) {
            this.freedomContent = freedomContent;
        }

        public int getGsID() {
            return gsID;
        }

        public void setGsID(int gsID) {
            this.gsID = gsID;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getUpdatePerson() {
            return updatePerson;
        }

        public void setUpdatePerson(String updatePerson) {
            this.updatePerson = updatePerson;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getWsName() {
            return wsName;
        }

        public void setWsName(String wsName) {
            this.wsName = wsName;
        }

    }



}
