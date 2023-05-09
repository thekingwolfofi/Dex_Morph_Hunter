package com.king.dexmorphhunter.model.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.king.dexmorphhunter.model.data.AppInfo
import org.json.JSONArray

class Converters {

    @TypeConverter
    fun fromNullableList(list: List<String>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Any>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toAppInfoList(value: String): List<AppInfo> {
        return Gson().fromJson(value, Array<AppInfo>::class.java).toList()
    }

    @TypeConverter
    fun fromAppInfo(value: AppInfo): String{
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toAppInfo(value: String): AppInfo {
        val jsonArray = JSONArray(value)
        val clazzList = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            clazzList.add(jsonArray.getString(i))
        }
        return AppInfo(
            packageName = jsonArray.getJSONObject(0).getString("packageName"),
            appName = jsonArray.getJSONObject(0).getString("appName"),
            isSystemApp = jsonArray.getJSONObject(0).getBoolean("isSystemApp"),
            isInterceptedApp = jsonArray.getJSONObject(0).getBoolean("isInterceptedApp"),
            clazzIntercepted = clazzList
        )
    }

    @TypeConverter
    fun toList(value: String?): List<String>? {
        return value?.split(",")
    }

}
