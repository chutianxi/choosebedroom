package company.chutianxi.bedroom2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017\12\27 0027.
 */

public class success extends Activity implements View.OnClickListener{
    private ImageView backbtn;//顶部栏返回首页
    private Button mainbtn;//返回首页
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        backbtn = findViewById(R.id.back);
        backbtn.setOnClickListener(this);
        mainbtn = findViewById(R.id.success_back);
        mainbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back)
        {
/*            Intent intent = new Intent(this,main.class);
            startActivity(intent);*/
            finish();
        }
        if(view.getId() == R.id.success_back)
        {
/*            Intent intent = new Intent(this,main.class);
            startActivity(intent);*/
            finish();
        }
    }
}
