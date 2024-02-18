package com.example.practicamagic.uiAdmin.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.practicamagic.databinding.FragmentNotificationsBinding
import com.example.practicamagic.uiAdmin.ajustes.AjustesAdminFragment

class NotificationsFragment : Fragment() {

    lateinit var binding: FragmentNotificationsBinding
    lateinit var viewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this)[NotificationsViewModel::class.java]

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