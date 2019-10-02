package com.example.yzbkaka.kakahealthy.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

}
