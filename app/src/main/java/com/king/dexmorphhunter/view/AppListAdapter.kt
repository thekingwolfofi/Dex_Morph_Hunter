package com.king.dexmorphhunter.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.model.AppInfo
import com.king.dexmorphhunter.databinding.AppListItemBinding
import com.king.dexmorphhunter.viewmodel.MyViewModel

class AppListAdapter(private val viewModel: MyViewModel) : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AppListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appList = viewModel.getInstalledAppsList().value
        val appInfo = appList?.get(position)

        if (appInfo != null) {
            holder.bind(appInfo)
        }
    }


    inner class ViewHolder(private val binding: AppListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(app: AppInfo) {
            binding.appName.text = app.name
            binding.appIcon.setImageDrawable(app.icon)
            binding.appPackage.text = app.packageName
        }
    }

}

class AppInfoDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
    override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
        return oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
        return oldItem == newItem
    }
}
