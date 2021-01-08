package ly.bale.movies.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ly.bale.movies.databinding.MovieFragmentBinding
import ly.bale.movies.ui.movie.intent.MovieIntent
import ly.bale.movies.ui.movie.state.MovieState

@AndroidEntryPoint
class MovieFragment:Fragment() {
    private var binding: MovieFragmentBinding? = null
    private val args: MovieFragmentArgs by navArgs()
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.movieId = args.movieId

        binding  = MovieFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MovieFragment.viewLifecycleOwner

            retryButton.setOnClickListener {
                lifecycleScope.launchWhenResumed {
                    viewModel.intentChannel.emit(MovieIntent.GetMovie(viewModel.movieId!!))
                }
            }

        }

        return binding?.root
    }


    override fun onStart() {
        super.onStart()

        lifecycleScope.launchWhenResumed {
            viewModel.intentChannel.emit(MovieIntent.GetMovie(viewModel.movieId!!))
        }

        viewModel.viewState.asLiveData().observe(viewLifecycleOwner) { state ->
            when(state){
                is MovieState.Loading -> {
                    binding?.loadingView?.visibility=  View.VISIBLE
                    binding?.errorLayout?.visibility = View.GONE
                }
                is MovieState.Error -> {
                    binding?.loadingView?.visibility =  View.GONE
                    binding?.errorLayout?.visibility = View.VISIBLE
                }
                is MovieState.Success -> {
                    binding?.errorLayout?.visibility = View.GONE
                    binding?.rootLayout?.visibility = View.VISIBLE
                    binding?.loadingView?.visibility = View.GONE

                    Glide.with(binding!!.root)
                        .load("https://image.tmdb.org/t/p/w500/${state.movie.posterPath}")
                        .into(binding!!.bannerVideo)

                    activity?.let {
                        (it as AppCompatActivity).supportActionBar?.title =  state.movie.title
                    }

                    state.movie.voteAverage?.let {
                        binding?.ratingBar?.rating = it.toFloat()/2
                    }

                    binding?.releaseDateTextView?.text = state.movie.releaseDate
                    binding?.descriptionTextView?.text = state.movie.overview
                }
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}