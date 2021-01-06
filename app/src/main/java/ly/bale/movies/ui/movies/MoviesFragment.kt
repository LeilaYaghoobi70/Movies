package ly.bale.movies.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import ly.bale.movies.databinding.MoviesFragmentBinding
import ly.bale.movies.model.Movie
import ly.bale.movies.ui.movie.state.MovieState
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

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                        if (!viewModel.isLoading) {
                            if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == (viewModel.movies.size)-1) {
                                lifecycleScope.launchWhenResumed { viewModel.intentChannel.emit(MoviesIntent.LoadMore) }
                                viewModel.isLoading = true
                            }
                        }
                    }
                })
            }


        }

        moviesAdapter.onClickItem ={ movieId ->
            val action = MoviesFragmentDirections.actionMoviesFragmentToMovieFragment(movieId)
            Navigation.findNavController(binding!!.root).navigate(action)
        }



        viewModel.viewState
        return binding?.root
    }


    override fun onStart() {
        super.onStart()

        viewModel.viewState.asLiveData().observe(viewLifecycleOwner) { state ->

            when (state) {
                is  MoviesState.Loading  -> Log.d("leila", "")
                is  MoviesState.Error  -> Log.d("leila", "")
                is  MoviesState.Success  -> moviesAdapter.submitList(state.movies as ArrayList<Movie>)
                is  MoviesState.None ->  {
                    lifecycleScope.launchWhenResumed {
                        viewModel.intentChannel.emit(MoviesIntent.OpenApp)
                    }
                }
                else -> Unit
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