package com.hadisormeyli.marketyaab.ui.features.auth.login

import androidx.lifecycle.viewModelScope
import com.hadisormeyli.marketyaab.domain.usecases.auth.LoginUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidatePasswordUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidatePhoneNumberUseCase
import com.hadisormeyli.marketyaab.domain.utils.Resource
import com.hadisormeyli.marketyaab.ui.base.BaseViewModel
import com.hadisormeyli.marketyaab.ui.base.RequestState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val phoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val passwordUseCase: ValidatePasswordUseCase,
) : BaseViewModel<LoginContact.Event, LoginContact.State, LoginContact.SideEffect>() {
    override fun setInitialState(): LoginContact.State = LoginContact.State()

    override fun onTriggerEvent(event: LoginContact.Event) {
        when (event) {
            is LoginContact.Event.Login -> login(event.phoneNumber, event.password)
            is LoginContact.Event.PhoneNumberChanged -> changePhoneNumber(event.email)
            is LoginContact.Event.PasswordChanged -> changePassword(event.password)
            is LoginContact.Event.ToRegister -> setEffect { LoginContact.SideEffect.Navigation.ToRegister }
        }
    }

    private fun changePhoneNumber(phoneNumber: String) {
        setState {
            copy(
                phoneNumber = phoneNumber,
                phoneNumberValidation = phoneNumberUseCase(phoneNumber)
            )
        }
    }

    private fun changePassword(password: String) {
        val passwordValidation = passwordUseCase(password)
        setState {
            copy(
                password = password,
                passwordValidation = passwordValidation
            )
        }
    }

    private fun login(phoneNumber: String, password: String) {
        viewModelScope.launch {
            loginUseCase(phoneNumber, password).collect {
                when (it) {
                    is Resource.Loading -> setState { copy(loginState = RequestState.Loading()) }
                    is Resource.Success -> {
//                        setState { copy(loginState = RequestState.Success(it.data)) }
                    }

                    is Resource.Error -> {
                        setEffect { LoginContact.SideEffect.ShowMessage(it.error.message) }
                        setState { copy(loginState = RequestState.Empty()) }
                    }
                }
            }
        }
    }
}