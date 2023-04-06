package com.king.dexmorphhunter.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemAppBinding
import com.king.dexmorphhunter.model.db.AppInfo

class AppListAdapter : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    private var appList: List<AppInfo> = listOf()

    class ViewHolder(private val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appInfo: AppInfo) {
            binding.appName.text = appInfo.appName
            binding.appPackage.text = appInfo.packageName
            binding.appIcon.setImageDrawable(appInfo.appIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(appList[position])
    }

    override fun getItemCount() = appList.size

    fun updateList(list: List<AppInfo>) {
        appList = list
        notifyDataSetChanged()
    }
}
