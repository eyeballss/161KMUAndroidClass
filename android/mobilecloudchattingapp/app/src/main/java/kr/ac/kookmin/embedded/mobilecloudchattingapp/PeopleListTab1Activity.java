package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import helper.StaticManager;
import listViewAdapter.ListViewAdapter_MainTab1;

/**
 * Created by kesl on 2016-05-05.
 */
public class PeopleListTab1Activity extends LinearLayout {

    static boolean singleton=false;
    View rootView;
    ListView listView;
    ListViewAdapter_MainTab1 listViewAdapterMainTab1;
    String data = "A*130*B*100*C*100*D*300*E*120";
    ArrayList<String> name;
    ArrayList<String> distance;

    //여기서 이 레이아웃이 할 일을 지정함.
    private void work(Context context) {

        name= new ArrayList<String>();
        distance= new ArrayList<String>();
        dataParser();


        listView = (ListView) findViewById(R.id.listViewMainTab1);
        //이렇게 어댑터를 생성하고 나면 리스트 다루는 일은 어댑터가 도맡아 한다.
        listViewAdapterMainTab1 = new ListViewAdapter_MainTab1((Activity)context, name, distance);
        //리스트뷰는 단지 보여주는 역할만 할 뿐.
        listView.setAdapter(listViewAdapterMainTab1);


//        Intent in = new Intent(context, ChattingActivity.class);
//        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(in); //테스트를 위하여 여기로 갑니당.


    }

    private void dataParser() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        StringTokenizer token = new StringTokenizer(data, "*");
        while(token.hasMoreTokens()){
            String name = token.nextToken();
            int dis= Integer.valueOf(token.nextToken());

            map.put(name, dis);

        }//while

        Iterator it = StaticManager.sortByValue(map).iterator();
        while(it.hasNext()){
            String temp = (String) it.next();
            name.add(temp+"\t");
            distance.add(String.valueOf(map.get(temp)));
//            System.out.println(temp + " = " + map.get(temp));
        }

    }












    //아래는 세팅하는 부분

    public PeopleListTab1Activity(Context context) { //생성자
        super(context);
        init(context);
    }

    public PeopleListTab1Activity(Context context, AttributeSet attrs) { //생성자
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        if(singleton){
            singleton=false;
        }else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //첫번째 : xml 파일, 두번째: 가서 붙을 곳, 세번째 : t면 바로 붙고 f면 필요할 때 붙음.
            rootView = inflater.inflate(R.layout.fragment_main_tab1, this, true);
            work(context);
            Log.d("PeopleTab1", "init call");
            singleton=true;
        }
    }

    public View getView() { //inflated View를 돌려줌.
        return rootView;
    }
}