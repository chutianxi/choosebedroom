package company.chutianxi.bedroom2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import company.chutianxi.adapter.ViewPagerAdapter;
import company.chutianxi.util.HttpsClient;

/**
 * Created by Administrator on 2017\12\27 0027.
 */

public class selectRoom extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private EditText Estu1idf,Estu2idf,Estu3idf,Ev1codef,Ev2codef,Ev3codef;//四人选宿舍
    private EditText Estu1idt,Estu2idt,Ev1codet,Ev2codet;//三人选宿舍
    private EditText Estu1idtw,Ev1codetw;//二人选宿舍
    private EditText Estu1ido;//单人选宿舍
    private Button ensure1,ensure2,ensure3,ensure4;//确认按钮
    private ImageView backbtn;
    private String stuid,stu1id,stu2id,stu3id,v1code,v2code,v3code;
    private List<View> views;//所有view
    private List<String> titles;
    private PagerTabStrip pagertab;
    private int[] images;//图片的切换
    private ImageView select;//选择人数strip
    private ViewPager vp;
    private ViewPagerAdapter vpa;
    private static final int SUCCESS = 1;
    private static final int FAIL = 2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    String re = (String)msg.obj;
                    Toast.makeText(selectRoom.this,"选择成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(selectRoom.this,success.class);
                    startActivity(intent);
                    break;
                case FAIL:
                    Toast.makeText(selectRoom.this,"选择失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectroom);
        stuid = getIntent().getStringExtra("username");
        initview();
        SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
        stuid = pref.getString("stuid", "1301210899");
    }

    private void requestForDetail(final Map<String,String> map) { new Thread(new Runnable() {
        @Override
        public void run() {
            String re = HttpsClient.HttpGet("https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom?",map);
            int errorcode = 0;
            JSONObject jo = null;
            try {
                jo = new JSONObject(re);
                errorcode = jo.getInt("errcode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(errorcode==0)
            {
                Message msg = new Message();
                msg.what = SUCCESS;
                msg.obj = re;
                mHandler.sendMessage(msg);
            }else{
                Message msg = new Message();
                msg.what = FAIL;
                msg.obj = re;
                mHandler.sendMessage(msg);
            }

        }
    }).start();
    }

    private void initview() {
        backbtn = findViewById(R.id.back);
        backbtn.setOnClickListener(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        vp = findViewById(R.id.viewpager);
        pagertab = findViewById(R.id.pagerTab);
        //设置Tab选中时的颜色
        pagertab.setTabIndicatorColor(getResources().getColor(R.color.skyblue));
        //设置Tab是否显示下划线
        pagertab.setDrawFullUnderline(true);
        //设置Tab背景色
        pagertab.setBackgroundColor(getResources().getColor(R.color.white));
        //设置Tab间的距离？我感觉是这样
        pagertab.setTextSpacing(10);
        views = new ArrayList<View>();
        titles = new ArrayList<String>();
        View view1 = inflater.inflate(R.layout.selectone,null);
        View view2 = inflater.inflate(R.layout.selecttwo,null);
        View view3 = inflater.inflate(R.layout.selectthree,null);
        View view4 = inflater.inflate(R.layout.selectfour,null);

        Estu1ido = view1.findViewById(R.id.sone_xuehao);
        Estu1ido.setText(stuid);

        Estu1idtw = view2.findViewById(R.id.stwo1_xuehao);
        Ev1codetw = view2.findViewById(R.id.stwo1_vcode);

        Estu1idt = view3.findViewById(R.id.sthr1_xuehao);
        Estu2idt = view3.findViewById(R.id.sthr2_xuehao);
        Ev1codet = view3.findViewById(R.id.sthr1_vcode);
        Ev2codet = view3.findViewById(R.id.sthr2_vcode);

        Estu1idf = view4.findViewById(R.id.sf1_xuehao);
        Estu2idf = view4.findViewById(R.id.sf2_xuehao);
        Estu3idf = view4.findViewById(R.id.sf3_xuehao);
        Ev1codef = view4.findViewById(R.id.sf1_vcode);
        Ev2codef = view4.findViewById(R.id.sf2_vcode);
        Ev3codef = view4.findViewById(R.id.sf3_vcode);

        ensure1 = view1.findViewById(R.id.ensure1);
        ensure2 = view2.findViewById(R.id.ensure2);
        ensure3 = view3.findViewById(R.id.ensure3);
        ensure4 = view4.findViewById(R.id.ensure4);

        ensure1.setOnClickListener(this);
        ensure2.setOnClickListener(this);
        ensure3.setOnClickListener(this);
        ensure4.setOnClickListener(this);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        titles.add("单人");
        titles.add("双人");
        titles.add("三人");
        titles.add("四人");
        vpa = new ViewPagerAdapter(views,titles,this);
        vp.setAdapter(vpa);
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ensure1)
        {
            Map<String,String> map = new HashMap<String,String>();
            map.put("num","1");
            map.put("stuid",stuid);
            requestForDetail(map);
        }
        if(view.getId() == R.id.ensure2)
        {
            Map<String,String> map = new HashMap<String,String>();
            map.put("num","2");
            map.put("stuid",stuid);
            stu1id = Estu1idtw.getText().toString();
            v1code = Ev1codetw.getText().toString();
            map.put("stu1id",stu1id);
            map.put("v1code",v1code);
            requestForDetail(map);
        }
        if(view.getId() == R.id.ensure3)
        {
            Map<String,String> map = new HashMap<String,String>();
            map.put("num","3");
            map.put("stuid",stuid);
            stu1id = Estu1idt.getText().toString();
            v1code = Ev1codet.getText().toString();
            stu2id = Estu2idt.getText().toString();
            v1code = Ev2codet.getText().toString();
            map.put("stu1id",stu1id);
            map.put("v1code",v1code);
            map.put("stu2id",stu1id);
            map.put("v2code",v1code);
            requestForDetail(map);
        }
        if(view.getId() == R.id.ensure4)
        {
            Map<String,String> map = new HashMap<String,String>();
            map.put("num","4");
            map.put("stuid",stuid);
            stu1id = Estu1idf.getText().toString();
            v1code = Ev1codef.getText().toString();
            stu2id = Estu2idf.getText().toString();
            v2code = Ev2codef.getText().toString();
            stu3id = Estu3idf.getText().toString();
            v3code = Ev3codef.getText().toString();
            map.put("stu1id",stu1id);
            map.put("v1code",v1code);
            map.put("stu1id",stu2id);
            map.put("v1code",v2code);
            map.put("stu1id",stu3id);
            map.put("v1code",v3code);
            requestForDetail(map);
        }
        if(view.getId()==R.id.back)
        {
            finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
