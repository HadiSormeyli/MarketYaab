package com.hadisormeyli.marketyaab.ui.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hadisormeyli.marketyaab.data.local.db.dao.AuthTokenDao
import com.hadisormeyli.marketyaab.data.local.db.entities.toAuthToken
import com.hadisormeyli.marketyaab.data.local.db.entities.toAuthTokenEntity
import com.hadisormeyli.marketyaab.domain.models.user.AuthToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SessionManager(
    private val authTokenDao: AuthTokenDao,
) {
    private val sessionScope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableLiveData<SessionContract.State>(SessionContract.State.Loading)
    val state: LiveData<SessionContract.State> get() = _state

    init {
        sessionScope.launch {
            authTokenDao.getAuthToken().collect {
                if (it != null && it.token.isNotEmpty() && it.token.isNotBlank()) {
                    setState(SessionContract.State.Login(it.toAuthToken()))
                } else if (_state.value != SessionContract.State.Logout) {
                    setState(SessionContract.State.Logout)
                }
            }
        }
    }

    private fun onTriggerEvent(event: SessionContract.Event) {
        when (event) {
            is SessionContract.Event.Login -> {
                login(event.authToken)
            }

            is SessionContract.Event.Logout -> {
                logout()
            }
        }
    }

    private fun login(authToken: AuthToken) {
        sessionScope.launch {
            authTokenDao.insert(authToken.toAuthTokenEntity())
        }
    }

    fun setEvent(event: SessionContract.Event) {
        onTriggerEvent(event)
    }

    private fun setState(newState: SessionContract.State) {
        _state.postValue(newState)
    }

    internal fun logout() {
        sessionScope.launch {
            authTokenDao.clearToken()
        }
    }
}