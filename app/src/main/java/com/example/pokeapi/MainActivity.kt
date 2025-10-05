package com.example.pokeapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pokeapi.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isDarkModeEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        isDarkModeEnabled = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .getBoolean(KEY_DARK_MODE, false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Título fijo en la barra decorativa (no es ActionBar)
        binding.decorToolbar.title = "ESPN"

        setupThemeToggle()

        // OBTENER navController de forma segura con NavHostFragment (evita crashes)
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        // Conectar el bottom nav
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun setupThemeToggle() {
        val toggleButton = binding.themeToggleButton

        updateThemeButtonState(toggleButton)
        toggleButton.setOnClickListener {
            isDarkModeEnabled = !isDarkModeEnabled
            persistThemePreference(isDarkModeEnabled)
            updateThemeButtonState(toggleButton)
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun updateThemeButtonState(button: MaterialButton) {
        val textRes = if (isDarkModeEnabled) R.string.light_mode else R.string.dark_mode
        val iconRes = if (isDarkModeEnabled) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
        button.setText(textRes)
        button.setIconResource(iconRes)
    }


    private fun persistThemePreference(isDarkMode: Boolean) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit {
            putBoolean(KEY_DARK_MODE, isDarkMode)
        }
    }

    companion object {
        private const val PREFS_NAME = "theme_prefs"
        private const val KEY_DARK_MODE = "dark_mode"
    }
}