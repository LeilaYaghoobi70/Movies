package ly.bale.movies.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ly.bale.movies.databinding.ItemMovieRecyclerViewBinding
import ly.bale.movies.model.Movie


class MoviesAdapter(var movies: ArrayList<Movie>) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    var onClickItem: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder =
        MoviesViewHolder(
            ItemMovieRecyclerViewBinding.inflate(LayoutInflater.from(parent.context)
                , parent
                , false
            ), onClickItem )


    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) { holder.onBind(movie = movies[position]) }

    fun submitList(newMovies: ArrayList<Movie>) {
        movies.addAll(newMovies)
        this.notifyItemChanged(movies.size - newMovies.size-1)
    }


    override fun getItemCount(): Int = movies.size


    class MoviesViewHolder(private val binding: ItemMovieRecyclerViewBinding,private val onClickItem: ((Int) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(movie: Movie) {

            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w500/${movie.posterPath}")
                .into(binding.bannerVideo)

            binding.titleMovie.text = movie.title
            binding.overViewTextView.text = movie.overview
            movie.voteAverage?.let { binding.ratingBar.rating = it.toFloat()/2 }
            binding.rootLayout.setOnClickListener { movie.serverId?.let { id -> onClickItem?.invoke(id) } }
        }
    }

}
