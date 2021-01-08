package ly.bale.movies.network

import ly.bale.movies.BuildConfig
import okhttp3.Interceptor


import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.lang.Long
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.inject.Inject

object RetrofitFactory {

    private val apiKey = BuildConfig.TMDB_API_KEY
    private val loggingInterceptor = HttpLoggingInterceptor()

    init {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        val newResponse = chain.request().newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newResponse)
    }

    private val responseFormatter = Interceptor { chain ->
        val request = chain.request()
        var response = chain.proceed(request)
        val responseBody = response.body
        if (responseBody != null) {
            val source = responseBody.source()
            val buffer = source.buffer()
            source.request(Long.MAX_VALUE)
            val rawDate = buffer.clone().readString(Charset.forName("UTF-8"))
            val jsonObject: JSONObject
            try {
                jsonObject = JSONObject(rawDate)
                response = response.newBuilder()
                    .body(
                        ResponseBody.create(
                            responseBody.contentType(),
                            jsonObject.get("results").toString()
                        )
                    )
                    .build()
            }catch (ignored: JSONException) {}

        }
        response
    }

    private val clientBuilder = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(responseFormatter)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    private fun getRetrofit(): Retrofit = Retrofit.Builder()
        .client(clientBuilder)
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val services: TmdbApi = getRetrofit().create()
}