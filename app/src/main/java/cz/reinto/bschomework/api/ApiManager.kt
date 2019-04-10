package cz.reinto.bschomework.api

import cz.reinto.bschomework.model.Note
import io.reactivex.Single

class ApiManager(private val apiService: ApiService) {

    fun getNotes(): Single<List<Note>> = apiService.getNotes()

    fun getNote(id: Int) = apiService.getNote(id)

    fun updateNote(id: Int, title: String) = apiService.updateNote(id, title)

    fun deleteNote(id: Int) = apiService.deleteNote(id)

    fun createNote(title: String) = apiService.createNote(title)
}