package com.example.pokeapi.ui.team

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokeapi.network.TeamDetailResponse
import com.example.pokeapi.network.espnApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

sealed class TeamUiState {
    object Idle : TeamUiState()
    object Loading : TeamUiState()
    data class Success(val players: List<RosterPlayer>) : TeamUiState()
    data class Error(val message: String) : TeamUiState()
}

data class RosterPlayer(
    val name: String,
    val jersey: String?,
    val position: String?,
    val group: String?
)

class TeamViewModel : ViewModel() {

    private val _state = MutableLiveData<TeamUiState>(TeamUiState.Idle)
    val state: LiveData<TeamUiState> = _state

    private var currentCall: Call<TeamDetailResponse>? = null

    fun loadTeam(league: String?, teamId: String?) {
        if (league.isNullOrBlank() || teamId.isNullOrBlank()) {
            _state.value = TeamUiState.Error("Información del equipo incompleta")
            return
        }

        _state.value = TeamUiState.Loading
        currentCall?.cancel()

        val call = espnApi.getTeamDetails(league, teamId)
        currentCall = call
        call.enqueue(object : Callback<TeamDetailResponse> {
            override fun onResponse(
                call: Call<TeamDetailResponse>,
                response: Response<TeamDetailResponse>
            ) {
                if (!response.isSuccessful) {
                    _state.value = TeamUiState.Error("No se pudo cargar el plantel")
                    return
                }

                val players = response.body()
                    ?.team
                    ?.athletes
                    ?.let { athletes ->
                        val groups = when {
                            !athletes.groups.isNullOrEmpty() -> athletes.groups
                            !athletes.legacyGroups.isNullOrEmpty() -> athletes.legacyGroups
                            else -> null
                        }

                        when {
                            !groups.isNullOrEmpty() -> {
                                groups.flatMap { group ->
                                    val groupName = group.displayName
                                    group.items.orEmpty().map { item ->
                                        val position = item.position?.abbreviation
                                            ?: item.position?.displayName
                                        RosterPlayer(
                                            name = item.fullName
                                                ?: item.displayName
                                                ?: item.shortName
                                                ?: "",
                                            jersey = item.jersey,
                                            position = position,
                                            group = groupName
                                        )
                                    }
                                }
                            }

                            !athletes.items.isNullOrEmpty() -> {
                                athletes.items.map { item ->
                                    val position = item.position?.abbreviation
                                        ?: item.position?.displayName
                                    RosterPlayer(
                                        name = item.fullName
                                            ?: item.displayName
                                            ?: item.shortName
                                            ?: "",
                                        jersey = item.jersey,
                                        position = position,
                                        group = item.position?.displayName
                                    )
                                }
                            }

                            else -> emptyList()
                        }
                    }
                    .orEmpty()
                    .sortedBy { it.name.lowercase() }

                if (players.isEmpty()) {
                    _state.value = TeamUiState.Error("Sin jugadores disponibles")
                } else {
                    _state.value = TeamUiState.Success(players)
                }
            }

            override fun onFailure(call: Call<TeamDetailResponse>, t: Throwable) {
                if (call.isCanceled) return
                _state.value = TeamUiState.Error("Error al cargar el plantel")
            }
        })
    }

    override fun onCleared() {
        currentCall?.cancel()
        currentCall = null
        super.onCleared()
    }
}