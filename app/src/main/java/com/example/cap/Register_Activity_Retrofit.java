package com.example.cap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Tag;

public class Register_Activity_Retrofit extends AppCompatActivity {
    //Login_Activitiy_Retrofit 기반


    // -------------------------변수 저장 부분-----------------------------------------------------
    private EditText et_id, et_phone, et_name, et_pass, et_pass2;
    private Button btn_signup;
    private Helper Helper;
    private long backKeyPressedTime = 0;    // 뒤로가기 버튼을 눌렀던 시간을 저장
    // ------------------------------------------------------------------------------------------
    //레트로핏 변수 생성
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(RegisterInterface.REGIST_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    //-----------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Helper = new Helper(this);

        // ------------------------------레이아웃 id 변수 설정 부분 ---------------------------------
        et_id =  findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_pass2 = findViewById(R.id.et_pass2);
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        btn_signup =findViewById(R.id.btn_signup);
        // --------------------------------------------------------------------------------------

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_pass.getText().toString().equals(et_pass2.getText().toString())){
                    Toast.makeText(Register_Activity_Retrofit.this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerMe();
                }
            }
        });
    }

    private void registerMe() {
        // 회원가입 정보 변수 String형태로 지정
        final String ID = et_id.getText().toString();
        final String PW = et_pass.getText().toString();
        final String Name = et_name.getText().toString();
        final String Phone = et_phone.getText().toString();
        //----------------------------------------------------------------------------------------

        RegisterInterface api = retrofit.create(RegisterInterface.class);
        Call<String> call = api.getUserRegist(ID, PW, Name, Phone);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    Log.e("onSuccess", response.body());
                    String jsonResponse = response.body();

                    try { parseRegData(jsonResponse); } catch (JSONException e) { e.printStackTrace(); }
                }
            }
            @Override
            public void onFailure(@NonNull Call<String>call, Throwable t) { Log.e("Error = %s " , t.getMessage()); }
        });
    }

     private void parseRegData(String response) throws JSONException // 데이터 JSON 파싱
     {
         JSONObject jsonObject = new JSONObject(response);
         if (jsonObject.optString("status").equals("true"))
         {
             saveInfo(response);
             Toast.makeText(Register_Activity_Retrofit.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
             finish();
             Intent intent = new Intent(Register_Activity_Retrofit.this, Login_Activitiy_Retrofit.class);
             startActivity(intent);
         }
         else
         {
             Toast.makeText(Register_Activity_Retrofit.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
         }
     }

    private void saveInfo(String response)
    {
        Helper.putIsLogin(true);
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true"))
            {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++)
                {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    Helper.putID(dataobj.getString("userID"));
                    Helper.putPW(dataobj.getString("userPW"));
                }
            }
        }
        catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            return;
        } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }
}
