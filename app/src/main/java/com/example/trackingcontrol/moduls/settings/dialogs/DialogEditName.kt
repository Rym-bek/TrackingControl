package com.example.trackingcontrol.moduls.settings.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.trackingcontrol.R
import com.example.trackingcontrol.moduls.user.UserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogEditName : DialogFragment() {
    private lateinit var editText: EditText
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        editText = EditText(requireActivity())
        editText.hint = getString(R.string.enter_name)
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle(R.string.attention)
        builder.setMessage(R.string.you_sure_to_exit)
        builder.setView(editText)
        builder.setPositiveButton(R.string.yes) { dialogInterface, i ->
            userViewModel.updateDisplayName(editText.text.toString())
        }
        builder.setNegativeButton(R.string.no) { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        return builder.create()
    }
}

