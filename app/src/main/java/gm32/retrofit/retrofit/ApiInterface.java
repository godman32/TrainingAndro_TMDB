package gm32.retrofit.retrofit;


import gm32.retrofit.retrofit.model.Movie;
import gm32.retrofit.retrofit.model.MovieSearch;
import gm32.retrofit.retrofit.model.TopMovie;
import gm32.retrofit.retrofit.model.Video;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/top_rated")
    Call<TopMovie> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<Video> getVideoLinks(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieSearch> getSearchMovies(@Query("query") String query, @Query("api_key") String apiKey);
}
