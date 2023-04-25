package com.king.dexmorphhunter.view.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.king.dexmorphhunter.databinding.ItemAppBinding
import com.king.dexmorphhunter.model.db.AppInfo
import com.king.dexmorphhunter.view.ActivityMethodSelect
import com.king.dexmorphhunter.viewmodel.AppListViewModel

class AppListAdapter(
    private val context: Context,
    private var appList: List<AppInfo>,
    private val updateIsIntercepted: (packageName: String, checked: Boolean) -> Unit,
    private val getBitmapFromPackage: (packageName: String) -> Drawable
) : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {


    class ViewHolder(
        private val context: Context,
        private val itemBinding: ItemAppBinding,
        private val updateIsIntercepted: (packageName: String, checked: Boolean) -> Unit,
        private val getBitmapFromPackage: (packageName: String) -> Drawable
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var classList: List<String>
        private val appListViewModel = AppListViewModel(context)
        fun bind(appInfo: AppInfo) {
            itemBinding.appName.text = appInfo.appName
            itemBinding.appPackage.text = appInfo.packageName
            itemBinding.appIcon.setImageDrawable(getBitmapFromPackage(appInfo.packageName))
            itemBinding.appInterceptionSwitch.isChecked = appInfo.appIsIntercepted ?: false

            itemBinding.appInterceptionSwitch.setOnCheckedChangeListener { _, isChecked  ->
                updateIsIntercepted(appInfo.packageName, isChecked)
            }

            itemBinding.itemList.setOnClickListener {
                val intent = Intent(context, ActivityMethodSelect::class.java)
                itemBinding.appInterceptionSwitch.isChecked = true
                classList = appListViewModel.getExtractedClassesFromApp(context, appInfo.packageName)
                if(classList.isNotEmpty()) {
                    intent.putExtra("packageName", appInfo.packageName)
                    //intent.putExtra("classList", classList)
                    context.startActivity(intent)
                    updateIsIntercepted(appInfo.packageName,itemBinding.appInterceptionSwitch.isChecked)

                } else{
                    itemBinding.appPackage.text = "Empty Classes"
                    itemBinding.appInterceptionSwitch.isChecked = false
                    itemBinding.appInterceptionSwitch.isEnabled = false

                }
            }

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemAppBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(context, itemBinding,updateIsIntercepted,getBitmapFromPackage)
    }
    override fun getItemCount(): Int = appList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val posAppInfo: AppInfo = appList[position]
        holder.bind(posAppInfo)
    }

}