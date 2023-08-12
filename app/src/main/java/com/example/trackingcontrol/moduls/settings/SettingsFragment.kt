package com.example.trackingcontrol.moduls.settings

import android.os.Bundle

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.trackingcontrol.R
import com.example.trackingcontrol.models.UserData
import com.example.trackingcontrol.moduls.user.UserViewModel
import com.example.trackingcontrol.utils.Resource


class SettingsFragment : PreferenceFragmentCompat() {

    private var userModel: UserData? = null

    private var namePreference: Preference? = null

    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var navController: NavController

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        namePreference = findPreference("name")
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        listeners()
    }

    private fun observers() {
        userViewModel.userData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    userModel= it.result
                    setUserInfo()
                }
            }
        }
    }

    private fun setUserInfo() {
        val name = userModel?.name
        //val email = userModel?.email
        if(userModel?.name!=null)
        {
            namePreference?.title = name
        }
    }

    private fun listeners()
    {
        namePreference?.setOnPreferenceClickListener {
            //navController.navigate(R.id.action_settingsFragment_to_dialogEditName)
            true
        }
    }
}