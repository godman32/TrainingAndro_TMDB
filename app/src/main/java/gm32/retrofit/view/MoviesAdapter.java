package gm32.retrofit.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import gm32.retrofit.MovieDetails;
import gm32.retrofit.R;
import gm32.retrofit.retrofit.model.Movie;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private int rowLayout;
    private Context context;
    private final static String urlImage= "http://image.tmdb.org/t/p/w185/";

    private ArrayList<Movie> arraylist;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout moviesLayout;
        TextView movieTitle;
        TextView rating;
        Integer id;
        ImageView image;


        public MovieViewHolder(final View v) {
            super(v);

            moviesLayout = (RelativeLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            rating = (TextView) v.findViewById(R.id.rating);
            image= v.findViewById(R.id.images);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(v.getContext(), MovieDetails.class);
                    intent.putExtra("id", id);
                    v.getContext().startActivity(intent);
                }
            });


        }
    }

    public MoviesAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;

        this.arraylist = new ArrayList<Movie>();
        this.arraylist.addAll(movies);
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.movieTitle.setText(movies.get(position).getTitle());
        holder.rating.setText(movies.get(position).getVoteAverage().toString());
        holder.id= movies.get(position).getId();

        //Log.d("GAMBAR", urlImage+movies.get(position).getPosterPath());

        Picasso.get().load(urlImage+movies.get(position).getPosterPath()).into(holder.image);

//        Glide.with(context)
//                .load(urlImage+movies.get(position).getPosterPath())
//                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        movies.clear();
        if (charText.length() == 0) {
            movies.addAll(arraylist);
        } else {
            for (Movie wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    movies.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
