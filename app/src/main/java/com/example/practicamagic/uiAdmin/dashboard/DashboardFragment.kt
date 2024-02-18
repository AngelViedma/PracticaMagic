package com.example.practicamagic.uiAdmin.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.practicamagic.databinding.FragmentDashboardBinding
import com.example.practicamagic.uiAdmin.ajustes.AjustesAdminFragment

class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding
    lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this)[DashboardViewModel::class.java]

        initObservers()

        return binding.root
    }

    private fun initObservers() {
        viewModel.text.observe(viewLifecycleOwner) {
        }
    }
    companion object {
        fun newInstance() = AjustesAdminFragment()
    }
}