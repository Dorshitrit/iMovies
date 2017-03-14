package com.app.dorsh.imovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dorsh.imovies.R;
import com.app.dorsh.imovies.db.DBHandler;
import com.app.dorsh.imovies.objects.Movies;
import com.app.dorsh.imovies.ui.EditMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieAdapter.ViewListHolder>  {
    private List<Movies> searchListMovies;
    private DBHandler handler;
    String imdbID;
    private Context context;
    private Activity activity;
    private String alertAdd = "This movie already in your Library!";

    public class ViewListHolder extends RecyclerView.ViewHolder {
        CardView scv;
        TextView resultSearchList;
        ImageView resultSearchListIMG;


        ViewListHolder(View view) {
            super(view);
            scv = (CardView) view.findViewById(R.id.scv);
            resultSearchList = (TextView) view.findViewById(R.id.resultSearchList);
            resultSearchListIMG = (ImageView) view.findViewById(R.id.resultSearchListIMG);
        }
    }

    public SearchMovieAdapter(List<Movies> searchListMovies, Context context, Activity activity) {
        this.searchListMovies = searchListMovies;
        this.context = context;
        this.activity = activity;
    }

    public SearchMovieAdapter.ViewListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scv, parent, false);
        ViewListHolder adapter = new ViewListHolder(view);
        return adapter;
    }

    @Override
    public void onBindViewHolder(final SearchMovieAdapter.ViewListHolder holder, int position) {
        holder.resultSearchList.setText(searchListMovies.get(position).getTitle());

        if(!searchListMovies.get(position).getPoster().equals("N/A")) {
            Picasso.with(context).load(searchListMovies.get(position).getPoster()).placeholder(R.drawable.mposter).fit().into(holder.resultSearchListIMG);
        } else {
            holder.resultSearchListIMG.setImageResource(R.drawable.mposter);
        }

        imdbID = searchListMovies.get(position).getImdbID();


        holder.scv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler = new DBHandler(activity);
                boolean check =  handler.checkMovie(searchListMovies.get(holder.getAdapterPosition()).getImdbID());
                if(check == true) {
                    Intent intent = new Intent(context, EditMovie.class);
                    intent.putExtra("thisIMDB", searchListMovies.get(holder.getAdapterPosition()).getImdbID());
                    intent.putExtra("Activity", "fromSearch");
                    activity.startActivityForResult(intent, 2000);
                    activity.setResult(Activity.RESULT_OK, intent);
                } else {
                    Toast.makeText(activity, alertAdd, Toast.LENGTH_SHORT).show();
                }
            }

        });


    }


    @Override
    public int getItemCount() {
        return searchListMovies.size();
    }
}

