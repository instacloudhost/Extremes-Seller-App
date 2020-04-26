package com.instacloudhost.extremes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mlogin {
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("token")
  @Expose
  private String token = null;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getToken() { return token; }

  public void setToken(String token) {
    this.token = token;
  }
}
