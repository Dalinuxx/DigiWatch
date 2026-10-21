#!/usr/bin/env bash
# ======================================================================
#            DIGIWEAR SIDELOADING & INSTALLATION SCRIPT
# ======================================================================
# Supports Linux and macOS with auto-detection of Android SDK & ADB.
# Supports interactive mode and CLI flags (--watch, --phone, --all, --rebuild, --help).

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Color codes
CYAN='\033[0;36m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Auto-detect ADB
find_adb() {
    if command -v adb >/dev/null 2>&1; then
        echo "$(command -v adb)"
        return
    fi

    local candidate_paths=(
        "$HOME/Library/Android/sdk/platform-tools/adb"
        "$HOME/Android/Sdk/platform-tools/adb"
        "/usr/local/bin/adb"
        "/opt/homebrew/bin/adb"
        "/usr/bin/adb"
    )

    for p in "${candidate_paths[@]}"; do
        if [ -x "$p" ]; then
            echo "$p"
            return
        fi
    done

    echo ""
}

ADB_CMD=$(find_adb)

get_apk() {
    local module="$1"
    local rel="$SCRIPT_DIR/$module/build/outputs/apk/release/$module-release.apk"
    local bin_named=""
    if [ "$module" = "wear-os-app" ]; then
        bin_named="$SCRIPT_DIR/release_bin/DigiWear-WearOS-release.apk"
    elif [ "$module" = "phone-companion-app" ]; then
        bin_named="$SCRIPT_DIR/release_bin/DigiWear-Companion-release.apk"
    fi
    local bin_rel="$SCRIPT_DIR/release_bin/$module-release.apk"
    local deb="$SCRIPT_DIR/$module/build/outputs/apk/debug/$module-debug.apk"
    local bin_deb="$SCRIPT_DIR/release_bin/$module-debug.apk"

    # Prefer newest release binary between build/outputs and release_bin
    if [ -f "$rel" ] && [ -n "$bin_named" ] && [ -f "$bin_named" ]; then
        if [ "$rel" -nt "$bin_named" ]; then
            echo "$rel"
        else
            echo "$bin_named"
        fi
    elif [ -f "$rel" ]; then
        echo "$rel"
    elif [ -n "$bin_named" ] && [ -f "$bin_named" ]; then
        echo "$bin_named"
    elif [ -f "$bin_rel" ]; then
        echo "$bin_rel"
    elif [ -f "$deb" ]; then
        echo "$deb"
    elif [ -f "$bin_deb" ]; then
        echo "$bin_deb"
    else
        echo ""
    fi
}

sync_release_bins() {
    mkdir -p "$SCRIPT_DIR/release_bin"
    local wear_rel="$SCRIPT_DIR/wear-os-app/build/outputs/apk/release/wear-os-app-release.apk"
    local wear_dbg="$SCRIPT_DIR/wear-os-app/build/outputs/apk/debug/wear-os-app-debug.apk"
    local phone_rel="$SCRIPT_DIR/phone-companion-app/build/outputs/apk/release/phone-companion-app-release.apk"
    local phone_dbg="$SCRIPT_DIR/phone-companion-app/build/outputs/apk/debug/phone-companion-app-debug.apk"

    if [ -f "$wear_rel" ]; then
        cp -f "$wear_rel" "$SCRIPT_DIR/release_bin/DigiWear-WearOS-release.apk"
        cp -f "$wear_rel" "$SCRIPT_DIR/release_bin/wear-os-app-release.apk"
        cp -f "$wear_rel" "$SCRIPT_DIR/release_bin/digiwear-aosp-standard-watch-release.apk"
    fi
    if [ -f "$wear_dbg" ]; then
        cp -f "$wear_dbg" "$SCRIPT_DIR/release_bin/wear-os-app-debug.apk"
    fi
    if [ -f "$phone_rel" ]; then
        cp -f "$phone_rel" "$SCRIPT_DIR/release_bin/DigiWear-Companion-release.apk"
        cp -f "$phone_rel" "$SCRIPT_DIR/release_bin/phone-companion-app-release.apk"
    fi
    if [ -f "$phone_dbg" ]; then
        cp -f "$phone_dbg" "$SCRIPT_DIR/release_bin/phone-companion-app-debug.apk"
    fi
}

install_apk() {
    local target_device="$1"
    local apk_path="$2"
    local desc="$3"

    if [ -z "$apk_path" ] || [ ! -f "$apk_path" ]; then
        echo -e "${RED}Error: APK not found for $desc. Run option [8] or --rebuild first.${NC}"
        return 1
    fi

    echo -e "${CYAN}Installing $desc ($(basename "$apk_path"))...${NC}"
    if [ -n "$target_device" ]; then
        "$ADB_CMD" -s "$target_device" install -r -d -g "$apk_path" || "$ADB_CMD" -s "$target_device" install -r -d "$apk_path"
    else
        "$ADB_CMD" install -r -d -g "$apk_path" || "$ADB_CMD" install -r -d "$apk_path"
    fi
    echo -e "${GREEN}[OK] Successfully installed $desc!${NC}"
}

show_guide() {
    echo ""
    echo -e "${YELLOW}----------------------------------------------------------------------${NC}"
    echo -e "${YELLOW}  HOW TO ENABLE DEVELOPER OPTIONS & WIRELESS / USB DEBUGGING          ${NC}"
    echo -e "${YELLOW}----------------------------------------------------------------------${NC}"
    echo ""
    echo -e "${GREEN}[ 1. Wear OS Smartwatch (Pixel Watch, Galaxy Watch 4/5/6/7/Ultra) ]${NC}"
    echo "  1. On your watch, open Settings -> System -> About -> Versions."
    echo "  2. Tap 'Build Number' 7 times rapidly until you see 'You are a developer!'"
    echo "  3. Go back to Settings -> Developer Options."
    echo "  4. Turn ON 'ADB Debugging' and 'Wireless Debugging' (Debug over Wi-Fi)."
    echo "  5. Make sure your watch and computer are connected to the SAME Wi-Fi network."
    echo "  6. First time on Wear OS 4/5? Use Option [6] to Pair with 6-digit code."
    echo ""
    echo -e "${GREEN}[ 2. Standalone AOSP / Full Android Watch (Kospet, Lokmat, Lemfo, Zeblaze) ]${NC}"
    echo "  1. Open Settings -> System -> About Watch -> tap 'Build Number' 7 times."
    echo "  2. Enable 'USB Debugging' or 'Wireless Debugging'."
    echo "  3. In DigiWear Watch Settings, use 'Display Shape Override: ROUND' if needed,"
    echo "     and connect to the Phone App via 'AOSP LAN Bridge' (TCP 45820 / UDP 45821)."
    echo ""
    echo -e "${GREEN}[ 3. Android Phone Companion ]${NC}"
    echo "  1. On your phone, go to Settings -> About Phone -> tap 'Build Number' 7 times."
    echo "  2. Go to Settings -> System -> Developer Options."
    echo "  3. Turn ON 'USB Debugging' (connect via USB cable) or 'Wireless Debugging'."
    echo -e "${YELLOW}----------------------------------------------------------------------${NC}"
    echo ""
    read -p "Press [Enter] to return to the menu..."
}

# --- CLI Flags ---
if [ $# -gt 0 ]; then
    for arg in "$@"; do
        case "$arg" in
            --help|-h)
                echo "DigiWear Sideloading Script -- Usage:"
                echo "  ./sideload.sh                  Launch interactive sideloading wizard"
                echo "  ./sideload.sh --watch          Install Wear OS / AOSP Watch APK"
                echo "  ./sideload.sh --phone          Install Phone Companion APK"
                echo "  ./sideload.sh --all            Install both Watch and Phone APKs"
                echo "  ./sideload.sh --rebuild        Rebuild Release & Debug APKs and sync release_bin/"
                echo "  ./sideload.sh --rebuild --all  Rebuild APKs and install both Watch and Phone APKs"
                exit 0
                ;;
            --rebuild)
                (cd "$SCRIPT_DIR" && ./gradlew assembleRelease assembleDebug)
                sync_release_bins
                echo -e "${GREEN}[OK] Rebuilt and synced all APKs into release_bin/!${NC}"
                ;;
            --watch)
                install_apk "" "$(get_apk "wear-os-app")" "Wear OS / AOSP Smartwatch App"
                ;;
            --phone)
                install_apk "" "$(get_apk "phone-companion-app")" "Phone Companion App"
                ;;
            --all)
                install_apk "" "$(get_apk "wear-os-app")" "Wear OS / AOSP Smartwatch App"
                install_apk "" "$(get_apk "phone-companion-app")" "Phone Companion App"
                ;;
        esac
    done
    exit 0
fi

while true; do
    clear
    echo -e "${CYAN}======================================================================${NC}"
    echo -e "${YELLOW}            DIGIWEAR SIDELOADING & INSTALLATION WIZARD            ${NC}"
    echo -e "${CYAN}======================================================================${NC}"
    echo -e "  500-Species Dual-Device Virtual Pet for Wear OS, AOSP & Android     "
    echo -e "${CYAN}======================================================================${NC}"
    echo ""

    if [ -n "$ADB_CMD" ]; then
        echo -e "${GREEN}ADB Status: FOUND ($ADB_CMD)${NC}"
    else
        echo -e "${RED}ADB Status: NOT FOUND in PATH or standard Android SDK directories!${NC}"
        echo -e "${YELLOW}Please install Android Platform-Tools or set your PATH.${NC}"
    fi
    echo ""

    echo "  [1] Install Wear OS / AOSP Smartwatch App (Wi-Fi Wireless Debugging)"
    echo "  [2] Install Wear OS / AOSP Smartwatch App (USB / Connected Device)"
    echo "  [3] Install Phone Companion App (USB / Wireless)"
    echo "  [4] Full Dual-Device Sideload (Smartwatch + Phone)"
    echo "  [5] Check Connected ADB Devices"
    echo "  [6] Pair New Wireless Watch (Wi-Fi Pairing Code)"
    echo "  [7] View Setup Guide (Wear OS, Standalone AOSP Watch & Phone)"
    echo "  [8] Rebuild Release & Debug APKs from Source (Syncs release_bin/)"
    echo "  [0] Exit"
    echo ""

    read -p "Select an option [0-8]: " choice

    case "$choice" in
        0)
            echo -e "${CYAN}Goodbye! May your Digimon digivolve to greatness!${NC}"
            exit 0
            ;;
        1)
            read -p "Enter Watch IP:Port (e.g. 192.168.1.50:5555): " watch_ip
            if [ -n "$watch_ip" ]; then
                "$ADB_CMD" connect "$watch_ip"
                install_apk "$watch_ip" "$(get_apk "wear-os-app")" "Wear OS / AOSP Smartwatch App"
            fi
            read -p "Press [Enter] to continue..."
            ;;
        2)
            install_apk "" "$(get_apk "wear-os-app")" "Wear OS / AOSP Smartwatch App"
            read -p "Press [Enter] to continue..."
            ;;
        3)
            install_apk "" "$(get_apk "phone-companion-app")" "Phone Companion App"
            read -p "Press [Enter] to continue..."
            ;;
        4)
            apk_watch=$(get_apk "wear-os-app")
            apk_phone=$(get_apk "phone-companion-app")
            echo -e "${YELLOW}Select target device for Wear OS / AOSP Smartwatch:${NC}"
            "$ADB_CMD" devices -l
            read -p "Enter Watch Device ID (from list above): " watch_id
            if [ -n "$watch_id" ]; then
                install_apk "$watch_id" "$apk_watch" "Wear OS / AOSP Smartwatch App"
            fi

            echo ""
            echo -e "${YELLOW}Select target device for Android Phone:${NC}"
            read -p "Enter Phone Device ID (from list above): " phone_id
            if [ -n "$phone_id" ]; then
                install_apk "$phone_id" "$apk_phone" "Phone Companion App"
            fi
            read -p "Press [Enter] to continue..."
            ;;
        5)
            echo ""
            echo -e "${CYAN}Connected ADB Devices:${NC}"
            "$ADB_CMD" devices -l
            echo ""
            read -p "Press [Enter] to continue..."
            ;;
        6)
            read -p "Enter Watch IP:Port for pairing (e.g. 192.168.1.50:42135): " pair_ip
            read -p "Enter 6-digit Wi-Fi Pairing Code: " pair_code
            if [ -n "$pair_ip" ] && [ -n "$pair_code" ]; then
                "$ADB_CMD" pair "$pair_ip" "$pair_code"
            fi
            read -p "Press [Enter] to continue..."
            ;;
        7)
            show_guide
            ;;
        8)
            echo -e "${CYAN}Rebuilding Release & Debug APKs...${NC}"
            (cd "$SCRIPT_DIR" && ./gradlew assembleRelease assembleDebug)
            sync_release_bins
            echo -e "${GREEN}[OK] Rebuilt and synced all APKs into release_bin/!${NC}"
            read -p "Press [Enter] to continue..."
            ;;
        *)
            echo -e "${YELLOW}Invalid option. Please choose between 0 and 8.${NC}"
            sleep 1
            ;;
    esac
done
