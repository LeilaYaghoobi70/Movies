package ly.bale.movies.ui.movies.intent

import ly.bale.movies.arc.Intent

sealed class MoviesIntent : Intent {
    object GetMovies : MoviesIntent()
    object LoadMore: MoviesIntent()
    data class Refresh(val isRefresh: Boolean): MoviesIntent()
}