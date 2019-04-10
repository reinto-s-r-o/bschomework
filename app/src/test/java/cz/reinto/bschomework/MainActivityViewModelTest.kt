package cz.reinto.bschomework

import androidx.annotation.NonNull
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import cz.reinto.bschomework.api.ApiManager
import cz.reinto.bschomework.api.ApiService
import cz.reinto.bschomework.interactors.NoteInteractor
import cz.reinto.bschomework.model.Note
import cz.reinto.bschomework.viewmodels.MainActivityViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.disposables.Disposable
import io.reactivex.Scheduler
import io.reactivex.Single
import org.mockito.Mockito
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class MainActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    var lifecycleOwner: LifecycleOwner = mock(LifecycleOwner::class.java)
    var lifecycleRegistry = LifecycleRegistry(lifecycleOwner)

    var apiManager = ApiManager(Mockito.mock(ApiService::class.java))
    var noteInteractor = NoteInteractor(apiManager)
    var viewModel = MainActivityViewModel(noteInteractor)

    private val noteList = listOf(Note(1, "Title"))
    private val message = R.string.note_created_success
    private val error = Throwable("error")

    @Before
    fun before() {
        MockitoAnnotations.initMocks(LifecycleOwner::class.java)
        `when`(lifecycleOwner.lifecycle).thenReturn(lifecycleRegistry)

        viewModel.getMessage().postValue(message)

        val immediate = object : Scheduler() {
            override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
    }

    @Test
    fun send_message_live_data_success() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        viewModel.getMessage().observeOnce(lifecycleOwner, Observer {
            Assert.assertEquals(message, it)
        })
    }

    @Test
    fun get_notes_from_api_equal_success() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        `when`(apiManager.getNotes()).thenReturn(Single.just(noteList))

        noteInteractor.getNotes().test().let {
            it.assertNoErrors()
            it.assertValue { list ->
                list == noteList
            }
        }
    }

    @Test
    fun get_notes_from_api_equal_fail() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        `when`(apiManager.getNotes()).thenReturn(Single.error(error))

        noteInteractor.getNotes().test().let {
            it.assertError(error)
        }
    }
}