package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import helper.StaticManager;

public class EditProfileActivity extends AppCompatActivity {

    EditText nicknameEditTxt, commentEditTxt;
    RadioButton radioManBtn, radioWomanBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_profile);

        nicknameEditTxt = (EditText)findViewById(R.id.nicknameEditTxt);
        commentEditTxt = (EditText)findViewById(R.id.commentEditTxt);
        radioManBtn = (RadioButton)findViewById(R.id.radioManBtn);
        radioWomanBtn = (RadioButton)findViewById(R.id.radioWomanBtn);

    }

    //닉네임과 sex 여부 체크하고 코멘트는 괜찮음.

    public void saveProfileBtnOnClick(View v){
        if(nicknameEditTxt.getText().toString().matches("")){ //닉네임이 정해져 있지 않으면
            StaticManager.testToastMsg(getApplicationContext(), "fill your nickname");
            return;
        }
        if(!(radioManBtn.isChecked() || radioWomanBtn.isChecked())){//둘 다 체크가 안 되어 있다면
            StaticManager.testToastMsg(getApplicationContext(), "choose sex");
            return;
        }

        StaticManager.testToastMsg(getApplicationContext(), "You make a profile!" );



    }//saveProfileBtnOnClick


}