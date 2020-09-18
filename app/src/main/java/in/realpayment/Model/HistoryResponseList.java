package in.realpayment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryResponseList {


    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Data")
    @Expose
    private List<HistoryResponse> historyResponseList=null;


    //All setter and getter method..
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

    public List<HistoryResponse> getHistoryResponseList() {
        return historyResponseList;
    }

    public void setHistoryResponseList(List<HistoryResponse> historyResponseList) {
        this.historyResponseList = historyResponseList;
    }


}
