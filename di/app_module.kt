package com.hadisormeyli.marketyaab.di

import com.google.gson.GsonBuilder
import com.hadisormeyli.marketyaab.domain.usecases.common.SearchLocationUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidateEmailUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidateFirstNameUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidateLastNameUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidatePasswordUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidatePhoneNumberUseCase
import com.hadisormeyli.marketyaab.domain.usecases.common.ValidateStoreNameUseCase
import com.hadisormeyli.marketyaab.ui.features.auth.location.AddLocationViewModel
import com.hadisormeyli.marketyaab.ui.features.common.LocationHelper
import com.hadisormeyli.marketyaab.ui.session.SessionManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ValidatePhoneNumberUseCase() }
    single { ValidateStoreNameUseCase() }
    single { ValidateFirstNameUseCase() }
    single { ValidateLastNameUseCase() }
    single { ValidatePasswordUseCase() }
    single { SearchLocationUseCase(get()) }
    single { ValidateEmailUseCase() }

    viewModel { AddLocationViewModel(get(), getOrNull(), get(), get()) }

    single { SessionManager(get()) }
    single { Scope() }
    single { LocationHelper(get()) }
    single { GsonBuilder().create() }
}

val marketyaabApp = listOf(appModule, localModule, remoteModule)
