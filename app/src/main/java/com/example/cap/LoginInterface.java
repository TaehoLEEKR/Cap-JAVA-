package com.example.cap;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {
        //Retrofit php 와 통신

        String LOGIN_URL = "http://3.34.255.8/Retrofit/"; // BASE_URL

        @FormUrlEncoded
        @POST("retrofit_login.php") // post 형식으로 php 웹서버에 전달

        Call<String> getUserLogin( // ID 와 PW 데이터 전달
                @Field("userID") String userID,
                @Field("userPW") String userPW
        );
}
