package biz.realpayment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Operator {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Data")
    @Expose
    private List<OperatorResponse> data = null;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Remark")
    @Expose
    private String remark;
    @SerializedName("ResponceId")
    @Expose
    private Integer responceId;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public List<OperatorResponse> getData() {
        return data;
    }

    public void setData(List<OperatorResponse> data) {
        this.data = data;
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

    public Integer getResponceId() {
        return responceId;
    }

    public void setResponceId(Integer responceId) {
        this.responceId = responceId;
    }
}
