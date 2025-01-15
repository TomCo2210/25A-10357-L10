package dev.tomco.a25a_10357_l10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.firebase.ui.auth.AuthUI
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.tomco.a25a_10357_l10.databinding.ActivityMainBinding
import dev.tomco.a25a_10357_l10.utilities.Constants

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    private fun getDatabaseReference(path: String): DatabaseReference {
        val database = Firebase.database
        return database.getReference(path);
    }

    private fun initViews() {
        binding.mainBTNSend.setOnClickListener {
            val messageRef = getDatabaseReference(Constants.DB.MESSAGE_REF)
            messageRef.setValue(binding.mainETMessage.text.toString())
        }

        getMessageFromDB(binding.mainLBLMessage)

        binding.mainBTNSignout.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    transactToLoginActivity()
                }
        }

        binding.mainBTNMovieList.setOnClickListener {
            transactToMovieListActivity()
        }
    }

    private fun transactToLoginActivity() {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun transactToMovieListActivity() {
        intent = Intent(this, MovieListActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getMessageFromDB(tv: TextView) {
        // Read from the database
        val messageRef = getDatabaseReference(Constants.DB.MESSAGE_REF)
//        messageRef.addListenerForSingleValueEvent(object : ValueEventListener {
        messageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                tv.text = buildString {
                    append(dataSnapshot.value)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }
}