package com.example.yzbkaka.kakahealthy.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.base.BaseActivity;
import com.example.yzbkaka.kakahealthy.db.DatasDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinePlanActivity extends BaseActivity {
    private List<Map<String,Object>> plan_List = new ArrayList<>();  //储存计划数据
    private ListView listView;  //列表
    private DatasDao datasDao;  //数据库操作诶
    private Cursor cursor;  //游标
    private MyAdapter myAdapter;  //适配器


    @Override
    protected void setActivityTitle() {
        initTitle();
        setTitle("我的计划",this);
        setMyBackGround(R.color.watm_background_gray);
        setTitleTextColor(R.color.theme_blue_two);
        setTitleLeftImage(R.mipmap.mrkj_back_blue);

    }


    @Override
    protected void getLayoutToView() {
        setContentView(R.layout.activity_mine_plan);
    }


    @Override
    protected void initViews() {
        listView = (ListView)findViewById(R.id.plan_list);
    }


    @Override
    protected void initValues() {
        datasDao = new DatasDao(this);
        cursor = datasDao.selectAll("plan");  //获得游标

        while (cursor.moveToNext()) {
            Map<String,Object> map = new HashMap<>();
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int type = cursor.getInt(cursor.getColumnIndex("sport_type"));
            int startYear = cursor.getInt(cursor.getColumnIndex("start_year"));
            int startMonth = cursor.getInt(cursor.getColumnIndex("start_month"));
            int startDay = cursor.getInt(cursor.getColumnIndex("start_day"));
            int stopYear = cursor.getInt(cursor.getColumnIndex("stop_year"));
            int stopMonth = cursor.getInt(cursor.getColumnIndex("stop_month"));
            int stopDay = cursor.getInt(cursor.getColumnIndex("stop_day"));
            String hintStr = cursor.getString(cursor.getColumnIndex("hint_str"));
            if (startYear == stopYear && startMonth == stopMonth & startDay == stopDay){
                map.put("date",startYear + "-" + startMonth + "-" + startDay);
            }else {
                map.put("date",startYear + "-" + startMonth + "-" + startDay + "~" + stopYear + "-" + stopMonth + "-" + stopDay);
            }
            map.put("id",id);
            map.put("type",type);
            map.put("time",hintStr);
            //向集合中添加数据
            plan_List.add(map);
        }
        cursor.close();
    }


    @Override
    protected void setViewsListener() {

    }


    @Override
    protected void setViewsFunction() {
        if(plan_List.size() > 0){
            myAdapter = new MyAdapter();
            listView.setAdapter(myAdapter);
        }

        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setText("没有数据");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(50);
        addContentView(textView,params);
        listView.setEmptyView(textView);
    }


    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }


        @Override
        public Object getItem(int i) {
            return null;
        }


        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }  //适配器
    }


    class ViewHolder{
        
    }
}
