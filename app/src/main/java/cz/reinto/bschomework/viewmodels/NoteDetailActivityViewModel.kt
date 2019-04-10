package cz.reinto.bschomework.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.reinto.bschomework.R
import cz.reinto.bschomework.interactors.NoteInteractor
import cz.reinto.bschomework.model.Note
import cz.reinto.bschomework.tools.SingleEventLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class NoteDetailActivityViewModel(private val interactor: NoteInteractor) : ViewModel() {

    private var note = MutableLiveData<Note>()
    var noteTitle = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()
    private val message = SingleEventLiveData<Int>()
    private var isActionDone = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()

    init {
        isLoading.value = true
        noteTitle.value = ""
    }

    fun getCertainNote(id: Int) {
        interactor.getNote(id).observeOn(Schedulers.io()).subscribe({ n ->
            note.postValue(n)
            isLoading.postValue(false)
        }, {
            isLoading.postValue(false)
        }).addTo(compositeDisposable)
    }

    fun deleteNote(id: Int) {
        interactor.deleteNote(id).observeOn(Schedulers.io()).subscribe({
            message.postValue(R.string.note_delete_success)
            isActionDone.postValue(true)
        }, {
            message.postValue(R.string.note_delete_error)
        }).addTo(compositeDisposable)
    }

    fun createNote(title: String) {
        interactor.createNote(title).observeOn(Schedulers.io()).subscribe({
            isLoading.postValue(false)
            message.postValue(R.string.note_created_success)
            isActionDone.postValue(true)
        }, {
            message.postValue(R.string.note_created_error)
        }).addTo(compositeDisposable)
    }

    fun updateNote(id: Int, title: String) {
        interactor.updateNote(id, title).observeOn(Schedulers.io()).subscribe({
            isLoading.postValue(false)
            message.postValue(R.string.note_updated_success)
            isActionDone.postValue(true)
        }, {
            message.postValue(R.string.note_updated_error)
        }).addTo(compositeDisposable)
    }

    fun getNote(): MutableLiveData<Note> = note

    fun getMessage(): SingleEventLiveData<Int> = message

    fun getIsActionDone(): MutableLiveData<Boolean> = isActionDone

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}