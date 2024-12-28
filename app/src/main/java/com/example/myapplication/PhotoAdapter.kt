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
        Glide.with(holder.photoImageView.context)
            .load(photoUri)
            .centerCrop()
            .into(holder.photoImageView)
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}
