package com.example.cap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.POST;

public class Main_Activity extends AppCompatActivity {

    private int img_sw = 0;
    private ImageButton img_btn, img_tv, img_air, img_beem ;
    private long backKeyPressedTime = 0;    // 뒤로가기 버튼을 눌렀던 시간을 저장
    private Toast toast;                    // 첫번째 뒤로가기 시 토스 던지기
    private Helper Helper;
    private ArrayList <String> ONDATALIST = new ArrayList();
    private ArrayList <String> OFFDATALIST = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Helper =  new Helper(this);

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

//        ImageButton Air = findViewById(R.id.air);
//        Air.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Main_Activity.this, AirActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        ImageButton TV = findViewById(R.id.tv);
//        TV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Main_Activity.this, TvActivity.class);
//                startActivity(intent);
//            }
//        });

//        ImageButton Beem = findViewById(R.id.beem);
//        Beem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Main_Activity.this, BeemActivity.class);
//                startActivity(intent);
//            }
//        });
        // 마지막거 만들면 추가할 예정.



        // 전원버튼 클릭 이미지 변경함수
        img_btn = findViewById(R.id.img_btn);
        img_tv = findViewById(R.id.tv);
        img_air = findViewById(R.id.air);
        img_beem = findViewById(R.id.beem);
//        img_btn_min = findViewById(R.id.btn_min);
//        img_btn_plus = findViewById(R.id.btn_plus);

        img_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final String AIRID = String.valueOf(img_btn.getId()); // 전원을 눌렀을때 아무 값이 전해지지 않아 일부러 버튼ID 값을 String으로 바꿔서 신호를 보냄
                final String AIROFF = String.valueOf(img_btn.getId());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        img_btn.setBackgroundResource(R.drawable.power_ing);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if(img_sw == 1) {
                            img_btn.setBackgroundResource(R.drawable.power_off); // 개빢치네 여기는 왜 안됨

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(OFF_interface.Post_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .build();
                            OFF_interface api = retrofit.create(OFF_interface.class);
                            Call<String> call = api.getOFFID(AIROFF);

                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.isSuccessful()){
                                        String jsonResponse = response.body();
                                        //System.out.println("ERROR2");
                                        parseDBdata(jsonResponse);
                                    }
                                }

                                private void parseDBdata(String response) {
                                    try
                                    {
                                        //  System.out.println("ERROR3");
                                        JSONObject jsonObject = new JSONObject((response));
                                        // System.out.println("ERROR4");
                                        if(jsonObject.getString("status").equals("true"))
                                        {
                                            //   System.out.println("ERROR5");
                                            saveData(response);
                                        }
                                    } catch (JSONException e) { e.printStackTrace();}
                                }

                                private void saveData(String response) {
                                    Helper.PutIsData(true);
                                    //System.out.println("ERROR7");
                                    try{
                                        //  System.out.println("ERROR8");
                                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                        System.out.println(jsonObject);
                                        //System.out.println("ERROR9");
                                        if(jsonObject.getString("status").equals("true")){
                                            //  System.out.println("ERROR10");
                                            JSONArray dataArray = jsonObject.getJSONArray("data");
                                            // System.out.println("ERROR11");
                                            for(int i = 0; i<dataArray.length(); i++){
                                                JSONObject dataobj = dataArray.getJSONObject(i);
                                                System.out.println("ERROR12");
                                                System.out.println(dataobj);
                                                //Helper.putAIRID(dataobj.getString(AIRID));
                                                // System.out.println("ERROR13");\
                                                OFFDATALIST.add((String) dataobj.get("AIROFF"));
                                                for(String j : OFFDATALIST) { System.out.println(j); }
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable throwable) {

                                }
                            });

                            img_sw = 0;
                        }
                        else {
                            img_btn.setBackgroundResource(R.drawable.power);
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(PostAPI.Post_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .build();
                            PostAPI api = retrofit.create(PostAPI.class);
                            Call<String> call = api.getONID(AIRID);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(@NonNull Call <String> call, @NonNull  Response<String> response) {
                                    if(response.isSuccessful()){
                                        String jsonResponse = response.body();
                                        //System.out.println("ERROR2");
                                        parseDBdata(jsonResponse);
                                    }
                                }   @Override
                                public void onFailure(Call<String> call, Throwable throwable) {
                                    throwable.printStackTrace();
                                    //System.out.println("ERORR");
                                }
                                private void parseDBdata(String response) {
                                    try
                                    {
                                      //  System.out.println("ERROR3");
                                        JSONObject jsonObject = new JSONObject((response));
                                       // System.out.println("ERROR4");
                                        if(jsonObject.getString("status").equals("true"))
                                        {
                                         //   System.out.println("ERROR5");
                                            saveData(response);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                private void saveData(String response) {
                                    //System.out.println("ERROR6");
                                    Helper.PutIsData(true);
                                    //System.out.println("ERROR7");
                                    try{
                                      //  System.out.println("ERROR8");
                                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                        System.out.println(jsonObject);
                                        //System.out.println("ERROR9");
                                        if(jsonObject.getString("status").equals("true")){
                                          //  System.out.println("ERROR10");
                                            JSONArray dataArray = jsonObject.getJSONArray("data");
                                           // System.out.println("ERROR11");
                                            for(int i = 0; i<dataArray.length(); i++){
                                                JSONObject dataobj = dataArray.getJSONObject(i);
                                             //System.out.println("ERROR12");
                                                System.out.println(dataobj);
                                                //Helper.putAIRID(dataobj.getString(AIRID));
                                               // System.out.println("ERROR13");\
                                                ONDATALIST.add((String) dataobj.get("AIRON"));
                                                        for(String j : ONDATALIST) { System.out.println(j); }
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            img_sw = 1;
                        }
                        break;
                    }

                }   // switch end
                return true; // 안써주면 반환형오류떠서 써줌!
            }
        });

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
                        Intent intent = new Intent(Main_Activity.this, AirActivity.class);
                        startActivity(intent);
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
                        Intent intent = new Intent(Main_Activity.this, TvActivity.class);
                        startActivity(intent);
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
                        Intent intent = new Intent(Main_Activity.this, BeemActivity.class);
                        startActivity(intent);
                        break;
                    }
                }  // switch end
                return true;
            }
        });


//        img_btn_min.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN:{
//                        img_btn_min.setBackgroundResource(R.drawable.min_w);
//                        break;
//                    }
//                    case MotionEvent.ACTION_UP:{
//                        img_btn_min.setBackgroundResource(R.drawable.min);
//                        if(img_sw == 1) {
//                            if (number > 18)
//                                number -= 1;
//                            temp.setText(number + "℃");
//                        }
//                        break;
//                    }
//                }
//                return true;
//            }
//        });
//        img_btn_plus.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN:{
//                        img_btn_plus.setBackgroundResource(R.drawable.plus_w);
//                        break;
//                    }
//                    case MotionEvent.ACTION_UP:{
//                        img_btn_plus.setBackgroundResource(R.drawable.plus);
//                        if(img_sw == 1) {
//                            if (number < 30)
//                                number += 1;
//                            temp.setText(number + "℃");
//                        }
//                        break;
//                    }
//                }
//                return true;
//            }
//        });
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