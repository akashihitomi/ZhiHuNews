package com.example.asus.zhihunews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import static android.R.attr.value;
import static android.content.ContentValues.TAG;
import static android.os.Build.ID;
import static android.os.Build.VERSION_CODES.N;
import static com.example.asus.zhihunews.NewDBHelper.SQL;

public class NewsDBUtils {
    private NewDBHelper newDBHelper;
    public NewsDBUtils(Context context) {  //与DB建立连接
        newDBHelper = new NewDBHelper(context);
    }

    public void Insertnews (NEWS news) {  //添加数据
        SQLiteDatabase sqlite = newDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_ID",news.getID());
        values.put("Title",news.getTitle());
        values.put("ImageUrl",news.getIcon_URL());
        sqlite.insert(newDBHelper.TABLE_NAME, null, values);
        Log.d("SQL", "SQL save " + values);
        sqlite.close();
    }

    public void Deletenews (){  //删除数据,未完成
        SQLiteDatabase db = newDBHelper.getReadableDatabase();
        db.delete("news", "Id = ?", new String[]{"ID"});
        db.close();
    }

    public ArrayList<NEWS> Checknews() {    //获取数据库数据
        SQLiteDatabase db = newDBHelper.getWritableDatabase();
        ArrayList<NEWS> arrayList = new ArrayList<NEWS>();
        Cursor cursor = db.query("news",null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                NEWS news2 = new NEWS();
                news2.setID(cursor.getString(cursor.getColumnIndex("_ID"))) ;
                news2.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                news2.setIcon_URL(cursor.getString(cursor.getColumnIndex("ImageUrl")));
                Log.d( "SQL","title read from SQL is " + cursor.getString(cursor.getColumnIndex("Title")));
                Log.d( "SQL","imageUrl read from SQL is " + cursor.getString(cursor.getColumnIndex("ImageUrl")));
                Log.d( "SQL","id read from SQL is " + cursor.getString(cursor.getColumnIndex("_ID")));
                arrayList.add(news2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return arrayList;
    }
}
