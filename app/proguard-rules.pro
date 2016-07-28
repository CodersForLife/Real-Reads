# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Users\Myrefers\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
#-optimizations !code/simplification/arithmetic
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-keepattributes *Annotation*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-dontwarn okio.**
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-keepattributes *Annotation*
-dontwarn android.net.http.**
-dontwarn sun.misc.**
-dontnote libcore.icu.ICU
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepclassmembers class **.R$* {
    public static <fields>;
}
#https://stackoverflow.com/questions/18978706/obfuscate-android-support-v7-widget-gridlayout-issue
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-dontwarn android.os.**
-keep class android.os.**
-keep interface android.os.**
# support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }
# support design
#@link http://stackoverflow.com/a/31028536
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
#error : Note: the configuration refers to the unknown class 'com.google.vending.licensing.ILicensingService'
#solution : @link http://stackoverflow.com/a/14463528
-dontnote com.google.vending.licensing.ILicensingService
-dontnote **ILicensingService

#-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# http://stackoverflow.com/questions/4525661/android-proguard-cant-find-dynamically-referenced-class-javax-swing
-dontwarn java.awt.**
-dontnote java.awt.**
-dontwarn com.badlogic.gdx.jnigen.**

-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn com.google.gson.**
-dontwarn java.nio.file.**

-keepclassmembers class * implements android.os.Parcelable {
 public <fields>;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keep class com.firebase.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**

