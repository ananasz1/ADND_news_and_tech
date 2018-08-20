package com.example.vikischmideg.newsandtech.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vikischmideg.newsandtech.BuildConfig;
import com.example.vikischmideg.newsandtech.R;
import com.example.vikischmideg.newsandtech.adapter.NewsAdapter;
import com.example.vikischmideg.newsandtech.model.Article;
import com.example.vikischmideg.newsandtech.model.ArticleResponse;
import com.example.vikischmideg.newsandtech.network.ApiClient;
import com.example.vikischmideg.newsandtech.network.ArticleParsing;
import com.example.vikischmideg.newsandtech.network.NetworkConnectivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * http://jakewharton.github.io/butterknife/
 * https://antonioleiva.com/swiperefreshlayout/
 */

public class TheVergeFragment extends Fragment implements NewsAdapter.OnArticleClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARTICLE_KEY = "article_key";
    public static final String LIST_STATE = "list_state";
    public static final String NEWS_LIST = "news_list";
    public static int listSize;
    @BindView(R.id.news_list_rv)
    RecyclerView newsRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.messages_to_show)
    TextView messagesTextView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.messages_view)
    LinearLayout messagesView;
    private NewsAdapter newsAdapter;
    private Parcelable parcelable;
    private String source;


    public TheVergeFragment() {
    }

    public static TheVergeFragment newInstance() {
        TheVergeFragment theVergeFragment = new TheVergeFragment();

        return theVergeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.article_list_fragment, container, false );
        ButterKnife.bind( this, view );

        newsAdapter = new NewsAdapter( getContext(), this, new ArrayList<ArticleResponse>() );
        newsRecyclerView.setAdapter( newsAdapter );
        newsRecyclerView.setHasFixedSize( true );
        source = getString( R.string.source_theverge );
        if (savedInstanceState != null) {
            List<ArticleResponse> list = savedInstanceState.getParcelableArrayList( NEWS_LIST );
            newsAdapter.setArticleList( list );
            parcelable = savedInstanceState.getParcelable( LIST_STATE );
            newsRecyclerView.getLayoutManager().onRestoreInstanceState( parcelable );
        } else {
            loadArticles();
        }

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeLayout.setOnRefreshListener(this);

        return view;
    }

    private void loadArticles() {
        progressBar.setVisibility( View.VISIBLE );
        if (!NetworkConnectivityUtil.isNetworkAvailable( getContext() )) {
            messagesView.setVisibility( View.VISIBLE );
            messagesTextView.setText( R.string.connection_message );
            progressBar.setVisibility( View.INVISIBLE );
            return;
        }
        ArticleParsing articleParsing = ApiClient.getNewsClient().create( ArticleParsing.class );
        final Call<Article> articleCall = articleParsing.getHeadlines( source, "en", 20, BuildConfig.API_KEY );
        articleCall.enqueue( new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                Article article = response.body();
                if (null == article) showErrorMessage();
                List<ArticleResponse> articleResponses = article.getArticles();
                newsAdapter.setArticleList( articleResponses );
                newsRecyclerView.setAdapter( newsAdapter );
                newsRecyclerView.scheduleLayoutAnimation();
                progressBar.setVisibility( View.INVISIBLE );
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                showErrorMessage();
            }
        } );

        newsAdapter.notifyDataSetChanged();
        swipeLayout.setRefreshing( false );
    }


    private void showErrorMessage() {
        progressBar.setVisibility( View.INVISIBLE );
        messagesView.setVisibility( View.VISIBLE );
        messagesTextView.setText( R.string.server_error_message );
    }

    @Override
    public void onPause() {
        super.onPause();
        parcelable = newsRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    @Override
    public void onArticleClick(ArticleResponse clickedArticle) {

        Intent intent = new Intent( getContext(), ArticleDetailActivity.class );
        intent.putExtra( ARTICLE_KEY, clickedArticle );
        startActivity( intent );
        //animation
        getActivity().overridePendingTransition( R.anim.anim_in, R.anim.anim_out );

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState( outState );
        outState.putParcelable( LIST_STATE, parcelable );
        ArrayList<ArticleResponse> articles = (ArrayList<ArticleResponse>) newsAdapter.getArticleList();
        listSize = articles.size();
        outState.putParcelableArrayList( NEWS_LIST, (ArrayList<ArticleResponse>) newsAdapter.getArticleList() );
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onRefresh() {
        if (NetworkConnectivityUtil.isNetworkAvailable( getContext() )) {
            messagesView.setVisibility( View.GONE );
            loadArticles();
        }
    }
}