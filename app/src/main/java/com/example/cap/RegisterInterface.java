package com.example.cap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterInterface {
    String REGIST_URL = "http://3.34.255.8/Retrofit/";

    @FormUrlEncoded
    @POST("retrofit_reg.php")
    Call<String> getUserRegist(
            @Field("userID") String userID,
            @Field("userPW") String userPW,
            @Field("name")  String name,
            @Field("phone") String phone
    );
}
