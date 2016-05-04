package helper;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by kesl on 2016-05-01.
 */
//static 변수들이나 static 메소드들을 갖고 있음.
public class StaticManager {

    public static String nickname;
    public static String comment;
    public static boolean sex; //F면 여자 T이면 남자

    //로컬 브로드캐스트
    public static void sendBroadcast(Context context, String intentName, String key, String data) {
        Intent intent = new Intent(intentName);
        intent.putExtra(key, data);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    //테스트용 토스트 메세지
    public static void testToastMsg(final Context context, final String string){
        new Handler().post(new Runnable() {
            public void run() {
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
