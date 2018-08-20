package com.example.vikischmideg.newsandtech.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by schmidegv on 2018. 07. 24..
 */

public class ArticleSource implements Parcelable {
    public static final Creator<ArticleSource> CREATOR = new Creator<ArticleSource>() {
        @Override
        public ArticleSource createFromParcel(Parcel in) {
            return new ArticleSource(in);
        }

        @Override
        public ArticleSource[] newArray(int size) {
            return new ArticleSource[size];
        }
    };
    private String name;
    private String id;

    protected ArticleSource(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static Creator<ArticleSource> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
