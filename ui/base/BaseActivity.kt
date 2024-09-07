package com.hadisormeyli.marketyaab.ui.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.constant.Constants.EXIT_INTERVAL
import com.hadisormeyli.marketyaab.di.Scope
import com.hadisormeyli.marketyaab.ui.features.common.UiText
import com.hadisormeyli.marketyaab.ui.session.SessionManager
import com.hadisormeyli.marketyaab.ui.theme.ShopAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.java.KoinJavaComponent
import java.util.Locale

abstract class BaseActivity : ComponentActivity() {

    private var doubleBackToExitPressedOnce = false

    protected val sessionManager: SessionManager by KoinJavaComponent.inject(
        SessionManager::class.java
    )

    protected val scopes: Scope by KoinJavaComponent.inject(
        Scope::class.java
    )

    protected lateinit var snackBarHostState: SnackbarHostState

    @Composable
    abstract fun CreateView()

    abstract fun loadModules()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(updateLocale(newBase, Locale("fa")))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadModules()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        snackBarHostState = SnackbarHostState()
        enableEdgeToEdge()
        setContent {
            KoinContext {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    ShopAppTheme {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            CreateView()
                        }
                    }
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        })
    }

    private fun handleBackPress() {
        if (doubleBackToExitPressedOnce) {
            finish()
            return
        }

        this.doubleBackToExitPressedOnce = true
        showSnackBar(UiText.StringResource(R.string.click_back_again_to_exit))
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, EXIT_INTERVAL)
    }

    private fun updateLocale(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    fun showSnackBar(message: UiText, actionLabel: String = "") {
        if (::snackBarHostState.isInitialized) {
            CoroutineScope(Dispatchers.Main).launch {
                snackBarHostState.currentSnackbarData?.dismiss()
                snackBarHostState.showSnackbar(
                    message = message.asString(this@BaseActivity),
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}