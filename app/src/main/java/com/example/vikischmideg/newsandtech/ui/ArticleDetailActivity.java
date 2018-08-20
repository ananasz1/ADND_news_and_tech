package com.example.vikischmideg.newsandtech.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.vikischmideg.newsandtech.R;

import static com.example.vikischmideg.newsandtech.ui.BbcFragment.ARTICLE_KEY;

/**
 * Created by schmidegv on 2018. 07. 26..
 */

public class ArticleDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_container );

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            Intent intent = getIntent();
            if (intent.hasExtra( ARTICLE_KEY)) {
                args.putParcelable( ARTICLE_KEY,
                        intent.getParcelableExtra( ARTICLE_KEY));

                ArticleDetailFragment fragment = new ArticleDetailFragment();
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.article_detail_container, fragment)
                        .commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.back_in, R.anim.back_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

}
