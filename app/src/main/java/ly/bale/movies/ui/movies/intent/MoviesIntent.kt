package ly.bale.movies.ui.movies.intent

import ly.bale.movies.arc.Intent

sealed class MoviesIntent : Intent {
    object OpenApp : MoviesIntent()
}