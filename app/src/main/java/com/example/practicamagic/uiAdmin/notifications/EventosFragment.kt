package com.example.practicamagic.uiAdmin.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.practicamagic.databinding.FragmentEventosAdminBinding
import com.example.practicamagic.uiAdmin.ventas.VentasAdminFragment

class EventosFragment : Fragment() {

    lateinit var binding: FragmentEventosAdminBinding
    lateinit var viewModel: EventosViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventosAdminBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this)[EventosViewModel::class.java]

        initObservers()

        return binding.root
    }

    private fun initObservers() {
        viewModel.text.observe(viewLifecycleOwner) {
        }
    }
    companion object {
        fun newInstance() = VentasAdminFragment()
    }
}