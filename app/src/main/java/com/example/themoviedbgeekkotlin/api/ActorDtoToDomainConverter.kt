package com.example.themoviedbgeekkotlin.api

import com.example.themoviedbgeekkotlin.BuildConfig
import com.example.themoviedbgeekkotlin.model.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Конвертер чтобы в результирующем классе Actor были все нужные поля
suspend fun convertActorDtoToDomain(actorsDto: List<ActorDto>): List<Actor> =
    withContext(Dispatchers.Default) {
        actorsDto.map { actorDto ->
            Actor(
                id = actorDto.id,
                name = actorDto.name,
                photo_image = actorDto.image?.let { BuildConfig.BASE_IMAGE_URL + actorDto.image }
            )
        }
    }