package com.example.practicamagic.uiAdmin.ajustes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.practicamagic.databinding.FragmentAjustesAdminBinding


class AjustesAdminFragment : Fragment() {

    //variables lateinit de vista y viewmodel
    lateinit var binding: FragmentAjustesAdminBinding
    lateinit var viewModel: AjustesAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inicializacion de vista y viewmodel
        binding = FragmentAjustesAdminBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(this)[AjustesAdminViewModel::class.java]

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