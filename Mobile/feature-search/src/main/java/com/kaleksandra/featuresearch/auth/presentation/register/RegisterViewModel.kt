package com.taekwondo.featureauth.presentation.register

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
class RegisterViewModel @Inject constructor(
    private val interactor: AuthInteractor,
) : ViewModel() {

    sealed class State
    object NavigateMainState : State()
    class ErrorState(val message: String) : State()

    val event = EventChannel<State>()

    /**
     * Регистрирует пользователя.
     * @param name имя пользователя.
     * @param username логин пользователя.
     * @param email почта пользователя.
     * @param password пароль пользователя.
     * @param phone телефон пользователя.
     * @param photo фото пользователя.
     * При успешной регистрации отправляет событие [NavigateMainState], которое перенаправляет на главный экран.
     * При ошибке отправляет событие [ErrorState], которое выводит ошибку.
     */
    fun onRegister(
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            if (email == "" || password == "") {
                event.send(ErrorState(message = "Заполните все поля"))
            } else {
                interactor.register(email, password)
                    .doOnSuccess {
                        event.send(NavigateMainState)
                    }
                    .doOnError {
                        event.send(ErrorState(message = "Ошибка регистрации"))
                    }
            }
        }
    }
}