# 🚀 Android Wake-on-LAN (WOL) Automator

A lightweight, clean, and automated Android application built with Kotlin that allows you to turn on your computer remotely from your local network using **Wake-on-LAN (WOL) Magic Packets**.

## ✨ Features

- **One-Tap Automation:** After the initial setup, just tap the app icon. It will automatically send the magic packet and close itself within 3 seconds.
- **Smart 3-Second Safe Guard:** Displays a 3-second countdown upon opening. If you need to change your IP or MAC address, simply tap the cancel button during the countdown to edit your credentials.
- **Persistent Storage:** Uses Android's `SharedPreferences` to safely store your IP and MAC addresses locally so you never have to re-enter them.
- **Asynchronous Networking:** Network operations run smoothly on a background thread using Kotlin's lightweight threading to prevent UI freezing.

## 🛠️ How It Works

1. **First Launch (Setup):** Enter your computer's local IP address and MAC address, then tap **Save**. The app will send the first wakeup signal and close.
2. **Subsequent Launches (Automation):** Every time you open the app, it triggers the countdown, sends the signal to boot your PC, and shuts itself down completely.
3. **Updating Credentials:** If your IP or MAC changes, open the app and tap **"CANCEL AUTO-CLOSE (3s)"** before the timer ends. This stops the automation and allows you to save new details.

## 📱 Technical Overview

- **Language:** Kotlin
- **Architecture:** AppCompatActivity (Using traditional Android XML Layouts)
- **Networking:** `DatagramSocket` & `DatagramPacket` over UDP Port 9
- **Required Permissions:**
```xml
<uses-permission android:name="android.permission.INTERNET" />

🚀 Installation & Setup
1-Clone the Repository: Clone this repository into Android Studio.

2-Enable WOL on PC: Ensure your computer has Wake-on-LAN enabled in both the BIOS/UEFI settings and your Network Adapter properties.

3-Connect to Wi-Fi: Connect your Android device to the same local network (Wi-Fi) as your PC.

4-Run the App: Build, run, and enjoy one-click computer startup!
