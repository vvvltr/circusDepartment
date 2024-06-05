package com.taekwondo.featureauth.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaleksandra.corecommon.ext.EventChannel
import com.kaleksandra.coredata.network.doOnError
import com.kaleksandra.coredata.network.doOnSuccess
import com.taekwondo.featureauth.domain.AuthInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor,
) : ViewModel() {
    sealed class State
    object NavigateMainState : State()
    object ErrorState : State()

    val event = EventChannel<State>()

    /**
     * Авторизует пользователя.
     * @param email почта пользователя.
     * @param password пароль пользователя.
     * При успешной авторизации отправляет событие [NavigateMainState], которое перенаправляет на главный экран.
     * При ошибке отправляет событие [ErrorState], которое выводит ошибку.
     */
    fun onAuth(email: String, password: String) {
        viewModelScope.launch {
            interactor
                .auth(email, password)
                .doOnSuccess {
                    event.send(NavigateMainState)
                }
                .doOnError {
                    event.send(ErrorState)
                }
        }
    }
}