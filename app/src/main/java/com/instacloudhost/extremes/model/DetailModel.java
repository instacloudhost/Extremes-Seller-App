package com.instacloudhost.extremes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailModel {
    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("category")
    @Expose
    private String category;


    @SerializedName("btn1")
    @Expose
    private String btn1;


    @SerializedName("btn2")
    @Expose
    private String btn2;


    @SerializedName("btn1_detail")
    @Expose
    private String btn1_detail;


    @SerializedName("btn2_detail")
    @Expose
    private String btn2_detail;


    @SerializedName("icon")
    @Expose
    private String icon;


    @SerializedName("layout")
    @Expose
    private String layout;

    @SerializedName("input_field")
    @Expose
    private CustomerInputField inputField;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBtn1() {
        return btn1;
    }

    public void setBtn1(String btn1) {
        this.btn1 = btn1;
    }

    public String getBtn2() {
        return btn2;
    }

    public void setBtn2(String btn2) {
        this.btn2 = btn2;
    }

    public CustomerInputField getInputField() {
        return inputField;
    }

    public void setInputField(CustomerInputField inputField) {
        this.inputField = inputField;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getBtn1_detail() {
        return btn1_detail;
    }

    public void setBtn1_detail(String btn1_detail) {
        this.btn1_detail = btn1_detail;
    }

    public String getBtn2_detail() {
        return btn2_detail;
    }

    public void setBtn2_detail(String btn2_detail) {
        this.btn2_detail = btn2_detail;
    }
}