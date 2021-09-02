package com.example.cap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login_Activitiy_Retrofit extends AppCompatActivity {
    private final String TAG ="Login_Activitiy_Retrofit";

    private EditText et_id,et_pass;
    private Button btn_login;
    private Button btn_signup;
    private Helper Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Helper = new Helper(this);

        et_id = (EditText) findViewById(R.id.et_id);
        et_pass = (EditText) findViewById(R.id.et_pass);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activitiy_Retrofit.this, Register_Activity_Retrofit.class);
                startActivity(intent);
                Login_Activitiy_Retrofit.this.finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser(){
        final String ID = et_id.getText().toString().trim();
        final String PW = et_pass.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginInterface.LOGIN_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        System.out.println("\nID : "+ ID +"\tPW :" +PW);
        LoginInterface api = retrofit.create(LoginInterface.class);
        Call<String> call = api.getUserLogin(ID, PW);
        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    System.out.println("\non Success - > " + response.body());
                    //Log.e("onSuccess", response.body());
                    String jsonResponse = response.body();
                    parseLoginData(jsonResponse);
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t)
            {
                Log.e("에러 = " , t.getMessage());
            }
        });
    }
    private void parseLoginData(String response)
    {
        System.out.println("\nresponse - > "+response);
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true"))
            {
                saveInfo(response);

                Toast.makeText(Login_Activitiy_Retrofit.this, "Login Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Login_Activitiy_Retrofit.this, Main_Activity.class);
                startActivity(intent);

            }
            else{
                Toast.makeText(Login_Activitiy_Retrofit.this, "Login Fail!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void saveInfo(String response) {
            Helper.putIsLogin(true);
        ArrayList <String> Datalist = new ArrayList() ; // 데이터들을 리스트 안에 저장 하기위한 변수
        try
        {

            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true"))
            {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++)
                {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    System.out.println("Data - >"+dataobj);
                    System.out.println("Data Type" + dataobj.getClass().getName());
                    System.out.println("jsonobject get? "+ dataobj.get("userID") + "\tjsonobject Type ? " + dataobj.get("userID").getClass().getName());

                    Datalist.add((String) dataobj.get("userID"));
                    Datalist.add((String) dataobj.get("name"));
                    Datalist.add((String) dataobj.get("userPW"));
                    Datalist.add((String) dataobj.get("phone"));

                    for (String j : Datalist){ System.out.println(j);}

                    System.out.println(Datalist.get(0)+ " " +Datalist.get(1)+ " "+Datalist.get(2)+ " "+Datalist.get(3)+ " ");

                    Helper.putID(dataobj.getString("userID"));
                    Helper.putPW(dataobj.getString("userPW"));

                }
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private long backKeyPressedTime = 0;    // 뒤로가기 버튼을 눌렀던 시간을 저장
    private Toast toast;                    // 첫번째 뒤로가기 시 토스 던지기

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료합니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        boolean isKill = intent.getBooleanExtra("KILL_ACT", false);
        if(isKill)
            finish();
    }
}

