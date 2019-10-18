package com.example.yzbkaka.kakahealthy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yzbkaka on 19-10-14.
 */

public class DatasDao {
    private DatasDB myDB;
    private SQLiteDatabase db;


    public DatasDao(Context context){
        myDB = new DatasDB(context);  //Helper对象
        db = myDB.getWritableDatabase();  //得到数据库对象
    }


    public long insertValue(String tabel, ContentValues values){  //插入数据
        long result = db.insert(tabel,null,values);
        return result;
    }


    public int updateValue(String table,ContentValues values,String whereClause, String[] whereArgs){  //更新数据
        int result = db.update(table,values,whereClause,whereArgs);
        return result;
    }


    public int deleteValue(String table,String whereClause,String[] whereArgs){  //删除数据
        return db.delete(table,whereClause,whereArgs);
    }


    public Cursor selectValue(String sql,String[] selectionArgs){  //查询数据
        return db.rawQuery(sql,selectionArgs);
    }


    public Cursor selectValue2(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
                               String having, String orderBy) {
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }


    public void close(){  //关闭数据库
        db.close();
    }


    public void clear(String name) {  //清空数据
        db.execSQL("delete from " + name);
    }


    public Cursor selectAll(String table){  //全查询
        return selectValue2(table,null,null,null,null,null,null);
    }


    public Cursor selectColumn(String table ,String[] column){  //查询指定的列
        return selectValue2(table,column,null,null,null,null,null);
    }

}
