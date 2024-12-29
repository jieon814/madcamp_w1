import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R

class PhotoAdapter(private val photos: List<Uri>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    // ViewHolder: 개별 아이템의 뷰를 관리
    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoImageView: ImageView = view.findViewById(R.id.photoImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoUri = photos[position]
        // Glide를 사용해 이미지 로드

        // RecyclerView의 화면 크기를 가져옴
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        // 열 개수와 간격을 기준으로 아이템 크기 계산
        val spanCount = 3 // 열 개수
        val spacing = 2 // 간격 (픽셀)
        val totalSpacing = (spanCount - 1) * spacing // 총 간격
        val itemSize = (screenWidth - totalSpacing) / spanCount // 아이템 크기 계산

        // 아이템 크기와 간격 설정
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = itemSize
        layoutParams.height = itemSize

        // 좌우 간격 설정
        val column = position % spanCount // 열 위치 계산
        layoutParams.leftMargin = column * spacing / spanCount
        layoutParams.rightMargin = spacing - (column + 1) * spacing / spanCount

        // 상단 간격 설정 (첫 번째 행만)
        if (position < spanCount) {
            layoutParams.topMargin = spacing
        }
        layoutParams.bottomMargin = spacing // 하단 간격 설정

        holder.itemView.layoutParams = layoutParams


        Glide.with(holder.photoImageView.context)
            .load(photoUri)
            .centerCrop()
            .into(holder.photoImageView)
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}
