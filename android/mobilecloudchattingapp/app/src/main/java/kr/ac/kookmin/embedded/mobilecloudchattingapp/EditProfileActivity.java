package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import helper.DataSaver;
import helper.HttpConnection;
import helper.StaticManager;

public class EditProfileActivity extends AppCompatActivity {

    EditText nicknameEditTxt, commentEditTxt;
    RadioButton radioManBtn, radioWomanBtn;
    DataSaver dataSaver;
    Intent intent;

    static int idpwHashCode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_profile);

        intent = getIntent();
        idpwHashCode = intent.getIntExtra("idpw", 0); //idpw를 가져옴.

        nicknameEditTxt = (EditText)findViewById(R.id.nicknameEditTxt);
        commentEditTxt = (EditText)findViewById(R.id.commentEditTxt);
        radioManBtn = (RadioButton)findViewById(R.id.radioManBtn);
        radioWomanBtn = (RadioButton)findViewById(R.id.radioWomanBtn);

        dataSaver = new DataSaver();

        StaticManager.checkIfSMHasProfile=false;
    }

    //닉네임과 sex 여부 체크하고 코멘트는 괜찮음.
    public void saveProfileBtnOnClick(View v){
        if(nicknameEditTxt.getText().toString().matches("")){ //닉네임이 정해져 있지 않으면
            StaticManager.testToastMsg("fill your nickname");
            return;
        }
        StaticManager.nickname=nicknameEditTxt.getText().toString();//닉네임 저장

        if(!(radioManBtn.isChecked() || radioWomanBtn.isChecked())){//둘 다 체크가 안 되어 있다면
            StaticManager.testToastMsg("choose sex");
            return;
        }
        if(radioWomanBtn.isChecked()) {
            StaticManager.sex=false; //여자에 체크면 false
        }
        else {
            StaticManager.sex=true; //남자에 체크면 true. 둘 중 하나가 안 되어 있다면 여기까지 올 수 없음.
        }
        if(commentEditTxt.getText().toString().matches("")){ // 코멘트가 비어있다면 디폴트 값으로 hi!
            StaticManager.comment="hi!";
        }
        else{ //코멘트가 있다면 그걸로 저장
            StaticManager.comment=commentEditTxt.getText().toString();
        }

        StaticManager.checkIfSMHasProfile=true; //저장했으므로 true;
        StaticManager.testToastMsg("You make a profile!");

    }//saveProfileBtnOnClick


    //취소 버튼이 눌린다면
    public void onBackPressed(){
        super.onBackPressed();

        Log.d("EditProfileActivity", "push Back Btn");

        //makeProfile ==true : 다 만들고 Back 눌러서 나가면
        if(StaticManager.checkIfSMHasProfile){
            saveProfileIntoServer();//디비를 저장하고
            setResult(RESULT_OK, intent); //OK라고 말해줌.
            Log.d("EditProfileActivity", "result OK");
        }
        //makeProfiel==false : 만들던 중간에 Back 눌러서 나가면
        else{
            setResult(RESULT_CANCELED, intent); //취소가 되었다고 말해줌.
            Log.d("EditProfileActivity", "result CANCLE");
        }

    }



    //데이터베이스에 저장
    static private void saveProfileIntoServer(){
        Log.d("LoginActivity", "save profile In server call");

        String sex;
        if(StaticManager.sex){ sex="m";}
        else {sex="f";}

        String[] key= {"idpw", "nickname", "sex", "comment"};
        String[] val= {
                String.valueOf(idpwHashCode),
                StaticManager.nickname,
                sex,
                StaticManager.comment
        };

        //db_login.php에 로그인 요청을 보냄. 결과는 브로드캐스트 리비서에서 받을 것임.
        Log.d("LoginActivity", val[0] + " " + val[1] + " " + val[2]+" "+val[3]+" are sended");
        HttpConnection httpConnection = new HttpConnection();
        httpConnection.connect("http://52.79.106.222/eyeballs/db_save.php", "db_save.php", key, val);

        Log.d("LoginActivity", "send http msg to db_save.php");
    }











    //EditProfileActivity에서 벗어날 때 SM에 있는 값을 가져와서 저장함.
    @Override
    protected void onPause() {
        super.onPause();

//        dataSaver.setData("nickname", StaticManager.nickname);
//        dataSaver.setData("sex", StaticManager.sex);
//        dataSaver.setData("comment", StaticManager.comment);
//        dataSaver.commit();
    }

    //EditProfileAcivity 화면을 다시 보여줄 때 데이터들을 세팅함.
    @Override
    protected void onResume() {
        super.onResume();
//        String temp = dataSaver.getData("nickname", ""); //닉네임 불러옴, 없으면 ""
//        StaticManager.nickname=temp; //SM에 넣음
//        nicknameEditTxt.setText(temp); //에딧텍스트에 넣음
//
//        temp=dataSaver.getData("comment", ""); //코멘트 불러옴, 없으면 ""
//        StaticManager.comment=temp; //SM에 넣음
//        commentEditTxt.setText(temp); //에딧텍스트에 넣음
//
//        boolean temp2 = dataSaver.getData("sex", false); //성별 불러옴, 없으면 여자로(ㅋㅋㅋㅋ) 이건 나중에 int로 바꾸자.
//        StaticManager.sex=temp2; //SM에 넣음
//        if(temp2){ //true면(남자면)
//            radioManBtn.setChecked(true);
//        }
//        else{ //false면(여자면)
//            radioWomanBtn.setChecked(true);
//        }
    }
}