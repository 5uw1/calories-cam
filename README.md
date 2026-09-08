# CalorieCam (calories-cam) 📸🥗

An AI-powered Android calorie & nutrition tracker application built with **Kotlin**, **Jetpack Compose**, and **Google Gemini API**.

---

## ✨ Features

- **AI Food Vision Scanner**: Point your camera at any food dish to automatically identify calories, macros (protein, carbs, fat), and micronutrients.
- **Swiss & Thai Cuisine Presets**: Curated dishes from Switzerland (Cheese Fondue, Raclette, Rösti, Bircher Muesli, etc.) and Thailand with accurate nutritional breakdowns.
- **Bilingual Interface**: Seamlessly switch between **English** and **Thai** across the entire UI.
- **Offline Data Persistence**: Built-in **Room Database** to log daily meals, track calorie goals, and browse food history offline.
- **Material 3 Design**: Modern, responsive UI with circular nutrient rings, macro progress bars, and high-contrast typography.

---

## 🛠️ Tech Stack

- **UI**: Jetpack Compose, Material 3
- **Language**: Kotlin
- **Architecture**: MVVM, Clean Architecture, Kotlin Coroutines & Flow
- **Local Storage**: Android Jetpack Room Database
- **AI Integration**: Google Gemini Vision API (Multimodal)
- **Networking**: OkHttp3
- **Testing**: JUnit 4, Robolectric

---

## 🚀 Getting Started

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/calorie-cam.git
   ```
2. Open the project in **Android Studio Ladybug (or newer)**.
3. Add your Gemini API key:
   - Copy `.env.example` to `.env` (or configure via AI Studio Secrets):
     ```env
     GEMINI_API_KEY=your_gemini_api_key_here
     ```
4. Build and run the app on an Android device or emulator (Android 7.0+ / API 24+).

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
