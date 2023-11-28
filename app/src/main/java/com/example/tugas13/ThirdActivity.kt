package com.example.tugas13

import FilmAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.tugas13.databinding.ActivityMainBinding
import com.example.tugas13.databinding.ActivityThirdBinding
import com.google.firebase.firestore.FirebaseFirestore

class ThirdActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val filmCollectionRef = firestore.collection("movies")
    private lateinit var binding: ActivityThirdBinding
    private var updateId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        title = "AnimeList"
        setContentView(binding.root)
        val title = intent.getStringExtra("judulFilm")
        val url = intent.getStringExtra("urlFilm")
        val rating = intent.getStringExtra("ratingFilm")
        val tag = intent.getStringExtra("tagFilm")
        val id = intent.getStringExtra("idFilm")

        with(binding){
            edtJudul.setText(title)
            edtRating.setText(rating)
            edtTag.setText(tag)
            imgCover.setText(url)
            updateId = id.toString()
            btnUpdate.setOnClickListener {
                val updateFilm = Film(url = imgCover.text.toString(), title = edtJudul.text.toString(), rating = edtRating.text.toString(), tag = edtTag.text.toString())
                updateData(updateFilm)
                updateId = ""
                finish()
            }
        }
    }

    private fun updateData(film: Film) {
        film.id = updateId
        filmCollectionRef.document(updateId).set(film).addOnFailureListener {
            Log.d("Main Activity", "error updating budget: ", it)
        }
    }
}