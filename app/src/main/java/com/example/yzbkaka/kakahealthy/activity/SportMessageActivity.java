package com.example.yzbkaka.kakahealthy.activity;

import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.base.BaseActivity;
import com.example.yzbkaka.kakahealthy.db.DatasDao;
import com.example.yzbkaka.kakahealthy.entity.SaveKeyValues;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SportMessageActivity extends BaseActivity implements View.OnClickListener {
    private TextView finish_plans;  //完成计划
    private TextView sport_days;  //累计运动
    private TextView sport_hot;  //累计消耗热量
    private TextView keepfit_time;  //累计运动时间
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
        keepfit_time = (TextView) findViewById(R.id.sport_time);
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
        DatasDao datasDao = new DatasDao(this);
        cursor = datasDao.selectAll("steps");
        day_values = 1 + cursor.getCount();  //获得运动的天数
        double hot_values = 0;  //热量数据
        int step = 0;  //步数数据
        counts = cursor.getCount();  //获得行数
        list = new ArrayList<>();
        if(counts > 0){  //如果返回的数据不为空
            while(cursor.moveToNext()){
                String hot = cursor.getString(cursor.getColumnIndex("hot"));  //得到hot的列
                double hots = Double.parseDouble(hot);   //进行格式转换
                int steps = cursor.getInt(cursor.getColumnIndex("steps"));  //获得步数
                hot_values = hot_values + hots;
                step = step + steps;

                Map<String,Object> map = new HashMap<>();
                String date_data = cursor.getString(cursor.getColumnIndex("date"));  //日期数据
                int step_data = cursor.getInt(cursor.getColumnIndex("steps"));  //步数数据
                String length_data = cursor.getString(cursor.getColumnIndex("length"));  //步长数据
                String hot_data = cursor.getString(cursor.getColumnIndex("hot"));  //热量数据

                map.put("date" , date_data);
                map.put("step" , step_data);
                map.put("length" , length_data);
                map.put("hot" , hot_data);
                list.add(map);
            }
        }
        hot_values = hot_values + Double.parseDouble(SaveKeyValues.getStringValues("sport_heat","0.00"));  //热量数据
        hot_str = formatDouble(hot_values);  //进行格式转换
        step = step + SaveKeyValues.getIntValues("sport_steps",0);
        scores = (int) (step * 0.5);  //事件
    }


    @Override
    protected void setViewsListener() {
        all_btn.setOnClickListener(this);
        day_btn.setOnClickListener(this);
    }


    @Override
    protected void setViewsFunction() {
        all_lin.setVisibility(View.VISIBLE);  //累计数据可见
        day_lin.setVisibility(View.GONE);  //列表布局不可见
        finish_plans.setText(String.valueOf(plans));
        sport_days.setText(String.valueOf(day_values));
        sport_hot.setText(hot_str);
        keepfit_time.setText(String.valueOf(scores) + "分");
        if(counts > 0){
            SimpleAdapter simpleAdapter = new SimpleAdapter(this,R.layout.step_item,
                    new String[]{"date","step","length","hot"},
                    new int[]{R.id.date , R.id.left_step,R.id.center_length,R.id.right_hot});
            dataList.setAdapter(simpleAdapter);
        }
        else{
            TextView empty = (TextView) findViewById(R.id.null_view);
            dataList.setEmptyView(empty);
            Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
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


    private String formatDouble(Double doubles) {  //进行格式转换
        DecimalFormat format = new DecimalFormat("####.##");
        String distanceStr = format.format(doubles);
        return distanceStr.equals("0") ? "0.00" : distanceStr;
    }
}
