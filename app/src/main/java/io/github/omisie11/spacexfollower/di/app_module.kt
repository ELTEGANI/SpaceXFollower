package io.github.omisie11.spacexfollower.di

import androidx.room.Room
import io.github.omisie11.spacexfollower.CapsulesViewModel
import io.github.omisie11.spacexfollower.data.SpaceDatabase
import io.github.omisie11.spacexfollower.data.SpaceRepository
import io.github.omisie11.spacexfollower.network.SpaceService
import io.github.omisie11.spacexfollower.util.SPACE_X_BASE_URL
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Main module for Koin DI
val appModule = module {

    // Space database instance
    single {
        Room.databaseBuilder<SpaceDatabase>(
            androidApplication(),
            SpaceDatabase::class.java,
            "space_data.db"
        )
            .build()
    }

    // Capsules DAO instance
    single { get<SpaceDatabase>().capsulesDao() }

    // Single instance of SpaceRepository
    single { SpaceRepository(get(), get()) }

    // ViewModel instance of CapsulesViewModel
    // get() will resolve Repository instance
    viewModel { CapsulesViewModel(get()) }
}

// Module for networking elements
val remoteDataSourceModule = module {

    // Create Retrofit instance
    single { buildRetrofit(SPACE_X_BASE_URL)  }

    // Create retrofit Service
    single { get<Retrofit>().create(SpaceService::class.java) }
}

internal fun buildRetrofit(baseUrl: String) : Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}