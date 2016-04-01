# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ryan/Develop/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# fir.flight
-keep class io.github.ryanhoo.firFlight.GlideConfiguration

# Common
-dontnote com.google.**
-dontnote android.net.**
-dontnote org.apache.**
-dontnote android.support.**
-dontnote com.android.**
-dontnote sun.**
-dontnote java.**
-dontnote org.robovm.appple.**

# Gson
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-dontnote com.google.gson.**
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
# -keep class com.google.gson.examples.android.model.** { *; }

# okio
-dontnote okhttp3.**
-dontnote okio.**
-dontwarn okio.**

# Retrofit
-dontnote retrofit2.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Butter Knife
-dontnote butterknife.**
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Fabric.io
-dontnote io.fabric.**
# Crashlytics
-keepattributes SourceFile,LineNumberTable,*Annotation*
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
