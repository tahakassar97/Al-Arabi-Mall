package com.tahakassar.ecommerce.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Brand implements Serializable {
    private String id;
    private String title;
    private String icon;
    private float rating;
    private ArrayList<String> pics;
    private String description;
    private String phone;
    private String lat;
    private String lng;

    public Brand() {
    }

    public Brand(String id, String title, String icon, float rating,
                 ArrayList<String> pics, String description, String phone, String lat,
                 String lng) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.rating = rating;
        this.pics = pics;
        this.description = description;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public float getRating() {
        return rating;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public Brand(String id, String title, String icon,
                 float rating, String description, String phone, ArrayList<String> pics) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.rating = rating;
        this.pics = pics;
        this.description = description;
        this.phone = phone;
    }
}
