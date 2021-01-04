package ly.bale.movies.ui.movie

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ly.bale.movies.repository.MoviesRepository

class MovieViewModel @ViewModelInject constructor(
    private val repository: MoviesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
):ViewModel() {

    var movieId: Int? = null
}