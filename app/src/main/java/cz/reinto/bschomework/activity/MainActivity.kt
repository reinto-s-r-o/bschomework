package cz.reinto.bschomework.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import cz.reinto.bschomework.BR
import cz.reinto.bschomework.R
import cz.reinto.bschomework.adapter.NoteAdapter
import cz.reinto.bschomework.databinding.ActivityMainBinding
import cz.reinto.bschomework.model.Note
import cz.reinto.bschomework.viewmodels.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MainView {

    private val viewModel by viewModel<MainActivityViewModel>()
    private lateinit var adapter: NoteAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = NoteAdapter(this)

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).let {
            binding = it
            binding.apply {
                setVariable(BR.view, this@MainActivity)
                setVariable(BR.viewmodel, viewModel)
                lifecycleOwner = this@MainActivity
            }
            binding.root
        }

        binding.mainRecycleview.adapter = adapter

        viewModel.getNotesFromRepository()

        viewModel.getNotes().observe(this, Observer { notes ->
            adapter.submitList(notes)
        })

        viewModel.getMessage().observeOnce(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onNoteClick(note: Note) {
        Intent(this, NoteDetailActivity::class.java).apply {
            putExtra(NoteDetailActivity.NOTE_ID, note.id)
            startActivity(this)
        }
    }

    override fun onCreateNewNote() {
        Intent(this, NoteDetailActivity::class.java).apply {
            startActivity(this)
        }
    }
}
