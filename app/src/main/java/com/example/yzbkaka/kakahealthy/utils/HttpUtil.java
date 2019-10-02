package com.example.yzbkaka.kakahealthy.utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yzbkaka on 19-10-2.
 */

public class HttpUtil {
    private static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;
    private static final String DEF_CHATSET = "UTF-8";
    public static final String HUMIDITY = "%rh";// 湿度单位
    public static final String TEMPERATURE = "°C";// 温度单位

    public static String getJSONStr(String dataUrl) {  //使用HttpUrl获取Json数据
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            URL url = new URL(dataUrl);
            connection = (HttpURLConnection) url.openConnection();// 引用这个url
            connection.setRequestProperty("User-agent", userAgent);
            connection.setUseCaches(false);
            connection.setConnectTimeout(DEF_CONN_TIMEOUT);
            connection.setReadTimeout(DEF_READ_TIMEOUT);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.connect();// 打开连接
            if (connection.getResponseCode() == 200) {// code为200位连接服务器成功
                InputStream is = connection.getInputStream();// 获取输入流
                reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sb.append(strRead);
                }
                rs = sb.toString();
                return rs;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();// 关闭流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();// 断开连接
            }
        }
        return null;
    }

    public static TodayInfo parseNowJSON(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            String reason = jsonObject.getString("reason");
            if (TextUtils.equals("successed!", reason)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject data = result.getJSONObject("data");
                JSONObject realtime = data.getJSONObject("realtime");
                JSONObject wind = realtime.getJSONObject("wind");
                // 解析出风的相关信息
                String windspeed = wind.getString("windspeed");// 风速
                String direct = wind.getString("direct");
                String power = wind.getString("power");
                // 解析出天气的相关信息
                JSONObject weather = realtime.getJSONObject("weather");
                String humidity = weather.getString("humidity");
                String info = weather.getString("info");
                String temperature = weather.getString("temperature");
                // 日期、城市、农历等信息
                String date = realtime.getString("date");
                String city_name = realtime.getString("city_name");
                String week = realtime.getString("week");
                String moon = realtime.getString("moon");
                // 构件对象
                TodayInfo todayInfo = new TodayInfo();
                todayInfo.setWindspeed(windspeed);
                todayInfo.setCity_name(city_name);
                todayInfo.setDate(date);
                todayInfo.setDirect(direct);
                todayInfo.setHumidity(humidity);
                todayInfo.setInfo(info);
                todayInfo.setMoon(moon);
                todayInfo.setPower(power);
                todayInfo.setTemperature(temperature);
                todayInfo.setWeek(week);

                return todayInfo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static PMInfo parsePMInfoJSON(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            String reason = jsonObject.getString("reason");
            if (TextUtils.equals("successed!", reason)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject data = result.getJSONObject("data");
                JSONObject pm25 = data.getJSONObject("pm25");
                // 构建对象
                PMInfo pmInfo = new PMInfo();
                pmInfo.setDateTime(pm25.getString("dateTime"));
                pmInfo.setCityName(pm25.getString("cityName"));

                JSONObject pm25s = pm25.getJSONObject("pm25");
                pmInfo.setCurPm(pm25s.getString("curPm"));
                pmInfo.setPm25(pm25s.getString("pm25"));
                pmInfo.setPm10(pm25s.getString("pm10"));
                pmInfo.setLevel(pm25s.getString("level"));
                pmInfo.setQuality(pm25s.getString("quality"));
                pmInfo.setDes(pm25s.getString("des"));

                return pmInfo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
