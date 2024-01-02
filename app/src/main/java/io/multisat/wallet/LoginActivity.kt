package io.multisat.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.multisat.wallet.scripts.SecureStorage

class LoginActivity : AppCompatActivity() {

    private lateinit var secureStorage: SecureStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        secureStorage = SecureStorage(this)

        val passwordConfirmButton = findViewById<Button>(R.id.password_confirm_button_login)
        val passwordInput = findViewById<EditText>(R.id.input_password_login)

        passwordConfirmButton.isEnabled = false  // Initially disable the button

        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                passwordConfirmButton.isEnabled = s.length >= 8
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        })

        passwordConfirmButton.setOnClickListener {
            val enteredPassword = passwordInput.text.toString()
            val storedPassword = secureStorage.load("password")

            // If the stored password is not null and matches the entered password, proceed to Dashboard
            if (storedPassword != null && enteredPassword == storedPassword) {
                val dashboardIntent = Intent(this, PortofolioActivity::class.java)
                startActivity(dashboardIntent)
                finish()
            } else {
                // If password is incorrect, show an error message
                Toast.makeText(this, "Incorrect password, try again.", Toast.LENGTH_SHORT).show()
                // Optionally clear the entered password or shake the password field to indicate an error
                passwordInput.text.clear()
            }
        }
    }
}
