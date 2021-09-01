package com.example.cap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class Main_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        DrawerLayout layout_drawer = findViewById(R.id.layout_drawer);
        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_drawer.openDrawer(GravityCompat.END);
            }
        });
        NavigationView naviView = findViewById(R.id.naviView);
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if (item.getItemId() == R.id.info)
                    System.out.println("내 정보");
                    Intent intent = new Intent(Main_Activity.this, Information_Activitiy.class);
                    startActivity(intent);
                    Main_Activity.this.finish();
                if (item.getItemId() == R.id.device)
                    System.out.println("디바이스");
                if (item.getItemId() == R.id.deviceAdd)
                    System.out.println("장치 추가");
                layout_drawer.closeDrawers();
                return false;
            }
        });
    }   // onCreate end

    private long backKeyPressedTime = 0;    // 뒤로가기 버튼을 눌렀던 시간을 저장
    private Toast toast;                    // 첫번째 뒤로가기 시 토스 던지기

    @Override
    public void onBackPressed() {
        DrawerLayout layout_drawer = findViewById(R.id.layout_drawer);
        if (layout_drawer.isDrawerOpen(GravityCompat.END))
            layout_drawer.closeDrawers();
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