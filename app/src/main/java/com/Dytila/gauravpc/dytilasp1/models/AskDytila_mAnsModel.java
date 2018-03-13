package com.Dytila.gauravpc.dytilasp1.models;

/**
 * Created by gaurav pc on 30-Dec-16.
 */
public class AskDytila_mAnsModel {
    private String que_id;
    private String que;
    private String que_date;
    private String que_username;
    private String ans_id;
    private String ans;
    private String ans_date;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAns_date() {
        return ans_date;
    }

    public void setAns_date(String ans_date) {
        this.ans_date = ans_date;
    }

    public String getAns_id() {
        return ans_id;
    }

    public void setAns_id(String ans_id) {
        this.ans_id = ans_id;
    }

    public String getQue() {
        return que;
    }

    public void setQue(String que) {
        this.que = que;
    }

    public String getQue_date() {
        return que_date;
    }

    public void setQue_date(String que_date) {
        this.que_date = que_date;
    }

    public String getQue_id() {
        return que_id;
    }

    public void setQue_id(String que_id) {
        this.que_id = que_id;
    }

    public String getQue_username() {
        return que_username;
    }

    public void setQue_username(String que_username) {
        this.que_username = que_username;
    }

    public String getAns() {

        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
