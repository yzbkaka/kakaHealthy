package com.example.yzbkaka.kakahealthy.entity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by yzbkaka on 19-10-2.
 */

public class StepDetector implements SensorEventListener {  //记步算法（开源代码），是一个传感器

    public static int CURRENT_SETP = 0;
    public int walk = 0;
    public static float SENSITIVITY = 8; // SENSITIVITY灵敏度

    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    private float mYOffset;//位移
    private static long mEnd = 0;//运动相隔时间
    private static long mStart = 0;//运动开始时间
    private Context context;

    /**
     * 最后加速度方向
     */
    private float mLastDirections[] = new float[3 * 2];//最后的方向
    private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;

    /**
     * 传入上下文的构造函数
     *
     *
     */
    public StepDetector(Context context) {
        super();
        this.context = context;
        // 用于判断是否计步的值
        int h = 480;
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));//重力加速度
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));//地球最大磁场
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            } else {
                // 判断传感器的类型是否为重力传感器(加速度传感器)
                int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                if (j == 1) {
                    float vSum = 0;
                    // 获取x轴、y轴、z轴的加速度
                    for (int i = 0; i < 3; i++) {
                        final float v = mYOffset + event.values[i] * mScale[j];
                        vSum += v;
                    }
                    int k = 0;
                    float v = vSum / 3;//获取三轴加速度的平均值
                    // 判断人是否处于行走中，主要从以下几个方面判断：
                    // 人如果走起来了，一般会连续多走几步。因此，如果没有连续4-5个波动，那么就极大可能是干扰。
                    // 人走动的波动，比坐车产生的波动要大，因此可以看波峰波谷的高度，只检测高于某个高度的波峰波谷。
                    // 人的反射神经决定了人快速动的极限，怎么都不可能两步之间小于0.2秒，因此间隔小于0.2秒的波峰波谷直接跳过通过重力加速计感应，
                    // 重力变化的方向，大小。与正常走路或跑步时的重力变化比对，达到一定相似度时认为是在走路或跑步。实现起来很简单，只要手机有重力感应器就能实现。
                    // 软件记步数的精准度跟用户的补偿以及体重有关，也跟用户设置的传感器的灵敏度有关系，在设置页面可以对相应的参数进行调节。一旦调节结束，可以重新开始。
                    float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                    if (direction == -mLastDirections[k]) {
                        int extType = (direction > 0 ? 0 : 1);
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                        if (diff > SENSITIVITY) {
                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                mEnd = System.currentTimeMillis();
                                // 通过判断两次运动间隔判断是否走了一步
                                if (mEnd - mStart > 500) {
                                    CURRENT_SETP++;
                                    mLastMatch = extType;
                                    mStart = mEnd;
                                }
                            } else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
