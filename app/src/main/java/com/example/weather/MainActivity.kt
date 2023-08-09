package com.example.weather

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weather.api.featchers.getForecastData
import com.example.weather.api.models.WeatherModel
import com.example.weather.screens.DialogSearch
import com.example.weather.screens.MainCard
import com.example.weather.screens.TabLayout
import com.example.weather.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                val daysWeatherList = remember {
                    mutableStateOf<List<WeatherModel>?>(null)
                }
                val currentDayWeather = remember {
                    mutableStateOf<WeatherModel?>(null)
                }
                val isOpenSearchDialog = remember {
                    mutableStateOf<Boolean>(false)
                }

                val isDay = if(daysWeatherList.value?.get(0) == null) true else daysWeatherList.value?.get(0)?.isDay == true

                fun handleGetData(city: String = "London") {getForecastData(city, this, daysWeatherList, currentDayWeather)}
                if(daysWeatherList.value == null) handleGetData()

                fun handleOpenSearchDialog() {
                    isOpenSearchDialog.value = true
                }
                fun handleCloseSearchDialog() { isOpenSearchDialog.value = false }
                fun handleFindByCity(city: String) {handleGetData(city)}

                if(isOpenSearchDialog.value) DialogSearch({ handleCloseSearchDialog() },
                    { handleFindByCity(it) })

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Image(
                        painter = painterResource(id = if(isDay) R.drawable.bg2 else R.drawable.bg5),
                        contentDescription = "background",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    if(currentDayWeather.value == null)
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(150.dp),color = Color.White,
                                strokeWidth = 8.dp)
                        }
                    else
                        Column {
                            MainCard(currentDayWeather,
                                { handleGetData() },{ handleOpenSearchDialog() })
                            TabLayout(daysWeatherList, currentDayWeather, isDay)
                        }
                }
            }
        }
    }
}
