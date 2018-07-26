package gm32.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;

import java.util.List;

import gm32.retrofit.retrofit.ApiClient;
import gm32.retrofit.retrofit.ApiInterface;
import gm32.retrofit.retrofit.model.Movie;
import gm32.retrofit.retrofit.model.MovieSearch;
import gm32.retrofit.retrofit.model.TopMovie;
import gm32.retrofit.view.MoviesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "950fba8cce01755c0cecc9810fa1d3dd";
    SearchView searchView;
    boolean search= false;
    ApiInterface apiService;
    MoviesAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        apiService = ApiClient.getClient().create(ApiInterface.class);

        searchView= findViewById(R.id.search_View);
        searchView.setOnQueryTextListener(this);

        viewMovieTop();

    }

    public void viewMovieByWord(Call<MovieSearch> call){
        call.enqueue(new Callback<MovieSearch>() {
            @Override
            public void onResponse(Call<MovieSearch> call, Response<MovieSearch> response) {
                List<Movie> movies = response.body().getResults();
                adapter= new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<MovieSearch> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    public void viewMovieTop(){
        Call<TopMovie> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<TopMovie>() {
            @Override
            public void onResponse(Call<TopMovie> call, Response<TopMovie> response) {
                List<Movie> movies = response.body().getResults();
                adapter= new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TopMovie> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        search= true;
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if(!s.equals("")){
            Call<MovieSearch> movie = apiService.getSearchMovies(s,API_KEY);
            viewMovieByWord(movie);
        }
        else{
            viewMovieTop();
        }

        return false;
    }
}
