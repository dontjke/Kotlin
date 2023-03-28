package com.example.kotlin.lesson9

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kotlin.R
import com.example.kotlin.databinding.FragmentWorkWithContentProviderBinding
import com.example.kotlin.utils.REQUEST_CODE


class WorkWithContentProviderFragment : Fragment() {

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

    fun checkPermission() {
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
            explain()
        } else { //если разрешения нет и это первый запрос
            mRequestPermission()
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(getString(R.string.alert_dialog_message))
            .setPositiveButton(getString(R.string.alert_dialog_positive_button)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(getString(R.string.alert_dialog_negative_button)) { dialog, _ -> dialog.dismiss() } //в случае отказа можно перекинуть на страничку с текстом почему это важно
            .create()
            .show()
    }


    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {

            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.READ_CONTACTS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    explain()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }

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
                    val columnNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val name = cursor.getString(columnNameIndex)
                    binding.contactsContainer.addView(TextView(requireContext()).apply {
                        textSize = 30f
                        text = name

                    })
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = WorkWithContentProviderFragment()
    }

}
