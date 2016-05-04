package helper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by kesl on 2016-05-01.
 */
public class StaticManager {

    //HttpConnection을 통해 결과를 받아옴.
    public void httpResult(String key, Object result){

        switch(key){
            case "user_pword": break;
            case "other": break;
        }
    }


    //로컬 브로드캐스트
    public void sendBroadcast(Context context, String intentName, String key, String data) {
        Intent intent = new Intent(intentName);
        intent.putExtra(key, data);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
