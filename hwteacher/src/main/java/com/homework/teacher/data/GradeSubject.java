package com.homework.teacher.data;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zhangkaichao on 2019/4/9.
 */

public class GradeSubject extends BaseData<GradeSubject> {

    @Override
    Type getType() {
        return new TypeToken<GradeSubject>() {
        }.getType();
    }

    @Override
    public GradeSubject getFromGson(String str) {
        return super.getFromGson(str);
    }

    public int code;
    public String message;
    public List<GradeSubjectData> data;


    public class GradeSubjectData implements Serializable {
        private String addPerson;
        private String addTime;
        private int delFlag;//删除标识位，0：未删除，1：已删除
        private String gradeCode;//年级Code
        private int gradeID;//年级ID
        private String gradeName;//年级名称
        private int id;
        private String subjectCode;//学科code（字典表）
        private String subjectName;//学科名称
        private String updatePerson;
        private String updateTime;
        private String startTime;
        private String overTime;
        private boolean selected ;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getOverTime() {
            return overTime;
        }

        public void setOverTime(String overTime) {
            this.overTime = overTime;
        }

        public boolean getSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }


        //        public static List<GradeSubject> parseJson(JSONArray jsonArray) {
//            List<GradeSubject> list = new ArrayList<>();
//            if (null != jsonArray && jsonArray.length() > 0) {
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject obj = jsonArray.optJSONObject(i);
//                    if (null != obj) {
//                        GradeSubject gradeSubject = new GradeSubject();
//                        gradeSubject.setAddPerson(obj.optString("addPerson"));
//                        gradeSubject.setAddTime(obj.optString("addTime"));
//                        gradeSubject.setDelFlag(obj.optInt("delFlag"));
//                        gradeSubject.setGradeCode(obj.optString("gradeCode"));
//                        gradeSubject.setGradeID(obj.optInt("gradeID"));
//                        gradeSubject.setGradeName(obj.optString("gradeName"));
//                        gradeSubject.setId(obj.optInt("id"));
//                        gradeSubject.setSubjectCode(obj.optString("subjectCode"));
//                        gradeSubject.setSubjectName(obj.optString("subjectName"));
//                        gradeSubject.setUpdatePerson(obj.optString("updatePerson"));
//                        gradeSubject.setUpdateTime(obj.optString("updateTime"));
//                        list.add(gradeSubject);
//                    }
//                }
//            }
//            return list;
//        }

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

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }

        public String getGradeCode() {
            return gradeCode;
        }

        public void setGradeCode(String gradeCode) {
            this.gradeCode = gradeCode;
        }

        public int getGradeID() {
            return gradeID;
        }

        public void setGradeID(int gradeID) {
            this.gradeID = gradeID;
        }

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSubjectCode() {
            return subjectCode;
        }

        public void setSubjectCode(String subjectCode) {
            this.subjectCode = subjectCode;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
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
    }


}
