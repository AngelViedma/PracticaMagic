package com.example.practicamagic.uiClientes.ajustesCliente

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practicamagic.R
import com.example.practicamagic.databinding.FragmentAjustedClienteBinding

class AjustedClienteFragment : Fragment() {


    lateinit var binding: FragmentAjustedClienteBinding
    lateinit var viewModel: AjustedClienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAjustedClienteBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this)[AjustedClienteViewModel::class.java]

        initObservers()

        return binding.root
    }

    private fun initObservers() {
        viewModel.text.observe(viewLifecycleOwner) {
        }
    }

    companion object {
        fun newInstance() = AjustedClienteFragment()
    }
}