package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import helper.HttpConnection;
import helper.Message;
import helper.StaticManager;
import listViewAdapter.ListViewAdapter_MainTab1;

/**
 * Created by kesl on 2016-05-05.
 */
public class PeopleListTab1Activity extends LinearLayout {

    static boolean singleton = false;
    View rootView;
    ListView listView;
    ListViewAdapter_MainTab1 listViewAdapterMainTab1;
    ArrayList<String> name; //리스트에 들어가는 데이터
    ArrayList<String> distance; //리스트에 들어가는 데이터
    Context context;

    HashMap<String, String> dataMap = new HashMap<String, String>();

    //여기서 이 레이아웃이 할 일을 지정함.
    private void work(final Context context) {
        this.context = context;

        name = new ArrayList<String>();
        distance = new ArrayList<String>();


        //사람들 정보를 받아옴.
        getPeopleData(context);

    }//work


    //사람들 정보를 받음
    private void getPeopleData(Context context) {

        LocalBroadcastManager.getInstance(context).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("localBroadCast"));

        HttpConnection httpConnection = new HttpConnection();
        String[] key = {"idpw"}; //나의 idpw를 줌.
        String[] val = {String.valueOf(StaticManager.idpw)};
        httpConnection.connect("http://" + StaticManager.ipAddress + "/eyeballs/db_getProfile.php", "db_getProfile.php", key, val);

    }

    //서버에서 가져온 값을 알려주는 브로드캐스트 리시버
    String message;
    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            message = intent.getStringExtra("chatting_dialog");
            if (message != null) {
                if (message.equals("no")) {
                    StaticManager.testToastMsg("싫구나..");
                } else {
                    StaticManager.testToastMsg(message + "랑 대화하자!");

                    Intent in = new Intent(context, ChattingActivity.class);
                    in.putExtra("oppoNickname", message);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in); //이쪽으로 가서 채팅.

                }
            }
            //사람들 정보를 받음
            message = intent.getStringExtra("db_getProfile.php");
            if (message != null) {
                listViewSetting(message);
            }


            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocalBroadcastReceiver);
            Log.d("LoginActivity", "local broadcast receiver works");
        }
    };


    //사람들 정보를 받아오면 여기서 리스트에 올라가게 됨. 맨 처음 나오는 두 값이 내 좌표값.
    private void listViewSetting(String data) {

        //받아온 사람들 정보를 파싱함. 파싱한 값은 name과 distance arrayList에 들어감
        dataParser(data);


        //아래는 리스트뷰 세팅---------------
        listView = (ListView) findViewById(R.id.listViewMainTab1);
        //이렇게 어댑터를 생성하고 나면 리스트 다루는 일은 어댑터가 도맡아 한다.
        listViewAdapterMainTab1 = new ListViewAdapter_MainTab1((Activity) context, name, distance);
        //리스트뷰는 단지 보여주는 역할만 할 뿐.
        listView.setAdapter(listViewAdapterMainTab1);
        //리스트뷰의 아이템을 클릭하면..!!!!
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringTokenizer token = new StringTokenizer(dataMap.get(name.get(position).toString()), "*");
                String nickname = token.nextToken();
                String sex = token.nextToken();
                String comment = token.nextToken();

                String msg =
                        "name : \n" + nickname + "\n" +
                                "distance : \n" + distance.get(position) + "\n\n" +
                                "sex : " + sex +"\n"+
                                "comment : " + comment + "\n\n" +
                                "do you want to chat?\n";
                Message.yesNoMsgShow(msg, "chatting_dialog", name.get(position), "no", context);
            }
        });

    }

    //데이터를 리스트에 정렬해서 넣어주는 녀석
    private void dataParser(String data) {
        Log.d("PeopleListTab1Activity", "parsing data : "+data);
        HashMap<String, Double> map = new HashMap<String, Double>();
        dataMap.clear(); //데이터맵 싹 지움
        name.clear(); //네임 싹 지움
        distance.clear(); //거리 싹 지움 초 기 화!
        StringTokenizer token = new StringTokenizer(data, "*");

        while (token.hasMoreTokens()) {
            String idpw = token.nextToken();
            String nickname_sex_comment = token.nextToken() + "*" + token.nextToken() + "*" + token.nextToken();
            String temp = token.nextToken();
            Double dis = Double.valueOf(temp.substring(0, temp.indexOf(".")+5));

            //map과 dataMap에 각각 idpw를 키로 두고 데이터를 넣음.
            map.put(idpw, dis);
            dataMap.put(idpw, nickname_sex_comment);
            Log.d("PeopleListTab1Activity", "idpw:" + idpw + " nickname_sex_comment:" + nickname_sex_comment + " dis:" + dis);

        }//while

        //정렬
        Iterator it = StaticManager.sortByValue(map).iterator();

        while (it.hasNext()) {
            String temp = (String) it.next();
            String nickname = dataMap.get(temp).toString();
            token = new StringTokenizer(nickname, "*");
            name.add(token.nextToken());
            distance.add(String.valueOf(map.get(temp)));

            Log.d("PeopleListTab1Activity", "name:" + temp + " distance:" + map.get(temp));
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
        if (singleton) {
            singleton = false;
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //첫번째 : xml 파일, 두번째: 가서 붙을 곳, 세번째 : t면 바로 붙고 f면 필요할 때 붙음.
            rootView = inflater.inflate(R.layout.fragment_main_tab1, this, true);
            work(context);
            Log.d("PeopleTab1", "init call");
            singleton = true;
        }
    }

    public View getView() { //inflated View를 돌려줌.
        return rootView;
    }
}