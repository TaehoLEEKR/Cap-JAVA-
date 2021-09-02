package com.example.cap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Information_Activitiy extends AppCompatActivity {

    private TextView infoID, infoName, infoPhone;
    private Button btn_Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        ArrayList<String> Datalist = new ArrayList<String>(); // 배열생성
        Datalist = getIntent().getStringArrayListExtra("Datalist");

        infoID = findViewById(R.id.infoID);
        infoName = findViewById(R.id.infoName);
        infoPhone = findViewById(R.id.infoPhone);
        btn_Logout = findViewById(R.id.btn_Logout);

        infoID.append(Datalist.get(0));
        infoName.append(Datalist.get(1));
        infoPhone.append(Datalist.get(3));

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Information_Activitiy.this,Login_Activitiy_Retrofit.class);
                startActivity(intent);
            }
        });

    }


}
