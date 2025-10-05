package com.example.pokeapi.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pokeapi.databinding.FragmentTeamBinding

class TeamFragment : Fragment() {

    private var _b: FragmentTeamBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentTeamBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val name = requireArguments().getString("teamName").orEmpty()
        val logo = requireArguments().getString("teamLogoUrl")

        b.tvName.text = name
        if (!logo.isNullOrBlank()) {
            Glide.with(this).load(logo).into(b.ivLogo)
        }
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}
