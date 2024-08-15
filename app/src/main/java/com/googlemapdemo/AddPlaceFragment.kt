package com.googlemapdemo

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.googlemapdemo.databinding.FragmentAddPlaceBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.text.SimpleDateFormat
import java.util.Locale

class AddPlaceFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentAddPlaceBinding
    private var calendar = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    companion object {
        private const val GALLERY_REQUEST_CODE = 200
        private const val CAMERA_REQUEST_CODE = 300
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlaceBinding.inflate(inflater, container, false)

        dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        binding.etDate.setOnClickListener(this)
        binding.tvAddImage.setOnClickListener(this)

        return binding.root
    }


    // Handle toolbar navigation click
    @Suppress("DEPRECATION")
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


    // Handle button clicks
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

            R.id.tv_add_image -> {
                val pictureAlertDialog = AlertDialog.Builder(requireContext())
                pictureAlertDialog.setTitle("Select Action")
                val pictureDialogItems =
                    arrayOf("Select photo from gallery", "Capture photo from camera")

                pictureAlertDialog.setItems(pictureDialogItems) { _, which ->
                    when (which) {
                        0 -> {
                            // Handle gallery selection
                            choosePhotoFromGallery()
                        }

                        1 -> {
                            // Handle camera selection
                            photoCaptureFromCamera()
                        }
                    }
                }
                pictureAlertDialog.show()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun photoCaptureFromCamera() {
        // Implement the logic to capture a photo from the camera
        Dexter.withContext(requireContext()).withPermissions(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA,
        ).withListener(object : MultiplePermissionsListener {
            // Handle permission granted
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    // Handle the camera intent
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "all permission are not granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // Handle permission denial
            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>,
                token: PermissionToken
            ) {
                showRationalDialogForPermission()
            }
        }).onSameThread().check()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun choosePhotoFromGallery() {
        // Implement the logic to choose a photo from the gallery
        Dexter.withContext(requireContext()).withPermissions(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA,
        ).withListener(object : MultiplePermissionsListener {
            // Handle permission granted
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    // Handle the selected photo
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "all permission are not granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // Handle permission denial
            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>,
                token: PermissionToken
            ) {
                showRationalDialogForPermission()
            }
        }).onSameThread().check()
    }

    private fun showRationalDialogForPermission() {
        AlertDialog.Builder(requireContext())
            .setMessage(
                "It's looks you turned off permission required from Set Image," +
                        " It can be enabled under Application Settings"
            )
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                // Open the settings screen
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            // Handle the selected photo
            if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, contentURI)
                        binding.ivPlaceHolder.setImageBitmap(selectedImageBitmap)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Failed to load image from gallery", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // Handle the captured photo
            else if (requestCode == CAMERA_REQUEST_CODE) {
                val thumbnail: Bitmap? = data!!.extras!!.get("data") as Bitmap?
                binding.ivPlaceHolder.setImageBitmap(thumbnail)
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etDate.setText(sdf.format(calendar.time).toString())
    }

}


