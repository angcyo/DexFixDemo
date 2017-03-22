package com.angcyo.dexfixdemo;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static String patchFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dex/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //测试补丁输出
                Demo.test();
            }
        });

        findViewById(R.id.button_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //打补丁
                    Fix.injectDexElements(MainActivity.this, patchFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    T_.show(e.getMessage());
                }
            }
        });

        /**复制补丁文件到SD卡*/
        UI.copyAssetsTo(this, "classes2.dex", patchFilePath + "out_dex.dex");
    }
}
