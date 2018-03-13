package com.Dytila.gauravpc.dytilasp1.models;

/**
 * Created by gaurav pc on 27-Dec-16.
 */
public class AskDytila_Answers {
    private String ans_id;
    private String username;
    private String ans;
    private String date;
    private String avatar;
    private String liked;
    private String likesCount;

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

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

    public String getAns_id() {
        return ans_id;
    }

    public void setAns_id(String ans_id) {
        this.ans_id = ans_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
