package net.treelzebub.readerbility.util

import android.content.Context
import android.content.SharedPreferences
import net.sarazan.prefs.Pref
import net.sarazan.prefs.SharedPref
import net.treelzebub.readerbility.R
import java.io.File

/**
 * Created by Tre Murillo on 1/1/16
 */
public object PrefsUtils {

    public fun getPrefs(c: Context): SharedPreferences? {
        return c.applicationContext.getSharedPreferences(c.getString(R.string.key_pref_file), Context.MODE_PRIVATE)
    }

    public fun clearPrefs(c: Context): Boolean {
        getPrefs(c)?.edit()?.clear()?.commit()
        val root = c.filesDir ?: return false
        val dir = File(root.parent + "/shared_prefs/")
        val xml = File(dir, c.getString(R.string.key_pref_file) + ".xml")
        return xml.delete()
    }

    public fun <T> userPref(key: String, clazz: Class<T>): Pref<T> {
        return object : SharedPref<T>(key, clazz) {
            override fun getSharedPreferences(c: Context): SharedPreferences? {
                return getPrefs(c)
            }
        }
    }
}
