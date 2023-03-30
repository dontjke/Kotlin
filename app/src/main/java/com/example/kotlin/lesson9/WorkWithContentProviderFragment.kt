package com.example.kotlin.lesson9

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kotlin.R
import com.example.kotlin.databinding.FragmentWorkWithContentProviderBinding


class WorkWithContentProviderFragment : Fragment() {

    private var currentPhone:String =""

    private var _binding: FragmentWorkWithContentProviderBinding? = null //убрали утечку памяти
    private val binding: FragmentWorkWithContentProviderBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkWithContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        //есть ли разрешение? проверка делается каждый раз т.к. пользователь может отобрать разрешение
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED  //если выдано
        ) {
            getContacts()  //получаем
            //если пользователь первый раз отклонил
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            //важно написать убедительную просьбу
            explainReadContacts()
        } else { //если разрешения нет или это первый запрос
            mRequestPermissionReadContacts()
        }
    }

    private fun explainReadContacts() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(getString(R.string.alert_dialog_message))
            .setPositiveButton(getString(R.string.alert_dialog_positive_button)) { _, _ ->
                mRequestPermissionReadContacts()
            }
            .setNegativeButton(getString(R.string.alert_dialog_negative_button)) { dialog, _ -> dialog.dismiss() } //в случае отказа можно перекинуть на страничку с текстом почему это важно
            .create()
            .show()
    }

    private fun explainCall() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title_call))
            .setMessage(getString(R.string.alert_dialog_message))
            .setPositiveButton(getString(R.string.alert_dialog_positive_button)) { _, _ ->
                mRequestPermissionCall()
            }
            .setNegativeButton(getString(R.string.alert_dialog_negative_button)) { dialog, _ -> dialog.dismiss() } //в случае отказа можно перекинуть на страничку с текстом почему это важно
            .create()
            .show()
    }


    private fun mRequestPermissionReadContacts() {
        readContactsResultLauncher.launch(Manifest.permission.READ_CONTACTS)
        // requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_READ_CONTACTS)
    }

    private fun mRequestPermissionCall() {
        callResultLauncher.launch(Manifest.permission.CALL_PHONE)
       // requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CODE_CALL_PHONE)
    }

    private val readContactsResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // Handle Permission granted/rejected
            if (isGranted) {
                getContacts()
            } else {
                explainReadContacts()
            }
        }
    private val callResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // Handle Permission granted/rejected
            if (isGranted) {
                startCall()
            } else {
                explainCall()
            }
        }


    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {

            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.READ_CONTACTS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    explainReadContacts()
                }
            }
        } else if (requestCode == REQUEST_CODE_CALL_PHONE) {

            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.CALL_PHONE && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    explainCall()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }*/



    @SuppressLint("Range")
    private fun getContacts() {
        val contentResolver: ContentResolver = requireContext().contentResolver
        //запрос
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        ) // or DESC по уменьшению
        cursor?.let {
            for (i in 0 until it.count) {
                if (cursor.moveToPosition(i)) {
                    val columnNameIndex =
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                    val name = cursor.getString(columnNameIndex)

                    val id: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                    val phones = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                        null,
                        null
                    )
                    phones?.let {
                        while (phones.moveToNext()) {
                            val phoneNumber: String =
                                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))


                            binding.contactsContainer.addView(TextView(requireContext()).apply {
                                textSize = 30f
                                text = getString(R.string.name_phone_template, name, phoneNumber)
                                setOnClickListener {
                                    currentPhone = phoneNumber
                                    if (ContextCompat.checkSelfPermission(
                                            requireContext(),
                                            Manifest.permission.CALL_PHONE
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        startCall()
                                    } else if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                                        explainCall()
                                    } else {
                                        mRequestPermissionCall()
                                    }
                                }
                            })
                        }
                    }
                }
            }
            cursor.close()
        }
    }

    private fun startCall() {
        startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:$currentPhone")))
    }

    companion object {
        @JvmStatic
        fun newInstance() = WorkWithContentProviderFragment()
    }

}


