package com.example.vikischmideg.newsandtech.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by schmidegv on 2018. 07. 24..
 */

public class NewsProvider extends ContentProvider {

    private NewsDbHelper newsDbHelper;


    @Override
    public boolean onCreate() {
        newsDbHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor retCursor = newsDbHelper.getReadableDatabase().query(
                NewsContract.NewsEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Unknown Uri: " + uri);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = newsDbHelper.getWritableDatabase();
        Uri returnUri;
        long id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, contentValues);
        if (id > 0) {
            returnUri = ContentUris.withAppendedId(NewsContract.NewsEntry.CONTENT_URI, id);
        } else {
            throw new android.database.SQLException("Failed to insert row into: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = newsDbHelper.getWritableDatabase();

        int rowsDeleted = db.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
}

