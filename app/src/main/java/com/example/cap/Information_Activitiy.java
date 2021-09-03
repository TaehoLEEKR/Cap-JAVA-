package com.example.cap;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Information_Activitiy extends AppCompatActivity {

    Login_Activitiy_Retrofit LAR = new Login_Activitiy_Retrofit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        ArrayList<String> Datalist = new ArrayList<String>(); // 배열생성
        Datalist = getIntent().getStringArrayListExtra("Datalist");
        System.out.println(Datalist);
        System.out.println(getIntent().getStringArrayListExtra("Datalist"));

    }
}
