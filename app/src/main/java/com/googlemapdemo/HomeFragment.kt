package com.googlemapdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.googlemapdemo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setListener()

        return binding.root
    }

    private fun setListener() {
        binding.fabAddPlace.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addPlaceFragment)
        }
    }
}