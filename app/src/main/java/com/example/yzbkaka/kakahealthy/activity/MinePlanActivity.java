package com.example.yzbkaka.kakahealthy.activity;

import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.application.DemoApplication;
import com.example.yzbkaka.kakahealthy.base.BaseActivity;
import com.example.yzbkaka.kakahealthy.db.DatasDao;
import com.example.yzbkaka.kakahealthy.fragment.FindFragment;

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

        while (cursor.moveToNext()) {  //开始获取表中的数据
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
            }
            else {
                map.put("date",startYear + "-" + startMonth + "-" + startDay + "~" + stopYear + "-" + stopMonth + "-" + stopDay);
            }
            map.put("id",id);
            map.put("type",type);
            map.put("time",hintStr);
            plan_List.add(map);  //向集合中添加数据
        }
        cursor.close();
    }


    @Override
    protected void setViewsListener() {}


    @Override
    protected void setViewsFunction() {
        if(plan_List.size() > 0){
            myAdapter = new MyAdapter();
            listView.setAdapter(myAdapter);  //设置适配器
        }

        TextView textView = new TextView(this);  //动态添加组件
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


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 3000 && resultCode == RESULT_OK){  //返回数据后刷新列表
            plan_List.clear();
            List<Map<String,Object>> update = new ArrayList<>();
            cursor = datasDao.selectAll("plan");
            while(!cursor.moveToNext()){  //重新开始读取
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
                }
                else {
                    map.put("date",startYear + "-" + startMonth + "-" + startDay + "~" + stopYear + "-" + stopMonth + "-" + stopDay);
                }
                map.put("id",id);
                map.put("type",type);
                map.put("time",hintStr);
                update.add(map);
            }
            plan_List.addAll(update);
            myAdapter.notifyDataSetChanged();  //更新适配器
            cursor.close();
        }
    }


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {  //返回数据的数量
            return plan_List.size();
        }


        @Override
        public Object getItem(int i) {  //得到子项
            return plan_List.get(i);
        }


        @Override
        public long getItemId(int i) {  //返回序号
            return i;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.plan_item,null);
                holder = new ViewHolder();
                holder.date = (TextView)findViewById(R.id.date);
                holder.name = (TextView)findViewById(R.id.project_name);
                holder.hini_time = (TextView)findViewById(R.id.hint_time);
                holder.count = (TextView)findViewById(R.id.cishu1);
                holder.update = (TextView)findViewById(R.id.update_plan);
                holder.delete = (TextView)findViewById(R.id.delete_plan);
                holder.imageView = (ImageView)findViewById(R.id.image_show);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }
            Map<String,Object> map = plan_List.get(position);
            holder.date.setText(map.get("date").toString());
            holder.hini_time.setText(map.get("time").toString());
            holder.name.setText(DemoApplication.shuoming[(int)map.get("type")]);
            holder.count.setText(FindFragment.cishu);
            holder.update.setText("更新计划");
            holder.delete.setText("删除计划");
            holder.imageView.setImageBitmap(DemoApplication.bitmaps[(int)map.get("type")]);

            final int id = (int) map.get("id");
            holder.update.setTag(id);
            holder.delete.setTag(id);

            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(id == (int)holder.update.getTag()){
                        startActivityForResult(new Intent(MinePlanActivity.this,UpdateActivity.class).putExtra("position",position).putExtra("id",id),3000);
                        Toast.makeText(MinePlanActivity.this, "添加计划", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            return null;
        }
    }


    class ViewHolder{
        private TextView date;  //日期
        private TextView name;  //名称
        private TextView count;  //次数
        private TextView update;  //更新计划
        private TextView delete;  //删除
        private TextView hini_time;  //提示时间
        private ImageView imageView;  //动作图片
    }
}
