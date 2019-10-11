package com.example.yzbkaka.kakahealthy.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yzbkaka on 19-10-8.
 */

public class DBHelper {  //数据库管理类
    private SQLiteDatabase db;  //数据库连接与操作的对象

    public DBHelper(){
        db = SQLiteDatabase.openOrCreateDatabase(BringData.DATA_PATH + BringData.DATA_NAME,null);  //打开对应的数据库文件
    }


    //查询数据库
    public Cursor select (String table, String[] columns,
                          String selection, String[] selectionArgs, String groupBy,
                          String having, String orderBy){  //表名、列名、约束条件、占位符、指定的group by、对group by结果进行约束、指定查询结果的排序方式
        return  db.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);


    }


    //关闭数据库
    public void dbHelpclose(){
        if (db.isOpen()){
            db.close();
        }
    }


    //按需求查询
    public String getTextResult(int testType,int handType){
        Cursor cursor = this.select("test",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int test_Type = cursor.getInt(cursor.getColumnIndex("test_type"));
            int hand_Type = cursor.getInt(cursor.getColumnIndex("line_type"));
            if (testType == test_Type & handType == hand_Type){
                String text = cursor.getString(cursor.getColumnIndex("text"));
                cursor.close();
                return text;
            }
        }
        return null;
    }


    //返回一个用于查询所有数据的游标
    public Cursor selectAllDataOfTable(String name){
        return this.select(name,null,null,null,null,null,null);
    }
}
