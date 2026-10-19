# DigiWear v2.0 Official Release Notes

### Complete Overview
DigiWear v2.0 represents a complete, exhaustive overhaul across both the standalone smartwatch application and the Android phone companion application, achieving 100% parity with historical Bandai virtual pets while leveraging modern wearable hardware, NFC, and wireless protocols.

---

### Highlights & New Features

#### 1. 300-Species Complete Canonical Roster
- 300 authentic Digimon from Baby I to Super Ultimate.
- 300 bespoke 16x16 pixel matrices with 12 distinct animation frames per monster (idle, walk, feed, chew, sleep, sick, attack charge, attack strike, refusal).
- Rendered on a 64x64 matrix at `FilterQuality.None` for pristine pixel sharpness across round and square watch displays.
- 10 Starter Digi-Eggs covering every historical generation and evolutionary lineage.

#### 2. 100% Evolutionary Reachability & 54 DNA Jogress / DigiXros Recipes
- Every single one of the 300 species can be naturally reached from the 10 starter eggs.
- Zero orphaned species and zero dead ends.
- 54 canonical DNA Jogress, DigiXros, and Mode Change fusions with dedicated 1-tap chambers on both watch and companion app.
- Full visual sprite sheet evolution poster (`docs/digiwear_evolution_guide_chart.png`) and 300-species master sprite sheet (`docs/digiwear_roster_spritesheet.png`).

#### 3. 4-Mode Battle System with Face-to-Face Animated Arena
- **Adventure Colosseum**: 30 sectors of physical-step exploration and turn-by-turn boss combat with environmental weather hazards.
- **Wild NFC & Barcode Scanner**: Scan physical NFC cards or everyday retail UPC/EAN product barcodes with your phone camera to spawn unique wild Digimon encounters with an 18% rare boss spawn rate.
- **NFC Tap-to-Duel**: Direct watch-to-watch, watch-to-phone, or phone-to-phone peer-to-peer duels and instant DNA Jogress fusions.
- **A-Com / D-Com / WiFiCom Online Battles**: Fully compatible with the 128-bit Crockford Base32 CommCode standard (`DW2-...`) and original 16/26-character DigiROM protocols (`V1-XXXX-XXXX-XXXX`). Includes 1-tap NFC & DataLayer relay from watch to phone.

#### 4. Broad Hardware Support: Wear OS & Standalone AOSP Standard Watches
- Seamless support for Wear OS 3.0, 4.0, and 5.0+ (Galaxy Watch 4/5/6/7/Ultra, Pixel Watch 1/2/3, TicWatch, OnePlus Watch 2).
- First-class support for standalone AOSP / Full Android Watches (Kospet, Lokmat, Lemfo, Zeblaze, Rogbid) without Google Play Services.
- **AOSP Wi-Fi & Hotspot LAN Bridge**: Built-in TCP sync server (port 45820) and UDP discovery responder (port 45821) for zero-GMS wireless pairing.
- **Display Shape Override (`AUTO / ROUND / SQUARE`)**: Resolves circular bezel clipping on round AOSP watches running square phone ROMs.
- **3-Tier Universal Pedometer**: Hardware step counter, hardware step detector, and raw 3-axis accelerometer peak detector fallback.
- **Expanded Hardware Key Mappings & Native BackHandler**: Full support for physical stem buttons, volume keys, rotating crowns, and Android back gestures.

#### 5. Complete Android Companion App & Home Screen Widget
- **DigiVault Cryo-Storage**: Zero-decay cold storage for raised Digimon with instant deployment and dual CommCode/DigiROM export.
- **300-Species Sprite Studio**: 12-frame animation viewer with authentic Bandai LCD screen overlays.
- **Story Campaign Architect**: 30-sector custom campaign editor with direct wireless push to watch.
- **Android Home Screen AppWidget**: Live 64x64 pixel art sprite, stage, weight, hunger hearts, and strength stars updated in real-time.
- **Cloud JSON Backup & Restore**: Full archive import/export.

---

### Release Binaries (`release_bin/`)
- `wear-os-app-release.apk`
- `digiwear-aosp-standard-watch-release.apk`
- `wear-os-app-debug.apk`
- `phone-companion-app-release.apk`
- `phone-companion-app-debug.apk`
