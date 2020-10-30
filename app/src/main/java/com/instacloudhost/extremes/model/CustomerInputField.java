package com.instacloudhost.extremes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class CustomerInputField {

    @SerializedName("c1")
    @Expose
    private String c1;
    @SerializedName("c2")
    @Expose
    private String c2;
    @SerializedName("c3")
    @Expose
    private String c3="";
    @SerializedName("c4")
    @Expose
    private String c4="";

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }

    public String getC4() {
        return c4;
    }

    public void setC4(String c4) {
        this.c4 = c4;
    }
}
