package com.Dytila.gauravpc.dytilasp1.models;

/**
 * Created by gaurav pc on 25-Dec-16.
 */
public class AskDytila_ques {
    private String que_id;

    public String getQue_id() {
        return que_id;
    }

    public void setQue_id(String que_id) {
        this.que_id = que_id;
    }

    private String que;
    private String asked_by_user;
    private String askedDate;
    private String user_who_asked_avatar;

    public String getAsked_by_user() {
        return asked_by_user;
    }

    public void setAsked_by_user(String asked_by_user) {
        this.asked_by_user = asked_by_user;
    }

    public String getAskedDate() {
        return askedDate;
    }

    public void setAskedDate(String askedDate) {
        this.askedDate = askedDate;
    }

    public String getQue() {
        return que;
    }

    public void setQue(String que) {
        this.que = que;
    }

    public String getUser_who_asked_avatar() {
        return user_who_asked_avatar;
    }

    public void setUser_who_asked_avatar(String user_who_asked_avatar) {
        this.user_who_asked_avatar = user_who_asked_avatar;
    }
}
