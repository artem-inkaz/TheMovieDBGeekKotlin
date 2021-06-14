package com.example.themoviedbgeekkotlin

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.themoviedbgeekkotlin.backgroundworkmanager.SimpleMovieWorkerManager
import com.example.themoviedbgeekkotlin.databinding.MainActivityBinding
import com.example.themoviedbgeekkotlin.favourite.FragmentFavourite
import com.example.themoviedbgeekkotlin.movielist.FragmentMovieList
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var mToolbar: Toolbar

    // навигация
    lateinit var navController: NavController
    lateinit var bottom_navigation: BottomNavigationView

    // создание связки, при закрытии MainActivity должны обнулят нашу ссылку _binding
    private var _binding: MainActivityBinding? = null

    // сссылка ссылается на наш _binding данная строчка val mBinding get() = _binding!! позволит избежать проверки на null
    // _binding!! - будет 100% не null
    val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // инициализация глобальной константы APP_ACTIVITY
        // теперь можем получать контекст из любого нашего приложения
        APP_ACTIVITY = this
        // инициализация нашей связки
        _binding = MainActivityBinding.inflate(layoutInflater)
        // root овый макет
        setContentView(mBinding.root)
        // инициализируем mToolbar
        mToolbar = mBinding.toolbar
        // инициализируем BottomNavigationView
        bottom_navigation = mBinding.bottomNavigation
        // main_activity - fragment сюда будут устанавливаться наши все фрагменеты
        navController = findNavController(R.id.nav_host_fragment)
        // установка ToolBar
        setSupportActionBar(mToolbar)
        title = getString(R.string.title)
        initBottomNavigation()

        // как только запустилинаше приложение проинициализируем наши настройки
        AppPreferences.getPreference(this)
    }

    private fun initBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.action_home -> {
                    navController.navigate(R.id.movielistFragment)
                    true
                }
                R.id.action_favourite -> {
                    navController.navigate(R.id.favouriteFragment)
                    true
                }
                R.id.action_ratings -> {
                    navController.navigate(R.id.ratingFragment)
                    true
                }
                else -> true
            }
        }
    }

    // создадим меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searItem = menu?.findItem(R.id.action_search)
        val searchView = searItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searItem.collapseActionView()
                val bundle = Bundle()
                bundle.putString("SEARCH_MOVIE", query?.trim())
                val navController = findNavController(R.id.nav_host_fragment)
                navController.navigate(R.id.ratingFragment, bundle)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //TODO("Not yet implemented")
                return false
            }
        })
        return true
    }

    // клик по кнопочке меню
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // получение item id
        when (item.itemId) {
            R.id.action_search -> {
                //  SimpleMovieWorkerManager.startWork(this)

            }
            R.id.action_update_all -> {
                //               Toast.makeText(this,"Удалить", Toast.LENGTH_LONG).show()
                SimpleMovieWorkerManager.startWork(this)
            }
            R.id.action_share -> {
                Toast.makeText(this, "Поделиться", Toast.LENGTH_LONG).show()
            }
            R.id.action_clear_filter -> {
                Toast.makeText(this, "Отменить фильтр", Toast.LENGTH_LONG).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //когда удалена наша MainActivity
    override fun onDestroy() {
        super.onDestroy()
        // присваиваем нашей связке null и предотвращаем утечку памяти
        _binding = null
    }
}