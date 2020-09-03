package biz.realpayment.Retrofit;

import biz.realpayment.Model.BankAmountResponse;
import biz.realpayment.Model.BankResponse;
import biz.realpayment.Model.ChangePass;
import biz.realpayment.Model.CustomerResponse;
import biz.realpayment.Model.LoginResponse;
import biz.realpayment.Model.Operator;
import biz.realpayment.Model.OperatorResponse;
import biz.realpayment.Model.Profile;
import biz.realpayment.Model.ProfileResponse;
import biz.realpayment.Model.RechargeResponse;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("/paybizapi/token")
    Call<JsonObject> Login(@Field("UserName") String userName,
                           @Field("Password") String Password,
                           @Field("grant_type") String grant_type);


    @GET("/paybizapi/api/UserMaster/Mobileno")
    Call<LoginResponse> checkMobileNumber(@Query("Mobileno") String mobileNumber);


    @POST("/paybizapi/api/UserMaster")
    Call<LoginResponse> Registration(@Body CustomerResponse customerResponse);


    @GET("/paybizapi/api/UserMaster/UserInfo")
    Call<ProfileResponse> getProfile(@HeaderMap Map<String, String> headers);


    @GET("/paybizapi/api/MobileRecharge/Oprator")
    Call<Operator> prepaidOperator(@Query("Category") String prepaid);


    @GET("/paybizapi/api/MobileRecharge/PrepaidRecharge")
    Call<RechargeResponse> rechargeMobile(@Query("mn") String customerNumber,
                                          @Query("amt") String amount,
                                          @Query("op") String operatorId,
                                          @Query("UserId") String userId);


    @POST("/api/payment_token")
    Call<BankResponse> getToken(@HeaderMap Map<String, String> headers,
                                @Body BankAmountResponse bankAmountResponse);


    @POST("/paybizapi/api/UserMaster/ChangePassword")
    Call<LoginResponse> changePassword(@Body ChangePass changePass);


    @PUT("/paybizapi/api/UserMaster/UpdateCustomer")
    Call<LoginResponse> addWallet(@HeaderMap Map<String, String> headers,
                                  @Query("id") String userId,
                                  @Body Profile profile);


    @GET("/v1/lookup/{number}")
    Call<OperatorResponse> getOCMobile(@Path("number") String mobileNumber);

}
