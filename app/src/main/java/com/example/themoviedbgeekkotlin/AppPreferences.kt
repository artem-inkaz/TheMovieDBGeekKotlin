package com.example.themoviedbgeekkotlin

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    private const val INIT_ADULT = "INIT_ADULT"
    private const val INIT_LANG = "INIT_LANG"
    private const val NAME_PREF = "PREFERENCES"

    private lateinit var mPreferences: SharedPreferences

    // получаем настройки из контекста
    fun getPreference(context: Context): SharedPreferences {
        mPreferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE)
        return mPreferences
    }

    // задаем был авторизован пользователь или нет
    fun setAdult(initAdult: Boolean) {
        mPreferences.edit()
            .putBoolean(INIT_ADULT, initAdult)
            .apply()
    }

    // передаем тип БД
    fun setLang(lang: String) {
        mPreferences.edit()
            .putString(INIT_LANG, lang)
            .apply()
    }

    // получение из наших настроек Adult
    fun getAdult(): Boolean {
        //в случае если не был установлен флаг Adult
        return mPreferences.getBoolean(INIT_ADULT, false)
    }

    // получение из наших настроек Lang
    fun getLang(): String? {
        //в случае если не был установлен флаг Adult
        return mPreferences.getString(INIT_LANG, "ru")
    }

    fun deleteAllPrefs() {
        mPreferences.edit()
            .clear()
            .apply()
    }
}