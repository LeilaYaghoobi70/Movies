package ly.bale.movies.arc

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow


interface Model<s : State, I : Intent> {
    val intentChannel: MutableSharedFlow<I>
    val viewState: StateFlow<s>
}
