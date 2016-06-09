package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import helper.HttpConnection;
import helper.StaticManager;

public class ChattingActivity extends AppCompatActivity {

    Button sendBtn;
    EditText editxtForChat;
    String oppoNickname; //상대방 닉네임.
    String myNickname = StaticManager.nickname; //나의 닉네임
    Intent intent;
    Handler handler;

    //서버 소켓
    Socket socket;
    ClientReceiver clientReceiver;
    ClientSender clientSender;
    StartNetwork startNetwork;

    //아래는 채팅 리스트
    ListView mChattingList;
    ArrayAdapter<String> mChattingAdapter; //이걸로 조종하면 됨.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//타이틀바 없애기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chatting);

        //상대방 아이디를 저장하는 곳
        intent = getIntent();
        oppoNickname = intent.getStringExtra("oppoNickname");

        //채팅에 필요한 뷰와 핸들러
        sendBtn = (Button) findViewById(R.id.sendBtn);
        editxtForChat = (EditText) findViewById(R.id.editxtForChat);
        handler = new Handler(Looper.getMainLooper());

        //채팅 리스트 객체 만들고 어댑터 적용
        mChattingList = (ListView) findViewById(R.id.chattingList);
        mChattingAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_chatting_items);
        mChattingList.setAdapter(mChattingAdapter);

        //채팅 쓰레드 시작
        startNetwork = new StartNetwork();
        startNetwork.start();

        initializeChatRequest();
    }

//    public void chattingSendOnClick(View v) {
//        mChattingAdapter.add(editxtForChat.getText().toString());
//        editxtForChat.setText("");
//    }


    private void initializeChatRequest(){

        String[] key = {
                "sender",
                "receiver",
        };
        String[] val = {
                oppoNickname,
                myNickname,
        };
        HttpConnection httpConnection = new HttpConnection();
        httpConnection.connect("http://" + StaticManager.ipAddress + "/eyeballs/db_getChat.php", "db_getChat.php", key, val);

    }
    //서버에서 가져온 값을 알려주는 브로드캐스트 리시버
    public BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // db_login.php로 보낸 결과값을 여기서 받음.
            final String message = intent.getStringExtra("db_getChat.php");

            메세지 받은게 hello*abc*hello*abc*hello*hello*hello*abc*LLLLLLLLL*hello*abc*LLLLLLLLL* 이렇게 오니까
                    파싱해야 함
            if (message != null) {
                if(!message.trim().equals("")) {
                    handler.post(new Runnable() { //VIEW 들을 만져줌.
                        public void run() {
                            mChattingAdapter.add(message); //채팅창에 올리고
                        }
                    });
                }
            }

            Log.d("ChattingActivity", "local broadcast receiver works");
        }
    };






//아래는 채팅 쓰레드


    public class StartNetwork extends Thread {
//        private Socket socket;
//        private String serverIp;

//        public StartNetwork(String ip) {
//            serverIp = ip;
//        }

        public void run() {
            try {
                socket = new Socket(StaticManager.ipAddress, 5011);
                Log.d("Chatting activity","start act success to connect");


//                System.out.println("success to connect");

//                myId = "samsung";

                clientReceiver = new ClientReceiver(socket);
                clientSender = new ClientSender(socket);

                clientReceiver.start();
                clientSender.start();
            } catch (IOException e) {
//            System.out.println("!!");
                StaticManager.testToastMsg("network error!!!");
            }
        }
    }


    public class ClientSender extends Thread {
        Socket socket;
        DataOutputStream output;

        public ClientSender(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());
                write(StaticManager.nickname); //서버에 먼저 내 아이디를 보내줌. 나인 것을 등록.
                Log.d("Chatting activity", "startNetwork register my id in server");
            } catch (Exception e) {
            }
        }

        public void write(String msg){
            try{
                output.writeUTF(msg);
            }catch(IOException e){Log.d("ChattingActivity", "write method io exception");}
            catch(Exception e){Log.d("ChattingActivity", "write method exception");}
        }

        @Override
        public void run() {
//            Scanner sc = new Scanner(System.in);
//            final String msg = {""};

            while (output != null) {

                //                    msg = sc.nextLine();

                //send 버튼을 누르면
                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("Chatting activity", "startNetwork sendBtn clicked");

                        //위에 있는게 정상적인 예제고 아래가 하드코딩 한 것
                        String msg = oppoNickname+"*" + editxtForChat.getText().toString();
//                        String msg = "abc*" + editxtForChat.getText().toString();

//                        if (msg.equals("exit"))
//                            System.exit(0);
                        try {
                            Log.d("Chatting activity", "startNetwork ready to send");
                            write(msg); //서버로 보내고
                            Log.d("Chatting activity", "msg for Server : " + msg);

                                    handler.post(new Runnable() { //VIEW 들을 만져줌.
                                        public void run() {
                                            mChattingAdapter.add(editxtForChat.getText().toString()); //채팅창에 올리고
                                            editxtForChat.setText(""); //비움.
                                        }
                                    });

                            Log.d("Chatting activity", "startNetwork success to send");
                        } catch (Exception e) {
                        }
                    }
                });//onClickListener


            }
        }

        public void stopDataOutputStream(){
            try{
                output.close();
                output=null;
            }catch(Exception e){};
        }
    }

    public class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream input;
//        DataOutputStream output;
        String inputStr;

        public ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
//                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
        }

        public void run() {
            inputStr = null;
            while (input != null) {
                try {
                    //상대방으로부터 내용을 받아옴.
                    inputStr = input.readUTF();

                    Log.d("Chatting activity", "msg form Server : "+inputStr);
                    StaticManager.sendBroadcast("dataFromChat", inputStr); //방송해도 되지만 여기선 그냥 핸들러로 처리함. 클래스로 만들면 방송하자.
                    handler.post(new Runnable() { //VIEW 들을 만져줌.
                        public void run() {
                            mChattingAdapter.add(inputStr); //채팅창에 올리고
                        }
                    });

                    String[] key = {
                            "sender",
                            "receiver",
                            "talk"
                    };
                    String[] val = {
                            oppoNickname,
                            myNickname,
                            inputStr
                    };

                    HttpConnection httpConnection = new HttpConnection();
                    httpConnection.connect("http://" + StaticManager.ipAddress + "/eyeballs/db_saveChat.php", "db_saveChat.php", key, val);



//                    System.out.println(inputStr);
                } catch (IOException e) {
                }
            }
        }

        public void stopDataInputStream(){
            try{
                input.close();
                input=null;
            }catch(Exception e){};
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("localBroadCast"));
    }

    @Override
    protected void onPause() {
        Log.d("chatting activity", "onPause method call");

        try {
            socket.close();
            socket=null;
            startNetwork.interrupt();
            clientReceiver.stopDataInputStream();
            clientReceiver.interrupt();
            clientSender.stopDataOutputStream();
            clientSender.interrupt();

            Log.d("chatting activity", "socket and threads close");
        } catch (IOException e) {
            Log.d("chatting activity", "socket close fail");
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        super.onPause();
    }
}
