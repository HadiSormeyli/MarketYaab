package com.hadisormeyli.marketyaab.ui.features.auth.register

import android.net.Uri
import com.hadisormeyli.marketyaab.constant.Constants.CENTER_LOCATION_LAT
import com.hadisormeyli.marketyaab.constant.Constants.CENTER_LOCATION_LONG
import com.hadisormeyli.marketyaab.domain.models.user.UserRole
import com.hadisormeyli.marketyaab.domain.models.ValidationResult
import com.hadisormeyli.marketyaab.domain.models.store.StoreWorkTime
import com.hadisormeyli.marketyaab.domain.utils.Utils
import com.hadisormeyli.marketyaab.ui.base.ViewEvent
import com.hadisormeyli.marketyaab.ui.base.ViewSideEffect
import com.hadisormeyli.marketyaab.ui.base.ViewState
import com.hadisormeyli.marketyaab.ui.features.common.UiText
import org.neshan.common.model.LatLng

class RegisterContract {

    sealed class Event : ViewEvent {
        data class SelectUserRole(val userRole: UserRole) : Event()
        data class UpdateWorkTime(val workTime: StoreWorkTime, val isForAllDay: Boolean) : Event()
        data class ShowBottomSheet(val workTime: StoreWorkTime) : Event()
        data object HideBottomSheet : Event()
        data object ConfirmPhoneNumber : Event()
        data object ConfirmCode : Event()
        data object ConfirmPersonalInfo : Event()
        data object ConfirmStoreInfo : Event()
        data class PhoneNumberChanged(val phoneNumber: String) : Event()
        data class FirstNameChanged(val firstName: String) : Event()
        data class StoreNameChanged(val storeName: String) : Event()
        data class LastNameChanged(val lastName: String) : Event()
        data class PasswordChanged(val password: String) : Event()
        data class LocationChanged(val location: LatLng) : Event()
        data class StoreProfileChanged(val profileImage: Uri?) : Event()
        data object CameraPermissionDenied : Event()
        data object ToLocation : Event()
        data object ToStoreWorkTime : Event()
        data object Back : Event()
    }

    data class State(
        val userRole: UserRole? = null,
        val user: User = User(),
        val store: Store = Store(),
        val location: LatLng = LatLng(CENTER_LOCATION_LAT, CENTER_LOCATION_LONG),
        val isLoading: Boolean = false
    ) : ViewState

    data class User(
        val phoneNumber: String = "",
        val sentCode: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val password: String = "",
        val phoneNumberValidation: ValidationResult = ValidationResult(),
        val sentCodeValidation: ValidationResult = ValidationResult(),
        val firstNameValidation: ValidationResult = ValidationResult(),
        val lastNameValidation: ValidationResult = ValidationResult(),
        val passwordValidation: ValidationResult = ValidationResult(),
    )

    data class Store(
        val showWorkTimeBottomSheet: Boolean = false,
        val selectedWorkTime: StoreWorkTime? = null,
        val profileImage: Uri? = null,
        val storeName: String = "",
        val storeNameValidation: ValidationResult = ValidationResult(),
        val workTimes: List<StoreWorkTime> = Utils.storeWorkTimeDefaults(),
    )

    sealed class SideEffect : ViewSideEffect {
        sealed class Navigation : SideEffect() {
            data object ToPhoneNumber : Navigation()
            data object ToCode : Navigation()
            data object ToPersonalInfo : Navigation()
            data object ToStoreInfo : Navigation()
            data class ToLocation(val latitude: Double, val longitude: Double) : Navigation()
            data object Back : Navigation()
            data object ToStoreWorkTime : Navigation()
        }

        data class ShowMessage(val message: UiText) : SideEffect()
    }
}