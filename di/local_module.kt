package com.hadisormeyli.marketyaab.di

import android.app.Application
import androidx.room.Room
import com.hadisormeyli.marketyaab.constant.Constants.DATABASE_NAME
import com.hadisormeyli.marketyaab.data.local.db.AppDatabase
import org.koin.dsl.module


val localModule = module {
//    single<AppDataStore> { AppDataStoreManager(get()) }
    single { createAppDatabase(get()) }
    single { get<AppDatabase>().authDao() }
}

fun createAppDatabase(app: Application) = Room
    .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
    .fallbackToDestructiveMigration()
    .build()

