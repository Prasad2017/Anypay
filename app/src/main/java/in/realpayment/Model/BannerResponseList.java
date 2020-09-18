package in.realpayment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerResponseList {


    @SerializedName("BannerId")
    @Expose
    private String bannerId;
    @SerializedName("LoginId")
    @Expose
    private String loginId;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("BannerDetails")
    @Expose
    private String bannerDetails;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("CompanyURL")
    @Expose
    private String companyURL;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("BannerImageId")
    @Expose
    private String bannerImageId;
    @SerializedName("BannerImageType")
    @Expose
    private String bannerImageType;
    @SerializedName("BannerImage")
    @Expose
    private String bannerImage;
    @SerializedName("BannerImageURL")
    @Expose
    private String bannerImageURL;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;


    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getBannerDetails() {
        return bannerDetails;
    }

    public void setBannerDetails(String bannerDetails) {
        this.bannerDetails = bannerDetails;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyURL() {
        return companyURL;
    }

    public void setCompanyURL(String companyURL) {
        this.companyURL = companyURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBannerImageId() {
        return bannerImageId;
    }

    public void setBannerImageId(String bannerImageId) {
        this.bannerImageId = bannerImageId;
    }

    public String getBannerImageType() {
        return bannerImageType;
    }

    public void setBannerImageType(String bannerImageType) {
        this.bannerImageType = bannerImageType;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerImageURL() {
        return bannerImageURL;
    }

    public void setBannerImageURL(String bannerImageURL) {
        this.bannerImageURL = bannerImageURL;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
