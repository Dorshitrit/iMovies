package com.app.dorsh.imovies.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditMovie extends AppCompatActivity {
    private EditText EditTitle, EditYear, EditReleased, EditWriter, EditRuntime, EditGenre, EditDirector, EditActor, EditPlot, EditLanguage, EditCountry;
    private String getJRated, getJAwards, getJMetascore, getJimdbRating, getJimdbVotes, getJType, getJRespons, getUWatched, getUFavorite  = null;
    private float getURating;
    private ImageView EditPoster;
    private DBHandler handler;
    private String stringExtra, holdIMGLINK, MID, holdIMDB = "";
    String checkNewPOST = "";
    private final static int CAMERA_IMG = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handler = new DBHandler(this);

        EditTitle = (EditText) findViewById(R.id.EditTitle);
        EditYear = (EditText) findViewById(R.id.EditYear);
        EditReleased = (EditText) findViewById(R.id.EditReleased);
        EditWriter = (EditText) findViewById(R.id.EditWriter);
        EditRuntime = (EditText) findViewById(R.id.EditRuntime);
        EditGenre = (EditText) findViewById(R.id.EditGenre);
        EditDirector = (EditText) findViewById(R.id.EditDirector);
        EditActor = (EditText) findViewById(R.id.EditActor);
        EditPlot = (EditText) findViewById(R.id.EditPlot);
        EditLanguage = (EditText) findViewById(R.id.EditLanguage);
        EditCountry = (EditText) findViewById(R.id.EditCountry);
        EditPoster = (ImageView) findViewById(R.id.EditPoster);
        holdIMGLINK = "";

        final Intent intent = getIntent();
        checkNewPOST = intent.getStringExtra("thisIMDB");

        stringExtra = getIntent().getStringExtra("Activity"); //Checking from witch activity for new or update
        if(stringExtra.equals("fromView")) { //Edit movie FROM DB WHEN ID = STRING MID
            MID = getIntent().getStringExtra("EditMOVIE");
            final Movies movies = handler.getMovieView(MID);
            EditTitle.setText(movies.getTitle());
            EditPlot.setText(movies.getPlot());
            EditYear.setText(movies.getYear());
            EditRuntime.setText(movies.getRuntime());
            EditReleased.setText(movies.getReleased());
            EditGenre.setText(movies.getGenre());
            EditDirector.setText(movies.getDirector());
            EditWriter.setText(movies.getWriter());
            EditActor.setText(movies.getActors());
            EditLanguage.setText(movies.getLanguage());
            EditCountry.setText(movies.getCountry());

            //CLM USER CANT UPDATE OR EDIT, have them only from OMDB
            getJRated = movies.getRated();
            getJAwards = movies.getAwards();
            getJMetascore = movies.getMetascore();
            getJimdbRating = movies.getImdbRating();
            getJimdbVotes = movies.getImdbVotes();
            getJType = movies.getType();
            getJRespons = movies.getResponse();
            getUWatched = movies.getWatched(); //USER COULD EDIIT FROM ViewMovie ACTIVITY
            getUFavorite = movies.getFavorite(); //USER COULD EDIIT FROM ViewMovie ACTIVITY
            getURating = movies.getURating(); //USER COULD EDIIT FROM ViewMovie ACTIVITY
            holdIMGLINK = movies.getPoster();

            if(!holdIMGLINK.equals("N/A") && !holdIMGLINK.equals("")) {
                Picasso.with(EditMovie.this).load(holdIMGLINK.toString()).placeholder(R.drawable.mposter).fit().into(EditPoster);
            } else {
                EditPoster.setImageResource(R.drawable.mposter);
            }

            EditPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] items = {
                            "Camera", "Album", "Cancel"
                    };

                    new AlertDialog.Builder(EditMovie.this)
                            .setTitle("")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (item == 0) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, CAMERA_IMG);
                                    } else if (item == 1) {
                                        Intent intent = new Intent();
                                        intent.setType(Constants.IMAGE_TYPE);
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent,
                                                "Select Picture"), Constants.SELECT_PICTURE);
                                    } else {
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .show();


                }
            });


        } else if (stringExtra.equals("NewFROMain")) {
            EditPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] items = {
                            "Camera", "Album", "Cancel"
                    };

                    new AlertDialog.Builder(EditMovie.this)
                            .setTitle("")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (item == 0) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, CAMERA_IMG);
                                    } else if (item == 1) {
                                        Intent intent = new Intent();
                                        intent.setType(Constants.IMAGE_TYPE);
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent,
                                                "Select Picture"), Constants.SELECT_PICTURE);
                                    } else {
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .show();


                }
            });

        } else if(stringExtra.equals("fromSearch")) { //New Movie from OMDB
            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    ("http://www.omdbapi.com/?i=" + intent.getStringExtra("thisIMDB") + "&plot=full&r=json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                holdIMDB = response.getString("imdbID");
                                EditTitle.setText(response.getString("Title"));
                                EditYear.setText(response.getString("Year"));
                                EditReleased.setText(response.getString("Released"));
                                EditWriter.setText(response.getString("Writer"));
                                EditRuntime.setText(response.getString("Runtime"));
                                EditGenre.setText(response.getString("Genre"));
                                EditDirector.setText(response.getString("Director"));
                                EditActor.setText(response.getString("Actors"));
                                EditPlot.setText(response.getString("Plot"));
                                EditLanguage.setText(response.getString("Language"));
                                EditCountry.setText(response.getString("Country"));
                                holdIMGLINK = response.getString("Poster");

                                if(!holdIMGLINK.equals("N/A") && !holdIMGLINK.equals("")) {
                                    Picasso.with(EditMovie.this).load(response.getString("Poster")).placeholder(R.drawable.mposter).fit().into(EditPoster);
                                } else {
                                    EditPoster.setImageResource(R.drawable.mposter);
                                }

                                //CLM USER CANT UPDATE OR EDIT, have them only from OMDB
                                getJRated = response.getString("Rated");
                                getJAwards = response.getString("Awards");
                                getJMetascore = response.getString("Metascore");
                                getJimdbRating = response.getString("imdbRating");
                                getJimdbVotes = response.getString("imdbVotes");
                                getJType = response.getString("Type");
                                getJRespons = response.getString("Response");
                                EditPoster.setImageResource(R.drawable.mposter);


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

            EditPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] items = {
                            "Camera", "Album", "Cancel"
                    };

                    new AlertDialog.Builder(EditMovie.this)
                            .setTitle("")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (item == 0) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, CAMERA_IMG);
                                    } else if (item == 1) {
                                        Intent intent = new Intent();
                                        intent.setType(Constants.IMAGE_TYPE);
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent,
                                                "Select Picture"), Constants.SELECT_PICTURE);
                                    } else {
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .show();


                }
            });

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stringExtra.equals("fromSearch") || stringExtra.equals("NewFROMain")) { //Add new movie
                    handler.addMovie(EditTitle.getText().toString(), EditYear.getText().toString(),
                            getJRated, EditReleased.getText().toString(),
                            EditRuntime.getText().toString(), EditGenre.getText().toString(),
                            EditDirector.getText().toString(), EditWriter.getText().toString(),
                            EditActor.getText().toString(), EditPlot.getText().toString(),
                            EditLanguage.getText().toString(), EditCountry.getText().toString(),
                            getJAwards, holdIMGLINK,
                            getJMetascore, getJimdbRating,
                            getJimdbVotes, holdIMDB,
                            getJType, getJRespons, "unWatched", "default", 0);
                    setResult(EditMovie.RESULT_OK, intent);
                    finish();
                } else if(stringExtra.equals("EditFROMain") || stringExtra.equals("fromView")) { //Update movie from DB
                    handler.updateMOVIE(Integer.valueOf(MID), EditTitle.getText().toString(), EditYear.getText().toString(), getJRated,
                             EditReleased.getText().toString(), EditRuntime.getText().toString(),
                            EditGenre.getText().toString(), EditDirector.getText().toString(), EditWriter.getText().toString(), EditActor.getText().toString(),
                            EditPlot.getText().toString(), EditLanguage.getText().toString(), EditCountry.getText().toString(), getJAwards, holdIMGLINK,
                            getJMetascore, getJimdbRating, getJimdbVotes, holdIMDB, getJType, getJRespons, getUWatched, getUFavorite, getURating);
                    setResult(EditMovie.RESULT_OK, intent);
                    finish();
                }
            }
        });


    }

    private void WhenCamerResult(Intent data) throws IOException {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        EditPoster.setImageBitmap(bitmap);
        saveImage(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_IMG) {
                try {
                    WhenCamerResult(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == Constants.SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                try {
                    EditPoster.setImageBitmap(new GetImage(selectedImageUri, getContentResolver()).getBitmap());
                    saveImage(new GetImage(selectedImageUri, getContentResolver()).getBitmap());

                } catch (IOException e) {
                    Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
                }
            }
        }
    }

    private void saveImage(Bitmap image) {
        try {
            File pictureFile = createImageFile();
            if (pictureFile == null) {
                Log.d("TEST",
                        "Error creating media file, check storage permissions: ");
                return;
            }

            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        holdIMGLINK = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Boolean testImage(String url){
        Bitmap tgtImg = BitmapFactory.decodeFile(url);
        if(tgtImg != null){
            return true;
        } else{
            return false;
        }
    }



}
