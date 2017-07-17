package com.example.asus.zhihunews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.R.attr.id;
import static android.R.attr.value;
import static android.R.attr.version;
import static android.content.ContentValues.TAG;
import static android.os.Build.ID;
import static java.sql.Types.REAL;

/**
 * SQLite DB类
 */

public class NewDBHelper extends SQLiteOpenHelper{   //创建news DB和列表
    public static final String DBNAME = "NEWS.db";  //数据库名称
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "news";  //表名称
    public static final String SQL = "create table " +
            TABLE_NAME +
            "(" + "id integer primary key ," +
            "_ID text," +
            "Title text ," +
            "ImageUrl text " +
            ")";
    private Context mContext;

    public NewDBHelper (Context context) {
        super(context,DBNAME, null,VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion) {
        String sql2="drop table if exists "+ TABLE_NAME;
         db.execSQL(sql2);
         onCreate(db);
    }
}

