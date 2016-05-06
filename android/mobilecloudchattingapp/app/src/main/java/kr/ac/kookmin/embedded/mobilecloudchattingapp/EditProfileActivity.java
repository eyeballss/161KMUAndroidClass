package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import helper.StaticManager;
import helper.DataSaver;

public class EditProfileActivity extends AppCompatActivity {

    EditText nicknameEditTxt, commentEditTxt;
    RadioButton radioManBtn, radioWomanBtn;
    DataSaver dataSaver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_profile);

        nicknameEditTxt = (EditText)findViewById(R.id.nicknameEditTxt);
        commentEditTxt = (EditText)findViewById(R.id.commentEditTxt);
        radioManBtn = (RadioButton)findViewById(R.id.radioManBtn);
        radioWomanBtn = (RadioButton)findViewById(R.id.radioWomanBtn);

        dataSaver = new DataSaver();
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

        StaticManager.testToastMsg("You make a profile!");

    }//saveProfileBtnOnClick


    //EditProfileActivity에서 벗어날 때 SM에 있는 값을 가져와서 저장함.
    @Override
    protected void onPause() {
        super.onPause();

        dataSaver.setData("nickname", StaticManager.nickname);
        dataSaver.setData("sex", StaticManager.sex);
        dataSaver.setData("comment", StaticManager.comment);
        dataSaver.commit();
    }

    //EditProfileAcivity 화면을 다시 보여줄 때 데이터들을 세팅함.
    @Override
    protected void onResume() {
        super.onResume();
        String temp = dataSaver.getData("nickname", ""); //닉네임 불러옴, 없으면 ""
        StaticManager.nickname=temp; //SM에 넣음
        nicknameEditTxt.setText(temp); //에딧텍스트에 넣음

        temp=dataSaver.getData("comment", ""); //코멘트 불러옴, 없으면 ""
        StaticManager.comment=temp; //SM에 넣음
        commentEditTxt.setText(temp); //에딧텍스트에 넣음

        boolean temp2 = dataSaver.getData("sex", false); //성별 불러옴, 없으면 여자로(ㅋㅋㅋㅋ) 이건 나중에 int로 바꾸자.
        StaticManager.sex=temp2; //SM에 넣음
        if(temp2){ //true면(남자면)
            radioManBtn.setChecked(true);
        }
        else{ //false면(여자면)
            radioWomanBtn.setChecked(true);
        }
    }
}