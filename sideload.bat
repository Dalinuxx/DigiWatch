@echo off
title DigiWear Sideloading Assistant
chcp 65001 >nul
cls

:: Check if PowerShell is available
where powershell.exe >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0sideload.ps1" %*
    goto :EOF
)

:: Fallback if PowerShell is somehow not installed
echo ======================================================================
echo             DIGIWEAR SIDELOADING ASSISTANT (Batch Mode)
echo ======================================================================
echo.

set "ADB_CMD=adb"
where adb >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    if exist "%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe" (
        set "ADB_CMD=%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe"
    ) else if exist "%ANDROID_HOME%\platform-tools\adb.exe" (
        set "ADB_CMD=%ANDROID_HOME%\platform-tools\adb.exe"
    ) else (
        echo [ERROR] ADB not found. Please install Android Platform-Tools.
        pause
        exit /b 1
    )
)

:MENU
cls
echo ======================================================================
echo             DIGIWEAR SIDELOADING ASSISTANT (Batch Mode)
echo ======================================================================
echo.
echo   [1] Install Wear OS Watch App (Wi-Fi)
echo   [2] Install Wear OS Watch App (USB/Connected)
echo   [3] Install Phone Companion App
echo   [4] Check Connected Devices
echo   [0] Exit
echo.
set /p CHOICE="Select an option [0-4]: "

if "%CHOICE%"=="0" exit /b 0
if "%CHOICE%"=="1" (
    set /p WATCH_IP="Enter Watch IP:Port (e.g. 192.168.1.50:5555): "
    "%ADB_CMD%" connect %WATCH_IP%
    if exist "%~dp0release_bin\DigiWear-WearOS-release.apk" (
        "%ADB_CMD%" install -r -d "%~dp0release_bin\DigiWear-WearOS-release.apk"
    ) else if exist "%~dp0release_bin\wear-os-app-release.apk" (
        "%ADB_CMD%" install -r -d "%~dp0release_bin\wear-os-app-release.apk"
    ) else (
        "%ADB_CMD%" install -r -d "%~dp0wear-os-app\build\outputs\apk\release\wear-os-app-release.apk"
    )
    pause
    goto MENU
)
if "%CHOICE%"=="2" (
    if exist "%~dp0release_bin\DigiWear-WearOS-release.apk" (
        "%ADB_CMD%" install -r -d "%~dp0release_bin\DigiWear-WearOS-release.apk"
    ) else if exist "%~dp0release_bin\wear-os-app-release.apk" (
        "%ADB_CMD%" install -r -d "%~dp0release_bin\wear-os-app-release.apk"
    ) else (
        "%ADB_CMD%" install -r -d "%~dp0wear-os-app\build\outputs\apk\release\wear-os-app-release.apk"
    )
    pause
    goto MENU
)
if "%CHOICE%"=="3" (
    if exist "%~dp0release_bin\DigiWear-Companion-release.apk" (
        "%ADB_CMD%" install -r -d "%~dp0release_bin\DigiWear-Companion-release.apk"
    ) else if exist "%~dp0release_bin\phone-companion-app-release.apk" (
        "%ADB_CMD%" install -r -d "%~dp0release_bin\phone-companion-app-release.apk"
    ) else (
        "%ADB_CMD%" install -r -d "%~dp0phone-companion-app\build\outputs\apk\release\phone-companion-app-release.apk"
    )
    pause
    goto MENU
)
if "%CHOICE%"=="4" (
    "%ADB_CMD%" devices -l
    pause
    goto MENU
)
goto MENU
