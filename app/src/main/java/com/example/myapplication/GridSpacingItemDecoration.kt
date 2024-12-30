import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,  // 열 개수
    private val spacing: Int,   // 간격 크기
    private val includeEdge: Boolean // 아이템의 가장자리 포함 여부
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // 아이템의 위치
        val column = position % spanCount // 열 번호

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position < spanCount) { // 첫 번째 행
                outRect.top = spacing
            }
            outRect.bottom = spacing // 모든 아이템에 아래 간격 추가
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing // 첫 번째 행이 아닌 경우 위 간격 추가
            }
        }
    }
}