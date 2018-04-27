package com.zzs.meizitu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author zzstar
 * @data 2018/2/7
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DBNAME="img.db";
    private static final int VERSION=1; //设置版本号
    private static final String TBL_DETA="collections"; //创建表名为news的表
    private static final String TBL_DETA_COLUMN_ID="id";
    private static final String TBL_DETA_COLUMN_TITLE="srctitle";
    private static final String TBL_DETA_COLUMN_SRC="coversrc";
    private static final String TBL_DETA_COLUMN_SRCID="srcid";

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ");
        sb.append(TBL_DETA+"(");
        sb.append(TBL_DETA_COLUMN_ID +" integer primary key ,"); //设置主键
        sb.append(TBL_DETA_COLUMN_TITLE+ " varchar(100) ,");
        sb.append(TBL_DETA_COLUMN_SRC+" varchar(100) ,");
        sb.append(TBL_DETA_COLUMN_SRCID+" integer ");
        sb.append(")");
        db.execSQL(sb.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
