package com.homework.teacher.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangkaichao on 2019/2/20.
 */

public class Subject implements Serializable {

    private String addPerson;
    private String addTime;
    private String code;//编码
    private int delFlag;//删除标识位，0：未删除，1：已删除
    private String hiconUrl;//高亮图标URL
    private String iconUrl;//图标URL
    private int id;
    private String updatePerson;
    private String updateTime;
    private String upperCode;//上级编码
    private String value;//值

    public static List<Subject> parseJson(JSONArray jsonArray) {
        List<Subject> list = new ArrayList<Subject>();
        if (null != jsonArray && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                if (null != obj) {
                    Subject subject = new Subject();
//                    subject.setAddPerson(obj.optString("addPerson"));
//                    subject.setAddTime(obj.optString("addTime"));
                    subject.setCode(obj.optString("code"));
//                    subject.setDelFlag(obj.optInt("delFlag"));
                    subject.setHiconUrl(obj.optString("hiconUrl"));
                    subject.setIconUrl(obj.optString("iconUrl"));
//                    subject.setId(obj.optInt("id"));
//                    subject.setUpdatePerson(obj.optString("updatePerson"));
//                    subject.setUpdateTime(obj.optString("updateTime"));
//                    subject.setUpperCode(obj.optString("upperCode"));
                    subject.setValue(obj.optString("value"));
                    list.add(subject);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
