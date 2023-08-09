package com.example.weather.api.featchers

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weather.api.mappers.mapForecastWeather
import com.example.weather.api.models.WeatherModel
import com.example.weather.api.utils.generateForecastUrl

fun getForecastData(
    city: String = "London",
    context: Context,
    daysList: MutableState<List<WeatherModel>?>,
    currentDayWeather: MutableState<WeatherModel?>
) {
    val url = generateForecastUrl(city, "7")
    val queue = Volley.newRequestQueue(context)
    val sRequest = StringRequest(
        Request.Method.GET,
        url,
        { res ->
            val dto = mapForecastWeather(res)
            daysList.value = dto
            currentDayWeather.value = dto[0] },
        { Log.d("MyLog", "VolleyError: $it") })
    queue.add(sRequest)
}