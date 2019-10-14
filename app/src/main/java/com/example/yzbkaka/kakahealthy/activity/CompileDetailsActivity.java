package com.example.yzbkaka.kakahealthy.activity;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.base.BaseActivity;
import com.example.yzbkaka.kakahealthy.entity.SaveKeyValues;

import java.io.File;
import java.util.Calendar;
import java.util.Map;

import mrkj.library.wheelview.circleimageview.CircleImageView;

public class CompileDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final int PHOTO_REQUEST_CAMERA = 1;  // 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;  // 从相册中选择
    private static final int PHOTO_REQUEST_GALLERY2 = 4;  // 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;  // 结果
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";  // 图片名称
    //头像
    private CircleImageView head_image;  //显示头像
    private TextView change_image;  //更换头像
    private String path;  //头像的路径
    private File tempFile;  //图片路径
    //昵称
    private String nick_str;  //用户昵称
    private EditText change_nick;  //修改昵称
    //性别
    private RadioGroup change_gender;  //更改性别
    private String sex_str;  //性别
    //生日
    private TextView change_birthDay;  //更改生日
    private String date;
    private int birth_year;
    private int birth_month;
    private int birth_day;
    //当日日期
    private int now_year;  //年
    private int now_month;  //月
    private int now_day;  //天
    //身高
    private EditText change_height;  //更改身高
    private int height;
    //体重
    private EditText change_weight;  //更改体重
    private int weight;
    //步长
    private EditText change_length;  //更改不长
    private int length;

    private Button change_OK_With_Save;  //确定保存


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compile_details);
    }


    @Override
    protected void setActivityTitle() {  //设置标题栏
        initTitle();
        setTitle("更改个人信息", this);
        setMyBackGround(R.color.watm_background_gray);
        setTitleTextColor(R.color.theme_blue_two);
        setTitleLeftImage(R.mipmap.mrkj_back_blue);
        setResult(RESULT_OK);
    }


    @Override
    protected void getLayoutToView() {
        setContentView(R.layout.activity_compile_details);
    }


    @Override
    protected void initViews() {  //初始化控件
        head_image = (CircleImageView) findViewById(R.id.head_pic);
        change_image = (TextView) findViewById(R.id.change_image);
        change_nick = (EditText) findViewById(R.id.change_nick);
        change_gender = (RadioGroup) findViewById(R.id.change_gender);
        change_birthDay = (TextView) findViewById(R.id.change_date);
        change_OK_With_Save = (Button) findViewById(R.id.change_ok);
        change_height = (EditText) findViewById(R.id.change_height);
        change_weight = (EditText) findViewById(R.id.change_weight);
        change_length = (EditText) findViewById(R.id.change_length);
    }


    @Override
    protected void initValues() {  //从数据库里面获取相关数据
        path = SaveKeyValues.getStringValues("path","path");
        nick_str = SaveKeyValues.getStringValues("nick","未填写");
        sex_str = SaveKeyValues.getStringValues("gender","男");
        getTodayDate();  //获取今日日期
        birth_year = SaveKeyValues.getIntValues("birth_year",now_year);
        birth_month = SaveKeyValues.getIntValues("birth_month",now_month);
        birth_day = SaveKeyValues.getIntValues("birth_day",now_day);
        date = birth_year+"-"+birth_month+"-"+birth_day;
        height = SaveKeyValues.getIntValues("height",0);
        weight = SaveKeyValues.getIntValues("weight",0);
        length = SaveKeyValues.getIntValues("length",0);

    }


    public void getTodayDate(){  //获取今日日期
        Map<String,Object> map = DateUtils.getDate();
        now_year = (int) map.get("year");
        now_month = (int) map.get("month");
        now_day = (int) map.get("day");
    }



    @Override
    protected void setViewsListener() {  //设置监听
        head_image.setOnClickListener(this);
        change_OK_With_Save.setOnClickListener(this);
        change_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.change_boy:
                        sex_str = "男";
                        break;
                    case R.id.change_girl:
                        sex_str = "女";
                        break;
                    default:
                        break;
                }
            }
        });
        change_birthDay.setOnClickListener(this);
    }


    @Override
    protected void setViewsFunction() {  //设置功能
        if(!"path".equals(path)){  //当path不为空时
            head_image.setImageBitmap(BitmapFactory.decodeFile(path));
        }
        change_nick.setHint(nick_str);  //设置背面的提示
        change_nick.setHintTextColor(getResources().getColor(R.color.watm_background_gray));  //设置提示的字的颜色
        change_height.setHint(height);
        change_height.setHintTextColor(getResources().getColor(R.color.watm_background_gray));
        change_weight.setHint(weight);
        change_weight.setHintTextColor(getResources().getColor(R.color.watm_background_gray));
        change_length.setHint(length);
        change_length.setHintTextColor(getResources().getColor(R.color.watm_background_gray));
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.head_pic:  //更改头像
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择图片");
                builder.setMessage("可以通过相册和拍照来修改默认图片！");
                builder.setPositiveButton("图库", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);  //获得图片的路径
                        gallery();  //打开相册
                    }
                });

                builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                        camera();  //打开相机
                    }
                });
                builder.show();
                break;
            case R.id.change_date:  //更改生日
                break;
            case R.id.change_ok:  //保存退出
                if(tempFile != null){
                    SaveKeyValues.putStringValues("path",tempFile.getPath());
                }
                if(!"".equals(change_nick.getText())){
                    SaveKeyValues.putStringValues("nick",change_nick.getText().toString());
                }
                SaveKeyValues.putStringValues("gender",sex_str);
                SaveKeyValues.putStringValues("birthday",birth_year + "年" + birth_month + "月" + birth_day + "日");
                SaveKeyValues.putIntValues("birth_year",birth_year);
                SaveKeyValues.putIntValues("birth_month",birth_month);
                SaveKeyValues.putIntValues("birth_day",birth_day);
                SaveKeyValues.putIntValues("age", now_year - birth_year);  //保存年龄年龄
                if (!"".equals(change_height.getText().toString())){
                    SaveKeyValues.putIntValues("height", Integer.parseInt(change_height.getText().toString().trim()));//保存身高
                }
                if (!"".equals(change_length.getText().toString())){
                    SaveKeyValues.putIntValues("length", Integer.parseInt(change_length.getText().toString().trim()));//保存步长
                }
                if (!"".equals(change_weight.getText().toString())){
                    SaveKeyValues.putIntValues("weight", Integer.parseInt(change_weight.getText().toString().trim()));//保存体重
                }
                finish();  //活动结束
                break;
            default:
                break;
        }
    }


    public void gallery(){

    }


    public void camera(){

    }
}
