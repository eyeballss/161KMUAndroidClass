package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by kesl on 2016-05-05.
 */
public class PeopleListTab1Activity extends LinearLayout {

    View rootView;


    //여기서 이 레이아웃이 할 일을 지정함.
    private void work(Context context) {
        TextView text = (TextView)findViewById(R.id.section_label1);
        text.setText("1111111111");

        Intent in = new Intent(context, ChattingActivity.class);
        context.startActivity(in); //테스트를 위하여 여기로 갑니당.

    }





    public PeopleListTab1Activity(Context context) { //생성자
        super(context);
        init(context);
    }
    public PeopleListTab1Activity(Context context, AttributeSet attrs) { //생성자
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //첫번째 : xml 파일, 두번째: 가서 붙을 곳, 세번째 : t면 바로 붙고 f면 필요할 때 붙음.
        rootView = inflater.inflate(R.layout.fragment_main_tab1, this, true);
        work(context);
    }

    public View getView(){ //inflated View를 돌려줌.
        return rootView;
    }
}