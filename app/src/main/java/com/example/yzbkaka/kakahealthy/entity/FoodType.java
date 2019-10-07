package com.example.yzbkaka.kakahealthy.entity;

import java.util.List;

/**
 * Created by yzbkaka on 19-10-7.
 */

public class FoodType {  //食物类型
    private String foodType;
    private List<FoodMessage> foodList;  //该类型食物的的列表

    public String getFood_type() {
        return foodType;
    }

    public void setFood_type(String foodType) {
        this.foodType = foodType;
    }

    public List<FoodMessage> getFood_list() {  //返回给类型食物的列表
        return foodList;
    }

    public void setFood_list(List<FoodMessage> foodList) {
        this.foodList = foodList;
    }
}
