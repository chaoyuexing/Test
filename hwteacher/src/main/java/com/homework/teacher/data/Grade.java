package com.homework.teacher.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangkaichao on 2019/2/21.
 */

public class Grade implements Serializable {

    private String addPerson;
    private String addTime;
    private String gradeCode;//编码
    private int delFlag;//删除标识位，0：未删除，1：已删除
    private String hiconUrl;//高亮图标URL
    private String iconUrl;//图标URL
    private int id;
    private String updatePerson;
    private String updateTime;
    private String upperCode;//上级编码
    private String gradeName;//值

    public static List<Grade> parseJson(JSONArray jsonArray) {
        List<Grade> list = new ArrayList<>();
        if (null != jsonArray && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                if (null != obj) {
                    Grade grade = new Grade();
//                    grade.setAddPerson(obj.optString("addPerson"));
//                    grade.setAddTime(obj.optString("addTime"));
                    grade.setGradeCode(obj.optString("gradeCode"));
//                    grade.setDelFlag(obj.optInt("delFlag"));
//                    grade.setHiconUrl(obj.optString("hiconUrl"));
//                    grade.setIconUrl(obj.optString("iconUrl"));
                    grade.setId(obj.optInt("id"));
//                    grade.setUpdatePerson(obj.optString("updatePerson"));
//                    grade.setUpdateTime(obj.optString("updateTime"));
//                    grade.setUpperCode(obj.optString("upperCode"));
                    grade.setGradeName(obj.optString("gradeName"));
                    list.add(grade);
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

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String code) {
        this.gradeCode = gradeCode;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getHiconUrl() {
        return hiconUrl;
    }

    public void setHiconUrl(String hiconUrl) {
        this.hiconUrl = hiconUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUpperCode() {
        return upperCode;
    }

    public void setUpperCode(String upperCode) {
        this.upperCode = upperCode;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
