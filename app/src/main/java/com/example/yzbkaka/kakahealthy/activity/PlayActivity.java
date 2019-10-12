package com.example.yzbkaka.kakahealthy.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.application.DemoApplication;
import com.example.yzbkaka.kakahealthy.base.BaseActivity;
import com.example.yzbkaka.kakahealthy.entity.SaveKeyValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PlayActivity extends BaseActivity implements View.OnClickListener {
    private int index;
    private int what;  //设置标记位
    private boolean isNext;
    private boolean isOff;  //判断开始或者暂停
    private TextView playTime;  //运动时间
    private TextView playName;  //运动名称
    private TextView playMessage;  //运动信息
    private boolean isChange;  //布局是否替换
    private TextView playMore;  //运动说明
    private TextView playBack;
    private ImageView imageView;  //运动的示范图
    private ImageView playSwitch;  //开始、暂停运动开关
    private ImageView playNext;  //开始下一个运动
    private AnimationDrawable animationDrawable;  //动画
    private static final String zeroStr = "00:00";
    private Button backSport;  //完成热身的按钮
    private LinearLayout one;  //1号布局
    private LinearLayout two;  //2号布局
    private int doplan;  //设置计划
    private boolean isClose;  //判断线程是否关闭
    private Thread thread;
    private int values;
    private ProgressBar progressBar;  //运动进度条

    private int[] frameRes = new int[]{  //运动画面的id
            R.drawable.donghua1,
            R.drawable.donghua2,
            R.drawable.donghua3,
            R.drawable.donghua4,
            R.drawable.donghua5};

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    int values1 = (int) msg.obj;
                    if (isNext){
                        return false;
                    }
                    if (values1 == 11){
                        handler.removeMessages(1);
                        animationDrawable.stop();
                        isOff = false;
                        isClose = true;
                    }
                    progressBar.setProgress(values);
                    if (values1 < 10){
                        playTime.setText("00:0"+values);
                    }else {
                        playTime.setText("00:" + values);
                    }
                    if (values1 == 12){
                        playSwitch.setImageResource(R.mipmap.mrkj_play_start);
                        Toast.makeText(PlayActivity.this, "运动结束", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    protected void setActivityTitle() {  //设置标题
        initTitle();
        setTitle("运动", this);
        setMyBackGround(R.color.watm_background_gray);
        setTitleTextColor(R.color.theme_blue_two);
        setTitleLeftImage(R.mipmap.mrkj_back_blue);
    }


    @Override
    protected void getLayoutToView() {  //将布局加载到活动中
        setContentView(R.layout.activity_play);
    }


    @Override
    protected void initValues() {  //初始化数据
        isNext = false;
        index = getIntent().getIntExtra("play_type",0);  //获取Intent中传递的消息
        what = getIntent().getIntExtra("what",0);
        doplan = getIntent().getIntExtra("do_hint",0);
        if (doplan == 1){
            SaveKeyValues.putIntValues("do_hint",0);
        }
    }


    @Override
    protected void initViews() {  //初始化控件
        imageView = (ImageView) findViewById(R.id.play_image);
        playName = (TextView) findViewById(R.id.play_name);
        playMore = (TextView) findViewById(R.id.play_more);
        playMessage = (TextView) findViewById(R.id.play_message);
        playBack = (TextView) findViewById(R.id.play_back);
        playSwitch = (ImageView) findViewById(R.id.play_on_or_off);
        progressBar = (ProgressBar) findViewById(R.id.play_progress);
        playTime = (TextView) findViewById(R.id.play_time);
        one = (LinearLayout) findViewById(R.id.down_one);  //布局
        two = (LinearLayout) findViewById(R.id.down_two);
        playNext = (ImageView) findViewById(R.id.play_next);
        backSport = (Button) findViewById(R.id.back_sport);
    }


    @Override
    protected void setViewsListener() {  //设按钮监听
        playBack.setOnClickListener(this);
        playMore.setOnClickListener(this);
        playSwitch.setOnClickListener(this);
        playNext.setOnClickListener(this);
        backSport.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_on_or_off:  //开关
                isNext = false;
                if (!isOff){  //如果是正在播放阶段
                    animationDrawable.start();  //播放动画
                    values = 0;
                    playSwitch.setImageResource(R.mipmap.mrkj_play_stop);
                    isOff = true;
                    isClose = false;
                    runThread();

                }else {  //如果是暂停阶段
                    animationDrawable.stop();  //停止动画
                    values = 0;
                    isOff = false;
                    playSwitch.setImageResource(R.mipmap.mrkj_play_start);
                    isClose = true;
                    handler.removeMessages(1);
                }
                break;

            case R.id.play_more:  //运动说明
                if(isChange == false){
                    setShow2();
                    isChange = true;
                }
                break;

            case R.id.play_back:
                if (isChange == true){
                    setShow();
                    isChange = false;
                }
                break;

            case R.id.play_next://播放下一个
                isNext = true;
                index++;
                if (index > 4){
                    Toast.makeText(this,"热身完毕请点击返回运动！",Toast.LENGTH_SHORT).show();
                }else {
                    animationDrawable.stop();
                    isOff = false;
                    isClose = true;
                    handler.removeMessages(1);
                    playName.setText(DemoApplication.shuoming[index]);
                    imageView.setImageResource(frameRes[index]);
                    animationDrawable = (AnimationDrawable) imageView.getDrawable();
                    if (animationDrawable.isRunning()){
                        animationDrawable.stop();
                    }
                    playSwitch.setImageResource(R.mipmap.mrkj_play_start);
                    progressBar.setProgress(0);
                    playTime.setText("00:00");

                }
                break;

            case R.id.back_sport:
                finish();  //结束该Activity
                break;

            default:
                break;
        }

    }


    @Override
    protected void setViewsFunction(){  //设置功能
        if (what == 1){
            playNext.setVisibility(View.GONE);  //设置不可见
            backSport.setVisibility(View.GONE);
        }else {
            playNext.setVisibility(View.VISIBLE);  //设置为可见
            backSport.setVisibility(View.VISIBLE);
        }
        setShow();
        playName.setText(DemoApplication.shuoming[index]);
        imageView.setImageResource(frameRes[index]);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();  //设置动画
        if (animationDrawable.isRunning()){
            animationDrawable.stop();
        }

        String message = getMyText(index);
        //Log.e("说明","【"+ message +"】");
        playMessage.setText(message);

    }


    private void setShow(){  //展示布局
        one.setVisibility(View.VISIBLE);  //1号布局设置为可见
        two.setVisibility(View.GONE);  //2号布局设置为不可见
    }


    private void setShow2(){
        one.setVisibility(View.GONE);  //设置1号可见
        two.setVisibility(View.VISIBLE);  //设置2号不可见
    }


    private String getMyText(int type){  //获得运动说明
        InputStream inputStream = null;  //输入流
        BufferedReader reader = null;  //读取
        StringBuffer buffer = new StringBuffer();
        try {
            inputStream = getAssets().open("sport/sport"+type+".txt");  //获取手机目录里面的文件
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            while ((str = reader.readLine()) != null){
                buffer.append(str);  //读取信息
            }
            String text = buffer.toString();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private void runThread(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isClose){
                    try {
                        Thread.sleep(1000);
                        Message message = Message.obtain();
                        message.obj = ++values;
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();
    }


    @Override
    protected void onRestart() {  //当活动重新启动的时候
        super.onRestart();
        setShow();  //重新设置展示布局
        values = 0;
        progressBar.setProgress(0);  //进度条设置为0
        playTime.setText(zeroStr);  //时间恢复为默认
        if (animationDrawable.isRunning()){  //停止动画
            animationDrawable.stop();
            isOff = false;
            playSwitch.setImageResource(R.mipmap.mrkj_play_start);  //switch设置为默认
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (thread != null){  //关闭线程
            isClose = true;
        }
    }
}
