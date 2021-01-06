package ly.bale.movies.ui.movie

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
import ly.bale.movies.arc.Intent
import ly.bale.movies.arc.Model
import ly.bale.movies.arc.State
import ly.bale.movies.repository.MoviesRepository
import ly.bale.movies.ui.movie.intent.MovieIntent
import ly.bale.movies.ui.movie.state.MovieState

class MovieViewModel @ViewModelInject constructor(
    private val repository: MoviesRepository,
    private val errorAnalyzer: ErrorAnalyzer,
    @Assisted private val savedStateHandle: SavedStateHandle
):ViewModel(),Model<State, Intent> {

    var movieId: Int? = null

    override val intentChannel: MutableSharedFlow<Intent> = MutableSharedFlow(replay = 0)

    private val _viewState: MutableStateFlow<State> = MutableStateFlow(MovieState.None)
    override val viewState: StateFlow<State> = _viewState

    init {
        viewModelScope.launch {
            intentChannel.collect { intent ->
                when(intent){
                   is  MovieIntent.GetMovie -> getMovie(intent.movieId)
                }

            }
        }

    }

    private fun getMovie(movieId: Int){
        viewModelScope.launch(CoroutineExceptionHandler{_, throwable ->
            _viewState.value = MovieState.Error(errorMessage = errorAnalyzer.analyze(throwable))
        }) {
            _viewState.value = MovieState.Loading
            val movie = repository.getMovieById(movieId)
            _viewState.value = MovieState.Success(movie)
        }
    }
}