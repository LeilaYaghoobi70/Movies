package ly.bale.movies.ui.movies.state

import ly.bale.movies.arc.State
import ly.bale.movies.model.Movie

sealed class MoviesState : State {

    data class GetMovies(
        var loading: Boolean,
        var sucsessful: Boolean,
        var error: Boolean,
        var errorMessage: String?,
        var movies: List<Movie>?
    ) : MoviesState()
}