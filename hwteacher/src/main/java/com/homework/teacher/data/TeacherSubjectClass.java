package com.homework.teacher.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by xing
 * on 2019/5/19
 */
public class TeacherSubjectClass extends BaseData<TeacherSubjectClass> {


    private int code;
    private String message;
    private TeacherSubjectClassData data;


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public TeacherSubjectClassData getData() {
        return data;
    }

    @Override
    Type getType() {
        return new TypeToken<TeacherSubjectClass>(){}.getType();
    }

    @Override
    public TeacherSubjectClass getFromGson(String str) {
        return super.getFromGson(str);
    }

    public class TeacherSubjectClassData {

        /**
         * addPerson :
         * addTime :
         * applyState : 0
         * classID : 0
         * classNumber : 0
         * delFlag : 0
         * gradeID : 0
         * gradeName :
         * id : 0
         * subjectCode :
         * subjectName :
         * updatePerson :
         * updateTime :
         */

        private String addPerson;
        private String addTime;
        private int applyState;
        private int classID;
        private int classNumber;
        private int delFlag;
        private int gradeID;
        private String gradeName;
        private int id;
        private String subjectCode;
        private String subjectName;
        private String updatePerson;
        private String updateTime;

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

        public int getApplyState() {
            return applyState;
        }

        public void setApplyState(int applyState) {
            this.applyState = applyState;
        }

        public int getClassID() {
            return classID;
        }

        public void setClassID(int classID) {
            this.classID = classID;
        }

        public int getClassNumber() {
            return classNumber;
        }

        public void setClassNumber(int classNumber) {
            this.classNumber = classNumber;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
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
