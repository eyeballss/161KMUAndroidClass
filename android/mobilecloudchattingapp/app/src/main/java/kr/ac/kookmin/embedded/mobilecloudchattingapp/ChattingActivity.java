package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChattingActivity extends AppCompatActivity {

    ListView mChattingList;
    ArrayAdapter<String> mChattingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chatting);

        //채팅 리스트 객체 만들고 어댑터 적용
        mChattingList = (ListView)findViewById(R.id.chattingList);
        mChattingAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_items);

        mChattingList.setAdapter(mChattingAdapter);


    }




/*


    class StartNetwork extends Thread{
        private Socket socket;
        private String serverIp;

        public StartNetwork(String ip){
            serverIp=ip;
        }

        public void run(){
            try {
                socket = new Socket(serverIp, 5011);
                System.out.println("success to connect");

                myId = "samsung";

                ClientReceiver clientReceiver = new ClientReceiver(socket);
                ClientSender clientSender = new ClientSender(socket);

                clientReceiver.start();
                clientSender.start();
            } catch (IOException e) {
//            System.out.println("!!");
                StaticManager.testToastMsg("error!!!");
            }
        }
    }

    class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream input;
        DataOutputStream output;

        public ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
        }

        @Override
        public void run() {
            inputStr=null;
            while (input != null) {
                try {
                    inputStr = input.readUTF();

                    StaticManager.sendBroadcast("dataFromChat", inputStr);


//                    System.out.println(inputStr);
                } catch (IOException e) {
                }
            }
        }
    }







    class ClientSender extends Thread {
        Socket socket;
        DataOutputStream output;

        public ClientSender(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(myId);
                Log.d("ChattingTab2Activity", "send myId");
            } catch (Exception e) {
            }
        }

        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);
//            final String msg = {""};

            while (output != null) {

                //                    msg = sc.nextLine();

                //send 버튼을 누르면
                chatSendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("ChattingTab2Activity", "chatSendBtn click");

                        msg ="xaomi*"+chatEdit.getText().toString();

                        if (msg.equals("exit"))
                            System.exit(0);
                        try {
                            Log.d("ChattingTab2Activity", "ready to send");
                            output.writeUTF(msg);
                            Log.d("ChattingTab2Activity", "success to send");
                        } catch (IOException e) {
                        }
                    }
                });





            }
        }
    }
*/
}
