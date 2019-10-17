package com.example.yzbkaka.kakahealthy.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.base.BaseActivity;
import com.example.yzbkaka.kakahealthy.db.DatasDao;
import com.example.yzbkaka.kakahealthy.entity.SaveKeyValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SportMessageActivity extends BaseActivity implements View.OnClickListener {
    private TextView finish_plans;  //完成计划
    private TextView sport_days;  //累计运动
    private TextView sport_hot;  //累计消耗热量
    private TextView keepfit_scores;  //累计运动时间
    private int plans;  //计划
    private String hot_str;
    private int day_values;  //每日数据
    private int scores;
    private DatasDao datasDao;
    private Button all_btn;  //累计数据按钮
    private Button day_btn;  //记录列表按钮
    private LinearLayout all_lin;  //累计数据布局
    private RelativeLayout day_lin;  //列表布局
    private ListView dataList;
    private int counts;
    private Cursor cursor;
    private List<Map<String , Object>> list;


    @Override
    protected void setActivityTitle() {
        initTitle();  //初始化标题控件
        setTitle("运动记录",this);
        setMyBackGround(R.color.watm_background_gray);
        setTitleTextColor(R.color.theme_blue_two);
        setTitleLeftImage(R.mipmap.mrkj_back_blue);
    }


    @Override
    protected void getLayoutToView() {
        setContentView(R.layout.activity_sport_message);
    }


    @Override
    protected void initViews() {
        finish_plans = (TextView) findViewById(R.id.finish_plan);
        keepfit_scores = (TextView) findViewById(R.id.sport_scores);
        sport_days = (TextView) findViewById(R.id.sport_day);
        sport_hot = (TextView) findViewById(R.id.sport_hot);
        all_lin = (LinearLayout) findViewById(R.id.all_data);
        all_btn = (Button) findViewById(R.id.data_all);
        day_lin = (RelativeLayout) findViewById(R.id.day_data);
        day_btn = (Button) findViewById(R.id.data_day);
        dataList = (ListView) findViewById(R.id.data_list);
    }


    @Override
    protected void initValues() {
        plans = SaveKeyValues.getIntValues("finish_plan",0);  //获得存储的计划数据
        /*DatasDao datasDao = new DatasDao(this);
        cursor = datasDao.selectAll("steps");*/
        day_values = 1 + cursor.getCount();
        double hot_values = 0;  //热量数据
        int step = 0;  //步数数据
        counts = cursor.getCount();
        list = new ArrayList<>();
    }



    @Override
    protected void setViewsListener() {
        all_btn.setOnClickListener(this);
        day_btn.setOnClickListener(this);
    }


    @Override
    protected void setViewsFunction() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.data_all:
                all_lin.setVisibility(View.VISIBLE);
                day_lin.setVisibility(View.GONE);
                break;
            case R.id.day_data:
                all_lin.setVisibility(View.GONE);
                day_lin.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
