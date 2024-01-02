package io.multisat.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.multisat.wallet.scripts.SecureStorage
import wallet.core.jni.HDWallet
import java.util.regex.Pattern

class CreateAccountActivity : AppCompatActivity() {
    init {
        System.loadLibrary("TrustWalletCore")
    }

    private lateinit var secureStorage: SecureStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        secureStorage = SecureStorage(this)

        val passwordInput = findViewById<EditText>(R.id.password_create_account)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirm_password_create_account)
        val confirmPasswordButton = findViewById<Button>(R.id.password_confirm_button_create_account)

        confirmPasswordButton.isEnabled = false  // Initially disable the button

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val password = passwordInput.text.toString()
                val confirmPassword = confirmPasswordInput.text.toString()
                confirmPasswordButton.isEnabled = password == confirmPassword && isValidPassword(password)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        }

        passwordInput.addTextChangedListener(textWatcher)
        confirmPasswordInput.addTextChangedListener(textWatcher)

        confirmPasswordButton.setOnClickListener {
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (password == confirmPassword && isValidPassword(password)) {
                val wallet = HDWallet(128, password)
                val mnemonic = wallet.mnemonic()
                secureStorage.save("mnemonic", mnemonic)
                secureStorage.save("password", password)
                println("Mnemonic: $mnemonic")
                println("Password: $password")
                val TAG = "Debug"
                Log.d(TAG, "Mnemonic: $mnemonic")
                Log.d(TAG, "Password: $password")
                val intent = Intent(this, PortofolioActivity::class.java)
                startActivity(intent)
                finish()  // This will finish the current activity
            } else {
                if (password != confirmPassword) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else if (!isValidPassword(password)) {
                    Toast.makeText(this, "Invalid password. Password must be at least 8 characters long and contain only valid characters.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^[\\p{Alnum}\\p{Punct}]{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)

        return matcher.matches()
    }
}
