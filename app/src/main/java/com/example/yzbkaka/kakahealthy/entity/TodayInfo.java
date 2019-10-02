package com.example.yzbkaka.kakahealthy.entity;

/**
 * Created by yzbkaka on 19-10-2.
 */

public class TodayInfo {
    private String windspeed;  //风速
    private String direct;  //风向
    private String power;  //几级风向
    private String humidity;  //湿度
    private String info;  //天气
    private String temperature;  //温度
    private String date;  //日期
    private String cityName;  //城市
    private String week;  //星期
    private String moon;  //农历

    public String getWindspeed() {
        return windspeed;
    }
    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }
    public String getDirect() {
        return direct;
    }
    public void setDirect(String direct) {
        this.direct = direct;
    }
    public String getPower() {
        return power;
    }
    public void setPower(String power) {
        this.power = power;
    }
    public String getHumidity() {
        return humidity;
    }
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String city_name) {
        this.cityName = city_name;
    }
    public String getWeek() {
        return week;
    }
    public void setWeek(String week) {
        this.week = week;
    }
    public String getMoon() {
        return moon;
    }
    public void setMoon(String moon) {
        this.moon = moon;
    }
}
