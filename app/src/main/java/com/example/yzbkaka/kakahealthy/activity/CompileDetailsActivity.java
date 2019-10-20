package com.example.yzbkaka.kakahealthy.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.base.BaseActivity;
import com.example.yzbkaka.kakahealthy.entity.SaveKeyValues;
import com.example.yzbkaka.kakahealthy.utils.GetPictureFromLocation;

import java.io.File;
import java.util.Calendar;

import mrkj.library.wheelview.circleimageview.CircleImageView;

public class CompileDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final int PHOTO_REQUEST_CAMERA = 1;  // 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;  // 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;  // 结果
    private static final int PHOTO_REQUEST_GALLERY2 = 4;  // 从相册中选择
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";  // 图片名称
    //头像
    private CircleImageView headImage;  //显示头像
    private TextView changeImage;  //更换头像
    private String path;  //头像的路径
    private File tempFile;  //临时文件
    //昵称
    private String nickStr;  //用户昵称
    private EditText changeNick;  //修改昵称
    //性别
    private RadioGroup changeGender;  //更改性别
    private String sex;  //性别
    //生日
    private TextView changeBirthDay;  //更改生日
    private String date;
    private int birth_year;
    private int birth_month;
    private int birth_day;
    //当日日期
    private int now_year;  //年
    private int now_month;  //月
    private int now_day;  //天
    //身高
    private EditText changeHeight;  //更改身高
    private int height;
    //体重
    private EditText changeWeight;  //更改体重
    private int weight;
    //步长
    private EditText changeLength;  //更改不长
    private int length;

    private Button changeOKWithSave;  //确定保存


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
        headImage = (CircleImageView) findViewById(R.id.head_pic);
        changeImage = (TextView) findViewById(R.id.change_image);
        changeNick = (EditText) findViewById(R.id.change_nick);
        changeGender = (RadioGroup) findViewById(R.id.change_gender);
        changeBirthDay = (TextView) findViewById(R.id.change_date);
        changeOKWithSave = (Button) findViewById(R.id.change_ok);
        changeHeight = (EditText) findViewById(R.id.change_height);
        changeWeight = (EditText) findViewById(R.id.change_weight);
        changeLength = (EditText) findViewById(R.id.change_length);
    }


    @Override
    protected void initValues() {  //从数据库里面获取相关数据
        path = SaveKeyValues.getStringValues("path","path");
        nickStr = SaveKeyValues.getStringValues("nick","未填写");
        sex = SaveKeyValues.getStringValues("gender","男");
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
        Calendar calendar = Calendar.getInstance();
        now_year = calendar.get(Calendar.YEAR);
        now_month = calendar.get(Calendar.MONTH + 1);
        now_day = calendar.get(Calendar.DATE);

       /* Map<String,Object> map = DateUtils.getDate();
        now_year = (int) map.get("year");
        now_month = (int) map.get("month");
        now_day = (int) map.get("day");*/
    }



    @Override
    protected void setViewsListener() {  //设置监听
        changeImage.setOnClickListener(this);
        changeOKWithSave.setOnClickListener(this);
        changeGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.change_boy:
                        sex = "男";
                        break;
                    case R.id.change_girl:
                        sex = "女";
                        break;
                    default:
                        break;
                }
            }
        });
        changeBirthDay.setOnClickListener(this);
    }


    @Override
    protected void setViewsFunction() {  //设置功能
        if(!"path".equals(path)){  //当path不为空时
            headImage.setImageBitmap(BitmapFactory.decodeFile(path));
        }
        changeNick.setHint(nickStr);  //设置背面的提示
        changeNick.setHintTextColor(getResources().getColor(R.color.watm_background_gray));  //设置提示的字的颜色
        changeHeight.setHint(height);
        changeHeight.setHintTextColor(getResources().getColor(R.color.watm_background_gray));
        changeWeight.setHint(weight);
        changeWeight.setHintTextColor(getResources().getColor(R.color.watm_background_gray));
        changeLength.setHint(length);
        changeLength.setHintTextColor(getResources().getColor(R.color.watm_background_gray));
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.change_image:  //更改头像
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择图片");
                builder.setMessage("可以通过相册和拍照来修改默认图片！");
                builder.setPositiveButton("图库", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);  //获得临时文件
                        gallery();  //打开相册                                                             //获得新的文件路径和名字
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        birth_year = year;
                        birth_month = monthOfYear;
                        birth_day = dayOfMonth;
                        date = birth_year+"-"+birth_month+"-"+birth_day;
                        changeBirthDay.setText(date);
                    }
                },birth_year,birth_month - 1,birth_day);
                datePickerDialog.setTitle("请选择生日日期");
                datePickerDialog.show();
                break;

            case R.id.change_ok:  //保存退出
                if(tempFile != null){
                    SaveKeyValues.putStringValues("path",tempFile.getPath());
                }
                if(!"".equals(changeNick.getText())){
                    SaveKeyValues.putStringValues("nick", changeNick.getText().toString());
                }
                SaveKeyValues.putStringValues("gender", sex);
                SaveKeyValues.putStringValues("birthday",birth_year + "年" + birth_month + "月" + birth_day + "日");
                SaveKeyValues.putIntValues("birth_year",birth_year);
                SaveKeyValues.putIntValues("birth_month",birth_month);
                SaveKeyValues.putIntValues("birth_day",birth_day);
                SaveKeyValues.putIntValues("age", now_year - birth_year);  //保存年龄年龄
                if (!"".equals(changeHeight.getText().toString())){
                    SaveKeyValues.putIntValues("height", Integer.parseInt(changeHeight.getText().toString().trim()));//保存身高
                }
                if (!"".equals(changeLength.getText().toString())){
                    SaveKeyValues.putIntValues("length", Integer.parseInt(changeLength.getText().toString().trim()));//保存步长
                }
                if (!"".equals(changeWeight.getText().toString())){
                    SaveKeyValues.putIntValues("weight", Integer.parseInt(changeWeight.getText().toString().trim()));//保存体重
                }
                finish();  //活动结束
                break;

            default:
                break;
        }
    }


    public void gallery(){  //打开相册
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);  //ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");  //设置获得图片的功能
        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){  //判断版本
            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);  //打开相册的操作（请求码）
        }else{
            startActivityForResult(intent, PHOTO_REQUEST_GALLERY2);
        }
    }


    public void camera(){  //打开手机相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");  //添加打开相机的操作
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));  //键值和数据
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);  //打开相机操作
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){  //获得相册和照相返回过来的数据
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == PHOTO_REQUEST_GALLERY || requestCode == PHOTO_REQUEST_GALLERY2){  //如果是相册返回过来的数据
            if(date != null){
                String path = GetPictureFromLocation.selectImage(getApplicationContext(),data);  //获得从相册选择图片的路径
                crop(Uri.parse("file://"+path));  //对图片进行裁剪
            }
        }
        if(requestCode == PHOTO_REQUEST_CAMERA){  //如果是相机返回过来的数据
            crop(Uri.fromFile(tempFile));
        }
        if(requestCode == PHOTO_REQUEST_CUT){  //如果返回过来的是裁剪的数据
            try{
                Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getPath());
                headImage.setImageBitmap(bitmap);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    public void crop(Uri uri){  //对图片进行裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");  //放入裁剪的动作
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);//黑边
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        // 图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);  //启动裁剪
    }
}
