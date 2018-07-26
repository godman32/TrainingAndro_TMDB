package gm32.retrofit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import gm32.retrofit.retrofit.ApiClient;
import gm32.retrofit.retrofit.ApiInterface;
import gm32.retrofit.retrofit.model.Movie;
import gm32.retrofit.retrofit.model.VideoResult;
import gm32.retrofit.retrofit.model.Video;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetails extends AppCompatActivity {

    private final static String API_KEY = "950fba8cce01755c0cecc9810fa1d3dd";
    private final static String urlImage= "http://image.tmdb.org/t/p/w185/";

    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    TextView judul,releaseDate, populer, vote, deskripsi;
    ImageView toolbarImage;
    FloatingActionButton goToYoutube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        collapsingToolbarLayout= findViewById(R.id.collapsingToolbar);
        judul= findViewById(R.id.judul);
        releaseDate= findViewById(R.id.realeaseDate);
        populer= findViewById(R.id.populer);
        vote= findViewById(R.id.vote);
        deskripsi= findViewById(R.id.deskripsi);
        coordinatorLayout= findViewById(R.id.coordinatorLayout);
        toolbarImage= findViewById(R.id.toolbarImage);
        goToYoutube= findViewById(R.id.goToYoutube);



        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("id",0);
        //Log.d("ID", String.valueOf(id));

        final ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Movie> call = apiService.getMovieDetails(Integer.valueOf(id),API_KEY);
        final Call<Video> video= apiService.getVideoLinks(Integer.valueOf(id),API_KEY);
        call.enqueue(new Callback<Movie>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {


                Movie movie= response.body();
                collapsingToolbarLayout.setTitle(movie.getTitle());
                judul.setText(movie.getTitle());
                releaseDate.setText(movie.getReleaseDate());
                populer.setText(String.valueOf(movie.getPopularity()));
                vote.setText(String.valueOf(movie.getVoteCount()));
                deskripsi.setText(movie.getOverview());



                    video.enqueue(new Callback<Video>() {
                        @Override
                        public void onResponse(Call<Video> call, Response<Video> responses) {
                            //Log.d("Response", String.valueOf(responses.code()));
                            //Log.d("Response", new GsonBuilder().setPrettyPrinting().create().toJson(responses));

                            final List<VideoResult> result= responses.body().getResults();
                            goToYoutube.setVisibility(View.VISIBLE);
                            goToYoutube.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String url = "https://www.youtube.com/watch?v="+result.get(0).getKey();
                                    Intent bukabrowser = new Intent(Intent. ACTION_VIEW);
                                    bukabrowser.setData(Uri. parse(url));
                                    startActivity(bukabrowser); }
                            });

                        }

                        @Override
                        public void onFailure(Call<Video> call, Throwable t) {
                            Log.e("Error", t.toString());
                        }
                    });

                    Glide.with(MovieDetails.this)
                            .load(urlImage+movie.getPosterPath())
                            .into(toolbarImage);

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
    }
}
