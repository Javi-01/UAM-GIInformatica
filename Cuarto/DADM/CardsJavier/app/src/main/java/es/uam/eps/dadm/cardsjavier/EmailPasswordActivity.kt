package es.uam.eps.dadm.cardsjavier

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cardsjavier.databinding.ActivityEmailPasswordBinding

class EmailPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Sign Up
        binding.singUp.setOnClickListener {
            if (binding.emailEditText.text.isEmpty() || binding.passwordEditText.text.isEmpty()) {
                Toast.makeText(applicationContext, "Debes rellenar los campos", Toast.LENGTH_LONG).show()
            }else {
                auth.createUserWithEmailAndPassword(binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(applicationContext, "Registro de Usuario Satisfactorio", Toast.LENGTH_LONG).show()
                        val user = auth.currentUser
                        updateUI(user)
                    }else {
                        Toast.makeText(applicationContext, "Error al registrar usuario", Toast.LENGTH_LONG).show()
                        updateUI(null)
                    }
                }
            }
        }

        // Log In
        binding.logIn.setOnClickListener {
            if (binding.emailEditText.text.isEmpty() || binding.passwordEditText.text.isEmpty()) {
                Toast.makeText(applicationContext, "Debes rellenar los campos", Toast.LENGTH_LONG).show()
            }else {
                auth.signInWithEmailAndPassword(binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(applicationContext, "Inicio de Usuario Satisfactorio", Toast.LENGTH_LONG).show()
                        val user = auth.currentUser
                        updateUI(user)
                    }else {
                        Toast.makeText(applicationContext, "Error de Inicio de Sesión", Toast.LENGTH_LONG).show()
                        updateUI(null)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, TitleActivity::class.java)
            startActivity(intent)
        }else {
            binding.emailEditText.text.clear()
            binding.passwordEditText.text.clear()
        }
    }
}