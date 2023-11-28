package com.example.tugas13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tugas13.databinding.ActivitySecondBinding
import com.google.firebase.firestore.FirebaseFirestore

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val filmCollectionRef = firestore.collection("movies")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "AnimeList"
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnInsert.setOnClickListener {
                addFilm(Film(url = imgCover.text.toString(), title = edtJudul.text.toString(), rating = edtRating.text.toString(), tag = edtTag.text.toString()))
                finish()
            }
        }
    }

    private fun addFilm(film: Film) {
        filmCollectionRef.add(film).addOnSuccessListener {
                documentReference ->
            val createdBudgetId = documentReference.id
            film.id = createdBudgetId
            documentReference.set(film).addOnFailureListener {
                Log.d("Main Activity", "Error updataing budget id: ", it)
            }
        }.addOnFailureListener {
            Log.d("Main Activity", "Error adding budget id: ", it)
        }
    }
}