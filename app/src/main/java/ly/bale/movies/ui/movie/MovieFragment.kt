package ly.bale.movies.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ly.bale.movies.BuildConfig
import ly.bale.movies.databinding.MovieFragmentBinding

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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}