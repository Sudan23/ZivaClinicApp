# Ziva Clinic Face Scanning Application

<div align="center">
  <h3>AI-Powered Skin Analysis & Face Scanning</h3>
  <p><strong>Personalized Care for Confidence</strong></p>
  <p>Professional-grade skin analysis powered by advanced ML technology</p>
</div>

## 📱 About

Ziva Clinic is a comprehensive Android application that provides AI-powered skin analysis through multi-angle face capture. Built with modern Android development practices using Kotlin and Jetpack Compose, this app delivers real-time face detection, detailed skin health assessments, and personalized care recommendations.

**Website:** [https://www.zivaclinic.com](https://www.zivaclinic.com)

## ✨ Features

### Core Functionality
- **Multi-Angle Face Capture**: Captures front, left, and right profile views for complete facial analysis
- **Real-Time Face Detection**: ML Kit-powered face detection with positioning guidance
- **AI Skin Analysis**: Advanced analysis of skin health metrics including:
  - Overall skin score
  - Skin age estimation
  - Hydration levels
  - Elasticity measurement
  - Skin type detection
  - Concern identification (wrinkles, dark spots, pores, etc.)
- **Personalized Recommendations**: Treatment, product, and lifestyle suggestions based on analysis
- **Scan History**: Track skin health progress over time
- **Beautiful UI**: Material Design 3 with Ziva Clinic brand theming

### Technical Features
- **CameraX Integration**: Modern camera API for reliable image capture
- **ML Kit Face Detection**: Google's ML Kit for accurate face tracking
- **Room Database**: Local persistence of scan results
- **Jetpack Compose**: Modern declarative UI framework
- **Material Design 3**: Latest Material Design guidelines
- **MVVM Architecture**: Clean separation of concerns
- **Coroutines & Flow**: Asynchronous programming with Kotlin Flow

## 🎨 Brand Identity

- **Primary Color**: #8B6F47 (Warm Brown)
- **Primary Light**: #A0826D (Muted Taupe)
- **Primary Dark**: #6B5537 (Dark Brown)
- **Cream**: #F5F0E8
- **Tagline**: "Personalized Care for Confidence"

## 🛠️ Technology Stack

### Core Technologies
- **Language**: Kotlin 1.9.20
- **UI Framework**: Jetpack Compose (BOM 2024.01.00)
- **Build System**: Gradle 8.2 with Kotlin DSL
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

### Key Libraries
- **AndroidX Core**: androidx.core:core-ktx:1.12.0
- **Compose**: Material3, UI, Navigation
- **CameraX**: 1.3.1 (Core, Camera2, Lifecycle, View)
- **ML Kit**: face-detection:16.1.6
- **Room**: 2.6.1 (Runtime, KTX, Compiler)
- **Coil**: 2.5.0 (Image loading)
- **Coroutines**: kotlinx-coroutines-android:1.7.3
- **Accompanist**: permissions:0.34.0
- **Gson**: 2.10.1

## 📋 Prerequisites

- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: Java 17
- **Android SDK**: API 34
- **Gradle**: 8.2+ (included via wrapper)
- **Device/Emulator**: Android 7.0+ with camera support

## 🚀 Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/Sudan23/ZivaClinicApp.git
cd ZivaClinicApp
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned directory
4. Click "OK"

### 3. Sync Gradle

Android Studio will automatically sync Gradle files. If not:
- Click "File" → "Sync Project with Gradle Files"
- Or click the elephant icon in the toolbar

### 4. Build the Project

Using Android Studio:
- Click "Build" → "Make Project"
- Or press `Ctrl+F9` (Windows/Linux) or `Cmd+F9` (Mac)

Using command line:
```bash
# Make gradlew executable (Linux/Mac)
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Build release APK (requires signing config)
./gradlew assembleRelease
```

### 5. Run the Application

#### On Physical Device:
1. Enable "Developer Options" on your Android device
2. Enable "USB Debugging"
3. Connect device via USB
4. Click "Run" → "Run 'app'" in Android Studio
5. Or use command: `./gradlew installDebug`

#### On Emulator:
1. Click "Device Manager" in Android Studio
2. Create a new virtual device (Pixel 5 or similar recommended)
3. Select API 34 (Android 14)
4. Click "Run" → "Run 'app'"
5. Or use command: `./gradlew installDebug`

## 📂 Project Structure

```
ZivaClinicApp/
├── app/
│   ├── build.gradle.kts                    # App-level Gradle configuration
│   ├── proguard-rules.pro                  # ProGuard rules
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml         # App manifest
│           ├── java/com/zivaclinic/skinscanner/
│           │   ├── MainActivity.kt         # Main entry point
│           │   ├── data/
│           │   │   ├── local/              # Database layer
│           │   │   │   ├── entities/       # Room entities
│           │   │   │   ├── ScanDao.kt      # Database queries
│           │   │   │   ├── ScanDatabase.kt # Room database
│           │   │   │   └── Converters.kt   # Type converters
│           │   │   ├── model/              # Data models
│           │   │   │   ├── CaptureAngle.kt
│           │   │   │   ├── ScanResult.kt
│           │   │   │   ├── SkinConcern.kt
│           │   │   │   └── Recommendation.kt
│           │   │   └── repository/         # Repository layer
│           │   │       └── ScanRepository.kt
│           │   ├── ui/
│           │   │   ├── components/         # Reusable UI components
│           │   │   │   ├── CameraPreview.kt
│           │   │   │   ├── FaceGuideOverlay.kt
│           │   │   │   ├── ScoreRing.kt
│           │   │   │   ├── ConcernCard.kt
│           │   │   │   └── RecommendationCard.kt
│           │   │   ├── navigation/         # Navigation setup
│           │   │   │   └── NavGraph.kt
│           │   │   ├── screens/            # App screens
│           │   │   │   ├── home/           # Home screen
│           │   │   │   ├── scan/           # Scan screen + ViewModel
│           │   │   │   ├── results/        # Results screen + ViewModel
│           │   │   │   ├── history/        # History screen + ViewModel
│           │   │   │   └── profile/        # Profile screen
│           │   │   └── theme/              # App theming
│           │   │       ├── Color.kt        # Color definitions
│           │   │       ├── Type.kt         # Typography
│           │   │       └── Theme.kt        # Theme setup
│           │   └── utils/                  # Utility classes
│           │       ├── FaceAnalyzer.kt     # ML Kit integration
│           │       ├── ImageProcessor.kt   # Image handling
│           │       └── AnalysisEngine.kt   # Analysis logic
│           └── res/
│               ├── values/
│               │   ├── strings.xml         # String resources
│               │   └── themes.xml          # Theme resources
│               └── xml/
│                   ├── backup_rules.xml
│                   └── data_extraction_rules.xml
├── build.gradle.kts                        # Project-level Gradle
├── settings.gradle.kts                     # Gradle settings
├── gradle.properties                       # Gradle properties
├── gradlew                                 # Gradle wrapper (Unix)
├── gradlew.bat                             # Gradle wrapper (Windows)
└── README.md                               # This file
```

## 🏗️ Architecture

This project follows **MVVM (Model-View-ViewModel)** architecture pattern:

### Layers:
1. **UI Layer**: Jetpack Compose screens and components
2. **ViewModel Layer**: State management and business logic
3. **Repository Layer**: Data access abstraction
4. **Data Layer**: Room database and data models
5. **Utility Layer**: ML Kit, image processing, analysis engine

### Data Flow:
```
UI (Compose) ↔ ViewModel ↔ Repository ↔ Database/Room
                    ↓
                 Utils (ML Kit, Image Processing)
```

## 🎯 Key Screens

1. **Home Screen**: Welcome page with app features and quick actions
2. **Scan Screen**: Camera interface with real-time face detection and multi-angle capture
3. **Results Screen**: Detailed analysis results with scores, concerns, and recommendations
4. **History Screen**: List of past scans with trend tracking
5. **Profile Screen**: User profile and app information

## 🔧 Build Commands

```bash
# Clean build
./gradlew clean

# Build debug variant
./gradlew assembleDebug

# Install debug APK
./gradlew installDebug

# Run unit tests
./gradlew test

# Generate test coverage report
./gradlew jacocoTestReport

# Lint check
./gradlew lint

# Build release (requires signing configuration)
./gradlew assembleRelease
```

## 📱 Permissions

The app requires the following permissions:
- **CAMERA**: Required for face scanning
- **WRITE_EXTERNAL_STORAGE** (API ≤28): For saving scan images

## 🔒 Privacy & Security

- All scan data is stored locally on the device
- No data is transmitted to external servers
- Images are stored in app-private directory
- Full data backup rules configured
- ProGuard rules for release builds

## 🐛 Troubleshooting

### Gradle Sync Issues
```bash
./gradlew clean
# Delete .gradle folder and rebuild
```

### Camera Not Working
- Ensure device has camera permission granted
- Check if device/emulator has camera hardware
- Verify AndroidManifest.xml has camera permissions

### Build Errors
- Ensure Java 17 is configured
- Check Android SDK tools are installed
- Verify internet connection for dependency download

## 📄 License

Copyright © 2024 Ziva Clinic. All rights reserved.

This project is proprietary software developed for Ziva Clinic.

## 🤝 Contributing

This is a proprietary project for Ziva Clinic. For internal contributions:

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -m "Add your feature"`
3. Push to branch: `git push origin feature/your-feature`
4. Create a Pull Request

## 📧 Contact

For questions or support:
- Website: [https://www.zivaclinic.com](https://www.zivaclinic.com)
- Email: support@zivaclinic.com

## 🙏 Acknowledgments

- **ML Kit**: Google's machine learning SDK for mobile
- **CameraX**: Modern camera API from Android Jetpack
- **Jetpack Compose**: Modern Android UI toolkit
- **Material Design**: Google's design system

---

<div align="center">
  <p>Made with ❤️ by Ziva Clinic Team</p>
  <p><strong>Version 1.0.0</strong></p>
</div>
