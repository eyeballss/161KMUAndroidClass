package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import helper.HttpConnection;
import helper.LocationService;
import helper.StaticManager;

/**
 * Don't forget:
 *  - makeing Log at method
 *  - TDD
 *  - makeing flow chart
 *  - naming rule
 *  - access modifier
 *  - exception
 *  - comments (//)
 *
 */


/**
 * Main screen that client sees first
 * A login screen that offers login.
 *
 */
public class LoginActivity extends AppCompatActivity {


    EditText idEditTxt, pwEditTxt;
    Button loginBtn;
    HttpConnection httpConnection ;
    LocationService locationService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        StaticManager.applicationContext=getApplicationContext(); //어플리케이션 콘텍스트 넘김.
        StaticManager.locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);//위치매니저 넘김.

        idEditTxt=(EditText)findViewById(R.id.idEditTxt);
        pwEditTxt=(EditText)findViewById(R.id.pwEditTxt);
        loginBtn=(Button)findViewById(R.id.loginBtn);
        httpConnection=new HttpConnection(); //http 컨넥터 만들기
        locationService = new LocationService(); //GPS 서비스 제공자 만들기


//        Intent in = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(in);

    }//onCreate


    //로그인 버튼 클릭하면
    public void loginBtnOnClick(View v){
        //연결을 시도함.
        String[] val= {"\""+idEditTxt.getText().toString()+"\"",pwEditTxt.getText().toString()};
        String[] key= {"id", "pw"};
        httpConnection.connect("http://52.79.106.222/eyeballs/db_save.php", "db_save.php",key, val);


//        Log.d("LoginActivity", "call http connection");
//        locationService.startLocationService(); //위치 정보 받아옵니다! 그래서 토스트로 출력! 이건 테스트 하고 있는 용도임.

    }//loginBtnOnClick






    //아래는 로컬 브로드캐스트

    //브로드캐스트 리시버
    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            final String message = intent.getStringExtra("db_save.php");

            //토스트 메세지로 테스트
            StaticManager.testToastMsg(message);

            Log.d("LoginActivity", "local broadcast receiver works");
        }
    };

    public void onResume() {
        super.onResume();
        //이것도 나중에 스태틱으로 바꿔주자. 여기서 특별히 다르게 처리해야 할 것은 없으니까.
        // Register mMessageReceiver to receive messages. 브로드캐스트 리시버 등록
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("localBroadCast"));
    }

    protected void onPause() {
        //이것도 나중에 스태틱으로 바꿔주자. 여기서 특별히 다르게 처리해야 할 것은 없으니까.
        // Unregister since the activity is not visible 브로드캐스트 리비서 해제
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        super.onPause();
    }
}