package in.realpayment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RechargeResponse {

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;


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

}
