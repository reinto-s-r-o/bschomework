package cz.reinto.bschomework.api

import cz.reinto.bschomework.model.Note
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {

    @GET("/notes")
    fun getNotes(): Single<List<Note>>

    @GET("/notes/{id}")
    fun getNote(@Path("id") id: Int): Single<Note>

    @Headers("Content-Type: application/json")
    @PUT("/notes/{id}")
    fun updateNote(@Path("id") id: Int, @Body title: String): Single<Note>

    @DELETE("/notes/{id}")
    fun deleteNote(@Path("id") id: Int): Completable

    @Headers("Content-Type: application/json")
    @POST("/notes")
    fun createNote(@Body title: String): Completable
}