package company.chutianxi.bedroom2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import company.chutianxi.util.HttpsClient;

/**
 * Created by Administrator on 2017\12\15 0015.
 */

public class chooseRoom extends Activity implements View.OnClickListener{
    private TextView build5,build13,build14,build8,build9;
    private ImageView backbtn;
    private String gender;
    private static final int SUCCESS = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    String re = (String)msg.obj;
                    Log.d("info",re);
                    updateinfo(re);
                    Toast.makeText(chooseRoom.this,"更新成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void updateinfo(String re) {
        JSONObject jo = null;
        try {
            jo = new JSONObject(re);
            JSONObject jo1 = jo.getJSONObject("data");
            build5.setText(jo1.getString("5"));
            build13.setText(jo1.getString("13"));
            build14.setText(jo1.getString("14"));
            build8.setText(jo1.getString("8"));
            build9.setText(jo1.getString("9"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leftroom);
        gender = getIntent().getStringExtra("gender");
        initview();
        requestForDetial();
    }

    private void requestForDetial() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> map = new HashMap<String,String>();
                map.put("gender",gender);
                String re = HttpsClient.HttpGet("https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?",map);
                Message msg = new Message();
                msg.what = SUCCESS;
                msg.obj = re;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private void initview() {
        build5 = findViewById(R.id.left_5);
        build13 = findViewById(R.id.left_13);
        build14 = findViewById(R.id.left_14);
        build8 = findViewById(R.id.left_8);
        build9 = findViewById(R.id.left_9);
        backbtn = findViewById(R.id.back);
        backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back)
        {
/*            Intent intent = new Intent(this,main.class);
            startActivity(intent);*/
            finish();
        }
    }
}
