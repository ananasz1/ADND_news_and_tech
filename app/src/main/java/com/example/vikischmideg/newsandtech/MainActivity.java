package com.example.vikischmideg.newsandtech;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.vikischmideg.newsandtech.ui.BbcFragment;
import com.example.vikischmideg.newsandtech.ui.BookmarkedFragment;
import com.example.vikischmideg.newsandtech.ui.CnnFragment;
import com.example.vikischmideg.newsandtech.ui.EngadgetFragment;
import com.example.vikischmideg.newsandtech.ui.TechRadarFragment;
import com.example.vikischmideg.newsandtech.ui.TheNYFragment;
import com.example.vikischmideg.newsandtech.ui.TheVergeFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by schmidegv on 2018. 07. 26..
 */

/**
 * When created this app,
 * I have used lessons and repos from Udacity course
 * sources from stackoverflow (OMG, many... :) )
 * my earlier apps from ABND and ADND mostly popular movies and baking app)
 * BloodDroid app (@https://medium.com/@melinda.kostenszki/the-blooddroid-app-475e2ef4f20d)
 * https://developer.android.com (for many parts of the app)
 * https://antonioleiva.com/recyclerview-listener/
 * some sources refer in relevant files
 * 
 * https://stackoverflow.com/questions/22296531/navigation-drawer-default-fragment
 * http://camposha.info/source/android-navigationview-fragments-recyclerview
 * http://jakewharton.github.io/butterknife/
 * https://github.com/firebase/quickstart-android/tree/master/analytics
 * https://firebase.google.com/docs/analytics/android/start/
 */


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String TOOLBAR_SUBTITLE_STATE = "toolbar_subtitle_state";

    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;
    private String subtitle;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        ButterKnife.bind( this );
        setSupportActionBar( toolbar );
        getSupportActionBar().setSubtitle( subtitle );

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener( this );


         // save chosen fragment and  set default when no opened one before
         // save toolbar subtitle state based on my popular movies app

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt( STATE_SELECTED_POSITION );
            mCurrentSelectedPosition = savedInstanceState.getInt( TOOLBAR_SUBTITLE_STATE );
            getSupportActionBar().setSubtitle( subtitle );
            mFromSavedInstanceState = true;
        } else {
            FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
            fragmentManager.replace( R.id.container_id, new BbcFragment() );
            subtitle = getResources().getString( R.string.bbc );
            getSupportActionBar().setSubtitle( subtitle );
            fragmentManager.commit();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        outState.putInt( STATE_SELECTED_POSITION, mCurrentSelectedPosition );
        outState.putString( "subtitle", subtitle );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        mCurrentSelectedPosition = savedInstanceState.getInt( STATE_SELECTED_POSITION, 0 );
        mCurrentSelectedPosition = savedInstanceState.getInt( TOOLBAR_SUBTITLE_STATE );
        subtitle = savedInstanceState.getString( "subtitle" );
        getSupportActionBar().setSubtitle( subtitle );
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String name = getResources().getResourceName(id);
        String text = "Hey, you choose " + name;
        //open fragment when item is selected
        if (id == R.id.bbc) {
            //perform transition to replace container with fragment
            mCurrentSelectedPosition = 1;
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace( R.id.container_id, BbcFragment.newInstance() ).commit();
            subtitle = getResources().getString( R.string.bbc );
            getSupportActionBar().setSubtitle( subtitle );

        } else if (id == R.id.newyorktimes) {
            mCurrentSelectedPosition = 2;
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace( R.id.container_id, TheNYFragment.newInstance() ).commit();
            subtitle = getResources().getString( R.string.TNYT );
            getSupportActionBar().setSubtitle( subtitle );

        } else if (id == R.id.cnn) {
            mCurrentSelectedPosition = 3;
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace( R.id.container_id, CnnFragment.newInstance() ).commit();
            subtitle = getResources().getString( R.string.cnn );
            getSupportActionBar().setSubtitle( subtitle );

        } else if (id == R.id.engadget) {
            mCurrentSelectedPosition = 4;
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace( R.id.container_id, EngadgetFragment.newInstance() ).commit();
            subtitle = getResources().getString( R.string.engadget );
            getSupportActionBar().setSubtitle( subtitle );

        } else if (id == R.id.theverge) {
            mCurrentSelectedPosition = 5;
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace( R.id.container_id, TheVergeFragment.newInstance() ).commit();
            subtitle = getResources().getString( R.string.theverge );
            getSupportActionBar().setSubtitle( subtitle );

        } else if (id == R.id.techradar) {
            mCurrentSelectedPosition = 6;
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace( R.id.container_id, TechRadarFragment.newInstance() ).commit();
            subtitle = getResources().getString( R.string.techradar );
            getSupportActionBar().setSubtitle( subtitle );

        } else if (id == R.id.bookmarked) {
            mCurrentSelectedPosition = 7;
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace( R.id.container_id, BookmarkedFragment.newInstance() ).commit();
            subtitle = getResources().getString( R.string.bookmarked );
            getSupportActionBar().setSubtitle( subtitle );
        }

        drawer.closeDrawer( GravityCompat.START );


        // [START custom_event]
        Bundle params = new Bundle();
        params.putString("fragment_name", name);
        params.putString("full_text", text);
        mFirebaseAnalytics.logEvent("choose_fragment", params);
        // [END custom_event]

        return false;
    }



}