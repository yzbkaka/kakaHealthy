package com.example.yzbkaka.kakahealthy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yzbkaka.kakahealthy.R;
import com.example.yzbkaka.kakahealthy.db.DatasDao;

public class UpdateActivity extends AppCompatActivity  {
    private Button change_stop_date;
    private Button dimind_change_date;
    private TextView back_to_front;
    private TimePicker change_time;
    private int hour,minute;
    private int change_year,change_month,change_day;
    private int index;
    private int id;
    private DatasDao datasDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

    }
}
