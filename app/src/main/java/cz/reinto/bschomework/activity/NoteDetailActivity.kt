package cz.reinto.bschomework.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import cz.reinto.bschomework.BR
import cz.reinto.bschomework.R
import cz.reinto.bschomework.databinding.ActivityNoteDetailBinding
import cz.reinto.bschomework.viewmodels.NoteDetailActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteDetailActivity : AppCompatActivity(), NoteDetailView {

    private val viewModel by viewModel<NoteDetailActivityViewModel>()
    private lateinit var binding: ActivityNoteDetailBinding
    private var noteId: Int = -1

    companion object {
        const val NOTE_ID = "note"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        DataBindingUtil.setContentView<ActivityNoteDetailBinding>(this, R.layout.activity_note_detail).let {
            binding = it
            binding.apply {
                setVariable(BR.view, this@NoteDetailActivity)
                setVariable(BR.viewmodel, viewModel)
                lifecycleOwner = this@NoteDetailActivity
            }
            binding.root
        }

        noteId = intent?.getIntExtra(NOTE_ID, -1) ?: -1
        if (noteId != -1) {
            supportActionBar?.title = getString(R.string.update_note)
            viewModel.getCertainNote(noteId)
            viewModel.getNote().observe(this, Observer { note ->
                viewModel.noteTitle.postValue(note.title)
            })
        } else {
            supportActionBar?.title = getString(R.string.create_note)
            viewModel.isLoading.value = false
        }

        viewModel.getMessage().observeOnce(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.getIsActionDone().observe(this, Observer {
            finish()
        })
    }

    private fun deleteNote() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.warning))
            .setMessage(getString(R.string.really_want_delete_note))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteNote(noteId)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onEditNote(title: String) {
        if (noteId != -1) {
            viewModel.isLoading.value = true
            viewModel.updateNote(noteId, title)
        } else {
            viewModel.isLoading.value = true
            viewModel.createNote(title)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (noteId == -1) menuInflater.inflate(R.menu.menu_empty, menu)
        else menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.delete -> {
                deleteNote()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}