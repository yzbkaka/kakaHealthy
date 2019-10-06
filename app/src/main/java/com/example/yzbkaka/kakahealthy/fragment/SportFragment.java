package com.example.yzbkaka.kakahealthy.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.activity.PlayActivity;
import com.example.yzbkaka.kakahealthy.entity.PMInfo;
import com.example.yzbkaka.kakahealthy.entity.SaveKeyValues;
import com.example.yzbkaka.kakahealthy.entity.StepDetector;
import com.example.yzbkaka.kakahealthy.entity.TodayInfo;
import com.example.yzbkaka.kakahealthy.service.StepCounterService;
import com.example.yzbkaka.kakahealthy.utils.Constant;
import com.example.yzbkaka.kakahealthy.utils.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import mrkj.library.wheelview.circlebar.CircleBar;

/**
 * Created by yzbkaka on 19-10-2.
 */

public class SportFragment extends Fragment {
    private static final int WEATHER_MESSAGE = 1;  //显示天气信息
    private static final int STEP_PROGRESS = 2;  //显示步数信息
    private View view;  //界面的布局
    private TextView cityName;  //展示的城市名字
    private TextView cityTemperature;  //展示的温度
    private TextView cityAirQuality;  //展示的空气质量
    private CircleBar circleBar;  //圆形控件进度条
    private TextView mileage;  //里程
    private TextView heat;  //热量
    private TextView wantSteps;  //显示目标步数
    private ImageButton warm;  //跳转按钮
    private TodayInfo todayInfo;  //下载的今日的天气
    private PMInfo pmInfo;  //下载的今日的pm
    private String weatherUrl;  //天气预报的接口网址
    private String queryCityName;  //查询天气的城市
    private int customSteps;  //记录的用户步数
    private int customStepLength;  //用户的步长
    private int customWeight;  //用户的体重
    private Thread getStepThread;  //记步服务的线程
    private Intent stepService;  //记步数服务
    private boolean isStop;  //是否在运行子线程
    private Double distanceValues;  //路程：米
    private int stepValues;  //步数
    private Double heatValues;  //热量
    private int duration;  //动画时间
    private Context context;

    private Handler handler = new Handler(new Handler.Callback() {  //异步消息处理机制
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case WEATHER_MESSAGE:  //获取天气
                    String jsonStr = (String)message.obj;  //拿出之前获得的JSON数据
                    if(jsonStr != null){
                        setDownLoadMessageToView(jsonStr);  //对JSON数据进行读取并显示在控件上
                    }
                    break;
                case STEP_PROGRESS:  //获取步数
                    stepValues = StepDetector.CURRENT_SETP;  //获取记步的步数
                    circleBar.update(stepValues,duration);  //将步数进度显示在进度条上
                    duration = 0;
                    SaveKeyValues.putIntValues("sport_steps",stepValues);  //储存当前的步数
                    distanceValues = stepValues * customStepLength * 0.01 * 0.001;  //计算里程
                    mileage.setText(formatDouble(distanceValues) + context.getString(R.string.km));  //显示里程
                    SaveKeyValues.putStringValues("sport_distance",formatDouble(distanceValues));  //存储里程
                    heatValues = customWeight * distanceValues * 1.036;  //计算消耗热量
                    heat.setText(formatDouble(heatValues) + context.getString(R.string.cal));  //显示热量
                    SaveKeyValues.putStringValues("sport_heat",formatDouble(heatValues));  //存储热量
                    break;
            }
            return false;
        }
    });

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){  //创建视图
        view = inflater.inflate(R.layout.fragment_sports,container,false);  //设置view
        SaveKeyValues.createSharePreferences(getContext());  //开启存储服务
        initView();  //初始化控件
        initValues();  //初始化数据并开启记步服务
        setNature();  //设置功能
        if(StepDetector.CURRENT_SETP > customSteps){  //当实际步数超过用户的步数
            Toast.makeText(context, "今日的目标步数已完成", Toast.LENGTH_SHORT).show();
        }
        if(SaveKeyValues.getIntValues("do_hint",0) == 1 && (System.currentTimeMillis() > (SaveKeyValues.getLongValues("show_hint",0) + Constant.DAY_FOR_24_HOURS))){  //如果在一天以内没有完成目标
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());  //设置弹窗
            alertDialog.setTitle("提示");
            alertDialog.setMessage("目标步数没有完成");
            alertDialog.setPositiveButton("点击确定不再提示",
                    new DialogInterface.OnClickListener() {  //设置弹窗的确定按钮功能
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SaveKeyValues.putIntValues("do_hint",0);  //点击后存值
                        }
                    });
            //alertDialog.create();  //创建弹窗
            alertDialog.show();  //显示弹窗
        }
        return view;
    }


    private void initView(){  //初始化控件
        circleBar = (CircleBar)view.findViewById(R.id.show_progress);
        cityName = (TextView)view.findViewById(R.id.city_name);
        cityTemperature = (TextView)view.findViewById(R.id.temperature);
        cityAirQuality = (TextView)view.findViewById(R.id.air_quality);
        warm = (ImageButton)view.findViewById(R.id.warm_up);
        mileage = (TextView)view.findViewById(R.id.mileage_txt);
        heat = (TextView)view.findViewById(R.id.heat_txt);
        wantSteps = (TextView)view.findViewById(R.id.want_steps);
    }


    private void initValues(){  //初始化数据并开启记步服务
        queryCityName = SaveKeyValues.getStringValues("city","北京");  //获取查询的城市
        try{
            weatherUrl = String.format("http://op.juhe.cn/onebox/weather/query?cityname="+queryCityName +"&key="+ Constant.APP_KEY, URLEncoder.encode(queryCityName,"utf-8"));  //获取网络链接URL
            downLoadDataFromNet();  //下载网络数据
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        isStop = false;  //初始值为false
        duration = 800;  //初始动画时长ms
        customSteps = SaveKeyValues.getIntValues("step_plan",6000);  //获取用户的步数
        customStepLength = SaveKeyValues.getIntValues("length",70);  //获取用户的步长
        customWeight = SaveKeyValues.getIntValues("weight",50);  //获取用户的体重
        int historyValues = SaveKeyValues.getIntValues("sport_steps",0);  //获取历史步数
        int serviceValues = StepDetector.CURRENT_SETP;  //获得步数
        boolean isLaunch = getArguments().getBoolean("is_launch",false);  //数据处理
        if(isLaunch){
            StepDetector.CURRENT_SETP = historyValues + serviceValues;
        }
        stepService = new Intent(getContext(),StepCounterService.class);  //开启记步服务
        getContext().startService(stepService);
    }


    private void setNature(){  //设置功能
        circleBar.setcolor(R.color.theme_blue_two);  //设置进度条颜色
        circleBar.setMaxstepnumber(customSteps);  //设置进度条的最大值
        getServiceValue();  //获取步数信息
        warm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),PlayActivity.class);  //跳转到热身界面
                intent.putExtra("play_type",0);  //传递信息
                intent.putExtra("what",0);
                startActivity(intent);
            }
        });
        wantSteps.setText("今日目标：" + customSteps + "步");
    }


    private void downLoadDataFromNet(){  //从api中获取天气数据JSON
        new Thread(new Runnable() {  //开启子线程
            @Override
            public void run() {
                String str = HttpUtil.getJSONStr(weatherUrl);
                Message message = new Message();  //Handler机制
                message.obj = str;  //将获得的JSON存入到message当中
                message.what = WEATHER_MESSAGE;
                handler.sendMessage(message);
            }
        }).start();
    }


    private void getServiceValue(){  //获取步数信息
        if(getStepThread == null){
            getStepThread = new Thread(){
                @Override
                public void run(){  //重写Thread内部的方法
                    super.run();
                    while(!isStop){  //当线程没有停止
                        try{
                            Thread.sleep(1000);  //每隔一秒发送一条消息给主线程
                            if(StepCounterService.FLAG){  //如果记步服务已经开启
                                handler.sendEmptyMessage(STEP_PROGRESS);  //通知主线程显示步数信息
                            }
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            getStepThread.start();  //开启线程
        }
    }


    private void setDownLoadMessageToView(String resultStr){  //将天气数据显示到界面上
        todayInfo = HttpUtil.parseNowJSON(resultStr);  //获取当日的天气数据
        pmInfo = HttpUtil.parsePMInfoJSON(resultStr);  //获取PM2.5的数据
        if(isAdded()){  //判断fragment是否被加载到Activity当中
            getActivity().runOnUiThread(new Runnable() {  //切换到主线程中去更改UI
                @Override
                public void run() {
                    cityName.setText(context.getString(R.string.city)+queryCityName);
                    cityTemperature.setText(context.getString(R.string.temperature_hint) + todayInfo.getTemperature() + getString(R.string.temperature_unit));
                    cityAirQuality.setText(context.getString(R.string.quality) + pmInfo.getQuality());
                }
            });
        }
    }

    private String formatDouble(Double doubles){  //格式化Double类型数据，使之保留2位小数
        DecimalFormat format = new DecimalFormat("###.##");
        String distanceStr = format.format(doubles);
        return distanceStr.equals("0") ? "0.00" : distanceStr;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(getStepThread);  //移除监听
        isStop = true;  //线程关闭
        getStepThread = null;  //清空线程对象
        stepValues = 0;  //设置初始化步数为0
        duration = 800;  //设置动画时间为800ms
    }

}
