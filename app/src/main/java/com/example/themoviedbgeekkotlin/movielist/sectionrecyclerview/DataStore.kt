package com.example.themoviedbgeekkotlin.movielist.sectionrecyclerview

import com.example.androidacademy.data.Database_type_movie
import com.example.themoviedbgeekkotlin.model.Movie

object DataStore {

    fun populateData(): List<RecyclerViewSection> {
        val sections = mutableListOf<RecyclerViewSection>()
        val dbCount = Database_type_movie().getTypeMovie()
        repeat(Database_type_movie().getTypeMovie().size) {
            val items = mutableListOf<String>()
            val section = RecyclerViewSection(dbCount[it].timeRelease, items)
            sections.add(section)
        }
        return sections
    }
}