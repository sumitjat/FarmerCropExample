package com.example.farmercropexample.presentation.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.farmercropexample.data.model.Records
import com.example.farmercropexample.databinding.LayoutRepoItemBinding

class FarmerCropAdapter : RecyclerView.Adapter<FarmerCropAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutRepoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    private var pullRequestList: List<Records> = listOf()

    override fun getItemCount(): Int {
        return pullRequestList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun submitList(list: List<Records>) {
        val diffUtil =
            DiffUtilCallBack(pullRequestList, list)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        pullRequestList = list
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun getItem(position: Int): Records {
        return pullRequestList[position]
    }

    class MyViewHolder(
        private val binding: LayoutRepoItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: Records) {


            binding.tvName.text = data.state
            binding.tvTitle.text = data.district
            binding.tvCreatedAt.text = "Modal Price ${data.modal_price}"
            binding.tvClosedAt.text = "Price Between ${data.min_price} ${data.max_price}"

        }
    }

    class DiffUtilCallBack(
        private val oldList: List<Records>,
        private val newList: List<Records>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].state == newList[newItemPosition].state
                    && oldList[oldItemPosition].market == newList[newItemPosition].market
        }

    }
}
