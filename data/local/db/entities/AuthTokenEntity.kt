package com.hadisormeyli.marketyaab.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hadisormeyli.marketyab.domain.models.user.AuthToken
import com.hadisormeyli.marketyab.domain.models.user.UserRole

@Entity
data class AuthTokenEntity(
    @PrimaryKey
    val id: Int = 0,
    val token: String,
    val userRole: Int
)

fun AuthTokenEntity.toAuthToken(): AuthToken {
    return AuthToken(token, UserRole.fromInt(userRole))
}

fun AuthToken.toAuthTokenEntity() = AuthTokenEntity(token = token, userRole = userRole.role)