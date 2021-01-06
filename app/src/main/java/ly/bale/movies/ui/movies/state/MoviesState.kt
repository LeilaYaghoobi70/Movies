package ly.bale.movies.ui.movies.state

import ly.bale.movies.arc.State
import ly.bale.movies.model.Movie
import ly.bale.movies.ui.movie.state.MovieState

sealed class MoviesState : State {
    data class Success(val movies: List<Movie>): MoviesState()
    data class Error(val errorMessage: String): MoviesState()
    object Loading: MoviesState()
    object None: MoviesState()
}