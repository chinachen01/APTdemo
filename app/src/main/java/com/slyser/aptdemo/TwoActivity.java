package com.slyser.aptdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.slyser.annotation.Router;

/**
 * author: chenyong(<a href="chenyong@danlu.com">chenyong@danlu.com</a>)<br/>
 * version: 1.0.0<br/>
 * since: 2018/8/6 下午7:31<br/>
 *
 * <p>
 * 内容描述区域
 * </p>
 */
@Router(name = "router_two")
public class TwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }
}
