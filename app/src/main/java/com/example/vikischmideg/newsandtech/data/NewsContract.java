package com.example.vikischmideg.newsandtech.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by schmidegv on 2018. 07. 24..
 */

public class NewsContract {
    public static final String AUTHORITY = "com.example.vikischmideg.newsandtech";
    public static final String PATH_NEWS = "news";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class NewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_NEWS)
                        .build();

        public static final String TABLE_NAME = "article_db";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_ARTICLE_SUM = "article_sum";
        public static final String COLUMN_ARTICLE_TITLE = "article_title";
        public static final String COLUMN_ARTICLE_DATE_PUB = "article_date_published";
        public static final String COLUMN_ARTICLE_SOURCE = "article_source";

    }
}
