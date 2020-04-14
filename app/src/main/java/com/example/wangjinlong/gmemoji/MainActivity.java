package com.example.wangjinlong.gmemoji;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private Button btn_main_editText;
    private Button btn_main_listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListentener();
        initDatas();
    }

    /**
     * 初始化控件
     */
    private void initView()
    {
        btn_main_editText= (Button) findViewById(R.id.btn_main_editText);
        btn_main_listView= (Button) findViewById(R.id.btn_main_listView);
    }

    /**
     * 初始化监听器
     */
    public void initListentener(){
        btn_main_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentE= new Intent(MainActivity.this, EditTextActivity.class);
                startActivity(intentE);
            }
        });

        btn_main_listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentL= new Intent(MainActivity.this, ListViewBarEditActivity.class);
                startActivity(intentL);
            }
        });
    }

    /**
     * 初始化布局数据
     */
    private void initDatas(){
    }



}
