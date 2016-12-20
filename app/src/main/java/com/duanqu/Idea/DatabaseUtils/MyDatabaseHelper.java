package com.duanqu.Idea.DatabaseUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/7.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER_TABLE = "CREATE TABLE messagelist(\n" +
            "\tid INTEGER primary key AUTOINCREMENT,\n" +
            "\tuserhead varchar(100) NOT NULL,\n" +
            "\tusername varchar(50) NOT NULL,\n" +
            "\tlastmessage varchar(1000) NOT NULL,\n" +
            "\tcount INTEGER  NOT NULL,\n" +
            "\tnickname varchar(50) NOT NULL,\n" +
            "\ttag INTEGER NOT NULL DEFAULT 0\n" +
            ")";

    public static final String CREATE_MESSAGE_TABLE = "CREATE TABLE message(\n" +
            "\tid INTEGER primary key autoincrement,\n" +
            "\ttime varchar(100) NOT NULL,\n" +
            "\tmessage varchar(500) NOT NULL,\n" +
            "\tusername varchar(100) NOT NULL,\n" +
            "\tleft int NOT NULL default 0,\n" +
            "\tright int NOT NULL default 0,\n" +
            "\ttype varchar(20) NOT NULL\n" +
            ")";

    public static final String CREATE_FRIENDS_TABLE = "CREATE TABLE friends(\n" +
            "\tid INTEGER primary key autoincrement,\n" +
            "\tusername varchar(100) NOT NULL,\n" +
            "\tuserhead varchar(150) NOT NULL,\n" +
            "\tnickname varchar(150) NOT NULL,\n" +
            "\tsign varchar(200) NOT NULL\n" +
            ")";



    private Context mContext;


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
        db.execSQL(CREATE_FRIENDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
