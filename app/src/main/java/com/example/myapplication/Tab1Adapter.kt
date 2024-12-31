package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemRecyclerviewBinding


class Tab1Adapter(
    private val items: List<CafeData>,
    private val pickManager: PickManager) : RecyclerView.Adapter<Tab1Adapter.Tab1ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Tab1ViewHolder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Tab1ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Tab1ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Tab1ViewHolder(private val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CafeData) {
            // 데이터 바인딩
            binding.shopName.text = item.name
            binding.shopAddress.text = item.address
            binding.shopHours.text = item.simpleHours

            // Glide를 사용하여 이미지 로드
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.shopImage)

            // LikeManager에서 현재 상태 로드
            val pickData = pickManager.loadPickData(item.name)
            updateButtonImages(pickData)


            binding.studyPickCount.text = String.format("%d", pickData.studyPick)
            binding.datePickCount.text = String.format("%d", pickData.datePick)
            binding.petPickCount.text = String.format("%d", pickData.petPick)

            // Study Pick 버튼 클릭 리스너
            binding.studyPickButton.setOnClickListener {
                pickManager.toggleStudyPick(item.name) // 좋아요 토글
                val updatedPickData = pickManager.loadPickData(item.name)
                binding.studyPickCount.text = updatedPickData.studyPick.toString()
                updateButtonImages(updatedPickData)
            }

            // Date Pick 버튼 클릭 리스너
            binding.datePickButton.setOnClickListener {
                pickManager.toggleDatePick(item.name) // 좋아요 토글
                val updatedPickData = pickManager.loadPickData(item.name)
                binding.datePickCount.text = updatedPickData.datePick.toString()
                updateButtonImages(updatedPickData)
            }

            // Pet Pick 버튼 클릭 리스너
            binding.petPickButton.setOnClickListener {
                pickManager.togglePetPick(item.name) // 좋아요 토글
                val updatedPickData = pickManager.loadPickData(item.name)
                binding.petPickCount.text = updatedPickData.petPick.toString()
                updateButtonImages(updatedPickData)
            }

            binding.shopImage.setOnClickListener {
                showCafeDialog(item)
            }
        }

        // 버튼 이미지를 상태에 따라 업데이트
        private fun updateButtonImages(pickData: CafePickData) {
            // Study Pick 버튼 이미지 업데이트
            binding.studyPickButton.setImageResource(
                if (pickData.isStudyPicked) R.drawable.ic_heart_orange_filled else R.drawable.ic_heart_orange
            )

            // Date Pick 버튼 이미지 업데이트
            binding.datePickButton.setImageResource(
                if (pickData.isDatePicked) R.drawable.ic_heart_yellow_filled else R.drawable.ic_heart_yellow
            )

            // Pet Pick 버튼 이미지 업데이트
            binding.petPickButton.setImageResource(
                if (pickData.isPetPicked) R.drawable.ic_heart_blue_filled else R.drawable.ic_heart_blue
            )
        }

        private fun showCafeDialog(item: CafeData) {
            val context = binding.root.context
            val dialog = ViewCafeDialogFragment.newInstance(item.imageUrl, item.name)
            if (context is FragmentActivity) {
                dialog.show(context.supportFragmentManager, "ViewPostDialog")
            }
        }
    }
}