package com.example.myapplication
import android.content.Context
import kotlin.random.Random

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

        val randomStudyPick = Random.nextInt(0, 21)
        val randomDatePick = Random.nextInt(0, 21)
        val randomPetPick = Random.nextInt(0, 21)

        var studyPick = sharedPreferences.getInt("${cafeName}_studyPick", 0)
        val isStudyPicked = sharedPreferences.getBoolean("${cafeName}_isStudyPicked", false)
        if (studyPick == 0)  {
            studyPick = randomStudyPick
            saveStudyPick(cafeName, studyPick, isStudyPicked)
        }
        var datePick = sharedPreferences.getInt("${cafeName}_datePick", 0)
        val isDatePicked = sharedPreferences.getBoolean("${cafeName}_isDatePicked", false)
        if (datePick == 0)  {
            datePick = randomDatePick
            saveDatePick(cafeName, datePick, isDatePicked)
        }

        var petPick = sharedPreferences.getInt("${cafeName}_petPick", 0)
        val isPetPicked = sharedPreferences.getBoolean("${cafeName}_isPetPicked", false)
        if (petPick == 0)  {
            petPick = randomPetPick
            savePetPick(cafeName, petPick, isPetPicked)
        }


        return CafePickData(cafeName, studyPick, isStudyPicked, datePick, isDatePicked, petPick, isPetPicked)
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

        SharedPickManager.notifyListeners()
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
        SharedPickManager.notifyListeners()
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
        SharedPickManager.notifyListeners()
    }

}