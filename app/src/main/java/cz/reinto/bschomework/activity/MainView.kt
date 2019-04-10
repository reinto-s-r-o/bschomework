package cz.reinto.bschomework.activity

import cz.reinto.bschomework.model.Note

interface MainView {

    fun onNoteClick(note: Note)

    fun onCreateNewNote()
}