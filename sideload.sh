#!/usr/bin/env bash
# ======================================================================
#            DIGIWEAR SIDELOADING & INSTALLATION SCRIPT 📟⌚
# ======================================================================
# Supports Linux and macOS with auto-detection of Android SDK & ADB.

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
    local bin_rel="$SCRIPT_DIR/release_bin/$module-release.apk"
    local bin_uns="$SCRIPT_DIR/release_bin/$module-release-unsigned.apk"
    local bin_deb="$SCRIPT_DIR/release_bin/$module-debug.apk"
    local rel="$SCRIPT_DIR/$module/build/outputs/apk/release/$module-release.apk"
    local uns="$SCRIPT_DIR/$module/build/outputs/apk/release/$module-release-unsigned.apk"
    local deb="$SCRIPT_DIR/$module/build/outputs/apk/debug/$module-debug.apk"

    if [ -f "$bin_rel" ]; then
        echo "$bin_rel"
    elif [ -f "$bin_uns" ]; then
        echo "$bin_uns"
    elif [ -f "$bin_deb" ]; then
        echo "$bin_deb"
    elif [ -f "$rel" ]; then
        echo "$rel"
    elif [ -f "$uns" ]; then
        echo "$uns"
    elif [ -f "$deb" ]; then
        echo "$deb"
    else
        echo ""
    fi
}

show_guide() {
    echo ""
    echo -e "${YELLOW}----------------------------------------------------------------------${NC}"
    echo -e "${YELLOW}  HOW TO ENABLE DEVELOPER OPTIONS & WIRELESS DEBUGGING                ${NC}"
    echo -e "${YELLOW}----------------------------------------------------------------------${NC}"
    echo ""
    echo -e "${GREEN}[ Wear OS Smartwatch (Pixel Watch, Galaxy Watch, etc.) ]${NC}"
    echo "  1. On your watch, open Settings -> System -> About -> Versions."
    echo "  2. Tap 'Build Number' 7 times rapidly until you see 'You are a developer!'"
    echo "  3. Go back to Settings -> Developer Options."
    echo "  4. Turn ON 'ADB Debugging'."
    echo "  5. Turn ON 'Wireless Debugging' (or 'Debug over Wi-Fi')."
    echo "  6. Make sure your watch and PC are connected to the SAME Wi-Fi network."
    echo "  7. Note the IP address and Port (e.g. 192.168.1.50:5555)."
    echo ""
    echo -e "${GREEN}[ Android Phone Companion ]${NC}"
    echo "  1. On your phone, go to Settings -> About Phone."
    echo "  2. Tap 'Build Number' 7 times."
    echo "  3. Go to Settings -> System -> Developer Options."
    echo "  4. Turn ON 'USB Debugging' (connect via USB cable) or 'Wireless Debugging'."
    echo -e "${YELLOW}----------------------------------------------------------------------${NC}"
    echo ""
    read -p "Press [Enter] to return to the menu..."
}

while true; do
    clear
    echo -e "${CYAN}======================================================================${NC}"
    echo -e "${YELLOW}            DIGIWEAR SIDELOADING & INSTALLATION WIZARD 📟⌚          ${NC}"
    echo -e "${CYAN}======================================================================${NC}"
    echo -e "  Dual-Device Virtual Pet System for Wear OS & Android Companion      "
    echo -e "${CYAN}======================================================================${NC}"
    echo ""

    if [ -n "$ADB_CMD" ]; then
        echo -e "${GREEN}ADB Status: FOUND ($ADB_CMD)${NC}"
    else
        echo -e "${RED}ADB Status: NOT FOUND in PATH or standard Android SDK directories!${NC}"
        echo -e "${YELLOW}Please install Android Platform-Tools or set your PATH.${NC}"
    fi
    echo ""

    echo "  [1] Install Wear OS Smartwatch App (Wi-Fi Wireless Debugging)"
    echo "  [2] Install Wear OS Smartwatch App (USB / Connected Device)"
    echo "  [3] Install Phone Companion App (USB / Wireless)"
    echo "  [4] Full Dual-Device Sideload (Smartwatch + Phone)"
    echo "  [5] Check Connected ADB Devices"
    echo "  [6] Pair New Wireless Watch (Wi-Fi Pairing Code)"
    echo "  [7] View Setup Guide (Enable Developer Options & Wi-Fi Debugging)"
    echo "  [8] Rebuild Release APKs from Source"
    echo "  [0] Exit"
    echo ""

    read -p "Select an option [0-8]: " choice

    case "$choice" in
        0)
            echo -e "${CYAN}Goodbye! May your Digimon digivolve to greatness! 📟✨${NC}"
            exit 0
            ;;
        1)
            read -p "Enter Watch IP:Port (e.g. 192.168.1.50:5555): " watch_ip
            if [ -n "$watch_ip" ]; then
                "$ADB_CMD" connect "$watch_ip"
                apk=$(get_apk "wear-os-app")
                if [ -n "$apk" ]; then
                    echo -e "${CYAN}Installing $(basename "$apk")...${NC}"
                    "$ADB_CMD" -s "$watch_ip" install -r -d "$apk"
                    echo -e "${GREEN}✅ Successfully installed to Wear OS smartwatch!${NC}"
                else
                    echo -e "${RED}Error: APK not found. Rebuild with option [8].${NC}"
                fi
            fi
            read -p "Press [Enter] to continue..."
            ;;
        2)
            apk=$(get_apk "wear-os-app")
            if [ -n "$apk" ]; then
                echo -e "${CYAN}Installing $(basename "$apk")...${NC}"
                "$ADB_CMD" install -r -d "$apk"
                echo -e "${GREEN}✅ Successfully installed to Wear OS smartwatch!${NC}"
            else
                echo -e "${RED}Error: APK not found. Rebuild with option [8].${NC}"
            fi
            read -p "Press [Enter] to continue..."
            ;;
        3)
            apk=$(get_apk "phone-companion-app")
            if [ -n "$apk" ]; then
                echo -e "${CYAN}Installing $(basename "$apk")...${NC}"
                "$ADB_CMD" install -r -d "$apk"
                echo -e "${GREEN}✅ Successfully installed to Android Phone!${NC}"
            else
                echo -e "${RED}Error: APK not found. Rebuild with option [8].${NC}"
            fi
            read -p "Press [Enter] to continue..."
            ;;
        4)
            apk_watch=$(get_apk "wear-os-app")
            apk_phone=$(get_apk "phone-companion-app")
            echo -e "${YELLOW}Select target device for Wear OS Smartwatch:${NC}"
            "$ADB_CMD" devices -l
            read -p "Enter Watch Device ID (from list above): " watch_id
            if [ -n "$watch_id" ] && [ -n "$apk_watch" ]; then
                "$ADB_CMD" -s "$watch_id" install -r -d "$apk_watch"
                echo -e "${GREEN}✅ Smartwatch app installed!${NC}"
            fi

            echo ""
            echo -e "${YELLOW}Select target device for Android Phone:${NC}"
            read -p "Enter Phone Device ID (from list above): " phone_id
            if [ -n "$phone_id" ] && [ -n "$apk_phone" ]; then
                "$ADB_CMD" -s "$phone_id" install -r -d "$apk_phone"
                echo -e "${GREEN}✅ Phone companion app installed!${NC}"
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
            echo -e "${CYAN}Rebuilding Release APKs...${NC}"
            (cd "$SCRIPT_DIR" && ./gradlew assembleRelease)
            read -p "Press [Enter] to continue..."
            ;;
        *)
            echo -e "${YELLOW}Invalid option. Please choose between 0 and 8.${NC}"
            sleep 1
            ;;
    esac
done
