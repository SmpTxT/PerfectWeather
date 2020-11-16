package com.example.perfectweathertrue.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.perfectweathertrue.R
import com.example.perfectweathertrue.data.ApiWeatherService
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    val cityName = arrayOf(
        "Москва",
        "Санкт-Петербург",
        "Челябинск",
        "Кисловодск",
        "Кубинка",
        "Подольск",
        "Ростов",
        "Кашира",
        "Мытищи",
        "Кострома",
        "Калуга",
        "Химки",
        "Красногорск",
        "Одинцово",
        "Якутск",
        "Королев",
        "Казань",
        "Чебоксары",
        "Воронеж",
        "Уфа",
        "Берёзовский",
        "Люберцы",
        "Минск",
        "Реутов",
        "Иркутск",
        "Домодедово",
        "Новотроицк",
        "Зеленоград",
        "Череповец",
        "Калуга"
    )

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main){

            button.setOnClickListener{
                val mdf = MyDialogFragment()
                val manager = activity?.supportFragmentManager
                if (manager != null) {mdf.show(manager, "myDialog")}

            }
        }
    }
}