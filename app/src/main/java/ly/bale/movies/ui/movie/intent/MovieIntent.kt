package ly.bale.movies.ui.movie.intent

import ly.bale.movies.arc.Intent

sealed class MovieIntent : Intent {
    data class GetMovie(val movieId: Int) : MovieIntent()
    object LoadMore : MovieIntent()
}