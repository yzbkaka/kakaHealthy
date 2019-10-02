package com.example.yzbkaka.kakahealthy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakahealthy.R;

import mrkj.library.wheelview.circlebar.CircleBar;

/**
 * Created by yzbkaka on 19-10-2.
 */

public class SportFragment extends Fragment {
    private static final int WEATHER_MESSAGE = 1;  //显示天气信息
    private static final int STEP_PROGRESS = 2;  //显示步数信息
    private View view;  //界面的布局
    private TextView cityName;
    private TextView citytemperature;
    private TextView cityAirQuality;
    private CircleBar circleBar;  //圆形控件进度条
    private TextView mileage;  //里程
    private TextView heat;  //热量
    private TextView wantSteps;  //目标步数
    private ImageButton warm;  //跳转按钮
    private TodayInfo todayInfo;  //今日的天气
    private PMInfo pmInfo;  //pm
    private String weatherUrl;  //天气预报的接口
    private String queryCityName;  //查询天气的城市
    private int customSteps;  //用户的步数
    private int cuotomStepLength;  //用户的步长
    private int cuotomWeight;  //用户的体重
    private Thread getStepThread;  //线程
    private Intent stepService;  //记步数服务
    private boolean isStop;  //是否运行子线程
    private double distanceValues;  //路程：米
    private int stepValues;  //步数
    private double heatValues;  //热量
    private int duration;  //动画时间
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_sports,container,false);  //设置view
        initView();  //初始化控件
        initValues();  //初始化数据
        setNature(); //设置功能
        return view;
    }

    public void initView(){
        circleBar = (CircleBar)view.findViewById(R.id.show_progress);
        cityName = (TextView)view.findViewById(R.id.city_name);
        citytemperature = (TextView)view.findViewById(R.id.temperature);
        cityAirQuality = (TextView)view.findViewById(R.id.air_quality);
        warm = (ImageButton)view.findViewById(R.id.warm_up);
        mileage = (TextView)view.findViewById(R.id.mileage_txt);
        heat = (TextView)view.findViewById(R.id.heat_txt);
        wantSteps = (TextView)view.findViewById(R.id.want_steps);
    }

    public void initValues(){

    }

    public void setNature(){
        circleBar.setcolor(R.color.theme_blue_two);  //设置进度条颜色
        circleBar.setMaxstepnumber(customSteps);  //设置进度条的最大值
        getServiceValue();
        warm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "跳转热身界面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),PlayActivity.class);  //跳转到热身界面
                intent.putExtra("play_type",0);
                intent.putExtra("what",0);
                startActivity(intent);
            }
        });
        wantSteps.setText("今日目标：" + customSteps + "步");
    }

}
