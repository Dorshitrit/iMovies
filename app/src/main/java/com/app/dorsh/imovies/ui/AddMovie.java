package com.app.dorsh.imovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.dorsh.imovies.R;
import com.app.dorsh.imovies.adapter.SearchMovieAdapter;
import com.app.dorsh.imovies.objects.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddMovie extends AppCompatActivity {
    private List<Movies> moviesList;
    private RecyclerView recyclerView;
    private SearchMovieAdapter adapter;
    private Button searchBTN;
    private EditText searchTITLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchTITLE = (EditText) findViewById(R.id.SearchTitle);
        searchBTN = (Button) findViewById(R.id.searchBTN);

        recyclerView = (RecyclerView) findViewById(R.id.MovieListrecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        moviesList = new ArrayList<>();

        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMoviesList(searchTITLE.getText().toString());
                    adapter.notifyDataSetChanged();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(AddMovie.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        adapter = new SearchMovieAdapter(moviesList, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 2000 && resultCode == RESULT_OK) {
            finish();
        }

    }

    private void getMoviesList(String searchMovieTitle) {
        String s = searchMovieTitle;
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                ("http://www.omdbapi.com/?s="+s+"&type=movie&tomatoes=true&r=json", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("Search");
                            moviesList.removeAll(moviesList);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject jsonObject = arr.getJSONObject(i);
                                moviesList.add(new Movies(i, jsonObject.getString("Title"), "",
                                        "", "", "", "", "", "", "", "", "", "", "", jsonObject.getString("Poster"), "", "", "", jsonObject.getString("imdbID"), "", "", "", "", 0));
                                adapter.notifyDataSetChanged();
                            }
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
