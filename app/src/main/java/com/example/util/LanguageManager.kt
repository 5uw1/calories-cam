package com.example.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppLanguage(val code: String, val displayName: String, val flagEmoji: String) {
    EN("en", "English", "🇺🇸"),
    TH("th", "ไทย", "🇹🇭")
}

object LanguageManager {
    private val _currentLanguage = MutableStateFlow(AppLanguage.EN)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.EN) AppLanguage.TH else AppLanguage.EN
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }
}
