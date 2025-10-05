package com.example.pokeapi.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pokeapi.databinding.FragmentTeamBinding

class TeamFragment : Fragment() {

    private var _b: FragmentTeamBinding? = null
    private val b get() = _b!!
    private val viewModel: TeamViewModel by viewModels()
    private val playersAdapter = PlayersAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentTeamBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val name = requireArguments().getString("teamName").orEmpty()
        val logo = requireArguments().getString("teamLogoUrl")
        val league = requireArguments().getString("teamLeague")
        val teamId = requireArguments().getString("teamId")


        b.tvName.text = name
        if (!logo.isNullOrBlank()) {
            Glide.with(this).load(logo).into(b.ivLogo)
        }
        b.rvPlayers.layoutManager = LinearLayoutManager(requireContext())
        b.rvPlayers.adapter = playersAdapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is TeamUiState.Idle -> {
                    b.progressPlayers.visibility = View.GONE
                    b.tvRosterMessage.visibility = View.GONE
                    playersAdapter.submit(emptyList())
                }
                is TeamUiState.Loading -> {
                    b.progressPlayers.visibility = View.VISIBLE
                    b.tvRosterMessage.visibility = View.GONE
                    playersAdapter.submit(emptyList())
                }
                is TeamUiState.Success -> {
                    b.progressPlayers.visibility = View.GONE
                    b.tvRosterMessage.visibility = View.GONE
                    playersAdapter.submit(state.players)
                }
                is TeamUiState.Error -> {
                    b.progressPlayers.visibility = View.GONE
                    b.tvRosterMessage.text = state.message
                    b.tvRosterMessage.visibility = View.VISIBLE
                    playersAdapter.submit(emptyList())
                }
            }
        }

        viewModel.loadTeam(league, teamId)
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}
