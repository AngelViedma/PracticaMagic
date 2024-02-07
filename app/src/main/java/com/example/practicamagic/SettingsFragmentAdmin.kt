package com.example.practicamagic

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class SettingsFragmentAdmin : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_admin, rootKey)
    }


}