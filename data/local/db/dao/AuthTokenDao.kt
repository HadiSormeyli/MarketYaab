package com.hadisormeyli.marketyaab.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hadisormeyli.marketyaab.data.local.db.entities.AuthTokenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(authToken: AuthTokenEntity): Long

    @Query("DELETE FROM AuthTokenEntity")
    suspend fun clearToken()

    @Query("SELECT * FROM AuthTokenEntity WHERE id = 0 LIMIT 1")
    fun getAuthToken(): Flow<AuthTokenEntity?>

    @Query("SELECT token FROM AuthTokenEntity WHERE id = 0 LIMIT 1")
    fun getToken(): String?
}