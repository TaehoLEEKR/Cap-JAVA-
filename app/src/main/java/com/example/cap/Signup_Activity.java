package com.example.cap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class Signup_Activity extends AppCompatActivity {
    //변수 부분
    private EditText et_id,et_pass,et_name,et_phone, et_pass2;
    private Button btn_signup;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //ID search

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_pass2 = findViewById(R.id.et_pass2);
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);

        // signup btn click

        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userPass2 = et_pass2.getText().toString();
                String username = et_name.getText().toString();
                String userphone = et_phone.getText().toString();


                if (!userPass.equals(userPass2)) { // 비밀번호 체크
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {

                    Response.Listener<String> responseLisTener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) { // 회원가입 오류 예외처리
                            try {
                                JSONObject jsonObject = new JSONObject(response); // json rep객체 생성
                                boolean success = jsonObject.getBoolean("Success"); // T/F
                                if (success) { // 회원가입 등록 성공

                                    Toast.makeText(getApplicationContext(), "Success Signup", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Signup_Activity.this, Login_Activity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Fail Sginup", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } // onResponse End
                    }; // Response End
                    RegisterRequest registerRequest = new RegisterRequest(userID, userPass, username, userphone, responseLisTener);
                    RequestQueue queue = Volley.newRequestQueue(Signup_Activity.this);
                    queue.add(registerRequest);
                }
            }
        });



    }
}