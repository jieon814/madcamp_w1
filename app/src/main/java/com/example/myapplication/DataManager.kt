package com.example.myapplication

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class Post(val photoUri: String, val text: String, val additionalText: String)


class DataManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("PostData", Context.MODE_PRIVATE)

    // 게시물 저장
    fun savePosts(posts: List<Post>) {
        val jsonArray = JSONArray()
        for (post in posts) {
            val jsonObject = JSONObject()
            jsonObject.put("photoUri", post.photoUri)
            jsonObject.put("text", post.text)
            jsonObject.put("additionalText", post.additionalText) // 추가 필드 저장
            jsonArray.put(jsonObject)
        }
        sharedPreferences.edit().putString("posts", jsonArray.toString()).apply()
    }

    // 게시물 불러오기
    fun loadPosts(): List<Post> {
        val jsonString = sharedPreferences.getString("posts", null) ?: return emptyList()
        val jsonArray = JSONArray(jsonString)
        val postList = mutableListOf<Post>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val photoUri = jsonObject.getString("photoUri")
            val text = jsonObject.getString("text")
            val additionalText = jsonObject.optString("additionalText", "") // 추가 필드 로드
            postList.add(Post(photoUri, text, additionalText))
        }

        return postList
    }
}
