package helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kesl on 2016-04-28.
 */
public class HttpConnection {

    BackgroundTask asykTsk; //어싱크태스크
    ArrayList<HttpQue> sBuffer; //http 통신 버퍼
    private String ServerURL, key, value, result="null"; //값 들. result는 http 통신으로 받은 결과값
    StaticManager staticManager = new StaticManager(); //스태틱 매니저
    Context context; //브로드캐스트를 위한 Context

    public HttpConnection(){ //생성자
        asykTsk = new BackgroundTask(); //AsyncTask 생성
        sBuffer= new ArrayList<HttpQue>(); //http 연결을 위한 버퍼 생성
        Log.d("http connection","creator");
    }

    private void setKey(String key){ //set key to send to server
        this.key = key;
    }
    private void setValue(String value){ //set value to send to server
        this.value = value;
    }
    private void setURL(String url){
        ServerURL=url;
    }
    private void setContext(Context con) {context = con; }

    public void connect(String url, String key, String value, Context con){
        setURL(url);
        setKey(key);
        setValue(value);
        setContext(con);
        Log.d("http connection", "connect method call");
        Log.d("http connection", "url : " + url);
        Log.d("http connection", "key : " + key);
        Log.d("http connection", "value : " + value);
        Log.d("http connection", "context : " + con);

        asykTsk.execute(); //AsyncTask를 실행함!
    }


    //AsynkTask
    class BackgroundTask extends AsyncTask { //AsyncTask for network. Work is charged with PostData()

        @Override
        protected Object doInBackground(Object[] params) { //쓰레드에서 하는 작업. http 통신해서 결과값 받아오기
            Log.d("http connection", "doInBackground call");
            result = PostData(); //http 통신을 통해 얻어온 결과가 temp에 저장

            Log.d("http connection", "doInBackground result : "+ result);
            return null;
        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); } //쓰레드 시작 전에 미리 처리해야 하는 작업

        @Override
        protected void onPostExecute(Object o) { //쓰레드가 끝난 후에 처리하는 작업

            //일이 끝난 후에 브로드캐스트 한다.
            staticManager.sendBroadcast(context, "httpConnection", key, result); //key는 같이 씀


//            staticManager.httpResult("key", result); //스태틱 매니저의 httpResult에 넣어줌.

            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        } //쓰레드 중간에 처리하는 작업
    }



    //실행 중인지 아닌지를 판별함.
    public AsyncTask.Status statusAsyncTask(){
        return asykTsk.getStatus();
    }






    //AsyncTask로 인자 값들 넘겨주면 아래의 PostData도 내부 클래스가 아닌 외부 클래스로 만들 수 있음.
    //------------------------------
    //    웹서버로 데이터 전송
    //------------------------------
    public String PostData() {
        Log.d("http connection", "PstData method call");
        // Server URL
//        String Server_URL = "http://ec2-52-79-190-209.ap-northeast-2.compute.amazonaws.com/test.php";

        // 전송할 데이터를 저장할 ArrayList 생성
//        ArrayList<HttpQue> sBuffer = new ArrayList<HttpQue>();

        // ArrayList에 <변수=값> 형태로 저장
        sBuffer.add(new HttpQue("", ServerURL)); // 서버 URL
        sBuffer.add(new HttpQue(key, value));   // "" 안의 문자열은
//        sBuffer.add(new HttpQue("user_pword", myPWord)); // 서버에 설정된 변수명이다
//        sBuffer.add(new HttpQue("user_nick", myNick));
//        sBuffer.add(new HttpQue("user_subject", mySubject));
        Log.d("http connection", "PstData method sBuffer add");

        // HttpPost 생성
        HttpPost mHttp = new HttpPost(sBuffer);

        Log.d("http connection", "PstData method mHttp");
        // Data 전송
        mHttp.HttpPostData();
        Log.d("http connection", "PstData method mHttp.HttpPostData()");
//        myResult =
        String result = mHttp.rString; // 전송 결과
        Log.d("http connection", "PstData method result : "+result);
        return result;
//        // Tab2에 있는 TextEdit에 전송 결과 표시
//        handler.post(new Runnable() {
//            public void run() {
//                ((TextView) (findViewById(R.id.text_result))).setText(myResult);
//            }
//        });
    } // PostData
}



//이하 Http 통신을 위한 메소드들.

//--------------------------
//  HttpQue
//--------------------------
class HttpQue {
    public String var;   // 변수명
    public String value;  // 값

    public HttpQue(String _var, String _value) { // 생성자
        var = _var;
        value = _value;
    }
}

//--------------------------
//HttpPost
//--------------------------
class HttpPost {
    public  String    rString;  // receive String
    public  StringBuilder   rBuffer;
    private ArrayList<HttpQue> sBuffer;  // sendBuffer

    //--------------------------
    // Constructor
    //--------------------------
    public HttpPost(ArrayList<HttpQue> _sBuffer) {
        sBuffer = _sBuffer;
        rBuffer = new StringBuilder(200000);  // receive 버퍼
        rString = "";      // receive 스트링
    }

    //--------------------------
    //   URL 설정하고 접속하기
    //--------------------------
    public void HttpPostData() {
        try {
            URL url = new URL(sBuffer.get(0).value);       // URL 설정
            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속

            //--------------------------
            // 전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            http.setConnectTimeout(5000);  // 5초
            http.setReadTimeout(10000) ;  // 10초
            http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                        // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");    // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            //--------------------------
            // 서버로 값 전송
            //--------------------------
            StringBuffer buffer = new StringBuffer();
            for (int i = 1; i < sBuffer.size(); i++) {
                buffer.append(sBuffer.get(i).var).append("=");
                buffer.append(sBuffer.get(i).value).append("&");
            }

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "EUC-KR");
            // OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");

            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            // Log.v("Http post", buffer.toString());

            //--------------------------
            // 서버에서 전송받기
            //--------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
            // InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);

            String str;
            while ((str = reader.readLine()) != null) {  // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                rBuffer.append(str + "\n");
            }
            rString = rBuffer.toString().trim();        // 전송결과를 문자열로
            // Log.v("Receive String", rString);

        } catch (MalformedURLException e) {
            Log.v("MalformedURLEx error", "------------");
            rString = "N/A";
        } catch (IOException e) {
            Log.v("IOException", rString + "--------------");
            rString = "N/A";
        } // try
    } // HttpPostData
} // class