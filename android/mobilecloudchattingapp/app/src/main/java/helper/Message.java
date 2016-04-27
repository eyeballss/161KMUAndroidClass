package helper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kesl on 2016-04-22.
 */
public class Message {

    public void confirmMsgShow(AppCompatActivity Activity, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(Activity);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//닫기
            }
        });
        alert.setMessage(msg);
        alert.show();
    }


    public void confirmAndCancleMsgShow(AppCompatActivity Activity, String msg){

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Activity);
        alert_confirm.setMessage(msg).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 'YES'
            }
        }).setNegativeButton("Cancle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }


}
