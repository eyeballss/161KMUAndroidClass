package helper;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by kesl on 2016-05-01.
 */
//static 변수들이나 static 메소드들을 갖고 있음.
public class StaticManager {

    private static Handler handler;
    private static boolean singleton=false;
    public static String nickname;
    public static String comment;
    public static boolean sex; //F면 여자 T이면 남자



    public static Context applicationContext;

    public static LocationManager locationManager;


//    //로컬 브로드캐스트 생각해보니 intentName이 하나로 고정되어 있어도 괜찮을 듯. key 값으로 구별하면 되니까.
//    public static void sendBroadcast(String intentName, String key, String data) {
//        Intent intent = new Intent(intentName);
//        intent.putExtra(key, data);
//        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
//    }
    //로컬 브로드캐스트
    public static void sendBroadcast(String key, String data) {
        Intent intent = new Intent("localBroadCast");
        intent.putExtra(key, data);
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
    }

    //테스트용 토스트 메세지
    public static void testToastMsg(final String string){
        if(!singleton){
            handler = new Handler(); //핸들러는 하나만 있어도 되므로 싱글톤을 사용하여 하나만 생성하도록, 이렇게 해보자.
            singleton=true;
            Log.d("Static Manager", "new handler and singleton is "+singleton);
        }
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
