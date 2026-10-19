# DigiWear Release Announcement for Reddit

Subreddits to post to:
- r/digimon
- r/WearOS
- r/vpet
- r/androidapps
- r/smartwatches

---

### Post Title
I spent the last few months building a standalone Digimon V-Pet for Wear OS and Android watches with 300 species and cross-play, and it is finally finished and open source.

---

### Post Body

Hey everyone,

I've been a huge fan of classic Digimon virtual pets since the original 1997 bricks and the Pendulum series. Over the last few years I kept waiting for an authentic, full-featured V-Pet experience on smartwatches that wasn't just a basic watch face or a companion app that requires your phone to stay tethered in your pocket 24/7.

I wanted something that ran completely standalone on my wrist, felt like a real Bandai V-Pet with accurate timing, physical button controls, and real battles, but took full advantage of modern wearable hardware. Since nobody had built quite what I was looking for, I decided to build it myself. It's called DigiWear, and today I'm releasing the entire project as open source along with ready-to-install signed release APKs.

Here is a breakdown of what is packed into this:

1. All 300 authentic Digimon with full color pixel art
Every single monster from Baby I all the way up to Super Ultimate is included. I hand-mapped 300 individual 16x16 pixel matrices with canonical multi-tone color palettes and 12 distinct animation frames each (idle, walking, eating, sleeping, sick, attack charge, attack strike, refusal). It renders at raw pixel scale with zero blurry filtering on both round and square watch displays.

2. 100% natural evolutionary reachability and 54 DNA Jogress recipes
Every single one of the 300 Digimon can be reached from the 10 starter Digi-Eggs using official Bandai evolution rules (care mistakes, training effort, overfeeding, battles, and win percentages). There are no dead ends. For special evolutions like Omnimon, Mastemon, Paildramon, Imperialdramon Paladin Mode, Susanoomon, GraceNovamon, Chaosmon, Jesmon GX, and ZeedMillenniummon, there are 54 canonical DNA Jogress, DigiXros, and Mode Change fusions. I added dedicated 1-tap fusion chambers on both the watch and phone so you can easily discover recipes and trigger fusions without guessing. I also compiled a massive visual evolution guide poster and sprite sheet and included it in the repo.

3. True standalone simulation on your wrist
The watch app runs its own deterministic simulation clock using UTC timestamp deltas. Your Digimon lives, digests, poops, calls for care, and sleeps on its own schedule on your wrist even if your phone is turned off or left at home. In ambient mode, the screen drops into a low-bit monochrome outline that uses under 15% on-pixel ratio to protect battery life and prevent OLED burn-in.

4. 3-button V-Pet controls, rotary crown, and hardware buttons
You can play using the touch screen with the classic 8 Bandai LCD icons, or you can use physical hardware buttons. The app maps physical watch stem buttons, volume keys, and the rotating bezel directly to Button A (Select), Button B (Confirm/QTE), and Button C (Cancel/Back). I also built two training modes: the classic timing bar QTE where you smash iron blocks, and an authentic Pendulum wrist-shake mode that uses the watch accelerometer so you physically shake your wrist 5 to 8 times to get maximum power.

5. 4 distinct battle modes and online compatibility
- Adventure Mode: A 30-sector campaign where your real-world daily steps walk you through File Island, Server Continent, Spiral Mountain, and the Genesis Core with boss fights at each gate.
- Wild Barcode & NFC Scanner: You can scan real-world NFC tags or scan any everyday grocery product barcode with your phone camera to spawn unique wild Digimon encounters.
- Tap-to-Duel: Tap two watches together, or tap watch-to-phone via NFC, to battle a friend or trigger a DNA Jogress on the spot.
- A-Com, D-Com, and WiFiCom Support: Uses the 128-bit Crockford Base32 CommCode standard and classic DigiROM protocols, so you can battle other players online in Digimon 0nl1ne or Alpha Terminal. You can even tap your watch to your phone to relay your watch Digimon into the phone's online lobby.

6. Broad hardware support: Wear OS and standalone AOSP watches
It runs great on Wear OS 3, 4, and 5 (Galaxy Watch 4/5/6/7, Pixel Watch 1/2/3, TicWatch, etc.), but I also made sure it works on standalone full-Android AOSP watches like Kospet, Lemfo, Lokmat, and Zeblaze that don't have Google Play Services. For AOSP watches, the watch runs a local Wi-Fi and Hotspot LAN sync server that the companion phone app auto-discovers with UDP. There is also a 3-tier pedometer fallback (step counter, step detector, and raw accelerometer peak detection) and a Display Shape Override in settings so circular AOSP watches running square phone ROMs don't get their corners clipped.

7. Android companion app, DigiVault, and Home Screen Widget
The phone app includes a Cryo-Vault where you can preserve raised Digimon in zero-decay cold storage, a 300-species animated Sprite Studio, a custom 30-sector story campaign editor, JSON backup and restore, and an Android Home Screen Widget that displays your active partner's live sprite, stage, weight, hunger hearts, and strength stars.

The project is completely free, licensed under MIT, and contains zero ads, zero trackers, and zero microtransactions.

GitHub Repository (source code, visual sprite sheet guide, and signed release APKs):
https://github.com/Dalinuxx/DigiWear

The signed release APKs are in the release_bin folder in the repository (wear-os-app-release.apk, digiwear-aosp-standard-watch-release.apk, and phone-companion-app-release.apk), along with automated one-click sideload scripts for Windows, Mac, and Linux.

If you test it out on your watch, let me know how it runs and what you think. I'm happy to answer any questions about the battle formulas, evolution branches, or sideloading setup in the comments.
