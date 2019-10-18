package com.example.yzbkaka.kakahealthy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yzbkaka on 19-10-14.
 */

public class DatasDB extends SQLiteOpenHelper {
    private static String DB_NAME = "healthy_db";  //数据库名称
    private static int DB_VERSION = 1;  //数据库版本
    //建立步数的表
    private String stepRecorded = "create table step (_id integer primary key autoincrement ," +
            " date varchar(20),year integer,month integer,day integer,steps integer,hot varchar(20),length varchar(20))";
    //建立计划的表
    private String sportTable = "create table plans (_id integer primary key autoincrement ,"+
            "sport_type integer,sport_name varchar(20),start_year integer,start_month integer,start_day integer,"
            +"stop_year integer,stop_month integer,stop_day integer,set_time integer,hint_time integer,hint_hour integer, hint_minute integer,hint_str varchar(20),add_24_hour integer,"
            +"number_values"+")";


    public DatasDB(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(stepRecorded);  //执行语句
        db.execSQL(sportTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
