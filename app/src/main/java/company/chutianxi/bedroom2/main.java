package company.chutianxi.bedroom2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import company.chutianxi.bedroom2.R;
import company.chutianxi.entity.student;
import company.chutianxi.util.HttpsClient;

/**
 * Created by Administrator on 2017\11\29 0029.
 */

public class main extends Activity implements View.OnClickListener{
    String username;
    private EditText Tname,Tgender,Tvcode,Troom,Tbuilding,Tlocation,Tgrade;
    private TextView Tstudentid;
    private Button secroom;//选宿舍按钮
    private Button checkroom;//查宿舍按钮
    private static final int SUCCESS = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    String re = (String)msg.obj;
                    updateinfo(re);
                    Toast.makeText(main.this,"更新成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
        username = pref.getString("stuid", "1301210899");
/*        SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("stuid", username);*/
        //editor.commit();
        initview();
        requestForDetail();
    }

    private void initview() {
        Tstudentid = findViewById(R.id.info_xuehao);
        Tname = findViewById(R.id.info_xingming);
        Tgender = findViewById(R.id.info_gender);
        Tvcode = findViewById(R.id.info_vcode);
        Troom = findViewById(R.id.info_room);
        Tbuilding = findViewById(R.id.info_building);
        Tlocation = findViewById(R.id.info_location);
        Tgrade = findViewById(R.id.info_grade);
        checkroom = findViewById(R.id.roomcheck);
        secroom = findViewById(R.id.selectroom);
        secroom.setOnClickListener(this);
        checkroom.setOnClickListener(this);
    }

    private void updateinfo(String re) {
        JSONObject jo = null;
        try {
            jo = new JSONObject(re);
            Log.d("imgmy",re);
            JSONObject jo1 = jo.getJSONObject("data");//取得嵌套的data层
            if(re.contains("room"))
            {
                Tstudentid.setText(jo1.getString("studentid"));
                Tname.setText(jo1.getString("name"));
                Tgender.setText(jo1.getString("gender"));
                Tvcode.setText(jo1.getString("vcode"));
                Troom.setText(jo1.getString("room"));
                Tbuilding.setText(jo1.getString("building"));
                Tlocation.setText(jo1.getString("location"));
                Tgrade.setText(jo1.getString("grade"));
            }else{
                secroom.setVisibility(View.VISIBLE);
                checkroom.setVisibility(View.VISIBLE);
                Tstudentid.setText(jo1.getString("studentid"));
                Tname.setText(jo1.getString("name"));
                Tgender.setText(jo1.getString("gender"));
                Tvcode.setText(jo1.getString("vcode"));
                Tlocation.setText(jo1.getString("location"));
                Tgrade.setText(jo1.getString("grade"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void requestForDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> map = new HashMap<String,String>();
                map.put("stuid",username);
                String re = HttpsClient.HttpGet("https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?",map);
                Message msg = new Message();
                msg.what = SUCCESS;
                msg.obj = re;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
        username = pref.getString("stuid", "1301210899");
        requestForDetail();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.selectroom)
        {
            //进入选宿舍界面
            Intent intent = new Intent(this,selectRoom.class);
            intent.putExtra("stuid",username);
            startActivity(intent);
            //fot result
        }else if(view.getId()==R.id.roomcheck)
        {
            Intent intent = new Intent(this,chooseRoom.class);
            intent.putExtra("gender","2");
            startActivity(intent);
        }
    }
}
