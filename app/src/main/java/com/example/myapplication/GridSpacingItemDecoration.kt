package com.example.myapplication

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // 아이템 위치
        val column = position % spanCount // 열 인덱스

        outRect.left = spacing - column * spacing / spanCount
        outRect.right = (column + 1) * spacing / spanCount

        if (position < spanCount) { // 첫 번째 행
            outRect.top = spacing
        }
        outRect.bottom = spacing // 모든 아이템에 아래쪽 여백
    }
}
