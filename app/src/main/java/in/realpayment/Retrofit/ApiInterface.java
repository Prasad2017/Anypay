package in.realpayment.Retrofit;

import in.realpayment.Model.BankAmountResponse;
import in.realpayment.Model.BankResponse;
import in.realpayment.Model.BannerResponse;
import in.realpayment.Model.ChangePass;
import in.realpayment.Model.ContactListResponse;
import in.realpayment.Model.CustomerResponse;
import in.realpayment.Model.HistoryResponseList;
import in.realpayment.Model.LoginResponse;
import in.realpayment.Model.Operator;
import in.realpayment.Model.OperatorResponse;
import in.realpayment.Model.Profile;
import in.realpayment.Model.ProfileResponse;
import in.realpayment.Model.RechargeResponse;
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
    @POST("/realapi/token")
    Call<JsonObject> Login(@Field("UserName") String userName,
                           @Field("Password") String Password,
                           @Field("grant_type") String grant_type);


    @GET("/realapi/api/UserMaster/Mobileno")
    Call<LoginResponse> checkMobileNumber(@Query("Mobileno") String mobileNumber);


    @POST("/realapi/api/UserMaster")
    Call<LoginResponse> Registration(@Body CustomerResponse customerResponse);


    @GET("/realapi/api/UserMaster/UserInfo")
    Call<ProfileResponse> getProfile(@HeaderMap Map<String, String> headers);


    @GET("/realapi/api/MobileRecharge/Oprator")
    Call<Operator> prepaidOperator(@Query("Category") String prepaid);


    @GET("/realapi/api/MobileRecharge/PrepaidRecharge")
    Call<RechargeResponse> rechargeMobile(@Query("mn") String customerNumber,
                                          @Query("amt") String amount,
                                          @Query("op") String operatorId,
                                          @Query("UserId") String userId,
                                          @Query("Category") String category);


    @GET("/realapi/api/MobileRecharge/UPIRecharge")
    Call<RechargeResponse> upiRechargeMobile(@Query("mn") String customerNumber,
                                             @Query("amt") String amount,
                                             @Query("op") String operatorId,
                                             @Query("UserId") String userId,
                                             @Query("Category") String category);


    @POST("/api/payment_token")
    Call<BankResponse> getToken(@HeaderMap Map<String, String> headers,
                                @Body BankAmountResponse bankAmountResponse);


    @POST("/realapi/api/UserMaster/ChangePassword")
    Call<LoginResponse> changePassword(@Body ChangePass changePass);


    @PUT("/realapi/api/UserMaster/UpdateCustomer")
    Call<LoginResponse> addWallet(@HeaderMap Map<String, String> headers,
                                  @Query("id") String userId,
                                  @Body Profile profile);


    @GET("/v1/lookup/{number}")
    Call<OperatorResponse> getOCMobile(@Path("number") String mobileNumber);


    @PUT("/realapi/api/UserMaster/UpdateCustomerProfile")
    Call<LoginResponse> updateProfile(@HeaderMap Map<String, String> headers,
                                      @Query("id") String userId,
                                      @Body Profile profile);


    @GET("/realapi/api/QRCode")
    Call<RechargeResponse> qrCodePayment(@Query("SenderUserId") String SenderUserId,
                                         @Query("ReceiverUserId") String ReceiverUserId,
                                         @Query("SenderName") String SenderName,
                                         @Query("RecName") String RecName,
                                         @Query("amt") String amount);


    @GET("/realapi/api/MobileRecharge/ResponceHistory")
    Call<HistoryResponseList> getHistoryList(@HeaderMap Map<String, String> headers,
                                             @Query("pageNo") String pageNo,
                                             @Query("pageSize") String pageSize,
                                             @Query("UserId") String userId);


    @GET("/realapi/api/Banner")
    Call<BannerResponse> getBannerList(@HeaderMap Map<String, String> headers,
                                       @Query("pageNo") String pageNo,
                                       @Query("pageSize") String pageSize);


    @GET("/realapi/api/UserMaster")
    Call<ContactListResponse> getRealPaymentList(@HeaderMap Map<String, String> headers,
                                                 @Query("pageNo") String pageNo,
                                                 @Query("pageSize") String pageSize,
                                                 @Query("Category") String category);


    @GET("/realapi/api/MobileRecharge/ResponceHistory")
    Call<HistoryResponseList> getRequestIdDetails(@HeaderMap Map<String, String> headers,
                                                  @Query("pageNo") String pageNo,
                                                  @Query("pageSize") String pageSize,
                                                  @Query("Category") String category);
}
