package dev.tomco.a25a_10357_l10

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.tomco.a25a_10357_l10.adapters.MovieAdapter
import dev.tomco.a25a_10357_l10.databinding.ActivityMovieListBinding
import dev.tomco.a25a_10357_l10.interfaces.MovieCallback

import dev.tomco.a25a_10357_l10.models.Movie
import dev.tomco.a25a_10357_l10.utilities.Constants

class MovieListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val movieRef = Firebase.database.getReference(Constants.DB.MOVIE_REF)

        movieRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<List<Movie>>()

                if (value != null){
                    movieAdapter.movies = value
                    movieAdapter.notifyDataSetChanged()
                }
                else
                    movieAdapter = MovieAdapter(emptyList())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })

        movieAdapter = MovieAdapter()
        movieAdapter.movieCallback = object : MovieCallback {
            override fun favoriteButtonClicked(movie: Movie, position: Int) {
                movie.isFavorite = !movie.isFavorite
                movieRef.child(position.toString())
                    .child("favorite")
                    .setValue(movie.isFavorite)
                movieAdapter.notifyItemChanged(position)
            }

        }

        binding.mainRVList.adapter = movieAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.mainRVList.layoutManager = linearLayoutManager

    }
}