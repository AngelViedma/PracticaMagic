package com.example.practicamagic.uiClientes.eventosCliente

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practicamagic.R
import com.example.practicamagic.databinding.FragmentEventosClienteBinding

class EventosClienteFragment : Fragment() {


    lateinit var binding: FragmentEventosClienteBinding
    lateinit var viewModel: EventosClienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventosClienteBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this)[EventosClienteViewModel::class.java]

        initObservers()

        return binding.root
    }

    private fun initObservers() {
            viewModel.text.observe(viewLifecycleOwner) {
            }
        }

    companion object {
        fun newInstance() = EventosClienteFragment()
    }
}