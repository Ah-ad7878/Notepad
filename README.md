Notepad App (v1.3.0)
A modern, intuitive, and feature-rich Android application designed to help users capture thoughts, manage tasks, and keep notes organized seamlessly. Built using Java and XML in Android Studio following modern Material Design guidelines.

🌟 Key Features
Multi-Format Document Export (New in v1.3.0): Convert and export notes directly to PDF and DOCX (Microsoft Word) formats.

Instant Document Sharing (New in v1.3.0): Seamlessly share exported files via WhatsApp, Gmail, or cloud storage using Android's native Share Intent.

Automated Account Cleanup (New in v1.3.0): Enforces data privacy and hygiene by automatically purging accounts and associated data after 30 days of inactivity.

Biometric Authentication: One-touch fingerprint login integration using the Android Biometric API for secure and rapid app access.

Integrated Customer Care Support: In-app support hub allowing users to submit queries, report issues, or provide feedback directly.

Modern UI/UX Design: Refined user interface featuring custom gradients, soft elevation card shadows, rounded button styling, and polished typography.

User Authentication: Sleek Sign-Up and Login screens with real-time input validation, Firebase Authentication, and accessibility controls.

🚀 What's New in Version 1.3.0
📄 PDF & DOCX Generator: Added native PdfDocument and Apache POI integrations to convert text notes into shareable document formats.

📤 File Sharing Hub: Integrated FileProvider architecture to handle secure cross-app file transfers.

🧹 Inactive Account Cleanup: Implemented 30-day account auto-deletion logic for enhanced database security and privacy management.

🔐 Biometric UI Enhancements: Updated biometric login button with custom rounded shape drawables and improved touch feedback.

🐛 Bug Fixes & Optimizations: Resolved file path permissions for Android 11+ devices and optimized memory management during heavy document generation.

🛠️ Tech Stack & Dependencies
Language: Java

UI Design: Android XML Layouts, Material Design Components 3

Security & Auth: Android Biometric API (androidx.biometric), Firebase Authentication

Document Processing: Apache POI (poi-ooxml), Native Android Graphics & PDF API

IDE: Android Studio

Target SDK: 36

Minimum SDK: 24 (Android 7.0)
