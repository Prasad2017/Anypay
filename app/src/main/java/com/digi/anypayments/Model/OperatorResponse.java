package com.digi.anypayments.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OperatorResponse {

    @SerializedName("operatorName")
    public String operatorName;

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Oprator")
    @Expose
    private String oprator;
    @SerializedName("opratorcode")
    @Expose
    private String opratorcode;
    @SerializedName("otype")
    @Expose
    private String otype;
    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("operator")
    @Expose
    private String operator;

    @SerializedName("circle")
    @Expose
    private String circle;


    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOprator() {
        return oprator;
    }

    public void setOprator(String oprator) {
        this.oprator = oprator;
    }

    public String getOpratorcode() {
        return opratorcode;
    }

    public void setOpratorcode(String opratorcode) {
        this.opratorcode = opratorcode;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }
}
