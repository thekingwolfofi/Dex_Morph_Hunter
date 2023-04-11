package com.king.dexmorphhunter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemAppBinding
import com.king.dexmorphhunter.model.db.AppInfo
import com.king.dexmorphhunter.viewmodel.AppListViewModel

class AppListAdapter(
    private val context: Context,
    private var appList: List<AppInfo>,
    private val onInterceptedAppChanged: (packageName: String, checked: Boolean) -> Unit) : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    class ViewHolder(private val itemBinding: ItemAppBinding, private val onInterceptedAppChanged: (packageName: String, checked: Boolean) -> Unit) : RecyclerView.ViewHolder(itemBinding.root) {


        fun bind(appInfo: AppInfo) {
            itemBinding.appName.text = appInfo.appName
            itemBinding.appPackage.text = appInfo.packageName
            itemBinding.appIcon.setImageDrawable(appInfo.appIcon)
            itemBinding.appInterceptionSwitch.isChecked = appInfo.isInterceptedApp

            itemBinding.appInterceptionSwitch.setOnCheckedChangeListener { _, isChecked  ->
                onInterceptedAppChanged(appInfo.packageName, isChecked)
            }

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemAppBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(itemBinding,onInterceptedAppChanged)
    }
    override fun getItemCount(): Int = appList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val posAppInfo: AppInfo = appList[position]
        holder.bind(posAppInfo)
    }

}