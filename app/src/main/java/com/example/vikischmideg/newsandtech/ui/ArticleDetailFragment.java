package com.example.vikischmideg.newsandtech.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vikischmideg.newsandtech.R;
import com.example.vikischmideg.newsandtech.model.ArticleResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by schmidegv on 2018. 07. 26..
 */

/**
 * https://medium.com/@gun0912/android-start-activity-by-custom-url-scheme-eada62fdf9eb
 */

public class ArticleDetailFragment extends Fragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.article_detail_image_iv)
    ImageView articleImageView;
    @BindView(R.id.article_detail_title_tv)
    TextView articleTitleTextView;
    @BindView(R.id.article_detail_summary_tv)
    TextView articleSummaryTv;
    @BindView(R.id.article_detail_source_tv)
    TextView articleSourceTv;
    @BindView(R.id.article_detail_published_tv)
    TextView articlePublishedAtTv;
    @BindView(R.id.fab)
    FloatingActionButton shareFab;
    @BindView(R.id.browser_button)
    ImageButton browserButton;
    @BindView(R.id.adView)
    AdView mAdView;
    private ArticleResponse mArticleResponse;


    public ArticleDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();


        if (args.containsKey(TheNYFragment.ARTICLE_KEY)) {
            mArticleResponse = args.getParcelable(TheNYFragment.ARTICLE_KEY);
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_detail, container, false);
        ButterKnife.bind(this, view);
        if (mArticleResponse != null) {
            setContentInViews();
        }
        if (getActivity() instanceof ArticleDetailActivity) {
            if (toolbar != null) {
                ((ArticleDetailActivity) getActivity()).setSupportActionBar(toolbar);
                ActionBar actionBar = ((ArticleDetailActivity) getActivity()).getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mArticleResponse.getSource().getName());
            }
        }

        shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareArticle();
            }
        });

        browserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInBrowser();
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        return view;
    }


    private void setContentInViews() {
        articleTitleTextView.setText(mArticleResponse.getTitle());
        setFormatedTextView( articleSummaryTv, mArticleResponse.getDescription(), false, -1);
        setFormatedTextView( articleSourceTv, mArticleResponse.getSource().getName(), true,
                R.string.article_source);
        String publishedDate = mArticleResponse.getPublishedAt();
        if (!isEmpty(publishedDate) && publishedDate.length() >= 10) {
            setFormatedTextView( articlePublishedAtTv, publishedDate.substring(0, 10), true, R.string.published_date);
        } else articlePublishedAtTv.setVisibility(View.GONE);

        setArticleImage(mArticleResponse.getImageUrl());
    }

    private void shareArticle() {
        String mimeType = "text/plain";
        String title = "Share Article";
        String newsTitle = mArticleResponse.getTitle();
        String newsUrl = mArticleResponse.getUrl();

        ShareCompat.IntentBuilder.from(getActivity())
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(newsTitle + "\n" + newsUrl)
                .startChooser();
    }


    private void openInBrowser() {
        if (isEmpty(mArticleResponse.getUrl()))
            browserButton.setVisibility(View.GONE);

        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(mArticleResponse.getUrl()));
        startActivity(browserIntent);

    }

    private boolean isEmpty(String description) {
        return TextUtils.isEmpty(description);
    }

    private void setFormatedTextView(TextView textView, String text, boolean format, int resId) {
        if (isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            if (format)
                formatText(textView, text, resId);
            else {
                textView.setText(text);
            }
        }
    }


    @SuppressLint("CheckResult")
    private void setArticleImage(String imageUrl) {
        Context context = getContext();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error( ContextCompat.getDrawable(context, R.drawable.placeholder));
        requestOptions.fallback(ContextCompat.getDrawable(context, R.drawable.placeholder));
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(imageUrl)
                .into( articleImageView );
    }

    private void formatText(TextView textView, String text, int resId) {
        String formattedText = getString(resId, text);
        textView.setText(formattedText);
    }

}
