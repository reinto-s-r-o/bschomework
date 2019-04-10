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

class MainActivityViewModel(private val interactor: NoteInteractor) : ViewModel() {

    private var notes = MutableLiveData<List<Note>>()
    private val message = SingleEventLiveData<Int>()
    var isLoading = MutableLiveData<Boolean>()
    var isEmpty = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()

    init {
        isEmpty.postValue(true)
        isLoading.postValue(true)
    }

    fun getNotesFromRepository() {
        interactor.getNotes().observeOn(Schedulers.io()).subscribe({ noteList ->
            notes.postValue(noteList)
            if (noteList.isNotEmpty()) isEmpty.postValue(false) else isEmpty.postValue(true)
            isLoading.postValue(false)
        }, {
            isLoading.postValue(false)
            message.postValue(R.string.notes_download_error)
        }).addTo(compositeDisposable)
    }

    fun getNotes(): MutableLiveData<List<Note>> = notes

    fun getMessage(): SingleEventLiveData<Int> = message

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}