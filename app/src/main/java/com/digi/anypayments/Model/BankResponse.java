package com.digi.anypayments.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankResponse {

    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("mtx")
    @Expose
    private String mtx;
    @SerializedName("attempts")
    @Expose
    private String attempts;
    @SerializedName("sub_accounts_id")
    @Expose
    private String subAccountsId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("entity")
    @Expose
    private String entity;
    @SerializedName("status")
    @Expose
    private String status;


    //All getter and Setter


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMtx() {
        return mtx;
    }

    public void setMtx(String mtx) {
        this.mtx = mtx;
    }

    public String getAttempts() {
        return attempts;
    }

    public void setAttempts(String attempts) {
        this.attempts = attempts;
    }

    public String getSubAccountsId() {
        return subAccountsId;
    }

    public void setSubAccountsId(String subAccountsId) {
        this.subAccountsId = subAccountsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
