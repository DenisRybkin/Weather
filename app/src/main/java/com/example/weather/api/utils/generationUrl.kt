package com.example.weather.api.utils

import com.example.weather.api.constants.URIs
import com.example.weather.api.constants.BASE_URL
import com.example.weather.api.constants.API_KEY


private fun addQueries(url: String, params: Map<String, String>): String =
    "$url${params.map { (key, value) -> "&$key=$value" }.joinToString("")}"

private fun generateBase(uri: String): String = "$BASE_URL$uri?key=$API_KEY"


fun generateCurrentUrl(city: String, aqi: String = "no"): String =
    addQueries(generateBase(URIs.current), mapOf("q" to city, "aqi" to aqi))

fun generateForecastUrl(city: String, days: String = "1",aqi: String = "no",alerts: String = "no"): String =
    addQueries(generateBase(URIs.forecast), mapOf("q" to city, "days" to days,"aqi" to aqi, "alerts" to alerts))


// TODO: add generateURLFunctions for all URIs