package helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by kesl on 2016-05-05.
 */
public class LocationService {

    public LocationService(){ //생성자
        checkDangerousPermissions();
    }

    //GPS 서비스 실행!
    public void startLocationService() {
        LocationManager manager = StaticManager.locationManager;

        GPSListener gpsListener = new GPSListener();
        long minTime = 10000; //이 시간(10000ms= 10초)이 지나면 GPS를 업데이트 해주세요.
        float minDistance = 0; //내가 이만큼(0이면 항상) 움직이면 업데이트 해주세요.

        try {
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            android.location.Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {

                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();

                Log.i("GPSListener", "최근 위도 경도 : "+latitude+" "+longitude);

                StaticManager.testToastMsg("위도 경도 : "+latitude+" "+longitude);
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }

        Log.d("GPS Location", "startLocationService() success");

    }




    //sdk 23부터 바뀐 권한 문제를 해결하는 메소드.
    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(StaticManager.applicationContext, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.d("GPS Location", "permissionCheck == PackageManager.PERMISSION_GRANTED");
        } else {
            Log.d("GPS Location", "permissionCheck != PackageManager.PERMISSION_GRANTED");

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) StaticManager.applicationContext, permissions[0])) {
                Log.d("GPS Location", "ActivityCompat.shouldShowRequestPermissionRationale((Activity) StaticManager.applicationContext, permissions[0])");
            } else {
                ActivityCompat.requestPermissions((Activity) StaticManager.applicationContext, permissions, 1);
            }
        }
    }

//나중에 필요할 것 같으니 우선 한 번만 커밋해두고 지운다.
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == 1) {
//            for (int i = 0; i < permissions.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
////                    Toast.makeText(this, permissions[i] + " d", Toast.LENGTH_LONG).show();
//                } else {
////                    Toast.makeText(this, permissions[i] + " e", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
}

//매니저에게 리퀘스트를 보내기 위해 필요한 리스너 객체의 클래스
class GPSListener implements LocationListener {

    //여기서 실시간 위도 경도를 받습니당.
    public void onLocationChanged(android.location.Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        Log.i("GPS Location", "위도 경도 : " + latitude + " " + longitude);

        StaticManager.testToastMsg("위도 경도 : "+latitude+" "+longitude);
    }


    public void onProviderDisabled(String provider) {}
    public void onProviderEnabled(String provider) {}
    public void onStatusChanged(String provider, int status, Bundle extras) {}

}//GPSListener
