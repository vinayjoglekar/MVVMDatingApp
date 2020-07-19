package com.jovinz.datingapp.account.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jovinz.datingapp.R
import com.jovinz.datingapp.common.preferences.UserPrefs
import com.jovinz.datingapp.utils.Constants.DOB
import com.jovinz.datingapp.utils.Constants.GENDER
import com.jovinz.datingapp.utils.Constants.IS_LOGGED_IN
import com.jovinz.datingapp.utils.Constants.PICK_IMAGE_REQUEST_CODE
import com.jovinz.datingapp.utils.Constants.PROFILE
import com.jovinz.datingapp.utils.Constants.READ_EXTERNAL_STORAGE_REQUEST_CODE
import com.jovinz.datingapp.utils.Constants.USERNAME
import com.jovinz.datingapp.utils.disable
import com.jovinz.datingapp.utils.gone
import com.jovinz.datingapp.utils.isValid
import kotlinx.android.synthetic.main.fragment_signup.*
import java.util.*


class SignUpFragment : Fragment(R.layout.fragment_signup), View.OnClickListener {

    private val navController: NavController by lazy { findNavController() }

    private var uri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        if (UserPrefs.getBoolean(IS_LOGGED_IN)) {
            updateViews()
        } else {
            btnSignUp.setOnClickListener(this)
            profile_image.setOnClickListener(this)
            edtDob.setOnClickListener(this)
        }
    }

    private fun updateViews() {
        btnSignUp.gone()

        context?.let {
            //for some reasons value is getting set to "null" when no photo is selected initially
            if (!UserPrefs.getString(PROFILE).isNullOrBlank()
                && !UserPrefs.getString(PROFILE).equals("null", true)
            ) {
                val uri = Uri.parse(UserPrefs.getString(PROFILE))
                Glide.with(it).load(uri).apply(RequestOptions().circleCrop()).into(profile_image)
            }
        }


        profile_image.setOnClickListener(this@SignUpFragment)

        edtFullName.apply {
            setText(UserPrefs.getString(USERNAME))
            disable()
        }
        edtDob.apply {
            setText(UserPrefs.getString(DOB))
            disable()
        }
        if (UserPrefs.getString(GENDER).equals(getString(R.string.male), true)) {
            radioMale.isChecked = true
        } else radioFemale.isChecked = true

        radioMale.isClickable = false
        radioFemale.isClickable = false
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSignUp -> {
                validateForm()
            }
            R.id.profile_image -> {
                pickImage()
            }
            R.id.edtDob -> {
                selectDOB()
            }
        }
    }

    private fun validateForm() {
        when {
            edtFullName.isValid() -> {
                edtFullName.error = getString(R.string.msg_name_validation)
            }
            edtDob.isValid() -> {
                edtDob.error = getString(R.string.msg_dob_validation)
            }
            !radioFemale.isChecked && !radioMale.isChecked -> {
                Toast.makeText(
                    context,
                    getString(R.string.msg_gender_validation),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                savDataToPreferences()
                navController.apply {
                    navigate(
                        R.id.action_fragment_sign_up_to_fragment_home
                    )
                }
            }
        }
    }

    private fun savDataToPreferences() {
        val gender =
            if (radioMale.isChecked) getString(R.string.male) else getString(R.string.female)

        UserPrefs.apply {
            saveBoolean(IS_LOGGED_IN, true)
            saveString(USERNAME, edtFullName.text.toString())
            saveString(DOB, edtDob.text.toString())
            saveString(GENDER, gender)
            saveString(PROFILE, uri.toString())
        }
    }

    //----------------------------------PROFILE PIC SELECTION --------------------------------//
    private fun pickImage() {
        when (PackageManager.PERMISSION_GRANTED) {
            context?.let {
                ActivityCompat.checkSelfPermission(it, READ_EXTERNAL_STORAGE)
            } -> {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                intent.apply {
                    type = "image/*"
                    putExtra("crop", "true")
                    putExtra("scale", true)
                    putExtra("aspectX", 16)
                    putExtra("aspectY", 9)
                }
                startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
            }
            else -> {
                requestPermissions(
                    arrayOf(READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_REQUEST_CODE
                )

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
            uri = data?.data
            context?.let {
                Glide.with(it).load(uri).apply(RequestOptions().circleCrop()).into(profile_image)
            }
            if (UserPrefs.getBoolean(IS_LOGGED_IN)) {
                UserPrefs.saveString(PROFILE, uri.toString())
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage()
                }
            }
        }
    }

    //----------------------------------DATE SELECTION --------------------------------//
    private fun selectDOB() {
        val cal: Calendar = Calendar.getInstance()
        val mYear = cal.get(Calendar.YEAR)
        val mMonth = cal.get(Calendar.MONTH)
        val mDay = cal.get(Calendar.DAY_OF_MONTH)
        cal.add(Calendar.YEAR, -18)
        val datePickerDialog = context?.let {
            DatePickerDialog(
                it,
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val date =
                        dayOfMonth.toString() + getString(R.string.date_sep) + (monthOfYear + 1) + getString(
                            R.string.date_sep
                        ) + year
                    edtDob.setText(date)
                }, mYear, mMonth, mDay
            )
        }
        datePickerDialog?.datePicker?.maxDate = cal.timeInMillis;
        datePickerDialog?.show()
    }

}