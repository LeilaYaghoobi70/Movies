package ly.bale.movies.ui.movies

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.launch
import ly.bale.movies.ErrorAnalyzer
import ly.bale.movies.arc.Model
import ly.bale.movies.model.Movie
import ly.bale.movies.repository.MoviesRepository
import ly.bale.movies.ui.movies.intent.MoviesIntent
import ly.bale.movies.ui.movies.state.MoviesState


class MoviesViewModel @ViewModelInject constructor(
    private val repository: MoviesRepository,
    private val errorAnalyzer: ErrorAnalyzer,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(), Model<MoviesState, MoviesIntent> {

    var isLoading = false

    private var pageMovie = 1

    override val intentChannel: MutableSharedFlow<MoviesIntent> = MutableSharedFlow(replay = 0)

    private val _viewState: MutableStateFlow<MoviesState> = MutableStateFlow(MoviesState.None)
    override val viewState: StateFlow<MoviesState> = _viewState

    var movies = ArrayList<Movie>()
    var isRefresh = false

    init {

        viewModelScope.launch {
            intentChannel.collect { intent ->
                when (intent) {
                    MoviesIntent.GetMovies -> getMovies()
                    MoviesIntent.LoadMore -> getMovies()
                    is MoviesIntent.Refresh -> {
                        isRefresh = intent.isRefresh
                        getMovies()
                    }
                }
            }
        }
    }

    private fun getMovies() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _viewState.value = MoviesState.Error(errorMessage = errorAnalyzer.analyze(throwable))
        }) {
            _viewState.value = MoviesState.Loading

            if (isRefresh)
                pageMovie = 1

            val movies = repository.getMovie(pageNumber = pageMovie, isRefresh = isRefresh)

            pageMovie += 1
            isLoading = false

            _viewState.value = MoviesState.Success(movies = movies)
        }
    }

}