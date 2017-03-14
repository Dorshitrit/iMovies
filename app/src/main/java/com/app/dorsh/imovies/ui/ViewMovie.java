package com.app.dorsh.imovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.dorsh.imovies.R;
import com.app.dorsh.imovies.db.Constants;
import com.app.dorsh.imovies.db.DBHandler;
import com.app.dorsh.imovies.objects.Movies;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewMovie extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    private DBHandler handler;
    private TextView mTitle, mYear, mGenre, mReleased, mRuntime, mActors, mLanguage, mCountry, mDirector, mWriter, mAwards, mPlot;
    private ImageButton mFAV, mWATCH;
    private ImageView mPoster;
    private String holdFAV, holdWATCH;
    private RatingBar mRAT;
    private String thisID = null;
    String HOLDIMDB, YTB;

    YouTubePlayerSupportFragment youTubePlayerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_fragment);

        mTitle = (TextView) findViewById(R.id.mTitle);
        mYear = (TextView) findViewById(R.id.mYear);
        mGenre = (TextView) findViewById(R.id.mGenre);
        mReleased = (TextView) findViewById(R.id.mReleased);
        mRuntime = (TextView) findViewById(R.id.mRuntime);
        mActors = (TextView) findViewById(R.id.mActors);
        mLanguage = (TextView) findViewById(R.id.mLanguage);
        mCountry = (TextView) findViewById(R.id.mCountry);
        mDirector = (TextView) findViewById(R.id.mDirector);
        mWriter = (TextView) findViewById(R.id.mWriter);
        mAwards = (TextView) findViewById(R.id.mAwards);
        mPlot = (TextView) findViewById(R.id.mPlot);

        mPoster = (ImageView) findViewById(R.id.mPoster);
        mFAV = (ImageButton) findViewById(R.id.mFAV);
        mWATCH = (ImageButton) findViewById(R.id.mWATCH);

        mRAT = (RatingBar) findViewById(R.id.mRAT);


        Intent intent = getIntent();
        handler = new DBHandler(this);
        thisID = intent.getStringExtra("MovieID");
        final int thisIDint = Integer.valueOf(thisID);

        final Movies movies = handler.getMovieView(intent.getStringExtra("MovieID"));
        mTitle.setText(movies.getTitle());
        mYear.setText(movies.getYear());
        mGenre.setText(movies.getGenre());
        mReleased.setText(movies.getReleased());
        mRuntime.setText(movies.getRuntime());
        mActors.setText(movies.getActors());
        mLanguage.setText(movies.getLanguage());
        mCountry.setText(movies.getCountry());
        mDirector.setText(movies.getDirector());
        mWriter.setText(movies.getWriter());
        mAwards.setText(movies.getAwards());
        mPlot.setText(movies.getPlot());
        mRAT.setRating(movies.getURating());

        HOLDIMDB = movies.getImdbID();


        JsonObjectRequest jsonRequest = new JsonObjectRequest
                ("https://api.themoviedb.org/3/movie/" + HOLDIMDB + "/videos?api_key=611350894b645d7c9feb3263d8c48b05", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            JSONObject object = jsonArray.getJSONObject(0);
                            YTB = object.getString("key");

                            youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, ViewMovie.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonRequest);


        holdFAV = movies.getFavorite();
        if (holdFAV.equals("default")) {
            mFAV.setImageResource(R.drawable.ic_favorite);
        } else if (holdFAV.equals("Favorite")) {
            mFAV.setImageResource(R.drawable.ic_favorite_on);
        }

        mFAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holdFAV.equals("default")) {
                    mFAV.setImageResource(R.drawable.ic_favorite_on);
                    handler.updateFAV(thisIDint, "Favorite");
                    holdFAV = "Favorite";
                } else if (holdFAV.equals("Favorite")) {
                    mFAV.setImageResource(R.drawable.ic_favorite);
                    handler.updateFAV(thisIDint, "default");
                    holdFAV = "default";
                }
            }
        });

        holdWATCH = movies.getWatched();
        if (holdWATCH.equals("unwatch")) {
            mWATCH.setImageResource(R.drawable.ic_unwatch);
        } else if (holdWATCH.equals("Watched")) {
            mWATCH.setImageResource(R.drawable.ic_watch);
        }


        mWATCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holdWATCH.equals("unWatched")) {
                    mWATCH.setImageResource(R.drawable.ic_watch);
                    handler.updateWATCH(thisIDint, "Watched");
                    holdWATCH = "Watched";
                } else if (holdWATCH.equals("Watched")) {
                    mWATCH.setImageResource(R.drawable.ic_unwatch);
                    handler.updateWATCH(thisIDint, "unWatched");
                    holdWATCH = "unWatched";
                }
            }
        });

        mRAT.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                handler.updateRAT(thisIDint, rating);
            }
        });


        if (!movies.getPoster().equals("N/A") && !movies.getPoster().equals("")) {
            Picasso.with(ViewMovie.this).load(movies.getPoster()).placeholder(R.drawable.mposter).fit().into(mPoster);
        } else {
            mPoster.setImageResource(R.drawable.mposter);
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Movies movies = handler.getMovieView(thisID);
        mTitle.setText(movies.getTitle());
        mYear.setText(movies.getYear());
        mGenre.setText(movies.getGenre());
        mReleased.setText(movies.getReleased());
        mRuntime.setText(movies.getRuntime());
        mActors.setText(movies.getActors());
        mLanguage.setText(movies.getLanguage());
        mCountry.setText(movies.getCountry());
        mDirector.setText(movies.getDirector());
        mWriter.setText(movies.getWriter());
        mAwards.setText(movies.getAwards());
        mPlot.setText(movies.getPlot());
        mRAT.setRating(movies.getURating());

        if(!movies.getPoster().equals("N/A") && !movies.getPoster().equals("")) {
            Picasso.with(ViewMovie.this).load(movies.getPoster()).placeholder(R.drawable.mposter).fit().into(mPoster);
        } else {
            mPoster.setImageResource(R.drawable.mposter);
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_SUBJECT, mTitle.getText());
        myShareIntent.putExtra(Intent.EXTRA_TEXT, mPlot.getText());
        actionProvider.setShareIntent(myShareIntent);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_vEdit) {
            Intent intent = new Intent(ViewMovie.this, EditMovie.class);
            intent.putExtra("EditMOVIE", thisID);
            Log.e("CHECK", thisID);
            intent.putExtra("Activity", "fromView");
            startActivityForResult(intent, 2);
            return true;
        }
        if (id == R.id.action_vDelete) {
            handler.dellmovies(thisID);
            Intent intent = new Intent(ViewMovie.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(YTB);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {

                }

                @Override
                public void onLoaded(String s) {

                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                }

                @Override
                public void onVideoEnded() {

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {

                }
            });
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
