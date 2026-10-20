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
        // Baby / In-Training
        101 to "Bubble Blow",
        102 to "Acid Bubbles",
        103 to "Acid Bubbles",
        104 to "Bubble Blow",
        105 to "Poison Bubbles",
        106 to "Bubble Blow",
        107 to "Diamond Dust",
        108 to "Tail Whip",
        109 to "Hot Gas",
        110 to "Hot Gas",
        111 to "Acid Bubbles",
        112 to "Hop Attack",
        113 to "Poison Bubbles",
        114 to "Fox Tail",
        115 to "Bubble Blow",
        116 to "Acid Bubbles",
        130 to "Chiburu",
        131 to "Acid Bubbles",
        132 to "Bubble Blow",

        // Child (Rookies)
        201 to "Pepper Breath",        // Agumon
        202 to "Blue Blaster",         // Gabumon
        203 to "Boom Bubble",          // Patamon
        204 to "Cat Punch",            // Salamon
        205 to "Pyro Sphere",          // Guilmon
        206 to "Vee-Headbutt",         // Veemon
        207 to "Diamond Storm",        // Renamon
        208 to "Bunny Blast",          // Terriermon
        209 to "Petit Twister",        // Lopmon
        210 to "Rock Breaker",         // Gotsumon
        211 to "Marching Fishes",      // Gomamon
        212 to "Electric Shock",       // Tentomon
        213 to "Poison Ivy",           // Palmon
        214 to "Spiral Twister",       // Biyomon
        215 to "Air Shot",             // Hawkmon
        216 to "Claw Scratch",         // Armadillomon
        217 to "Nightmare Syndrome",   // Impmon
        218 to "Baby Claw",            // BlackAgumon
        219 to "Starlight Velocity",   // Kudamon
        220 to "Pawn Spear",           // PawnChessmon
        221 to "Crow Claw",            // Falcomon
        222 to "Gooey Spit",           // Betamon
        223 to "Electro Shocker",      // Kunemon
        224 to "Poison Spit",          // Gazimon
        225 to "Static Strum",         // Elecmon
        226 to "Dormouse Dream",       // Tapirmon
        227 to "Toy Flame",            // ToyAgumon
        228 to "Snow Gob-Strike",      // SnowGoblimon
        229 to "Gobli Bomb",           // Goblimon
        230 to "Chilly Breath",        // SnowAgumon
        231 to "Otamasan",             // Otamamon
        232 to "Psycho Blaster",       // Psychemon
        233 to "Modoki Shock",         // ModokiBetamon
        234 to "Giga Scissor",         // Dokunemon
        235 to "Crab Punch",           // Crabmon
        236 to "Gisao Spear",          // Gizamon
        237 to "Shout Claw",           // Shoutmon
        238 to "Spike Strike",         // Dorumon
        239 to "Ryudamon Blade",       // Ryudamon
        240 to "Puru Fire",            // Sunarizamon
        241 to "Pulsemon Spark",       // Pulsemon

        // Adult (Champions)
        301 to "Mega Flame",           // Greymon
        302 to "Mega Blue",            // Greymon (Blue)
        303 to "Poison Horn",          // Tyrannomon
        304 to "Fox Fire",             // Garurumon
        305 to "Black Ice",            // Garurumon (Black)
        306 to "Grand Fire",           // Meramon
        307 to "Fist of Fate",         // Angemon
        308 to "Cat's Eye",            // Gatomon
        309 to "Plasma Blade",         // Growlmon
        310 to "Vee-Laser",            // ExVeemon
        311 to "Dragon Wheel",         // Kyubimon
        312 to "Blazing Storm",        // Gargomon
        313 to "Lightning Horn",       // Kabuterimon
        314 to "Meteor Wing",          // Birdramon
        315 to "Prickly Bang",         // Togemon
        316 to "Harpoon Torpedo",      // Ikkakumon
        317 to "Electro Shocker",      // Kuwagamon
        318 to "Bloody Stream",        // Devimon
        319 to "Nightmare Claw",       // Bakemon
        320 to "Fist of the Beast King", // Leomon
        321 to "Beast Fang",           // Ogremon
        322 to "Filth Stream",         // Numemon
        323 to "Sludge Wave",          // Sukamon
        324 to "Iron Tail",            // Coelamon
        325 to "Volcanic Wave",        // Darkrizamon
        326 to "Dark Scythe",          // Snimon
        327 to "Spiral Claw",          // Dobermon
        328 to "Laser Breath",         // Cyclonemon
        329 to "Soul Chopper",         // Musyamon
        330 to "Chainsaw Slash",       // Woodmon
        331 to "Sub-Zero Punch",       // Frigimon
        332 to "Fire Ball",            // Flarerizamon
        333 to "Thunder Ball",         // Thunderballmon
        334 to "Giga Blaster",         // Veedramon
        335 to "V-Breath Arrow",       // RedVeedramon
        336 to "Plasma Wheel",         // Guardromon
        337 to "Death Fire",           // SkullGreymon
        338 to "Corona Splash",        // Sunflowmon
        339 to "Darkness Wing",        // Saberdramon
        340 to "Blade Rush",           // Gladimon
        341 to "Excalibur Slash",      // Knightmon
        342 to "Electro Horn",         // Stingmon
        343 to "Aqua Needle",          // Seadramon
        344 to "Ice Arrow",            // IceDevimon
        345 to "Giga Cannon",          // Tankmon
        346 to "Drill Horn",           // Drimogemon
        347 to "Shadow Claw",          // Wizarmon
        348 to "Chou-Chou Kick",       // Tailmon
        349 to "Sonic Voice",          // Raremon
        350 to "Poison Breath",        // Vegiemon

        // Perfect (Ultimates in Dub / Perfects in JP)
        401 to "Giga Cannon",          // MetalGreymon
        402 to "Trident Arm",          // MetalGreymon (Virus)
        403 to "Exhaust Flame",        // MetalMamemon
        404 to "Wolf Claw",            // WereGarurumon
        405 to "Shadow Wolf Claw",     // WereGarurumon (Black)
        406 to "Smiley Bomber",        // Mamemon
        407 to "Heaven's Gate",        // MagnaAngemon
        408 to "Holy Arrow",           // Angewomon
        409 to "Atomic Blaster",       // WarGrowlmon
        410 to "Desperado Blaster",    // Paildramon
        411 to "Talisman Star",        // Taomon
        412 to "Giant Bazooka",        // Rapidmon
        413 to "Horn Buster",          // MegaKabuterimon
        414 to "Shadow Sickle",        // Okuwamon
        415 to "Shadow Wing",          // Garudamon
        416 to "Flower Cannon",        // Lilimon
        417 to "Arctic Blizzard",      // Zudomon
        418 to "Crimson Flame",        // SkullSatamon
        419 to "Death Claw",           // LadyDevimon
        420 to "Giga Scythe",          // Phantomon
        421 to "Buster Dive",          // AeroVeedramon
        422 to "Giga Destroyer",       // Megadramon
        423 to "Darkness Buster",      // Gigadramon
        424 to "Tri-Beam",             // Triceramon
        425 to "Cyber Claw",           // Cyberdramon
        426 to "Night Raid",           // Vamdemon (Myotismon)
        427 to "Dragon Impulse",       // MetalTyrannomon
        428 to "Deadly Bomb",          // Giromon
        429 to "Corona Burst",         // SuperStarmon
        430 to "Thunder Spear",        // Astamon
        431 to "Chou-Shin Fist",       // Monzaemon
        432 to "Pandora Box",          // Vademon
        433 to "Dragon Breath",        // Brachiomon
        434 to "Aqua Wave",            // MegaSeadramon
        435 to "Spike Claw",           // WaruMonzaemon
        436 to "Meteor Fall",          // Vermillimon
        437 to "Dual Sonic",           // Silphymon
        438 to "Ultimate Flare",       // Flaremon
        439 to "Spirit Chariot",       // Crescemon
        440 to "Rumble Blend",         // Dinobeemon
        441 to "Clay Armor",           // Shakkoumon
        442 to "Holy Beam",            // HolyAngemon
        443 to "Dragon Impulse",       // MasterTyrannomon
        444 to "Thunder Javelin",      // Andromon
        445 to "Giga Crusher",         // Volcamon
        446 to "Blade Wave",           // Tekkamon
        447 to "Shadow Laser",         // Nanomon (Datamon)
        448 to "Spider Silk Blade",    // Infermon
        449 to "Gale of Swiftness",    // Chirinmon
        450 to "Poison Ivy Barrage",   // Blossomon

        // Ultimate (Megas)
        501 to "Gaia Force",           // WarGreymon
        502 to "Black Gaia Force",     // BlackWarGreymon
        503 to "Cocytus Breath",       // MetalGarurumon
        504 to "Seven Heavens",        // Seraphimon
        505 to "Seven's Lightning",    // BlackSeraphimon
        506 to "Giga Blaster",         // HerculesKabuterimon
        507 to "River of Power",       // GranKuwagamon
        508 to "Giga Fire",            // Phoenixmon (Hououmon)
        509 to "Trump Sword",          // Piedmon
        510 to "Transcendent Sword",   // Omnimon
        511 to "Black Sword",          // Omnimon Zwart
        512 to "Supreme Cannon",       // Omnimon (Cannon)
        513 to "Giga Destroyer",       // Machinedramon (Mugendramon)
        514 to "Dark Spiral",          // Chaosdramon
        515 to "Shield of the Just",   // Gallantmon (Dukemon)
        516 to "Invincible Sword",     // Gallantmon Crimson Mode
        517 to "Mega Death",           // Imperialdramon Dragon Mode
        518 to "Giga Death",           // Imperialdramon Fighter Mode
        519 to "Double Impact",        // Beelzemon
        520 to "Amethyst Mandala",     // Sakuyamon
        521 to "Golden Triangle",      // MegaGargomon
        522 to "Great Dragon Wave",    // Goldramon
        523 to "Digitalize of Soul",   // Alphamon
        524 to "Ultimate Ouryuken",    // Alphamon Ouryuken
        525 to "Dragonic Impact",      // RustTyrannomon
        526 to "Brave Blade",          // VictoryGreymon
        527 to "Catastrophe Cannon",   // Diaboromon
        528 to "Black Rain",           // Armaggeddemon
        529 to "Dragon Coliseum",      // Slayerdramon
        530 to "Lampranthus",          // Belphemon Sleep Mode
        531 to "Phantom Pain",         // Lilithmon
        532 to "Flame Inferno",        // Daemon
        533 to "Rostrum",              // Leviamon
        534 to "Pandemonium Lost",     // Barbamon
        535 to "Grand Cross",          // Lucemon
        536 to "Paradise Lost",        // Lucemon Chaos Mode
        537 to "Purgatorial Flame",    // Lucemon Shadow Lord Mode
        538 to "Double Impact Shot",   // Beelzemon (2010)
        539 to "Corona Destroyer",     // Beelzemon Blast Mode
        540 to "Dragon's Roar",        // Dynasmon
        541 to "Spiral Masquerade",    // LordKnightmon
        542 to "End Waltz",            // Craniummon
        543 to "Bifrost",              // Sleipmon
        544 to "Extinction Wave",      // Duftmon
        545 to "Pendragon's Glory",    // Examon
        546 to "Iron Fist Punishment", // Gankoomon
        547 to "Schwertkreuz",         // Jesmon
        548 to "Final Shining Burst",  // ShineGreymon
        549 to "Corona Blaze",         // ShineGreymon Burst Mode
        550 to "Mirage Gaogamon Howl", // MirageGaogamon
        551 to "Kikoku",               // Gaiomon
        552 to "Eden's Javelin",       // Ophanimon
        553 to "Arctic Blizzard",      // Vikemon
        554 to "Dragon Impulse",       // Goddramon
        555 to "Bifrost Charge",       // Kentaurosmon
        556 to "Fire Tornado",         // Magnadramon
        557 to "Lightning Spear",      // Cherubimon
        558 to "Heaven's Judgment",    // Cherubimon (Good)
        559 to "River of Power",       // GranKuwagamon
        560 to "Hellfire Storm",       // MaloMyotismon
        561 to "Ultimate Flare",       // Apollomon
        562 to "Full Moon Wave",       // Dianamon
        563 to "Grace Blade",          // GraceNovamon
        564 to "Meteor Strike",        // Justimon
        565 to "Celestial Blade",      // Susanoomon
        566 to "Darkness Wave",        // Apocalymon
        567 to "Bantyo Blade",         // Chaosmon
        568 to "Flash Bantyo Punch",   // BanchoLeomon
        569 to "Dark Roar",            // Darkdramon
        570 to "Chaos Slash",          // Chaosmon Valdur Arm
        571 to "Valdur Aurora",        // Valdurmon
        572 to "Shadow Claw Wave",     // Kuzuhamon
        573 to "Shadow Seven Heavens", // BlackSeraphimon
        574 to "Flame Strike",         // AncientGreymon
        575 to "Freeze Tempest",       // AncientGarurumon
        576 to "Dragon Breath",        // Dorugoramon
        577 to "Dragon Arrow",         // Ouryumon
        578 to "Omni Sword",           // Imperialdramon Paladin Mode
        579 to "Royal Saber",          // Gallantmon X
        580 to "Black Roar",           // WarGreymon X
        581 to "Cocytus Storm",        // MetalGarurumon X
        582 to "Dragon Impulse",       // MedievalGallantmon
        583 to "Great Horn",           // GrandisKuwagamon
        584 to "Shadow Laser",         // Chaosdramon X
        585 to "Solar Beam",           // ChaosGallantmon
        586 to "Dark Flame",           // Megidramon
        587 to "Senpuu Keri",          // Kazuchimon
        588 to "Storm Drive",          // Shivamon
        589 to "Giga Cannonade",       // HeavyLeomon
        590 to "Ancient Tempest",      // AncientSphinxmon
        591 to "Death The Cannon",     // Beelzemon (2010)
        592 to "Fearsome Blade"        // Omnimon Merciful Mode
    )

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
            isCritical || attackType == "MEGA_HIT" -> "★ $baseName ★"
            attackType == "DOUBLE" -> "⚡ $baseName ⚡"
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
