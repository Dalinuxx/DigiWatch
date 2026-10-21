package com.digiwear.core.battle

import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage

/**
 * Canonical Bandai Reference Book & Anime Signature Attack Move Registry.
 * Provides authentic named special techniques for combat encounters across all 500 species.
 */
object SignatureAttackRegistry {

    private val CANONICAL_ATTACKS: Map<Int, String> = mapOf(
        // ==========================================
        // Baby I & Baby II (101 - 134, 1101 - 1116)
        // ==========================================
        101 to "Bubble Blow",          // Botamon
        102 to "Bubble Blow",          // Koromon
        103 to "Acid Bubbles",         // Punimon
        104 to "Bubble Blow",          // Tsunomon
        105 to "Acid Bubbles",         // Poyomon
        106 to "Bubble Blow",          // Tokomon
        107 to "Sticky Net",           // Yuramon
        108 to "Bubble Blow",          // Tanemon
        109 to "Poison Bubbles",       // Zurumon
        110 to "Poison Bubbles",       // Pagumon
        111 to "Smoky Blow",           // Mokumon
        112 to "Fireball",             // DemiMeramon
        113 to "Glare Eye",            // Kuramon
        114 to "Hop Attack",           // DemiVeemon
        115 to "Hot Bite",             // Gigimon
        116 to "Killing Stone",        // Viximon
        117 to "Double Bubble",        // Gummymon
        118 to "Double Bubble",        // Chocomon
        119 to "Rolling Black",        // Yaamon
        120 to "Fox Tail",             // Nyaromon
        121 to "Soap Bubbles",         // Pyocomon
        122 to "Metal Drop",           // Dorimon
        123 to "Nail Scratch",         // Tsumemon
        124 to "Hop Hip",              // Hopmon
        125 to "Howlingertz",          // Kapurimon
        126 to "Pinecone",             // Minomon
        127 to "Starlight",            // Sunmon
        128 to "WaterDrops",           // Moonmon
        129 to "Light Bubbles",        // Ketomon
        130 to "Seed Cracker",         // Nyokimon
        131 to "Soap Bubbles",         // Pichimon
        132 to "Bubble Blow",          // Pukamon
        133 to "Pororo Breeze",        // Poromon
        134 to "Shout Shock",          // Upamon
        1101 to "Stone Bump",          // Cotsucomon
        1102 to "Ka-Ching Headbutt",   // Kakkinmon
        1103 to "Saku Cut",            // Sakuttomon
        1104 to "Small Iron Spikes",   // Fufumon
        1105 to "Hop Attack",          // Chibimon
        1106 to "Acid Bubbles",        // Algomon Baby I
        1107 to "Hypno Tangler",       // Algomon Baby II
        1108 to "Pulse Bubble",        // Dokimon
        1109 to "Static Bubble",       // Bibimon
        1110 to "Smile Fang",          // Wanyamon
        1111 to "Jamming Powder",      // Choromon
        1112 to "Howlingertz",         // Caprimon Color
        1113 to "Sticky Bubbles",      // Bubbmon Color
        1114 to "Elastic Bubbles",     // Mochimon Color
        1115 to "Pinecone",            // Minomon Color
        1116 to "Hot Gas",             // Babydmon

        // ==========================================
        // Child / Rookie (201 - 260, 1201 - 1225)
        // ==========================================
        201 to "Pepper Breath",        // Agumon
        202 to "Electric Shock",       // Betamon
        203 to "Blue Blaster",         // Gabumon
        204 to "Super Thunder Strike", // Elecmon
        205 to "Boom Bubble",          // Patamon
        206 to "Spiral Twister",       // Biyomon
        207 to "Poison Ivy",           // Palmon
        208 to "Super Shocker",        // Tentomon
        209 to "Paralyze Breath",      // Gazimon
        210 to "Spiral Saw",           // Gizamon
        211 to "Electro Thread",       // Kunemon
        212 to "Vee-Headbutt",         // Veemon
        213 to "Sticky Net",           // Wormmon
        214 to "Pyro Sphere",          // Guilmon
        215 to "Diamond Storm",        // Renamon
        216 to "Bunny Blast",          // Terriermon
        217 to "Blazing Ice",          // Lopmon
        218 to "Bada Boom",            // Impmon
        219 to "Spitfire Blast",       // BlackAgumon
        220 to "Little Horn",          // BlackGabumon
        221 to "Grand Cross",          // Lucemon
        222 to "Fifth Rush",           // Hackmon
        223 to "Puppy Howling",        // Salamon
        224 to "Double Backhand",      // Gaomon
        225 to "Feather Slash",        // Hawkmon
        226 to "Diamond Shell",        // Armadillomon
        227 to "Metal Cannon",         // Dorumon
        228 to "Hothead",              // Kotemon
        229 to "Cracking Bite",        // Monodramon
        230 to "Rain of Pollen",       // Floramon
        231 to "Gobli Strike",         // Goblimon
        232 to "Marching Fishes",      // Gomamon
        233 to "Angry Rock",           // Gotsumon
        234 to "Lullaby Bubble",       // Otamamon
        235 to "Flame Bomber",         // Candlemon
        236 to "Nightmare Syndrome",   // Bakumon
        237 to "Darkness Gear",        // Hagurumon
        238 to "Poison Smash",         // Mushroomon
        239 to "Scissors Execution",   // Crabmon
        240 to "Black Pearl Shot",     // Syakomon
        241 to "Nuts Shoot",           // Lalamon
        242 to "Scratch Smash",        // Falcomon
        243 to "Blinding Ray",         // Kudamon
        244 to "Katana Attack",        // Ryudamon
        245 to "Baby Breath",          // Dracomon
        246 to "Pointer Arrow",        // Kamemon
        247 to "Corona Flame",         // Coronamon
        248 to "Tear Shot",            // Lunamon
        249 to "Toy Flame",            // ToyAgumon
        250 to "Crazy Giggle",         // Keramon
        251 to "Elec Rush",            // Pulsemon
        252 to "Breaclaw",             // Gammamon
        253 to "Petit Tornado",        // Angoramon
        254 to "Bibi Thunder",         // Jellymon
        255 to "Howling Fire",         // Loogamon
        256 to "Soul Crusher",         // Shoutmon
        257 to "Ran-gum Break",        // Gumdramon
        258 to "Pyro Punch",           // Agunimon
        259 to "Lobo Kendo",           // Lobomon
        260 to "Eye Scratch",          // Dracmon
        1201 to "Spitfire",            // Agumon X
        1202 to "BB Burner",           // Agumon (Black) X
        1203 to "Petit Fire",          // Gabumon X
        1204 to "Shurunen",            // Dracomon X
        1205 to "Bear Fist",           // Bearmon
        1206 to "Scissors Execution",  // Ganimon
        1207 to "Parabolic Junk",      // Junkmon
        1208 to "Sweet Breath",        // Pomumon
        1209 to "Coral Cleanser",      // Sangomon
        1210 to "Grit Nail",           // Sunarizamon
        1211 to "Little Blaze",        // Ghostmon
        1212 to "Prison Fist",         // Algomon Child
        1213 to "Blazing Fire",        // Terriermon X
        1214 to "Petit Twister",       // Lopmon X
        1215 to "Beetle Lariat",       // KoKabuterimon
        1216 to "Lio Claw",            // Liollmon
        1217 to "Ice Wall",            // Paledramon
        1218 to "Twenty Dive",         // Zubamon
        1219 to "Slingshot",           // Koemon
        1220 to "Wall Bash",           // Ludomon
        1221 to "Lightning Fur",       // Herissmon
        1222 to "Rinrin Therapy",      // Morphomon
        1223 to "Licht Nagel",         // Strabimon
        1224 to "Puppy Howling",       // Plotmon Color
        1225 to "Escape Book",         // Bokomon

        // ==========================================
        // Adult / Champion (301 - 387, 1301 - 1330)
        // ==========================================
        301 to "Mega Flame",           // Greymon
        302 to "Blaze Blaster",        // Tyrannomon
        303 to "Touch of Evil",        // Devimon
        304 to "Fireball",             // Meramon
        305 to "Numemon Sludge",       // Numemon
        306 to "Howling Blaster",      // Garurumon
        307 to "Hand of Fate",         // Angemon
        308 to "Needle Spray",         // Togemon
        309 to "Meteor Wing",          // Birdramon
        310 to "Electro Shocker",      // Kabuterimon
        311 to "Volcanic Strike",      // Monochromon
        312 to "Pummel Whack",         // Ogremon
        313 to "Fist of the Beast King", // Leomon
        314 to "Ice Blast",            // Seadramon
        315 to "Poop Toss",            // Sukamon
        316 to "Fire Blast",           // DarkTyrannomon
        317 to "Crimson Claw",         // Devidramon
        318 to "Hyper Heat",           // Cyclonemon
        319 to "Panzer Knuckle",       // Tuskmon
        320 to "Brown Stinger",        // Flymon
        321 to "Dragon Breath",        // Raremon
        322 to "Scissor Claw",         // Kuwagamon
        323 to "Petra Fire",           // Kokatorimon
        324 to "Variable Darts",       // Coelamon
        325 to "Power Drive",          // Nanimon
        326 to "Zombie Claw",          // Bakemon
        327 to "Hydro Pressure",       // Shellmon
        328 to "Aerial Attack",        // Unimon
        329 to "Sub-Zero Ice Punch",   // Frigimon
        330 to "Compost Bomber",       // Vegiemon
        331 to "V-Nova Blast",         // Veedramon
        332 to "Fire Rocket",          // Flamedramon
        333 to "Spiking Strike",       // Stingmon
        334 to "Pyro Blaster",         // Growlmon
        335 to "Dragon Wheel",         // Kyubimon
        336 to "Gargo Laser",          // Gargomon
        337 to "Thunder Cloud",        // Wizardmon
        338 to "Lightning Paw",        // Gatomon
        339 to "Mega Flame",           // BlackGreymon
        340 to "Fox Fire",             // BlackGarurumon
        341 to "Teen Blade",           // BaoHackmon
        342 to "Power Metal",          // Dorugamon
        343 to "Crystal Cloud",        // Sorcerymon
        344 to "Grand Horn",           // Aquilamon
        345 to "Tail Hammer",          // Ankylomon
        346 to "Spiral Blow",          // Gaogamon
        347 to "Bone Boomerang",       // Mojyamon
        348 to "Meteor Squall",        // Starmon
        349 to "Poison Cobweb",        // Dokugumon
        350 to "Harpoon Torpedo",      // Ikkakumon
        351 to "Crimson Curse",        // Golemon
        352 to "Symphony Crusher",     // Gekomon
        353 to "Chrono Breaker",       // Clockmon
        354 to "Destruction Grenade",  // Guardromon
        355 to "Branch Drain",         // Woodmon
        356 to "Spurting Ink",         // Octomon
        357 to "Deadly Shade",         // Gesomon
        358 to "Twin Scissors",        // Ebidramon
        359 to "Blue Thunder",         // Raidramon
        360 to "Gold Rush",            // Digmon
        361 to "Double Stars",         // Shurimon
        362 to "Oxygen Torpedo",       // Submarimon
        363 to "Kunai Wing",           // Peckmon
        364 to "Sunshine Beam",        // Sunflowmon
        365 to "Vacuum Razor",         // Reppamon
        366 to "Armor Piercing Blade", // Ginryumon
        367 to "Blue Flare Breath",    // Coredramon
        368 to "Data Crusher",         // Chrysalimon
        369 to "Flame Dive",           // Firamon
        370 to "Moon Night Bomb",      // Lekismon
        371 to "Vee-Laser",            // ExVeemon
        372 to "Thermal Mane",         // Lynxmon
        373 to "Mega Burst",           // GeoGreymon
        374 to "Thunderfist",          // Bulkmon
        375 to "Solblow",              // BetelGammamon
        376 to "Urda Impulse",         // KausGammamon
        377 to "Sedna Cannon",         // WezenGammamon
        378 to "Dark Pales",           // GulusGammamon
        379 to "Javelin Twist",        // SymbareAngoramon
        380 to "Fistbump",             // TeslaJellymon
        381 to "Dark Roar",            // Loogarmon
        382 to "Shooting Star",        // Pegasusmon
        383 to "Rosetta Stone",        // Nefertimon
        384 to "Strike Fang",          // Strikedramon
        385 to "Schwarz Strahl",       // Dobermon
        386 to "Sticker Blade",        // Sangloupmon
        387 to "Twin Spear",           // DarkKnightmon
        1301 to "Dread Fire",          // Darklizamon
        1302 to "Tri-plex Force",      // Deltamon
        1303 to "Strong Carapace",     // Tortamon
        1304 to "Shadow Sickle",       // Snimon
        1305 to "Dream Dust",          // Gokimon
        1306 to "Petra Fire",          // Cockatrimon
        1307 to "Little Pecker",       // Kiwimon
        1308 to "Kirigomen",           // Musyamon
        1309 to "Shaking Pulse",       // Rukamon
        1310 to "Burning Salamander",  // Agnimon
        1311 to "Licht Sieger",        // Wolfmon
        1312 to "Tornado Gamba",       // Fairimon
        1313 to "Thor Hammer",         // Blitzmon
        1314 to "Snow Bomber",         // Chackmon
        1315 to "Geist Abend",         // Duskmon
        1316 to "Giga Roar",           // Baboongamon
        1317 to "Schwarz Donner",      // Kaiser Leomon
        1318 to "Attract Echo",        // Aegiomon
        1319 to "Flare Buster",        // Shadramon
        1320 to "Lightning Sting",     // Flybeemon
        1321 to "Wind Seeker",         // Harpymon
        1322 to "Balloon Mine",        // Manbomon
        1323 to "Crazy Sonic",         // Pipismon
        1324 to "Meteor Dance",        // Baromon
        1325 to "Sidewinder",          // Pteranomon
        1326 to "Tide Cluster",        // Marin Bullmon
        1327 to "Vajra Rush",          // Kongoumon
        1328 to "Rainbow Shower",      // Peacockmon
        1329 to "Horn Blade",          // Moosemon
        1330 to "Fire Rocket",         // Fladramon

        // ==========================================
        // Perfect / Ultimate (401 - 477, 1401 - 1434)
        // ==========================================
        401 to "Giga Destroyer",       // MetalGreymon
        402 to "Hearts Attack",        // Monzaemon
        403 to "Ground Zero",          // SkullGreymon
        404 to "Wolf Claw",            // WereGarurumon
        405 to "Heaven's Gate",        // MagnaAngemon
        406 to "Flower Cannon",        // Lillymon
        407 to "Shadow Wing",          // Garudamon
        408 to "Horn Buster",          // MegaKabuterimon
        409 to "Lightning Javelin",    // MegaSeadramon
        410 to "Grisly Wing",          // Myotismon
        411 to "Dark Network",         // Etemon
        412 to "Smiley Bomb",          // Mamemon
        413 to "Energy Bomb",          // MetalMamemon
        414 to "Giga Destroyer II",    // MetalTyrannomon
        415 to "Pretty Attack",        // Ex-Tyrannomon
        416 to "Genocide Attack",      // Megadramon
        417 to "Bit Bomb",             // Piximon
        418 to "Lightning Blade",      // Andromon
        419 to "Deadly Bomb",          // Giromon
        420 to "Alien Ray",            // Vademon
        421 to "Spiral Bone",          // SkullMamemon
        422 to "V-Wing Blade",         // AeroVeedramon
        423 to "Desperado Blaster",    // Paildramon
        424 to "Atomic Blaster",       // WarGrowlmon
        425 to "Talisman of Light",    // Taomon
        426 to "Golden Triangle",      // Rapidmon
        427 to "Celestial Arrow",      // Angewomon
        428 to "Darkness Wave",        // LadyDevimon
        429 to "Revenge Flame",        // BlackMetalGreymon
        430 to "Full Moon Kick",       // BlackWereGarurumon
        431 to "Erase Claw",           // Cyberdramon
        432 to "Bunny Blades",         // Antylamon
        433 to "Trident Saber",        // SaviorHackmon
        434 to "Paradise Lost",        // Lucemon Chaos Mode
        435 to "Metal Meteor",         // Dorugreymon
        436 to "Berserk Sword",        // Knightmon
        437 to "Top Gun",              // Silphymon
        438 to "Vulcan's Hammer",      // Zudomon
        439 to "Fist of Ice",          // Panjyamon
        440 to "Shadow Scythe",        // Phantomon
        441 to "Justice Beam",         // Shakkoumon
        442 to "Nightmare Syndrome",   // Digitamamon
        443 to "Illusion Mist",        // Cherrymon
        444 to "Musical Fist",         // ShogunGekomon
        445 to "Big Bang Voice",       // Volcamon
        446 to "Guilty Black",         // MarineDevimon
        447 to "Dark Strom",           // WaruSeadramon
        448 to "Spider Shooter",       // Infermon
        449 to "Wind Cutter Sword",    // Chirinmon
        450 to "Seiryuu Jin",          // Hisyaryumon
        451 to "Blaze Sonic Breath",   // Wingdramon
        452 to "Mikafutsunokami",      // Yatagaramon
        453 to "Lila Shower",          // Lilamon
        454 to "Crimson Beast King Wave", // Flaremon
        455 to "Lunatic Dance",        // Crescemon
        456 to "Nail Bone",            // SkullSatamon
        457 to "Snake Bandage",        // Mummymon
        458 to "Spider Thread",        // Arukenimon
        459 to "Cross Blade",          // Grademon
        460 to "Hellfire",             // Astamon
        461 to "White Statue",         // Gargoylemon
        462 to "Tusk Strikes",         // Mammothmon
        463 to "Trident Arm",          // MetalGreymon (Virus)
        464 to "Desolation Claw",      // Cyberdramon (Savers)
        465 to "Trident Revolver",     // RizeGreymon
        466 to "Gaoga Tornado",        // MachGaogamon
        467 to "Raigekishou",          // Boutmon
        468 to "Gran Nova",            // Canoweissmon
        469 to "Dead End Spike",       // Regulusmon
        470 to "Knockout",             // Lamortemon
        471 to "Adieu World",          // Thetismon
        472 to "Hellfire Buster",      // Soloogarmon
        473 to "Kamiuchi",             // Baalmon
        474 to "Spike Buster",         // JewelBeemon
        475 to "Hell Masquerade",      // Dinobeemon
        476 to "Heavy Metal Fire",     // SkullMeramon
        477 to "Emerald Blaze",        // Cerberusmon
        1401 to "Kobushi Tone",        // Tonosama Gekomon
        1402 to "Forbidden Trident",   // Dagomon
        1403 to "Needle Squall",       // Pukumon
        1404 to "Strike Fishing",      // Hangyomon
        1405 to "Tail Blade",          // Anomalocarimon
        1406 to "Stinger Surprise",    // Scorpiomon
        1407 to "Spiral Flower",       // Blossomon
        1408 to "Asura God Fist",      // Asuramon
        1409 to "Lightning Shower",    // Aegiochusmon
        1410 to "Flame Storm",         // Vritramon
        1411 to "Brahmastra",          // Aldamon
        1412 to "Zweihander",          // Beowolfmon
        1413 to "Zone Deleter",        // Velgrmon
        1414 to "Kugelblitz",          // Raihimon
        1415 to "Soul Chopper",        // Fantomon
        1416 to "Guilty Black",        // Marin Devimon
        1417 to "Thunder Javelin",     // Mega Seadramon
        1418 to "Dark Strom",          // Waru Seadramon
        1419 to "V-Wing Blade",        // Aero V-dramon
        1420 to "Predator Thread",     // Arachnemon
        1421 to "Dead or Alive",       // Lucemon Falldown Mode
        1422 to "Hellfire",            // Cerberumon
        1423 to "Bladegarm",           // Duramon
        1424 to "Death Cloud",         // Mephismon
        1425 to "Samurai Tiger",       // Mihiramon
        1426 to "Terra Blade",         // Vajramon
        1427 to "Asipatravana",        // Andiramon Deva
        1428 to "Inferno Gate",        // Cerberumon Werewolf Mode
        1429 to "Justice Kick",        // Justimon Blitz Arm
        1430 to "Thunderball",         // Thunderballmon
        1431 to "Callous Fang",        // Crys Paledramon
        1432 to "Grave Scream",        // Metal Fantomon
        1433 to "Avalanche Step",      // Blizzarmon
        1434 to "Distortion Line",     // Algomon Perfect

        // ==========================================
        // Ultimate / Mega (501 - 592, 1501 - 1545)
        // ==========================================
        501 to "Gaia Force",           // WarGreymon
        502 to "Giga Cannon",          // Machinedramon
        503 to "Cocytus Breath",       // MetalGarurumon
        504 to "Seven Heavens",        // Seraphimon
        505 to "Thorn Whip",           // Rosemon
        506 to "Starlight Explosion",  // Phoenixmon
        507 to "Giga Blaster",         // HerculesKabuterimon
        508 to "Sorrow Blue",          // Plesiomon
        509 to "Trump Sword",          // Piedmon
        510 to "Transcendent Sword",   // Omnimon
        511 to "Banana Slip",          // MetalEtemon
        512 to "Mega Death",           // Imperialdramon Dragon Mode
        513 to "Giga Death",           // Imperialdramon Fighter Mode
        514 to "Omni Sword",           // Imperialdramon Paladin Mode
        515 to "Lightning Joust",      // Gallantmon
        516 to "Quo Vadis",            // Gallantmon Crimson Mode
        517 to "Amethyst Mandala",     // Sakuyamon
        518 to "Burst Shot",           // MegaGargomon
        519 to "Double Impact",        // Beelzemon
        520 to "Chaos Flare",          // Beelzemon Blast Mode
        521 to "Extreme Jihad",        // Magnamon
        522 to "Shining V-Force",      // UlforceVeedramon
        523 to "Digitalize of Soul",   // Alphamon
        524 to "Ultimate Ouryuken",    // Alphamon Ouryuken
        525 to "Dark Terra Force",     // BlackWarGreymon
        526 to "Chaos Gaia Force",     // ChaosBlackWarGreymon
        527 to "Catastrophe Cannon",   // Diaboromon
        528 to "Black Rain",           // Armaggeddemon
        529 to "Total Annihilation",   // Apocalymon
        530 to "Eternal Nightmare",    // Belphemon
        531 to "Phantom Pain",         // Lilithmon
        532 to "Flame Inferno",        // Daemon
        533 to "Rostrum",              // Leviamon
        534 to "Pandemonium Lost",     // Barbamon
        535 to "Grey Cannon",          // Omnimon Alter-S
        536 to "Garuru Sword",         // Omnimon Zwart
        537 to "Chaos Degradation",    // Mastemon
        538 to "Schwertgeist",         // Jesmon
        539 to "Tekken Seisai",        // Gankoomon
        540 to "Dragon's Roar",        // Dynasmon
        541 to "Spiral Masquerade",    // LordKnightmon
        542 to "End Waltz",            // Craniummon
        543 to "Bifrost",              // Sleipmon
        544 to "Aussterben",           // Duftmon
        545 to "Pendragon's Glory",    // Examon
        546 to "Time Unlimited",       // Millenniummon
        547 to "Death Crystal",        // MoonMillenniummon
        548 to "Time Destroyer",       // ZeedMillenniummon
        549 to "Celestial Blade",      // Susanoomon
        550 to "Brave Metal",          // Dorugoramon
        551 to "Rinkazan",             // Gaiomon
        552 to "Eden's Javelin",       // Ophanimon
        553 to "Arctic Blizzard",      // Vikemon
        554 to "Hyper Infinity Cannon", // Chaosdramon
        555 to "Odin's Breath",        // Kentaurosmon
        556 to "Fire Tornado",         // Magnadramon
        557 to "Lightning Spear",      // Cherubimon
        558 to "Terror's Cluster",     // RustTyrannomon
        559 to "Plasma Stake",         // BlitzGreymon
        560 to "獸狼大回転 (Kijuu Enbu)", // CresGarurumon
        561 to "Eiseiryuoujin",        // Ouryumon
        562 to "Tenryu Slash",         // Slayerdramon
        563 to "Solblaster",           // Apollomon
        564 to "Crescent Hook",        // Dianamon
        565 to "Calydon Arcus",        // GraceNovamon
        566 to "Purgatorial Flame",    // Lucemon Satan Mode
        567 to "Bantyo Blade",         // Chaosmon
        568 to "Flash Bantyo Punch",   // BanchoLeomon
        569 to "Dark Roar",            // Darkdramon
        570 to "Ocean Love",           // MarineAngemon
        571 to "Mach Stinger V",       // TigerVespamon
        572 to "Ura Izuna",            // Kuzuhamon
        573 to "Seven's Devil",        // BlackSeraphimon
        574 to "Flame Hellscythe",     // Ophanimon Falldown Mode
        575 to "Spiral Raven Claw",    // Ravemon
        576 to "Glorious Burst",       // ShineGreymon
        577 to "Full Moon Blaster",    // MirageGaogamon
        578 to "Knights' Intruder",    // Jesmon GX
        579 to "Sieg Saber",           // Gallantmon X
        580 to "Poseidon Force",       // WarGreymon X
        581 to "Grace Cross Freezer",  // MetalGarurumon X
        582 to "Seventh Full Cluster", // Beelzemon X
        583 to "Photon Blaster",       // Siriusmon
        584 to "Backdraft Lewisham",   // Diarbbitmon
        585 to "Valkyrie's Ice",       // Amphimon
        586 to "Ragnarok Howling",     // Fenriloogamon
        587 to "Shiden-kai",           // Kazuchimon
        588 to "Black Death Cloud",    // Arcturusmon
        589 to "Corona Blaze Sword",   // ShineGreymon Burst Mode
        590 to "Full Moon Meteor Impact", // MirageGaogamon Burst Mode
        591 to "Death the Cannon",     // Beelzemon (2010)
        592 to "Fearsome Blade",       // Omnimon Merciful Mode
        1501 to "Corona Sanctions",    // Marsmon
        1502 to "Strike Roll",         // Minervamon
        1503 to "Vortex Penetrate",    // Neptunemon
        1504 to "Bomber Art",          // Vulcanusmon
        1505 to "Island Freedom",      // Ceresmon
        1506 to "Cook of Fate",        // Bacchusmon
        1507 to "Wide Plasment",       // Jupitermon
        1508 to "Grace Lance",         // Junomon
        1509 to "Haggard Cluster",     // Plutomon
        1510 to "Healing Therapy",     // Venusmon
        1511 to "Soul Core Attack",    // Titamon
        1512 to "Phoebus Blow",        // Apollomon Whispered
        1513 to "Omega Corona",        // Ancient Greymon
        1514 to "Sharpness Claymore",  // Ancient Garurumon
        1515 to "Calamity Thunder",    // Ancient Beatmon
        1516 to "Storm Gazer",         // Ancient Irismon
        1517 to "Freezing Blizzard",   // Ancient Megatheriumon
        1518 to "Necro Eclipse",       // Ancient Sphinxmon
        1519 to "Supernova",           // Ancient Volcamon
        1520 to "Epeius",              // Ancient Troiamon
        1521 to "Great Maelstrom",     // Ancient Mermaimon
        1522 to "Laplace's Demon",     // Ancient Wisemon
        1523 to "Enryuugeki",          // Kaiser Greymon
        1524 to "Machinegun Destroy",  // Magna Garurumon
        1525 to "Kongou",              // Baihumon
        1526 to "Sourai",              // Qinglongmon
        1527 to "Mugen",               // Xuanwumon
        1528 to "Koujin",              // Zhuqiaomon
        1529 to "Taikyoku",            // Huanglongmon
        1530 to "Banana Slip",         // Metal Etemon
        1531 to "Saru Shibai",         // King Etemon
        1532 to "Brain Rupture",       // Ebemon
        1533 to "Pandemonium Flame",   // Belial Vamdemon
        1534 to "Venom Infusion",      // Venom Vamdemon
        1535 to "Shine Breaker",       // Aegisdramon
        1536 to "Gift of Darkness",    // Belphemon Rage Mode
        1537 to "Catedral",            // Ogudomon
        1538 to "Tesshin Shouriken",   // Jesmon X
        1539 to "Jishin! Kaminari! Kaji! Oyaji!", // Gankoomon X
        1540 to "Dragon's Gust",       // Dynasmon X
        1541 to "God Breath",          // Craniummon X
        1542 to "Muspelheim",          // Sleipmon X
        1543 to "Brauner Nebel",       // Duftmon X
        1544 to "Extreme Jihad",       // Magnamon X
        1545 to "Hyper Infinity Cannon", // Chaosdramon X

        // 550 Roster Expansion (50 New Canonical Species)
        261 to "Blue Brave",           // Spadamon
        262 to "Beak Storm",           // Pteromon
        263 to "Scissor Step",         // Shoemon
        388 to "V-Cutter",             // Zubaeagermon
        389 to "Reflector Guard",      // TiaLudomon
        390 to "Shut Claw",            // Meicoomon
        391 to "Lightning Dash",       // Runnermon
        392 to "Insect Yoga",          // Exermon
        393 to "Sloth Punch",          // Namakemon
        394 to "Spiral Gale",          // Galemon
        395 to "Stitch Kick",          // Shoeshoemon
        396 to "Great Sword of Victory", // Shoutmon X4
        397 to "Solar Laser",          // Garmmon
        398 to "Scissor Arms Omega",   // Kuwagamon X
        478 to "Lightning Buster",     // RaijiLudomon
        479 to "Modestly Blizzard",    // Meicrackmon
        480 to "Howling Burn",         // Helloogarmon
        481 to "Sonic Grand",          // GrandGalemon
        482 to "Flame Basket",         // Chaperomon
        483 to "Hard Rock Soul",       // OmegaShoutmon
        484 to "Meteor Buster",        // Shoutmon X5
        485 to "Crack Grip",           // Climbmon
        486 to "Submerge Fang",        // Divemon
        487 to "Round Bullet",         // Shootmon
        488 to "Upbeat Sonic",         // Tempomon
        593 to "Thousand Fist",        // Merukimon
        594 to "Zweihander",           // Durandamon
        595 to "G-Impact Force",       // BryweLudramon
        596 to "Direct Smasher",       // RagnaLoardmon
        597 to "Extinction Cloud",     // Proximamon
        598 to "Form Scratch",         // Raguelmon
        599 to "End of the World",     // Ordinemon
        1546 to "Red Solar Flare",     // Agumon (Bond of Courage)
        1547 to "Moonlight Fang",      // Gabumon (Bond of Friendship)
        1548 to "Asterion Flash",      // Achillesmon
        1549 to "Samadhi Shanti",      // Shivamon
        1550 to "Shadow Knuckle",      // Shroudmon
        1551 to "Zephyr Tornado",      // Zephagamon
        1552 to "Glass Needle",        // Cendrillmon
        1553 to "Raikou Gouka",        // Fenriloogamon: Takemikazuchi
        1554 to "Seven Victorize",     // Shoutmon X7
        1555 to "Astral Snatcher",     // Bagramon
        1556 to "Ichi-no-Tachi",       // Tactimon
        1557 to "Metal Cast",          // Death-X-DORUgoramon
        1558 to "Grey Cannon Alter",   // Omnimon Alter-B
        1559 to "Transcendent Sword Defeat", // Omnimon Zwart Defeat
        1560 to "Bloody Finish",       // BanchoStingmon
        1561 to "Twin Yo-Yo",          // BanchoLilimon
        1562 to "Golden Bat",          // BanchoMamemon
        1563 to "Ten-Thousand Ton Stamp" // BanchoGolemon
    )

    /**
     * Returns true if the given speciesId has an explicit canonical signature attack registered.
     */
    fun hasCanonicalAttack(speciesId: Int): Boolean = CANONICAL_ATTACKS.containsKey(speciesId)

    /**
     * Resolves the canonical signature attack for a given monster.
     * Incorporates attack tier (SINGLE, DOUBLE, MEGA_HIT) and critical state.
     */
    fun getAttackName(
        speciesId: Int,
        isCritical: Boolean = false,
        attackType: String = "SINGLE"
    ): String {
        val baseName = CANONICAL_ATTACKS[speciesId] ?: run {
            val species = PetSpecies.getById(speciesId)
            generateFallbackAttack(species)
        }

        return when {
            isCritical || attackType == "MEGA_HIT" -> "[ $baseName ]"
            attackType == "DOUBLE" -> ">> $baseName <<"
            else -> baseName
        }
    }

    private fun generateFallbackAttack(species: PetSpecies): String {
        val stagePrefix = when (species.stage) {
            PetStage.BABY_I, PetStage.BABY_II -> "Bubble"
            PetStage.CHILD -> "Claw"
            PetStage.ADULT -> "Burst"
            PetStage.PERFECT -> "Cannon"
            PetStage.ULTIMATE -> "Nova"
        }

        val attrName = when (species.attribute) {
            ElementalAttribute.VACCINE -> "Solar"
            ElementalAttribute.DATA -> "Matrix"
            ElementalAttribute.VIRUS -> "Chaos"
            ElementalAttribute.FREE -> "Miracle"
        }

        return "$attrName $stagePrefix"
    }
}
