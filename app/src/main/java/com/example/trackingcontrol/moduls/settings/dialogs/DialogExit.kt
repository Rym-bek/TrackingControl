package com.example.trackingcontrol.moduls.settings.dialogs

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.trackingcontrol.MainActivity
import com.example.trackingcontrol.R
import com.example.trackingcontrol.moduls.user.UserViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogExit : DialogFragment() {
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle(R.string.attention)
        builder.setMessage(R.string.you_sure_to_exit)
        builder.setPositiveButton(R.string.yes) { dialogInterface, i ->
            userViewModel.exit()
            AuthUI.getInstance().signOut(requireContext())
            goToHome()
        }
        builder.setNegativeButton(R.string.no) { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        return builder.create()
    }

    private fun goToHome()
    {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        )
        startActivity(intent)
    }
}

