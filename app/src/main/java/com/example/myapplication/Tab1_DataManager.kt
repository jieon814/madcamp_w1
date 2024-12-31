package com.example.myapplication

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class CafeData(val name: String, val address: String, val simpleHours: String, val detailHours: String, val contact: String, val imageUrl: String)

class Tab1_DataManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Tab1Data", Context.MODE_PRIVATE)
    private val cachedData: MutableList<CafeData> = mutableListOf()

    fun loadCafes(): List<CafeData> {
        val inputStream = context.resources.openRawResource(R.raw.cafes)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val cafeList = mutableListOf<CafeData>()
        val jsonObject = JSONObject(jsonString) // 루트는 JSONObject

        val keys = jsonObject.keys() // JSONObject의 키 가져오기
        while (keys.hasNext()) {
            val key = keys.next() // 각 카페 이름
            val values = jsonObject.getJSONArray(key) // 해당 키의 값은 JSONArray
            val address = values.getString(0)
            val simpleHours = values.getString(1)
            val detailHours = values.getString(2)
            val contact = values.getString(3)
            val imageUrl = values.getString(4)

            // CafeData 객체 추가
            cafeList.add(CafeData(key, address, simpleHours, detailHours, contact, imageUrl))
        }

        return cafeList
    }


    fun getCachedCafes(): List<CafeData> {
        return cachedData
    }

}