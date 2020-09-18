package in.realpayment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerResponse {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Data")
    @Expose
    private List<BannerResponseList> bannerResponseList=null;


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

    public List<BannerResponseList> getBannerResponseList() {
        return bannerResponseList;
    }

    public void setBannerResponseList(List<BannerResponseList> bannerResponseList) {
        this.bannerResponseList = bannerResponseList;
    }
}
