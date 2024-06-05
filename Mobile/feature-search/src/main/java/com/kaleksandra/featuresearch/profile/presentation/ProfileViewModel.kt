package com.kaleksandra.featuresearch.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaleksandra.corecommon.ext.EventChannel
import com.kaleksandra.coredata.network.doOnSuccess
import com.kaleksandra.coredata.network.model.Profile
import com.kaleksandra.featuresearch.profile.domain.ProfileInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: ProfileInteractor
) : ViewModel() {
    val profile: MutableStateFlow<Profile?> = MutableStateFlow(null)
    val skills: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    sealed class State
    object NavigateAuthState : State()
    object NavigateEditProfileState : State()

    val event = EventChannel<State>()

    init {
        viewModelScope.launch {
            interactor.getProfile().doOnSuccess {
                profile.emit(it)
            }
            interactor.getSkills().distinctUntilChanged()
                .onEach { skills.emit(it) }
                .launchIn(viewModelScope)
        }
    }

    fun onChangeSkills(skill: String) {
        viewModelScope.launch {
            interactor.updateSkills(
                mutableListOf<String>().apply {
                    addAll(skills.value)
                    add(skill)
                }
            ).doOnSuccess {
                event.send(NavigateEditProfileState)
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            interactor.logOut().doOnSuccess {
                event.send(NavigateAuthState)
            }
        }
    }
}