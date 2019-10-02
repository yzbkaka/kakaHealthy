package com.example.yzbkaka.kakahealthy.fragment;

import android.content.Context;
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
import com.example.yzbkaka.kakahealthy.utils.HttpUtil;

import java.text.DecimalFormat;

import mrkj.library.wheelview.circlebar.CircleBar;

/**
 * Created by yzbkaka on 19-10-2.
 */

public class SportFragment extends Fragment {
    private static final int WEATHER_MESSAGE = 1;  //显示天气信息
    private static final int STEP_PROGRESS = 2;  //显示步数信息
    private View view;  //界面的布局
    private TextView cityName;
    private TextView cityTemperature;
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
    private int customStepLength;  //用户的步长
    private int customWeight;  //用户的体重
    private Thread getStepThread;  //线程
    private Intent stepService;  //记步数服务
    private boolean isStop;  //是否运行子线程
    private Double distanceValues;  //路程：米
    private int stepValues;  //步数
    private Double heatValues;  //热量
    private int duration;  //动画时间
    private Context context;

    private Handler handler = new Handler(new Handler.Callback() {  //异步消息处理机制
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case WEATHER_MESSAGE:  //天气
                    String jsonStr = (String)message.obj;
                    if(jsonStr != null){
                        setDownLoadMessageToView(jsonStr);  //对JSON数据进行读取并显示在控件上
                    }
                    break;
                case STEP_PROGRESS:  //步数
                    stepValues = StepDetector.CURRENT_SETP;  //获取记步的步数
                    circleBar.update(stepValues,duration);  //将步数进度显示在进度条上
                    duration = 0;
                    SaveKeyValues.putIntValues("sport_steps",stepValues);  //储存当前的步数
                    distanceValues = stepValues * customStepLength * 0.01 * 0.001;  //计算里程
                    mileage.setText(formatDouble(distanceValues) + context.getString(R.string.km));  //显示里程
                    SaveKeyValues.putStringValues("sport_distance",formatDouble(distanceValues));
                    heatValues = customWeight * distanceValues * 1.036;  //计算消耗热量
                    heat.setText(formatDouble(heatValues) + context.getString(R.string.cal));  //显示热量
                    SaveKeyValues.putStringValues("sport_heat",formatDouble(heatValues));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_sports,container,false);  //设置view
        initView();  //初始化控件
        initValues();  //初始化数据
        setNature(); //设置功能
        return view;
    }

    private void initView(){
        circleBar = (CircleBar)view.findViewById(R.id.show_progress);
        cityName = (TextView)view.findViewById(R.id.city_name);
        cityTemperature = (TextView)view.findViewById(R.id.temperature);
        cityAirQuality = (TextView)view.findViewById(R.id.air_quality);
        warm = (ImageButton)view.findViewById(R.id.warm_up);
        mileage = (TextView)view.findViewById(R.id.mileage_txt);
        heat = (TextView)view.findViewById(R.id.heat_txt);
        wantSteps = (TextView)view.findViewById(R.id.want_steps);
    }

    private void initValues(){

    }

    private void setNature(){
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

    private void downLoadDataFromNet(){  //从api中获取天气数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = HttpUtil.getJSONStr(weatherUrl);
                Message message = Message.obtain();  //Handler机制
                message.obj = str;
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
                            Thread.sleep(1000);
                            if(StepConterService.FLAG){
                                handler.sendEmptyMessage(STEP_PROGRESS);
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

    private void setDownLoadMessageToView(String resultStr){  //将天气数据显示出来
        todayInfo = HttpUtil.parseNowJSON(resultStr);  //获取当日的天气信息
        pmInfo = HttpUtil.parsePMInfoJSON(resultStr);  //获取PM2.5的数据
        if(isAdded()){
            cityName.setText(context.getString(R.string.city) + queryCityName);
            cityTemperature.setText(context.getString(R.string.temperature_hint) + todayInfo.getTemperature() + getString(R.string.temperature_unit));
            cityAirQuality.setText(context.getString(R.string.quality) + pmInfo.getQuality());
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
