# Glassic Player — Liquid Glass Android Music Player

Glassic Player is a premium, offline-first Android music player built with Jetpack Compose and Material 3, featuring a translucent **Liquid Glass (Glassmorphism)** user interface and dynamic album artwork color ambient lighting.

![Liquid Glass Aesthetics](https://img.shields.org/badge/Design-Liquid%20Glass-38bdf8)
![Kotlin](https://img.shields.org/badge/Kotlin-2.2.10-7c3aed)
![Android Media3](https://img.shields.org/badge/Media3-ExoPlayer-06b6d4)
![Jetpack Compose](https://img.shields.org/badge/Compose-Material%203-4285f4)
![CI/CD](https://img.shields.org/badge/CI%2FCD-GitHub%20Actions-22c55e)

---

## ✨ Features

- 💎 **Liquid Glass UI**: Translucent glass cards, floating bottom navigation bar, frosted glass search bars, floating mini player, and glass progress sliders.
- 🎨 **Dynamic Album Artwork Canvas**: Extracts Palette accent colors from active track artwork, generating animated blurred ambient gradient backgrounds that smoothly transition between songs.
- 🎵 **Offline Local Music Scanning**: Scans device audio via `MediaStore` (MP3, M4A, FLAC, AAC, WAV, OGG) with graceful permission handling and a fallback sample library.
- 📻 **Android Media3 & ExoPlayer Engine**: Seamless background playback, lock-screen MediaSession controls, system media notification with album art, shuffle, repeat, and play queue support.
- 🗄️ **Room Local Persistence**: Save favorite tracks, custom playlists, track metadata, and listening history locally on your device.
- 🔍 **Instant Search**: Liquid glass search bar with real-time filtering across tracks, artists, and album collections.
- 🌗 **Custom Centralized Theme System**: Full Light, Dark, and System default translucent glass themes.
- 🤖 **GitHub Actions CI/CD**: Automated debug APK compilation workflow on every push or workflow trigger.

---

## 🏗 Architecture & Codebase Structure

```
app/src/main/java/com/example/
├── data/
│   ├── database/        # Room Entities (SongEntity, PlaylistEntity, CrossRef), DAO & AppDatabase
│   ├── media/           # MediaScanner for MediaStore audio retrieval & sample audio fallback
│   └── repository/      # MusicRepository coordinating local audio & Room persistence
├── domain/
│   └── model/           # Domain data models (Song, Album, Artist, Playlist, ThemeMode)
├── media/
│   ├── player/          # MusicPlayerEngine (ExoPlayer state, queue, repeat/shuffle)
│   └── service/         # PlaybackService (Android Media3 MediaSessionService & Notifications)
├── ui/
│   ├── components/      # Reusable Glass UI components (GlassCard, GlassButton, GlassMiniPlayer, etc.)
│   └── theme/           # LiquidGlassTheme design tokens, colors, shapes, and typography
├── presentation/        # ViewModels & Screens (Home, Songs, Albums, Artists, Playlists, Search, NowPlaying, Settings)
└── MainActivity.kt      # Main entry point & MediaSession binding
```

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Ladybug / Jellyfish or newer
- **JDK**: Version 17 or higher
- **Android SDK**: API Level 36 (Min SDK 24 / Android 7.0+)

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/glassic-player.git
cd glassic-player
```

### 2. Open in Android Studio

1. Launch Android Studio.
2. Select **Open** and choose the `glassic-player` directory.
3. Allow Gradle to sync dependencies automatically.

### 3. Run on Device or Emulator

1. Connect a physical Android device (with USB Debugging enabled) or start an Android Virtual Device (AVD).
2. Click **Run** (`Shift + F10` or the green play button) in Android Studio.

---

## ⚙️ Building via Command Line

You can build the debug APK directly from your terminal:

```bash
# On Linux / macOS
chmod +x ./gradlew
./gradlew assembleDebug

# On Windows
gradlew.bat assembleDebug
```

The generated APK will be available at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 🤖 GitHub Actions CI/CD Setup

The repository includes a pre-configured GitHub Actions workflow in `.github/workflows/android-build.yml`.

### How it Works:

1. **Triggers**: Runs automatically on pushes or pull requests to `main`, as well as manual triggers via `workflow_dispatch`.
2. **Environment**: Ubuntu runner with JDK 17 and Gradle setup.
3. **Execution**: Runs `./gradlew assembleDebug`.
4. **Artifacts**: Uploads the compiled `app-debug.apk` as a downloadable artifact.

### Finding your Generated APK Artifact:

1. Go to your GitHub Repository -> **Actions** tab.
2. Select the latest workflow run.
3. Scroll down to the **Artifacts** section at the bottom of the page.
4. Click **glassic-player-debug-apk** to download the compiled APK ZIP file.

---

## 🔐 Security & Secrets

This project runs fully offline and does not hardcode any sensitive API keys or user credentials.
If you integrate cloud APIs or signing keys for production releases in the future, configure them securely via **GitHub Repository Secrets** (`Settings -> Secrets and variables -> Actions`).

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.
