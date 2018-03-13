package com.Dytila.gauravpc.dytilasp1.models;

/**
 * Created by gaurav pc on 24-Jan-17.
 */
public class Monthly_user_model {

    private String order_id;
    private String mealName;
    private String price;
    private String addons;
    private String pickUpAddress;
    private String time;
    private String img;
    private String Tmeal_freq;
    private String meal_freq;
    private String over;

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getAddons() {
        return addons;
    }

    public void setAddons(String addons) {
        this.addons = addons;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMeal_freq() {
        return meal_freq;
    }

    public void setMeal_freq(String meal_freq) {
        this.meal_freq = meal_freq;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPickUpAddress() {
        return pickUpAddress;
    }

    public void setPickUpAddress(String pickUpAddress) {
        this.pickUpAddress = pickUpAddress;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTmeal_freq() {
        return Tmeal_freq;
    }

    public void setTmeal_freq(String tmeal_freq) {
        Tmeal_freq = tmeal_freq;
    }
}
