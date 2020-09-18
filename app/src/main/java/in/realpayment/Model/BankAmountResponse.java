package in.realpayment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankAmountResponse {


    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("WalletBalance")
    @Expose
    private String WalletBalance;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("mtx")
    @Expose
    private String mtx;


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getWalletBalance() {
        return WalletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        WalletBalance = walletBalance;
    }
}