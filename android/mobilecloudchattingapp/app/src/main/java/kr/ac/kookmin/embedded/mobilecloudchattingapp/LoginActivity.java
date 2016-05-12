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

import java.util.StringTokenizer;

import helper.DataSaver;
import helper.HttpConnection;
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
//    LocationService locationService; //여기서 GPS는 실험용이었음. 사실 로그인 화면에 GPS가 있을 이유가 없다.

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        StaticManager.applicationContext=getApplicationContext(); //어플리케이션 콘텍스트 넘김.
        StaticManager.locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);//위치매니저 넘김.

        idEditTxt=(EditText)findViewById(R.id.idEditTxt);
        pwEditTxt=(EditText)findViewById(R.id.pwEditTxt);
        loginBtn=(Button)findViewById(R.id.loginBtn);
        httpConnection=new HttpConnection(); //http 컨넥터 만들기
//        locationService = new LocationService(); //GPS 서비스 제공자 만들기


//        Intent in = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(in);

    }//onCreate


    //로그인 버튼 클릭하면
    public void loginBtnOnClick(View v){

        String idTxt = idEditTxt.getText().toString().trim();
        String pwTxt = pwEditTxt.getText().toString().trim();

        if(idTxt.length()==0) {
            StaticManager.testToastMsg("ID is empty!");
            return;
        }
        if(pwTxt.length()==0) {
            StaticManager.testToastMsg("PW is empty!");
            return;
        }
        //연결을 시도함.
        //아이디+비밀번호 문자열을 해쉬코드로 넘김.
        int idpwHashCode = (idTxt+""+pwTxt).hashCode();
//        if(temp<0) temp*=-1; //음수가 되면 양수로 넘겨주려고 했으나 딱히 그럴 필요가 없다는 걸 깨달았다.


        //key-value를 String[]으로 만듦.
        String[] key= {"idpw"};
        String[] val= {
                String.valueOf(idpwHashCode)
        };
//        httpConnection.connect("http://52.79.106.222/eyeballs/db_save.php", "db_save.php",key, val);
        //db_login.php에 로그인 요청을 보냄. 결과는 브로드캐스트 리비서에서 받을 것임.
        httpConnection.connect("http://52.79.106.222/eyeballs/db_login.php", "db_login.php",key, val);


//        Log.d("LoginActivity", "call http connection");
//        locationService.startLocationService(); //위치 정보 받아옵니다! 그래서 토스트로 출력! 이건 테스트 하고 있는 용도임.

    }//loginBtnOnClick






    //아래는 로컬     //브로드캐스트 리시버
    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // db_login.php로 보낸 결과값을 여기서 받음.
            final String message = intent.getStringExtra("db_login.php");

            Intent in;
            if(message.equals("false")) { //로그인에 실패하면 바로 가입을 위해 EidtProfileActivity로 이동
                in = new Intent(LoginActivity.this, EditProfileActivity.class);
            }else{ //로그인에 성공하면 MainActivity로 이동.
                in = new Intent(LoginActivity.this, MainActivity.class);
//                in.putExtra("messageFromServer", message);
                saveProfileToStaticManager(message); //로그인 성공이므로 profile 데이터를 핸드폰에 저장함.
            }

            startActivity(in);


            Log.d("LoginActivity", "local broadcast receiver works");
        }
    };

    //msg를 파싱해서 원하는 것만
    static private void saveProfileToStaticManager(String msg){
        DataSaver dataSaver = new DataSaver();

        StringTokenizer token = new StringTokenizer(msg, " ");

        //StaticManager에 저장하여 다른 곳에서도 이용할 수 있도록 함.
        StaticManager.nickname=token.nextToken();
        if(token.nextToken().equals("f")) StaticManager.sex=false;
        else StaticManager.sex=true;
        StaticManager.comment=token.nextToken();

        //나의 데이터 저장해두기.
        dataSaver.setData("nickname", StaticManager.nickname);
        dataSaver.setData("sex", StaticManager.sex);
        dataSaver.setData("comment", StaticManager.comment);
        dataSaver.commit();
    }




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