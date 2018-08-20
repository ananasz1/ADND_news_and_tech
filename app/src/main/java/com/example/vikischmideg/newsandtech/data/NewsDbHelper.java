package com.example.vikischmideg.newsandtech.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by schmidegv on 2018. 07. 24..
 */

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "article_db";
    private static final int DATABASE_VERSION = 1;

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_NEWS_ARTICLE_TABLE =
                "CREATE TABLE " + NewsContract.NewsEntry.TABLE_NAME + " ("
                        + NewsContract.NewsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + NewsContract.NewsEntry.COLUMN_ARTICLE_TITLE + " TEXT NOT NULL, "
                        + NewsContract.NewsEntry.COLUMN_ARTICLE_SOURCE + " TEXT, "
                        + NewsContract.NewsEntry.COLUMN_ARTICLE_SUM + " TEXT NOT NULL, "
                        + NewsContract.NewsEntry.COLUMN_ARTICLE_DATE_PUB + " TEXT ); ";

        sqLiteDatabase.execSQL(CREATE_NEWS_ARTICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}