package com.example.practicamagic.uiClientes.pedidosCliente

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practicamagic.R
import com.example.practicamagic.databinding.FragmentPedidosClienteBinding

class PedidosClienteFragment : Fragment() {


    lateinit var binding: FragmentPedidosClienteBinding
    lateinit var viewModel: PedidosClienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPedidosClienteBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this)[PedidosClienteViewModel::class.java]

        initObservers()

        return binding.root
    }

    private fun initObservers() {
            viewModel.text.observe(viewLifecycleOwner) {
            }
        }


    companion object {
        fun newInstance() = PedidosClienteFragment()
    }

}