package com.example.secondachiamatadirete

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.secondachiamatadirete.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_AUTHORIZATION_HEADER = "X-RapidAPI-Key"

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder().addHeader(
            API_AUTHORIZATION_HEADER,
            "31b26c3289mshf1943203dd7c630p1e048fjsncbb31e23f6ac"
        ).build()

        return chain.proceed(newRequest)
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val logging = HttpLoggingInterceptor()
    val authorizationInterceptor = AuthorizationInterceptor()
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authorizationInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://covid-193.p.rapidapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val apiService = retrofit.create(ApiService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logging.level = HttpLoggingInterceptor.Level.BODY

        setDetails()
    }

    private fun setDetails() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getDetails()

                val totalCases = response.response[0].cases.total
                val totalDeaths = response.response[0].deaths.total
                val totalRecovered = response.response[0].cases.recovered

                withContext(Dispatchers.Main) {
                    binding.tvCovidStatistics.text =
                        "Total cases: $totalCases\nTotal deaths: $totalDeaths\nTotal recovered: $totalRecovered"
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.message}")
            }
        }
    }


}