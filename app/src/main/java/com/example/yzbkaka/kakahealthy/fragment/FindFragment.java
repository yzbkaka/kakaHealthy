package com.example.yzbkaka.kakahealthy.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.application.DemoApplication;
import com.example.yzbkaka.kakahealthy.db.DatasDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FindFragment extends Fragment {
    private View view;  //界面的布局
    private Context context;
    public static Bitmap[] bitmaps = DemoApplication.bitmaps;
    public static String[] shuoming = DemoApplication.shuoming;
    public static final String cishu = "1组6次";
    private ListView listView;
    private List<Map<String,Object>> list;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_find,null);
        listView = (ListView)view.findViewById(R.id.find_list);
        if(isAdded()){
            list = new ArrayList<>();
            for(int i = 0;i < 5;i++){
                Map<String,Object> map =new HashMap<>();
                map.put("tu",bitmaps[i]);
                map.put("xm",shuoming[i]);
                map.put("cs",cishu);
                map.put("tj","添加新计划");
                list.add(map);
            }
            /*MyAdapter adapter = new MyAdapter(context,list,this);
            listView.setAdapter(adapter);
            DatasDao datasDao = new DatasDao(getContext());
            Cursor cursor = datasDao.selectAll("plans");*/
        }
    }

}
