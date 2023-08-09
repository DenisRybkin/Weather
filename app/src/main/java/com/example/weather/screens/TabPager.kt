package com.example.weather.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.api.mappers.mapForecastWeatherByHours
import com.example.weather.api.models.WeatherModel
import com.example.weather.ui.theme.DarkColorScheme
import com.example.weather.ui.theme.LightColorScheme
import com.example.weather.utils.getDayOfWeek
import com.example.weather.utils.getHour
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

private enum class Tabs {
    HOURS,
    DAYS
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(
    daysList: MutableState<List<WeatherModel>?>,
    currentDay: MutableState<WeatherModel?>,
    isDay: Boolean
) {
    val tabList = listOf(Tabs.HOURS, Tabs.DAYS)
    val pagerState = rememberPagerState(Tabs.HOURS.ordinal)
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    fun onClickTab(tab: Int) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(tab)
        }

    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .padding(start = 10.dp, end = 10.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex, indicator = { positions ->
                TabRowDefaults.Indicator(
                    color = Color.White, height = 2.dp, modifier = Modifier.pagerTabIndicatorOffset(
                        pagerState, positions
                    )
                )
            }, contentColor = Color.White, backgroundColor = if(isDay) LightColorScheme.primary else DarkColorScheme.primary
        ) {
            tabList.forEach { tab ->
                Tab(selected = tab.ordinal == selectedTabIndex, onClick = {
                    onClickTab(tab.ordinal)
                }, text = {
                    Text(
                        text = tab.toString(), color = Color.White, fontSize = 16.sp
                    )
                })
            }
        }
        HorizontalPager(
            count = tabList.size,
            state = pagerState,
            modifier = Modifier
                .weight(1.0f)
                .fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { tab ->
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp),
                colors = CardDefaults.cardColors(containerColor = if(isDay) LightColorScheme.primary else DarkColorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                ListOfPage(
                    list = when (tab) {
                        Tabs.HOURS.ordinal -> if (currentDay.value?.hours != null) mapForecastWeatherByHours(
                            currentDay.value!!.hours!!
                        ) else emptyList()
                        Tabs.DAYS.ordinal -> daysList.value ?: listOf()
                        else -> daysList.value ?: listOf()
                    }, currentDay = currentDay,
                    tab = tab
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListOfPage(list: List<WeatherModel>, currentDay: MutableState<WeatherModel?>, tab: Int) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(
            list
        ) { _, item -> ListItem(item, currentDay, tab) }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListItem(weatherModel: WeatherModel, currentDay: MutableState<WeatherModel?>, tab: Int) {
    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth().clip(RoundedCornerShape(10.dp))
            .clickable (enabled = weatherModel.hours != null) { currentDay.value = weatherModel },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (tab == Tabs.HOURS.ordinal) getHour(weatherModel.time) else getDayOfWeek(
                weatherModel.time
            ),
            color = Color.White,
            fontSize = 16.sp
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https:" + weatherModel.iconUrl,
                contentDescription = "weather icon",
                modifier = Modifier.size(30.dp)
            )
            Text(text = weatherModel.condition, color = Color.White, fontSize = 16.sp)
        }
        Text(
            text = if (weatherModel.maxTemp == null || weatherModel.minTemp == null) "${weatherModel.currentTemp}°" else "${weatherModel.maxTemp}° / ${weatherModel.minTemp}°",
            color = Color.White,
            fontSize = 16.sp
        )

    }
}