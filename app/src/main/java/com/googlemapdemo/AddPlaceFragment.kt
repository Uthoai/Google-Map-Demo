package com.googlemapdemo

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.googlemapdemo.databinding.FragmentAddPlaceBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AddPlaceFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentAddPlaceBinding
    private var calendar = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPlaceBinding.inflate(inflater, container, false)

        dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day_of_month ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day_of_month)
                updateDateInView()
            }

        binding.etDate.setOnClickListener(this)

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

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.et_date -> {
                DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()

            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etDate.setText(sdf.format(calendar.time).toString())
    }

}