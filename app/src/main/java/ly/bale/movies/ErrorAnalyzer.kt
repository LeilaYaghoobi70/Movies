package ly.bale.movies

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorAnalyzer @Inject constructor(@ApplicationContext context: Context) {
    fun analyze(throwable: Throwable):String{
        return  "null"
    }
}