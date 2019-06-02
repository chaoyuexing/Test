package com.homework.teacher.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xing
 * on 2019/5/18
 */
public class HomeListJob extends BaseData<HomeListJob> {
    @Override
    Type getType() {
        return  new TypeToken<HomeListJob>(){}.getType();
    }

    @Override
    public HomeListJob getFromGson(String str) {
        return super.getFromGson(str);
    }

    public int code;
    public String message;
    public List<HomeListJobData> data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<HomeListJobData> getData() {
        return data;
    }

    public class  HomeListJobData {

        /**
         * addPerson : 张凯超
         * addTime : 1558186057000
         * creTeacherID : 6
         * id : 25
         * state : 0
         * wsName :
         */

        private String addPerson;
        private long addTime;
        private int creTeacherID;
        private int id;
        private int state;
        private String wsName;

        public String getAddPerson() {
            return addPerson;
        }

        public void setAddPerson(String addPerson) {
            this.addPerson = addPerson;
        }

        public long getAddTime() {
            return addTime;
        }

        public void setAddTime(long addTime) {
            this.addTime = addTime;
        }

        public int getCreTeacherID() {
            return creTeacherID;
        }

        public void setCreTeacherID(int creTeacherID) {
            this.creTeacherID = creTeacherID;
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

        public String getWsName() {
            return wsName;
        }

        public void setWsName(String wsName) {
            this.wsName = wsName;
        }
    }
}
