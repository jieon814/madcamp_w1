package com.example.myapplication
import android.content.Context

data class CafePickData(val cafeName: String,
                        var studyPick: Int = 0, var isStudyPicked: Boolean = false,
                        var datePick: Int = 0, var isDatePicked: Boolean = false,
                        var petPick: Int = 0, var isPetPicked: Boolean = false )

class PickManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("CafeLikes", Context.MODE_PRIVATE)

    fun saveStudyPick(cafeName: String, studyPick: Int, isStudyPicked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putInt("${cafeName}_studyPick", studyPick)
        editor.putBoolean("${cafeName}_isStudyPicked", isStudyPicked)
        editor.apply()
    }

    fun saveDatePick(cafeName: String, datePick: Int, isDatePicked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putInt("${cafeName}_datePick", datePick)
        editor.putBoolean("${cafeName}_isDatePicked", isDatePicked)
        editor.apply()
    }

    fun savePetPick(cafeName: String, petPick: Int, isPetPicked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putInt("${cafeName}_petPick", petPick)
        editor.putBoolean("${cafeName}_isPetPicked", isPetPicked)
        editor.apply()
    }

    fun loadPickData(cafeName: String): CafePickData {
        val studyPick = sharedPreferences.getInt("${cafeName}_studyPick", 0)
        val isStudyPiked = sharedPreferences.getBoolean("${cafeName}_isStudyPicked", false)
        val datePick = sharedPreferences.getInt("${cafeName}_datePick", 0)
        val isDatePicked = sharedPreferences.getBoolean("${cafeName}_isDatePicked", false)
        val petPick = sharedPreferences.getInt("${cafeName}_petPick", 0)
        val isPetPicked = sharedPreferences.getBoolean("${cafeName}_isPetPicked", false)

        return CafePickData(cafeName, studyPick, isStudyPiked, datePick, isDatePicked, petPick, isPetPicked)
    }

    fun toggleStudyPick(cafeName: String) {
        val pickData = loadPickData(cafeName)
        if (pickData.isStudyPicked) {
            pickData.studyPick -= 1
            pickData.isStudyPicked = false
        }
        else {
            pickData.studyPick += 1
            pickData.isStudyPicked = true
        }
        saveStudyPick(cafeName, pickData.studyPick, pickData.isStudyPicked)
    }

    fun toggleDatePick(cafeName: String) {
        val pickData = loadPickData(cafeName)
        if (pickData.isDatePicked) {
            pickData.datePick -= 1
            pickData.isDatePicked = false
        }
        else {
            pickData.datePick += 1
            pickData.isDatePicked = true
        }
        saveDatePick(cafeName, pickData.datePick, pickData.isDatePicked)
    }

    fun togglePetPick(cafeName: String) {
        val pickData = loadPickData(cafeName)
        if (pickData.isPetPicked) {
            pickData.petPick -= 1
            pickData.isPetPicked = false
        }
        else {
            pickData.petPick += 1
            pickData.isPetPicked = true
        }
        savePetPick(cafeName, pickData.petPick, pickData.isPetPicked)
    }

}