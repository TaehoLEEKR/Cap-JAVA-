package com.example.cap;
import retrofit2.Call;import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {

        String LOGIN_URL = "http://3.34.255.8/Retrofit/";

        @FormUrlEncoded
        @POST("retrofit_login.php")

        Call<String> getUserLogin(
                @Field("userID") String userID,
                @Field("userPW") String userPW
        );
}
