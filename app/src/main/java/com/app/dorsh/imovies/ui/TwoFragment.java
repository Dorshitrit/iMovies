package com.app.dorsh.imovies.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.dorsh.imovies.R;
import com.app.dorsh.imovies.adapter.MoviesListAdapter;
import com.app.dorsh.imovies.db.DBHandler;
import com.app.dorsh.imovies.objects.Movies;

import java.util.ArrayList;
import java.util.List;

public class TwoFragment extends Fragment {
    private DBHandler handler;
    List<Movies> moviesList;
    public GridLayoutManager gLayout;
    private MoviesListAdapter adapter;
    RecyclerView recyclerView;


    public TwoFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_two, container, false);

        moviesList = new ArrayList<>();
        handler = new DBHandler(getContext());

        gLayout = new GridLayoutManager(getContext(), 3);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewPOP);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gLayout);


        adapter = new MoviesListAdapter(getActivity().getBaseContext(), getActivity(), handler.getMoviesList(2));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return v;
    }




}
