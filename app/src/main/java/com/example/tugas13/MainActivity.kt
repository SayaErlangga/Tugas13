package com.example.tugas13

import FilmAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tugas12.BottomSheetFragment
import com.example.tugas13.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val budgetCollectionRef = firestore.collection("movies")
    private lateinit var binding: ActivityMainBinding
    private lateinit var filmAdapter: FilmAdapter
    private val filmListLiveData: MutableLiveData<List<Film>> by lazy {
        MutableLiveData<List<Film>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "AnimeList"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            filmAdapter = FilmAdapter { film ->

            }

            filmAdapter.setOnItemClickListener { selectedData ->
                showBottomSheetDialog(selectedData)
            }
            rvFilm.layoutManager = GridLayoutManager(this@MainActivity, 2)
            rvFilm.adapter = filmAdapter

            btnAdd.setOnClickListener {
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                startActivity(intent)
            }
        }
        observeBudgets()
        getAllBudgets()
    }

    private  fun getAllBudgets() {
        observeBudgetChanges()
    }

    private fun observeBudgetChanges() {
        budgetCollectionRef.addSnapshotListener{ snapshot, error ->
            if (error != null) {
                Log.d("MainActivity", "Error Listening for budget changes: ", error)
            }
            val movies = snapshot?.toObjects(Film::class.java)
            if (movies != null) {
                filmListLiveData.postValue(movies)
            }
        }
    }

    private fun observeBudgets(){
        filmListLiveData.observe(this){
                budgets ->
            filmAdapter.submitList(budgets)
        }
    }

    private fun deleteBudget(film: Film) {
        if(film.id.isEmpty()) {
            Log.d("MainActivity", "Error delete items: budget Id is empty")
            return
        }
        budgetCollectionRef.document(film.id).delete().addOnFailureListener {
            Log.d("Main Activity", "Error deleting budget", it)
        }
    }

    private fun showBottomSheetDialog(selectedData: Film) {
        val bottomSheetFragment = BottomSheetFragment(
            editCallback = {
                // Intent to edit activity
                val intent = Intent(this@MainActivity, ThirdActivity::class.java)
                intent.putExtra("judulFilm", selectedData.title)
                intent.putExtra("urlFilm", selectedData.url)
                intent.putExtra("ratingFilm", selectedData.rating)
                intent.putExtra("tagFilm", selectedData.tag)
                intent.putExtra("idFilm", selectedData.id)
                startActivity(intent)
            },
            deleteCallback = {
                // Delete the film
                deleteBudget(selectedData)
            }
        )
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }
}