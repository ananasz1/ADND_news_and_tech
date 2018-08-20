package com.example.vikischmideg.newsandtech.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vikischmideg.newsandtech.R;
import com.example.vikischmideg.newsandtech.adapter.BookmarkAdapter;
import com.example.vikischmideg.newsandtech.model.ArticleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.vikischmideg.newsandtech.adapter.BookmarkAdapter.getNewsArticleFromDatabase;
import static com.example.vikischmideg.newsandtech.ui.BbcFragment.ARTICLE_KEY;


/**
 * * Created by schmidegv on 2018. 07. 26..
 */

public class BookmarkedFragment extends Fragment implements BookmarkAdapter.OnArticleClickListener,
        LoaderManager.LoaderCallbacks<List<ArticleResponse>>
{

    @BindView(R.id.news_list_rv)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.nothing_here)
    TextView nothingHere;
    private BookmarkAdapter bookmarkAdapter;
    private Parcelable parcelable;
    private List<ClipData.Item> newsList;
    private int LOADER_ID = 0;


    public BookmarkedFragment() {
    }

    public static BookmarkedFragment newInstance() {
        BookmarkedFragment saved = new BookmarkedFragment();

        return saved;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmarked_fragment, container, false);
        ButterKnife.bind(this, view);


        bookmarkAdapter = new BookmarkAdapter(getContext(),  this,
                new ArrayList<ArticleResponse>(),
                true);
        recyclerView.setAdapter(bookmarkAdapter);
        recyclerView.setHasFixedSize(true);
        return view;
    }

    private void loadSavedArticles(List<ArticleResponse> articleResponses) {
        bookmarkAdapter.setArticleList( articleResponses );
        recyclerView.setAdapter(bookmarkAdapter);
        recyclerView.scheduleLayoutAnimation();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        parcelable = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onArticleClick(ArticleResponse clickedArticle) {

        Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
        intent.putExtra(ARTICLE_KEY, clickedArticle);

        startActivity(intent);
        Objects.requireNonNull( getActivity() ).overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable( TheNYFragment.LIST_STATE, parcelable);
        outState.putParcelableArrayList( TheNYFragment.NEWS_LIST, (ArrayList<ArticleResponse>) bookmarkAdapter.getArticleList());
    }

    @NonNull
    @Override
    public Loader<List<ArticleResponse>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<ArticleResponse>> loader,
                               List<ArticleResponse> articles) {
        progressBar.setVisibility(View.GONE);

        loadSavedArticles(articles);

        if (bookmarkAdapter.getItemCount() == 0) {
            nothingHere.setText(getString(R.string.no_bookmarked_articles ));
            nothingHere.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility( View.VISIBLE );
            nothingHere.setVisibility( View.GONE );
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<ArticleResponse>> loader) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }


    @SuppressLint("StaticFieldLeak")
    static class NewsLoader extends AsyncTaskLoader<List<ArticleResponse>> {
        private Context context;

        NewsLoader(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<ArticleResponse> loadInBackground() {
            return getNewsArticleFromDatabase(context);
        }
    }
}
