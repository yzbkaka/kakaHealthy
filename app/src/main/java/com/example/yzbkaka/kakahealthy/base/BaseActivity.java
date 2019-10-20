package com.example.yzbkaka.kakahealthy.base;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yzbkaka.kakahealthy.R;

/**
 * Created by yzbkaka on 19-10-7.
 */

public abstract class BaseActivity extends AppCompatActivity {  //抽象类（必须被继承）
    private TextView titleCenter;  //标题的中间部分
    private ImageView titleLeft;  //标题的左边
    private ImageView titleRight;  //标题的右边
    private RelativeLayout titleRelativeLayout;  //标题的布局


    @Override
    protected void onCreate(Bundle savedInstanceState) {  //方法会被继承，然后自动调用
        super.onCreate(savedInstanceState);
        getLayoutToView();  //都是后面定义的抽象方法
        initValues();
        setActivityTitle();
        initViews();
        setViewsListener();
        setViewsFunction();
    }


    public void initTitle(){  //初始化控件
        titleCenter = (TextView) findViewById(R.id.titles);
        titleLeft = (ImageView) findViewById(R.id.left_btn);
        titleRight = (ImageView) findViewById(R.id.right_btn);
        titleLeft.setVisibility(View.INVISIBLE);  //先设置为不可见
        titleRight.setVisibility(View.INVISIBLE);
        titleRelativeLayout = (RelativeLayout) findViewById(R.id.title_back);
    }


    public void setMyBackGround(int color){  //设置背景图
        titleRelativeLayout.setBackgroundResource(color);
    }


    public void setTextViewUnderLine(TextView view){  //设置TextView的下划线
        Paint paint = view.getPaint();
        paint.setColor(getResources().getColor(R.color.btn_gray));  //设置画笔颜色
        paint.setAntiAlias(true);  //设置抗锯齿
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);  //设置下滑线
        view.invalidate();
    }


    public void setTitle(String name){  //设置标题名称
        titleCenter.setText(name);
        titleLeft.setVisibility(View.INVISIBLE);  //设置不可见
    }


    public void setTitle(String name,final Activity activity){  //设置标题返回键功能
        titleCenter.setText(name);
        titleLeft.setVisibility(View.VISIBLE);
        titleLeft.setOnClickListener(new View.OnClickListener() {  //返回键功能
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


    public ImageView setTitleLeft(String name){  //获取标题左边的按钮
        titleCenter.setText(name);
        titleLeft.setVisibility(View.VISIBLE);
        return titleLeft;
    }


    public ImageView setTitle(String name,final Activity activity ,int picID){  //设置标题左 中 右 全部显示
        titleCenter.setText(name);
        titleLeft.setVisibility(View.VISIBLE);
        titleRight.setVisibility(View.VISIBLE);
        if (picID != 0){
            titleRight.setImageResource(picID);
        }
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return titleRight;
    }


    public void setTitleTextColor(int colorID){  //设置标题的文字颜色
        titleCenter.setTextColor(colorID);
    }


    public void setTitleLeftImage(int picID){  //设置标题左侧图片按钮的图片
        titleLeft.setImageResource(picID);
    }


    public void setTitleRightImage(int picID){  //设置标题右侧图片按钮的图片
        titleRight.setImageResource(picID);
    }


    //抽象方法，需要子类进行实现
    protected abstract void setActivityTitle();  //设置标题
    protected abstract void getLayoutToView();  //初始化窗口
    protected abstract void initValues();  //设置初始化的值和变量
    protected abstract void initViews();  //初始化控件
    protected abstract void setViewsListener();  //初始化控件的监听
    protected abstract void setViewsFunction();  //设置相关功能
}