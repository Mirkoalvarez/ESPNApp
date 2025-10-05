package com.example.pokeapi.ui.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class SportItem(val title: String, val key: String)

class MenuViewModel : ViewModel() {

    // Lista estática (si mañana querés cargarla desde red/BD, solo la cambiás acá)
    private val baseSports = listOf(
        SportItem("Fútbol (general)", "soccer"),
        SportItem("Tenis", "tennis"),
        SportItem("Básquet", "basketball"),
        SportItem("Béisbol", "baseball"),
        SportItem("Fórmula 1", "f1"),
        SportItem("MMA", "mma"),
        SportItem("NFL (Fútbol americano)", "football"),
        SportItem("Hockey", "hockey")
    )

    private val _sports = MutableLiveData<List<SportItem>>(baseSports)
    val sports: LiveData<List<SportItem>> = _sports

    /** Opcional: pequeño filtro local si lo necesitás más adelante */
    fun filter(query: String?) {
        val q = query?.trim()?.lowercase().orEmpty()
        if (q.isEmpty()) {
            _sports.value = baseSports
        } else {
            _sports.value = baseSports.filter { it.title.lowercase().contains(q) }
        }
    }
}
