package com.example.nihongomaster.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentRegisterBinding
import com.example.nihongomaster.model.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnRegister.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.tvError.text = error
                binding.tvError.visibility = View.VISIBLE
            } else {
                binding.tvError.visibility = View.GONE
            }
        }

        viewModel.registerSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                // Navigate to main screen
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val displayName = binding.etDisplayName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (validateInput(email, password, confirmPassword)) {
                viewModel.register(email, password, displayName.ifEmpty { null })
            }
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.tvError.text = "Vui lòng nhập email"
                binding.tvError.visibility = View.VISIBLE
                false
            }
            password.isEmpty() -> {
                binding.tvError.text = "Vui lòng nhập mật khẩu"
                binding.tvError.visibility = View.VISIBLE
                false
            }
            confirmPassword.isEmpty() -> {
                binding.tvError.text = "Vui lòng xác nhận mật khẩu"
                binding.tvError.visibility = View.VISIBLE
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tvError.text = "Email không hợp lệ"
                binding.tvError.visibility = View.VISIBLE
                false
            }
            password.length < 6 -> {
                binding.tvError.text = "Mật khẩu phải có ít nhất 6 ký tự"
                binding.tvError.visibility = View.VISIBLE
                false
            }
            password != confirmPassword -> {
                binding.tvError.text = "Mật khẩu không khớp"
                binding.tvError.visibility = View.VISIBLE
                false
            }
            else -> {
                binding.tvError.visibility = View.GONE
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}