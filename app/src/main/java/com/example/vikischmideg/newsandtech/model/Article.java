package com.example.vikischmideg.newsandtech.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by schmidegv on 2018. 07. 24..
 */

public class Article implements Parcelable {
    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
    private List<ArticleResponse> articles;

    protected Article(Parcel in) {
        in.readList(articles, ArticleResponse.class.getClassLoader());
    }

    public List<ArticleResponse> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleResponse> articles) {
        this.articles = articles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(articles);
    }
}
