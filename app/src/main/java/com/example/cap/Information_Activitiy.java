package com.example.cap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Information_Activitiy extends AppCompatActivity {

    private TextView infoID, infoName, infoPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        ArrayList<String> Datalist = new ArrayList<String>(); // 배열생성
        Datalist = getIntent().getStringArrayListExtra("Datalist");

        infoID = findViewById(R.id.infoID);
        infoName = findViewById(R.id.infoName);
        infoPhone = findViewById(R.id.infoPhone);

        infoID.append(Datalist.get(0));
        infoName.append(Datalist.get(1));
        infoPhone.append(Datalist.get(3));
    }
}
