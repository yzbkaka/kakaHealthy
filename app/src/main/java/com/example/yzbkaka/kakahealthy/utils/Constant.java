package com.example.yzbkaka.kakahealthy.utils;

/**
 * Created by yzbkaka on 19-10-2.
 */

public class Constant {  //常量值
    //加载布局的变量
    public static final int TURN_MAIN = 1;//加载运动界面
    public static final int MAKE_PLAN = 2;//加载发现界面
    //每个Fragment的Tag值
    public static final String SPORT_TAG = "sport";//运动TAG
    public static final String FIND_TAG = "find";//发现TAG
    public static final String HEART_TAG = "heart";//心率TAG
    public static final String MINE_TAG = "mine";//我的TAG
    //天气预报接口
    //APP_KEY
    public static final String APP_KEY = "06ba330de85cf5484fedbcd1c2247e28";

    //文件夹名
    public static final String file_name = "keep_fit";
    public static final String head_image = "head.jpg";


    //时间常量
    public static final long DAY_FOR_24_HOURS = 60 * 60 * 24 * 1000;
    public static final long DAY_FOR_23_HOUR = 23;
    public static final long DAY_FOR_59_MINURE = 59;


    //执行运动计划的服务会使用到的集中类型值
    public static final int START_PLAN = 1;//开始计划
    public static final int CHANGE_PLAN = 2;//更改计划
    public static final int NEXT_PLAN = 3;//下一个计划
    public static final int ONE_PLAN = 4;//循环一个
    public static final int STOP_PLAN = 5;//结束计划

}
