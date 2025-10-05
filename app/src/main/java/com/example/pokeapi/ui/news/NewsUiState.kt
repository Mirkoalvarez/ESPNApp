package com.example.pokeapi.ui.news

import com.example.pokeapi.model.espn.Article

sealed class NewsUiState {
    object Idle : NewsUiState()
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Empty(val message: String) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}
