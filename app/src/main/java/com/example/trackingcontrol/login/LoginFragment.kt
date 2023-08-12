package com.example.trackingcontrol.login


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trackingcontrol.MainActivity
import com.example.trackingcontrol.R
import com.example.trackingcontrol.databinding.LoginFragmentBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private var _binding: LoginFragmentBinding? = null

    private val binding get() = _binding!!

    private var user: FirebaseUser?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private fun isLogin() {
        user = Firebase.auth.currentUser
        if (user != null) {
            goToHome()
        }
        else
        {
            createSignInIntent()
        }
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLogin()
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            goToHome()
        }
    }

    private fun goToHome()
    {
        findNavController().navigate(R.id.action_loginFragment_to_navigation_map)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
