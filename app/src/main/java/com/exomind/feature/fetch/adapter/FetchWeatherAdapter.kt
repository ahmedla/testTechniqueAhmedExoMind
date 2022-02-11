package com.exomind.feature.fetch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exomind.R
import com.exomind.domain.model.WeatherEntity
import kotlin.math.roundToLong

class FetchWeatherAdapter(
    private val context: Context,
    private val dataSet: List<WeatherEntity>
) : RecyclerView.Adapter<FetchWeatherAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val icon: ImageView = view.findViewById(R.id.weatherIcon)
        private val name: TextView = view.findViewById(R.id.weatherCityName)
        private val temperature: TextView = view.findViewById(R.id.weatherCityTemperature)

        fun bind(city: WeatherEntity) {
            name.text = city.name
            temperature.text = context.getString(
                R.string.city_temperature, ((((city.temperature?.minus(32))?.times(5))?.div(9))?.times(
                                100.0
                            )?.roundToLong()?.div(100.0)).toString()
            )
            Glide
                .with(context)
                .load(city.icon)
                .into(icon)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_weather_city, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}