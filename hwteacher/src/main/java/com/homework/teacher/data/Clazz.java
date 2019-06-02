package com.homework.teacher.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangkaichao on 2019/2/21.
 */

public class Clazz implements Serializable {

    private String addPerson;
    private String addTime;
    private int applyState;//班主任申请状态，1：待审核，2：通过，-1：拒绝
    private String auditor;//审核人
    private int auditorID;//审核人ID
    private int delFlag;//删除标识位，0：未删除，1：已删除
    private int gradeID;//年级ID
    private String gradeName;//年级名称
    private int id;
    private int masterID;//班主任ID
    private int number;//班级序号
    private String updatePerson;
    private String updateTime;

    public static List<Clazz> parseJson(JSONArray jsonArray) {
        List<Clazz> list = new ArrayList<>();
        if (null != jsonArray && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                if (null != obj) {
                    Clazz clazz = new Clazz();
                    clazz.setAddPerson(obj.optString("addPerson"));
                    clazz.setAddTime(obj.optString("addTime"));
                    clazz.setApplyState(obj.optInt("applyState"));
                    clazz.setAuditor(obj.optString("auditor"));
                    clazz.setAuditorID(obj.optInt("auditorID"));
                    clazz.setDelFlag(obj.optInt("delFlag"));
                    clazz.setGradeID(obj.optInt("gradeID"));
                    clazz.setGradeName(obj.optString("gradeName"));
                    clazz.setId(obj.optInt("id"));
                    clazz.setMasterID(obj.optInt("masterID"));
                    if ((obj.optInt("number")) != 0) {
                        clazz.setNumber(obj.optInt("number"));
                    } else {
                        clazz.setNumber(obj.optInt("classNumber"));
                    }
                    clazz.setUpdatePerson(obj.optString("updatePerson"));
                    clazz.setUpdateTime(obj.optString("updateTime"));
                    list.add(clazz);
                }
            }
        }
        return list;
    }

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

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public int getAuditorID() {
        return auditorID;
    }

    public void setAuditorID(int auditorID) {
        this.auditorID = auditorID;
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

    public int getMasterID() {
        return masterID;
    }

    public void setMasterID(int masterID) {
        this.masterID = masterID;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
