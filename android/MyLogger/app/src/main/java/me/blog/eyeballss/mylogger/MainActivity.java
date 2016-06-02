package me.blog.eyeballss.mylogger;

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
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import helper.LocationService;
import helper.StaticManager;

public class MainActivity extends AppCompatActivity {

    private GoogleMap map;
    private LocationService locationService;
    private ListView activityListView;
    private Button registerBtn;
    private ListViewAdapter listViewAdapter;

    ArrayList<String> datas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //스태틱 매니저 세팅
        settingStaticManager();

        //지도 세팅
        settingMap();

        //뷰 객체 가져오기
        settingViews();

        //리스트뷰 세팅
        settingListView();



    }

    public void registerBtnClick(View v){
        datas.add("click!");
        listViewAdapter.notifyDataSetChanged();
    }

    private void recodeGPS() {
        //gps 받아서 레코딩 함.
        boolean gps = StaticManager.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps) {
            StaticManager.testToastMsg("Turn on the gps.\nOther people can see you now.");
        } else {
//            updateMyGPS("0 0"); //0으로 내 gps를 업데이트 함.
            StaticManager.testToastMsg("Turn off the gps.\nOther people can't see you now.");

        }
    }



    //서버에서 가져온 값을 알려주는 브로드캐스트 리시버
    String message;
    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // db_login.php로 보낸 결과값을 여기서 받음.
            message = intent.getStringExtra("gps data");
            if(message !=null){
                String longitude = message.substring(message.indexOf(" ")+1);
                String latitude = message.substring(0, message.indexOf(" "));

                Log.d("gps data", "위도 : "+latitude+" 경도 : "+longitude);
                LatLng curPoint = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)); //double형으로 la, lo을 LatLng 객체에 넣음
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15)); //확대를 15만큼 합니다.

                map.setMapType(GoogleMap.MAP_TYPE_NORMAL); //맵의 타입을 정함. 위성인지 항공인지 등

            }

        }
    };//mLocalBroadcastReceiver



    private void settingListView(){
        datas = new ArrayList<String>();

        listViewAdapter = new ListViewAdapter(this, datas);
        activityListView.setAdapter(listViewAdapter);
    }

    private void settingViews(){
        registerBtn = (Button)findViewById(R.id.registerBtn);
        activityListView = (ListView)findViewById(R.id.activityListView);
    }

    private void settingMap(){
        SupportMapFragment fragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        map = fragment.getMap();
    }

    private void settingStaticManager() {
        StaticManager.applicationContext = getApplicationContext(); //어플리케이션 콘텍스트 넘김.
        StaticManager.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//위치매니저 넘김.
        locationService = new LocationService();
        locationService.startLocationService(); //GPS 서비스 곧 바로 시작.
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
        locationService.stopGPS(this);
        super.onPause();
    }
}
