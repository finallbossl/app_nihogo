package com.example.nihongomaster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nihongomaster.databinding.ActivityWelcomeBinding
import com.example.nihongomaster.model.viewmodel.AuthState
import com.example.nihongomaster.model.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class WelcomeActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val authViewModel: AuthViewModel by viewModels()
    
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { token ->
                    authViewModel.signInWithGoogle(token)
                } ?: run {
                    Toast.makeText(this, "Không lấy được token", Toast.LENGTH_SHORT).show()
                    resetLoginButton()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Đăng nhập thất bại: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                resetLoginButton()
            }
        } else {
            resetLoginButton()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Check if user is already signed in
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            navigateToMain()
            return
        }
        
        setupGoogleSignIn()
        setupClickListeners()
        observeAuthState()
    }
    
    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    
    private fun setupClickListeners() {
        binding.btnGoogleLogin.setOnClickListener {
            // Tạm thời bypass Google Sign In để test
            Toast.makeText(this, "Đăng nhập thành công (Demo)", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }
        
        // Uncomment để dùng Google Sign In thật
        // binding.btnGoogleLogin.setOnClickListener {
        //     signInWithGoogle()
        // }
    }
    
    private fun signInWithGoogle() {
        binding.btnGoogleLogin.isEnabled = false
        binding.btnGoogleLogin.text = "🔄 Đang chuẩn bị..."
        
        try {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi khởi tạo đăng nhập: ${e.message}", Toast.LENGTH_SHORT).show()
            resetLoginButton()
        }
    }
    
    private fun resetLoginButton() {
        binding.btnGoogleLogin.isEnabled = true
        binding.btnGoogleLogin.text = "🔐 Đăng nhập với Google"
    }
    
    private fun observeAuthState() {
        authViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Authenticated -> {
                    Toast.makeText(this, "Chào mừng ${state.user.displayName}!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
                is AuthState.Unauthenticated -> {
                    // Stay on welcome screen
                }
                is AuthState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    resetLoginButton()
                }
            }
        }
        
        authViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.btnGoogleLogin.isEnabled = false
                binding.btnGoogleLogin.text = "🔄 Đang đăng nhập..."
            } else {
                resetLoginButton()
            }
        }
        
        authViewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}