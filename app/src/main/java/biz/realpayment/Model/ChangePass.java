package biz.realpayment.Model;

import com.google.gson.annotations.SerializedName;

public class ChangePass {

    @SerializedName("Password")
    String password;

    @SerializedName("ConfirmPassword")
    String ConfirmPassword;

    @SerializedName("MobileNo")
    String MobileNo;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
