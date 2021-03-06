package com.app.dorsh.imovies.objects;

public class Movies {
    private int id;
    private String Title, Year, Rated, Released, Runtime, Genre, Director, Writer, Actors, Plot, Language,
            Country, Awards, Poster, Metascore, imdbRating, imdbVotes, imdbID, Type, Response, Watched, Favorite;
    private float URating;


    public Movies() {
    }


    public Movies(int id, String title, String year, String rated, String released, String runtime, String genre, String director, String writer, String actors, String plot, String language,
                  String country, String awards, String poster, String metascore, String imdbRating, String imdbVotes, String imdbID, String type, String response, String Watched, String Favorite, float URating) {
        this.id = id;
        this.Title = title;
        this.Year = year;
        this.Rated = rated;
        this.Released = released;
        this.Runtime = runtime;
        this.Genre = genre;
        this.Director = director;
        this.Writer = writer;
        this.Actors = actors;
        this.Plot = plot;
        this.Language = language;
        this.Country = country;
        this.Awards = awards;
        this.Poster = poster;
        this.Metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.imdbID = imdbID;
        this.Type = type;
        this.Response = response;
        this.Watched = Watched;
        this.Favorite = Favorite;
        this.URating = URating;
    }

    public Movies(String title, String year, String rated, String released, String runtime, String genre, String director, String writer, String actors, String plot, String language,
                  String country, String awards, String poster, String metascore, String imdbRating, String imdbVotes, String imdbID, String type, String response, String Watched, String Favorite, float URating) {
        this.Title = title;
        this.Year = year;
        this.Rated = rated;
        this.Released = released;
        this.Runtime = runtime;
        this.Genre = genre;
        this.Director = director;
        this.Writer = writer;
        this.Actors = actors;
        this.Plot = plot;
        this.Language = language;
        this.Country = country;
        this.Awards = awards;
        this.Poster = poster;
        this.Metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.imdbID = imdbID;
        this.Type = type;
        this.Response = response;
        this.Watched = Watched;
        this.Favorite = Favorite;
        this.URating = URating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getAwards() {
        return Awards;
    }

    public void setAwards(String awards) {
        Awards = awards;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getMetascore() {
        return Metascore;
    }

    public void setMetascore(String metascore) {
        Metascore = metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getWatched() {
        return Watched;
    }

    public void setWatched(String watched) {
        Watched = watched;
    }

    public String getFavorite() {
        return Favorite;
    }

    public void setFavorite(String favorite) {
        Favorite = favorite;
    }

    public float getURating() {
        return URating;
    }

    public void setURating(float URating) {
        this.URating = URating;
    }
}
