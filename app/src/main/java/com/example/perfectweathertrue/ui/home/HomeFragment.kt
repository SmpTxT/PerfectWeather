package com.example.perfectweathertrue.ui.home

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.perfectweathertrue.MainActivity
import com.example.perfectweathertrue.R
import com.example.perfectweathertrue.data.ApiWeatherService
import com.example.perfectweathertrue.data.response.Main
import com.example.perfectweathertrue.data.response.UrlModel
import com.example.perfectweathertrue.ui.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var anim: AnimationDrawable

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var Temp = 0

        var sharePref : SharedPreferences = requireParentFragment().requireContext().getSharedPreferences(MainActivity().PREF_NAME, MainActivity().PRIVATE_MODE)
        val editor = sharePref.edit()
        var citys = Array<String?>(sharePref!!.getInt(MainActivity().FAVORITES_SIZE, 0), {""})
        for(i in 0..citys.size-1){
            citys[i] = sharePref!!.getString(MainActivity().FAVORITES_+(i.toString()), "")
        }


        GlobalScope.launch(Dispatchers.Main) {

            val adapter = ArrayAdapter(
                requireParentFragment().requireContext(),
                android.R.layout.simple_spinner_item,
                citys
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spinner?.adapter = adapter
        }
        spinner.setSelection(sharePref.getInt(MainActivity().SELECT_CITY, 0))


        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){

                text_home.text = "Загрузка..."
                val city : String = parent.getItemAtPosition(position).toString()
                editor.putInt(MainActivity().SELECT_CITY, position)
                editor.apply()

                (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                (parent.getChildAt(0) as TextView).textSize = 20f

                if(false){
                }
                else{
                    val apiService = ApiWeatherService()
                    GlobalScope.launch(Dispatchers.Main) {
                        try{
                            val Weather = apiService.getCurrentWeather(city).await()
                            val Main = apiService.getCurrentWeather(city).await()
                            val Wind = apiService.getCurrentWeather(city).await()
                            val UrlModel = apiService.getCurrentWeather(city).await()
                            textName.text = "Город:" + "   " + Weather.name.toString()
                            text_home.text = "Температура" + "   " + (Weather.main.temp.toInt() - 273).toString() + " C°"
                            text_home2.text = "Ощущается как:  " + (Main.main.feelslike.toInt() - 273).toString() + " C°"
                            text_home3.text = "Скорость ветра " + Wind.wind.speed.toString()+" м/с"
                            text_home4.text = "Влажность: " + Main.main.humidity.toString()+" %"

                            if (UrlModel.weather[0].main == "Clouds"){
                                if(Temp <= 25 && Temp >= 15){
                                    text_home5.text = "Можно пойти погулять, но возможно скоро будет дождь"
                                }
                                imageView.setBackgroundResource(R.drawable.anim_cloud)
                                (imageView.background as AnimationDrawable).start()
                                text_home6.text = "Облачно"
                            }

                            if (UrlModel.weather[0].main == "Clear"){
                                text_home5.text = "Позовите друзей и идите гулять"
                                imageView.setBackgroundResource(R.drawable.anim_clear)
                                (imageView.background as AnimationDrawable).start()
                                text_home6.text = "Небо ясное"
                            }

                            if (UrlModel.weather[0].main == "Rain"){
                                imageView.setBackgroundResource(R.drawable.anim_rain)
                                (imageView.background as AnimationDrawable).start()
                                text_home6.text = "На улице идет классный дождь"
                                text_home5.text = "Дождик"
                            }

                            if (UrlModel.weather[0].main == "Snow"){
                                if(Temp < 0){
                                    text_home5.text = "Оденьтесь потеплее и где выши шипованные уги? "
                                }
                                imageView.setBackgroundResource(R.drawable.anim_snow)
                                (imageView.background as AnimationDrawable).start()
                                text_home6.text = "Весьма снежно"
                            }

                            if (UrlModel.weather[0].main == "Mist" || UrlModel.weather[0].main == "Fog"){
                                text_home5.text = "Будьте аккуратнее на дорогах и пешеходных переходах "
                                imageView.setBackgroundResource(R.drawable.anim_mist)
                                (imageView.background as AnimationDrawable).start()
                                text_home6.text = "Туманно"
                            }

                            if(Temp >= 0){
                                text_home5.text = "Оденьтесь потеплее и не забудьте ключи"
                            }

                            if(Temp > 30){
                                text_home5.text = "Ты мог бы быть на море, но ты учишься в шараге"
                            }
                        }
                        catch (e: Exception){
                            textName.text   = "Ошибка"
                            text_home.text  = "Ошибка"
                            text_home5.text = ""
                            text_home4.text = ""
                            text_home3.text = ""
                            text_home2.text = ""
                            text_home6.text = ""
                            imageView.setBackgroundColor(Color.parseColor("#FAFAFA"))
                        }
                    }
                }
            }
            
            override fun onNothingSelected(parent: AdapterView<*>){ }
        }
    }
}

