package com.hadisormeyli.marketyaab.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hadisormeyli.marketyaab.data.local.db.dao.AuthTokenDao
import com.hadisormeyli.marketyaab.data.local.db.entities.AuthTokenEntity

@Database(
    entities = [AuthTokenEntity::class],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authDao(): AuthTokenDao
}