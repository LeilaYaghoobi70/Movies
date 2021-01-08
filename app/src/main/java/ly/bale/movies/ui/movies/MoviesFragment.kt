package ly.bale.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ly.bale.movies.R
import ly.bale.movies.databinding.MoviesFragmentBinding
import ly.bale.movies.model.Movie
import ly.bale.movies.ui.movies.intent.MoviesIntent
import ly.bale.movies.ui.movies.state.MoviesState


@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private var binding: MoviesFragmentBinding? = null
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moviesAdapter = MoviesAdapter(viewModel.movies)

        binding = MoviesFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MoviesFragment.viewLifecycleOwner

            activity?.let {
                (it as AppCompatActivity).supportActionBar?.title =  context?.getString(R.string.app_name)
            }

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = moviesAdapter

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                        if (!viewModel.isLoading) {
                            if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == (viewModel.movies.size) - 1) {
                                lifecycleScope.launchWhenResumed {
                                    viewModel.intentChannel.emit(
                                        MoviesIntent.LoadMore
                                    )
                                }
                                viewModel.isLoading = true
                            }
                        }
                    }
                })
            }
            retryButton.setOnClickListener {
                lifecycleScope.launchWhenResumed { viewModel.intentChannel.emit(MoviesIntent.GetMovies) }
            }

            swipeRefreshLayout.setOnRefreshListener {
                lifecycleScope.launchWhenResumed { viewModel.intentChannel.emit(MoviesIntent.Refresh(true)) }
            }

        }

        moviesAdapter.onClickItem = { movieId ->
            val action = MoviesFragmentDirections.actionMoviesFragmentToMovieFragment(movieId)
            Navigation.findNavController(binding!!.root).navigate(action)
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.asLiveData().observe(viewLifecycleOwner) { state ->

            when (state) {
                is MoviesState.Loading -> {
                    binding?.errorLayout?.visibility = View.GONE

                    when {
                        viewModel.isRefresh -> return@observe
                        moviesAdapter.movies.isNullOrEmpty() -> binding?.loadingView?.visibility = View.VISIBLE
                        else -> binding?.LoadingEndLessRecyclerView?.visibility = View.VISIBLE
                    }
                }
                is MoviesState.Error -> {
                    viewModel.isLoading = false
                    binding?.swipeRefreshLayout?.isRefreshing = false
                    viewModel.isRefresh = false

                    if (moviesAdapter.movies.isNullOrEmpty()) {
                        binding?.errorLayout?.visibility = View.VISIBLE
                        binding?.loadingView?.visibility = View.GONE
                    } else {
                        Toast.makeText(requireContext(), "Oops! An error has occurred ", Toast.LENGTH_LONG).show()
                        binding?.LoadingEndLessRecyclerView?.visibility = View.GONE
                    }

                }

                is MoviesState.Success -> {

                    binding?.loadingView?.visibility = View.GONE
                    binding?.errorLayout?.visibility = View.GONE
                    binding?.LoadingEndLessRecyclerView?.visibility = View.GONE

                    if(viewModel.isRefresh) {
                        binding?.swipeRefreshLayout?.isRefreshing = false
                        viewModel.isRefresh = false
                        moviesAdapter.movies.clear()
                    }

                    moviesAdapter.submitList(state.movies as ArrayList<Movie>)

                }
                is MoviesState.None -> {
                    lifecycleScope.launchWhenResumed {
                        viewModel.intentChannel.emit(MoviesIntent.GetMovies)
                    }
                }
                else -> Unit
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}