package com.digi.anypayments.Model;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("UserId")
    private String userId;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("Email")
    private String email;

    @SerializedName("MobileNo")
    private String mobileNo;

    @SerializedName("WalletBalance")
    private String walletBalance;

    @SerializedName("Password")
    private String password;

    @SerializedName("reqid")
    private String reqid;

    @SerializedName("status")
    private String status;

    @SerializedName("remark")
    private String remark;

    @SerializedName("mn")
    private String mn;

    @SerializedName("ec")
    private String ec;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }
}
