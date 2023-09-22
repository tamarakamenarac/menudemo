package com.example.menupracticaltestapp.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.menupracticaltestapp.R
import com.example.menupracticaltestapp.databinding.FragmentLoginBinding
import com.example.menupracticaltestapp.helpers.ConnectionHelper
import com.example.menupracticaltestapp.helpers.ViewBindingHolder
import com.example.menupracticaltestapp.helpers.ViewBindingHolderImpl
import com.example.menupracticaltestapp.helpers.isValidEmail
import com.example.menupracticaltestapp.helpers.isValidPassword
import com.example.menupracticaltestapp.helpers.setBackgroundTint
import com.example.menupracticaltestapp.screens.Dialogs.setUpOfflineDialog
import com.example.menupracticaltestapp.screens.Dialogs.setupLoginErrorDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment: Fragment(), ViewBindingHolder<FragmentLoginBinding> by ViewBindingHolderImpl() {

    private val loginViewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = initBinding(
        this,
        FragmentLoginBinding.inflate(inflater, container, false)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is LoginViewState.Loading -> {
                    binding.loginProgressBar.visibility = View.VISIBLE
                }
                is LoginViewState.Success -> {
                    binding.loginProgressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_login)
                }
                is LoginViewState.Error -> {
                    binding.loginProgressBar.visibility = View.GONE
                    setupLoginErrorDialog(requireContext()).show()
                }
            }
        }

        binding.loginButton.setOnClickListener {
            val validEmail = isEmailValid()
            val validPassword = isPasswordValid()
            if (validEmail && validPassword) {
                //If the user is offline when starting the app the observer will not catch the initial state
                //To insure the dialog is shown to the user an additional check is added on the login attempt
                if (ConnectionHelper.isOnline(requireContext())) {
                    loginViewModel.login(binding.loginEnterEmail.text.toString(), binding.loginEnterPassword.text.toString())
                }
                else {
                    setUpOfflineDialog(requireContext()).show()
                }
            }
        }
    }

    private fun isEmailValid() : Boolean {
        return if (binding.loginEnterEmail.text.isValidEmail()) {
            binding.loginEmailError.visibility = View.GONE
            binding.loginEnterEmail.setBackgroundTint(R.color.inputTint)
            true
        } else {
            binding.loginEnterEmail.setBackgroundTint(R.color.red)
            binding.loginEmailError.visibility = View.VISIBLE
            false
        }
    }

    private fun isPasswordValid() : Boolean {
        return if (binding.loginEnterPassword.text.isValidPassword()) {
            binding.loginPasswordError.visibility = View.GONE
            binding.loginEnterPassword.setBackgroundTint(R.color.inputTint)
            true
        } else {
            binding.loginEnterPassword.setBackgroundTint(R.color.red)
            binding.loginPasswordError.visibility = View.VISIBLE
            false
        }
    }
}