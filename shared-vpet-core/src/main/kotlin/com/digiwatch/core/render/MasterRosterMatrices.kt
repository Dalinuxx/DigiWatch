package com.digiwatch.core.render

/**
 * Dot-matrix pixel art matrices and palettes for the 95 master expansion species (#123..#129, #232..#250, #350..#372, #444..#464, #558..#582).
 * Every sprite is a 16x16 authentic dot-matrix array with distinct silhouette, outline, and multi-tone shading.
 * Together with the base 205 species, DigiWatch features exactly 300 unique canonical Digimon.
 */
object MasterRosterMatrices {

    // =========================================================================
    // Stage 2: Baby II (123 - 129)
    // =========================================================================

    // [ID 123] Tsumemon (spiky droplet with red jagged hair)
    val TSUMEMON_ROM = arrayOf(
        ".......TT.......",
        "......BTTB......",
        ".....BT##TB.....",
        "....B######B....",
        "...B#T####T#B...",
        "..B###WW##WW#B..",
        "..B##WRE##WREB..",
        "..B###SS##SS#B..",
        "..B#BBBBBBBB#B..",
        "..B#BWWWWWWBB...",
        "..B#BRRRRRR#B...",
        "...B########B...",
        "....B######B....",
        ".....BB##BB.....",
        "......B##B......",
        ".......BB......."
    )

    // [ID 124] Hopmon (bouncy horned blob)
    val HOPMON_ROM = arrayOf(
        "....TT....TT....",
        "...BTTB..BTTB...",
        "..B#TTB..BTT#B..",
        "..B##########B..",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        ".B############B.",
        ".B############B.",
        ".B###SSSSSS###B.",
        "..B#SSSSSSSS#B..",
        "...B#SSSSSS#BBB.",
        "....B######B##TB",
        "....B######B#TB.",
        "...B########BB..",
        "..BB#BB..BB#B...",
        "..BBB......BB..."
    )

    // [ID 125] Kapurimon (metallic horned helmet with dual horns)
    val KAPURIMON_ROM = arrayOf(
        "..TT........TT..",
        ".BTTB......BTTB.",
        "..BTTB....BTTB..",
        "...B########B...",
        "..BTTTTTTTTTTB..",
        ".BTTTTTTTTTTTTB.",
        ".BTBBBBBBBBBBTB.",
        ".BTBWW####WWTB..",
        ".BTBEE####EETB..",
        ".BTBBBBBBBBBBTB.",
        "..BTTTTTTTTTTB..",
        "...B########B...",
        "....B######B....",
        ".....BSSSSB.TT..",
        "....BB####BB.TB.",
        "....BB....BB..B."
    )

    // [ID 126] Minomon (hanging leaf pinecone)
    val MINOMON_ROM = arrayOf(
        ".......TT.......",
        ".......TB.......",
        "......BTTB......",
        "....BB#TT#BB....",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        "..B##########B..",
        ".B#TT######TT#B.",
        ".BTTTT#TT#TTTTB.",
        "..BTTTTTTTTTTB..",
        "...BTT#TT#TTB...",
        "....BTTTTTTB....",
        ".....BTTTTB.....",
        "......BTTB......",
        ".......BB......."
    )

    // [ID 127] Sunmon (shining sun with flame tips)
    val SUNMON_ROM = arrayOf(
        "...TT..TT..TT...",
        "....TT.TT.TT....",
        "..TT.B####B.TT..",
        "...TB######BT...",
        ".TTB########BTT.",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".TB##########BT.",
        ".TB##########BT.",
        "..B###SSSS###B..",
        ".TTB#SSSSSS#BTT.",
        "...TB######BT...",
        "..TT.B####B.TT..",
        "....TT.TT.TT....",
        "...TT..TT..TT...",
        "................"
    )

    // [ID 128] Moonmon (drooping crescent bunny ears)
    val MOONMON_ROM = arrayOf(
        "....TT....TT....",
        "...BTTB..BTTB...",
        "..BTTTB..BTTTB..",
        "..B#TTB..BTT#B..",
        ".B############B.",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        ".B############B.",
        ".B#SS######SS#B.",
        "..B#SSSSSSSS#B..",
        "..B#SSSSSSSS#B..",
        "...B########B...",
        "....B######B....",
        ".....BB##BB.....",
        "......B##B......",
        ".......BB......."
    )

    // [ID 129] Ketomon (tiny dragon droplet with single crown horn)
    val KETOMON_ROM = arrayOf(
        ".......TT.......",
        "......BTTB......",
        ".....B#TT#B.....",
        "....B######B....",
        "...B##TTTT##B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        ".B###TT##TT###B.",
        "..B##########B..",
        "..B#SSSSSSSS#B..",
        "...BSSSSSSSSB.B.",
        "....B######B.#TB",
        ".....BB##BB..BTB",
        "......B##B....BB",
        ".......BB......."
    )

    // =========================================================================
    // Stage 3: Child / Rookie (232 - 250)
    // =========================================================================

    // [ID 232] Gomamon (white seal with crimson mohawk)
    val GOMAMON_ROM = arrayOf(
        ".....TTTT.......",
        "....BTTTTB......",
        "...B#TTTT#B.....",
        "..B########B....",
        ".B##WW##WW##B...",
        ".B##EE##EE##B...",
        "B############B..",
        "B#SS######SS#B..",
        "B#SSSSSSSSSS#B..",
        ".B#SSSSSSSS#B...",
        "BB#SSSSSSSS#BB..",
        "BTTB#SSSS#BTTB..",
        "BTTTB####BTTTB..",
        ".BBBB####BBBB...",
        "....B####B.TTB..",
        "....BBBBBB.BB..."
    )

    // [ID 233] Gotsumon (rock boulder creature with jagged head)
    val GOTSUMON_ROM = arrayOf(
        "....BB..BB......",
        "...B##BB##B.....",
        "..B########B....",
        ".B##########B...",
        ".B#TT####TT#B...",
        ".B#WW####WW#B...",
        ".B#EE####EE#B...",
        "B############B..",
        "B##SS####SS##B..",
        "B#SSSSSSSSSS#B..",
        ".B##SSSSSS##B...",
        "..B#SSSSSS#B....",
        ".BB#SS##SS#BB...",
        "B###B#SS#B###B..",
        "B###B....B###B..",
        ".BBB......BBB..."
    )

    // [ID 234] Otamamon (blue tadpole with musical spiral belly)
    val OTAMAMON_ROM = arrayOf(
        ".....BBBB.......",
        "...BB####BB.....",
        "..B########B....",
        ".B##########B...",
        ".B##WW##WW##B...",
        ".B##EE##EE##B...",
        "B############B..",
        "B##WWWWWWWW##B..",
        "B#W########W#B..",
        ".B#W######W#B...",
        "..B#WWWWWW#B....",
        "...B######B.....",
        "..BB######BB.TT.",
        ".B##B####B##BTB.",
        ".B##B....B##BBTB",
        "..BB......BB..BB"
    )

    // [ID 235] Candlemon (wax candle with blazing flame)
    val CANDLEMON_ROM = arrayOf(
        ".......RR.......",
        "......BRRB......",
        ".....BRWWRB.....",
        "....BRWWWWWRB...",
        ".....BRWWRB.....",
        "......BRRB......",
        "....BBTTTTBB....",
        "...BTTTTTTTTB...",
        "...B########B...",
        "...B##WW##WW#B..",
        "...B##EE##EE#B..",
        "...B########B...",
        "...B#T####T#B...",
        "...B########B...",
        "..B#T######T#B..",
        "..BBBBBBBBBBBB.."
    )

    // [ID 236] Bakumon (tapir dream-eater with sacred collar)
    val BAKUMON_ROM = arrayOf(
        "....BBBB........",
        "...B####BB......",
        "..B#######B.....",
        ".B#########B....",
        ".B##WW#####B....",
        ".B##EE#####B....",
        "B###########B...",
        "B############B..",
        ".B#TTTTTTTT#B...",
        ".B#T######T#B...",
        "..B#SSSSSS#B.TT.",
        "...B######B.BTTB",
        "..BB######B..TB.",
        ".B##B####B#B....",
        ".B##B....B##B...",
        "..BB......BB...."
    )

    // [ID 237] Hagurumon (spinning gear creature with dark iris)
    val HAGURUMON_ROM = arrayOf(
        ".....BB..BB.....",
        "....BTTBBTTB....",
        "..BBB######BBB..",
        ".BTT########TTB.",
        ".BT##WW##WW##TB.",
        "..B##EE##EE##B..",
        "BBB##########BBB",
        "BTT##########TTB",
        "BTT##BB##BB##TTB",
        "BBB#B##BB##B#BBB",
        "..B##BBBBBB##B..",
        ".BT##########TB.",
        ".BTT########TTB.",
        "..BBB######BBB..",
        "....BTTBBTTB....",
        ".....BB..BB....."
    )

    // [ID 238] Mushroomon (mushroom-capped imp)
    val MUSHROOMON_ROM = arrayOf(
        "......BBBB......",
        "....BB####BB....",
        "...B#TT##TT#B...",
        "..B##########B..",
        ".B#TT######TT#B.",
        ".B############B.",
        "BBBBBBBBBBBBBBBB",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        "..B##########B..",
        ".B##BBBBBBBB##B.",
        ".B#B########B#B.",
        "..BB########BB..",
        "...B#B####B#B...",
        "...BBB....BBB..."
    )

    // [ID 239] Crabmon (armored crab with heavy pincers)
    val CRABMON_ROM = arrayOf(
        "....B......B....",
        "...BWB....BWB...",
        ".BBBTTBBBBTTBBB.",
        "BTTTT########TTB",
        "BTTTT#WW##WW#TTB",
        ".BBTT#EE##EE#BB.",
        "..B############B",
        ".B##############",
        "B#B#SSSSSSSSSS#B",
        "B#B#SSSSSSSSSS#B",
        ".B.B#SSSSSSSS#B.",
        "...BB########BB.",
        "..B#B#B####B#B#B",
        ".B#B.B......B.B#",
        ".BB............B",
        "................"
    )

    // [ID 240] Syakomon (clam shell with blushing face)
    val SYAKOMON_ROM = arrayOf(
        ".......TT.......",
        "....TT.TT.TT....",
        "...BTTBTTBTTB...",
        "..BTTTTTTTTTTB..",
        ".BTT########TTB.",
        ".BT##WW##WW##TB.",
        "BTT##EE##EE##TTB",
        "BTT##########TTB",
        "BTT##SSSSSS##TTB",
        ".BT#SSSSSSSS#TB.",
        ".BTT#SSSSSS#TTB.",
        "..BTTTTTTTTTTB..",
        "...BTTBTTBTTB...",
        "....TT.TT.TT....",
        ".......TT.......",
        "................"
    )

    // [ID 241] Lalamon (floating pink bud with green sprout propeller)
    val LALAMON_ROM = arrayOf(
        "....TT....TT....",
        ".....TT..TT.....",
        "......BTTB......",
        ".....B####B.....",
        "....B######B....",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        ".B############B.",
        ".B###SSSSSS###B.",
        "..B#SSSSSSSS#B..",
        "...B########B...",
        "....B######B....",
        ".....BB##BB.....",
        ".....BB..BB....."
    )

    // [ID 242] Falcomon (ninja falcon with kunai crest)
    val FALCOMON_ROM = arrayOf(
        ".....BBBB.......",
        "....BTTTTB......",
        "...B#TTTT#B.....",
        "..B########B....",
        "..B##WW####B....",
        "..B##EE####B....",
        ".B####W#####B...",
        ".B###WWW####B...",
        "B############B..",
        "B#TT######TT#B..",
        "B#TT#SSSS#TT#B..",
        ".B#TSSSSSS#B....",
        "..B########B....",
        "...BB#BB#BB.....",
        "..BTTB..BTTB....",
        "..BBB....BBB...."
    )

    // [ID 243] Kudamon (holy ferret wrapped in gold sacred cartridge)
    val KUDAMON_ROM = arrayOf(
        ".......BBBB.....",
        "......B####B....",
        ".....B######B...",
        "....B##WW####B..",
        "....B##EE####B..",
        "...B##########B.",
        "...B##########B.",
        "..BBTTTTTTTTTTBB",
        ".BTTTTTTTTTTTTTB",
        ".BTT#TT#TT#TTTTB",
        ".BTTTTTTTTTTTTTB",
        "..BBTTTTTTTTTTBB",
        "...B##########B.",
        "...B##########B.",
        "....B########B..",
        ".....BBBBBBBB..."
    )

    // [ID 244] Ryudamon (samurai horned dragon cub)
    val RYUDAMON_ROM = arrayOf(
        "...TT......TT...",
        "....TT....TT....",
        ".....BTTTTB.....",
        "....B######B....",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        ".B#TT######TT#B.",
        ".B#TT#SSSS#TT#B.",
        "..B#SSSSSSSS#B..",
        "...B########B...",
        "..BB########BB..",
        ".B##B######B##B.",
        ".B##B......B##B.",
        "..BB........BB.."
    )

    // [ID 245] Dracomon (progenitor winged dragon)
    val DRACOMON_ROM = arrayOf(
        "....TT....TT....",
        "...BTTB..BTTB...",
        "..B####BB####B..",
        ".B############B.",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        "B##############B",
        "B######TT######B",
        "B#TT########TT#B",
        ".B#T#SSSSSS#T#B.",
        "..B#SSSSSSSS#B..",
        "...B########B.TT",
        "..BB########BBTB",
        ".B##B######B##TB",
        ".B##B......B##BB",
        "..BB........BB.."
    )

    // [ID 246] Kamemon (cyborg mouse turtle)
    val KAMEMON_ROM = arrayOf(
        ".....BBBB.......",
        "....B####B......",
        "...B######B.....",
        "..B##WW####B....",
        "..B##EE####B....",
        ".B##########B...",
        "BBTTTTTTTTTTBB..",
        "BTTTTTTTTTTTTB..",
        "BTT##TT##TT#TTB.",
        "BTTTTTTTTTTTTTB.",
        "BTT##TT##TT#TTB.",
        ".BTTTTTTTTTTTB..",
        "..BBTTTTTTTTBB..",
        "...B########B...",
        "..BB#BB..BB#BB..",
        "..BBB......BBB.."
    )

    // [ID 247] Coronamon (sun flame lion cub)
    val CORONAMON_ROM = arrayOf(
        "...TT..TT..TT...",
        "..BTTBBTTBBTTB..",
        ".BTT########TTB.",
        ".BT##WW##WW##TB.",
        ".BT##EE##EE##TB.",
        "BTT##########TTB",
        "BTT##########TTB",
        ".BT##SSSSSS##TB.",
        "..B#SSSSSSSS#B..",
        "...B#SSSSSS#B...",
        "..B##########B..",
        ".B##B######B##B.",
        ".B##B......B##BT",
        "..BB........BBTB",
        "...............B",
        "................"
    )

    // [ID 248] Lunamon (moon rabbit with drooping crescent ears)
    val LUNAMON_ROM = arrayOf(
        "..TT........TT..",
        ".BTTB......BTTB.",
        ".B#TTB....BTT#B.",
        ".B##TB....BT##B.",
        "..B##########B..",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        ".B############B.",
        "..B#TTTTTTTT#B..",
        "..B#T######T#B..",
        "...B#SSSSSS#B...",
        "..B##########B..",
        ".B##B######B##B.",
        ".B##B......B##B.",
        "..BB........BB..",
        "................"
    )

    // [ID 249] ToyAgumon (brick constructed toy dinosaur)
    val TOYAGUMON_ROM = arrayOf(
        "....TT....TT....",
        "....TT....TT....",
        "...BBBBBBBBBB...",
        "..B##########B..",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        "..B##########B..",
        "..BBBBBBBBBBBB..",
        "...B########B...",
        "..B#TT####TT#B..",
        "..B#TT####TT#B..",
        "..B##########B..",
        "..BBBBBBBBBBBB..",
        ".B####B..B####B.",
        ".B####B..B####B.",
        ".BBBBBB..BBBBBB."
    )

    // [ID 250] Keramon (wireframe tentacle rookie)
    val KERAMON_ROM = arrayOf(
        ".......TT.......",
        "......BTTB......",
        ".....B####B.....",
        "....B######B....",
        "...B#WW##WW#B...",
        "...B#EE##EE#B...",
        "..B##########B..",
        ".B#BBBBBBBBBB#B.",
        ".B#BWWWWWWWW#B..",
        "..B#BBBBBBBB#B..",
        "...B########B...",
        "....B######B....",
        "B....B####B....B",
        "B#B..B####B..B#B",
        ".B#BB.B##B.BB#B.",
        "..BBB..BB..BBB.."
    )

    // =========================================================================
    // Stage 4: Adult / Champion (350 - 372)
    // =========================================================================

    // [ID 350] Ikkakumon (giant walrus with crimson mane and harpoon horn)
    val IKKAKUMON_ROM = arrayOf(
        "......TTTT......",
        ".....BTTTTB.....",
        "....B#TTTT#B....",
        "...B########B...",
        "..B##WW######B..",
        "..B##EE######B..",
        ".B############B.",
        ".B#WWWWWWWWWW#B.",
        "B##############B",
        "B##SSSSSSSSSS##B",
        ".B#SSSSSSSSSS#B.",
        "..B#SSSSSSSS#B..",
        "..BB########BB..",
        ".B#B########B#B.",
        "BRRB.BB##BB.BRRB",
        "BBBB..BBBB..BBBB"
    )

    // [ID 351] Golemon (rock colossus with magma fissures)
    val GOLEMON_ROM = arrayOf(
        "....BBBBBBBB....",
        "...B########B...",
        "..B##########B..",
        "..B####RR####B..",
        "..B##########B..",
        ".BBBBBBBBBBBBBB.",
        "B##############B",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..BB#SSSSSS#BB..",
        ".B##B######B##B.",
        "B###B.BBBB.B###B",
        "BBBBB.B##B.BBBBB",
        "......BBBB......"
    )

    // [ID 352] Gekomon (horned musical amphibian)
    val GEKOMON_ROM = arrayOf(
        "...BB......BB...",
        "..B##B....B##B..",
        ".B####BBBB####B.",
        ".B############B.",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        "BBTTTTTTTTTTTTBB",
        "BTTTTTTTTTTTTTTB",
        ".BTT########TTB.",
        "..B#SSSSSSSS#B..",
        "..B#SSSSSSSS#B..",
        "...B########B...",
        "..B##########B..",
        ".B##B######B##B.",
        "BWWWBB....BBWWWB",
        "BBBBBB....BBBBBB"
    )

    // [ID 353] Clockmon (mechanical timekeeper robot)
    val CLOCKMON_ROM = arrayOf(
        ".......TT.......",
        "......BTTB......",
        "....BB####BB....",
        "...B########B...",
        "..B##########B..",
        "..B##WWWWWW##B..",
        "..B##W#TT#W##B..",
        "..B##WWWWWW##B..",
        "..B##########B..",
        "...B###TT###B...",
        "....B##TT##B....",
        "...BB##TT##BB...",
        "..BTTB#TT#B##B..",
        ".BTTTTBTTB####B.",
        ".BTTTTBBBB####B.",
        "..BBBB....BBBB.."
    )

    // [ID 354] Guardromon (heavily armored defensive cyborg)
    val GUARDROMON_ROM = arrayOf(
        "..TT........TT..",
        "..TTB......BTT..",
        "...B########B...",
        "..B##########B..",
        "..B####RR####B..",
        "..B##########B..",
        ".BBBBBBBBBBBBBB.",
        "B##############B",
        "B#SS########SS#B",
        "B#SS#TT##TT#SS#B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B###BB##BB###B.",
        ".B##B..BB..B##B.",
        ".B##B......B##B.",
        ".BBBB......BBBB."
    )

    // [ID 355] Woodmon (forest treant with hollow branch eyes)
    val WOODMON_ROM = arrayOf(
        ".TT..........TT.",
        "..TTB......BTT..",
        "...B########B...",
        "..B##########B..",
        ".B############B.",
        ".B##BB####BB##B.",
        ".B##BB####BB##B.",
        ".B############B.",
        ".B##BBBBBBBB##B.",
        ".B##B######B##B.",
        ".B############B.",
        "..B#SSSSSSSS#B..",
        "..B#SSSSSSSS#B..",
        ".B#B#B####B#B#B.",
        "B#B..B####B..B#B",
        "BB....BBBB....BB"
    )

    // [ID 356] Octomon (ink pot octopus)
    val OCTOMON_ROM = arrayOf(
        ".....BTTTTB.....",
        "....BTTTTTTB....",
        "...BTT####TTB...",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        "..B##########B..",
        ".B############B.",
        "B#B##########B#B",
        "B#B#SSSSSSSS#B#B",
        ".B.B#SSSSSS#B.B.",
        "..BB#SSSSSS#BB..",
        ".B#B#B####B#B#B.",
        "B#B..B#BB#B..B#B",
        "B#B..B#BB#B..B#B",
        ".B....BB..BB...B"
    )

    // [ID 357] Gesomon (white squid with abyssal tentacles)
    val GESOMON_ROM = arrayOf(
        ".......BB.......",
        "......B##B......",
        ".....B####B.....",
        "....B######B....",
        "...B########B...",
        "..B##########B..",
        ".B############B.",
        "B##############B",
        "B##WW######WW##B",
        "B##EE######EE##B",
        ".B############B.",
        ".B#B#B#BB#B#B#B.",
        "B#B#B#B..B#B#B#B",
        "B#B#B#B..B#B#B#B",
        ".B.B.B....B.B.B.",
        "................"
    )

    // [ID 358] Ebidramon (shrimp sea dragon with long antennae)
    val EBIDRAMON_ROM = arrayOf(
        ".TT..........TT.",
        "..TTB......BTT..",
        "...B########B...",
        "..B##########B..",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        "B##############B",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...B#SSSSSS#B.TT",
        "..BB#SSSSSS#BBTB",
        ".B##B######B##TB",
        ".BBBB......BBBBB"
    )

    // [ID 359] Raidramon (blue lightning quadruped beast)
    val RAIDRAMON_ROM = arrayOf(
        "........TT......",
        ".......BTTB.....",
        "......BTTTTB....",
        ".....B##TT##B...",
        "....B########B..",
        "...B##WW######B.",
        "...B##EE######B.",
        "..B############B",
        ".B#TT########TTB",
        "B##TT#SSSSSS#TTB",
        "B##############B",
        ".B############B.",
        ".B#B########B#B.",
        "B##B#B####B#B##B",
        "B##B.B####B.B##B",
        "BBBB..BBBB..BBBB"
    )

    // [ID 360] Digmon (golden drill beetle)
    val DIGMON_ROM = arrayOf(
        "......TTTT......",
        ".....BTTTTB.....",
        "....B#TTTT#B....",
        "...B########B...",
        "..B##########B..",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        "B##############B",
        "B#WW########WW#B",
        "B#TT#SSSSSS#TT#B",
        "B#TT#SSSSSS#TT#B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".BB##B####B##BB.",
        "B##B.B####B.B##B",
        "BBBB..BBBB..BBBB"
    )

    // [ID 361] Shurimon (ninja leaf shuriken fighter)
    val SHURIMON_ROM = arrayOf(
        ".......TT.......",
        "......BTTB......",
        ".....BTTTTB.....",
        "....B######B....",
        "..TTB######BTT..",
        ".BTT#WW##WW#TTB.",
        "BTTT#EE##EE#TTTB",
        "BTTT########TTTB",
        ".BTT#SSSSSS#TTB.",
        "..TTB######BTT..",
        "....B######B....",
        "...B########B...",
        "..B#B######B#B..",
        ".B##B......B##B.",
        ".B#B........B#B.",
        ".BBB........BBB."
    )

    // [ID 362] Submarimon (yellow submarine companion)
    val SUBMARIMON_ROM = arrayOf(
        ".......TT.......",
        ".......TTB......",
        "......BTTB......",
        "....BB####BB....",
        "...B########B...",
        "..B##########B..",
        ".B##WWWWWWWW##B.",
        ".B##WCCCCCCW##B.",
        "B###WWWWWWWW###B",
        "B##############B",
        "B##SSSSSSSSSS##B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...BB######BB.TT",
        "....BB####BB..TT",
        "......BBBB......"
    )

    // [ID 363] Peckmon (ninja avian runner)
    val PECKMON_ROM = arrayOf(
        "....BBBB........",
        "...BTTTTB.......",
        "..B#TTTT#B......",
        "..B#######B.....",
        ".B##WW####B.....",
        ".B##EE####B.....",
        ".B###WW###B.....",
        ".B####WW##B.....",
        "B##########B....",
        "B#TT####TT#B....",
        ".B#T#SS#T#B.....",
        "..B#SSSS#B......",
        "...B####B.......",
        "..B#B..B#B......",
        ".B##B..B##B.....",
        ".BBB....BBB....."
    )

    // [ID 364] Sunflowmon (sunflower blooming fairy)
    val SUNFLOWMON_ROM = arrayOf(
        "...TT..TT..TT...",
        "..BTTBBTTBBTTB..",
        ".BTT########TTB.",
        ".BT##WW##WW##TB.",
        ".BT##EE##EE##TB.",
        "BTT##########TTB",
        "BTT##########TTB",
        ".BT##SSSSSS##TB.",
        "..B#SSSSSSSS#B..",
        "...B#SSSSSS#B...",
        "..B##########B..",
        ".B##B######B##B.",
        ".B##B......B##B.",
        "..BB........BB..",
        "....TT....TT....",
        ".....TT..TT....."
    )

    // [ID 365] Reppamon (blade-tailed sacred fox)
    val REPPAMON_ROM = arrayOf(
        ".....BBBB.......",
        "....B####B......",
        "...B######B.....",
        "..B##WW####B....",
        "..B##EE####B....",
        ".B##########B...",
        "B############B..",
        "B#############WW",
        "B#SSSSSSSSSSSSWW",
        ".B#SSSSSSSSSS#TT",
        "..B##########BTT",
        ".B####BB####B#TT",
        "B####B..B####BTT",
        "B####B..B####BBB",
        "BBBBBB..BBBBBB..",
        "................"
    )

    // [ID 366] Ginryumon (armored samurai dragon)
    val GINRYUMON_ROM = arrayOf(
        "....TT....TT....",
        "...BTTB..BTTB...",
        "..B####BB####B..",
        ".B############B.",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        "B##############B",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".BB##########BB.",
        "B##B#B####B#B##B",
        "B##B.B####B.B##B",
        "BBBB..BBBB..BBBB"
    )

    // [ID 367] Coredramon (winged sapphire dragon)
    val COREDRAMON_ROM = arrayOf(
        ".TT..........TT.",
        ".BTTB......BTTB.",
        "..BTTB....BTTB..",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "..BB########BB..",
        ".B##B#BB##B#B##B",
        "B###B......B###B",
        "BBBBB......BBBBB"
    )

    // [ID 368] Chrysalimon (tentacled cocoon entity)
    val CHRYSALIMON_ROM = arrayOf(
        "TT............TT",
        "BTTB........BTTB",
        ".BTTB......BTTB.",
        "..B##########B..",
        ".B############B.",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        "B##############B",
        "B##SSSSSSSSSS##B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...B########B...",
        "....B######B....",
        ".....BB##BB.....",
        "......B##B......",
        ".......BB......."
    )

    // [ID 369] Firamon (sun mane fire lion)
    val FIRAMON_ROM = arrayOf(
        "...TT..TT..TT...",
        "..BTTBBTTBBTTB..",
        ".BTT########TTB.",
        ".BT##WW##WW##TB.",
        ".BT##EE##EE##TB.",
        "BTT##########TTB",
        "BTT#TT####TT#TTB",
        ".BT#TT#SS#TT#TB.",
        "..B#SSSSSSSS#B..",
        "...B#SSSSSS#B...",
        "..B##########B..",
        ".BB##B####B##BB.",
        "B##B.B####B.B##B",
        "B##B..BBBB..B##B",
        "BBBB........BBBB",
        "................"
    )

    // [ID 370] Lekismon (moon boxer rabbit)
    val LEKISMON_ROM = arrayOf(
        "..TT........TT..",
        ".BTTB......BTTB.",
        ".B#TTB....BTT#B.",
        ".B##TB....BT##B.",
        "..B##########B..",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        ".B############B.",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...B########B...",
        "..B#B######B#B..",
        ".B##B......B##B.",
        ".BBB........BBB."
    )

    // [ID 371] ExVeemon (pure dragon champion with chest X)
    val EXVEEMON_ROM = arrayOf(
        ".......TT.......",
        "......BTTB......",
        ".....B####B.....",
        "....B######B....",
        "...B##WW####B...",
        "...B##EE####B...",
        "..B##########B..",
        ".B#TT######TT#B.",
        "B##TT#W##W#TT##B",
        "B#####WWWW#####B",
        ".B####W##W####B.",
        "..B#SSSSSSSS#B..",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "BBBBBB..BBBBBB.."
    )

    // [ID 372] Lynxmon (flaming feral feline)
    val LYNXMON_ROM = arrayOf(
        "..TT........TT..",
        ".BTTB......BTTB.",
        "..B##########B..",
        ".B############B.",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        "B##############B",
        "B##SSSSSSSSSS##B",
        "B#SSSSSSSSSSSS#B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".BB##########BB.",
        "B##B#B####B#B##B",
        "B##B.B####B.B##B",
        "BBBB..BBBB..BBBB",
        "................"
    )

    // =========================================================================
    // Stage 5: Perfect / Ultimate (444 - 464)
    // =========================================================================

    // [ID 444] ShogunGekomon (massive warlord amphibian with pipe organs)
    val SHOGUNGEKOMON_ROM = arrayOf(
        "..TT..TTTT..TT..",
        ".BTTB.BTTB.BTTB.",
        ".BTTB.B##B.BTTB.",
        "BBTTBB####BBTTBB",
        "BTTT#WW##WW#TTTB",
        "BTTT#EE##EE#TTTB",
        "BBTT########TTBB",
        ".B##############",
        "B###TTTTTTTT###B",
        "B##T#SSSSSS#T##B",
        "B##SSSSSSSSSS##B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "BBBBBB..BBBBBB.."
    )

    // [ID 445] Volcamon (magma rock android)
    val VOLCAMON_ROM = arrayOf(
        "....RRTTTTRR....",
        "...BRRTTTTRRB...",
        "..B##########B..",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        ".B############B.",
        "B##############B",
        "B#WW########WW#B",
        "B#WW#SSSSSS#WW#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".BB##########BB.",
        "B###B.BBBB.B###B",
        "B###B.B##B.B###B",
        "BBBBB.BBBB.BBBBB"
    )

    // [ID 446] MarineDevimon (demon deep-sea squid)
    val MARINEDEVIMON_ROM = arrayOf(
        ".TT..........TT.",
        ".BTTB......BTTB.",
        "..BTTB....BTTB..",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        ".B###SSSSSS###B.",
        "..B#SSSSSSSS#B..",
        ".B#B#B#BB#B#B#B.",
        "B#B.B#B..B#B.B#B",
        "B#B..B....B..B#B",
        ".B............B.",
        "................"
    )

    // [ID 447] WaruSeadramon (dark serrated sea serpent)
    val WARUSEADRAMON_ROM = arrayOf(
        ".......TT.......",
        "......BTTB......",
        ".....BTTTTB.....",
        "....B######B....",
        "...B##WW####B...",
        "...B##EE####B...",
        "..B##########B..",
        ".B#TT######TT#B.",
        "B##TT######TT##B",
        "B#SSSSSSSSSSSS#B",
        ".B#SSSSSSSSSS#B.",
        "..BB#SSSSSS#BB..",
        ".B##B######B##B.",
        "B###BB####BB###B",
        "B###B......B###B",
        "BBBBB......BBBBB"
    )

    // [ID 448] Infermon (spidery spider cocoon demon)
    val INFERMON_ROM = arrayOf(
        "B..............B",
        "B#B..........B#B",
        ".B#B........B#B.",
        "..B#BB####BB#B..",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        "...B########B...",
        "....B######B....",
        "...B##SSSS##B...",
        "..B#SSSSSSSS#B..",
        "..B#SSSSSSSS#B..",
        ".B#B#B####B#B#B.",
        "B#B..B####B..B#B",
        "B#B..........B#B",
        ".B............B."
    )

    // [ID 449] Chirinmon (sacred golden horned qilin)
    val CHIRINMON_ROM = arrayOf(
        ".......TT.......",
        "......BTTB......",
        ".....BTTTTB.....",
        "....B######B....",
        "...B##WW####B...",
        "...B##EE#TT##B..",
        "..B######TT###B.",
        ".B############B.",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "B####B..B####B..",
        "BBBBBB..BBBBBB.."
    )

    // [ID 450] Hisyaryumon (armored samurai serpent dragon)
    val HISYARYUMON_ROM = arrayOf(
        "TT............TT",
        ".BTTB......BTTB.",
        "..B##TT##TT##B..",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B###TT####TT###B",
        "B##SSSSSSSSSS##B",
        ".B#SSSSSSSSSS#B.",
        "..B#SSSSSSSS#B..",
        "...B########B...",
        "..B#B######B#B..",
        ".B##B......B##B.",
        ".B#B........B#B.",
        ".BBB........BBB."
    )

    // [ID 451] Wingdramon (lance-winged cobalt dragon)
    val WINGDRAMON_ROM = arrayOf(
        "TT............TT",
        "BTTB........BTTB",
        ".B##B......B##B.",
        "..B##########B..",
        "...B#WW##WW#B...",
        "...B#EE##EE#B...",
        "..B##########B..",
        ".B############B.",
        "B##############B",
        "B##SSSSSSSSSS##B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "B####B..B####B..",
        "BBBBBB..BBBBBB.."
    )

    // [ID 452] Yatagaramon (three-legged gold ringed raven)
    val YATAGARAMON_ROM = arrayOf(
        ".......TT.......",
        "......BTTB......",
        ".....B####B.....",
        "....B######B....",
        "...B##WW####B...",
        "...B##EE#TT##B..",
        "..B######TT###B.",
        ".B############B.",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B##B##BB##B##B.",
        "B##B..B..B..B##B",
        "B##B..B..B..B##B",
        "BBBB..BBBB..BBBB"
    )

    // [ID 453] Lilamon (graceful floral fairy)
    val LILAMON_ROM = arrayOf(
        "TT............TT",
        ".BTTB..TT..BTTB.",
        "..B##BTTTTB##B..",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        ".B#TT######TT#B.",
        "B##TTSSSSSSSTT##",
        ".B###SSSSSS###B.",
        "..B##########B..",
        "...B########B...",
        "....B######B....",
        ".....B####B.....",
        "....B##BB##B....",
        "....BB....BB...."
    )

    // [ID 454] Flaremon (solar beast warrior)
    val FLAREMON_ROM = arrayOf(
        "..TT.TTTT.TT....",
        ".BTTBTTTTBTTB...",
        "..B##TTTT##B....",
        "...B######B.....",
        "..B##WW##WW#B...",
        "..B##EE##EE#B...",
        ".B############B.",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        ".B###SSSSSS###B.",
        "..B##########B..",
        ".BB##########BB.",
        "B##B#B####B#B##B",
        "B##B.B####B.B##B",
        "BBBB..BBBB..BBBB",
        "................"
    )

    // [ID 455] Crescemon (moonlight scythe warrior)
    val CRESCEMON_ROM = arrayOf(
        "TT............TT",
        ".BTTB......BTTB.",
        "..BTTB....BTTB..",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        ".B###SSSSSS###B.",
        "..B#SSSSSSSS#B..",
        "...B########B...",
        "..B#B######B#B..",
        ".B##B......B##B.",
        ".B#B........B#B.",
        ".BBB........BBB."
    )

    // [ID 456] SkullSatamon (skeletal fallen demon)
    val SKULLSATAMON_ROM = arrayOf(
        "....TT....TT....",
        "...BTTB..BTTB...",
        "..B##########B..",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        "B##RR######RR##B",
        "B##RR#SSSS#RR##B",
        "B##SSSSSSSSSS##B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...B########B...",
        "..B#B######B#B..",
        ".B##B......B##B.",
        ".B#B........B#B.",
        ".BBB........BBB."
    )

    // [ID 457] Mummymon (bandaged gunslinger)
    val MUMMYMON_ROM = arrayOf(
        "....BBBBBBBB....",
        "...B########B...",
        "..B##########B..",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        "B##############B",
        "B##SSSSSSSSSS##B",
        "B##SSSSSSSSSS##B",
        "B##WW########TTB",
        ".B#WWSSSSSS#TTB.",
        "..B##########B..",
        ".B###BB##BB###B.",
        ".B##B..BB..B##B.",
        ".B##B......B##B.",
        ".BBBB......BBBB."
    )

    // [ID 458] Arukenimon (spider enchantress)
    val ARUKENIMON_ROM = arrayOf(
        "..TT.TTTT.TT....",
        "..BTTBTTTTBTTB..",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B##############B",
        "B##SSSSSSSSSS##B",
        ".B#TT######TT#B.",
        "B##TT######TT##B",
        "B##B##B##B##B##B",
        "B##B..B..B..B##B",
        ".BB....BB....BB.",
        "B##B........B##B",
        "B###B......B###B",
        "BBBBB......BBBBB"
    )

    // [ID 459] Grademon (golden dual-blade knight)
    val GRADEMON_ROM = arrayOf(
        "......TTTT......",
        "....BB####BB....",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B#WW########WW#B",
        "B#TT#SSSSSS#TT#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "B####B..B####B..",
        "BBBBBB..BBBBBB.."
    )

    // [ID 460] Astamon (demonic mobster prince)
    val ASTAMON_ROM = arrayOf(
        "....TTTTTTTT....",
        "...B########B...",
        "..B##########B..",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        "B##############B",
        "B#TT########WW#B",
        "B#TT#SSSSSS#WWB.",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...B########B...",
        "..B#B######B#B..",
        ".B##B......B##B.",
        ".BBB........BBB."
    )

    // [ID 461] Gargoylemon (white stone sacred gargoyle)
    val GARGOYLEMON_ROM = arrayOf(
        "..TTTT....TTTT..",
        ".BTTTTB..BTTTTB.",
        "..B##########B..",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        "B#TT########TT#B",
        "B#TTSSSSSSSSTT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...B########B...",
        "..B#B######B#B..",
        ".B##B......B##B.",
        ".B#B........B#B.",
        ".BBB........BBB."
    )

    // [ID 462] Mammothmon (primeval ancient beast with ivory tusks)
    val MAMMOTHMON_ROM = arrayOf(
        "....BBBBBBBB....",
        "...B########B...",
        "..B##########B..",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        "B##############B",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..BB#SSSSSS#BB..",
        ".B##B######B##B.",
        "B###B.BBBB.B###B",
        "B###B.B##B.B###B",
        "BBBBB.BBBB.BBBBB"
    )

    // [ID 463] MetalGreymon (Virus) (cybernetic blue dragon with chrome trident claw)
    val METALGREYMON_VIRUS_ROM = arrayOf(
        "..TT........TT..",
        "..BTTB....BTTB..",
        "...B########B...",
        "..B##########B..",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        "B#WW########TT#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "B####B..B####B..",
        "BBBBBB..BBBBBB.."
    )

    // [ID 464] Cyberdramon (Savers) (streamlined sleek dragoon hunter)
    val CYBERDRAMON_SAVERS_ROM = arrayOf(
        "TT............TT",
        "BTTB........BTTB",
        ".BTTB..TT..BTTB.",
        "..B##BTTTTB##B..",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B#WW########WW#B",
        "B#TT#SSSSSS#TT#B",
        ".B###SSSSSS###B.",
        "..B#SSSSSSSS#B..",
        "...B########B...",
        "..B###B##B###B..",
        ".B####B..B####B.",
        ".BBBBB....BBBBB."
    )

    // =========================================================================
    // Stage 6: Ultimate / Mega (558 - 582)
    // =========================================================================

    // [ID 558] RustTyrannomon (rust-armored dreadnought with electromagnetic cannon)
    val RUSTTYRANNOMON_ROM = arrayOf(
        "TTTT............",
        "BTTTTB..........",
        ".BTTTTBBBB......",
        "..B##TTTT#B.....",
        "...B#######B....",
        "...B#WW#####B...",
        "...B#EE#RR###B..",
        "..B##########B..",
        ".B#TT######TT#B.",
        "B##TT#SSSS#TT##B",
        "B##SSSSSSSSSS##B",
        ".B#SSSSSSSSSS#B.",
        "..BB########BB..",
        ".B##B#BB##B#B##B",
        "B###B......B###B",
        "BBBBB......BBBBB"
    )

    // [ID 559] BlitzGreymon (plasma cannon artillery dragon)
    val BLITZGREYMON_ROM = arrayOf(
        "...TT......TT...",
        "..BTTB....BTTB..",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B#WW########WW#B",
        "B#TT#SSSSSS#TT#B",
        "B#TT#SSSSSS#TT#B",
        "B#WW########WW#B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B###BB##BB###B.",
        "B###B..BB..B###B",
        "B###B......B###B",
        "BBBBB......BBBBB"
    )

    // [ID 560] CresGarurumon (golden blade knight wolf)
    val CRESGARURUMON_ROM = arrayOf(
        "TT..............",
        "BTTB............",
        ".BTTB...BBBB....",
        "..BTTB.B####B...",
        "...B##B######B..",
        "...B#WW##WW##B..",
        "...B#EE##EE##B..",
        "..B############B",
        ".B#TT########TTB",
        "B##TT#SSSSSS#TTB",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "BBBBBB..BBBBBB.."
    )

    // [ID 561] Ouryumon (golden armored blade dragon emperor)
    val OURYUMON_ROM = arrayOf(
        "TT............TT",
        "BTTB........BTTB",
        ".BTTB..TT..BTTB.",
        "..B##BTTTTB##B..",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B###TT####TT###B",
        "B##SSSSSSSSSS##B",
        ".B#SSSSSSSSSS#B.",
        "..B#SSSSSSSS#B..",
        "...B########B...",
        "..B#B######B#B..",
        ".B##B......B##B.",
        ".BBB........BBB."
    )

    // [ID 562] Slayerdramon (fragarach sovereign dragon knight)
    val SLAYERDRAMON_ROM = arrayOf(
        ".......TT.......",
        "......BTTB......",
        ".....B####B.....",
        "....B######B....",
        "...B#WW##WW#B...",
        "...B#EE##EE#B...",
        "..B##########B..",
        ".B#TT######TT#B.",
        "B##TT#WWWW#TT##B",
        "B#####WWWW#####B",
        ".B####W##W####B.",
        "..B#SSSSSSSS#B..",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "BBBBBB..BBBBBB.."
    )

    // [ID 563] Apollomon (sun deity of the Olympos XII)
    val APOLLOMON_ROM = arrayOf(
        "..TT...TT...TT..",
        "...BTT.BTT.TTB..",
        "....B######B....",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".BB##########BB.",
        "B##B#B####B#B##B",
        "B##B.B####B.B##B",
        "BBBB..BBBB..BBBB"
    )

    // [ID 564] Dianamon (moon deity of the Olympos XII)
    val DIANAMON_ROM = arrayOf(
        "..TT........TT..",
        ".BTTB......BTTB.",
        ".B#TTB....BTT#B.",
        ".B##TB....BT##B.",
        "..B##########B..",
        ".B##WW####WW##B.",
        ".B##EE####EE##B.",
        ".B############B.",
        "B#CC########CC#B",
        "B#CC#SSSSSS#CC#B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...B########B...",
        "..B#B######B#B..",
        ".B##B......B##B.",
        ".BBB........BBB."
    )

    // [ID 565] GraceNovamon (galaxy super ultimate sovereign)
    val GRACENOVAMON_ROM = arrayOf(
        "TT..TT....TT..TT",
        "BTTBTTB..BTTBTTB",
        ".B############B.",
        "B#TT########TT#B",
        "B##WW######WW##B",
        "B##EE######EE##B",
        "B##############B",
        "B#CC#SSSSSS#CC#B",
        "B#CC#SSSSSS#CC#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "B####B..B####B..",
        "BBBBBB..BBBBBB.."
    )

    // [ID 566] Lucemon Satan Mode (apocalyptic dragon carrying the Gehenna orb)
    val LUCEMON_SATAN_MODE_ROM = arrayOf(
        "TT............TT",
        "BTTB..TTTT..BTTB",
        ".B###BTTTTB###B.",
        "B##############B",
        "B##WW######WW##B",
        "B##EE######EE##B",
        "B##############B",
        "B##RRCCCCCCRR##B",
        "B##RRCCCCCCRR##B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "..BB########BB..",
        ".B##B#BB##B#B##B",
        "B###B......B###B",
        "BBBBB......BBBBB"
    )

    // [ID 567] Chaosmon (unstable fusion with duel avatar arms)
    val CHAOSMON_ROM = arrayOf(
        "TTTT........TTTT",
        "BTTB........BTTB",
        ".B############B.",
        "..B#WW####WW#B..",
        "..B#EE####EE#B..",
        ".B############B.",
        "B#TT########WW#B",
        "B#TTSSSSSSSSWWB.",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B############B.",
        "B###BB####BB###B",
        "B##B..B..B..B##B",
        "B###B......B###B",
        "BBBBB......BBBBB"
    )

    // [ID 568] BanchoLeomon (Gaku-Ran jacket king of beasts)
    val BANCHOLEOMON_ROM = arrayOf(
        "...TTTT..TTTT...",
        "..BTTTTB.BTTB...",
        "..B##########B..",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        "B##############B",
        "B#SS########SS#B",
        "B#SS#TT##TT#SS#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".BB##########BB.",
        "B##B#B####B#B##B",
        "B##B.B####B.B##B",
        "BBBB..BBBB..BBBB"
    )

    // [ID 569] Darkdramon (D-Brigade cybernetic assault dragon)
    val DARKDRAMON_ROM = arrayOf(
        "..TT.TTTT.TT....",
        "..BTTBTTTTBTTB..",
        "..B##########B..",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        "B#WW########TT#B",
        "B#WW#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "B####B..B####B..",
        "BBBBBB..BBBBBB..",
        "................"
    )

    // [ID 570] MarineAngemon (holy ocean fairy with golden heart halo)
    val MARINEANGEMON_ROM = arrayOf(
        "......TTTT......",
        ".....BTTTTB.....",
        "....B#TTTT#B....",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B#WW########WW#B",
        "B#WW#SSSSSS#WW#B",
        ".B###SSSSSS###B.",
        "..B#SSSSSSSS#B..",
        "...B########B...",
        "....B######B....",
        ".....B####B.....",
        "......B##B......",
        ".......BB......."
    )

    // [ID 571] TigerVespamon (royal hornet cyborg assassin)
    val TIGERVESPAMON_ROM = arrayOf(
        "TT..TT....TT..TT",
        "BTTBTTB..BTTBTTB",
        ".B############B.",
        "..B#WW####WW#B..",
        "..B#EE####EE#B..",
        ".B############B.",
        "B#WW########WW#B",
        "B#TT#SSSSSS#TT#B",
        "B###SSSSSSSS###B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...B########B...",
        "..B#B######B#B..",
        ".B##B......B##B.",
        ".B#B........B#B.",
        ".BBB........BBB."
    )

    // [ID 572] Kuzuhamon (dark sacred miko variant)
    val KUZUHAMON_ROM = arrayOf(
        "......TTTT......",
        "....BTTTTTTB....",
        "...B########B...",
        "..B##WW##WW##B..",
        "..B##EE##EE##B..",
        ".B############B.",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        "B###SSSSSSSS###B",
        ".B##SSSSSSSS##B.",
        "..B##########B..",
        ".B############B.",
        "B##B########B##B",
        "B###B######B###B",
        ".B##B.B##B.B##B.",
        "..BB...BB...BB.."
    )

    // [ID 573] BlackSeraphimon (fallen winged seraph in obsidian armor)
    val BLACKSERAPHIMON_ROM = arrayOf(
        "TTTT..TTTT..TTTT",
        "BTTB..B##B..BTTB",
        ".B############B.",
        "B##############B",
        "B##WW######WW##B",
        "B##EE######EE##B",
        "B#TT########TT#B",
        "B#TT#SSSSSS#TT#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B####BB####B#B.",
        "B####B..B####BB.",
        "B####B..B####B..",
        "BBBBBB..BBBBBB.."
    )

    // [ID 574] Ophanimon Falldown Mode (corrupted holy flame archangel)
    val OPHANIMON_FALLDOWN_MODE_ROM = arrayOf(
        "TT..TTTTTTTT..TT",
        "BTTB.B####B.BTTB",
        ".B############B.",
        "B##############B",
        "B##WW######WW##B",
        "B##EE######EE##B",
        "B##############B",
        "B###SSSSSSSS###B",
        "B#RRSSSSSSSSRR#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...B########B...",
        "..B##########B..",
        ".B###B....B###B.",
        ".BBBB......BBBB."
    )

    // [ID 575] Ravemon (shadow blade winged raven ninja)
    val RAVEMON_ROM = arrayOf(
        "TT............WW",
        "BTTB...TT...BWW#",
        ".B###BTTTTB###W.",
        "B##############B",
        "B##WW######WW##B",
        "B##EE######EE##B",
        "B##############B",
        "B#TT#SSSSSS#WW#B",
        "B###SSSSSSSS###B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "...B#BB##BB#B...",
        "..B##B....B##B..",
        ".B###B....B###B.",
        ".BBBB......BBBB."
    )

    // [ID 576] ShineGreymon (solar blazing knight dragon)
    val SHINEGREYMON_ROM = arrayOf(
        "TTTT..TTTT..TTTT",
        "BTTB.B####B.BTTB",
        ".B############B.",
        "B##############B",
        "B##WW######WW##B",
        "B##EE######EE##B",
        "B#TT########WW#B",
        "B#TT#SSSSSS#WWB.",
        "B###SSSSSSSS###B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B###B####B###B.",
        "B#####B..B#####B",
        "B#BB##B..B##BB#B",
        "BBB.BBB..BBB.BBB"
    )

    // [ID 577] MirageGaogamon (howling moonlight beast knight)
    val MIRAGEGAOGAMON_ROM = arrayOf(
        "..TT.TTTT.TT....",
        ".BTTBTTTTBTTB...",
        "...B########B...",
        "..B##########B..",
        ".B#WW######WW#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        "B#WW########WW#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".BB##########BB.",
        "B##B#B####B#B##B",
        "B##B.B####B.B##B",
        "BBBB..BBBB..BBBB"
    )

    // [ID 578] Jesmon GX (transcendental apex Royal Knight)
    val JESMON_GX_ROM = arrayOf(
        "TT....TTTT....TT",
        "BTTB.BTTTTB.BTTB",
        ".B############B.",
        "B##############B",
        "B##WW######WW##B",
        "B##EE######EE##B",
        "B#WW########WW#B",
        "B#TT#SSSSSS#TT#B",
        "B#TT#SSSSSS#TT#B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B############B.",
        "B###B#B##B#B###B",
        "B###B......B###B",
        ".BBB........BBB."
    )

    // [ID 579] Gallantmon X (awakened X-Antibody holy knight)
    val GALLANTMON_X_ROM = arrayOf(
        "......TTTT......",
        ".....BTTTTB.....",
        "....B######B....",
        "...B########B...",
        "...B#WW##WW#B...",
        "...B#EE##EE#B...",
        "..B##########B..",
        ".B#TT######CC#B.",
        "B##TTSSSSSSCC##B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B###B####B###B.",
        "B####B....B####B",
        "B####B....B####B",
        "BBBBBB....BBBBBB"
    )

    // [ID 580] WarGreymon X (sharpened X-Antibody dragon warrior)
    val WARGREYMON_X_ROM = arrayOf(
        "TT..TTTTTTTT..TT",
        "BTTB.BTTTTB.BTTB",
        ".B############B.",
        "B##############B",
        "B##WW######WW##B",
        "B##EE######EE##B",
        "B#WW########WW#B",
        "B#TTSSSSSSSSTT#B",
        "B###SSSSSSSS###B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".B############B.",
        "B##BB#BBBB#BB##B",
        "B##B..B##B..B##B",
        "BBBB..BBBB..BBBB"
    )

    // [ID 581] MetalGarurumon X (quadrupedal heavy gatling beast)
    val METALGARURUMON_X_ROM = arrayOf(
        "TTTT..TTTT..TTTT",
        "BTTB..B##B..BTTB",
        ".B############B.",
        "..B#WW####WW#B..",
        "..B#EE####EE#B..",
        ".B############B.",
        "B#WW########WW#B",
        "B#TT#SSSSSS#TT#B",
        "B###SSSSSSSS###B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        ".BB##########BB.",
        "B###B.BBBB.B###B",
        "B###B.B##B.B###B",
        "BBBBB.BBBB.BBBBB"
    )

    // [ID 582] Beelzemon X (awakened X-Antibody demon lord of gluttony)
    val BEELZEMON_X_ROM = arrayOf(
        "RR............RR",
        "BRRB..TTTT..BRRB",
        ".B###BTTTTB###B.",
        "B##############B",
        "B##WW######WW##B",
        "B##EE######EE##B",
        "B#WW########WW#B",
        "B#TT#SSSSSS#TT#B",
        "B###SSSSSSSS###B",
        "B##############B",
        ".B#SSSSSSSSSS#B.",
        "..B##########B..",
        "..B##########B..",
        ".B##B######B##B.",
        ".B##B......B##B.",
        ".BBBB......BBBB."
    )

    /**
     * Looks up the dot-matrix ROM array for species in the master roster.
     */
    fun getMatrix(sid: Int): Array<String>? {
        return when (sid) {
            123 -> TSUMEMON_ROM
            124 -> HOPMON_ROM
            125 -> KAPURIMON_ROM
            126 -> MINOMON_ROM
            127 -> SUNMON_ROM
            128 -> MOONMON_ROM
            129 -> KETOMON_ROM

            232 -> GOMAMON_ROM
            233 -> GOTSUMON_ROM
            234 -> OTAMAMON_ROM
            235 -> CANDLEMON_ROM
            236 -> BAKUMON_ROM
            237 -> HAGURUMON_ROM
            238 -> MUSHROOMON_ROM
            239 -> CRABMON_ROM
            240 -> SYAKOMON_ROM
            241 -> LALAMON_ROM
            242 -> FALCOMON_ROM
            243 -> KUDAMON_ROM
            244 -> RYUDAMON_ROM
            245 -> DRACOMON_ROM
            246 -> KAMEMON_ROM
            247 -> CORONAMON_ROM
            248 -> LUNAMON_ROM
            249 -> TOYAGUMON_ROM
            250 -> KERAMON_ROM

            350 -> IKKAKUMON_ROM
            351 -> GOLEMON_ROM
            352 -> GEKOMON_ROM
            353 -> CLOCKMON_ROM
            354 -> GUARDROMON_ROM
            355 -> WOODMON_ROM
            356 -> OCTOMON_ROM
            357 -> GESOMON_ROM
            358 -> EBIDRAMON_ROM
            359 -> RAIDRAMON_ROM
            360 -> DIGMON_ROM
            361 -> SHURIMON_ROM
            362 -> SUBMARIMON_ROM
            363 -> PECKMON_ROM
            364 -> SUNFLOWMON_ROM
            365 -> REPPAMON_ROM
            366 -> GINRYUMON_ROM
            367 -> COREDRAMON_ROM
            368 -> CHRYSALIMON_ROM
            369 -> FIRAMON_ROM
            370 -> LEKISMON_ROM
            371 -> EXVEEMON_ROM
            372 -> LYNXMON_ROM

            444 -> SHOGUNGEKOMON_ROM
            445 -> VOLCAMON_ROM
            446 -> MARINEDEVIMON_ROM
            447 -> WARUSEADRAMON_ROM
            448 -> INFERMON_ROM
            449 -> CHIRINMON_ROM
            450 -> HISYARYUMON_ROM
            451 -> WINGDRAMON_ROM
            452 -> YATAGARAMON_ROM
            453 -> LILAMON_ROM
            454 -> FLAREMON_ROM
            455 -> CRESCEMON_ROM
            456 -> SKULLSATAMON_ROM
            457 -> MUMMYMON_ROM
            458 -> ARUKENIMON_ROM
            459 -> GRADEMON_ROM
            460 -> ASTAMON_ROM
            461 -> GARGOYLEMON_ROM
            462 -> MAMMOTHMON_ROM
            463 -> METALGREYMON_VIRUS_ROM
            464 -> CYBERDRAMON_SAVERS_ROM

            558 -> RUSTTYRANNOMON_ROM
            559 -> BLITZGREYMON_ROM
            560 -> CRESGARURUMON_ROM
            561 -> OURYUMON_ROM
            562 -> SLAYERDRAMON_ROM
            563 -> APOLLOMON_ROM
            564 -> DIANAMON_ROM
            565 -> GRACENOVAMON_ROM
            566 -> LUCEMON_SATAN_MODE_ROM
            567 -> CHAOSMON_ROM
            568 -> BANCHOLEOMON_ROM
            569 -> DARKDRAMON_ROM
            570 -> MARINEANGEMON_ROM
            571 -> TIGERVESPAMON_ROM
            572 -> KUZUHAMON_ROM
            573 -> BLACKSERAPHIMON_ROM
            574 -> OPHANIMON_FALLDOWN_MODE_ROM
            575 -> RAVEMON_ROM
            576 -> SHINEGREYMON_ROM
            577 -> MIRAGEGAOGAMON_ROM
            578 -> JESMON_GX_ROM
            579 -> GALLANTMON_X_ROM
            580 -> WARGREYMON_X_ROM
            581 -> METALGARURUMON_X_ROM
            582 -> BEELZEMON_X_ROM
            else -> null
        }
    }

    /**
     * Looks up the canonical 32-bit ARGB palette for species in the master roster.
     */
    fun getPalette(sid: Int): SpritePalette? {
        return when (sid) {
            123 -> SpritePalette(SpritePalette.rgb(70, 75, 90), SpritePalette.rgb(235, 60, 60), SpritePalette.rgb(245, 215, 65), SpritePalette.rgb(255, 255, 255)) // Tsumemon
            124 -> SpritePalette(SpritePalette.rgb(80, 150, 230), SpritePalette.rgb(240, 240, 248), SpritePalette.rgb(245, 180, 40), SpritePalette.rgb(220, 40, 40)) // Hopmon
            125 -> SpritePalette(SpritePalette.rgb(160, 170, 185), SpritePalette.rgb(215, 50, 60), SpritePalette.rgb(110, 120, 135), SpritePalette.rgb(0, 200, 255)) // Kapurimon
            126 -> SpritePalette(SpritePalette.rgb(130, 200, 60), SpritePalette.rgb(140, 95, 60), SpritePalette.rgb(240, 240, 180), SpritePalette.rgb(20, 20, 25)) // Minomon
            127 -> SpritePalette(SpritePalette.rgb(255, 140, 30), SpritePalette.rgb(255, 215, 60), SpritePalette.rgb(230, 50, 40), SpritePalette.rgb(255, 255, 255)) // Sunmon
            128 -> SpritePalette(SpritePalette.rgb(220, 215, 245), SpritePalette.rgb(135, 145, 220), SpritePalette.rgb(255, 240, 120), SpritePalette.rgb(20, 20, 25)) // Moonmon
            129 -> SpritePalette(SpritePalette.rgb(100, 110, 130), SpritePalette.rgb(245, 200, 50), SpritePalette.rgb(240, 242, 248), SpritePalette.rgb(220, 35, 35)) // Ketomon

            232 -> SpritePalette(SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(185, 30, 40), SpritePalette.rgb(155, 95, 175), SpritePalette.rgb(40, 150, 225)) // Gomamon
            233 -> SpritePalette(SpritePalette.rgb(150, 155, 165), SpritePalette.rgb(95, 100, 110), SpritePalette.rgb(245, 200, 45), SpritePalette.rgb(220, 40, 40)) // Gotsumon
            234 -> SpritePalette(SpritePalette.rgb(70, 130, 225), SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(225, 60, 60), SpritePalette.rgb(20, 20, 25)) // Otamamon
            235 -> SpritePalette(SpritePalette.rgb(245, 242, 230), SpritePalette.rgb(240, 90, 30), SpritePalette.rgb(255, 210, 50), SpritePalette.rgb(60, 160, 230)) // Candlemon
            236 -> SpritePalette(SpritePalette.rgb(125, 75, 160), SpritePalette.rgb(245, 242, 235), SpritePalette.rgb(245, 195, 55), SpritePalette.rgb(225, 45, 45)) // Bakumon
            237 -> SpritePalette(SpritePalette.rgb(165, 140, 120), SpritePalette.rgb(215, 180, 50), SpritePalette.rgb(90, 80, 75), SpritePalette.rgb(235, 40, 40)) // Hagurumon
            238 -> SpritePalette(SpritePalette.rgb(90, 185, 70), SpritePalette.rgb(235, 60, 50), SpritePalette.rgb(245, 240, 180), SpritePalette.rgb(250, 215, 60)) // Mushroomon
            239 -> SpritePalette(SpritePalette.rgb(235, 90, 40), SpritePalette.rgb(245, 220, 80), SpritePalette.rgb(175, 60, 30), SpritePalette.rgb(20, 20, 25)) // Crabmon
            240 -> SpritePalette(SpritePalette.rgb(245, 150, 175), SpritePalette.rgb(240, 235, 220), SpritePalette.rgb(110, 50, 120), SpritePalette.rgb(255, 215, 50)) // Syakomon
            241 -> SpritePalette(SpritePalette.rgb(235, 130, 185), SpritePalette.rgb(115, 195, 80), SpritePalette.rgb(250, 235, 90), SpritePalette.rgb(225, 40, 60)) // Lalamon
            242 -> SpritePalette(SpritePalette.rgb(45, 55, 80), SpritePalette.rgb(245, 240, 225), SpritePalette.rgb(245, 180, 40), SpritePalette.rgb(220, 35, 35)) // Falcomon
            243 -> SpritePalette(SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(245, 200, 50), SpritePalette.rgb(190, 40, 50), SpritePalette.rgb(40, 170, 235)) // Kudamon
            244 -> SpritePalette(SpritePalette.rgb(230, 235, 245), SpritePalette.rgb(215, 45, 45), SpritePalette.rgb(245, 195, 45), SpritePalette.rgb(40, 110, 220)) // Ryudamon
            245 -> SpritePalette(SpritePalette.rgb(75, 170, 215), SpritePalette.rgb(245, 240, 210), SpritePalette.rgb(225, 50, 50), SpritePalette.rgb(245, 200, 45)) // Dracomon
            246 -> SpritePalette(SpritePalette.rgb(80, 180, 120), SpritePalette.rgb(140, 95, 65), SpritePalette.rgb(220, 230, 240), SpritePalette.rgb(20, 20, 25)) // Kamemon
            247 -> SpritePalette(SpritePalette.rgb(245, 120, 25), SpritePalette.rgb(220, 35, 35), SpritePalette.rgb(255, 220, 65), SpritePalette.rgb(50, 180, 240)) // Coronamon
            248 -> SpritePalette(SpritePalette.rgb(195, 190, 240), SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(245, 215, 65), SpritePalette.rgb(220, 45, 75)) // Lunamon
            249 -> SpritePalette(SpritePalette.rgb(235, 50, 50), SpritePalette.rgb(245, 210, 40), SpritePalette.rgb(50, 115, 225), SpritePalette.rgb(255, 255, 255)) // ToyAgumon
            250 -> SpritePalette(SpritePalette.rgb(70, 150, 160), SpritePalette.rgb(215, 50, 50), SpritePalette.rgb(245, 220, 60), SpritePalette.rgb(255, 255, 255)) // Keramon

            350 -> SpritePalette(SpritePalette.rgb(240, 245, 250), SpritePalette.rgb(180, 35, 45), SpritePalette.rgb(240, 200, 55), SpritePalette.rgb(20, 20, 25)) // Ikkakumon
            351 -> SpritePalette(SpritePalette.rgb(130, 120, 110), SpritePalette.rgb(215, 80, 35), SpritePalette.rgb(75, 70, 65), SpritePalette.rgb(250, 205, 50)) // Golemon
            352 -> SpritePalette(SpritePalette.rgb(90, 180, 75), SpritePalette.rgb(245, 195, 45), SpritePalette.rgb(225, 50, 50), SpritePalette.rgb(240, 245, 250)) // Gekomon
            353 -> SpritePalette(SpritePalette.rgb(190, 150, 95), SpritePalette.rgb(245, 210, 55), SpritePalette.rgb(60, 65, 80), SpritePalette.rgb(225, 40, 40)) // Clockmon
            354 -> SpritePalette(SpritePalette.rgb(175, 130, 85), SpritePalette.rgb(80, 85, 95), SpritePalette.rgb(225, 45, 45), SpritePalette.rgb(245, 210, 55)) // Guardromon
            355 -> SpritePalette(SpritePalette.rgb(135, 90, 55), SpritePalette.rgb(75, 150, 60), SpritePalette.rgb(215, 160, 85), SpritePalette.rgb(220, 35, 35)) // Woodmon
            356 -> SpritePalette(SpritePalette.rgb(220, 50, 65), SpritePalette.rgb(245, 240, 220), SpritePalette.rgb(45, 45, 55), SpritePalette.rgb(245, 205, 50)) // Octomon
            357 -> SpritePalette(SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(140, 150, 175), SpritePalette.rgb(225, 45, 55), SpritePalette.rgb(40, 180, 230)) // Gesomon
            358 -> SpritePalette(SpritePalette.rgb(235, 75, 45), SpritePalette.rgb(245, 210, 75), SpritePalette.rgb(50, 140, 210), SpritePalette.rgb(20, 20, 25)) // Ebidramon
            359 -> SpritePalette(SpritePalette.rgb(45, 50, 70), SpritePalette.rgb(245, 215, 55), SpritePalette.rgb(55, 125, 225), SpritePalette.rgb(220, 35, 35)) // Raidramon
            360 -> SpritePalette(SpritePalette.rgb(240, 185, 45), SpritePalette.rgb(70, 75, 85), SpritePalette.rgb(180, 190, 205), SpritePalette.rgb(220, 35, 35)) // Digmon
            361 -> SpritePalette(SpritePalette.rgb(75, 165, 80), SpritePalette.rgb(215, 45, 45), SpritePalette.rgb(245, 210, 55), SpritePalette.rgb(245, 245, 250)) // Shurimon
            362 -> SpritePalette(SpritePalette.rgb(245, 205, 50), SpritePalette.rgb(70, 140, 215), SpritePalette.rgb(175, 185, 200), SpritePalette.rgb(220, 35, 35)) // Submarimon
            363 -> SpritePalette(SpritePalette.rgb(55, 65, 95), SpritePalette.rgb(245, 200, 50), SpritePalette.rgb(215, 45, 45), SpritePalette.rgb(245, 242, 235)) // Peckmon
            364 -> SpritePalette(SpritePalette.rgb(245, 205, 45), SpritePalette.rgb(95, 180, 70), SpritePalette.rgb(140, 85, 50), SpritePalette.rgb(20, 20, 25)) // Sunflowmon
            365 -> SpritePalette(SpritePalette.rgb(245, 242, 235), SpritePalette.rgb(215, 40, 45), SpritePalette.rgb(175, 185, 200), SpritePalette.rgb(40, 160, 230)) // Reppamon
            366 -> SpritePalette(SpritePalette.rgb(195, 205, 220), SpritePalette.rgb(215, 40, 45), SpritePalette.rgb(245, 195, 45), SpritePalette.rgb(40, 120, 220)) // Ginryumon
            367 -> SpritePalette(SpritePalette.rgb(55, 120, 215), SpritePalette.rgb(240, 240, 248), SpritePalette.rgb(215, 45, 45), SpritePalette.rgb(245, 200, 45)) // Coredramon
            368 -> SpritePalette(SpritePalette.rgb(125, 75, 155), SpritePalette.rgb(215, 50, 50), SpritePalette.rgb(60, 160, 170), SpritePalette.rgb(250, 215, 60)) // Chrysalimon
            369 -> SpritePalette(SpritePalette.rgb(235, 105, 25), SpritePalette.rgb(245, 210, 55), SpritePalette.rgb(215, 40, 40), SpritePalette.rgb(45, 175, 235)) // Firamon
            370 -> SpritePalette(SpritePalette.rgb(175, 170, 225), SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(245, 200, 50), SpritePalette.rgb(215, 45, 75)) // Lekismon
            371 -> SpritePalette(SpritePalette.rgb(50, 120, 220), SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(245, 195, 45), SpritePalette.rgb(220, 35, 40)) // ExVeemon
            372 -> SpritePalette(SpritePalette.rgb(240, 115, 30), SpritePalette.rgb(245, 240, 220), SpritePalette.rgb(215, 40, 40), SpritePalette.rgb(40, 160, 230)) // Lynxmon

            444 -> SpritePalette(SpritePalette.rgb(180, 45, 55), SpritePalette.rgb(245, 200, 45), SpritePalette.rgb(85, 160, 75), SpritePalette.rgb(20, 20, 25)) // ShogunGekomon
            445 -> SpritePalette(SpritePalette.rgb(195, 60, 35), SpritePalette.rgb(75, 75, 85), SpritePalette.rgb(245, 205, 50), SpritePalette.rgb(255, 255, 255)) // Volcamon
            446 -> SpritePalette(SpritePalette.rgb(55, 60, 85), SpritePalette.rgb(215, 40, 50), SpritePalette.rgb(140, 150, 170), SpritePalette.rgb(245, 200, 50)) // MarineDevimon
            447 -> SpritePalette(SpritePalette.rgb(45, 50, 65), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(245, 190, 45), SpritePalette.rgb(0, 215, 255)) // WaruSeadramon
            448 -> SpritePalette(SpritePalette.rgb(240, 240, 245), SpritePalette.rgb(215, 45, 55), SpritePalette.rgb(45, 50, 65), SpritePalette.rgb(250, 210, 55)) // Infermon
            449 -> SpritePalette(SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(245, 205, 55), SpritePalette.rgb(55, 150, 230), SpritePalette.rgb(215, 40, 50)) // Chirinmon
            450 -> SpritePalette(SpritePalette.rgb(215, 225, 240), SpritePalette.rgb(215, 40, 45), SpritePalette.rgb(245, 195, 50), SpritePalette.rgb(40, 110, 220)) // Hisyaryumon
            451 -> SpritePalette(SpritePalette.rgb(65, 140, 230), SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(215, 40, 45), SpritePalette.rgb(245, 200, 45)) // Wingdramon
            452 -> SpritePalette(SpritePalette.rgb(40, 45, 60), SpritePalette.rgb(215, 175, 45), SpritePalette.rgb(215, 40, 45), SpritePalette.rgb(0, 200, 255)) // Yatagaramon
            453 -> SpritePalette(SpritePalette.rgb(215, 120, 185), SpritePalette.rgb(90, 180, 80), SpritePalette.rgb(245, 230, 80), SpritePalette.rgb(255, 255, 255)) // Lilamon
            454 -> SpritePalette(SpritePalette.rgb(240, 95, 25), SpritePalette.rgb(215, 35, 35), SpritePalette.rgb(250, 210, 55), SpritePalette.rgb(40, 160, 235)) // Flaremon
            455 -> SpritePalette(SpritePalette.rgb(165, 155, 225), SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(245, 205, 55), SpritePalette.rgb(215, 40, 75)) // Crescemon
            456 -> SpritePalette(SpritePalette.rgb(235, 230, 215), SpritePalette.rgb(45, 40, 55), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(245, 200, 50)) // SkullSatamon
            457 -> SpritePalette(SpritePalette.rgb(215, 205, 185), SpritePalette.rgb(55, 60, 80), SpritePalette.rgb(225, 45, 45), SpritePalette.rgb(245, 205, 50)) // Mummymon
            458 -> SpritePalette(SpritePalette.rgb(115, 45, 140), SpritePalette.rgb(215, 40, 50), SpritePalette.rgb(235, 235, 245), SpritePalette.rgb(245, 205, 50)) // Arukenimon
            459 -> SpritePalette(SpritePalette.rgb(245, 200, 50), SpritePalette.rgb(215, 40, 45), SpritePalette.rgb(185, 195, 210), SpritePalette.rgb(0, 215, 255)) // Grademon
            460 -> SpritePalette(SpritePalette.rgb(50, 45, 60), SpritePalette.rgb(215, 40, 45), SpritePalette.rgb(245, 240, 220), SpritePalette.rgb(245, 200, 50)) // Astamon
            461 -> SpritePalette(SpritePalette.rgb(235, 235, 245), SpritePalette.rgb(65, 125, 215), SpritePalette.rgb(245, 200, 50), SpritePalette.rgb(215, 40, 45)) // Gargoylemon
            462 -> SpritePalette(SpritePalette.rgb(145, 105, 70), SpritePalette.rgb(240, 235, 215), SpritePalette.rgb(75, 70, 65), SpritePalette.rgb(220, 35, 35)) // Mammothmon
            463 -> SpritePalette(SpritePalette.rgb(70, 75, 95), SpritePalette.rgb(185, 195, 210), SpritePalette.rgb(140, 45, 160), SpritePalette.rgb(245, 195, 45)) // MetalGreymon (Virus)
            464 -> SpritePalette(SpritePalette.rgb(55, 60, 75), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(185, 195, 210), SpritePalette.rgb(0, 210, 255)) // Cyberdramon (Savers)

            558 -> SpritePalette(SpritePalette.rgb(175, 70, 40), SpritePalette.rgb(90, 85, 80), SpritePalette.rgb(215, 35, 35), SpritePalette.rgb(245, 195, 45)) // RustTyrannomon
            559 -> SpritePalette(SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(50, 55, 70), SpritePalette.rgb(245, 195, 50), SpritePalette.rgb(0, 225, 255)) // BlitzGreymon
            560 -> SpritePalette(SpritePalette.rgb(245, 200, 50), SpritePalette.rgb(60, 110, 210), SpritePalette.rgb(240, 245, 255), SpritePalette.rgb(215, 35, 45)) // CresGarurumon
            561 -> SpritePalette(SpritePalette.rgb(245, 205, 50), SpritePalette.rgb(215, 40, 45), SpritePalette.rgb(235, 240, 250), SpritePalette.rgb(50, 180, 240)) // Ouryumon
            562 -> SpritePalette(SpritePalette.rgb(60, 125, 220), SpritePalette.rgb(245, 205, 50), SpritePalette.rgb(240, 242, 248), SpritePalette.rgb(215, 40, 45)) // Slayerdramon
            563 -> SpritePalette(SpritePalette.rgb(240, 110, 30), SpritePalette.rgb(245, 210, 55), SpritePalette.rgb(215, 35, 35), SpritePalette.rgb(0, 215, 255)) // Apollomon
            564 -> SpritePalette(SpritePalette.rgb(145, 140, 215), SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(245, 205, 55), SpritePalette.rgb(215, 45, 85)) // Dianamon
            565 -> SpritePalette(SpritePalette.rgb(165, 85, 215), SpritePalette.rgb(245, 210, 55), SpritePalette.rgb(70, 160, 245), SpritePalette.rgb(255, 255, 255)) // GraceNovamon
            566 -> SpritePalette(SpritePalette.rgb(40, 35, 50), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(50, 180, 100), SpritePalette.rgb(255, 215, 50)) // Lucemon Satan Mode
            567 -> SpritePalette(SpritePalette.rgb(240, 242, 248), SpritePalette.rgb(45, 50, 65), SpritePalette.rgb(215, 40, 45), SpritePalette.rgb(0, 220, 255)) // Chaosmon
            568 -> SpritePalette(SpritePalette.rgb(235, 165, 50), SpritePalette.rgb(40, 45, 55), SpritePalette.rgb(215, 35, 40), SpritePalette.rgb(245, 245, 252)) // BanchoLeomon
            569 -> SpritePalette(SpritePalette.rgb(45, 50, 65), SpritePalette.rgb(185, 195, 210), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(245, 200, 50)) // Darkdramon
            570 -> SpritePalette(SpritePalette.rgb(245, 150, 195), SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(245, 210, 55), SpritePalette.rgb(40, 160, 235)) // MarineAngemon
            571 -> SpritePalette(SpritePalette.rgb(245, 195, 45), SpritePalette.rgb(45, 45, 55), SpritePalette.rgb(185, 195, 210), SpritePalette.rgb(215, 40, 45)) // TigerVespamon
            572 -> SpritePalette(SpritePalette.rgb(115, 65, 155), SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(245, 195, 50), SpritePalette.rgb(215, 40, 50)) // Kuzuhamon
            573 -> SpritePalette(SpritePalette.rgb(50, 55, 75), SpritePalette.rgb(185, 35, 45), SpritePalette.rgb(185, 195, 210), SpritePalette.rgb(245, 195, 50)) // BlackSeraphimon
            574 -> SpritePalette(SpritePalette.rgb(55, 40, 60), SpritePalette.rgb(215, 35, 55), SpritePalette.rgb(245, 195, 50), SpritePalette.rgb(235, 235, 245)) // Ophanimon Falldown Mode
            575 -> SpritePalette(SpritePalette.rgb(45, 48, 60), SpritePalette.rgb(185, 195, 210), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(245, 205, 50)) // Ravemon
            576 -> SpritePalette(SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(235, 115, 30), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(0, 220, 255)) // ShineGreymon
            577 -> SpritePalette(SpritePalette.rgb(55, 115, 220), SpritePalette.rgb(240, 245, 255), SpritePalette.rgb(245, 195, 50), SpritePalette.rgb(215, 35, 45)) // MirageGaogamon
            578 -> SpritePalette(SpritePalette.rgb(245, 248, 255), SpritePalette.rgb(245, 205, 50), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(0, 225, 255)) // Jesmon GX
            579 -> SpritePalette(SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(245, 200, 50), SpritePalette.rgb(50, 200, 240)) // Gallantmon X
            580 -> SpritePalette(SpritePalette.rgb(245, 190, 40), SpritePalette.rgb(235, 110, 30), SpritePalette.rgb(190, 200, 215), SpritePalette.rgb(215, 35, 40)) // WarGreymon X
            581 -> SpritePalette(SpritePalette.rgb(60, 120, 225), SpritePalette.rgb(245, 195, 45), SpritePalette.rgb(180, 190, 210), SpritePalette.rgb(215, 35, 45)) // MetalGarurumon X
            582 -> SpritePalette(SpritePalette.rgb(42, 42, 54), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(245, 200, 50), SpritePalette.rgb(215, 225, 240)) // Beelzemon X
            else -> null
        }
    }
}
