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

    private val stateGetMovies = MoviesState.GetMovies(
        loading = false,
        sucsessful = false,
        error = false,
        errorMessage = null,
        movies = null
    )


    override  val intentChannel: MutableSharedFlow<MoviesIntent> = MutableSharedFlow(replay = 0)

    private val _viewState: MutableStateFlow<MoviesState> = MutableStateFlow(stateGetMovies)
    override val viewState: StateFlow<MoviesState> = _viewState

    var movies = ArrayList<Movie>()

    init {
        viewModelScope.launch {
           intentChannel.collect { intent ->
                when (intent) {
                    MoviesIntent.OpenApp -> getMovies()
                }
            }
        }
    }

    private fun getMovies() {
       viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _viewState.value = stateGetMovies.copy(error = true, errorMessage = errorAnalyzer.analyze(throwable) )
        }) {
            _viewState.value = stateGetMovies.copy(loading = true)
            val movies = repository.getMovie()
            _viewState.value = stateGetMovies.copy(sucsessful = true, movies = movies)
        }
    }


}