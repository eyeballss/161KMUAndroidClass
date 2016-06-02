package me.blog.eyeballss.mylogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import helper.LocationService;
import helper.StaticManager;

public class MainActivity extends AppCompatActivity {

    private GoogleMap map;
    private LocationService locationService;
    private ListView activityListView;
    private Button registerBtn;
    private ListViewAdapter listViewAdapter;

    ArrayList<String> listDatas;
    HashMap<Integer, ActivityLocation> registeredActivities;
    private double myLatitude=0.0, myLongitude=0.0;
    private static int counter=0;

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


        //리스트뷰를 아이템 하나를 클릭하면
        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //해당 위치로 카메라를 돌림. 이거 계속 내 현재위치 따라다니니까, 고쳐야 함.
                LatLng curPoint = new LatLng(registeredActivities.get(position).getLatitude(),
                        registeredActivities.get(position).getLongitude()); //double형으로 la, lo을 LatLng 객체에 넣음
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15)); //확대를 15만큼 합니다.

            }
        });

    }

    //등록 버튼을 누르면 세 가지를 해야 함.
    //1. 다이얼로그 띄워서 아이콘 만들기
    //2. 리스트에 넣기
    //3. 디비에 넣기
    String time, comment;
    public void registerBtnClick(View v) {
        if(myLatitude==0.0 && myLongitude==0.0) return;

        //1. 다이얼로그 띄워서 아이콘 만들기
        dialog();
        Log.d("MainActivity", comment + "를 띄움");
        //3. 디비에 넣기
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



    private void dialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //시간을 구해 먼저 넣습니다.
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy.mm.dd hh:mm:ss");
        time = dayTime.format(new Date(System.currentTimeMillis()));

        alert.setTitle("활동 기록 " + time);
        final EditText input = new EditText(this); //에딧텍스트
        input.setHint("내용");
        alert.setView(input);

        alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                comment = input.getText().toString();

                //현재 위치를 지도에 아이콘을 추가함
                addIconOnMap(myLatitude, myLongitude, comment);
                //2. 리스트에 넣기
                listDatas.add(time+"\n "+comment);
                //등록도 함.
                registeredActivities.put(counter++, new ActivityLocation(myLatitude, myLongitude));
                listViewAdapter.notifyDataSetChanged();
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();

    }

    private void addIconOnMap(Double latitude, Double longitude, String snippet){

        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(latitude, longitude));
        marker.title(time);
        marker.snippet(snippet);
        marker.draggable(true);
        marker.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_on));

        map.addMarker(marker);

    }








    //서버에서 가져온 값을 알려주는 브로드캐스트 리시버
    String message;
    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // db_login.php로 보낸 결과값을 여기서 받음.
            message = intent.getStringExtra("gps data");
            if (message != null) {
                myLongitude = Double.valueOf(message.substring(message.indexOf(" ") + 1));
                myLatitude = Double.valueOf(message.substring(0, message.indexOf(" ")));

                Log.d("gps data", "위도 : " + myLatitude + " 경도 : " + myLongitude);
                LatLng curPoint = new LatLng(myLatitude, myLongitude); //double형으로 la, lo을 LatLng 객체에 넣음
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15)); //확대를 15만큼 합니다.

                map.setMapType(GoogleMap.MAP_TYPE_NORMAL); //맵의 타입을 정함. 위성인지 항공인지 등

            }

        }
    };//mLocalBroadcastReceiver





    private void settingListView() {
        listDatas = new ArrayList<String>();
        registeredActivities = new HashMap<Integer, ActivityLocation>();

        listViewAdapter = new ListViewAdapter(this, listDatas);
        activityListView.setAdapter(listViewAdapter);
    }

    private void settingViews() {
        registerBtn = (Button) findViewById(R.id.registerBtn);
        activityListView = (ListView) findViewById(R.id.activityListView);
    }

    private void settingMap() {
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        map.setMyLocationEnabled(true); // 내 위치를 지도상에 깜빡깜빡 나타나게 함.
    }

    protected void onPause() {
        //이것도 나중에 스태틱으로 바꿔주자. 여기서 특별히 다르게 처리해야 할 것은 없으니까.
        // Unregister since the activity is not visible 브로드캐스트 리비서 해제
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        locationService.stopGPS(this);
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        map.setMyLocationEnabled(false); // 내 위치를 지도상에 깜빡깜빡 나타나게 함.
        super.onPause();
    }
}
