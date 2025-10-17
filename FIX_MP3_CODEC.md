# 🔧 MP3 Codec Not Loading - FINAL FIX

## Problem
Even when you run `mvn exec:java "-Dexec.classpathScope=runtime"`, the MP3 codec (mp3spi) is **still not being loaded**.

Error:
```
UnsupportedAudioFileException: File of unsupported format
⚠️  WARNING: MP3 codec (mp3spi) not found!
```

## Why This Happens
The `exec-maven-plugin` configuration in your `pom.xml` is set to `<classpathScope>runtime</classpathScope>` by default, but it's not being applied correctly when you run the command.

---

## ✅ SOLUTION 1: Use the Batch File (EASIEST)

I've updated `run.bat` to use the correct command:

```powershell
.\run.bat
```

This will automatically use the runtime classpath.

---

## ✅ SOLUTION 2: Run Correct Maven Command

Make sure you're using **QUOTES** around the parameter:

```powershell
mvn exec:java "-Dexec.classpathScope=runtime"
```

NOT this (without quotes):
```powershell
mvn exec:java -Dexec.classpathScope=runtime  # WRONG in PowerShell
```

---

## ✅ SOLUTION 3: Build JAR and Run Directly (BEST for Production)

```powershell
# Build the JAR with all dependencies
mvn clean package

# Run the JAR directly
java -jar target\bhojpurri-app-1.0.0.jar
```

But wait - this won't include dependencies. Let me add a proper plugin...

---

## ✅ SOLUTION 4: Add Maven Shade Plugin (RECOMMENDED)

Add this to your `pom.xml` in the `<build><plugins>` section:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.5.1</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>com.bhojpurri.MainApp</mainClass>
                    </transformer>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                </transformers>
                <filters>
                    <filter>
                        <artifact>*:*</artifact>
                        <excludes>
                            <exclude>META-INF/*.SF</exclude>
                            <exclude>META-INF/*.DSA</exclude>
                            <exclude>META-INF/*.RSA</exclude>
                        </excludes>
                    </filter>
                </filters>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Then build and run:
```powershell
mvn clean package
java -jar target\bhojpurri-app-1.0.0.jar
```

This creates a "fat JAR" with ALL dependencies included!

---

## ✅ SOLUTION 5: Quick Test - Manual Classpath

```powershell
# Build first
mvn clean compile

# Run with explicit classpath
java -cp "target/classes;%USERPROFILE%\.m2\repository\org\json\json\20231013\json-20231013.jar;%USERPROFILE%\.m2\repository\com\googlecode\soundlibs\mp3spi\1.9.5.4\mp3spi-1.9.5.4.jar;%USERPROFILE%\.m2\repository\com\googlecode\soundlibs\jlayer\1.0.1.4\jlayer-1.0.1.4.jar;%USERPROFILE%\.m2\repository\com\googlecode\soundlibs\tritonus-share\0.3.7.4\tritonus-share-0.3.7.4.jar;%USERPROFILE%\.m2\repository\org\slf4j\slf4j-api\2.0.9\slf4j-api-2.0.9.jar;%USERPROFILE%\.m2\repository\org\slf4j\slf4j-simple\2.0.9\slf4j-simple-2.0.9.jar" com.bhojpurri.MainApp
```

---

## 🎯 What To Do RIGHT NOW:

### Option A: Quick Fix (Use run.bat)
```powershell
.\run.bat
```

### Option B: Add Shade Plugin (Best Long-term)
1. Add the shade plugin to `pom.xml` (see SOLUTION 4 above)
2. Run:
   ```powershell
   mvn clean package
   java -jar target\bhojpurri-app-1.0.0.jar
   ```

---

## 📊 How to Verify MP3 Codec is Loaded

You should see this when the app starts:
```
🔍 Checking audio codec providers:
   - com.sun.media.sound.AiffFileReader
   - com.sun.media.sound.AuFileReader
   - javazoom.spi.mpeg.sampled.file.MpegAudioFileReader  ← THIS ONE!
✅ MP3 codec loaded successfully
```

If you see:
```
⚠️ WARNING: MP3 codec (mp3spi) not found!
```

Then the classpath is WRONG.

---

## TL;DR

**The MP3 file downloaded successfully (50KB), but Java can't play it because mp3spi isn't loaded.**

**EASIEST FIX**: Run `.\run.bat` instead of the maven command directly.
