package com.app.dorsh.imovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dorsh.imovies.R;
import com.app.dorsh.imovies.db.DBHandler;
import com.app.dorsh.imovies.objects.Movies;
import com.app.dorsh.imovies.ui.EditMovie;
import com.app.dorsh.imovies.ui.ViewMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewListHolder> {
    private List<Movies> moviesList;
    private Context context;
    private Activity activity;
    private static final int GO_VIEW_MOVIE = 1200;
    DBHandler handler;


    public class ViewListHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        CardView cv;
        private TextView mSubject;
        private ImageView mImage, isFAV, isWATCHED, isRATE;


        ViewListHolder(View view) {
            super(view);
            cv = (CardView) view.findViewById(R.id.cv);
            mSubject = (TextView) view.findViewById(R.id.mSubject);
            mImage = (ImageView) view.findViewById(R.id.mImage);
            isFAV = (ImageView) view.findViewById(R.id.isFAV);
            isWATCHED = (ImageView) view.findViewById(R.id.isWATCHED);
            isRATE = (ImageView) view.findViewById(R.id.isRATE);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.menu_list);
            popup.setOnMenuItemClickListener(this);
            popup.show();
            return false;
        }

        @Override
        public void onClick(View v) {

        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.idx_edit_movie:
                    Intent EditMovie = new Intent(activity, EditMovie.class);
                    String sendMOVIEID = String.valueOf(moviesList.get(getAdapterPosition()).getId());
                    EditMovie.putExtra("EditMOVIE", sendMOVIEID);
                    EditMovie.putExtra("Activity", "EditFROMain");
                    activity.startActivityForResult(EditMovie, 3000);
                    break;
                case R.id.idx_delete_movie:
                    handler = new DBHandler(context);
                    String whichid = String.valueOf(moviesList.get(getAdapterPosition()).getId());
                    handler.dellmovies(whichid);
                    moviesList.remove(moviesList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                    break;
            }
            return true;
        }

    }

    public MoviesListAdapter(Context context, Activity activity, List<Movies> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public MoviesListAdapter.ViewListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cv, null);
        ViewListHolder adapter = new ViewListHolder(view);
        return adapter;


    }

    @Override
    public void onBindViewHolder(final MoviesListAdapter.ViewListHolder holder, int position) {

        holder.mSubject.setText(moviesList.get(position).getTitle());

        if(!moviesList.get(position).getPoster().equals("N/A") && !moviesList.get(position).getPoster().equals("")) {
            Picasso.with(context).load(moviesList.get(position).getPoster()).placeholder(R.drawable.mposter).fit().into(holder.mImage);
        } else {
            holder.mImage.setImageResource(R.drawable.mposter);
        }

        if(moviesList.get(holder.getAdapterPosition()).getFavorite().equals("Favorite")) {
            holder.isFAV.setImageResource(R.drawable.ic_favorite_on);
        } else if(moviesList.get(holder.getAdapterPosition()).getFavorite().equals("default")) {
            holder.isFAV.setImageResource(R.drawable.ic_favorite);
        }

        if(moviesList.get(holder.getAdapterPosition()).getWatched().equals("unwatch")) {
            holder.isWATCHED.setImageResource(R.drawable.ic_unwatch);
        } else if(moviesList.get(holder.getAdapterPosition()).getWatched().equals("Watched")) {
            holder.isWATCHED.setImageResource(R.drawable.ic_watch);
        }

        if(moviesList.get(holder.getAdapterPosition()).getURating() == 0) {
            holder.isRATE.setImageResource(R.drawable.ic_rating_off);
        } else {
            holder.isRATE.setImageResource(R.drawable.ic_rating_on);
        }

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewMovie.class);
                String MovieID = String.valueOf(moviesList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("MovieID", MovieID);
                intent.putExtra("Fragment", "Library");
                ((Activity) view.getContext()).startActivityForResult(intent, GO_VIEW_MOVIE );
                activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }



}


