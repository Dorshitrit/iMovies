package com.app.dorsh.imovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.app.dorsh.imovies.objects.Movies;

import java.util.ArrayList;
import java.util.List;


public class DBHandler {
    private DBHelper helper;

    public DBHandler(Context context) {
        helper = new DBHelper(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    public void addMovie(String Title, String Year, String Rated, String Released, String Runtime, String Genre, String Director, String Writer,
                         String Actors, String Plot, String Language, String Country, String Awards, String Poster, String Metascore, String imdbRating,
                         String imdbVotes, String imdbID, String Type, String Response, String Watched, String Favorite, float URating) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Constants.DB_CLM_NAME_Title, Title);
            values.put(Constants.DB_CLM_NAME_Year, Year);
            values.put(Constants.DB_CLM_NAME_Rated, Rated);
            values.put(Constants.DB_CLM_NAME_Released, Released);
            values.put(Constants.DB_CLM_NAME_Runtime, Runtime);
            values.put(Constants.DB_CLM_NAME_Genre, Genre);
            values.put(Constants.DB_CLM_NAME_Director, Director);
            values.put(Constants.DB_CLM_NAME_Writer, Writer);
            values.put(Constants.DB_CLM_NAME_Actors, Actors);
            values.put(Constants.DB_CLM_NAME_Plot, Plot);
            values.put(Constants.DB_CLM_NAME_Language, Language);
            values.put(Constants.DB_CLM_NAME_Country, Country);
            values.put(Constants.DB_CLM_NAME_Awards, Awards);
            values.put(Constants.DB_CLM_NAME_Poster, Poster);
            values.put(Constants.DB_CLM_NAME_Metascore, Metascore);
            values.put(Constants.DB_CLM_NAME_imdbRating, imdbRating);
            values.put(Constants.DB_CLM_NAME_imdbVotes, imdbVotes);
            values.put(Constants.DB_CLM_NAME_imdbID, imdbID);
            values.put(Constants.DB_CLM_NAME_Type, Type);
            values.put(Constants.DB_CLM_NAME_Response, Response);
            values.put(Constants.DB_CLM_NAME_Watched, Watched);
            values.put(Constants.DB_CLM_NAME_Favorite, Favorite);
            values.put(Constants.DB_CLM_NAME_URating, URating);
            db.insert(Constants.DB_TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public List<Movies> getMoviesList(int select) {
        SQLiteDatabase db = null;
        List<Movies> list = new ArrayList<>();
        String ORDER = null;
        String WHAT = null;
        switch(select) {
            case 1:
                WHAT = "_id=?";
                ORDER = " ORDER BY _id DESC";
                break;
            case 2:
                WHAT = "Favorite=?";
                ORDER = "Favorite";
                break;
            case 3:
                WHAT = "Watched=?";
                ORDER = "Watched";
                break;
            case 4:
                WHAT = "URating>?";
                ORDER = " WHERE URating > 0";
                break;
        }
        try {
            db = helper.getReadableDatabase();
            Cursor cursor = null;
            if(select == 1) {
                cursor = db.rawQuery("SELECT * FROM " + Constants.DB_TABLE_NAME + ORDER, null);
            } else if(select == 2 || select == 3) {
                cursor = db.query(Constants.DB_TABLE_NAME, null,WHAT,new String [] {ORDER}, null, null, null, null);
            } else if(select == 4) {
                cursor = db.rawQuery("SELECT * FROM " + Constants.DB_TABLE_NAME + ORDER, null);
            }
            int id;
            String Title, Poster, imdbID, FAV, WATCH;
            float RAT;
            while (cursor.moveToNext()) {
                id = cursor.getInt(0);
                Title = cursor.getString(1);
                Poster = cursor.getString(14);
                imdbID = cursor.getString(18);
                FAV = cursor.getString(21);
                WATCH = cursor.getString(22);
                RAT = cursor.getFloat(23);
                Movies movies = new Movies(id, Title, "", "", "", "", "", "", "", "", "", "", "", "", Poster, "", "", "", imdbID, "", "", FAV, WATCH, RAT);
                list.add(movies);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        return list;
    }

    public List<Movies> getMoviesListFAV() {
        SQLiteDatabase db = null;
        List<Movies> list = new ArrayList<>();
        try {
            db = helper.getReadableDatabase();
            Cursor cursor = null;
            cursor = db.rawQuery("SELECT * FROM " + Constants.DB_TABLE_NAME + " WHERE", null);
            int id;
            String Title, Poster, imdbID, FAV, WATCH;
            float RAT;
            while (cursor.moveToNext()) {
                id = cursor.getInt(0);
                Title = cursor.getString(1);
                Poster = cursor.getString(14);
                imdbID = cursor.getString(18);
                FAV = cursor.getString(21);
                WATCH = cursor.getString(22);
                RAT = cursor.getFloat(23);
                Movies movies = new Movies(id, Title, "", "", "", "", "", "", "", "", "", "", "", "", Poster, "", "", "", imdbID, "", "", FAV, WATCH, RAT);
                list.add(movies);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        return list;
    }

    public boolean checkMovie(String checkimdbID) {
        SQLiteDatabase db = null;
        boolean check = true;
        try {
            db = helper.getReadableDatabase();
            Cursor cursor = null;
            String  WHAT = "imdbID=?";
            cursor = db.query(Constants.DB_TABLE_NAME, null,WHAT,new String [] {checkimdbID}, null, null, null, null);
            String sendID;
            while (cursor.moveToNext()) {
                sendID = cursor.getString(18);
                if(sendID.equals(checkimdbID)) {
                    check = false;
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        } return check;
    }


    public Movies getMovieView(String movID) {
        SQLiteDatabase db = null;
        Movies movies = null;
        try {
            db = helper.getReadableDatabase();
            Cursor cursor = null;
            String  WHAT = "_id=?";
            cursor = db.query(Constants.DB_TABLE_NAME, null,WHAT,new String [] {movID}, null, null, null, null);
            cursor.moveToFirst();
            movies = new Movies();
            String Title, Year, Rated, Released, Runtime, Genre, Director, Writer, Actors, Plot, Language,
                        Country, Awards, Poster, Metascore, imdbRating, imdbVotes, imdbID, Type, Response, Watched, Favorite;
            float URating;
            int id = cursor.getInt(0);
            Title = cursor.getString(1);
            Year = cursor.getString(2);
            Rated = cursor.getString(3);
            Released = cursor.getString(4);
            Runtime = cursor.getString(5);
            Genre = cursor.getString(6);
            Director = cursor.getString(7);
            Writer = cursor.getString(8);
            Actors = cursor.getString(9);
            Plot = cursor.getString(10);
            Language = cursor.getString(11);
            Country = cursor.getString(12);
            Awards = cursor.getString(13);
            Poster = cursor.getString(14);
            Metascore = cursor.getString(15);
            imdbRating = cursor.getString(16);
            imdbVotes = cursor.getString(17);
            imdbID = cursor.getString(18);
            Type = cursor.getString(19);
            Response = cursor.getString(20);
            Watched = cursor.getString(21);
            Favorite = cursor.getString(22);
            URating = cursor.getFloat(23);

            movies.setId(id);
            movies.setTitle(Title);
            movies.setYear(Year);
            movies.setRated(Rated);
            movies.setReleased(Released);
            movies.setRuntime(Runtime);
            movies.setGenre(Genre);
            movies.setDirector(Director);
            movies.setWriter(Writer);
            movies.setActors(Actors);
            movies.setPlot(Plot);
            movies.setLanguage(Language);
            movies.setCountry(Country);
            movies.setAwards(Awards);
            movies.setPoster(Poster);
            movies.setMetascore(Metascore);
            movies.setImdbRating(imdbRating);
            movies.setImdbVotes(imdbVotes);
            movies.setImdbID(imdbID);
            movies.setType(Type);
            movies.setResponse(Response);
            movies.setWatched(Watched);
            movies.setFavorite(Favorite);
            movies.setURating(URating);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        return movies;
    }


    public void dellAllmovies() {
        SQLiteDatabase db = null;
        try{
            db = helper.getWritableDatabase();
            db.execSQL("DELETE FROM "+Constants.DB_TABLE_NAME);
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if(db.isOpen()) {
                db.close();
            }
        }
    }

    public void dellmovies(String thisMOVIE) {
        SQLiteDatabase db = null;
        try{
            db = helper.getWritableDatabase();
            db.delete(Constants.DB_TABLE_NAME, "_id=?", new String[] {thisMOVIE});
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if(db.isOpen()) {
                db.close();
            }
        }
    }

    public void updateMOVIE(int MID ,String title, String year, String rated, String released, String runtime, String genre, String director, String writer,
                            String actors, String plot, String lang, String country, String awards, String poster, String metascore, String imdbRating, String imdbVotes,
                            String imdbID, String type, String response, String Watched, String Favorite, float URating) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(Constants.DB_CLM_NAME_Title, title);
            args.put(Constants.DB_CLM_NAME_Year, year);
            args.put(Constants.DB_CLM_NAME_Rated, rated);
            args.put(Constants.DB_CLM_NAME_Released, released);
            args.put(Constants.DB_CLM_NAME_Runtime, runtime);
            args.put(Constants.DB_CLM_NAME_Genre, genre);
            args.put(Constants.DB_CLM_NAME_Director, director);
            args.put(Constants.DB_CLM_NAME_Writer, writer);
            args.put(Constants.DB_CLM_NAME_Actors, actors);
            args.put(Constants.DB_CLM_NAME_Plot, plot);
            args.put(Constants.DB_CLM_NAME_Language, lang);
            args.put(Constants.DB_CLM_NAME_Country, country);
            args.put(Constants.DB_CLM_NAME_Awards, awards);
            args.put(Constants.DB_CLM_NAME_Poster, poster);
            args.put(Constants.DB_CLM_NAME_Metascore, metascore);
            args.put(Constants.DB_CLM_NAME_imdbRating, imdbRating);
            args.put(Constants.DB_CLM_NAME_imdbVotes, imdbVotes);
            args.put(Constants.DB_CLM_NAME_imdbID, imdbID);
            args.put(Constants.DB_CLM_NAME_Type, type);
            args.put(Constants.DB_CLM_NAME_Response, response);
            args.put(Constants.DB_CLM_NAME_Watched, Watched);
            args.put(Constants.DB_CLM_NAME_Favorite, Favorite);
            args.put(Constants.DB_CLM_NAME_URating, URating);
            db.update(Constants.DB_TABLE_NAME, args, "_id=?", new String[] { String.valueOf(MID) });
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if(db.isOpen()) {
                db.close();
            }
        }
    }


    public void updateFAV(int MID, String fav) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(Constants.DB_CLM_NAME_Favorite, fav);
            db.update(Constants.DB_TABLE_NAME, args, "_id=?", new String[] { String.valueOf(MID) });
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if(db.isOpen()) {
                db.close();
            }
        }
    }

    public void updateWATCH(int MID, String watch) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(Constants.DB_CLM_NAME_Watched, watch);
            db.update(Constants.DB_TABLE_NAME, args, "_id=?", new String[] { String.valueOf(MID) });
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if(db.isOpen()) {
                db.close();
            }
        }
    }

    public void updateRAT(int MID, Float RATING) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(Constants.DB_CLM_NAME_URating, RATING);
            db.update(Constants.DB_TABLE_NAME, args, "_id=?", new String[] { String.valueOf(MID) });
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if(db.isOpen()) {
                db.close();
            }
        }
    }



}