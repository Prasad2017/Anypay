package com.digi.anypayments.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPResponse {

    @SerializedName("LoginStatus")
    @Expose
    private String loginStatus;
    @SerializedName("Balance")
    @Expose
    private String balance;
    @SerializedName("BalanceStatus")
    @Expose
    private String balanceStatus;
    @SerializedName("ValidNumbers")
    @Expose
    private String validNumbers;
    @SerializedName("SMSCount")
    @Expose
    private String sMSCount;
    @SerializedName("MsgStatus")
    @Expose
    private String msgStatus;
    @SerializedName("CurrentBalance")
    @Expose
    private String currentBalance;
    @SerializedName("Transaction_ID")
    @Expose
    private String transactionID;



    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceStatus() {
        return balanceStatus;
    }

    public void setBalanceStatus(String balanceStatus) {
        this.balanceStatus = balanceStatus;
    }

    public String getValidNumbers() {
        return validNumbers;
    }

    public void setValidNumbers(String validNumbers) {
        this.validNumbers = validNumbers;
    }

    public String getSMSCount() {
        return sMSCount;
    }

    public void setSMSCount(String sMSCount) {
        this.sMSCount = sMSCount;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

}
