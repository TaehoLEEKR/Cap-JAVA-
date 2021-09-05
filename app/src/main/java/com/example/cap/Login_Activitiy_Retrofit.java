package com.example.cap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login_Activitiy_Retrofit extends AppCompatActivity {

    // ------------------------------  변수 설정 부분 ---------------------------------------------

    private EditText et_id,et_pass;
    private Button btn_login;
    private Button btn_signup;
    private Helper Helper;
    private long backKeyPressedTime = 0;    // 뒤로가기 버튼을 눌렀던 시간을 저장
    private Toast toast;                    // 첫번째 뒤로가기 시 토스 던지기
    private ArrayList <String> Datalist = new ArrayList() ; // 데이터들을 리스트 안에 저장 하기위한 변수

    // ------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // ------------------------------레이아웃 id 변수 설정 부분 ---------------------------------

        Helper = new Helper(this);
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        // --------------------------------------------------------------------------------------


        // 로그인 , 회원가입 버튼을 눌렀을 때
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activitiy_Retrofit.this, Register_Activity_Retrofit.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(); // -> 로그인 눌렀을때 로그인 user 함수로
            }
        });

        // btn_signup , btn_login  -=> setOnClickListener()

    }

    private void loginUser(){ // loginUser Start

        // ------------레이아웃 변수를 스트링 타입으로 받아온다 ----------------------------------------
        final String ID = et_id.getText().toString().trim();
        final String PW = et_pass.getText().toString().trim();
        // --------------------------------------------------------------------------------------


        // retrofit  API 로그인인터페이스에 들어가서 baseURL인 로그인URL로 들어가서 php에 전달할 준비
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginInterface.LOGIN_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();


        System.out.println("\nID : "+ ID +"\tPW :" +PW);

        LoginInterface api = retrofit.create(LoginInterface.class);

        Call<String> call = api.getUserLogin(ID, PW); //로그인 인터페이스에 getUserLogin 에 ID,PW를 DBphp에 전달

        //enqueue 를 통해 결과값을 Callback 으로 넘겨받는다
        call.enqueue(new Callback<String>()
        {
            // 통신을 하였을 경우 값을 돌려 받는 부분 onResponse
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response)
            {
                if (response.isSuccessful() && response.body() != null) //통신이 성공하거나 비어있지않는경우
                {
                    System.out.println("\non Success - > " + response.body());
                    String jsonResponse = response.body(); // String jsonResponse 에 스트링타입으로 저장한다
                    parseLoginData(jsonResponse); //parseLoginData () 함수에 jsonResponse 에 데이터를 전달
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            //로그인 실패
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t)
            {
                Log.e("에러 = " , t.getMessage());
            }

        });

    } //    loginUser () end
    
    private void parseLoginData(String response) // parseLoginData Start
    {
        System.out.println("\nresponse - > "+response);
        try
        {
            JSONObject jsonObject = new JSONObject(response); //JSONObject 형식에 변수를 만들어 response를 받는다
            if (jsonObject.getString("status").equals("true")) // 만약 php status가 true 이면
            {
                saveInfo(response); // svaeInfo () 함수에 response 를 전달

                Toast.makeText(Login_Activitiy_Retrofit.this, "Login Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Login_Activitiy_Retrofit.this, Main_Activity.class);
                intent.putExtra("Datalist", Datalist); // DataList 객체배열리스트에 Lgoin DB를 전달
                startActivity(intent);
            }
            else { Toast.makeText(Login_Activitiy_Retrofit.this, "Login Fail!", Toast.LENGTH_SHORT).show(); }
        }
        catch (JSONException e)     { e.printStackTrace(); }
    } // parseLoginData end


    private void saveInfo(String response) { //saveInfo Start
        Helper.putIsLogin(true); //Helper 클래스에서 True bool을 전달
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true"))
            {
                // JSONArray 형식에 변수 dataArray 생성
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) // for 반복문을 통해 dataArray길이 만큼 반복을 하면서 JSONObject 형식으로 배열을 저장
                {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    // 배열 저장 체크
                    System.out.println("Data - >"+dataobj);
                    System.out.println("Data Type" + dataobj.getClass().getName());
                    System.out.println("jsonobject get? "+ dataobj.get("userID") + "\tjsonobject Type ? " + dataobj.get("userID").getClass().getName());
                    //

                    // Datalist 에 LoginDB를 저장
                    Datalist.add((String) dataobj.get("userID"));
                    Datalist.add((String) dataobj.get("name"));
                    Datalist.add((String) dataobj.get("userPW"));
                    Datalist.add((String) dataobj.get("phone"));
                    //

                    for (String j : Datalist){ System.out.println(j);} // 빠른 for문으로 저장된 데이터 출력체크

                    System.out.println(Datalist.get(0)+ " " +Datalist.get(1)+ " "+Datalist.get(2)+ " "+Datalist.get(3)+ " "); // 배열 인덱스 체크

                    //Helper 클레스에 userID ,userPW 를 전달
                    Helper.putID(dataobj.getString("userID"));
                    Helper.putPW(dataobj.getString("userPW"));

                }
            }
        }
        catch (JSONException e) { e.printStackTrace(); }
    } //saveInfo end


    // 뒤로가기 구현함수
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000)
        {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료합니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000)
        {
            finish();
            toast.cancel();
        }
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        boolean isKill = intent.getBooleanExtra("KILL_ACT", false);
        if(isKill) { finish();}
    }
}

