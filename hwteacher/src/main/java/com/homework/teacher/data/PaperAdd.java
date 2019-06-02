package com.homework.teacher.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by xing
 * on 2019/5/20
 */
public class PaperAdd extends BaseData<PaperAdd>{

    private int code;
    private String message;
    private PaperAddData data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public PaperAddData getData() {
        return data;
    }

    @Override
    Type getType() {
        return new TypeToken<PaperAdd>(){}.getType();
    }

    @Override
    public PaperAdd getFromGson(String str) {
        return super.getFromGson(str);
    }


    public class PaperAddData {

        /**
         * gsID : 1
         * id : 0
         * name :
         * term : 1
         * type : 3
         */

        private int gsID;
        private int id;
        private String name;
        private int term;


        private int type;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTerm() {
            return term;
        }

        public void setTerm(int term) {
            this.term = term;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
