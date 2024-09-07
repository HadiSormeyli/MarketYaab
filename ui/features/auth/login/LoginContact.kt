package com.hadisormeyli.marketyaab.ui.features.auth.login

import com.hadisormeyli.marketyaab.domain.models.user.AuthToken
import com.hadisormeyli.marketyaab.domain.models.ValidationResult
import com.hadisormeyli.marketyaab.ui.base.RequestState
import com.hadisormeyli.marketyaab.ui.base.ViewEvent
import com.hadisormeyli.marketyaab.ui.base.ViewSideEffect
import com.hadisormeyli.marketyaab.ui.base.ViewState
import com.hadisormeyli.marketyaab.ui.features.common.UiText

class LoginContact {
    sealed class Event : ViewEvent {
        data class Login(val phoneNumber: String, val password: String) : Event()
        data class PhoneNumberChanged(val email: String) : Event()
        data class PasswordChanged(val password: String) : Event()
        data object ToRegister : Event()
    }

    data class State(
        val phoneNumber: String = "",
        val password: String = "",
        val phoneNumberValidation: ValidationResult = ValidationResult(isValid = true),
        val passwordValidation: ValidationResult = ValidationResult(isValid = true),
        val loginState: RequestState<AuthToken> = RequestState.Empty()
    ) : ViewState

    sealed class SideEffect : ViewSideEffect {
        sealed class Navigation : SideEffect() {
            data object ToRegister : Navigation()
        }

        data class ShowMessage(val message: UiText) : SideEffect()
    }
}