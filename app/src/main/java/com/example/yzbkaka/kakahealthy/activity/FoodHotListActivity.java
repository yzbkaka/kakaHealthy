package com.example.yzbkaka.kakahealthy.activity;

import android.app.ExpandableListActivity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.base.BaseActivity;
import com.example.yzbkaka.kakahealthy.entity.FoodMessage;
import com.example.yzbkaka.kakahealthy.entity.FoodType;

import java.util.ArrayList;
import java.util.List;

public class FoodHotListActivity extends BaseActivity {
    private int sign = -1;  //
    private String[] foodTypeArray;  //食物类型的数组
    private List<FoodType> foodList;  //食物数据类型的集合
    private ExpandableListView dataList;
    private Bitmap[] bitmaps;  //存放图片资源
    private int[] ids;  //存放图片资源的id数组

    @Override
    protected void setActivityTitle() {  //设置标题栏
        initTitle();  //初始化标题
        setTitle("食物热量对照表",this);  //设置标题
        setMyBackGround(R.color.watm_background_gray);  //设置背景
        setTitleTextColor(R.color.theme_blue_two);  //设置文字颜色
        setTitleLeftImage(R.mipmap.mrkj_back_blue);  //设置左边图片
    }

    @Override
    protected void getLayoutToView() {  //设置界面布局
        setContentView(R.layout.activity_food_hot_list);  //将布局加载进去
    }

    @Override
    protected void initValues() {  //初始化食物数据
        ids = new int[]{
                R.mipmap.gu,  //谷
                R.mipmap.mrkj_cai,  //菜
                R.mipmap.mrkj_guo,  //果
                R.mipmap.mrkj_rou,  //肉
                R.mipmap.mrkj_dan,  //蛋
                R.mipmap.mrkj_yv,  //鱼
                R.mipmap.mrkj_nai,  //奶
                R.mipmap.mrkj_he,  //喝
                R.mipmap.mrkj_jun,  //菌
                R.mipmap.you  //油
        };
        bitmaps = new Bitmap[ids.length];
        for(int i = 0;i < ids.length;i++){
            bitmaps[i] = BitmapFactory.decodeResource(getResources(),ids[i]);
        }
        foodTypeArray = new String[]{  //显示名称的数组
                "五谷类",
                "蔬菜类",
                "水果类",
                "肉类",
                "蛋类",
                "水产类",
                "奶类",
                "饮料类",
                "菌藻类",
                "油脂类"
        };
        foodList = new ArrayList<>();  //创建食物集合
        DBHelper dbHelper = new DBHelper();  //创建数据库
        Cursor cursor = dbHelper.selectAllDataOfTable("hot");  //查询数据库中的数据
        for(int i = 0;i < 10;i++){
            FoodType foodType = null;
            List<FoodMessage> foods = null;
            int counts = 1;
            while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String hot = cursor.getString(cursor.getColumnIndex("hot"));
                String typeName = cursor.getString(cursor.getColumnIndex("type_name"));
                if(counts == 1){
                    foodType = new FoodType();  //实例化对象
                    foods = new ArrayList<>();
                    foodType.setFoodType(typeName);
                }
                FoodMessage foodMessage = new FoodMessage();
                foodMessage.setFoodName(name);
                foodMessage.setHot(hot);
                foodMessage.add(foodMessage);
                foodType.setFoodList(foods);
                if(counts == 20){
                    foodList.add(foodType);
                    break;;
                }
                counts++;
            }
        }
        cursor.close();
    }

    @Override
    protected void initViews() {  //实例化控件
        dataList = (ExpandableListView)findViewById(R.id.food_list);
    }

    @Override
    protected void setViewsListener() {  //设置点击事件，展开一个，其他的都收起
        dataList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View v, int groupPosition, long id) {
                if(sign == -1){
                    dataList.expandGroup(groupPosition);  //展开被选的group
                    dataList.setSelectedGroup(groupPosition);  //设置选中的group放置到顶端
                    sign = groupPosition;
                }
                else if(sign == groupPosition){
                    dataList.collapseGroup(sign);
                    sign = -1;
                }
                else{
                    dataList.collapseGroup(sign);
                    dataList.expandGroup(groupPosition);
                    dataList.setSelectedGroup(groupPosition);
                    sign = groupPosition;
                }
                return true;
            }
        });
    }

    @Override
    protected void setViewsFunction() {  //创建适配器并绑定
        MyFoodAdapter adapter = new MyFoodAdapter();  //创建适配器
        dataList.setAdapter(adapter);  //绑定适配器
    }


    class MyFoodAdapter extends BaseExpandableListAdapter { //适配器类（内部类）
        @Override
        public int getGroupCount() {  //Group的数量（一级布局的数量）
            return foodList.size();
        }


        @Override
        public int getChildrenCount(int groupPosition) {  //每个Group中的Child的数量
            return foodList.get(groupPosition).getFood_list().size();
        }


        @Override
        public Object getGroup(int groupPosition) {  //获取对应位置的Group
            return foodList.get(groupPosition);
        }


        @Override
        public Object getChild(int groupPosition, int childPosition) {  //获取对应位置中的Child
            return foodList.get(groupPosition).getFood_list().get(childPosition);
        }


        @Override
        public long getGroupId(int groupPosition) {  //获取对应位置的Group的ID
            return groupPosition;
        }


        @Override
        public long getChildId(int groupPosition, int childPosition) {  //获取对应位置的Child的ID
            return childPosition;
        }


        @Override
        public boolean hasStableIds() {  //判断同一个ID是否指向同一个对象
            return true;
        }


        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {  //获取Group的视图
            GroupViewHolder holder;
            if (convertView == null){  //如果之前没有创建过该视图
                holder = new GroupViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.group_item ,null);  //将布局加载进去
                holder.image = (ImageView) convertView.findViewById(R.id.group_image);  //初始化控件
                holder.title = (TextView) convertView.findViewById(R.id.group_title);
                convertView.setTag(holder);
            }else {
                holder = (GroupViewHolder) convertView.getTag();
            }
            holder.image.setImageBitmap(bitmaps[groupPosition]);  //设置显示的图片
            holder.title.setText(foodTypeArray[groupPosition]);  //设置显示的汉字
            return convertView;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {  //获取child的视图ChildViewHolder holder;
            ChildViewHolder holder;
            if (convertView == null){
                holder = new ChildViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.child_item,null);  //加载布局
                holder.name = (TextView) convertView.findViewById(R.id.food_name);  //初始化控件
                holder.hot = (TextView) convertView.findViewById(R.id.food_hot);
                convertView.setTag(holder);
            }else {
                holder = (ChildViewHolder) convertView.getTag();
            }
            FoodMessage food = foodList.get(groupPosition).getFood_list().get(childPosition);  //获得食物的信息
            holder.name.setText(food.getFood_name());//设置食物名称
            holder.hot.setText(food.getHot()+"千卡/克");      //设置食物热量
            return convertView;
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {  //判断child是否可以被选择
            return true;
        }
    }

    class GroupViewHolder{
        ImageView image;
        TextView title;
    }
    class ChildViewHolder{
        TextView name,hot;
    }
}
