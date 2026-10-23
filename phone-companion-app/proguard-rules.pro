# DigiWear Phone Companion ProGuard / R8 Rules

# Kotlin & Serialization
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-dontnote kotlinx.serialization.SerializationKt
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable class *;
}
-keepclassmembers class **$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers enum * {
    kotlinx.serialization.KSerializer serializer(...);
}

# DigiWear Core Models & Companion Data
-keep class com.digiwear.core.** { *; }
-keep class com.digiwear.companion.data.** { *; }

# AndroidX Room
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# Cryptography, BouncyCastle, Conscrypt & Wireless ADB
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**
-keep class org.conscrypt.** { *; }
-dontwarn org.conscrypt.**
-keep class io.github.muntashirakon.adb.** { *; }
-dontwarn io.github.muntashirakon.adb.**
-keep class dadb.** { *; }
-dontwarn dadb.**
-keep class javax.crypto.** { *; }
-keep class java.security.** { *; }

# MLKit Barcode Scanning & Google Play Services Vision
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**
-keep class com.google.android.gms.vision.** { *; }
-dontwarn com.google.android.gms.vision.**

# Jetpack Compose & Navigation
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.navigation.** { *; }
