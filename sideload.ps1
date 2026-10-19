<#
.SYNOPSIS
    DigiWear Automated Sideloading Assistant
    Easily connect and install the Wear OS app and Phone Companion app via ADB.
#>

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

function Write-Header {
    Clear-Host
    Write-Host '======================================================================' -ForegroundColor Cyan
    Write-Host '            DIGIWEAR SIDELOADING & INSTALLATION WIZARD 📟⌚           ' -ForegroundColor Yellow -BackgroundColor Black
    Write-Host '======================================================================' -ForegroundColor Cyan
    Write-Host '  Dual-Device Virtual Pet System for Wear OS and Android Companion    ' -ForegroundColor Gray
    Write-Host '======================================================================' -ForegroundColor Cyan
    Write-Host ''
}

function Find-Adb {
    # 1. Check if adb is directly on PATH
    $cmd = Get-Command 'adb' -ErrorAction SilentlyContinue
    if ($cmd) {
        return $cmd.Source
    }

    # 2. Check standard Android SDK locations
    $candidatePaths = @(
        "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe",
        "$env:ANDROID_HOME\platform-tools\adb.exe",
        "$env:ANDROID_SDK_ROOT\platform-tools\adb.exe",
        'C:\Android\sdk\platform-tools\adb.exe',
        'C:\Program Files\Android\platform-tools\adb.exe',
        "$HOME\Library\Android\sdk\platform-tools\adb",
        "$HOME\Android\Sdk\platform-tools\adb",
        '/usr/bin/adb',
        '/usr/local/bin/adb'
    )

    foreach ($path in $candidatePaths) {
        if (Test-Path $path) {
            $toolDir = Split-Path $path -Parent
            $env:PATH = "$toolDir;$($env:PATH)"
            return $path
        }
    }

    return $null
}

function Get-ApkPath {
    param(
        [string]$moduleName
    )
    $scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

    # 1. Check release_bin folder (distributed release binaries)
    if ($moduleName -eq "wear-os-app") {
        $namedRel = Join-Path $scriptDir "release_bin\DigiWear-WearOS-release.apk"
        if (Test-Path $namedRel) { return $namedRel }
    } elseif ($moduleName -eq "phone-companion-app") {
        $namedRel = Join-Path $scriptDir "release_bin\DigiWear-Companion-release.apk"
        if (Test-Path $namedRel) { return $namedRel }
    }

    $binRelease = Join-Path $scriptDir "release_bin\$($moduleName)-release.apk"
    if (Test-Path $binRelease) { return $binRelease }

    $binUnsigned = Join-Path $scriptDir "release_bin\$($moduleName)-release-unsigned.apk"
    if (Test-Path $binUnsigned) { return $binUnsigned }

    $binDebug = Join-Path $scriptDir "release_bin\$($moduleName)-debug.apk"
    if (Test-Path $binDebug) { return $binDebug }

    # 2. Check local Gradle build outputs
    $releaseApk = Join-Path $scriptDir "$moduleName\build\outputs\apk\release\$($moduleName)-release.apk"
    if (Test-Path $releaseApk) { return $releaseApk }

    $unsignedApk = Join-Path $scriptDir "$moduleName\build\outputs\apk\release\$($moduleName)-release-unsigned.apk"
    if (Test-Path $unsignedApk) { return $unsignedApk }

    $debugApk = Join-Path $scriptDir "$moduleName\build\outputs\apk\debug\$($moduleName)-debug.apk"
    if (Test-Path $debugApk) { return $debugApk }

    return $null
}

function Show-Guide {
    Write-Host ''
    Write-Host '----------------------------------------------------------------------' -ForegroundColor Yellow
    Write-Host '  HOW TO ENABLE DEVELOPER OPTIONS AND WIRELESS DEBUGGING              ' -ForegroundColor Yellow
    Write-Host '----------------------------------------------------------------------' -ForegroundColor Yellow
    Write-Host ''
    Write-Host '[ Wear OS Smartwatch (Pixel Watch, Galaxy Watch, etc.) ]' -ForegroundColor Green
    Write-Host '  1. On your watch, open Settings -> System -> About -> Versions.' -ForegroundColor White
    Write-Host '  2. Tap "Build Number" 7 times rapidly until you see "You are a developer!"' -ForegroundColor White
    Write-Host '  3. Go back to Settings -> Developer Options.' -ForegroundColor White
    Write-Host '  4. Turn ON "ADB Debugging".' -ForegroundColor White
    Write-Host '  5. Turn ON "Wireless Debugging" (or "Debug over Wi-Fi").' -ForegroundColor White
    Write-Host '  6. Make sure your watch and PC are connected to the SAME Wi-Fi network.' -ForegroundColor White
    Write-Host '  7. Note the IP address and Port (e.g. 192.168.1.50:5555).' -ForegroundColor White
    Write-Host ''
    Write-Host '[ Android Phone Companion ]' -ForegroundColor Green
    Write-Host '  1. On your phone, go to Settings -> About Phone.' -ForegroundColor White
    Write-Host '  2. Tap "Build Number" 7 times.' -ForegroundColor White
    Write-Host '  3. Go to Settings -> System -> Developer Options.' -ForegroundColor White
    Write-Host '  4. Turn ON "USB Debugging" (connect via USB cable) or "Wireless Debugging".' -ForegroundColor White
    Write-Host '----------------------------------------------------------------------' -ForegroundColor Yellow
    Write-Host ''
    Read-Host 'Press [Enter] to return to the menu'
}

function Pair-WirelessDevice {
    param([string]$adb)
    Write-Host ''
    Write-Host '--- PAIR WEAR OS DEVICE (Android 11+ Pairing Code) ---' -ForegroundColor Cyan
    Write-Host 'On your watch: Settings -> Developer Options -> Wireless Debugging -> Pair new device.' -ForegroundColor Gray
    Write-Host ''
    $pairIpPort = Read-Host 'Enter Watch IP:Port (e.g. 192.168.1.50:42135)'
    if ([string]::IsNullOrWhiteSpace($pairIpPort)) {
        Write-Host 'Pairing aborted.' -ForegroundColor Yellow
        return
    }
    $pairCode = Read-Host 'Enter 6-digit Wi-Fi Pairing Code'
    if ([string]::IsNullOrWhiteSpace($pairCode)) {
        Write-Host 'Pairing aborted.' -ForegroundColor Yellow
        return
    }

    Write-Host "Executing: adb pair $pairIpPort $pairCode" -ForegroundColor Gray
    & $adb pair $pairIpPort $pairCode
    Write-Host ''
    Read-Host 'Press [Enter] to continue'
}

function Connect-WirelessDevice {
    param([string]$adb)
    Write-Host ''
    Write-Host '--- CONNECT TO WIRELESS WEAR OS WATCH ---' -ForegroundColor Cyan
    Write-Host 'On your watch: Settings -> Developer Options -> Wireless Debugging (Main Screen)' -ForegroundColor Gray
    $ipPort = Read-Host 'Enter Watch IP:Port (e.g. 192.168.1.50:5555)'
    if ([string]::IsNullOrWhiteSpace($ipPort)) {
        Write-Host 'Connection aborted.' -ForegroundColor Yellow
        return $null
    }

    Write-Host "Connecting to $ipPort..." -ForegroundColor Gray
    & $adb connect $ipPort
    return $ipPort
}

function Install-Apk {
    param(
        [string]$adb,
        [string]$apkPath,
        [string]$deviceName,
        [string]$targetDesc
    )

    if (-not (Test-Path $apkPath)) {
        Write-Host "Error: APK not found at $apkPath" -ForegroundColor Red
        Write-Host 'Run option [8] to build the APK first!' -ForegroundColor Yellow
        return $false
    }

    $fileName = Split-Path $apkPath -Leaf
    $sizeMb = [math]::Round(((Get-Item $apkPath).Length / 1MB), 2)
    Write-Host ''
    Write-Host "Installing $targetDesc ($fileName, ${sizeMb} MB)..." -ForegroundColor Cyan

    $cmdArgs = @()
    if (![string]::IsNullOrWhiteSpace($deviceName)) {
        $cmdArgs += @('-s', $deviceName)
    }
    $cmdArgs += @('install', '-r', '-d', $apkPath)

    & $adb $cmdArgs
    if ($LASTEXITCODE -eq 0) {
        Write-Host "SUCCESS: Successfully installed $targetDesc!" -ForegroundColor Green
        return $true
    } else {
        Write-Host "FAILED: Installation failed (Exit code: $LASTEXITCODE)." -ForegroundColor Red
        Write-Host 'Tip: If you have an existing older build, try uninstalling it first on the device.' -ForegroundColor Yellow
        return $false
    }
}

function Select-TargetDevice {
    param([string]$adb)
    $lines = & $adb devices -l | Where-Object { $_ -match '\bdevice\b' -and $_ -notmatch 'List of devices' }
    if (!$lines -or $lines.Count -eq 0) {
        Write-Host 'No connected ADB devices found!' -ForegroundColor Red
        Write-Host 'Please connect via USB or Wi-Fi first.' -ForegroundColor Yellow
        return $null
    }

    if ($lines.Count -eq 1) {
        $deviceId = ($lines[0] -split '\s+')[0]
        return $deviceId
    }

    Write-Host ''
    Write-Host 'Multiple devices detected:' -ForegroundColor Cyan
    for ($i = 0; $i -lt $lines.Count; $i++) {
        Write-Host "  [$($i+1)] $($lines[$i])" -ForegroundColor White
    }
    $choice = Read-Host "Select target device (1-$($lines.Count))"
    $idx = [int]$choice - 1
    if ($idx -ge 0 -and $idx -lt $lines.Count) {
        return ($lines[$idx] -split '\s+')[0]
    }
    return $null
}

# --- Main Program Loop ---
$adbPath = Find-Adb

while ($true) {
    Write-Header

    if ($adbPath) {
        Write-Host "ADB Status: FOUND ($adbPath)" -ForegroundColor Green
    } else {
        Write-Host 'ADB Status: NOT FOUND in PATH or standard Android SDK directories!' -ForegroundColor Red
        Write-Host 'Please install Android Studio / Platform-Tools or set your PATH.' -ForegroundColor Yellow
    }
    Write-Host ''

    Write-Host '  [1] Install Wear OS Smartwatch App (Wi-Fi Wireless Debugging)' -ForegroundColor White
    Write-Host '  [2] Install Wear OS Smartwatch App (USB / Connected Device)' -ForegroundColor White
    Write-Host '  [3] Install Phone Companion App (USB / Wireless)' -ForegroundColor White
    Write-Host '  [4] Full Dual-Device Sideload (Smartwatch + Phone)' -ForegroundColor White
    Write-Host '  [5] Check Connected ADB Devices' -ForegroundColor White
    Write-Host '  [6] Pair New Wireless Watch (Wi-Fi Pairing Code)' -ForegroundColor White
    Write-Host '  [7] View Setup Guide (Enable Developer Options & Wi-Fi Debugging)' -ForegroundColor White
    Write-Host '  [8] Rebuild Release APKs from Source' -ForegroundColor White
    Write-Host '  [0] Exit' -ForegroundColor Gray
    Write-Host ''

    $choice = Read-Host 'Select an option [0-8]'

    if ($choice -eq '0') {
        Write-Host 'Goodbye! May your Digimon digivolve to greatness! 📟✨' -ForegroundColor Cyan
        break
    }

    if (!$adbPath -and $choice -ne '7' -and $choice -ne '8') {
        Write-Host 'ADB is required for this operation. Please check option [7].' -ForegroundColor Red
        Read-Host 'Press [Enter] to continue'
        continue
    }

    switch ($choice) {
        '1' {
            Connect-WirelessDevice -adb $adbPath
            $target = Select-TargetDevice -adb $adbPath
            if ($target) {
                $apk = Get-ApkPath 'wear-os-app'
                Install-Apk -adb $adbPath -apkPath $apk -deviceName $target -targetDesc 'Wear OS Smartwatch App'
            }
            Read-Host 'Press [Enter] to continue'
        }
        '2' {
            $target = Select-TargetDevice -adb $adbPath
            if ($target) {
                $apk = Get-ApkPath 'wear-os-app'
                Install-Apk -adb $adbPath -apkPath $apk -deviceName $target -targetDesc 'Wear OS Smartwatch App'
            }
            Read-Host 'Press [Enter] to continue'
        }
        '3' {
            $target = Select-TargetDevice -adb $adbPath
            if ($target) {
                $apk = Get-ApkPath 'phone-companion-app'
                Install-Apk -adb $adbPath -apkPath $apk -deviceName $target -targetDesc 'Phone Companion App'
            }
            Read-Host 'Press [Enter] to continue'
        }
        '4' {
            Write-Host '--- DUAL-DEVICE INSTALLATION ---' -ForegroundColor Cyan
            Write-Host "First, let's install the Wear OS Smartwatch App:" -ForegroundColor Yellow
            $watchTarget = Select-TargetDevice -adb $adbPath
            if ($watchTarget) {
                $apkWatch = Get-ApkPath 'wear-os-app'
                Install-Apk -adb $adbPath -apkPath $apkWatch -deviceName $watchTarget -targetDesc 'Wear OS Smartwatch App'
            }

            Write-Host ''
            Write-Host "Next, let's install the Phone Companion App:" -ForegroundColor Yellow
            $phoneTarget = Select-TargetDevice -adb $adbPath
            if ($phoneTarget) {
                $apkPhone = Get-ApkPath 'phone-companion-app'
                Install-Apk -adb $adbPath -apkPath $apkPhone -deviceName $phoneTarget -targetDesc 'Phone Companion App'
            }
            Read-Host 'Press [Enter] to continue'
        }
        '5' {
            Write-Host ''
            Write-Host 'Connected ADB Devices:' -ForegroundColor Cyan
            & $adbPath devices -l
            Write-Host ''
            Read-Host 'Press [Enter] to continue'
        }
        '6' {
            Pair-WirelessDevice -adb $adbPath
        }
        '7' {
            Show-Guide
        }
        '8' {
            Write-Host ''
            Write-Host 'Rebuilding Release APKs...' -ForegroundColor Cyan
            $scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
            Push-Location $scriptDir
            if (Test-Path 'scratch\run_gradle.bat') {
                cmd.exe /c "scratch\run_gradle.bat assembleRelease"
            } else {
                cmd.exe /c "gradlew.bat assembleRelease"
            }
            Pop-Location
            Read-Host 'Press [Enter] to continue'
        }
        default {
            Write-Host 'Invalid option. Please choose between 0 and 8.' -ForegroundColor Yellow
            Start-Sleep -Seconds 1
        }
    }
}
