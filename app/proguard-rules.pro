# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontusemixedcaseclassnames

# Keep the support library classes
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
