package com.example.vikischmideg.newsandtech.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vikischmideg.newsandtech.R;
import com.example.vikischmideg.newsandtech.data.NewsContract;
import com.example.vikischmideg.newsandtech.model.ArticleResponse;
import com.example.vikischmideg.newsandtech.model.ArticleSource;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by schmidegv on 2018. 07. 26..
 */

/**
 * https://github.com/bumptech/glide
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.NewsViewHolder> {

    private final OnArticleClickListener onArticleClickListener;
    private Context context;
    private List<ArticleResponse> articleList;

    public BookmarkAdapter(Context context, OnArticleClickListener onArticleClickListener, List<ArticleResponse> articleList, boolean isBookmarked) {
        this.context = context;
        this.onArticleClickListener = onArticleClickListener;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.article_list_item, parent, false );

        return new NewsViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        ArticleResponse currentArticle = articleList.get( position );
        ArticleSource articleSource = currentArticle.getSource();
        holder.articleSourceTv.setText( articleSource.getName() );

        String publishedDate = currentArticle.getPublishedAt();
        if (!TextUtils.isEmpty( publishedDate ) && publishedDate.length() >= 10) {
            holder.articlePublishedTv.setText( publishedDate.substring( 0, 10 ) );
        } else {
            holder.articlePublishedTv.setText( currentArticle.getPublishedAt() );
        }
        holder.articleTitleTv.setText( currentArticle.getTitle() );
        setNewsThumbnail( holder.articleImageView, currentArticle.getImageUrl() );
        setBookmarkButton( holder.bookmarkArticle, currentArticle );
        holder.bookmarkArticle.setImageResource( R.drawable.baseline_bookmark_black_18dp );

    }


    private void setBookmarkButton(final ImageButton button, final ArticleResponse article) {
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                deleteArticleFromDb( context, article );
                deleteFromBookmarked( article );
                Toast toast = Toast.makeText( view.getContext(), R.string.remove_bookmarked, Toast.LENGTH_SHORT );
                toast.show();
            }
        } );
    }


    private void deleteFromBookmarked(ArticleResponse article) {
        articleList.remove( article );
        notifyDataSetChanged();
    }

    public static void deleteArticleFromDb(Context context, ArticleResponse article) {
        String selection = NewsContract.NewsEntry.COLUMN_ARTICLE_TITLE + "=? AND " + NewsContract.NewsEntry.COLUMN_ARTICLE_SOURCE + "=? AND " + NewsContract.NewsEntry.COLUMN_ARTICLE_DATE_PUB + "=?";

        String[] selections = {

                article.getTitle(), article.getSource().getName(), article.getPublishedAt()};
        int rowsDeleted = context.getContentResolver().delete( NewsContract.NewsEntry.CONTENT_URI, selection, selections );

    }

    @SuppressLint("CheckResult")
    private void setNewsThumbnail(ImageView imageThumbnail, String imageUrl) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error( ContextCompat.getDrawable( context, R.drawable.placeholder ) );
        Glide.with( context ).setDefaultRequestOptions( requestOptions ).load( imageUrl ).into( imageThumbnail );
    }

    public List<ArticleResponse> getArticleList() {
        return this.articleList;
    }

    public void setArticleList(List<ArticleResponse> articleList) {
        this.articleList = articleList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return articleList == null ? 0 : articleList.size();
    }


    public interface OnArticleClickListener {
        void onArticleClick(ArticleResponse clickedArticle);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.article_image_iv)
        ImageView articleImageView;
        @BindView(R.id.article_title)
        TextView articleTitleTv;
        @BindView(R.id.article_source_tv)
        TextView articleSourceTv;
        @BindView(R.id.published_at_tv)
        TextView articlePublishedTv;
        @BindView(R.id.bookmark_btn)
        ImageButton bookmarkArticle;

        NewsViewHolder(View itemView) {
            super( itemView );
            itemView.setOnClickListener( this );
            ButterKnife.bind( this, itemView );
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onArticleClickListener.onArticleClick( articleList.get( position ) );
        }
    }


    public static List<ArticleResponse> getNewsArticleFromDatabase(Context context) {
        String orderBy = NewsContract.NewsEntry.COLUMN_ID + " DESC";

        List<ArticleResponse> articles = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query( NewsContract.NewsEntry.CONTENT_URI, null, null, null, orderBy );

        if (null != cursor && cursor.moveToFirst()) {
            do {
                int articleIndex = cursor.getColumnIndex( NewsContract.NewsEntry.COLUMN_ARTICLE_SUM );
                String articleJson = cursor.getString( articleIndex );
                Gson gson = new Gson();
                ArticleResponse article = gson.fromJson( articleJson, ArticleResponse.class );
                articles.add( article );
            } while (cursor.moveToNext());
            cursor.close();
        }
        return articles;
    }
}
