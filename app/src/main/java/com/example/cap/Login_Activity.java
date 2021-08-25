package com.example.cap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Login_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // 변수 부분
        EditText et_id = (EditText) findViewById(R.id.et_id);
        EditText et_pass = (EditText) findViewById(R.id.et_pass);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        Button btn_signup = (Button) findViewById(R.id.btn_signup);
        //

        btn_signup.setOnClickListener(new View.OnClickListener() { // 회원가입 클릭시 함수
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Signup_Activity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                Response.Listener<String> responseLisTener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { // 로그인 오류 예외처리
                        try {
                            System.out.println("Cap" + response);
                            JSONObject jsonObject = new JSONObject(response); // json rep객체 생성
                            boolean success = jsonObject.getBoolean("success"); // T/F
                            if (success) { // 로그인 성공
                                String userID = jsonObject.getString("userID");
                                String userpass = jsonObject.getString("userPass");

                                Toast.makeText(getApplicationContext(), "success Login", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login_Activity.this, Main_Activity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userpass", userPass);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Fail Login", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } // onResponse End
                }; // Response End
                LoginRequest loginRequest = new LoginRequest(userID,userPass,responseLisTener);
                RequestQueue queue = Volley.newRequestQueue(Login_Activity.this);
                queue.add(loginRequest);
            }
        }); // onClick End
    } // onCreate end

    private long backKeyPressedTime = 0;    // 두번째 뒤로가기 버튼을 눌렀던 시간을 저장
    private Toast toast;                    // 첫번째 뒤로가기 시 토스 던지기

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료합니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) { // 메인 엑티비티에서 종료시키기 위한 함수.
        super.onNewIntent(intent);
        boolean isKill = intent.getBooleanExtra("KILL_ACT", false);
        if (isKill)
            finish();
    }
}