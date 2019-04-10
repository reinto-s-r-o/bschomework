package cz.reinto.bschomework.koin

import cz.reinto.bschomework.Config
import cz.reinto.bschomework.api.ApiManager
import cz.reinto.bschomework.api.ApiService
import cz.reinto.bschomework.interactors.NoteInteractor
import cz.reinto.bschomework.viewmodels.MainActivityViewModel
import cz.reinto.bschomework.viewmodels.NoteDetailActivityViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC } }
    single { createOkHttpClient(get()) }
    single { createWebService<ApiService>(get(), Config.SERVER_URL) }
    single { ApiManager(get()) }
    single { NoteInteractor(get()) }

    viewModel { NoteDetailActivityViewModel(get()) }
    viewModel { MainActivityViewModel(get()) }
}

fun createOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
            .connectTimeout(Config.CONNECT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Config.READ_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}