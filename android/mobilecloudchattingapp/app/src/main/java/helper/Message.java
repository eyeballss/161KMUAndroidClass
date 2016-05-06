package helper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by kesl on 2016-04-22.
 */
public class Message {

    public void okayMsgShow(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(StaticManager.applicationContext);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//닫기
            }
        });
        alert.setMessage(msg);
        alert.show();
    }


    public void yesNoMsgShow(String msg, final String yesKey, final String yesVal, final String noKey, final String noVal){

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(StaticManager.applicationContext);
        alert_confirm.setMessage(msg);
        alert_confirm.setCancelable(false);
        alert_confirm.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 'YES'누르면 yesKey, yesVal로 로컬브로드캐스트 함.
                StaticManager.sendBroadcast(yesKey, yesVal);
            }
        });
        alert_confirm.setNegativeButton("Cancle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No' 누르면 noKey, noVal로 로컬브로드캐스트 함.
                        StaticManager.sendBroadcast(noKey, noVal);
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }


}
