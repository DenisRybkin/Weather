package com.example.weather.api.mappers

import com.example.weather.api.models.WeatherModel
import org.json.JSONArray
import org.json.JSONObject

fun mapForecastWeather(response: String): List<WeatherModel> {
    if (response.isEmpty()) return emptyList()

    val rootObject = JSONObject(response)
    val city = rootObject.getJSONObject("location").getString("name")
    val days = rootObject.getJSONObject("forecast").getJSONArray("forecastday")

    val list = ArrayList<WeatherModel>()
    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject
        val day = item.getJSONObject("day") as JSONObject
        val condition = day.getJSONObject("condition") as JSONObject
        list.add(
            WeatherModel(
                city,
                item.getString("date"),
                (if (i == 0) rootObject.getJSONObject("current").getString("temp_c").toFloat().toInt()
                    .toString() else null),
                condition.getString("text"),
                condition.getString("icon"),
                day.getString("maxtemp_c").toFloat().toInt().toString(),
                day.getString("mintemp_c").toFloat().toInt().toString(),
                item.getJSONArray("hour").toString(),
                (if (i == 0) (rootObject.getJSONObject("current")
                    .getInt("is_day") == 1) else null),
            )
        )
    }

    return list
}

fun mapForecastWeatherByHours(hours: String): List<WeatherModel> {
    if (hours.isEmpty()) return emptyList()

    val hoursArray = JSONArray(hours)
    val list = ArrayList<WeatherModel>()

    for (i in 0 until hoursArray.length()) {
        val item = hoursArray[i] as JSONObject
        val condition = item.getJSONObject("condition") as JSONObject
        list.add(
            WeatherModel(
                "",
                item.getString("time"),
                item.getString("temp_c").toFloat().toInt().toString(),
                condition.getString("text"),
                condition.getString("icon"),
                null,
                null,
                null,
                item.getString("is_day") == "1",
            )
        )
    }

    return list
}