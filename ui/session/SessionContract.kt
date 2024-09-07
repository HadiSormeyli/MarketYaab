package com.hadisormeyli.marketyaab.ui.session

import com.hadisormeyli.marketyaab.domain.models.user.AuthToken

class SessionContract {

    sealed class Event {
        data class Login(val authToken: AuthToken) : Event()
        data object Logout : Event()
    }

    sealed class State {
        data class Login(val authToken: AuthToken) : State()
        data object Logout : State()
        data object Loading : State()
    }
}