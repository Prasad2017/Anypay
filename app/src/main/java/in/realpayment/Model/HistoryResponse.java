package in.realpayment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryResponse {

    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("OTP")
    @Expose
    private String oTP;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("ReferalId")
    @Expose
    private String referalId;
    @SerializedName("MPin")
    @Expose
    private String mPin;
    @SerializedName("MacAddress")
    @Expose
    private String macAddress;
    @SerializedName("WalletBalance")
    @Expose
    private String walletBalance;
    @SerializedName("CashBack")
    @Expose
    private String cashBack;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("ModifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("RoleId")
    @Expose
    private String roleId;
    @SerializedName("ResponceId")
    @Expose
    private String responceId;
    @SerializedName("reqid")
    @Expose
    private String reqid;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("mn")
    @Expose
    private String mn;
    @SerializedName("field1")
    @Expose
    private String field1;
    @SerializedName("ec")
    @Expose
    private String ec;
    @SerializedName("apirefid")
    @Expose
    private String apirefid;
    @SerializedName("amt")
    @Expose
    private String amt;


    //All Getter and Setter Method..

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getoTP() {
        return oTP;
    }

    public void setoTP(String oTP) {
        this.oTP = oTP;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferalId() {
        return referalId;
    }

    public void setReferalId(String referalId) {
        this.referalId = referalId;
    }

    public String getmPin() {
        return mPin;
    }

    public void setmPin(String mPin) {
        this.mPin = mPin;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getCashBack() {
        return cashBack;
    }

    public void setCashBack(String cashBack) {
        this.cashBack = cashBack;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResponceId() {
        return responceId;
    }

    public void setResponceId(String responceId) {
        this.responceId = responceId;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getApirefid() {
        return apirefid;
    }

    public void setApirefid(String apirefid) {
        this.apirefid = apirefid;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
}
