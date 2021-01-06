package ly.bale.movies.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding  = MovieFragmentBinding.inflate(inflater, container, false)
        viewModel.movieId = args.movieId

        return binding?.root
    }


    override fun onStart() {
        super.onStart()

        lifecycleScope.launchWhenResumed {
            viewModel.intentChannel.emit(MovieIntent.GetMovie(viewModel.movieId!!))
        }

        viewModel.viewState.asLiveData().observe(viewLifecycleOwner) { state ->
            when(state){
                is MovieState.Loading -> {}
                is MovieState.Error -> {}
                is MovieState.Success -> {
                    Glide.with(binding!!.root)
                        .load("https://image.tmdb.org/t/p/w500/${state.movie.posterPath}")
                        .into(binding!!.bannerVideo)
                    binding?.descriptionTextView?.text = state.movie.overview
                    binding?.titleTextView?.text = state.movie.title
                    binding?.releaseDateTextView?.text = state.movie.releaseDate
                }
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}