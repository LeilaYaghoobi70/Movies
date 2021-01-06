package ly.bale.movies.ui.movie.state

import ly.bale.movies.arc.State
import ly.bale.movies.model.Movie

sealed class MovieState : State {
    data class Success(val movie: Movie): MovieState()
    data class Error(val errorMessage: String): MovieState()
    object Loading: MovieState()
    object None: MovieState()
}