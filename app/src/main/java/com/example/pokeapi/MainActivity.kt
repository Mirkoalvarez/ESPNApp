package com.example.pokeapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.pokeapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isDarkModeEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // Aplicar tema guardado ANTES de inflar vistas
        isDarkModeEnabled = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .getBoolean(KEY_DARK_MODE, false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Si querés usarla como ActionBar
        setSupportActionBar(binding.decorToolbar)
        binding.decorToolbar.title = "ESPN"

        // NavController
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        // Vincula tabs con Navigation Component
        binding.bottomNavigation.setupWithNavController(navController)

        // Intercepta el ítem "action_toggle_theme" para NO navegar y alternar el tema
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_toggle_theme -> {
                    // Alternar y persistir
                    isDarkModeEnabled = !isDarkModeEnabled
                    persistThemePreference(isDarkModeEnabled)
                    AppCompatDelegate.setDefaultNightMode(
                        if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES
                        else AppCompatDelegate.MODE_NIGHT_NO
                    )
                    // Devolver false evita que el BottomNavigation lo marque como seleccionado
                    false
                }
                else -> {
                    // Para los demás items, dejar que Navigation navegue
                    NavigationUI.onNavDestinationSelected(item, navController)
                }
            }
        }

        // Opcional: no hacer nada en reselección (evita recargar el fragment actual)
        binding.bottomNavigation.setOnItemReselectedListener { /* no-op */ }
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
