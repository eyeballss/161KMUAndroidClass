package kr.ac.kookmin.embedded.mobilecloud_simplecalapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText value1, value2, result;
    Button key0, key1, key2, key3, key4, key5, key6, key7, key8, key9;
    Button addBtn, subBtn, mulBtn, divBtn, delBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        value1 = (EditText) findViewById(R.id.value1);
        value2 = (EditText) findViewById(R.id.value2);
        result = (EditText) findViewById(R.id.result);
        key0 = (Button) findViewById(R.id.key0);
        key1 = (Button) findViewById(R.id.key1);
        key2 = (Button) findViewById(R.id.key2);
        key3 = (Button) findViewById(R.id.key3);
        key4 = (Button) findViewById(R.id.key4);
        key5 = (Button) findViewById(R.id.key5);
        key6 = (Button) findViewById(R.id.key6);
        key7 = (Button) findViewById(R.id.key7);
        key8 = (Button) findViewById(R.id.key8);
        key9 = (Button) findViewById(R.id.key9);
        addBtn = (Button) findViewById(R.id.addBtn);
        subBtn = (Button) findViewById(R.id.subBtn);
        mulBtn = (Button) findViewById(R.id.mulBtn);
        divBtn = (Button) findViewById(R.id.divBtn);
        delBtn = (Button) findViewById(R.id.delBtn);

    }

    public void onClick(View v) {
        View cView = getCurrentFocus();
        switch (v.getId()) {
            case R.id.key0: {
                if (cView.getId() == R.id.value1)
                    value1.setText(value1.getText() + "0");
                else if (cView.getId() == R.id.value2)
                    value2.setText(value2.getText() + "0");
                break;
            }
            case R.id.key1: {
                if (cView.getId() == R.id.value1)
                    value1.setText(value1.getText() + "1");
                else if (cView.getId() == R.id.value2)
                    value2.setText(value2.getText() + "1");
                break;
            }
            case R.id.key2: {
                if (cView.getId() == R.id.value1)
                    value1.setText(value1.getText() + "2");
                else if (cView.getId() == R.id.value2)
                    value2.setText(value2.getText() + "2");
                break;
            }
            case R.id.key3: {
                if (cView.getId() == R.id.value1)
                    value1.setText(value1.getText() + "3");
                else if (cView.getId() == R.id.value2)
                    value2.setText(value2.getText() + "3");
                break;
            }
            case R.id.key4: {
                if (cView.getId() == R.id.value1)
                    value1.setText(value1.getText() + "4");
                else if (cView.getId() == R.id.value2)
                    value2.setText(value2.getText() + "4");
                break;
            }
            case R.id.key5: {
                if (cView.getId() == R.id.value1)
                    value1.setText(value1.getText() + "5");
                else if (cView.getId() == R.id.value2)
                    value2.setText(value2.getText() + "5");
                break;
            }
            case R.id.key6: {
                if (cView.getId() == R.id.value1)
                    value1.setText(value1.getText() + "6");
                else if (cView.getId() == R.id.value2)
                    value2.setText(value2.getText() + "6");
                break;
            }
            case R.id.key7: {
                if (cView.getId() == R.id.value1)
                    value1.setText(value1.getText() + "7");
                else if (cView.getId() == R.id.value2)
                    value2.setText(value2.getText() + "7");
                break;
            }
            case R.id.key8: {
                if (cView.getId() == R.id.value1)
                    value1.setText(value1.getText() + "8");
                else if (cView.getId() == R.id.value2)
                    value2.setText(value2.getText() + "8");
                break;
            }
            case R.id.key9: {
                if (cView.getId() == R.id.value1)
                    value1.setText(value1.getText() + "9");
                else if (cView.getId() == R.id.value2)
                    value2.setText(value2.getText() + "9");
                break;
            }

            case R.id.addBtn: {
                cal(0, value1.getText().toString(), value2.getText().toString());
                break;
            }
            case R.id.subBtn: {
                cal(1, value1.getText().toString(), value2.getText().toString());
                break;
            }
            case R.id.mulBtn: {
                cal(2, value1.getText().toString(), value2.getText().toString());
                break;
            }
            case R.id.divBtn: {
                cal(3, value1.getText().toString(), value2.getText().toString());
                break;
            }
            case R.id.delBtn: {
                String temp;
                if (cView.getId() == R.id.value1) {
                    temp = value1.getText().toString();
                    if (temp.length() > 0)
                        value1.setText(temp.substring(0, temp.length() - 1));
                } else if (cView.getId() == R.id.value2) {
                    temp = value2.getText().toString();
                    if (temp.length() > 0)
                        value2.setText(temp.substring(0, temp.length() - 1));
                } else if (cView.getId() == R.id.result) {
                    result.setText("");
                }
                break;
            }

        }
    }//onClick

    public void cal(int what, String val1, String val2) {
        if (val1.equals("") || val1 == null) val1 = "0";
        if (val2.equals("") || val2 == null) val2 = "0";

        int answer = 0;


        if (what == 0) { //더하기
            answer = Integer.parseInt(val1) + Integer.parseInt(val2);
        } else if (what == 1) {//빼기
            answer = Integer.parseInt(val1) - Integer.parseInt(val2);
        } else if (what == 2) {//곱하기
            answer = Integer.parseInt(val1) * Integer.parseInt(val2);
        } else if (what == 3) {//나누기
            if (val2.equals("0")) {
                Toast.makeText(getApplicationContext(), "0으로 나눌 수 없음",
                        Toast.LENGTH_SHORT).show();
            } else {
                double a = Integer.parseInt(val1);
                double b = Integer.parseInt(val2);
                double c = (a / b);
                result.setText(String.valueOf(c));
            }
            what = 5;

        }
        if (what != 5) result.setText(String.valueOf(answer));
    }
}