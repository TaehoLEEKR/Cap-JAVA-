package com.example.cap;

import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterInterface {
    //Retrofit

    String REGIST_URL = "http://3.34.255.8/Retrofit/"; //BASE_URL

    @FormUrlEncoded
    @POST("retrofit_reg.php") // POST

    Call<String> getUserRegist( // ID,PW,name,phone  //나머지 부분은 DB에서 NULL 처러
            @Field("userID") String userID,
            @Field("userPW") String userPW,
            @Field("name")  String name,
            @Field("phone") String phone
    );
}
