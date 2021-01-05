package ly.bale.movies.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(MoviesCallback(newMovies, movies))
        movies.clear()
        movies.addAll(newMovies)
        diffResult.dispatchUpdatesTo(this)
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
            binding.ratingBar.rating = movie.voteAverage.toFloat()/2
            binding.rootLayout.setOnClickListener {onClickItem?.invoke(movie.id) }
        }
    }

    class MoviesCallback(
        private var newMovies: List<Movie>, private var oldMovies: List<Movie>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldMovies.size

        override fun getNewListSize(): Int = newMovies.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldMovies[oldItemPosition].id == newMovies[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldMovies[oldItemPosition] == newMovies[newItemPosition]

    }
}
