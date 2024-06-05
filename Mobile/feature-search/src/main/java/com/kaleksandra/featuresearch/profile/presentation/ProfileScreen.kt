package com.kaleksandra.featuresearch.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kaleksandra.corecommon.ext.observe
import com.kaleksandra.coredata.network.model.Profile
import com.kaleksandra.corenavigation.AuthDirection
import com.kaleksandra.coretheme.Dimen
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val profile by profileViewModel.profile.collectAsState()
    val event = profileViewModel.event.receiveAsFlow()
    val skills by profileViewModel.skills.collectAsState()
    var isEdit by remember { mutableStateOf(false) }
    event.observe {
        when (it) {
            ProfileViewModel.NavigateAuthState -> {
                navController.navigate(AuthDirection.path) {
                    popUpTo(AuthDirection.path) {
                        inclusive = true
                    }
                }
            }

            else -> {}
        }
    }
    ProfileScreen(
        profile,
        skills,
        isEdit,
        profileViewModel::logOut,
        {
            isEdit = !isEdit
        },
        {
            profileViewModel.onChangeSkills(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: Profile?,
    skills: List<String>,
    isEdit: Boolean,
    logOut: () -> Unit = {},
    onEdit: () -> Unit = {},
    onChangeSkillsClick: (String) -> Unit = {}
) {
    var skillText by remember { mutableStateOf("") }
    profile?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = logOut, modifier = Modifier.align(Alignment.TopEnd)) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Выйти")
                }
            }
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Аккаунт",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = profile.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = Dimen.padding_20)
            )
            if (isEdit) {
                OutlinedTextField(
                    value = skillText,
                    onValueChange = { skillText = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(onSend = {
                        onChangeSkillsClick(skillText)
                        skillText = ""
                    }),
                    shape = RoundedCornerShape(Dimen.radius_16),
                    modifier = Modifier.padding(top = Dimen.padding_20)
                )
            }
            Button(
                onClick = {
                    onEdit()
                    skillText = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimen.padding_16)
            ) {
                Text(if (isEdit) "Сохранить" else "Изменить скиллы")
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimen.padding_16),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(skills) {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}