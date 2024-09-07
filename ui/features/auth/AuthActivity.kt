package com.hadisormeyli.marketyab.ui.features.auth

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.hadisormeyli.marketyab.di.auth_module
import com.hadisormeyli.marketyab.di.customer_module
import com.hadisormeyli.marketyab.di.store_module
import com.hadisormeyli.marketyab.domain.models.user.UserRole
import com.hadisormeyli.marketyab.ui.base.BaseActivity
import com.hadisormeyli.marketyab.ui.features.auth.login.LoginScreenRoute
import com.hadisormeyli.marketyab.ui.features.auth.register.ConfirmPhoneNumberRoute
import com.hadisormeyli.marketyab.ui.features.auth.register.PersonalInfoRoute
import com.hadisormeyli.marketyab.ui.features.auth.register.PhoneNumberScreenRoute
import com.hadisormeyli.marketyab.ui.features.auth.register.StoreInfoRoute
import com.hadisormeyli.marketyab.ui.features.auth.register.StoreWorkTimeScreenRoute
import com.hadisormeyli.marketyab.ui.features.auth.register.UserRoleScreenRoute
import com.hadisormeyli.marketyab.ui.features.common.components.CustomSnackBar
import com.hadisormeyli.marketyab.ui.features.common.toActivity
import com.hadisormeyli.marketyab.ui.navigation.Screen
import com.hadisormeyli.marketyab.ui.navigation.locationGraph
import com.hadisormeyli.marketyab.ui.session.SessionContract
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class AuthActivity : BaseActivity() {

    @Composable
    override fun CreateView() {
        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
            snackbarHost = {
                SnackbarHost(snackBarHostState, snackbar = { snackBarData ->
                    CustomSnackBar(snackBarData = snackBarData)
                })
            },
        ) {
            NavHost(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                navController = navController,
                route = Screen.Unauthenticated.Route.route,
                startDestination = Screen.Unauthenticated.Login.route
            ) {
                composable(
                    route = Screen.Unauthenticated.Login.route
                ) {
                    LoginScreenRoute(navController = navController)
                }

                navigation(
                    route = Screen.Unauthenticated.RegisterRoute.route,
                    startDestination = Screen.Unauthenticated.UserRole.route
                ) {
                    composable(
                        route = Screen.Unauthenticated.UserRole.route
                    ) {
                        UserRoleScreenRoute(navController = navController)
                    }

                    composable(
                        route = Screen.Unauthenticated.PhoneNumber.route
                    ) {
                        PhoneNumberScreenRoute(navController = navController)
                    }

                    composable(
                        route = Screen.Unauthenticated.Code.route
                    ) {
                        ConfirmPhoneNumberRoute(navController = navController)
                    }

                    composable(
                        route = Screen.Unauthenticated.PersonalInfo.route
                    ) {
                        PersonalInfoRoute(navController = navController)
                    }

                    composable(
                        route = Screen.Unauthenticated.StoreInfo.route
                    ) {
                        StoreInfoRoute(navController = navController)
                    }

                    composable(
                        route = Screen.Unauthenticated.StoreWorkTime.route
                    ) {
                        StoreWorkTimeScreenRoute(navController = navController)
                    }

                    locationGraph(navController)
                }
            }
        }
    }

    override fun loadModules() {
        if (scopes.isAuthClosed) {
            loadKoinModules(auth_module)
            scopes.isAuthClosed = false
        }

        if (!scopes.isStoreClosed) {
            unloadKoinModules(store_module)
            scopes.isStoreClosed = true
        }

        if (!scopes.isCustomerClosed) {
            unloadKoinModules(customer_module)
            scopes.isCustomerClosed = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager.state.observe(this) {
            if (it is SessionContract.State.Login) {
                if (it.authToken.userRole == UserRole.CUSTOMER)
                    this.toActivity<com.hadisormeyli.marketyab.ui.features.customer.CustomerActivity>()
                else if (it.authToken.userRole == UserRole.STORE)
                    this.toActivity<com.hadisormeyli.marketyab.ui.features.store.StoreActivity>()
            }
        }
    }
}