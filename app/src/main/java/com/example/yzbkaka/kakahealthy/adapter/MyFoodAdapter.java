package com.example.yzbkaka.kakahealthy.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzbkaka.kakahealthy.R;

/**
 * Created by yzbkaka on 19-10-7.
 */

class MyFoodAdapter extends BaseExpandableListAdapter{
    @Override
    public int getGroupCount() {  //Group的数量（一级布局的数量）
        return food_list.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {  //每个Group中的Child的数量
        return food_list.get(groupPosition).getFood_list().size();
    }


    @Override
    public Object getGroup(int groupPosition) {  //获取对应位置的Group
        return food_list.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {  //获取对应位置中的Child
        return food_list.get(groupPosition).getFood_list().get(childPosition);
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
        if (convertView == null){
            holder = new GroupViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.group_item , null);
            holder.image = (ImageView) convertView.findViewById(R.id.group_image);
            holder.title = (TextView) convertView.findViewById(R.id.group_title);
            convertView.setTag(holder);
        }else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.image.setImageBitmap(bitmaps[groupPosition]);//设置显示的图片
        holder.title.setText(food_type_array[groupPosition]);//设置显示的汉字
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {  //获取child的视图
        ChildViewHolder holder;
        if (convertView == null){
            holder = new ChildViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.child_item,null);
            holder.name = (TextView) convertView.findViewById(R.id.food_name);
            holder.hot = (TextView) convertView.findViewById(R.id.food_hot);
            convertView.setTag(holder);
        }else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        FoodMessage food = food_list.get(groupPosition).getFood_list().get(childPosition);
        holder.name.setText(food.getFood_name());//设置食物名称
        holder.hot.setText(food.getHot()+"千卡/克");      //设置食物热量
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {  //判断child是否可以被选择
        return true;
    }
}

public class GroupViewHolder{
    ImageView image;
    TextView title;
}
class ChildViewHolder{
    TextView name,hot;
}

