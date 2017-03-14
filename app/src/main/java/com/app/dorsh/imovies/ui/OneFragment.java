package com.app.dorsh.imovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.app.dorsh.imovies.R;
import com.app.dorsh.imovies.adapter.MoviesListAdapter;
import com.app.dorsh.imovies.db.DBHandler;
import com.app.dorsh.imovies.objects.Movies;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class OneFragment extends Fragment{
    public GridLayoutManager gLayout;
    private static final int ADD_MOVIE_REQ_ON = 1000;
    private static final int ADD_MOVIE_REQ_OFF = 2000;
    private DBHandler handler;
    List<Movies> allItems;
    private MoviesListAdapter adapter;
    private RecyclerView recyclerView;
    private FrameLayout hideLayout;
    private LinearLayout fabLayout;
    private FloatingActionButton fab, addMovieFab, addMovieOfflineFab;
    private Animation fabOpenAnimation, fabCloseAnimation;
    boolean isFabMenuOpen = false;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_one, container, false);

        allItems = new ArrayList<>();
        handler = new DBHandler(getContext());

        fabLayout = (LinearLayout) v.findViewById(R.id.fabLayout);
        hideLayout = (FrameLayout) v.findViewById(R.id.hideLayout);
        addMovieFab = (FloatingActionButton) v.findViewById(R.id.addMovieFab);
        addMovieOfflineFab = (FloatingActionButton) v.findViewById(R.id.addMovieOfflineFab);
        fabOpenAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabCloseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovieFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent addMoviewON = new Intent(getContext(), AddMovie.class);
                        startActivityForResult(addMoviewON, ADD_MOVIE_REQ_ON);
                        closeFabMenu();

                    }
                });
                addMovieOfflineFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent addMoviewOFF = new Intent(getContext(), EditMovie.class);
                        addMoviewOFF.putExtra("Activity", "NewFROMain");
                        startActivityForResult(addMoviewOFF, ADD_MOVIE_REQ_OFF);
                        closeFabMenu();
                    }
                });
                if (isFabMenuOpen) {
                    closeFabMenu();
                } else {
                    openFabMenu();
                }
            }
        });

        hideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFabMenu();
            }
        });

        gLayout = new GridLayoutManager(getContext(), 3);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gLayout);

        adapter = new MoviesListAdapter(getActivity().getBaseContext(), getActivity(), handler.getMoviesList(1));
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        allItems = handler.getMoviesList(1);
        adapter = new MoviesListAdapter(getActivity().getBaseContext(), getActivity(), handler.getMoviesList(1));
        recyclerView.setAdapter(adapter);

        if (requestCode == ADD_MOVIE_REQ_ON && resultCode == RESULT_OK) {
            closeFabMenu();
            allItems = handler.getMoviesList(1);
            adapter = new MoviesListAdapter(getActivity().getBaseContext(), getActivity(), handler.getMoviesList(1));
            recyclerView.setAdapter(adapter);
        } if (requestCode == ADD_MOVIE_REQ_OFF && resultCode == RESULT_OK) {
            closeFabMenu();
            allItems = handler.getMoviesList(1);
            adapter = new MoviesListAdapter(getActivity().getBaseContext(), getActivity(), handler.getMoviesList(1));
            recyclerView.setAdapter(adapter);
        }
    }



    private void openFabMenu() {
        hideLayout.setVisibility(View.VISIBLE);
        ViewCompat.animate(fab).rotation(45.0F).withLayer().setDuration(300).setInterpolator(new OvershootInterpolator(10.0F)).start();
        fabLayout.startAnimation(fabOpenAnimation);
        addMovieFab.setClickable(true);
        isFabMenuOpen = true;
    }

    private void closeFabMenu() {
        hideLayout.setVisibility(View.GONE);
        ViewCompat.animate(fab).rotation(0.0F).withLayer().setDuration(300).setInterpolator(new OvershootInterpolator(10.0F)).start();
        fabLayout.startAnimation(fabCloseAnimation);
        addMovieFab.setClickable(false);
        isFabMenuOpen = false;
    }


}