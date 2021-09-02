package com.example.cap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Information_Activitiy extends AppCompatActivity {

    Login_Activitiy_Retrofit LAR = new Login_Activitiy_Retrofit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
    }

}
