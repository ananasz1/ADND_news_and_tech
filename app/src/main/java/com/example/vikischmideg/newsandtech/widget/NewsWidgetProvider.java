package com.example.vikischmideg.newsandtech.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.example.vikischmideg.newsandtech.BuildConfig;
import com.example.vikischmideg.newsandtech.R;
import com.example.vikischmideg.newsandtech.model.Article;
import com.example.vikischmideg.newsandtech.model.ArticleResponse;
import com.example.vikischmideg.newsandtech.network.ApiClient;
import com.example.vikischmideg.newsandtech.network.ArticleParsing;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by schmidegv on 2018. 07. 24..
 */
/**
 * https://medium.com/@puruchauhan/android-widget-for-starters-5db14f23009b
 * open browser from widget:
 *https://stackoverflow.com/questions/4730652/how-to-launch-website-on-widget-click
 */

public class NewsWidgetProvider extends AppWidgetProvider {


    private AppWidgetTarget appWidgetTarget;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

        ArticleParsing articleParsing = ApiClient.getNewsClient().create(ArticleParsing.class);
        final Call<Article> articleCall = articleParsing.getHeadlines(  "the-verge","en",
                20,  BuildConfig.API_KEY);
        articleCall.enqueue(new Callback<Article>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                Article article = response.body();
                if (article != null) {
                    List<ArticleResponse> articleList = article.getArticles();
                    ArticleResponse articleResponse = articleList.get(0);
                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.news_widget);
                    Uri uri = Uri.parse( articleResponse.getUrl() );
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    PendingIntent pIntent = PendingIntent.getActivity(context, R.id.widget_parent_layout, intent, 0);
                    remoteViews.setOnClickPendingIntent(R.id.widget_parent_layout, pIntent);
                    remoteViews.setTextViewText(R.id.widget_title, articleResponse.getTitle());
                    appWidgetTarget = new AppWidgetTarget(context, R.id.widget_image, remoteViews, appWidgetIds);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.error(ContextCompat.getDrawable(context.getApplicationContext(),
                            R.drawable.placeholder));
                    Glide
                            .with(context.getApplicationContext())
                            .setDefaultRequestOptions(requestOptions)
                            .asBitmap()
                            .load( articleResponse.getImageUrl())
                            .into(appWidgetTarget);

                    }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
