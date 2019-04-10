package cz.reinto.bschomework.interactors

import cz.reinto.bschomework.api.ApiManager
import cz.reinto.bschomework.model.Note
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NoteInteractor(private val apiManager: ApiManager) {

    fun getNotes(): Single<List<Note>> {
        return apiManager.getNotes().subscribeOn(Schedulers.io())
    }

    fun getNote(id: Int): Single<Note> {
        return apiManager.getNote(id).subscribeOn(Schedulers.io())
    }

    fun updateNote(id: Int, title: String): Single<Note> {
        return apiManager.updateNote(id, title).subscribeOn(Schedulers.io())
    }

    fun deleteNote(id: Int): Completable {
        return apiManager.deleteNote(id).subscribeOn(Schedulers.io())
    }

    fun createNote(title: String): Completable {
        return apiManager.createNote(title).subscribeOn(Schedulers.io())
    }
}