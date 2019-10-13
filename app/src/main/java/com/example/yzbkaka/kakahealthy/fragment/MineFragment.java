package com.example.yzbkaka.kakahealthy.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.activity.FoodHotListActivity;
import com.example.yzbkaka.kakahealthy.base.BaseActivity;
import com.example.yzbkaka.kakahealthy.entity.SaveKeyValues;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
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
    private TextView want;  //目标体重
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
        //开始初始化控件
        head_image = (CircleImageView) view.findViewById(R.id.head_pic);
        custom_name = (TextView) view.findViewById(R.id.show_name);
        change_values = (ImageButton) view.findViewById(R.id.change_person_values);
        change_values.setOnClickListener(this);  //点击跳转到编辑个人信息界面
        show_steps = (TextView) view.findViewById(R.id.show_steps);
        lineChartView = (LineChartView) view.findViewById(R.id.step_chart);
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
        if (isAdded()) {
            datasDao = new DatasDao(getContext());
        }
        showMessage();  //显示信息
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_person_values:  //编辑个人信息
                startActivityForResult(new Intent(context, CompileDetailsActivity.class), CHANGE);
                break;

            case R.id.about_btn:  //关于我们
                startActivity(new Intent(context, AboutActivity.class));
                break;

            case R.id.sport_btn:  //运动历史
                startActivity(new Intent(context, SportMessageActivity.class));
                break;

            case R.id.plan_btn:  //我的计划
                startActivity(new Intent(context, MinePlanActivity.class));
                break;

            case R.id.food_hot:  //食物热量对照表
                startActivity(new Intent(context, FoodHotListActivity.class));
                break;
            default:
                break;
        }
    }


    public void showMessage() {
        String name = SaveKeyValues.getStringValues("nick", "未填写");  //获取名称
        String image_path = SaveKeyValues.getStringValues("path", "path");  //获取图片路径
        custom_name.setText(name);  //设置用户昵称
        if (!"path".equals(image_path)) {
            Bitmap bitmap = BitmapFactory.decodeFile(image_path);
            head_image.setImageBitmap(bitmap);  //设置用户头像
        }
        int today_steps = SaveKeyValues.getIntValues("sport_steps", 0);  //获得今日步数
        show_steps.setText(today_steps + "步");  //展示今日步数

        //设置图表
        //获取保存的数据
        Cursor cursor = datasDao.selectAll("step");
        int counts = cursor.getCount();
        getDataValues(counts);
    }


    private void getDataValues(int count) {  //绘制折线图
        //用来做X轴的标签
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);//获取日期
        //点的集合
        List<PointValue> list;
        int[] dateArray = new int[7];
        dateArray[6] = day;
        if (count == 0){
           // Log.e("没有数据","1");
            getNestDayDate(dateArray, dateArray.length - 2);
            //Log.e("集合元素", Arrays.toString(dateArray));
            //设置X轴的坐标说明
            List<AxisValue> axisValues = new ArrayList<>();
            for (int i = 0; i < points.length; i++) {
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel(dateArray[i] + "");
                axisValues.add(axisValue);
            }
            Axis axisx = new Axis();  //X轴
            Axis axisy = new Axis();  //Y轴
            axisx.setTextColor(Color.BLACK)
                    .setName("日期")
                    .setValues(axisValues);
            axisy.setTextColor(Color.BLACK)
                    .setName("步数")
                    .setHasLines(true)
                    .setMaxLabelChars(5);
            //添加点
            list = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                list.add(new PointValue(i, 0));
            }
            list.add(new PointValue(6, SaveKeyValues.getIntValues("sport_steps", 0)));
            //设置折线图的集合
            List<Line> lines = new ArrayList<>();
            //添加折线并设置折线
            Line line = new Line(list)
                    .setColor(Color.parseColor("#4592F3"))
                    .setCubic(false)
                    .setHasPoints(false);
            line.setHasLines(true);
            line.setHasLabels(true);
            line.setHasPoints(true);
            lines.add(line);
            //显示折线图
            data = new LineChartData();
            data.setLines(lines);
            data.setAxisYLeft(axisy);
            data.setAxisXBottom(axisx);
        }else if(count > 0 && count < 7){//数据库中数据大于0小于6
            //Log.e("有数据","7个以下");
            getNestDayDate(dateArray, dateArray.length - 2);
            //Log.e("集合元素", Arrays.toString(dateArray));
            List<AxisValue> axisValues = new ArrayList<>();
            for (int i = 0; i < points.length; i++) {
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel(dateArray[i] + "");
                axisValues.add(axisValue);
            }
            Axis axisx = new Axis();
            Axis axisy = new Axis();
            axisx.setTextColor(Color.BLACK)
                    .setName("日期")
                    .setValues(axisValues);
            axisy.setTextColor(Color.BLACK)
                    .setName("步数")
                    .setHasLines(true)
                    .setMaxLabelChars(5);
            //设置数据点
            list = new ArrayList<>();
            if (count != 6){
                for (int i = 0; i < 6 - count; i++) {
                    list.add(new PointValue(i, 0));
                }
            }
            //获取游标用来检索数据
            Cursor cursor = datasDao.selectAll("step");
            int i = count;
            while (cursor.moveToNext()){
                int a = cursor.getInt(cursor.getColumnIndex("steps"));
                list.add(new PointValue(6 - (i--), a));
            }
            cursor.close();
            //加入当天数据
            list.add(new PointValue(6, SaveKeyValues.getIntValues("sport_steps", 0)));
            List<Line> lines = new ArrayList<>();
            Line line = new Line(list)
                    .setColor(Color.parseColor("#4592F3"))
                    .setCubic(false)
                    .setHasPoints(false);
            line.setHasLines(true);
            line.setHasLabels(true);
            line.setHasPoints(true);
            lines.add(line);
            data = new LineChartData();
            data.setLines(lines);
            data.setAxisYLeft(axisy);
            data.setAxisXBottom(axisx);
        }else{
            //Log.e("有数据","7个以上");
            getNestDayDate(dateArray, dateArray.length - 2);
            //Log.e("集合元素", Arrays.toString(dateArray));
            List<AxisValue> axisValues = new ArrayList<>();
            for (int i = 0; i < points.length; i++) {
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel(dateArray[i] + "");
                axisValues.add(axisValue);
            }
            Axis axisx = new Axis();
            Axis axisy = new Axis();
            axisx.setTextColor(Color.BLACK)
                    .setName("日期")
                    .setValues(axisValues);
            axisy.setTextColor(Color.BLACK)
                    .setName("步数")
                    .setHasLines(true)
                    .setMaxLabelChars(5);
            list = new ArrayList<>();
            int length = count - 6 + 1;//开始去元素的ID
            for (int i = length; i <= count ; i ++){
                int b = 0;
                Cursor cursor = datasDao.selectValue2("step",null,"_id=?",new String[]{String.valueOf(i)},null,null,null);
                while (cursor.moveToNext()){
                    int a = cursor.getInt(cursor.getColumnIndex("steps"));
                    list.add(new PointValue(b, a));
                }
                cursor.close();
                b++;
            }
        }
        lineChartView.setLineChartData(data);
    }


    /*private void getDateTest() {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            AxisValue axisValue = new AxisValue(i);
            axisValue.setLabel((i + 8) + "");
            axisValues.add(axisValue);
        }
        Axis axisx = new Axis();
        Axis axisy = new Axis();
        axisx.setTextColor(Color.BLACK)
                .setName("日期")
                .setValues(axisValues);
        axisy.setTextColor(Color.BLACK)
                .setName("步数")
                .setHasLines(true)
                .setMaxLabelChars(5);
        List<PointValue> values = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            points[i] = (int) (Math.random() * 1000 + 5000);
            values.add(new PointValue(i, points[i]));
            //Log.e("运行" + "【" + i + "】", points[i] + "");
        }
        List<Line> lines = new ArrayList<>();
        Line line = new Line(values)
                .setColor(Color.parseColor("#4592F3"))
                .setCubic(false)
                .setHasPoints(false);
        line.setHasLines(true);
        line.setHasLabels(true);
        line.setHasPoints(true);
        lines.add(line);
        data = new LineChartData();
        data.setLines(lines);
        data.setAxisYLeft(axisy);
        data.setAxisXBottom(axisx);
        lineChartView.setLineChartData(data);
    }*/


    private void getNestDayDate(int[] dateList, int k) {  //获取未来的6天日期
        Calendar calendar = Calendar.getInstance();
        for (int i = k; i >= 0; i--) {
            calendar.add(Calendar.DATE, -1);
            dateList[i] = calendar.get(Calendar.DAY_OF_MONTH);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE && resultCode == Activity.RESULT_OK) {
            showMessage();
            //Log.e("返回", "success");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!"".equals(steps.getText().toString())){
            SaveKeyValues.putIntValues("step_plan",Integer.parseInt(steps.getText().toString()));
        }else {
            SaveKeyValues.putIntValues("step_plan",6000);
        }
    }
}
