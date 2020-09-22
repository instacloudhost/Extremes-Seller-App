package com.instacloudhost.extremes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MDistrict {
    @SerializedName("dist")
    @Expose
    private String dist;

    public String getDist() {
        return dist;
    }

    public void setDist(String name) {
        this.dist = name;
    }
}
