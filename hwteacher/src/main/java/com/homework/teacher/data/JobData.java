package com.homework.teacher.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xing
 * on 2019/5/2
 */
public class JobData {


    private String jobName;
    private List<Problems> mProblemsArrayList;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public List<Problems> getProblemsArrayList() {
        return mProblemsArrayList;
    }

    public void setProblemsArrayList(List<Problems> problemsArrayList) {
        mProblemsArrayList = problemsArrayList;
    }

    public static class Problems {
        private String problemsName;
        private ArrayList<String> answer;
        private ArrayList<String> video;

        public String getProblemsName() {
            return problemsName;
        }

        public void setProblemsName(String problemsName) {
            this.problemsName = problemsName;
        }

        public ArrayList<String> getAnswer() {
            return answer;
        }

        public void setAnswer(ArrayList<String> answer) {
            this.answer = answer;
        }

        public ArrayList<String> getVideo() {
            return video;
        }

        public void setVideo(ArrayList<String> video) {
            this.video = video;
        }
    }

}
