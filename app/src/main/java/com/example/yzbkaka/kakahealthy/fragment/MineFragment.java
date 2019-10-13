package com.example.yzbkaka.kakahealthy.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.base.BaseActivity;
import com.example.yzbkaka.kakahealthy.entity.SaveKeyValues;

import java.util.concurrent.CopyOnWriteArrayList;

import mrkj.library.wheelview.circleimageview.CircleImageView;

/**
 * Created by yzbkaka on 19-10-13.
 */

public class MineFragment extends Fragment implements View.OnClickListener {
    private static final int CHANGE = 200;
    private View view;  //布局
    private Context context;
    private CircleImageView head_image;  //显示头像
    private ImageButton change_values;  //更改信息按钮
    private TextView custom_name;  //用户名称
    private TextView want;
    private LineChartView lineChartView;  //统计图
    private LineChartData data;  //数据集
    private float[] points = new float[7];  //折线点的数组
    private DatasDao datasDao;  //读取数据工具
    private TextView show_steps;  //显示今日已走的步数
    private TextView food;  //食物热量对照表
    private EditText steps;  //步数
    private TextView about;  //关于我们
    private TextView sport_message;  //运动信息
    private TextView plan_btn;  //计划


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        //1、第一部分显示头像、昵称
        head_image = (CircleImageView) view.findViewById(R.id.head_pic);
        custom_name = (TextView) view.findViewById(R.id.show_name);
        change_values = (ImageButton) view.findViewById(R.id.change_person_values);
        change_values.setOnClickListener(this);  //点击跳转到编辑个人信息界面
        //2、第二部分显示当日的步数和历史统计图
        show_steps = (TextView) view.findViewById(R.id.show_steps);
        lineChartView = (LineChartView) view.findViewById(R.id.step_chart);
        if (isAdded()) {
            datasDao = new DatasDao(getContext());
        }
        //显示信息
        showMessage();
        //3.初始化其余相关控件并添加点击事件的监听
        food = (TextView) view.findViewById(R.id.food_hot);
        food.setOnClickListener(this);
        want = (TextView) view.findViewById(R.id.want);
        want.setText("在" + SaveKeyValues.getStringValues("plan_stop_date","2016年6月16日")+"体重达到【"+SaveKeyValues.getIntValues("weight",50)+"】公斤");
        about = (TextView) view.findViewById(R.id.about_btn);
        about.setOnClickListener(this);
        sport_message = (TextView) view.findViewById(R.id.sport_btn);
        sport_message.setOnClickListener(this);
        steps = (EditText) view.findViewById(R.id.change_step);
        steps.setText(SaveKeyValues.getIntValues("step_plan" , 6000) + "");
        plan_btn = (TextView) view.findViewById(R.id.plan_btn);
        plan_btn.setOnClickListener(this);
        return view;
    }


}
