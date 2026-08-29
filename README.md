# 📝 Notepad App (v1.3.0)

A modern, intuitive, and feature-rich Android application designed to help users capture thoughts, manage tasks, and keep notes organized seamlessly. Built using Java and XML in Android Studio following modern Material Design guidelines.

---

## 🌟 Key Features

* **Automated Account Cleanup (New in v1.3.0):** Enforces data privacy and system hygiene by automatically purging accounts and associated data after 30 days of inactivity.
* **Self-Service Password Recovery (New in v1.3.0):** Dedicated Forget Password module that sends instant password reset links to registered email addresses via Firebase Authentication.
* **Secure User Authentication:** Sleek Sign-Up and Login screens with real-time input validation and Firebase Authentication integration.
* **Biometric Authentication:** One-touch fingerprint login integration using the Android `androidx.biometric` API paired with custom oval drawables for rapid, secure app access.
* **Integrated Customer Support:** Direct in-app support service allowing users to submit queries, report issues, or connect via support channels instantly.
* **Modern Material UI/UX:** Refined layout featuring custom gradients, soft elevation card shadows, rounded action buttons, and polished typography.

---

## 🚀 What's New in Version 1.3.0

* 🔑 **Forget Password Integration:** Built a dedicated `forget_password` activity with automated Firebase password reset emails and custom spam-folder warning dialogs.
* 👆 **Biometric UI & Persistence:** Added standalone rounded biometric trigger button and auto-prompt logic linked with saved user preferences.
* 🧹 **Inactive Account Cleanup:** Implemented 30-day account auto-deletion logic for enhanced database security and privacy management.
* 🐛 **Bug Fixes & Optimizations:** Resolved file path permissions for Android 11+ devices, fixed SDK target issues, and optimized memory management during heavy document generation.

---

## 🛠️ Tech Stack & Dependencies

* **Language:** Java
* **UI Framework:** Android XML Layouts, Material Design Components 3
* **Security & Auth:** Android Biometric API (`androidx.biometric`), Firebase Authentication (Email/Password & Password Reset)
* **Document Processing:** Apache POI (`poi-ooxml`), Native Android Graphics & `PdfDocument` API
* **IDE:** Android Studio
* **Target SDK:** 36
* **Minimum SDK:** 24 (Android 7.0)
