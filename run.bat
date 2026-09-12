@echo off
setlocal
set "JAVA_HOME=%USERPROFILE%\.jdks\temurin-25.0.4.1"
if not exist "%JAVA_HOME%\bin\javac.exe" set "JAVA_HOME=%JAVA_HOME_OVERRIDE%"
chcp 65001 >nul
if not exist out mkdir out
dir /s /b src\*.java > out\sources.txt
"%JAVA_HOME%\bin\javac.exe" -encoding UTF-8 -d out @out\sources.txt || exit /b 1
"%JAVA_HOME%\bin\java.exe" -Dstdout.encoding=UTF-8 -cp out br.unifor.trocatroca.Main
