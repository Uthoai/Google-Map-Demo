package com.googlemapdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.googlemapdemo.databinding.FragmentAddPlaceBinding

class AddPlaceFragment : Fragment() {
    private lateinit var binding: FragmentAddPlaceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPlaceBinding.inflate(inflater, container, false)

        setListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the toolbar
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_add_place)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Optionally set up the back button
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        // Handle the toolbar's navigation click (back button)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun setListener() {
        //
    }
}