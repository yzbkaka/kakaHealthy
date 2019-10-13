package com.example.yzbkaka.kakahealthy.application;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.entity.SaveKeyValues;
import com.example.yzbkaka.kakahealthy.utils.BringData;

import java.io.IOException;

/**
 * Created by yzbkaka on 19-10-12.
 */

public class DemoApplication extends Application {
    public static Bitmap[] bitmaps = new Bitmap[5];  //存储具体的热身运动
    public static String[] shuoming = new String[5];  //对热身运动的名称


    @Override
    public void onCreate() {
        super.onCreate();
        //new GetLocation(getApplicationContext());  //定位
        SaveKeyValues.createSharePreferences(this);  //创建数据库
        bitmaps[0] = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.mrkj_fushen1);  //初始化热身运动的图片
        bitmaps[1] = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.mrkj_fuwocheng1);
        bitmaps[2] = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.mrkj_gunlun1);
        bitmaps[3] = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.mrkj_wotui1);
        bitmaps[4] = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.mrkj_sanwanju1);

        shuoming[0] = "俯身哑铃飞鸟";  //初始化热身运动描述
        shuoming[1] = "俯卧撑";
        shuoming[2] = "滚轮支点俯卧撑";
        shuoming[3] = "平板卧推";
        shuoming[4] = "仰卧平板杠铃肱三弯举";
        int saveDateIndex = SaveKeyValues.getIntValues("date_index", 0);
        //Log.e("数据库数否被存入", "【" + saveDateIndex + "】");
        if (saveDateIndex == 0) {
            try {
                SaveKeyValues.putIntValues("date_index", 1);
                BringData.getDataFromAssets(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
