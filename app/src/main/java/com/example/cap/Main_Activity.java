package com.example.cap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Main_Activity extends AppCompatActivity {

    private int img_sw = 0;
    private ImageButton img_btn, img_btn_plus,  img_btn_min ;
    private long backKeyPressedTime = 0;    // 뒤로가기 버튼을 눌렀던 시간을 저장
    private Toast toast;                    // 첫번째 뒤로가기 시 토스 던지기
    public int number=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayList<String> Datalist = new ArrayList<String>();
        Datalist = getIntent().getStringArrayListExtra("Datalist");

        DrawerLayout layout_drawer = findViewById(R.id.layout_drawer);
        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_drawer.openDrawer(GravityCompat.END);
            }
        });
        NavigationView naviView = findViewById(R.id.naviView);

        ArrayList<String> finalDatalist = Datalist;
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if (item.getItemId() == R.id.info) {
                    System.out.println("내 정보");
                    Intent intent = new Intent(Main_Activity.this, Information_Activitiy.class);
                    intent.putExtra("Datalist", finalDatalist);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.device)
                    System.out.println("디바이스");
                if (item.getItemId() == R.id.deviceAdd)
                    System.out.println("장치 추가");
                layout_drawer.closeDrawers();
                return false;
            }
        });

        // 전원버튼 클릭 이미지 변경함수
        img_btn = findViewById(R.id.img_btn);
        img_btn_min = findViewById(R.id.btn_min);
        img_btn_plus = findViewById(R.id.btn_plus);
        TextView temp = findViewById(R.id.display_temp);
        CharSequence b = temp.getText();
        System.out.println(b);

        img_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        img_btn.setBackgroundResource(R.drawable.power_ing);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if(img_sw == 1) {
                            img_btn.setBackgroundResource(R.drawable.power_off);
                            temp.setText("OFF");
                            img_sw = 0;
                        }
                        else {
                            img_btn.setBackgroundResource(R.drawable.power);
                            temp.setText(number + "℃");
                            img_sw = 1;
                        }
                        break;
                    }

                }   // switch end
                return true; // 안써주면 반환형오류떠서 써줌!
            }
        });

        img_btn_min.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        img_btn_min.setBackgroundResource(R.drawable.min_w);
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        img_btn_min.setBackgroundResource(R.drawable.min);
                        if(img_sw == 1) {
                            if (number > 18)
                                number -= 1;
                            temp.setText(number + "℃");
                        }
                        break;
                    }
                }
                return true;
            }
        });
        img_btn_plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        img_btn_plus.setBackgroundResource(R.drawable.plus_w);
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        img_btn_plus.setBackgroundResource(R.drawable.plus);
                        if(img_sw == 1) {
                            if (number < 30)
                                number += 1;
                            temp.setText(number + "℃");
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }   // onCreate end



    @Override
    public void onBackPressed() {
        DrawerLayout layout_drawer = findViewById(R.id.layout_drawer);
        if (layout_drawer.isDrawerOpen(GravityCompat.END)) {
            layout_drawer.closeDrawers();
        }
        else if (System.currentTimeMillis() > backKeyPressedTime + 2000) {

            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료합니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;

        } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
            Intent intent = new Intent(Main_Activity.this, Login_Activitiy_Retrofit.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("KILL_ACT", true);
            startActivity(intent);
        }
    }
}