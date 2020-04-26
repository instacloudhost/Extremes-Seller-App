package com.instacloudhost.extremes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class graphData {
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("today")
    @Expose
    private String today;
    @SerializedName("agent_name")
    @Expose
    private String agentName;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
