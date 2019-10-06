package com.example.yzbkaka.kakahealthy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.fragment.SportFragment;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_launch",false);  //往bundle里面放入值
        SportFragment sportFragment = new SportFragment();
        sportFragment.setArguments(bundle);  //设置参数
        getSupportFragmentManager().beginTransaction().add(R.id.frag,sportFragment).commit();  //提交事务，进行加载
    }
}
