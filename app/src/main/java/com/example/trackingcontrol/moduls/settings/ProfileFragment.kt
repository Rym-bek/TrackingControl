package com.example.trackingcontrol.moduls.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.trackingcontrol.R
import com.example.trackingcontrol.databinding.ProfileFragmentBinding
import com.example.trackingcontrol.handlers.PhotoHandler
import com.example.trackingcontrol.moduls.storage.viewmodels.StorageViewModel
import com.example.trackingcontrol.moduls.user.UserViewModel
import com.example.trackingcontrol.utils.Resource

class ProfileFragment : Fragment(), MenuProvider {
    private var _binding: ProfileFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var navController:NavController

    private lateinit var photoHandler: PhotoHandler

    private val storageViewModel: StorageViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        navController=findNavController()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        observePhoto()
        observeUser()
        binding.handlers = photoHandler
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_edit_profile, menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoHandler = PhotoHandler(this,storageViewModel)
        initSettings()
    }

    private fun initSettings()
    {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .addToBackStack("first frag")
            .commit()
    }

    private fun observePhoto() {
        storageViewModel.photoUrl.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    val photoUrl = it.result
                    if (photoUrl != null) {
                        userViewModel.updatePhotoUrl(photoUrl)
                    }
                }
            }
        }
    }

    private fun observeUser() {
        userViewModel.userData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    binding.viewModel=userViewModel
                }
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_changeName ->
            {
                navController.navigate(R.id.action_navigation_profile_to_dialogEditName)
                return true
            }
            R.id.action_selectPhoto ->
            {
                photoHandler.onClickPhotoUpdate(binding.fab)
                return true
            }
            R.id.action_exit ->
            {
                navController.navigate(R.id.action_navigation_profile_to_dialogExit)
                return true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}