package com.instacloudhost.extremes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mgraph {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private graphData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public graphData getData() {
        return data;
    }

    public void setData(graphData data) {
        this.data = data;
    }
}
