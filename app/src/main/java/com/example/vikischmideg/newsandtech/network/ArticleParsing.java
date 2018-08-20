package com.example.vikischmideg.newsandtech.network;

import com.example.vikischmideg.newsandtech.model.Article;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by schmidegv on 2018. 07. 24..
 */

public interface ArticleParsing {

    @GET("top-headlines")
    Call<Article> getHeadlines(@Query("sources") String sources, @Query("language") String language, @Query("pageSize") int pageSize, @Query("apiKey") String apiKey);
}
