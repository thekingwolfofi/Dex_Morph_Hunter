package com.king.dexmorphhunter.model.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.king.dexmorphhunter.model.data.AppInfo
import com.king.dexmorphhunter.model.data.ArgumentInfo
import com.king.dexmorphhunter.model.data.ClassInfo
import com.king.dexmorphhunter.model.data.MethodInfo
import org.json.JSONArray
import org.json.JSONObject

class Converters {
    private val gson = Gson()

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
    fun toList(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun toAppInfoList(value: String): List<AppInfo> {
        return gson.fromJson(value, Array<AppInfo>::class.java).toList()
    }

    @TypeConverter
    fun fromAppInfo(value: AppInfo?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toAppInfo(value: String?): AppInfo? {
        val jsonObject = JSONObject(value)
        val packageName = jsonObject.getString("packageName")
        val appName = jsonObject.getString("appName")
        val isSystemApp = if (jsonObject.isNull("isSystemApp")) null else jsonObject.getBoolean("isSystemApp")
        val isInterceptedApp = if (jsonObject.isNull("isInterceptedApp")) null else jsonObject.getBoolean("isInterceptedApp")
        val classInterceptedJson = jsonObject.getJSONArray("classIntercepted").toString()

        val classInterceptedList = gson.fromJson<List<ClassInfo>>(classInterceptedJson, object : TypeToken<List<ClassInfo>>() {}.type)

        return AppInfo(
            packageName = packageName,
            appName = appName,
            isSystemApp = isSystemApp,
            isInterceptedApp = isInterceptedApp
        )
    }

    @TypeConverter
    fun fromClassInfoList(value: List<ClassInfo>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toClassInfoList(value: String?): List<ClassInfo>? {
        return gson.fromJson(value, object : TypeToken<List<ClassInfo>>() {}.type)
    }

    @TypeConverter
    fun fromMethodInfoList(value: List<MethodInfo>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMethodInfoList(value: String?): List<MethodInfo>? {
        return gson.fromJson(value, object : TypeToken<List<MethodInfo>>() {}.type)
    }

    @TypeConverter
    fun fromArgumentInfoList(value: List<ArgumentInfo>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toArgumentInfoList(value: String?): List<ArgumentInfo>? {
        return gson.fromJson(value, object : TypeToken<List<ArgumentInfo>>() {}.type)
    }

    @TypeConverter
    fun fromAny(any: Any?): String? {
        return Gson().toJson(any)
    }

    @TypeConverter
    fun toAny(json: String?): Any? {
        return Gson().fromJson(json, Any::class.java)
    }
    @TypeConverter
    fun toClass(value: String?): Class<*>? {
        return try {
            Class.forName(value)
        } catch (e: ClassNotFoundException) {
            null
        }
    }

    @TypeConverter
    fun fromClass(value: Class<*>?): String? {
        return value?.name
    }
}
