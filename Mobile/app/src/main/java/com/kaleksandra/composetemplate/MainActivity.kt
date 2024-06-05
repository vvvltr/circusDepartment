package com.kaleksandra.composetemplate

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kaleksandra.corenavigation.AuthDirection
import com.kaleksandra.corenavigation.MainDirection
import com.kaleksandra.corenavigation.ProfileDirection
import com.kaleksandra.corenavigation.RegisterDirection
import com.kaleksandra.coretheme.AppTheme
import com.kaleksandra.featuresearch.main.presentation.SearchScreen
import com.kaleksandra.featuresearch.profile.presentation.ProfileScreen
import com.taekwondo.featureauth.presentation.auth.AuthScreen
import com.taekwondo.featureauth.presentation.register.RegisterScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme {
                Scaffold {
                    val viewModel: MainViewModel = hiltViewModel()
                    val isAuthorized by viewModel.isAuthorized.collectAsState()
                    isAuthorized?.let {
                        val startDestination = if (it) MainDirection.path else AuthDirection.path
                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable(MainDirection.path) {
                                SearchScreen(navController = navController)
                            }
                            composable(AuthDirection.path) {
                                AuthScreen(navController = navController)
                            }
                            composable(RegisterDirection.path) {
                                RegisterScreen(navController = navController)
                            }
                            composable(ProfileDirection.path) {
                                ProfileScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}