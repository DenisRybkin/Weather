package com.example.weather.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.R
import com.example.weather.api.models.WeatherModel
import com.example.weather.utils.getDayAndMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainCard(
    currentDayWeather: MutableState<WeatherModel?>,
    onRefresh: () -> Unit,
    onOpenSearchDialog: () -> Unit
) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = if (currentDayWeather.value?.time != null) getDayAndMonth(
                            currentDayWeather.value!!.time
                        ) else "",
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White
                    )
                    AsyncImage(
                        model = if (currentDayWeather.value?.iconUrl != null) "https:" + currentDayWeather.value!!.iconUrl else "",
                        contentDescription = "weather icon",
                        modifier = Modifier.size(35.dp)
                    )
                }
                Text(
                    text = if (currentDayWeather.value?.city != null) currentDayWeather.value!!.city else "",
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White
                )
                if (currentDayWeather.value?.currentTemp != null)
                    Text(
                        text = (currentDayWeather.value!!.currentTemp ?: "0") + "°",
                        style = TextStyle(fontSize = 75.sp),
                        color = Color.White
                    )
                else if (currentDayWeather.value?.minTemp != null && currentDayWeather.value?.maxTemp != null)
                    Text(
                        text = "${currentDayWeather.value!!.maxTemp}°/${currentDayWeather.value!!.minTemp}°",
                        style = TextStyle(fontSize = 75.sp),
                        color = Color.White
                    )
                Text(
                    text = if (currentDayWeather.value?.condition != null) currentDayWeather.value!!.condition else "Unknown",
                    style = TextStyle(fontSize = 20.sp),
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onOpenSearchDialog.invoke() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "city search",
                            tint = Color.White
                        )
                    }
                    if (currentDayWeather.value?.currentTemp != null && currentDayWeather.value?.maxTemp != null && currentDayWeather.value?.minTemp != null)
                        Text(
                            text = "${currentDayWeather.value!!.maxTemp}°/${currentDayWeather.value!!.minTemp}°",
                            style = TextStyle(fontSize = 16.sp),
                            color = Color.White
                        )

                    IconButton(onClick = { onRefresh.invoke() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.sync),
                            contentDescription = "refresh",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSearch(onClose: () -> Unit, onSubmit: (String) -> Unit) {

    val city = remember {
        mutableStateOf<String>("")
    }

    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(onClick = { onSubmit(city.value); onClose.invoke() }) {
                Text(
                    text = "Ok"
                )
            }
        },
        dismissButton = { TextButton(onClick = onClose) { Text(text = "Cancel") } },
        title = {
            Text(text = "Enter the city:")
        },
        text = {
            TextField(
                value = city.value,
                onValueChange = {
                    city.value = it
                },
                label = { Text("City") },
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}