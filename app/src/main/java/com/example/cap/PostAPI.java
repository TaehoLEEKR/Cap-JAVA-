package com.example.cap;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PostAPI {
    //Retrofit php 와 통신

    String Post_URL = "http://3.34.255.8/Retrofit/"; // BASE_URL

    @FormUrlEncoded
    @POST("ONad.php") // post 형식으로 php 웹서버에 전달

    Call<String> getONID(
            @Field("AIRON") String AIRON
    );
}
