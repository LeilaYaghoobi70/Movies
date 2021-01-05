package ly.bale.movies.ui.movies

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.launch
import ly.bale.movies.databinding.MoviesFragmentBinding
import ly.bale.movies.model.Movie
import ly.bale.movies.ui.movies.intent.MoviesIntent
import ly.bale.movies.ui.movies.state.MoviesState

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private var binding: MoviesFragmentBinding? = null
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var moviesAdapter: MoviesAdapter
    private var uiStateJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moviesAdapter = MoviesAdapter(viewModel.movies)

        binding = MoviesFragmentBinding.inflate(inflater, container, false).apply {

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = moviesAdapter
            }
        }

        moviesAdapter.onClickItem ={movieId ->
            val action = MoviesFragmentDirections.actionMoviesFragmentToMovieFragment(movieId)
            Navigation.findNavController(binding!!.root).navigate(action)
        }

        viewModel.viewState
        return binding?.root
    }


    override fun onStart() {
        super.onStart()

        lifecycleScope.launchWhenResumed {
            viewModel.intentChannel.emit(MoviesIntent.OpenApp)
        }

        viewModel.viewState.asLiveData().observe(viewLifecycleOwner) { state ->

            when (state) {
                is MoviesState.GetMovies -> {
                    when {
                        state.sucsessful -> { moviesAdapter.submitList(state.movies as ArrayList<Movie>) }
                        state.loading -> Log.d("leila", "")
                        state.error -> Log.d("leila", "")
                    }
                }

            }
        }
    }

    override fun onStop() {
        uiStateJob?.cancel()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}