package company.chutianxi.bedroom2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import company.chutianxi.bedroom2.R;
import company.chutianxi.util.HttpsClient;

/**
 * Created by Administrator on 2017\11\27 0027.
 */

public class login extends Activity implements View.OnClickListener{
    Button login,reset;
    TextView input_password,input_username;
    private String username,password;
    private static final int SUCCESS = 1;
    private static final int FAIL = 2;
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case SUCCESS:
                    Toast.makeText(login.this,"登录成功",Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("stuid", username);
                    editor.commit();
                    Intent intent = new Intent(login.this,main.class);
                    //intent.putExtra("stuid",username);
                    startActivity(intent);
                    break;
                case FAIL:
                    Toast.makeText(login.this,"登录失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initview();
    }

    private void initview() {
        login = findViewById(R.id.login_login);
        reset = findViewById(R.id.login_reset);
        input_password = findViewById(R.id.input_password);
        input_username = findViewById(R.id.input_username);
        login.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_login)
        {
            username = input_username.getText().toString();
            password = input_password.getText().toString();
            loginrequest();
        }
        if(view.getId()==R.id.login_reset)
        {
            input_username.setText("");
            input_password.setText("");
        }
    }

    private void loginrequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> map = new HashMap<String,String>();
                map.put("username",username);
                map.put("password",password);
                String re = HttpsClient.HttpGet("https://api.mysspku.com/index.php/V1/MobileCourse/Login?",map);
                int errorcode = 0;
                try {
                    JSONObject jo = new JSONObject(re);
                    errorcode = jo.getInt("errcode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(errorcode==0)
                {
                    Message msg = new Message();
                    msg.what = SUCCESS;
                    mHandler.sendMessage(msg);
                    //Toast.makeText(login.this,"登录成功",Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(login.this,"登录成功11",Toast.LENGTH_SHORT).show();
                    Message msg = new Message();
                    msg.what = FAIL;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }
}
