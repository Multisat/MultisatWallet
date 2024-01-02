package io.multisat.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val createAccount = findViewById<ConstraintLayout>(R.id.create_account_container)
        val importAccount = findViewById<ConstraintLayout>(R.id.import_account_container)

        createAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        importAccount.setOnClickListener {
            val intent = Intent(this, ImportAccountActivity::class.java)
            startActivity(intent)
        }
    }
}