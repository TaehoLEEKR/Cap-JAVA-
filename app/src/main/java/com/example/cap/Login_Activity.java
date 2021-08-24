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
import android.widget.Toast;

import java.util.List;

public class Login_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Main_Activity.class);
                startActivity(intent);
            }
        });

        Button btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Signup_Activity.class);
                startActivity(intent);
            }
        });
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