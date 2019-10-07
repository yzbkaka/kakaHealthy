package com.example.yzbkaka.kakahealthy.entity;

/**
 * Created by yzbkaka on 19-10-7.
 */

public class FoodMessage {  //食物的信息类
    private String foodName;  //食物的名字
    private String hot;  //食物的热量

    public String getFood_name() {
        return foodName;
    }

    public void setFood_name(String foodName) {
        this.foodName = foodName;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }
}
