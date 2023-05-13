package com.king.dexmorphhunter.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemAppBinding
import com.king.dexmorphhunter.model.data.AppInfo
import com.king.dexmorphhunter.model.util.Constants.removePackage
import com.king.dexmorphhunter.view.MethodSelectActivity
import com.king.dexmorphhunter.viewmodel.AppListViewModel
import javax.inject.Inject

class AppListAdapter @Inject constructor(
    private val context: Context,
    private val appListViewModel: AppListViewModel,
    private var updateIsIntercepted: ((packageName: String, isIntercepted: Boolean) -> Unit),
    private var getBitmapFromPackage: ((packageName: String) -> Bitmap?)
) : RecyclerView.Adapter<AppListAdapter.AppListViewHolder>() {

    private var appList: List<AppInfo> = emptyList()

    fun updateList(appList: List<AppInfo>) {
        val diffResult = DiffUtil.calculateDiff(AppListDiffUtilCallback(this.appList, appList))
        this.appList = appList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppBinding.inflate(inflater, parent, false)
        return AppListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppListViewHolder, position: Int) {
        holder.bind(appList[position])
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    inner class AppListViewHolder(private val itemBinding: ItemAppBinding) : RecyclerView.ViewHolder(itemBinding.root) {


        private lateinit var classList: List<String>
        fun bind(appInfo: AppInfo) {
            itemBinding.appName.text = appInfo.appName
            itemBinding.appPackage.text = appInfo.packageName
            itemBinding.appIcon.setImageBitmap(getBitmapFromPackage(appInfo.packageName))
            itemBinding.appInterceptionSwitch.isChecked = appInfo.isInterceptedApp ?: false

            itemBinding.appInterceptionSwitch.setOnCheckedChangeListener { _, _  ->
                updateInterceptSwitch(appInfo)
            }

            itemBinding.itemList.setOnClickListener {
                updateInterceptSwitch(appInfo)
            }

        }

        @SuppressLint("SetTextI18n")
        private fun updateInterceptSwitch(appInfo: AppInfo) {
            classList = appListViewModel.getExtractedClassesFromApp(context, appInfo.packageName)
            if(classList.isNotEmpty()) {
                val intent = Intent(context, MethodSelectActivity::class.java)
                itemBinding.appInterceptionSwitch.isChecked = true
                val isRemovedApp = removePackage.any { it in appInfo.appName || it in appInfo.packageName }
                if (isRemovedApp) {
                    itemBinding.appPackage.text = "Block App"
                    itemBinding.appInterceptionSwitch.isChecked = false
                    itemBinding.appInterceptionSwitch.isEnabled = false
                } else {
                    intent.putExtra("packageName", appInfo.packageName)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                    updateIsIntercepted(
                        appInfo.packageName,
                        itemBinding.appInterceptionSwitch.isChecked
                    )
                }
            } else {
                itemBinding.appPackage.text = "Empty Classes"
                itemBinding.appInterceptionSwitch.isChecked = false
                itemBinding.appInterceptionSwitch.isEnabled = false

            }
        }
    }

    private class AppListDiffUtilCallback(private val oldList: List<AppInfo>, private val newList: List<AppInfo>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].packageName == newList[newItemPosition].packageName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].isInterceptedApp == newList[newItemPosition].isInterceptedApp
        }
    }
}
