package com.Dytila.gauravpc.dytilasp1.models;

/**
 * Created by gaurav pc on 25-Dec-16.
 */
public class AskDytila_all {
    private String que_id;
    private String ans_id;

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    private String liked;

    public String getAns_id() {
        return ans_id;
    }

    public void setAns_id(String ans_id) {
        this.ans_id = ans_id;
    }

    public String getQue_id() {
        return que_id;
    }

    public void setQue_id(String que_id) {
        this.que_id = que_id;
    }

    private String que;
    private String ans;
    private String asked_by_user;
    private String answered_by_user;
    private String askedDate;
    private String answeredDate;
    private String user_who_asked_avatar;
    private String user_who_answered_avatar;
    private String likesCount;

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getAnswered_by_user() {
        return answered_by_user;
    }

    public void setAnswered_by_user(String answered_by_user) {
        this.answered_by_user = answered_by_user;
    }

    public String getAnsweredDate() {
        return answeredDate;
    }

    public void setAnsweredDate(String answeredDate) {
        this.answeredDate = answeredDate;
    }

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

    public String getUser_who_answered_avatar() {
        return user_who_answered_avatar;
    }

    public void setUser_who_answered_avatar(String user_who_answered_avatar) {
        this.user_who_answered_avatar = user_who_answered_avatar;
    }

    public String getUser_who_asked_avatar() {
        return user_who_asked_avatar;
    }

    public void setUser_who_asked_avatar(String user_who_asked_avatar) {
        this.user_who_asked_avatar = user_who_asked_avatar;
    }
}
