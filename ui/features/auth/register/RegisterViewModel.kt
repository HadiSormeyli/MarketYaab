package com.hadisormeyli.marketyaab.ui.features.auth.register

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.constant.Constants
import com.hadisormeyli.marketyaab.domain.models.store.StoreWorkTime
import com.hadisormeyli.marketyaab.domain.models.user.UserRole
import com.hadisormeyli.marketyaab.domain.usecases.auth.CheckPhoneNumberUseCase
import com.hadisormeyli.marketyaab.domain.usecases.auth.RegisterCustomerUseCase
import com.hadisormeyli.marketyaab.domain.usecases.auth.RegisterStoreUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidateFirstNameUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidateLastNameUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidatePasswordUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidatePhoneNumberUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidateStoreNameUseCase
import com.hadisormeyli.marketyaab.domain.utils.Resource
import com.hadisormeyli.marketyaab.ui.base.BaseApplicationViewModel
import com.hadisormeyli.marketyaab.ui.features.common.UiText
import com.hadisormeyli.marketyaab.ui.features.common.Utils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterViewModel(
    application: Application,
    private val checkPhoneNumber: CheckPhoneNumberUseCase,
    private val registerStoreUseCase: RegisterStoreUseCase,
    private val registerCustomerUseCase: RegisterCustomerUseCase,
    private val phoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val storeNameUseCase: ValidateStoreNameUseCase,
    private val firstNameUseCase: ValidateFirstNameUseCase,
    private val lastNameUseCase: ValidateLastNameUseCase,
    private val passwordUseCase: ValidatePasswordUseCase,
) : BaseApplicationViewModel<RegisterContract.Event, RegisterContract.State, RegisterContract.SideEffect>(
    application
) {
    override fun setInitialState(): RegisterContract.State = RegisterContract.State()

    override fun onTriggerEvent(event: RegisterContract.Event) {
        when (event) {
            is RegisterContract.Event.SelectUserRole -> {
                setState { RegisterContract.State(userRole = event.userRole) }
                setEffect { RegisterContract.SideEffect.Navigation.ToPhoneNumber }
            }

            is RegisterContract.Event.PhoneNumberChanged -> changePhoneNumber(event.phoneNumber)
            is RegisterContract.Event.FirstNameChanged -> changeFirstName(event.firstName)
            is RegisterContract.Event.LastNameChanged -> changeLastName(event.lastName)
            is RegisterContract.Event.PasswordChanged -> changePassword(event.password)
            is RegisterContract.Event.LocationChanged -> setState { copy(location = event.location) }
            is RegisterContract.Event.StoreProfileChanged -> changeProfileImage(event.profileImage)
            is RegisterContract.Event.CameraPermissionDenied -> setEffect {
                RegisterContract.SideEffect.ShowMessage(
                    UiText.StringResource(
                        R.string.camera_permission_denied
                    )
                )
            }

            is RegisterContract.Event.ConfirmPhoneNumber -> checkPhoneNumber()
            is RegisterContract.Event.ConfirmCode -> setEffect { RegisterContract.SideEffect.Navigation.ToPersonalInfo }
            is RegisterContract.Event.ConfirmPersonalInfo -> {
                if (state.value.userRole == UserRole.CUSTOMER) {
                    registerCustomer()
                } else setEffect { RegisterContract.SideEffect.Navigation.ToStoreInfo }
            }

            is RegisterContract.Event.ConfirmStoreInfo -> registerStore()
            is RegisterContract.Event.Back -> setEffect { RegisterContract.SideEffect.Navigation.Back }

            RegisterContract.Event.ToLocation -> setEffect {
                RegisterContract.SideEffect.Navigation.ToLocation(
                    state.value.location.latitude,
                    state.value.location.longitude,
                )
            }

            is RegisterContract.Event.StoreNameChanged -> setState {
                copy(
                    store = store.copy(
                        storeName = event.storeName,
                        storeNameValidation = storeNameUseCase(event.storeName)
                    )
                )
            }

            RegisterContract.Event.ToStoreWorkTime -> setEffect { RegisterContract.SideEffect.Navigation.ToStoreWorkTime }
            is RegisterContract.Event.UpdateWorkTime -> changeWorkTime(
                event.workTime,
                event.isForAllDay
            )

            is RegisterContract.Event.ShowBottomSheet -> setState {
                copy(
                    store = store.copy(
                        showWorkTimeBottomSheet = true,
                        selectedWorkTime = event.workTime
                    )
                )
            }

            RegisterContract.Event.HideBottomSheet -> setState {
                copy(
                    store = store.copy(
                        showWorkTimeBottomSheet = false,
                    )
                )
            }
        }
    }

    private fun registerCustomer() {
        viewModelScope.launch {
            val phoneNumber = state.value.user.phoneNumber
            val firstName = state.value.user.firstName
            val lastName = state.value.user.lastName
            val password = state.value.user.password
            val latitude = state.value.location.latitude
            val longitude = state.value.location.longitude

            registerCustomerUseCase(
                phoneNumber,
                password,
                firstName,
                lastName,
                latitude,
                longitude
            ).collect {
                when (it) {
                    is Resource.Error -> {
                        setState { copy(isLoading = false) }
                        setEffect { RegisterContract.SideEffect.ShowMessage(it.error.message) }
                    }

                    is Resource.Loading -> {
                        setState { copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                    }
                }
            }
        }
    }

    private fun checkPhoneNumber() {
        viewModelScope.launch {
            checkPhoneNumber(state.value.user.phoneNumber).collectLatest {
                when (it) {
                    is Resource.Error -> {
                        setState { copy(isLoading = false) }
                        setEffect { RegisterContract.SideEffect.ShowMessage(it.error.message) }
                    }

                    is Resource.Loading -> {
                        setState { copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        setEffect { RegisterContract.SideEffect.Navigation.ToPersonalInfo }
                        setState { copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private fun changeWorkTime(workTime: StoreWorkTime, isForAllDay: Boolean) {
        if (workTime.isHoliday || Utils.validateShiftTimes(workTime.shiftTimes)) {
            if (isForAllDay) {
                setState {
                    copy(
                        store = store.copy(
                            workTimes = store.workTimes.map { workTime.copy(dayOfWeek = it.dayOfWeek) },
                            showWorkTimeBottomSheet = false
                        )
                    )
                }
            } else {
                if (workTime.isHoliday) {
                    setState {
                        copy(
                            store = store.copy(
                                workTimes = store.workTimes.map {
                                    if (it.dayOfWeek == workTime.dayOfWeek) workTime.copy(
                                        dayOfWeek = it.dayOfWeek,
                                        shiftTimes = emptyList(),
                                        isHoliday = true
                                    ) else it
                                },
                                showWorkTimeBottomSheet = false
                            )
                        )
                    }
                } else
                    setState {
                        copy(
                            store = store.copy(
                                workTimes = store.workTimes.map {
                                    if (it.dayOfWeek == workTime.dayOfWeek) workTime.copy(
                                        dayOfWeek = it.dayOfWeek
                                    ) else it
                                },
                                showWorkTimeBottomSheet = false
                            )
                        )
                    }
            }
        } else setEffect { RegisterContract.SideEffect.ShowMessage(UiText.StringResource(R.string.shift_times_error)) }
    }

    private fun changeProfileImage(profileImage: Uri?) {
        setState {
            copy(
                store = store.copy(
                    profileImage = profileImage
                )
            )
        }
    }

    private fun changePhoneNumber(phoneNumber: String) {
        setState {
            copy(
                user = user.copy(
                    phoneNumber = phoneNumber,
                    phoneNumberValidation = phoneNumberUseCase(phoneNumber)
                )
            )
        }
    }

    private fun changeFirstName(firstName: String) {
        setState {
            copy(
                user = user.copy(
                    firstName = firstName,
                    firstNameValidation = firstNameUseCase(firstName)
                )
            )
        }
    }

    private fun changeLastName(lastName: String) {
        setState {
            copy(
                user = user.copy(
                    lastName = lastName,
                    lastNameValidation = lastNameUseCase(lastName)
                )
            )
        }
    }

    private fun changePassword(password: String) {
        setState {
            copy(
                user = user.copy(
                    password = password,
                    passwordValidation = passwordUseCase(password)
                )
            )
        }
    }

    private fun registerStore() {
        viewModelScope.launch {
            val phoneNumber = state.value.user.phoneNumber
            val firstName = state.value.user.firstName
            val lastName = state.value.user.lastName
            val password = state.value.user.password
            val latitude = state.value.location.latitude
            val longitude = state.value.location.longitude
            val storeName = state.value.store.storeName
            val profileImage = state.value.store.profileImage
            val workTimes = state.value.store.workTimes
            Log.d("TAG", "registerStore: ${profileImage}")
            if (state.value.userRole == UserRole.STORE && profileImage == null) {
                setEffect { RegisterContract.SideEffect.ShowMessage(UiText.StringResource(R.string.set_image)) }
            } else {
                registerStoreUseCase(
                    phoneNumber,
                    password,
                    firstName,
                    lastName,
                    latitude,
                    longitude,
                    storeName,
                    workTimes,
                    Utils.createMultipartBodyPart(
                        getApplication(),
                        profileImage,
                        Constants.STORE_PROFILE_PART
                    )
                ).collect {
                    when (it) {
                        is Resource.Loading -> setState { copy(isLoading = true) }
                        is Resource.Success -> {}

                        is Resource.Error -> {
                            setEffect { RegisterContract.SideEffect.ShowMessage(it.error.message) }
                            setState { copy(isLoading = false) }
                        }
                    }
                }
            }
        }
    }
}