package com.example.cap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Main_Activity extends AppCompatActivity {
    // -----------------------------------변수 선언 부분 -------------------------------------//
    private int img_sw = 0;
    private ImageButton img_btn, img_tv, img_air, img_beem ;
    private Button send_btn;
    private EditText InputTemperature;
    private long backKeyPressedTime = 0;    // 뒤로가기 버튼을 눌렀던 시간을 저장
    private Toast toast;                    // 첫번째 뒤로가기 시 토스 던지기
    private Helper Helper;
    private ArrayList <String> ONDATALIST = new ArrayList();
    private ArrayList <String> OFFDATALIST = new ArrayList();

    // -----------------------------------레트로핏 선언 부분 -------------------------------------//

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(P_INTER.Post_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
    // ---------------------------------------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //--------------------- 버튼을 눌렀을 때 필요한 버튼들의 변수들을 생성-------------------------//
        Helper =  new Helper(this);
        DrawerLayout layout_drawer = findViewById(R.id.layout_drawer); // 메인화면 레이아웃
        ImageButton imageButton = findViewById(R.id.imageButton); // 메뉴바 이미지 버튼 변수
        ArrayList<String> Datalist = new ArrayList<String>();    // 로그인의 정보를 담을 배열 변수
        Datalist = getIntent().getStringArrayListExtra("Datalist");
        NavigationView naviView = findViewById(R.id.naviView); // 네비게이션 변수
        ArrayList<String> finalDatalist = Datalist;
        img_btn = findViewById(R.id.img_btn);
        img_tv = findViewById(R.id.tv);
        img_air = findViewById(R.id.air);
        img_beem = findViewById(R.id.beem);
        InputTemperature = findViewById(R.id.Temperature);
        send_btn = findViewById(R.id.SEND_btn);

        // ---------------------------------------------------------------------------------------//


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_drawer.openDrawer(GravityCompat.END);
            }
        });

        // ---------------------------------------------------------------------------------------//

        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if (item.getItemId() == R.id.info) {
                    Intent intent = new Intent(Main_Activity.this, Information_Activitiy.class);
                    intent.putExtra("Datalist", finalDatalist);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.device)
                if (item.getItemId() == R.id.deviceAdd)
                layout_drawer.closeDrawers();
                return false;
            }
        });
        // ---------------------------------------------------------------------------------------//

        // 전원버튼 클릭 이미지 변경함수

        // ---------------------------------메인화면의 전원 버튼을 눌렀을 때-------------------------------------//
        img_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String AIR;         // 레트로핏의 줄 신호
                String Temperature; //

                switch (event.getAction()) { // switch~ case ON - ING - OFF 상황

                    case MotionEvent.ACTION_DOWN: {
                        img_btn.setBackgroundResource(R.drawable.power_ing);
                        break;
                    } // ING 누르고 있는 상황

                    case MotionEvent.ACTION_UP: { // 전원이 꺼져 있는 경우
                        if(img_sw == 1) {
                            img_sw = 0;
                            AIR = new String("OFF");        // Post OFF
                            Temperature = new String("NULL");
                            img_btn.setBackgroundResource(R.drawable.power_off);
                        }
                        else {

                            AIR = new String("ON");        // Post ON
                            Temperature = new String(InputTemperature.getText().toString().trim()); // 온도 변수

                            // ----------------------------------------예외처리--------------------------------------//
                            try { //18~30 도사이의 온도를 입력하고 예외의 경우를 처리해주는 부분
                                if (Integer.parseInt(Temperature) < 18 || Integer.parseInt(Temperature) > 30) {
                                    Toast.makeText(Main_Activity.this, "Error : 18~30℃사이의 온도를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    InputTemperature.setText("");
                                    InputTemperature.setHint("Temperature");
                                    img_btn.setBackgroundResource(R.drawable.power_off);
                                    break;
                                }
                            }
                            catch (NumberFormatException e){
                                Toast.makeText(Main_Activity.this, "Error : 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                                InputTemperature.setText("");
                                InputTemperature.setHint("Temperature");
                                img_btn.setBackgroundResource(R.drawable.power_off);
                                break;
                            }
                            // --------------------------------------문자를 입력했을 경우의 예외처리------------------------------------------//
                            img_sw = 1;
                            img_btn.setBackgroundResource(R.drawable.power);
                        }
                        POSTDATA(AIR, Temperature); // P_INTER 에 POST 해줄 함수
                        break;
                    }

                }   // switch end
                return true;
            }
        });
        // ---------------------------메인화면의 3개의 이미지 버튼들 --------------------------------//
        img_air.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        img_air.setBackgroundResource(R.color.back);
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        img_air.setBackgroundResource(R.color.white);
                        Toast.makeText(Main_Activity.this, "Developing!!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }   // switch end
                return true;
            }
        });

        img_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        img_tv.setBackgroundResource(R.color.back);
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        img_tv.setBackgroundResource(R.color.white);
                        Toast.makeText(Main_Activity.this, "Developing!!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }  // switch end
                return true;
            }
        });

        img_beem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        img_beem.setBackgroundResource(R.color.back);
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        img_beem.setBackgroundResource(R.color.white);
                        Toast.makeText(Main_Activity.this, "Developing!!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }  // switch end
                return true;
            }
        });
        // ---------------------------메인화면의 3개의 이미지 버튼들 --------------------------------//


        // ---------------------------온도를 보내줄 SEND 버튼 함수--------------------------------//
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String AIR = new String("ON");
                String Temperature;
                if(img_sw == 1){
                    try {
                        Temperature = new String(InputTemperature.getText().toString().trim());

                        if (Integer.parseInt(Temperature) < 18 || Integer.parseInt(Temperature) > 30) {
                            Toast.makeText(Main_Activity.this, "Error : 18~30℃사이의 온도를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            InputTemperature.setText("");
                            InputTemperature.setHint("Temperature");
                            return;
                        }

                        POSTDATA(AIR, Temperature);
                    }
                    catch (NumberFormatException e){
                        Toast.makeText(Main_Activity.this, "Error : 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        InputTemperature.setText("");
                        InputTemperature.setHint("Temperature");
                        return;
                    }
                }
            }
        });
    }   // onCreate end


    void POSTDATA(String AIR, String Temperature){
        P_INTER API = retrofit.create(P_INTER.class);
        Call<String> call = API.getID(AIR, Temperature);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String jsonResponse = response.body();
                    parseDBdata(jsonResponse);
                }
            }

            private void parseDBdata(String response) {
                try
                {
                    JSONObject jsonObject = new JSONObject((response));
                    if(jsonObject.getString("status").equals("true"))
                    {
                        saveData(response);
                    }
                } catch (JSONException e) { e.printStackTrace();}
            }

            private void saveData(String response) {
                Helper.PutIsData(true);
                try{
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    System.out.println(jsonObject);
                    if(jsonObject.getString("status").equals("true")){
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                    }

                } catch (JSONException e) { e.printStackTrace(); }
            }
            @Override
            public void onFailure(Call<String> call, Throwable throwable) { }
        });

    }



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