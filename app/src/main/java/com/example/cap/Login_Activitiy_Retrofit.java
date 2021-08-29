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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        LoginInterface api = retrofit.create(LoginInterface.class);
        Call<String> call = api.getUserLogin(ID, PW);
        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    Log.e("onSuccess", response.body());

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
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true"))
            {
                saveInfo(response);

                Toast.makeText(Login_Activitiy_Retrofit.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void saveInfo(String response) {
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
                    Helper.putID(dataobj.getString("name"));
                    Helper.putName(dataobj.getString("hobby"));
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


}

