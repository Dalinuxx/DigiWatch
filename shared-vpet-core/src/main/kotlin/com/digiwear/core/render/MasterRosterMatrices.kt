package com.digiwear.core.render

/**
 * Dot-matrix pixel art matrices and palettes for all master expansion species.
 * Every sprite is a 16x16 authentic dot-matrix array with distinct silhouette, outline, and multi-tone shading.
 * Together with the base species, DigiWear features exactly 350 unique canonical Digimon.
 */
object MasterRosterMatrices {

    // =========================================================================
    // Stage 2: Baby II (123 - 129)
    // =========================================================================

    // [ID 123] Tsumemon (spiky droplet with red jagged hair)
    val TSUMEMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        ".......BTB.BT...",
        "......#...B..B..",
        "......#BB#.B#.B.",
        ".....B....B..BB.",
        "....B..B...B..B.",
        "....B.#.B..B....",
        "....B.#BB..B....",
        "....#.......B...",
        "...B..B...#.B...",
        "..B..B..B.#..#..",
        "..B.#B.#B.#B.#..",
        "..BB.BB..B#.B#..",
        "................"
    )

    // [ID 124] Hopmon (bouncy horned blob)
    val HOPMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "......TB........",
        "......#.B.......",
        ".....B..B.......",
        "....BB.B.#......",
        "....B.#...B.....",
        ".BB#.......B.BB.",
        ".B.#..#....B#.B.",
        "..B.#...B..B.#..",
        ".#B.#...B.....B.",
        ".#B.........B#..",
        "..B..BB#....B...",
        "...B.......B....",
        "....#BB#BB#....."
    )

    // [ID 125] Kapurimon (metallic horned helmet with dual horns)
    val KAPURIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "...........BBBB.",
        "..........BWW##B",
        ".........BWWWW#B",
        "BB.....BB##TWWTB",
        "B.B...B.B#T##TTB",
        "BT.BBB.TBTW###B.",
        ".BT##.TBBWWT##B.",
        "B.S..STTBTTTBB..",
        "B.STTSTT#BBB....",
        "BTTBTTT##B......",
        "BTBTBBTT#B......",
        ".BTWWTTTB.......",
        "..BBBBBB........"
    )

    // [ID 126] Minomon (hanging leaf pinecone)
    val MINOMON_ROM = arrayOf(
        ".......B........",
        "...BT......BT...",
        "...B..TBT...T...",
        "....TB..T.TB....",
        "...#............",
        "..B..B...#BB#...",
        "..B..B#.....#B..",
        ".B.#..#.B....B..",
        "...#BB#.B#.B..B.",
        ".BB.......B..B..",
        "...B..B.BB......",
        "..BB#BB.....B...",
        "....#B...B......",
        "....#.B..B#.....",
        "................",
        "................"
    )

    // [ID 127] Sunmon (shining sun with flame tips)
    val SUNMON_ROM = arrayOf(
        ".........#......",
        ".........##.....",
        ".......##T#.....",
        "......#TT##.....",
        ".......#T#......",
        "....BB..#..BB...",
        "....B#BBTBB#B...",
        ".....B#TT##B....",
        "...BB#T#####BB..",
        "..B#BB###B##B#B.",
        "...BB#BBB###BB..",
        ".....B#####B....",
        "....B#BB#BB#B...",
        "....BB..B..BB...",
        "................",
        "................"
    )

    // [ID 128] Moonmon (drooping crescent bunny ears)
    val MOONMON_ROM = arrayOf(
        ".......BB.......",
        "......BWTB......",
        "......BWTB......",
        "......BTTB......",
        "......BTTB......",
        "......BTB.......",
        "......BTB.......",
        "....BB#B#B.B....",
        ".....BBWBTB.....",
        ".....BTTT#B.....",
        "....BTWTTT#B....",
        "....BBTTBT#B....",
        "....BTBBTT#B....",
        ".....BTTT#B.....",
        "......BBBB......",
        "................"
    )

    // [ID 129] Ketomon (tiny dragon droplet with single crown horn)
    val KETOMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        ".......BB#......",
        "......#..#......",
        ".....B..B.......",
        ".....B..B#......",
        "....B.....B.....",
        "...B..B....B....",
        "..B.#...B..BB#B.",
        "..B.#...B..B..B.",
        "..B.........B#..",
        "...B......#BB...",
        "....#BB#BB......"
    )

    // =========================================================================
    // Stage 3: Child / Rookie (232 - 250)
    // =========================================================================

    // [ID 232] Gomamon (white seal with crimson mohawk)
    val GOMAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "........B#B.#BB.",
        ".......B...B...#",
        "...#BB#BB.....B.",
        "..B......#...B..",
        ".B........B..B..",
        ".B.#...B..B...B.",
        ".#.B...#..#....B",
        "B.........#....B",
        "B...#...B..BB..B",
        ".#BB.BB#.....#B.",
        "B.B......B.....B",
        "B#BB#BB#BB#BB#BB"
    )

    // [ID 233] Gotsumon (rock boulder creature with jagged head)
    val GOTSUMON_ROM = arrayOf(
        "................",
        "................",
        "......BBBB......",
        ".....B.TTBBBBB..",
        "..BBB.T##TB#TBB.",
        "..B.B.ST##TB#TB.",
        "..B..T####S###B.",
        "..B.T#B##B#S#BB.",
        "..BT#TB##B#TBB..",
        "..BT#T###TT#TB..",
        "..BBT##BB##TTB..",
        "...BSTB##B#SSB..",
        "...BBS#S#T##BB..",
        "..BTTB#SS#TBTTB.",
        ".BTS#SBBBBBST#TB",
        ".BBBBBBB..BBBBBB"
    )

    // [ID 234] Otamamon (blue tadpole with musical spiral belly)
    val OTAMAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "....BB#BB#B.....",
        "...##WWS#WWB....",
        "..BSBWWSBWWS#...",
        "..B#.B#B.##S#...",
        "..B#.B#B.##S#...",
        "..BS#BB#BS#SB...",
        "..BS#...BS#SB.B.",
        "..BS#.B.BS#S##B.",
        "...B#...BS#BB#..",
        "....#BB#BS#.....",
        "..BB#B...B#BB#.."
    )

    // [ID 235] Candlemon (wax candle with blazing flame)
    val CANDLEMON_ROM = arrayOf(
        "............T...",
        "......TBTBTBT...",
        ".....B......T...",
        "....T...TB.B....",
        "....B..BB...#...",
        "....B..BB..B....",
        ".....B....B.....",
        "....BB#BB#BB....",
        "...#........#...",
        "...#.B..B...#...",
        "...B.B..B.#.B...",
        "....#......B....",
        "....#.B#...B....",
        "...B#.....#BB...",
        "..B..B...B...#..",
        "..BB#BB#BB#BB#.."
    )

    // [ID 236] Bakumon (tapir dream-eater with sacred collar)
    val BAKUMON_ROM = arrayOf(
        "..TB....TBT.....",
        "..T.TBTBT.T.....",
        "...BTBTBTB......",
        "..TBTBTBTB......",
        ".B#S#S#.BSB.....",
        "#S#S#B..B#.B#...",
        "#S#S#W#B##.B.B..",
        "#S##BWWSB..B.BB.",
        "#SB..B#B..B..B.#",
        "#B.#BB#.B...#..#",
        "B#.B...#B..B..BB",
        "...B#.BSB.....BB",
        "...B#.BSB.#B.#B.",
        "...B#B##BB..B#BB",
        "..B..BB..B.B...B",
        "..BB#.B#B..BB#B."
    )

    // [ID 237] Hagurumon (spinning gear creature with dark iris)
    val HAGURUMON_ROM = arrayOf(
        ".......BT.......",
        ".......BT.......",
        "...BT.TBTB.BT...",
        "...B.B....T.T...",
        "....B.#BB#.B....",
        "...#.B.BB.B.#...",
        ".BB#.B.BB.B.#BB.",
        ".B##.B#BB#B.#SB.",
        ".BB#.B#..#B.#BB.",
        "...#B.#BB#.B#...",
        "..BB#B....#BB#..",
        ".#...BB#BB#...B.",
        "B#.B.B.#B.#.B.BB",
        ".#...B.#B.#...B.",
        "..BB#......BB#..",
        "...B........B..."
    )

    // [ID 238] Mushroomon (mushroom-capped imp)
    val MUSHROOMON_ROM = arrayOf(
        ".....BBBBBBB....",
        "....BTT##TT#B...",
        "...B#TT##TTSBB..",
        "...BT##TT##TTB..",
        "..BTT##TT#STTSB.",
        ".BB###SSSSSSSSBB",
        ".B#SBBBBBBBBBSSB",
        ".BSBSBTBTBTBSBSB",
        "..BSBTTTTTTTBSB.",
        "...BBT#TT#TTBB..",
        "..BSBTSTTSTTBSB.",
        "..BTBTTTTTTBTWB.",
        "...BBTTBBTTTBB..",
        "....BBTTTTBBB...",
        "...BTSBBBBS#TB..",
        "...BBBB..BBBBB.."
    )

    // [ID 239] Crabmon (armored crab with heavy pincers)
    val CRABMON_ROM = arrayOf(
        ".........BBBBB..",
        "........BTTT##B.",
        "........BBBBTTTB",
        ".BBB.....BWWBTTB",
        "B#TBB....BTTWBTB",
        "BTBWB.....BTTTTB",
        "BBWTB......BBBTB",
        "BWTB.BB..BB.BTTB",
        ".BTTB##BB##BTTB.",
        "..BBWTTTTTTTBB..",
        "..BTTBTTTTBTTB..",
        ".BBTTTSTTSTTTBB.",
        "B#B#TTTTTTTT#B#B",
        "BBTBTTBBBBTTBTBB",
        "BTTTBTTTTTTBTTTB",
        "BBBB.BBBBBB.BBBB"
    )

    // [ID 240] Syakomon (clam shell with blushing face)
    val SYAKOMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "....BBBBBBB.....",
        "..BB#S###S#B....",
        ".B#TBTBTBT##B...",
        ".BBBBBBBBBB##B..",
        ".....B###SSB#B..",
        "....BT####SSB#B.",
        "...BT#T##T#SB#BB",
        ".BBBBBBBBBBBBB#B",
        ".B#BTTBTTBTT###B",
        "..B#BT#BT#BT#BB.",
        "...BBBBBBBBBB..."
    )

    // [ID 241] Lalamon (floating pink bud with green sprout propeller)
    val LALAMON_ROM = arrayOf(
        ".....BT.TB......",
        ".......B........",
        ".....BTBTB......",
        "....T.T.T.T.....",
        "...#.......B....",
        "...#.B...#.B....",
        "...#.B...#.B....",
        "..B.........#...",
        "..B...#.....#...",
        ".B..B.....B..B..",
        ".#BB.B..BB.BB#..",
        "...B..B#....B...",
        "..B..........#..",
        "..BB#......BB#..",
        "....#.B#B..B....",
        ".....BB#.B#B...."
    )

    // [ID 242] Falcomon (ninja falcon with kunai crest)
    val FALCOMON_ROM = arrayOf(
        "..TBTB.....BTB..",
        "..T..BTBTBT..B..",
        "...BT.......T...",
        "...B.....BTB....",
        "...#B..BB#B.#...",
        "...#BB#.B.B.#...",
        "...#..#..#B.#...",
        "..B#.B#.....#BB.",
        "..B.BB.....B..B.",
        "...#.B#BB#BB.B..",
        "...B#.BSB.#.B...",
        "..B.#..#...B.#..",
        ".#..#B....#B..B.",
        ".#.B.BB#BB.BB..B",
        ".#B.#..#B.#..#BB",
        "..BB#BB#BB#BB#.."
    )

    // [ID 243] Kudamon (holy ferret wrapped in gold sacred cartridge)
    val KUDAMON_ROM = arrayOf(
        "................",
        "................",
        ".....BB.BBBBBBB.",
        "....B.SBSSBWWB..",
        "...B.WB.WBWWB...",
        "...BBB.TB.TB....",
        "..B...WBT.WB....",
        ".B..T.WB.T.#B...",
        "B..TTWWBBB.TWB..",
        "BSWWWWB#T.BT##B.",
        ".BBWWBS#BT.BWW#B",
        "...BBS##WBT#BB#B",
        "...B###WWB#TTTBB",
        "...BWB.BTWBT#TB.",
        "...B.B.B.TBTTTB.",
        "..BBBBB.BBBBBB.."
    )

    // [ID 244] Ryudamon (samurai horned dragon cub)
    val RYUDAMON_ROM = arrayOf(
        "....TBTBT.TBTB..",
        "..TBTBTBTB...B..",
        ".B.BTBT.....T...",
        "T.TBT.....TB....",
        "#....B#BB#B.....",
        "#BB#B.#B.#.B....",
        "#....B#..#BB....",
        "#BB#.....#B.#...",
        ".B.......##B#...",
        "..B#B..BB.B..B..",
        ".#.B..B.BB#BB.BB",
        ".#.B..B.BB.BB..B",
        "..BB...#BB#..#B.",
        ".#B.#.....#...B.",
        "B.B..BB#BB.B.#.B",
        "B#BB#BB...#BB#BB"
    )

    // [ID 245] Dracomon (progenitor winged dragon)
    val DRACOMON_ROM = arrayOf(
        ".....BTBT...TB..",
        "....T....BTB.BTB",
        "...B......T....B",
        "..TB...BT..BTBT.",
        ".BB...#B....#...",
        "#....B#B..BB..B.",
        "#...........#.B#",
        "#..#BB#.B.BB.B##",
        ".BB....B...B#S##",
        "...#BB#.....#BB#",
        "...B....BB..B..B",
        "..B.#..#...B.#..",
        "..B.#..#BB#...B.",
        "...B.B..B...B..B",
        "..B...B#.B.B.#BB",
        "..BB#B..BB#BB#.."
    )

    // [ID 246] Kamemon (cyborg mouse turtle)
    val KAMEMON_ROM = arrayOf(
        "......BBBB......",
        "....BB.###BB....",
        "...B.##TTTTCB...",
        "...BBS##SBBCB...",
        "..B##T#T.TTBB...",
        "..B#BBBBBBTTCB..",
        "..BBGGGGGBBBB...",
        "...BTBBBGGBB....",
        "....BTGGGGBBBB..",
        ".....BBBBBBGBSB.",
        "....BBRRRRBGGBB.",
        "...BGBTTTRBGGBB.",
        "..BGBBTBTBG.BBB.",
        "..BBBBBTTBBBBB..",
        "....BG.BBBG.GBB.",
        "....BBBBBBBBBB.."
    )

    // [ID 247] Coronamon (sun flame lion cub)
    val CORONAMON_ROM = arrayOf(
        "....TBTB...B....",
        "...BTBT...T.T...",
        "..T.TBTBTB..T...",
        "..TBT....B..T...",
        "...#..#B...B....",
        "..B....BB..B....",
        "..B...#BB..B...#",
        "...#B......B..B#",
        "...#......B..BB#",
        "....BB#BB#B..B.#",
        "...B.B##...B..B.",
        "..B.#.B..B..B.B.",
        "..B.#....B..B.B.",
        "...B.B..B.#BB#..",
        "..B...B#...B....",
        "..BB#B..BB#B...."
    )

    // [ID 248] Lunamon (moon rabbit with drooping crescent ears)
    val LUNAMON_ROM = arrayOf(
        ".....BT....BT...",
        "....T..B..T..B..",
        "...B..T..B..T...",
        "...B.BTBTB.B....",
        "..B.........#...",
        ".B.#B..BB....B..",
        ".B.#....B.....B.",
        ".B.#B..BB..B...#",
        ".B.#B..BB..B#..#",
        "..B.......BB.BB.",
        "..BB#BB#BB..B...",
        ".#.B.B.#.....#..",
        ".#.B.B.#..#..#..",
        ".#.B..B...#B.#..",
        ".#B.........B#..",
        "..BB#BB#BB#BB..."
    )

    // [ID 249] ToyAgumon (brick constructed toy dinosaur)
    val TOYAGUMON_ROM = arrayOf(
        "......TB.BT.....",
        "....TBTBTBTBT...",
        ".BT.TBTBTBTBT...",
        ".BTBTBT..BTBT...",
        ".B#S#S#B..BS#...",
        ".B#S#S#...BS#...",
        ".BB#BB#BB#BB#...",
        ".B..........#...",
        ".BB#BB#....B#...",
        "..B#B...B#.B....",
        "..B.#...B...B#..",
        "..BB#...BB#..#..",
        "....#B.......#..",
        "..BB#SB#BB#BB#..",
        ".##S#SB.B.....B.",
        ".#BB#BB.BB#BB#B."
    )

    // [ID 250] Keramon (wireframe tentacle rookie)
    val KERAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        ".....B...B......",
        ".....B.B........",
        "....BTTT#B#.....",
        ".....STSB#..B...",
        "....BSWB#.......",
        "..BSBBTTTBBBB...",
        "..BB#.BTS.#.S...",
        "...#.#.#TB......",
        ".......BBTG.....",
        ".......BBB......",
        "................"
    )

    // =========================================================================
    // Stage 4: Adult / Champion (350 - 372)
    // =========================================================================

    // [ID 350] Ikkakumon (giant walrus with crimson mane and harpoon horn)
    val IKKAKUMON_ROM = arrayOf(
        ".BTB............",
        ".B..TB..........",
        "..T...TBT.......",
        "...B..T..B......",
        "..B.BB....B.....",
        ".B........B.....",
        ".B.....B...B....",
        ".BB...#B...B....",
        "#..........B....",
        "#...B......B....",
        ".#BB#BB#....B...",
        ".#.B..B.B...B...",
        ".#.B..B.B.#..#B.",
        ".#B....#..#....B",
        "B..B...#...B...B",
        "B#B.#BB#BB#BB#BB"
    )

    // [ID 351] Golemon (rock colossus with magma fissures)
    val GOLEMON_ROM = arrayOf(
        "................",
        "....TBTBT..BT...",
        "...BTBTBTB.B.B..",
        "..TBTBTBT.T..B..",
        ".B#S#B.BB.B.#...",
        ".B#SB..BB.BB#B..",
        ".B#S#B#SB.B...B.",
        ".B##BB#B.#B...B.",
        "..B.....B.BB#B..",
        "...#BB#B..B..B..",
        "..BB#.....#BB#B.",
        ".#..#.B..B#...B.",
        ".#.B#B.#BB#B..B.",
        ".#..#.B.B.#...B.",
        "..BB..B#...BB#..",
        "...B#BB.BB#B...."
    )

    // [ID 352] Gekomon (horned musical amphibian)
    val GEKOMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "....TB.BT.......",
        "...#..#..#......",
        "...#..#..#......",
        "...#B.#B.#B.....",
        "..B.B..B..BB....",
        ".B..........#...",
        ".B..........#B..",
        ".#BB#BB#......B.",
        "..B.....B.....B.",
        "...B....B.....B.",
        "...B...#B..BB..B",
        "..B.#BB...#....B",
        "..BB#.B#BB.BB#BB"
    )

    // [ID 353] Clockmon (mechanical timekeeper robot)
    val CLOCKMON_ROM = arrayOf(
        "........TB......",
        "....TB.B..T.TB..",
        "...B..TBTBTB..T.",
        "...B.B...BTBT.T.",
        "....B.#BB.BS#B.#",
        "...#.B.B.#.B#SB#",
        "...#.B.B.#.B#B.#",
        ".BB#.B..B#.B#B.#",
        ".B.#.B...#.B..B#",
        "#BB#B.#BB.BS#..#",
        "B.B..B...B#B..BB",
        "B.B..BB#BB#BB#B.",
        "B#BB#.B.B.#.B...",
        "....#B##B.#BB#..",
        "...B..BSB.#B..B.",
        "...B#BB#B.#BB#B."
    )

    // [ID 354] Guardromon (heavily armored defensive cyborg)
    val GUARDROMON_ROM = arrayOf(
        "...BTBTBTBT.TBT.",
        "..T........B...B",
        "..TBTBT...T....B",
        ".BT.T.TB..TBTB.B",
        "#.B#BB#BB#B...B#",
        "#B...B....B...B.",
        "#B...B....B...B.",
        ".BB#B..BB..B#BB.",
        "#BB....BB#B....#",
        "#.B#BB#B..B....#",
        "B..B#..#..#....B",
        ".#BS#BB#BB#BB#BB",
        "...B..B.B..B....",
        ".#BS#BB.BB#SB#B.",
        "B..B..B.B..B...B",
        "B#BB#BB.BB#BB#BB"
    )

    // [ID 355] Woodmon (forest treant with hollow branch eyes)
    val WOODMON_ROM = arrayOf(
        "........T.......",
        "...B.B.BTB.B....",
        "T..BT.TB.BTB...B",
        "TB.B..T..B.B..TB",
        "#.B#.......B.B.#",
        "#.B#.......B.B.#",
        "#.B.B....#.B#B.#",
        "#...BB..B#.B...#",
        "#..#...........#",
        ".BB#.........BB.",
        "...B#.B#.B#.B...",
        "...B.BB#BB..B...",
        "..BB.......BB#..",
        ".#............B.",
        ".#.B.BB#BB..B..B",
        ".#BB#BB...#BB#BB"
    )

    // [ID 356] Octomon (ink pot octopus)
    val OCTOMON_ROM = arrayOf(
        ".....BTBTBT.....",
        "....T......B....",
        "...B.B......T...",
        "..T.T........B..",
        "..B.B........B..",
        ".BB..........BB.",
        "#..#.B#BB#..#..#",
        "#.B..........B.#",
        "#.B.BB#BB#BB.B.#",
        "#.B#.B#S##.B.B.#",
        "B..B..B#B..BB..B",
        ".#B.#B...B#...B.",
        ".#.B.B.#.B.BB#..",
        ".#.B.B...B.B.#..",
        "B..B..B#B..B..B.",
        ".#B.#B...B#.B#.."
    )

    // [ID 357] Gesomon (white squid with abyssal tentacles)
    val GESOMON_ROM = arrayOf(
        ".......BT.......",
        "......T..B......",
        ".....B....T.....",
        "....T..B...B....",
        "...#...B....#...",
        "..B...#......B..",
        ".B............B.",
        "..B#B.....BB#B..",
        "...#.B#BB#.B#...",
        "...#..#BB..B#B..",
        "..B.#B..BB#B..B.",
        ".#....B#B......B",
        ".#.B.........#.B",
        "..BB#.B.B..BB.B.",
        ".#....B..B....B.",
        "..BB#B.#B.#BB#.."
    )

    // [ID 358] Ebidramon (shrimp sea dragon with long antennae)
    val EBIDRAMON_ROM = arrayOf(
        ".B..TB.....BTBTB",
        "T..B...BTBT...T.",
        "T.T..BTBTBTBTB..",
        "T.T.TBT..BT..B..",
        ".B###B..BSBB#.B.",
        "..B#BS#B##.B..B.",
        "..B..BWS#WB..B.#",
        ".B.#BW#B##..#..#",
        "#....B..B.B...B#",
        "#.B.B...B.BB.B.#",
        "B.BB..B#..#.B.BB",
        ".#B..B...B..B#.B",
        "....#BB..B..B.B.",
        "....#...B..B##..",
        ".....BB#..#SBSB.",
        ".........B#B.#BB"
    )

    // [ID 359] Raidramon (blue lightning quadruped beast)
    val RAIDRAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "..T.....B.......",
        "..BSBCBB........",
        "...S#BBS.BS.....",
        "..S#S#SS.#B.B...",
        "..##S#SSSBSB....",
        "..BBBBB#SBS##...",
        ".....BSBSB#B....",
        ".....BB.SBBS....",
        ".....BBBSS.SB...",
        "...#SBTWWBSSB...",
        "................"
    )

    // [ID 360] Digmon (golden drill beetle)
    val DIGMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        ".......BR.S.....",
        "......TB.#R.....",
        "......TBSB......",
        ".....STTTST.....",
        ".....BBT#T#B....",
        "....BS#TTSTBT...",
        "...#SBBSBT#.#...",
        "....##BSSSTT....",
        "....SBBSTTTT#...",
        "....B.BB##.B....",
        ".....T#SBBTTT...",
        "................"
    )

    // [ID 361] Shurimon (ninja leaf shuriken fighter)
    val SHURIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "....B.BSBBB.....",
        ".....TTTBBB.....",
        ".....WSBT##B....",
        "....TBBB#TBS#...",
        "...B.WTTTBBB....",
        "...SBBB#SBSSB...",
        "...B#WWTWB#BB...",
        "..B..#B.TB..S...",
        ".....BB...GB....",
        "...B##B..BS#B...",
        "................"
    )

    // [ID 362] Submarimon (yellow submarine companion)
    val SUBMARIMON_ROM = arrayOf(
        ".............BT.",
        "............T.T.",
        ".....BTBTBTB..T.",
        "....T..B.B.B.B..",
        "...#..#BB...#SB.",
        "..B.BB.....B.B.#",
        "..B.....B.B..B.#",
        "..B....BB.B.#SB.",
        "..B#BB....B..BB.",
        "..B#BS#...BB#B.#",
        "..B..BB..B.B##BB",
        ".#..#B##B..BB...",
        "B..B#.B#BB#..#B.",
        ".#B..B.....B...B",
        "..BB#.......B#BB",
        "................"
    )

    // [ID 363] Peckmon (ninja avian runner)
    val PECKMON_ROM = arrayOf(
        "....TBTBTBT.....",
        "..TB.....B......",
        ".B.BTBTBT..BTBT.",
        "...B..T.T.TBTB..",
        "..B.B...B#B...B#",
        "..B#BB.B##..#B..",
        ".....B#SB.BB#...",
        "....B..BB#B..B..",
        "....BB#..#.B..B.",
        "...#..#....B..B.",
        "...B...#..#...B.",
        "....#.B.BB#B.#..",
        "....#BB..B.BB...",
        ".....B.#.B......",
        "....#.B..B......",
        "...B#BB#BB#....."
    )

    // [ID 364] Sunflowmon (sunflower blooming fairy)
    val SUNFLOWMON_ROM = arrayOf(
        "......TB........",
        "...BTB..TBT.....",
        "..T..BTBT..B....",
        "..T.TBTBTB.B....",
        ".B.#.B#B..B.#...",
        ".B.#B...B.B.#...",
        "..B#......BB....",
        ".B.#.B#B..B.#...",
        ".B..B....#..#...",
        "..B#.B#BB.BB..B#",
        "...B#B..BB#..#.B",
        "..B..BB#B..BB..B",
        ".#..#....B..B.B.",
        ".#.B#....B#.B#..",
        ".#B..BB#B.#BB...",
        "..BB#B..BB#BB..."
    )

    // [ID 365] Reppamon (blade-tailed sacred fox)
    val REPPAMON_ROM = arrayOf(
        "...........BT...",
        "...........B.B..",
        "....TB.BT..BT.T.",
        "...B.BT.T..B.B.B",
        "..B..B..B...#B.#",
        "..B.B...B...#B.#",
        ".B......B#...B.#",
        ".B...B#BB.B..BB#",
        "#...BB..B.BB.B##",
        "#......B..B.#SB.",
        ".#....B..B...#B.",
        "..BB#B.#B....#..",
        "...B#.........B.",
        "..BB..B.BB.BB.B.",
        ".#.B.B.#BB#.B.B.",
        ".#BB#BB#..#BB#.."
    )

    // [ID 366] Ginryumon (armored samurai dragon)
    val GINRYUMON_ROM = arrayOf(
        "................",
        ".....BTB..TB....",
        "...BT..B.B.BT...",
        "..TBTBTB....T...",
        ".B.#B.....BB.B..",
        ".B...B#BB#....B.",
        ".BB#B.#.B.B...B.",
        ".B...B..B.B..BB.",
        "..B......#.B..B.",
        "...#BB....B.#.B.",
        "...B....B..BB.B.",
        "........BB#...B.",
        "...B#..#...B..B.",
        "..BB.B.#.B.B..B.",
        "..BB#B.#BB#B.#B.",
        "................"
    )

    // [ID 367] Coredramon (winged sapphire dragon)
    val COREDRAMON_ROM = arrayOf(
        "TB..TBTBTBTBT...",
        "T.TB....T...T...",
        "T.T......BTB....",
        "TB....TB.B.B..T.",
        "#....B#B.#..#.B.",
        "#.........BB#B##",
        "#.B#BB....B.#S##",
        ".B....#...B..B##",
        "..B#B......B#S##",
        ".B.#..#BB..B.B##",
        "B..B.B.....B..BB",
        "B..B.B....#BB#.B",
        ".#BB..B#BB..B..B",
        ".#..#...B....#..",
        "B.B..BB#..#.B.B.",
        "B#BB#B.#BB#BB#B."
    )

    // [ID 368] Chrysalimon (tentacled cocoon entity)
    val CHRYSALIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "........B#B.....",
        ".....#.T#..#....",
        ".....BRR#B..B...",
        "..BS..BB#SB.....",
        ".WS..BB##BB.....",
        ".....TSB##......",
        "..S...#T#B......",
        "......BBBT#S....",
        "...T..ST#S......",
        "...#..B.#S......",
        ".....TBWB.BB....",
        "................",
        "................"
    )

    // [ID 369] Firamon (sun mane fire lion)
    val FIRAMON_ROM = arrayOf(
        "..............TB",
        "....TBTBTBT..B.B",
        "...B.......BT..B",
        "..T..BTBT...T.TB",
        ".B..B....#..#B.#",
        ".BB#..#B..B..B.#",
        ".B...B#...B..BB.",
        "#....B#B..B..B.#",
        "#BB#.....#..#..#",
        "#......BB..B...#",
        ".#BB#BB...#...B.",
        ".#.......B.B..B.",
        "..B....#B..BB..B",
        "..BB.BB#.B#SB..B",
        ".#..#.B..B.B...B",
        ".#BB#.B#BB.BB#BB"
    )

    // [ID 370] Lekismon (moon boxer rabbit)
    val LEKISMON_ROM = arrayOf(
        ".BTBT..BTBT.....",
        "..T..B..T..B....",
        "...BT.T..B.B....",
        ".....BTBTBT..BT.",
        "....BS#SB##B#..#",
        "....BS#B..BB.BB.",
        "...##W#..#WB...#",
        "...##B#BBWB.....",
        "....B...B#......",
        ".....B#B..B.....",
        "....#...B..B....",
        "...B.BB#.B#.B...",
        "...B.BB..B..B...",
        "....#....B#B....",
        "...B..B#B...B...",
        "...B#BB.BB#BB..."
    )

    // [ID 371] ExVeemon (pure dragon champion with chest X)
    val EXVEEMON_ROM = arrayOf(
        ".BT......BTB....",
        ".B.B....T..BTBT.",
        ".B.B.BTBTBT..B..",
        ".B..T.......T...",
        ".B..B..BB#.B....",
        "..B#..#BB...#.B.",
        "..B.........#B.#",
        "..B.BB.B...B...#",
        "...#..#...B.#..#",
        "....B.......#.B#",
        "...B#B.#..#B.#.B",
        "..B.#.B..B...#..",
        "..B.#B.#.B...#..",
        "...B#....B#BB#..",
        "..B.#.B#B.#.B.B.",
        "..BB#B..BB#BB#B."
    )

    // [ID 372] Lynxmon (flaming feral feline)
    val LYNXMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        ".......B........",
        ".....B.RB.......",
        "...BTT.SB.BT.B..",
        "..TTT##BRTR...B.",
        "..BTRRRTTR...BB.",
        "..TR####TTTSTSB.",
        ".B##BRBRTB###B..",
        "...BB.TBR#.RT...",
        "....BBB..ST.BBB.",
        "..BTRTB##T..TR..",
        "..##T#TTBBS#TT..",
        "................"
    )

    // =========================================================================
    // Stage 5: Perfect / Ultimate (444 - 464)
    // =========================================================================

    // [ID 444] ShogunGekomon (massive warlord amphibian with pipe organs)
    val SHOGUNGEKOMON_ROM = arrayOf(
        "........BBBBB...",
        "........BSSSSB..",
        ".........BSSSB..",
        "..........BTB...",
        "....BB.BBBTRB...",
        "...BTTBTTBRB....",
        "...BS.BS.BB.....",
        "..BBB.BB.RBB....",
        ".BTTTRRRSSRBB...",
        "BTTTRRRRSSRBTB..",
        ".BBBBBBRRRSSBBB.",
        "..BTTTTBSSSSBSB.",
        "...BTTTTBSSSSBB.",
        "...BTTTBBSSBBSSB",
        "..BRBBBSSSBSSSSB",
        "..BBB.BBBB.BBBBB"
    )

    // [ID 445] Volcamon (magma rock android)
    val VOLCAMON_ROM = arrayOf(
        "................",
        "......BBBBB.....",
        ".....BSSSSSB....",
        "....BSTSTSSBBB..",
        "....BSSTSSSBR#B.",
        "....BTBBBTSBSSB.",
        "..BBBBSSSBBBBBB.",
        ".BTTBTSSSSTBTTTB",
        ".BTTBBTTTTBTBT#B",
        ".B#BTBBBBBTTB#B.",
        "..BBBTTTTTT#BBTB",
        ".B#SB#BBB##BSBTB",
        ".BSSBBSSSBBBSSB.",
        "..BB##BBB##BBB..",
        "..B#SSSBBSSS#B..",
        "..BBBBB..BBBBB.."
    )

    // [ID 446] MarineDevimon (demon deep-sea squid)
    val MARINEDEVIMON_ROM = arrayOf(
        ".......BB.......",
        "......BTSB......",
        ".....B#TSSB.....",
        "....B#STSSSB....",
        ".B.B##STSSSSB.B.",
        ".BB#S#S#SSSSSBB.",
        ".B##SRSSSRSSSSB.",
        "..B##SSSSSSSSB..",
        "...B##SSSSSBB...",
        "..B#BTTTTTB##B..",
        ".B##BBBBBBBBBTB.",
        ".BTBB#TTTSB..TB.",
        ".B..B##TSSSBBBB.",
        "BTBB#B##SSB#TBTB",
        "B...T#BBBB#T...B",
        ".BBBBBB...BBBBB."
    )

    // [ID 447] WaruSeadramon (dark serrated sea serpent)
    val WARUSEADRAMON_ROM = arrayOf(
        "TB.......BTBTB..",
        "TBTB.BTBTBTBT...",
        ".BTBTBTBTBT..BT.",
        "..TBT...TBTBTB..",
        ".B##.B.B##..#...",
        "#S##BB#S##BB#B..",
        "#SB...#SB....BB.",
        "#B.#BBWB.#BB#...",
        "..B##B#BBS#B#B..",
        "....B.#.BSB.....",
        "...B#.B#.B#B..BB",
        "..BB.B#SB....##B",
        "..B.#.BS#B#BBSB.",
        "..BB.B.##B#S##..",
        "...B#.B.B.#SB...",
        "....#BB#BB#B...."
    )

    // [ID 448] Infermon (spidery spider cocoon demon)
    val INFERMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "......B.T.......",
        "......SSST......",
        ".BS..SSBSBB.....",
        ".BBB#S#B#T#B....",
        "..#STSB#TST#.B..",
        "..##WT#B#BTS#S..",
        "..#TWBBBB.S#BS..",
        "..#.##BT..BST#..",
        "..#...B.B..#.S..",
        ".BTS..BBBSTBBB..",
        "................"
    )

    // [ID 449] Chirinmon (sacred golden horned qilin)
    val CHIRINMON_ROM = arrayOf(
        "................",
        ".BB...BBB..BB.BB",
        ".BRBBBT#BBBBTB.B",
        ".BRSBBBBSB#BB.B.",
        "..BSBSSSBSBBTTBB",
        "BBBBS.BBTTSBBTTB",
        "BBSSBTSTBBBSBTB.",
        "BSSSSST.TBSBTT.B",
        "BTTTTT.TBBBTTT.B",
        ".B..TBBBBSSBBBB.",
        "..BBBSSBRTTSSBTB",
        "...BTBT#B#BT#BTB",
        "....BTBTBB#BT#B.",
        "....BSBSB.BBBSB.",
        "...BSBSBBBBBSBB.",
        "...BBBBB.BBBBB.."
    )

    // [ID 450] Hisyaryumon (armored samurai serpent dragon)
    val HISYARYUMON_ROM = arrayOf(
        "................",
        "......TBTBTBT...",
        "....TBTBT..B....",
        "...BT.....TBTB..",
        "..B..B#....B....",
        ".B..BB...#BB#...",
        "#......BB#......",
        "#.........B.....",
        "#..#BB#BB#...BB.",
        ".BB.B....#...B.#",
        "....#...BB#..#.B",
        "...B#..#B.#BB#.B",
        "..B.#BB...#SB..B",
        "..B.#.B..B.B.#.B",
        "...B#B##BB..B#B.",
        "......B#BB#B...."
    )

    // [ID 451] Wingdramon (lance-winged cobalt dragon)
    val WINGDRAMON_ROM = arrayOf(
        ".BT...TBTBTBT...",
        ".B.BTBTBT...T...",
        "T.TBT..BTBTB.B..",
        "T.TB..TBT..B.BT.",
        "#B##BB#B..B..BB.",
        "#SB.B.#.B#..#S##",
        ".B........BB#S##",
        "..B#BB#B...B#S##",
        "....B.#..#.B#B##",
        "....BB#BB...#.B#",
        "...B#...B...B.BB",
        "..B.#..#...BB.B.",
        "..BB#..#..#..#..",
        "..B..B..BB....B.",
        ".#.B..B#BB.B.#.B",
        ".#BB#BB..B#BB#BB"
    )

    // [ID 452] Yatagaramon (three-legged gold ringed raven)
    val YATAGARAMON_ROM = arrayOf(
        "........TB.BT...",
        "....TBTB...BTB..",
        "...B..T....B.BT.",
        "..T....B.BTB.BT.",
        ".B..BB.BB..B.B.#",
        ".B.#B.#B##.B#B.#",
        "#....B#.B.BS#.B#",
        "#..#B..B..BS#B##",
        "#.B..B#..##S#SB.",
        "#B.#BB..BS#S#B..",
        "B.B...B##S#SB...",
        "....#B#SBS#SB#..",
        "....#.B#.B.B.#B.",
        "....#B.#B.#B....",
        ".....B.#B.#.....",
        "......B#.B#....."
    )

    // [ID 453] Lilamon (graceful floral fairy)
    val LILAMON_ROM = arrayOf(
        ".B..TBTBTBTB..T.",
        "T.TB......T..B.B",
        "T.T........BT..B",
        "TB.........B...B",
        "#B...B..B...#..#",
        "#B..B.#B.#..#..#",
        ".B.#.B..B.BB#.B.",
        "..B#.B..B..B.B..",
        "..B#.......B#B..",
        ".B..B.....B...B.",
        "B..B#BB#BB.B...B",
        "B.B.#....B..B..B",
        "B#..#....B.B.#.B",
        "B#.B.B..B.#..#BB",
        ".#B..BB#...BB#..",
        "..BB#B..BB#B...."
    )

    // [ID 454] Flaremon (solar beast warrior)
    val FLAREMON_ROM = arrayOf(
        "....TBT.........",
        "..TBTB.BTBTBT...",
        ".BTBT......BTB..",
        ".B.BTBTB.B..T...",
        ".BB#....B.B.#BB.",
        ".B....#B..B...B.",
        "#....B#..#B..B..",
        "#BB......#....B.",
        "#......BB.BB#BB.",
        ".BB#BB#..#B..B..",
        "B..B#BB#BS#B.#..",
        "B..B#...BB#..#..",
        "B.BB......#..#..",
        "B...#BB..B#BB#..",
        "B...#..#BB....B.",
        ".#BB#BB#..#BB#B."
    )

    // [ID 455] Crescemon (moonlight scythe warrior)
    val CRESCEMON_ROM = arrayOf(
        "......TB.BT.....",
        ".....B.BT.T...T.",
        "....TBTB.B..TB..",
        "...BTBTBTBTB..T.",
        "...##S#.BSB.#B..",
        "..BS#B..B#......",
        "..BSBB#B##BB#B..",
        "...#...BB.B...B.",
        "....BB#BB#..#B..",
        "...#..#..#..#...",
        "..B..B..BB..B...",
        "..B.#BB#.B..B#..",
        "...B#.....#...B.",
        "....#.B....BB#..",
        "...B...#...B....",
        "...B#BB.BB#B...."
    )

    // [ID 456] SkullSatamon (skeletal fallen demon)
    val SKULLSATAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "...B..SB..#BT...",
        "..B.SSS#..B##S..",
        "..BBSRS..BBBBBB.",
        ".B.B#BBBRBBBB.#.",
        ".BB.SBRRW#B.B.B.",
        ".BB.#SSBBT#TS...",
        "...#..RSBSTBR...",
        "...S#BWBBB......",
        "....T#.BTSB.....",
        "..BBS##BS.B.....",
        "..#BS..TSBT.....",
        "..BB.TWBST#W....",
        "................"
    )

    // [ID 457] Mummymon (bandaged gunslinger)
    val MUMMYMON_ROM = arrayOf(
        ".....BTBTB......",
        "....TBTBTBT.....",
        "....TBTBTBTB....",
        "...B...B.BTB....",
        "...#....B#..#...",
        "...#BB#....B....",
        "...#...B...B#...",
        "....B.....BS#...",
        "....BB#BB#.B#...",
        "...#.B.B.#BB....",
        "..B.#.B..B..B...",
        "..B.#B.#BB..B...",
        "...B#....B#B....",
        "....#.B#..#.....",
        "...B..B.B..B....",
        "...B#BB.BB#B...."
    )

    // [ID 458] Arukenimon (spider enchantress)
    val ARUKENIMON_ROM = arrayOf(
        "T..BTBTBTB...B..",
        "TBT......BTBTB..",
        "TB..T.TB..TBT...",
        ".B.BTBT.T.T..B..",
        "..B.BB.B##B...B.",
        "..B##S#BBS#B.B..",
        "..B.BB#..#WS#...",
        "...#....BWWB#...",
        "...#BB#B##B.#...",
        "..B..B#SB...#B..",
        "..B..B#SB..B#SB.",
        "..BB#SB##B#S##B.",
        ".#..#B..BB.BB..B",
        ".#.B#B.#BB.BB#.B",
        ".#.B.B.#.B.B.#.B",
        ".#B..BB..B#...BB"
    )

    // [ID 459] Grademon (golden dual-blade knight)
    val GRADEMON_ROM = arrayOf(
        "....TBT.TBT.....",
        "...B.BTB..T.....",
        "..T.T...TB...BT.",
        ".B...BTB..T.T.T.",
        "#...B.#.B#..#.B.",
        "#BB#BB...#BB.B..",
        "..B..B..BWBB#B..",
        "..B.B..B.#...B..",
        "...#BB#..#BB#...",
        "...#....B#B.#...",
        "..B.#.B#B...B#..",
        "..B.#B..B..B##..",
        "...B#..#.B#S#SB.",
        "..B..BB#...B##BB",
        ".#.B.B..B.#.B...",
        ".#BB#B..BB#BB..."
    )

    // [ID 460] Astamon (demonic mobster prince)
    val ASTAMON_ROM = arrayOf(
        ".BT....BTBTB....",
        ".B.BTBTBTBT.....",
        ".B.BTBTBTB......",
        ".BTBTBTBT.T.....",
        ".B#S#B.BB..B.B..",
        ".B#SB..BB..B.BB.",
        ".B#SB.#.B..B#SB.",
        ".B##BB..B..B#S##",
        "..B....B..BS#S##",
        ".B.#BB#BB#.B#B##",
        "B..B..B..B..B.BB",
        "B.B.#B..BB#.B..B",
        ".##B...#B...B...",
        "..B.#BB..B#B....",
        ".#...BB#....B...",
        ".#BB#B..BB#BB..."
    )

    // [ID 461] Gargoylemon (white stone sacred gargoyle)
    val GARGOYLEMON_ROM = arrayOf(
        "...BTBTB..TBTBT.",
        "T....BTB...B..T.",
        "..T.T.TB.B..T...",
        "...BTBTB........",
        ".......BB...#...",
        ".BB#B...B..B....",
        ".B..BB.BB#......",
        "#.....#...B..B..",
        ".B..B....#B..B..",
        "....B.#BB.B.#.B.",
        ".#.B.BB.B...B...",
        "BSB...B..B#..#..",
        "B#B.#B.#.B#...B.",
        "B...#B..B..B....",
        "B...#B.......#..",
        "................"
    )

    // [ID 462] Mammothmon (primeval ancient beast with ivory tusks)
    val MAMMOTHMON_ROM = arrayOf(
        "....TB.BTB......",
        "...B..T...TB....",
        ".BT..B......TBT.",
        "T.T.TB......TBTB",
        "#.B#..#.....#.B#",
        "#.B.BB......#.B#",
        ".BB.....B#..#B.#",
        "#..#...B..BB#B.#",
        "#.B#...BB.B.#.B#",
        "#B.....BB.B.#.B#",
        ".#.B..B...#B.#.B",
        ".#...B##BB.B.#.B",
        "..BB#SB#BB.BB#B.",
        ".#..#B...B#B...B",
        "B.B.#.B..B#.B..B",
        "B#BB.BB#B.#BB#BB"
    )

    // [ID 463] MetalGreymon (Virus) (cybernetic blue dragon with chrome trident claw)
    val METALGREYMON_VIRUS_ROM = arrayOf(
        ".....BBBBB..BBB.",
        "BB..BWWWWWBBWWB.",
        "BWBBBWBBBWRWRB..",
        "BBBWWBGBWTTBBSB.",
        "BWWWWWWWWBB.BSB.",
        "BBBBBBWWB#B.BSB.",
        ".B####BB##RBSSSB",
        "..BBBBB##WTBSSSB",
        "....BWTWWBWBSBSB",
        "..BBWTWBBWWWBBSB",
        ".B#BTTBWWBWTBBSB",
        ".B#B##BWTBTBB.BB",
        "..BB###BBBB##B.B",
        ".BB#B#####B###B.",
        "B#B##BBBBB#B#B#B",
        "BBBBBBB...BBBBBB"
    )

    // [ID 464] Cyberdramon (Savers) (streamlined sleek dragoon hunter)
    val CYBERDRAMON_SAVERS_ROM = arrayOf(
        ".....BTBTBT..BTB",
        "..TBT....B..T.T.",
        ".B...BT.T..B..T.",
        "T...TB.BTB.B...B",
        "#BB...#B#SB.#B.#",
        ".B.#BB#.B#BB.B.#",
        "..B....BB..B.B.#",
        "...#BB#..#B.#..#",
        ".BB.B..BBS#B#..#",
        "#B##BB#B.##S#B.#",
        "B.BB...#B.#B..BB",
        ".##S#BB.BB#...BB",
        "..BB..B##S#B...B",
        ".##S#B.##B#SB...",
        "B.BS#B..B..B.#..",
        "B#BB#B..BB#BB#.."
    )

    // =========================================================================
    // Stage 6: Ultimate / Mega (558 - 582)
    // =========================================================================

    // [ID 558] RustTyrannomon (rust-armored dreadnought with electromagnetic cannon)
    val RUSTTYRANNOMON_ROM = arrayOf(
        "TB....TB..TB....",
        "T.T..B.B.B.B....",
        "T.T..BTBTBTB....",
        "T..BT.....T..B..",
        "#.B#..#B.#.B#.B.",
        "#....B#....B...#",
        "#.........B....#",
        "#.B.B.#.B#BB...#",
        ".B.#.B.BB..B...#",
        "#.BSBB#..#.B...#",
        "B#.B..B#.B..B#B.",
        "B..B..B..B#B....",
        ".#BB...#BB..B...",
        "..BS#....B...#..",
        ".#.B.BB#B.#.B.B.",
        ".#BB#BB#.B#BB#B."
    )

    // [ID 559] BlitzGreymon (plasma cannon artillery dragon)
    val BLITZGREYMON_ROM = arrayOf(
        ".......BB..BB...",
        ".BB..BBSSBB#B...",
        ".B#BB###SBB#BBB.",
        ".BCB###BB#SSB##B",
        "..BC#SBGBBBB###B",
        "..BBBBBBSB#BBS#B",
        "...BTTTTBB###B#B",
        "....BBBB#BBBBBSB",
        "..BBS##SSBB#TBSB",
        ".B#B#SSSB##TSBSB",
        ".BSBS##SB#STSBSB",
        "..BBB##BTBBBBBSB",
        "..B#BBBBBS##B.BB",
        ".BB#SB..BSSSB...",
        "B#S#SB..BS#S#B..",
        "BBBBBB..BBBBBB.."
    )

    // [ID 560] CresGarurumon (golden blade knight wolf)
    val CRESGARURUMON_ROM = arrayOf(
        ".......BTBTBT...",
        ".....BT.....T...",
        "....T.....TB....",
        "..TB..TB.BT.TB..",
        ".B...B#..#BB.B..",
        ".B......B#...B..",
        ".BB#BB#BB..B#...",
        "..B....B..BB....",
        "...#BB#B.##B....",
        "..B#....B#B.#...",
        ".#.B#.B.B...B...",
        ".#.B.B..B..B....",
        "..BB#..#BB#.....",
        "..B.#BB#..#B....",
        ".#..#...B...B...",
        ".#BB#...BB#BB..."
    )

    // [ID 561] Ouryumon (golden armored blade dragon emperor)
    val OURYUMON_ROM = arrayOf(
        ".....BT.TBT.T...",
        "....TBTBT..B.B..",
        "..TBT.....TBT.T.",
        "TB....TB....T.T.",
        "#...BB#..#BB#..#",
        ".BB#......B.#..#",
        ".B.....BB#B.#B.#",
        "..B#BB#BB.BB#B.#",
        ".....B....BS#B.#",
        "....B....#.B#B.#",
        "...B#..#B...B#BB",
        "..B.#.B.B..B..B.",
        "..B.#.B#.B#....B",
        "...B#B.#B..BB#.B",
        ".....BB...#B.#.B",
        ".......#BB....BB"
    )

    // [ID 562] Slayerdramon (fragarach sovereign dragon knight)
    val SLAYERDRAMON_ROM = arrayOf(
        "......TB...BT...",
        ".BT.TBTBTBT.T...",
        ".B.BT...T..BT.TB",
        ".B.B..TB..T..B.B",
        "..B..B.BB#B.#..#",
        "..B#BB#..#BB#..#",
        ".B.#....B#...B.#",
        ".B..BB#B.#...B.#",
        "..B#B..B..BB#B.#",
        ".B.#.B#..#B..B.#",
        ".#BS#..#B.#BB#.B",
        "B..B#..#.B#..#.B",
        "B.B.#BB#.B.BB#BB",
        ".#B..B.#..#B..BB",
        "B.B..B..BB.BB.B.",
        "B#BB#B..BB#BB#B."
    )

    // [ID 563] Apollomon (sun deity of the Olympos XII)
    val APOLLOMON_ROM = arrayOf(
        ".....BTBTBTBTBT.",
        "...BT.........T.",
        "..T..BTBTB...B..",
        "..TBT.....TB.BTB",
        ".BB....BB..B...#",
        ".B....#B..B...B.",
        ".BB#B....#..#.B#",
        ".B.....BB..B#B.#",
        "..B#BB#...BB#B.#",
        "...##S#BB#B..BB.",
        "..B.#B..BB...#..",
        ".#..#.B#.B..B...",
        ".#.B#.....#B....",
        "..BB..B#....B...",
        "..B.#.B.B..B.#..",
        "..BB#BB.BB#BB#.."
    )

    // [ID 564] Dianamon (moon deity of the Olympos XII)
    val DIANAMON_ROM = arrayOf(
        "TBTB......TB....",
        "T..BTBTBT..BT...",
        ".B.B.....B..TB..",
        "..TBTBTB..T.TBT.",
        "..B#...B..B.#SB.",
        "..B#..#B..BB#B##",
        "..BSBB#..#..#.B#",
        "...#BB#BB#.B#.B#",
        "..B#.....#..#..#",
        ".B.#B...BSBB.B.#",
        ".#.B#BB#BB#..#..",
        "..B..B##..#BB...",
        "B..B..BSB..B....",
        "B#BB#..#.B..B...",
        ".#B..BB#..#BB...",
        "................"
    )

    // [ID 565] GraceNovamon (galaxy super ultimate sovereign)
    val GRACENOVAMON_ROM = arrayOf(
        "...B..T.........",
        "...B..T.........",
        "..TBTBTB........",
        ".BT.TBTBTBT.....",
        "..B##B..B#......",
        "..BSB..B##......",
        "..B#BB#SB.BB....",
        "..B...#BB...#...",
        "...#BB#.B#BB....",
        "..BSBB.B##B.#...",
        ".#.B.B.#B...B#..",
        ".#.B#SB.B..B##..",
        "..B.#B.#.B#B#SB.",
        "..B..B.#..#.BSB.",
        "...B.B..B.#..#B.",
        "....#B...B#...B."
    )

    // [ID 566] Lucemon Satan Mode (apocalyptic dragon carrying the Gehenna orb)
    val LUCEMON_SATAN_MODE_ROM = arrayOf(
        "....TBTBT.TB....",
        "...BTB..T.T.TB..",
        "..T..B.B..TB..T.",
        ".BTBT..BTBT.T..B",
        ".B....#..#...B.#",
        ".BB#BB.B..B.#B.#",
        "..B....B.#BB#B.#",
        "...#BB#....B#B.#",
        "....B...B#.B#.B#",
        "..B#B.#BB..B.B..",
        ".#.B#B....#..#..",
        "BSBS#.B..B....B.",
        "BS#S#B..B.#BB#.B",
        "BS#S#SB#BB#...BB",
        ".##S#B.#.B.B..BB",
        "..BB#BB#BB#BB#B."
    )

    // [ID 567] Chaosmon (unstable fusion with duel avatar arms)
    val CHAOSMON_ROM = arrayOf(
        "....BB..........",
        "....B#BBB.......",
        "....B.TTTBBBBB..",
        "...B.T.BBB.#B...",
        "...B..BCBBBB....",
        "...BBBBBBTBBBB..",
        ".BBB.B..TBBCCB..",
        ".BC#BBBBBBCRRB..",
        "..BBBSB.TB#BB...",
        ".BSB.B..TSB##B..",
        ".BTB.B.TSSB##B..",
        ".BTBBBSSSBTT#B..",
        "..BB#BSS#BTTB...",
        "...BC#BBS#BBB...",
        "..B.BCB.BCB.B...",
        "..BBBBB.BBBBB..."
    )

    // [ID 568] BanchoLeomon (Gaku-Ran jacket king of beasts)
    val BANCHOLEOMON_ROM = arrayOf(
        ".....BBBBBBB....",
        ".BB.BRRTBBTTB...",
        "BTB.BBBRRTBTTB..",
        "BTBB##SBBBTBTBB.",
        "BTB##SBBTTBTBB..",
        "BTBBBBTTTBTTBTBB",
        "BTBBTTTBCTT#BTB.",
        "B#BBTTTTT##BTTTB",
        "B#B.BBBBBBBSBTB.",
        "BBBBSBTRTT#BSB..",
        "BTBSTB.....BSSB.",
        "BR.TBBBBBBBTBSB.",
        ".BBBBSSSSSBRT#B.",
        "..BBBBBSBSBBBB..",
        "..BSBTTBBBTBSSB.",
        "..BBBBBB.BBBBBB."
    )

    // [ID 569] Darkdramon (D-Brigade cybernetic assault dragon)
    val DARKDRAMON_ROM = arrayOf(
        ".....BB.BBBB....",
        "....B#BBT##B....",
        "...BTBT###B.....",
        "..BTBT#TT#B.BBB.",
        ".BT###TT#SBBBB..",
        ".B#####SSBBTB...",
        ".BBBBBBSBBT#B...",
        "..BT###BBT#SB...",
        "...BBBBBB#SSBB..",
        "...BSBSBTBBTSB..",
        "..BTBBBB#BSSTB..",
        ".BTSBBTTSBSSB...",
        ".BBBBBBBBBBBB...",
        ".BBTSBBBBBTSSB..",
        "BTBSBBBBBTBTBTB.",
        "BBBBBB..BBBBBBB."
    )

    // [ID 570] MarineAngemon (holy ocean fairy with golden heart halo)
    val MARINEANGEMON_ROM = arrayOf(
        "................",
        ".....BB.BB......",
        "....B..B..B.....",
        "....B.WW.WB.....",
        "...B.BWTBTTB....",
        "...B.GWTGTTB....",
        "...B.WWTTTTWB...",
        ".BB.WWBTTTTWWBB.",
        "B..WWTTTTTTTWWWB",
        ".BWWRRTRRTBTTTB.",
        "..BBR.RRRTTBBB..",
        "...BRRWRRTTB....",
        "...BTRRRTTTB....",
        "....BTRTTTTTB...",
        ".....BTTTTTTB...",
        "......BBBBBB...."
    )

    // [ID 571] TigerVespamon (royal hornet cyborg assassin)
    val TIGERVESPAMON_ROM = arrayOf(
        "........TBTB....",
        "......TBTBT.....",
        "....TB.BTB......",
        "...B..TB.BT.....",
        "..B..B#.B.B...B#",
        "..B.B...B.B.#B##",
        "...#BB#B...B#BB.",
        "...#..#..#B.#...",
        "....BB#BB#...B..",
        "...#.B.B..BB.B..",
        "...B#.B.B.#.B...",
        "..B.#BB#BB..B...",
        "..B.#....B..B...",
        "...B..B#.B#B....",
        "..B.#.B.B.#.B...",
        "..BB#BB.BB#BB..."
    )

    // [ID 572] Kuzuhamon (dark sacred miko variant)
    val KUZUHAMON_ROM = arrayOf(
        "....BB.BB..BBB..",
        "....BSBBSBB..TB.",
        "....BBBB#SBBBBB.",
        "....B####SB#B#B.",
        "..BB##BBBSSB#B..",
        "..BSSSSSSSBBBB..",
        "...BBBBBBBSB#BB.",
        "...BB#SSBSSBTBSB",
        "..BBSBBBBSBB#BSB",
        "..BBBSSSB#BSSBSB",
        ".BTSB#BB#BSSSBSB",
        ".BSSBBT#BBTS#BBB",
        ".BSSTBSSBTBBTB..",
        "..BBSSBBSSBB.B..",
        "..BTSSBBSSTBTB..",
        "..BBBB..BBBBBB.."
    )

    // [ID 573] BlackSeraphimon (fallen winged seraph in obsidian armor)
    val BLACKSERAPHIMON_ROM = arrayOf(
        "....T..B.BT.....",
        "TBTB.B..TB..TBT.",
        ".BTB....TB.BT...",
        "..TBTB..TBTBT...",
        ".B.#.B..B#......",
        "#BB....BB.B.#BB.",
        "#.B#B...B#.B#BB.",
        "..B##B...#BS#.B.",
        "#BB.BB#BBS#B....",
        "#BB.B..B.##B.BB.",
        "B.BB.B.#..#B.#B.",
        "B.B..B.#..#BB.B.",
        ".#B.#BB#.B..B...",
        ".#..#.B#.B..B...",
        ".#....B#B.......",
        "................"
    )

    // [ID 574] Ophanimon Falldown Mode (corrupted holy flame archangel)
    val OPHANIMON_FALLDOWN_MODE_ROM = arrayOf(
        ".......BTBTB.BTB",
        "......T.....T..B",
        "......TB...B..T.",
        ".....B...BTB.B..",
        ".....B#BB..B#.B.",
        ".BB#.B...#.B..B.",
        "#S##.B..B#.B...#",
        "#SB..B.....B.BB.",
        "#SB#..#..#BB#S##",
        "#B.#B.#BB...#S##",
        "B#..#B...B..B#BB",
        "BSB..B..BB#BB..B",
        ".#B...B#B.#..#..",
        "..BB..B.B.#...B.",
        ".....B..BB.BB..B",
        ".....BB#....B#BB"
    )

    // [ID 575] Ravemon (shadow blade winged raven ninja)
    val RAVEMON_ROM = arrayOf(
        ".....BTBTBTBT...",
        "....T.TB...B....",
        "...B.B...BT.....",
        "...B..TB..T..BT.",
        "..B..B.B..B.#.B#",
        "..B..B#..#BB..B#",
        ".BB....BB#...B##",
        ".B.#.B#.B..B..B#",
        ".B..B..B##....B#",
        "..B##B#B#SBB#B##",
        "..B.#...BB..B.BB",
        "..B.#BB#.B..B..B",
        "...B#...B.#B...B",
        "...B#B.##B#B....",
        "..B.#.B.B.#.B...",
        "..BB#BB.BB#BB..."
    )

    // [ID 576] ShineGreymon (solar blazing knight dragon)
    val SHINEGREYMON_ROM = arrayOf(
        "TT.....TT..TT...",
        "B.TTRTT.B.S.S...",
        ".B#..#..BB..SSS.",
        ".B..####.B.S..SS",
        ".B.#.C...BB..S.S",
        ".B###B.RR..SS..S",
        "B.R...RR..SS..S.",
        "BBBBBBB.SSS..S..",
        "..B.....S..SS...",
        ".BB.BB.SS..S....",
        "W..B..B..S.S..SS",
        "W..BBBB..BB.SS.S",
        "BBBBBBBBB.SS..S.",
        "..BB.BBB...SSS..",
        ".B.B..B.B.B.B...",
        ".BBBBBBBBBBBB..."
    )

    // [ID 577] MirageGaogamon (howling moonlight beast knight)
    val MIRAGEGAOGAMON_ROM = arrayOf(
        ".......BT.TB....",
        ".....BTBTBT.T...",
        ".....B....T.T...",
        "...BT...TB.B....",
        "..B....BB..B#B..",
        "..B#BB#...BB..B.",
        "....B..BB#.B.B.#",
        "...##B#..#BB#..#",
        "..B.B....#..#.B.",
        "..B..B..B..B.BB#",
        ".#.B#SB#BB#B...B",
        ".#..#B....#B...B",
        "B..B..B..B..B..B",
        "B#BB#B##BB#BB..B",
        "B.B...B.B..B.#B.",
        ".#BB#BB.BB#BB#.."
    )

    // [ID 578] Jesmon GX (transcendental apex Royal Knight)
    val JESMON_GX_ROM = arrayOf(
        ".......BT.TBTB..",
        "..TB..TBTB..T...",
        ".B.B.BTB...BTB..",
        ".B.BTB.......B..",
        ".B.#...BB..B#...",
        ".BB...#B...B....",
        ".B........BB.BB.",
        ".BB#BB...#WB#.B.",
        "..B.....BSB..B..",
        "...#BB#B##....B.",
        "...B.B..BS#BB#..",
        "..B.#..##B..B#..",
        ".#..#BBSB..BB.B.",
        "B..B#.B#..#SB..B",
        "B#B.#.B.BB.B.#.B",
        ".....BB...#B..BB"
    )

    // [ID 579] Gallantmon X (awakened X-Antibody holy knight)
    val GALLANTMON_X_ROM = arrayOf(
        "....TBTBT.TBTB..",
        "...B.BTB.B..T...",
        "..TBTB.....B....",
        "..T.T..BTBT.....",
        "..B...#.B.BB#...",
        "..B#BB#BB.B..B..",
        "...#....B#..#B..",
        "...#BB#B.#.B..B.",
        "..B#....BSB..BB.",
        ".B.#B..B.#B.#.B.",
        ".#..#BB#BS#.B.BB",
        "B..B#...BB#.B.BB",
        "B.BB.B.#..#..##B",
        "B#B..BB#...B..BB",
        ".#.B..B.B.#.B#.B",
        ".#BB#B...B#BB..."
    )

    // [ID 580] WarGreymon X (sharpened X-Antibody dragon warrior)
    val WARGREYMON_X_ROM = arrayOf(
        ".TT.........T...",
        "B.T..TTTTTTT.T..",
        "T..BB....T...T..",
        "T.T..###.TTTTT..",
        ".B..#SS.....T...",
        ".B.#......TTTTT.",
        ".B###B.TTTT...T.",
        "..B..B.TTT...T..",
        "...B###B..TTTTT.",
        "..BB......TT..T.",
        ".B.B.BB..TT...T.",
        ".B.BB..TTT.T.T..",
        "..BBBBBBBBB.T...",
        "..BB.B.B..TTTT..",
        ".B.B.BBBB.T.T.T.",
        ".BBBBB..BBBBBBB."
    )

    // [ID 581] MetalGarurumon X (quadrupedal heavy gatling beast)
    val METALGARURUMON_X_ROM = arrayOf(
        ".........BTB....",
        "......TBT..B.BT.",
        ".....B....TBT.TB",
        ".BTBT..B.B..TBTB",
        ".BB...#B..BB#.B#",
        ".B.......#..#B##",
        ".BB#BB..BWBB#SB.",
        "..B....BB#..#B..",
        "...#BB#B.#.B.B..",
        "....B.#..#B...B.",
        "...B.BB#BB.B..B.",
        "..B..B..B.#.B#..",
        "..B.#BB#BB.BB...",
        "...B..B#..#B....",
        "..B..BB.B...B...",
        "..BB#B..BB#BB..."
    )

    // [ID 582] Beelzemon X (awakened X-Antibody demon lord of gluttony)
    val BEELZEMON_X_ROM = arrayOf(
        ".....BTBTBT.....",
        "...BT.....T.....",
        "..T.T....B.B.BT.",
        "..TB..TB..T.TBTB",
        ".B...B#..#.B#S##",
        ".B......B#BS#S##",
        ".BB#BB#BB.BB#B##",
        "..B...#B..B...B#",
        ".B.#BB#..##B...#",
        "..BSB.#BBSBS#..#",
        "..BS#.B##B.B##..",
        ".#.B#.B.B..BBSB.",
        ".#.B#B##B..B.#B.",
        "..BB#.BSBB#...B.",
        ".#..#..#..#.....",
        ".#BB....BB#....."
    )

    // =========================================================================
    // Wave 3: Ghost Game, Seekers, Vital Bracelet & Apex Burst Forms (133..592)
    // =========================================================================

    // [ID 133] Poromon (avian plume chick with flutter wings)
    val POROMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        ".........#B.....",
        "........B.B.....",
        "......#BB#B.....",
        "..B#.B.....B.BB.",
        "..B.B.#..#..#.B.",
        "..B.B.#..#..#.B.",
        "...B#.......B#..",
        "....#..#....B...",
        ".....BB.B..B....",
        "......B#BB#.....",
        "................",
        "................"
    )

    // [ID 134] Upamon (amphibian axolotl with horizontal ear frills)
    val UPAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "..B#.........BB.",
        "..B.B.#BB#..#.B.",
        "..B..B....BB.BB.",
        "..B#..........B.",
        "..B#.B...#..#BB.",
        "...#...B....#...",
        "...B..B.B..B....",
        "....#.....#.....",
        ".....BB#BB......",
        "................",
        "................",
        ".....BB#BB#....."
    )

    // [ID 251] Pulsemon (electric shockwave fur and lightning crest)
    val PULSEMON_ROM = arrayOf(
        ".B.....BTBTBTBT.",
        ".BT..BT......B..",
        ".BT.T.......T...",
        ".BTB........TBT.",
        "...#...........#",
        "..B....BB.....B.",
        "..B...#SB....B..",
        "..B....BB.....B.",
        "..B...#B.....B..",
        "...#........#B..",
        "....#BB#B.....B.",
        "...B.B.....B...B",
        "...B#.......B#BB",
        "....#..#BB...#..",
        "...B#BB#.B#BBSB.",
        "..BB#BB#.B#BB#BB"
    )

    // [ID 252] Gammamon (white dragon with triceratops horns and blue bandanna)
    val GAMMAMON_ROM = arrayOf(
        ".BB..BB..BBB....",
        ".BSB.BSBBSSB....",
        ".BT.BBTTWT#B....",
        "..BTTWWWTTBBB...",
        "..B.WSTWWWT#SB..",
        ".BBTT.BTWWTTB...",
        ".B.WWSSSWWTB....",
        ".BWWWWWWWT#SB...",
        "..BWBBBTTTBBSSSS",
        "...BTTTTSSSSSSS.",
        "...BSSSS#BWTB...",
        "..BWB###BWRSBBB.",
        "..B#BWWWB#WBWSB.",
        "...BWTWWTBBBTB..",
        "..B#TTBBTTW#B...",
        "..BBBBB.BBBBB..."
    )

    // [ID 253] Angoramon (long-eared fluffy beast with gentle paws)
    val ANGORAMON_ROM = arrayOf(
        ".......BBBB.....",
        "...BBBBBBSSB....",
        "..BTBWTTTBBTB...",
        ".BTBWTTTTTTBTB..",
        ".BTBWTTTTTTBTTB.",
        "BTBWTSSWTSTTBTB.",
        "BTBSSSSSSSTTBTTB",
        "B#BT#TTTSTTTBB#B",
        "B#BTT##STTTSWB#B",
        ".BTSTTTTTTSBTWBB",
        "BTBTSTTTSSTBTWBB",
        "BTBT.SSS.TBTTTB.",
        "BTBT.....TBTTTBB",
        ".BBBT....TTBBBTB",
        ".B##TSBBBTT##BB.",
        ".BBBBB...BBBBB.."
    )

    // [ID 254] Jellymon (translucent aquatic jelly spirit with curled tentacles)
    val JELLYMON_ROM = arrayOf(
        "......BBB.......",
        "....BBTTTBB.....",
        "BBBBTTTTTC#BBB..",
        "BTBTT#TTTTC#TB..",
        ".BBTTTTTTTC#B...",
        "..BTCTTCTTC#B...",
        "..BCBCCTBC##TB..",
        "..BW.BWW.B#BTB..",
        ".B#BBBWWBB#BBCB.",
        ".B#BWWWWT#BBCBTB",
        "BTB#BBBBBBBCBTB.",
        "BBTBWTSBBTWBTBB.",
        "..BBWBBBBBWBB...",
        "....BB#B#BB.....",
        "...BT#BBB#TB....",
        "...BBB...BBB...."
    )

    // [ID 255] Loogamon (demonic wolf pup with forehead magic digicore interface)
    val LOOGAMON_ROM = arrayOf(
        "..B...B.........",
        "..BB..BB........",
        "..BB..BSB...B...",
        "..BSBBBSB...BB..",
        "..B###SSB...BWB.",
        "BBS#SBRSBB..BWTB",
        "BSWWSSSTBB..BSTB",
        "BWWWWSSBTBB.BSSB",
        "BBBBWWWTBSSBBSSB",
        "BWWWWTTBSSSSBSB.",
        ".BBBBBBBSBSBBB..",
        "...BBBBSBBB#SB..",
        "....BSSBBBB#SSB.",
        "...BSBBSSBBWTTB.",
        "..BS#BS#SB.BSSB.",
        "..BBBBBBBB.BBBB."
    )

    // [ID 256] Shoutmon (red dragon rocker with V-crest and mic)
    val SHOUTMON_ROM = arrayOf(
        ".......BT.TBTBTB",
        "......T.TB....T.",
        ".....BTB....TB..",
        ".B..TB.......B..",
        ".BB#...BB.BB#.B.",
        ".B....#BB.B.#B..",
        ".B........BB#...",
        "..B.BB#B.....B..",
        "...#.......B#...",
        "....BB#BB#B.#...",
        "....#B...B#.B...",
        "...B.B..B...B#B.",
        "...B.B..B..B..B.",
        "...B#.B##B#BB#..",
        "..B.#.B.B.#.B...",
        "..BB#BB.BB#BB..."
    )

    // [ID 257] Gumdramon (rubber dragon with hammer tail)
    val GUMDRAMON_ROM = arrayOf(
        ".....BB...BBBB..",
        "....B#B.BBT##B..",
        "...B#BBBTT##B...",
        "...BBBTTT##B....",
        "..BT.TTT##B.....",
        ".BT.TSC##BB.....",
        ".BT##.BCT##B....",
        "B....##S#BB.....",
        "B.BB..T###B.BBB.",
        ".B...T##BBTBTTTB",
        "..BBBBBBB#BB###B",
        ".B#BTRTB.#B.BSB.",
        ".B.BRRRB..BBT#B.",
        "..BTTT##BBB##B..",
        ".BSBTBB#TBSBB...",
        ".BBBB..BBBBB...."
    )

    // [ID 258] Agunimon (flame warrior with flowing flame mane and martial stance)
    val AGUNIMON_ROM = arrayOf(
        ".BT....BTBT.....",
        ".B.B.BT..B.BTB..",
        ".B..TBTBTB.B..T.",
        "..T.TBT.T..BTBT.",
        "...##B..B#B..B..",
        "...#BB#B##..#...",
        "...#....B#.B#B..",
        "....BB..BWBB.B..",
        "....B..B##..#B..",
        ".....B#BB.B..B..",
        "....#SB.BB#BB#..",
        "...B.BB.BB..B...",
        "...B.B...B..B...",
        "....#BB#BS#BB...",
        "...B..B.BB...#..",
        "...B#BB..B#BB#.."
    )

    // [ID 259] Lobomon (wolf knight with wolf helm and dual Licht blades)
    val LOBOMON_ROM = arrayOf(
        ".......BT..BT...",
        ".....BTBTBT.T...",
        "....T.......T...",
        "..TBT...TB.B....",
        "..B#...BB...#B..",
        "..B.........#BB.",
        "..B#BB#BB..B#..#",
        "...#.....#B..B.#",
        "..B.B...B#...B.#",
        "..B##B#BBSBB#..#",
        ".#B.#.B#.B#B.#.B",
        ".#..#....B...#..",
        ".#..#BB#BB...#..",
        "..BB#.B#B.#BB...",
        "..B.#.B.B.#.B...",
        "..BB#BB.BB#BB..."
    )

    // [ID 260] Gazimon (dark beast with long notched ears and claws)
    val GAZIMON_ROM = arrayOf(
        "................",
        "................",
        "....TBTBTB......",
        "..TB......TB....",
        ".B....#B....#...",
        "#....B.BB....B..",
        "#.....#BB....B..",
        ".BB#B.....B..B..",
        ".B.........B#...",
        "..B#BB..B#..#...",
        ".#..#..#.....#..",
        ".#BB#..#BB#..#BB",
        "....#........#.B",
        "..BB#B...B..B.B.",
        ".#.B.BB#B.#..#..",
        ".#BB#BB.BB#BB#.."
    )

    // [ID 373] GeoGreymon (striped horn skull helmet and toothy jaw)
    val GEOGREYMON_ROM = arrayOf(
        "W....TTTRT..TTT.",
        "TW..TTRTTTRTTTR.",
        "TTRTEWT...TRTT..",
        "TRWWWB.W.TTTT...",
        "B#R#####R#.B....",
        ".B#B.B##...B.BB.",
        ".B..........B.B.",
        "..B#R#B....B.B..",
        "....B.....B.BB..",
        "..B#....B#...B..",
        ".B.B...B...B.BBB",
        ".B.B...B...B.B.B",
        "..BB....B#B..B.B",
        ".BB.B.....B...B.",
        "B.B..B###B.B.B.B",
        "BBBBBBB...BBBBBB"
    )

    // [ID 374] Bulkmon (heavy lightning gorilla brawler)
    val BULKMON_ROM = arrayOf(
        "T......BTB.BTBT.",
        "TB...BTBTBT...T.",
        "TBT.TBTBTB..TB..",
        ".BTBTB..T..B....",
        "..BSB..BB#.B#B..",
        ".B#S#B#B....#...",
        ".B##B.#....B....",
        "..B.........#B..",
        "...#BB#.......B.",
        ".....B....BB#..#",
        "...B#B...B.B##.B",
        "..B.#B...B..B#.B",
        "..B.#B....#BB#BB",
        "..BB#.B.......B.",
        ".#.B..B#BB.B.#.B",
        ".#BB#BB..B#BB#BB"
    )

    // [ID 375] BetelGammamon (bipedal fire dragon with flaming horns)
    val BETELGAMMAMON_ROM = arrayOf(
        ".BBB.BB..BBBBBB.",
        ".BSRBBSBB##TTB..",
        "..B#RBRS###RB...",
        "..BRRRT##RRBBBB.",
        "...BRT#BBB##TB..",
        "...BRBBTB#RBB...",
        "..B.TTBBTS#TB...",
        "..B.....TTBB....",
        "...BBBBBBBSSB...",
        "...BCSSS##RB#B..",
        "....B###.RB#B#B.",
        "...BRB..RBRTBBBB",
        "..B#B#TTRBSRBRSB",
        "...B.RBBR.BBSBB.",
        "..BS#RB.BR#SB...",
        "..BBBB...BBBB..."
    )

    // [ID 376] KausGammamon (aerodynamic blue glider wings)
    val KAUSGAMMAMON_ROM = arrayOf(
        ".......B..BBBB..",
        "BBB...BSBB#TTB..",
        "BSSB.BB#S#SSB...",
        ".B##B#SS#SSB....",
        "..BS##TB###TB...",
        ".B###BT##STB....",
        ".B.....TSBBBBBBB",
        "..BBBTTTB#SBBB..",
        ".....BBBSBB.....",
        "....B#SSBSTB....",
        "...B#BBB#BS#B.BB",
        "..B.TB.#SBT.BBSB",
        "..BSB#TS.#BSB#SB",
        "...BB.SBS.#BSSB.",
        "..BS#SB.BSSSBB..",
        "..BBBB...BBBB..."
    )

    // [ID 377] WezenGammamon (heavy green artillery dragon with triple rail cannons)
    val WEZENGAMMAMON_ROM = arrayOf(
        "................",
        "...BB...........",
        "...BSB..BBB.....",
        "....B#BB#TTB....",
        "BB.B#B####B.....",
        "BSBT####SBB...BB",
        "BS###BBB##TB.B#B",
        "B###BTB##SBSBBSB",
        "BTTT..##SB##SB#B",
        "B.BBBB.##TB##SB.",
        ".B....TSBB####B.",
        "..BBBBBB#####.SB",
        "...B#..##.#B##.B",
        "...B#BB###BBB#.B",
        "...B.TBB.TB.B.TB",
        "...BBB..BBB..BBB"
    )

    // [ID 378] GulusGammamon (dark dragon with black flames and dark crest)
    val GULUSGAMMAMON_ROM = arrayOf(
        "BB..BB..BBBBBB..",
        "BTB.B#BBSSSSB...",
        "BT#BBSBSSBBB....",
        ".BTSSBSSSSS#B...",
        "..B#SSBBSBBB....",
        "..B##B.SSBB.....",
        ".BTTTS#SBS#B..S.",
        "..BTTT#BBBB...S.",
        "...BBBB#SSB..S#.",
        "...B##SBBSSB..B.",
        "..B#BBBSSSSSB.B.",
        "..BSSBRB##SSSBB.",
        "..BSB#SBBB#SBSB.",
        "..BBSSBB#BBBSB..",
        ".BTSSB.BBTSTB...",
        ".BBBBB..BBBBB..."
    )

    // [ID 379] SymbareAngoramon (martial hare with slender ear scarf)
    val SYMBAREANGORAMON_ROM = arrayOf(
        ".....BBB........",
        "..BBBWTTBB......",
        ".BWWTTTTTTB.....",
        ".BWTBTSTTBTB....",
        ".BTB##TBTBTTB...",
        "..B##BS#TBBTB...",
        "..B#####B.BTTB..",
        "...BBBBBTBB#TB..",
        "..BBWWWTTTBB#TB.",
        ".BTBTWTTBTTB#TBB",
        "BTB.B##B.B#TBT#B",
        "B#B.BS##BB#TB#B.",
        "BB.B#SS#B.BB#B..",
        "...BSSBSSB..B...",
        "...BSB.BSB......",
        "..BBB...BBB....."
    )

    // [ID 380] TeslaJellymon (floating electric shock tentacles)
    val TESLAJELLYMON_ROM = arrayOf(
        "BBBB.....BBBB...",
        "B#TTBBBBBTT#B...",
        ".BTB#T#TTSTB....",
        "..B#T#TT##BB....",
        "..BSTSTTST#B....",
        "..BSTSTTST#B....",
        "..BTTTTTT#BBB...",
        ".BTBBBBBBB#BTBB.",
        "BGBT#WWWT##TBTGB",
        ".BTTB#TT#TB#TBB.",
        "BTTBTBTTSBTBTGB.",
        ".BBTTBSSSBTTBB..",
        "..B#BTTTTTB#B...",
        "...BTTSSSTTBB...",
        "....BSSBSSB.....",
        "...BBBB.BBBB...."
    )

    // [ID 381] Loogarmon (demonic wolf with blazing blue flames)
    val LOOGARMON_ROM = arrayOf(
        "................",
        ".....B..........",
        ".....BB.......B.",
        ".....BSB......B.",
        "....BBSB....B.BB",
        "...B###SB...B.BB",
        ".BBT#RSSBB.BRBSB",
        "BTTTTSSSBRBBRBSB",
        "BTT##SSSB#BRSRBB",
        ".BBBBBBBSRBRSRB.",
        "..B#S#S##BSSSSB.",
        "...B#B#BRBSSSSB.",
        "...BBBRBBBTTTB..",
        "..BSBSBSSBSSSB..",
        ".BRSSBSSBRSRSRB.",
        ".BBBBBBBBBBBBBB."
    )

    // [ID 382] Pegasusmon (winged celestial steed with golden helm)
    val PEGASUSMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "....B.B.........",
        "...B#TBBTTTTT...",
        "....BTTTTTTBT...",
        "...T#T#BTTB.....",
        "...TBT#BTB......",
        "..TTTTTT#.......",
        "..#TBBTTTTTT.B..",
        ".....##TTTT.T...",
        ".....SBTBS#BBB..",
        "....TBBT.T.T....",
        "....T.TT.T.T....",
        "................"
    )

    // [ID 383] Nefertimon (sacred golden sphinx with royal headdress)
    val NEFERTIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "...B..B..B..BB..",
        "...TBTTTTTT.SS..",
        "...BBTBBT.TT....",
        "...TSTTTTTTTT...",
        "...W.TTBBTTBTB..",
        "...BTTBTW..TB.B.",
        "...TBT#BTBB..BB.",
        "....BBTTT.B.#...",
        "....B..RB#.WB...",
        "...TT..WTTTT....",
        "..BBR.BSTB.RB...",
        "................"
    )

    // [ID 384] Strikedramon (commando dragon with red hair crest)
    val STRIKEDRAMON_ROM = arrayOf(
        "........TBTBT...",
        "......TB..T.....",
        ".....B..TB......",
        "...BT.....T.....",
        "..B...#BB#B.....",
        "..B#BB..B#.B....",
        "..B.....B.BB....",
        "...#BB#BB#..#...",
        "....B....#..#...",
        "....BB.B..BB#...",
        "...B.B..BB#.B...",
        "..B..BB#B...B...",
        "..B..B#SB...B...",
        "...B#SBSBB#B....",
        "..B.#B.#.B.B....",
        "..BB#B.#BB#B...."
    )

    // [ID 385] Dobermon (hunter demon dog with spiked collar)
    val DOBERMON_ROM = arrayOf(
        "................",
        "................",
        ".....BT..BTBT...",
        "....TB..TBT.....",
        "..B##S#B##......",
        ".B#S#B..B#......",
        "#S#SB..B##......",
        "#S#S#B#SB.B....#",
        ".BB##S#B..B...B#",
        "....BB...##B#B.#",
        "...B...#BS#SBSB.",
        "....#BBSBS#B.#B.",
        "....#B.#.B#B.#B.",
        "...B#B.#BS#.BSB.",
        "..B.#.B.BB.B.#B.",
        "..BB#.B#B..BB#B."
    )

    // [ID 386] Sangloupmon (vampire wolf with bloody blade fangs)
    val SANGLOUPMON_ROM = arrayOf(
        "...BT..BT.......",
        "...B.B.B.B....TB",
        "....TBTB.B...B.B",
        "...B.....BT..B.B",
        "..B#.B#BBSB.#..#",
        "#B....#B#SB.#..#",
        "#.......B#WB#B.#",
        "#BB#..#...BB..B.",
        "#....B...##B..B.",
        ".BB#B...B#B..B##",
        "....#...B....##B",
        "....#.B.......BB",
        "....#B##.B..B..B",
        "...B#.B.BS#BB#.B",
        "..B..B...B.B...B",
        "..BB#BB#BB.BB#BB"
    )

    // [ID 387] DarkKnightmon (ebony chrome armor with twin cross spear horns)
    val DARKKNIGHTMON_ROM = arrayOf(
        "..TB...BTB......",
        "..T.TBTB.B......",
        "..T.T.T..B......",
        "...B..TBTBT.....",
        "...#BB#..#B.....",
        "...##B..BSB.....",
        "...#BB#B#WB.#...",
        "...#...B##.B#.B.",
        "....BB#SBSBS#BB.",
        "...#...B.#BS#SB.",
        "..B.#.B.BB.BBSB.",
        ".#.B...#B..B.#..",
        ".#.B#.BSB..B....",
        "..BB#BB##B#.....",
        "..B..B..B..B....",
        "..BB#B..BB#B...."
    )

    // [ID 465] RizeGreymon (cyborg dragon with chrome revolver arm)
    val RIZEGREYMON_ROM = arrayOf(
        "................",
        "..W..TTTRT..TTT.",
        ".TW.TTRTTTRTTT..",
        ".TRTEWW..TTTT...",
        ".TRWWB..TTTTT...",
        "B#R#####R#.B.BB.",
        "T###.T##...BB.B.",
        ".T........BB.B..",
        "..B###B..B...SSS",
        "....B....B.B...S",
        "..BB....BB...BB.",
        ".B.B...B..TTWWWW",
        ".B.B...B..TTWW.T",
        "..B.B...BB...TTT",
        ".B.B.BBBB.B.B.B.",
        ".BBBBBBB.BBBBBB."
    )

    // [ID 466] MachGaogamon (rocket turbine engine boxer wolf)
    val MACHGAOGAMON_ROM = arrayOf(
        "....TBT..BTBTB..",
        "....T.TBTB...B..",
        "...BTB.....BT...",
        "..T....BT.TB....",
        ".BB#..#B....#...",
        ".B........BB.BB#",
        ".B.......#B..B.#",
        ".BB#BB#BB..B.B.#",
        "...#...B.#..#SB#",
        ".BB#..#..#B.#B.#",
        ".#.B.B..B..B#SBB",
        "..B.#SB#B..BB#.B",
        "...B#S#S#B#B.#BB",
        "..BS#S###S#SB...",
        ".#.B.BB.BB.B.#..",
        ".#BB#B...B#BB#.."
    )

    // [ID 467] Boutmon (electrified karate master with lightning belt)
    val BOUTMON_ROM = arrayOf(
        "...B.BTBTBT.....",
        "..T.T....B......",
        ".B........TB..T.",
        ".B.BTBTB..T..BT.",
        "..BS#B#SB#BB#B..",
        "...#B..B.#.B....",
        "#B.#..#B..BB....",
        "#.B#.....#B.....",
        "#..#BB#BB..B#B..",
        "#...B.#B......B.",
        ".#BB#.....#....B",
        ".....B....#BB..B",
        "..BB#SB#BB#S##BB",
        "..BS#BB#BB#S##..",
        ".##S#......B#SB.",
        "B#BB#......BB#BB"
    )

    // [ID 468] Canoweissmon (four-winged white celestial dragon)
    val CANOWEISSMON_ROM = arrayOf(
        "BB......B.BBBB..",
        "B#B..BBBRBWT#B..",
        "BWBBBWWWRSTTB...",
        "BWB#WTSWRTTB..B.",
        "BBWWSTWWWTT#B.BB",
        "BWWTTT#TTTBB.BRB",
        ".BBBSBBTBBB#BRSB",
        ".BRRBBBBB##BRRSB",
        "..BB.BSRSBTWBRSB",
        ".....BRTRRBBBBSB",
        "....BTBSSSBT#BBB",
        "...BWTBRRBWT#B.B",
        "...BSBWBRBSTBTB.",
        "....BWT#BBBBTBWB",
        "...BSSTB..BTSSBB",
        "...BBBB....BBBB."
    )

    // [ID 469] Regulusmon (dark dragon lord with shield lance)
    val REGULUSMON_ROM = arrayOf(
        "BB......B.BBBBB.",
        "B#B..BBB#BSS#B..",
        "B#SBB##B##BSB...",
        "B#B##TSSBTSBSB..",
        ".B#SSSS#SSSSB.B.",
        ".BSSST#BBBBB.B#B",
        "..BTT#BBSSBTB#SB",
        "..BBBBSSSBT##BSB",
        "....B#SSB#TSSBSB",
        "...BSBBBSBTS#BSB",
        "..B#B#S#B#TS#BBB",
        ".B#SBTTSSBTSSB.B",
        ".BTBSBSSSB#SSB.B",
        "..BSSSBBBSB#B...",
        ".BTBTBB.BBSBTB..",
        ".BBBBB...BBBBB.."
    )

    // [ID 470] Lamortemon (beast berserker with dual bone greatswords)
    val LAMORTEMON_ROM = arrayOf(
        "....BBB...BBBB..",
        "...B#B..BBTTTTB.",
        "...BSBBBTTTSSB..",
        "..BTTTTT#SSSB...",
        "..BT#SSSTTTB....",
        ".B##STSST#B...BB",
        ".B##SRSSTB#B.BTB",
        ".B####SST###BSTB",
        "..BSSSST###BSSTB",
        ".BTTTTT##S#BSTTB",
        "BTBTT##SS#BS###B",
        "B#BSSSST##B###B.",
        "B##BTTTT##BS#SBB",
        ".BBBB######BBBTB",
        ".BSBSSBBBBSSBSB.",
        ".BBBBBB..BBBBBB."
    )

    // [ID 471] Thetismon (sea goddess with toxic needle sleeves)
    val THETISMON_ROM = arrayOf(
        "BBB.BBBBBBB.BBB.",
        "B#TBTWTWTT#BT#B.",
        ".BTTWTTTTTT#TB..",
        "..BTTTTTTTT#B...",
        "..B#TTTTTTT#B...",
        "...B#S#SS##BTB..",
        "...BS#T##STBTB..",
        "....BS#SSSTBTTB.",
        "....B..TTBTBBT#B",
        "...BTBBBBTB#BTTB",
        "..BBTBT#BTBB#BTB",
        ".B#SB####BBSS#B.",
        ".BSBTT#TT##BS#B.",
        "..B.B.TBT.B.BB..",
        "...B##BBB##B....",
        "...BBB...BBB...."
    )

    // [ID 472] Soloogarmon (dual-blade demon knight)
    val SOLOOGARMON_ROM = arrayOf(
        ".....BB..BB.....",
        "....B#B..B#B....",
        "....BSBBBBSB....",
        ".....B#S#SBB....",
        "T.BBBS##BBSB.B.T",
        "T.BT#SSBRBS#BRTT",
        "TTBSSSSSBSSSRTTT",
        "TT#BBBBBTTTBT#TT",
        "T##TBTTTTTBTS##T",
        ".T##BSTTSSB#S#T.",
        "..BBBSTSS#SBBB..",
        ".B#SBS#S#SSB##B.",
        ".BSSBSTTSSSBSSB.",
        "..BB#SSSSSS#BB#B",
        "..BTT#SBBS#TTBBB",
        "..BBBBBB.BBBBB.."
    )

    // [ID 473] Baalmon (robed assassin with inscribed talismans)
    val BAALMON_ROM = arrayOf(
        "....TBTBTBT.....",
        "...BTBTBTBTBT...",
        "..TB.BTBTBTBTB..",
        "..TB.BT...TB.BT.",
        "..BSBB..B#.B..B.",
        "..B#B..BB..B....",
        "..B........B#...",
        "...#.....#BS#B..",
        "....BB#BB##S#SB.",
        "...##B#B..BB.B##",
        "..B.#....B.BB.BB",
        ".#..#BB#BS#.B.BB",
        ".#..#....B#.B.BB",
        "..BB#.B#B.#B.#.B",
        "...B..B.B..BB..B",
        "...B#BB.BB#B...."
    )

    // [ID 474] JewelBeemon (jewel beetle knight with rapier)
    val JEWELBEEMON_ROM = arrayOf(
        "..TB.BTBTBT.TB..",
        ".B..T......B..TB",
        ".B.B....TB.B...B",
        "...BT..BT...T...",
        "...#........#...",
        "...#.B#BB#.B.B..",
        "....B.....B.#...",
        "....B.#B..BB....",
        ".....B...#B.#...",
        ".....B#BB#B.#...",
        "....#B....#B....",
        "...B.BB#BB#.B...",
        "...B.B.#.B..B...",
        "....#.B#B.#B....",
        "...B..B.B..B....",
        "...B#BB..B#B...."
    )

    // [ID 475] Dinobeemon (dragon-wasp chimera)
    val DINOBEEMON_ROM = arrayOf(
        "................",
        "....TBTBT.......",
        "..TB....TBT.....",
        ".B.....B..T..BTB",
        "#..#B.#.B#..#..#",
        "#.BSB.#BB..B...#",
        "#.B#..#SB#B...B.",
        ".B...BWB.#WB.B.#",
        "..B#BB#..##B#..#",
        "....B.#.BSB.#BB#",
        "...B.BB##B..B#..",
        "..B..B..B...B.B.",
        "..B.#BB.B..BB..B",
        "...B#..#BB#SB#.B",
        "...B.B.#.B.B.#BB",
        "...B#BB...#BB#.."
    )

    // [ID 476] SkullMeramon (chained hellfire skull phantom)
    val SKULLMERAMON_ROM = arrayOf(
        "....T..B.B...B..",
        "....TBT.T.TB.BT.",
        "...B........T.T.",
        "...B.BT.TBTB..T.",
        "..B.B.#B##.B..B.",
        "..B.B..BB..B..B.",
        "..B.BB.BB.BB..B.",
        "..B.BB#BB#B..B.#",
        ".BB.B.....BB#.B#",
        "#..#.B#BB#..#..#",
        "B..B#....B..B..B",
        "B...#....B.....B",
        ".#BB.B....#BB#B.",
        "B#B...B#B....#B.",
        "B....B...B.....B",
        ".#BB#B...B#BB#BB"
    )

    // [ID 477] Cerberusmon (three-headed hellhound beast)
    val CERBERUSMON_ROM = arrayOf(
        "................",
        "..BBB...BBB.....",
        "...BSBBB#SB.....",
        "..BSS##SSB#B.BB.",
        "BBSS#TBSB#TB..BB",
        "BSSSBTSSBTBB..BB",
        "BBBBBBBBSBBSB.BB",
        "B#RTRTRB##SSBBSB",
        ".BRRSSSBST##SBB.",
        "..BBBBBBSSSTSBB.",
        "....BSSSBSSSSBSB",
        "....BSBSSBBBBSSB",
        "....BSBSSBBB#SSB",
        "..BBSB#SSB..BSSB",
        ".BTSBTBTB..BTBTB",
        ".BBBBBBBB..BBBBB"
    )

    // [ID 583] Siriusmon (radiant white dragon knight with arm blades)
    val SIRIUSMON_ROM = arrayOf(
        "S.......S.BBBB..",
        "SS..BBBS#B#TWB..",
        "S#SBC..SWTWTB...",
        ".S#.WWWWBTTTWB..",
        ".B.WWTBBWWTTBTB.",
        ".BWTTBTBWTTBTB..",
        "..BSSWRTTTTWB...",
        "..BWWTTTTBBB..B.",
        "...BBBBBBW#TBB#B",
        "..BT.#.WTBWRBS#B",
        ".B#BTSSTTBSB#BSB",
        ".BSBST#SSBBS#BSB",
        ".B#BWSSRWTBS#BSB",
        "..B#BWTB#T#BB.B.",
        ".B#WTBB.BTW#B...",
        ".BBBB....BBBB..."
    )

    // [ID 584] Diarbbitmon (noble white rabbit fencing knight)
    val DIARBBITMON_ROM = arrayOf(
        "......BBB.......",
        ".....BTBTB......",
        ".S...BTBTB......",
        ".S...BBBTB......",
        ".S..BTTT##B.....",
        ".S.BTTTBB#B.....",
        ".S.BTTBS#TTB....",
        ".S..BR##TTBBB...",
        ".S...BBBBB#TB...",
        ".BB.BB.#B#TTTB..",
        "BTTB#B#.TBBBTTB.",
        "BBB#B.SS.TBTBTB.",
        ".BBB.S##S.TBBTB.",
        "...B.##B#STBBTB.",
        "....STB.BTS..B..",
        "...SSS...SSS...."
    )

    // [ID 585] Amphimon (futuristic titanium diving goddess)
    val AMPHIMON_ROM = arrayOf(
        "...BBBBBBBB.....",
        "...BTTT#TS#BTT..",
        "..BTSSST##SB..T.",
        "..BTSSST##SB...T",
        "..BSTSTSS#SB...T",
        "..BSTSTSSSBBB..T",
        ".BTBSSSSSBB#TB.T",
        "BTSBT##SBB#TSTBT",
        "B#BT##SSSBSSSSBT",
        ".BSBSSSSSSBSSB.T",
        "BSBBBSSBBB.BBSBT",
        "B##BTTSTTTBB##B.",
        "BTBTTBBBTTTB#TB.",
        ".BBB#SBS#BBTBB..",
        ".BS#SB.BS#SB....",
        ".BBBB...BBBB...."
    )

    // [ID 586] Fenriloogamon (demonic wolf knight with hellfire wings)
    val FENRILOOGAMON_ROM = arrayOf(
        "...B..B.........",
        "..BCBBCB........",
        "..BCCBCCB.B.C.CC",
        "...BBBSSCBCB#C#C",
        "...B##.TSCWCB#S#",
        "BBBS#.RBTWBBSSS#",
        "B#..WBRWWTCBSSSB",
        "B.WW#WWWTCBSSSB.",
        "BBBBBBTTBBSSSB..",
        ".B.WWTTBSBSBBCB.",
        "..BBBBBSTSBTBCCB",
        "...B.TCWSBCWTBCB",
        "..B#B..#B#BWSSB.",
        "..B.#BBB.WSBWTB.",
        ".BSWTB.B#S#B#TB.",
        ".BBBB..BBBB.BBB."
    )

    // [ID 587] Kazuchimon (golden thunder god samurai warrior)
    val KAZUCHIMON_ROM = arrayOf(
        ".B..TB.BT.....T.",
        ".B.BT...TB....T.",
        "TB..TB.BT.....TB",
        "TB...BTB.BT...TB",
        "#B..B.#....B..B#",
        "#.B.B...B#.B.B.#",
        "#.B#...BB..B.B.#",
        "#..#B.....B.#..#",
        "#B.#BB#BB#BB#.B#",
        "#SB#.B...#..#B##",
        "B#..#.B.B..B..BB",
        ".#..#B###B#B..B.",
        ".#BB..B.B...B#B.",
        "..B.#B...B#B.#..",
        ".#.B..B#BB..B.B.",
        ".#BB#BB..B#BB#B."
    )

    // [ID 588] Arcturusmon (abyssal GRB dark dragon with plasma drill cannons)
    val ARCTURUSMON_ROM = arrayOf(
        "B........BBBBB..",
        "BB....BBBSSSB...",
        "BTB.BBSSSSB#TB..",
        ".B#BCS#BSS#SB...",
        "..BSSBSSSBBB#B..",
        "..BSSSSBBBB#B...",
        "...BBBBSSBBB....",
        "..BSSSBBBBB#B.B.",
        "...B#BBTBTSSBBSB",
        "..BBBBBBBBTSBSSB",
        ".BSB##S#BBBBSBSB",
        ".BTSBT#BBBSCTBSB",
        "BT#BTBBBBTB#TBBB",
        "B#BSSSBBSSSB#B.B",
        "BBTBBB..BBBTB..B",
        ".BBBB....BBBB..."
    )

    // [ID 589] ShineGreymon Burst Mode (blazing solar corona wings)
    val SHINEGREYMON_BURST_MODE_ROM = arrayOf(
        "..BB...BBB......",
        ".BGB.BBBRBB..SSS",
        ".BGWBRRBWBRBBSS.",
        ".BGRRBBRBRBRRBSS",
        ".BRRBCRRRBBBBSS.",
        "..BRRRRBBBRRRBSS",
        "..BRBBB.BRRWBRB.",
        ".BRBW..BRRWBWBSS",
        ".BWWBBBBWWB.BBB.",
        ".BBBBRCRBBWBRRB.",
        "BWRRBRRRRBBWRRB.",
        "B.WB.B.RBRB.WB..",
        ".BBBR.RBR.RBBB..",
        ".B.B.BBBRR.B.B..",
        "BRBRBWB.BWBRBRB.",
        "BBBBBBB.BBBBBBB."
    )

    // [ID 590] MirageGaogamon Burst Mode (full moon auroral wolf knight)
    val MIRAGEGAOGAMON_BURST_MODE_ROM = arrayOf(
        ".BB..BB.BBBBBBB.",
        ".BTB.B.B.BBT..B.",
        ".B.TBBBBBB.BTB..",
        "..B.TBSSSBTBT.BB",
        ".BBBBSSBBSSBBBSB",
        ".BRSSSBRSSBBSS#B",
        ".BSSSSSSSBBSS#B.",
        ".BB....TTBBSSBBB",
        ".BTBBBBSRRBTBS.B",
        "..BBBSSRRSSB.SSB",
        ".BSSBS.TTBBSTTBB",
        ".BSBSB..BTBTSBB.",
        "..BBSSSBSSSBB...",
        ".BBBTBBB.STBTB..",
        "B.B.BTB.BTB.B.B.",
        "BBBBBBB.BBBBBBB."
    )

    // [ID 591] Beelzemon Blast Mode (black seraph wings and Death Slinger blaster)
    val BEELZEMON_BLAST_MODE_ROM = arrayOf(
        "....BBBBBBBB....",
        "...BSSSSBBTB....",
        "..BTSSSBBTB.....",
        "..BSSBTSBBTB.B..",
        "..BSBTTBBTB.B#B.",
        "...BBBB#TBBB#S#B",
        "..BB..SBB.TBSSSB",
        "..B.BBB..TBBSSSB",
        ".BBT.B.TBBRRBSSB",
        "BTBBBSBBBSBBSBSB",
        "B#BSBTBSSB.TSBSB",
        ".BBBSSSBBBT#BBSB",
        "..BTSBB#TSBB.BSB",
        "..BBBB.BBBBB.BSB",
        ".B.T#B..B#T.B.B.",
        ".BBBBB..BBBBB..."
    )

    // [ID 592] Omnimon Merciful Mode (seraphic holy knight with celestial Habakiri)
    val OMNIMON_MERCIFUL_MODE_ROM = arrayOf(
        "BBB.BBB.BBB.....",
        "BWTBB..BT.B.BB..",
        ".BW.#..W#B.BTTB.",
        ".B##..BBWBBTBTB.",
        ".B...BBWTBBBTTTB",
        ".BW..WWTB#CBTTTB",
        ".BBBBWBBB##BBTTB",
        ".B..TBTTBBB.BTTB",
        "..BBBBBBBTB..BTB",
        "..BT.#.TBW.B.BTB",
        ".BTBTCTB...B..BB",
        "B.TBBBBB..B...BB",
        "BBBBTTTBBBB....B",
        ".BB.BBBB..BB....",
        "B.TB.B.BTBT.B...",
        "BBBBBB.BBBBBB..."
    )

    // [ID 1101] Cotsucomon (BABY_I)
    val COTSUCOMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "......BBB.......",
        "....BBT##BB.....",
        "...BTTTTTTTB....",
        "...B#######TB...",
        "..#TTTTTTTTTB#..",
        "...BWWWSSWSB....",
        "...BWWWWSWSB....",
        "...BWWWWWWWB....",
        "....BBBBBBB....."
    )

    // [ID 1102] Kakkinmon (BABY_II)
    val KAKKINMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "......BBBBBB....",
        "....BBWTWTWTBB..",
        "...BWTWTWTWTTTB.",
        "...BTTTTTTTTTTTB",
        "..BTT#TT##TTTTTB",
        "..BTTTTTTT#TTTB.",
        "...BT#####TTTB..",
        "...BTTTTTT#TTTB.",
        "...BTTTTSTSSTTB.",
        "...BWSWWSSSSWB..",
        "...BWWWWSWWWWB..",
        "....BWWBWWBWB...",
        "....BBB.BB.W...."
    )

    // [ID 1103] Sakuttomon (BABY_II)
    val SAKUTTOMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "....BB......BB..",
        "...BWB.....BTB..",
        "...BWB.....BTB..",
        "...BTB.....BTB..",
        "...BTTBBBBBTTB..",
        "...BTT#####TTB..",
        "...BT######TTB..",
        "..BTTTT##T#TTTB.",
        "..BTTTTTSTSTTTB.",
        "..BWWSWWWSSWWWB.",
        "...BWWWWWWWWWB..",
        "...BWWBBBBBWB...",
        "....BB.....W...."
    )

    // [ID 1104] Fufumon (BABY_I)
    val FUFUMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        ".....BB.........",
        ".....BWBBB......",
        ".....BWTWTB.....",
        "....BTTTT#TB....",
        "....BT##T##B....",
        "....BT#####B....",
        ".....BTT###B....",
        "......BBTSSB....",
        "........BSB.....",
        ".......BWWB.....",
        ".......BWB......",
        "......BWWB......",
        "......BBBB......"
    )

    // [ID 1105] Chibimon (BABY_II)
    val CHIBIMON_ROM = arrayOf(
        "................",
        "................",
        ".......BBB......",
        ".....BBTW#BBBBB.",
        "....BTWTWTWTTTB.",
        "...BWTWTWTWTTB..",
        "...BTTTTTTTTTB..",
        "...BTTT##TTTB...",
        "...BTTT##TTTB...",
        "...B##TT###TB...",
        "...BTSSSSSSB....",
        "....BTTTTTTTB...",
        "...BWWWWSWWWB...",
        "...BWWWWWWWWB...",
        "....BWWBBWWB....",
        "....BBB..BBB...."
    )

    // [ID 1106] Algomon Baby I (BABY_I)
    val ALGOMON_BABY_I_ROM = arrayOf(
        "................",
        "................",
        ".......T........",
        "......BTB.......",
        ".....BWTWB......",
        ".....BWT#B......",
        "....BTTTTTB.....",
        "...BTTTTTTTB....",
        "...BT#TT#T#B....",
        "...BTTTT#T#B....",
        "....BTSSTSB.....",
        ".....BTTTB......",
        "......BWB.......",
        ".......W........",
        ".....BBWBB......",
        ".....BBBBB......"
    )

    // [ID 1107] Algomon Baby II (BABY_II)
    val ALGOMON_BABY_II_ROM = arrayOf(
        "................",
        "................",
        "...BBB....BB....",
        "...BWTBBBBWB....",
        "...BWTWT##WB....",
        "...BWTWTW##B....",
        "...BTTTTTT#TB...",
        "..BTTTTT#TTTTBB.",
        ".BTTTTTT#T#TTTTB",
        ".BBTTT##T##TBBBB",
        "...BTTTTSSSB....",
        "...BTSTTSSTT#...",
        "...BWWWWWWBB....",
        "....BWWSWB......",
        "...WWWWWWWBB....",
        "....BBBBBBBB...."
    )

    // [ID 1108] Dokimon (BABY_I)
    val DOKIMON_ROM = arrayOf(
        "................",
        "................",
        ".......B........",
        ".......B........",
        "....B..B........",
        "....B.T.........",
        "......BT........",
        "....BBB.........",
        "...BSS.SSS..B...",
        "...B.WSW#B..B...",
        "...BB.W.BT..B...",
        "...B..B.B.TTB...",
        "....B######B....",
        "................",
        "................",
        "................"
    )

    // [ID 1109] Bibimon (BABY_II)
    val BIBIMON_ROM = arrayOf(
        "................",
        "................",
        "......B.........",
        "......B.........",
        "...B..B..BB.....",
        "...B.#B###BBB...",
        "...B.BS####S....",
        "...#BB#####S....",
        "..BTT#TTT###S...",
        "..B.TTTBB###B...",
        "..BB###BS##SB...",
        "..B##B#####SB...",
        "...S#####SSW....",
        "....WBBBWBBW....",
        "................",
        "................"
    )

    // [ID 1110] Wanyamon (BABY_II)
    val WANYAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "...BB.....BB....",
        "...BTB...BTB....",
        "...B#TB.B##B....",
        "...BT#TB##TB....",
        "...BSSSSSSSB....",
        "..BTTTSTSSTTBBB.",
        "..BWWWSWSSWWWWTB",
        "..BWSSSSSSWWWWTB",
        "...BWWWWWWWWWWB.",
        "....BBBBBBBBBB.."
    )

    // [ID 1111] Choromon (BABY_I)
    val CHOROMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "......BB.#......",
        ".....BT#B.......",
        ".....BTTB.......",
        "....BTTTTBB.....",
        "....BWWWWWB.....",
        "....BWWWWWWB....",
        "....BWWWWWWB....",
        "....BBBBBBBB...."
    )

    // [ID 1112] Caprimon Color (BABY_II)
    val CAPRIMON_COLOR_ROM = arrayOf(
        "................",
        "................",
        "................",
        "...........BBBB.",
        "..........B..##B",
        ".........B....#B",
        "BB.....BB##W..SB",
        "B.B...B.B#W##SWB",
        "BT.BBB.#BS.###B.",
        ".BT##.#BB..S##B.",
        "B.#..##TBSSWBB..",
        "B.####TT#BBB....",
        "B#TBTTT##B......",
        "BSBSBBSW#B......",
        ".BS..SWWB.......",
        "..BBBBBB........"
    )

    // [ID 1113] Bubbmon Color (BABY_I)
    val BUBBMON_COLOR_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "....##..........",
        ".......BBB.B#...",
        ".....BBTTTBB....",
        ".....BWWWWWB....",
        "....BWWWWWWWB...",
        "....BWWWWWWWB...",
        ".....BBBBBBB...."
    )

    // [ID 1114] Mochimon Color (BABY_II)
    val MOCHIMON_COLOR_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "......#####.....",
        ".....#...TT#....",
        "....#.....TS#...",
        "...#.......TS#..",
        "...#..#...#TS#..",
        "...#..#...#TS#..",
        "...#....TTTTS#..",
        "...#...BWBTSS#..",
        "....#TTTSSSS#...",
        "...#SSSS#SSSS#..",
        "....####.####..."
    )

    // [ID 1115] Minomon Color (BABY_II)
    val MINOMON_COLOR_ROM = arrayOf(
        "................",
        "................",
        "......BBW#BB....",
        ".....SBBBB#B....",
        "....BS####B.....",
        "....#SSSBS#.....",
        "...BBBBS#B#B....",
        "...BBB#BSS#.....",
        "....BB#B##BB....",
        "....#BBBB#BB....",
        "....BTTTTB#B....",
        ".....BWWWT......",
        ".....BTTWB......",
        ".......B........",
        "................",
        "................"
    )

    // [ID 1116] Babydmon (BABY_II)
    val BABYDMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "....BBBBBBBBB...",
        "...BWT###TWTB...",
        "...BWTW#W#WB....",
        "...BTTT#T##B....",
        "...B#TT#T#TTBB..",
        "...BBTTTTTTTTTB.",
        ".....BT##TTTTTB.",
        "....BT####T##TB.",
        "....BTTSSSTTTB..",
        ".....BWWWWWWWB..",
        "......BWWWWBB...",
        ".....BWWWWB.....",
        ".....BBBBBB....."
    )

    // [ID 1201] Agumon X (CHILD)
    val AGUMON_X_ROM = arrayOf(
        "................",
        ".....BBBBBB.....",
        "....BT#####B....",
        ".BBBW###WTWTB...",
        ".B######WTWTB...",
        ".B###TWTWTWTB...",
        ".BTTTTTT###TB...",
        "..BTTT##TT#TB...",
        "...BTTTT###B....",
        "...BTT##TT#TB...",
        "...BTT#T###TB...",
        "...BTTTTTTSTTBBB",
        "...BWWWWWWSWWWTB",
        "..BWWSWWWWWWWWB.",
        "..BWWWWBWWWWWWB.",
        "..BBBBB.BBBBBBB."
    )

    // [ID 1202] Agumon (Black) X (CHILD)
    val AGUMON_BLACK_X_ROM = arrayOf(
        "................",
        ".....BBBBBB.....",
        "....BTWTWTWB....",
        ".BBBWTWTWT#TB...",
        ".BTTWTWT#TWTB...",
        ".BTTWTWTWTWTB...",
        ".BTTTTTTTTTTB...",
        ".BBTTTTTTTTB....",
        "...BTTTTTTTTB.BB",
        "...BTTTT##TTTBTB",
        "...BTTTTTTT#TTTB",
        "...BTTTTTTTTTTTB",
        "...BWWWWWWWWWWB.",
        "..BWWWWWBWWWWWB.",
        "..BWWWWB.BWWWWB.",
        "..BBBBBB..BBBBB."
    )

    // [ID 1203] Gabumon X (CHILD)
    val GABUMON_X_ROM = arrayOf(
        "................",
        "..BBB...BBB.....",
        "..B#WBBB##WB....",
        "..BTWT#TW##TBBB.",
        ".BTTW#WTW##TTTTB",
        ".BTTWTWTW##TTTTB",
        ".B#TTTTTT#TTTTTB",
        ".BB##TTTT#TTTTB.",
        "...BT#TT###TTTTB",
        "...BTTT#TT##TTTB",
        "...BTTSSTSSTTTTB",
        "..BTTSSTTTTTSTTB",
        "..BWWWSWWWSWWWTB",
        "..BWWWWWSSWWWWTB",
        "..BWWWWWWWWWWWTB",
        "..BBBBBBBBBBBBB."
    )

    // [ID 1204] Dracomon X (CHILD)
    val DRACOMON_X_ROM = arrayOf(
        "................",
        "......BBB..BBB..",
        "....BBW##BBT#TT.",
        "...BWTWT##WTTB..",
        ".BBTWTWTW##TB...",
        ".BTT#TWTW##TB...",
        ".B###TTT###TB...",
        ".BB#TTTT###TB...",
        "...BT###TT#TTBB.",
        "...BTTTT###T#TB.",
        "..BTTTT##T##TTTB",
        "..BTTTTSTTTTTTTB",
        "...BWWWWWWWWWWB.",
        "...BWWWWWWWWWWB.",
        "...BWWWBBWWWWWB.",
        "...BBBB..BBBBBB."
    )

    // [ID 1205] Bearmon (CHILD)
    val BEARMON_ROM = arrayOf(
        "......BBBBB.....",
        ".....B#####BB...",
        "....B#######BBB.",
        "....BBBBB###TTB.",
        "...B#SSWWBB#BB..",
        "..B#SSSWBWBB#BB.",
        ".B.B...SBWTTB##B",
        ".BB#BB#..##TBBB.",
        "..B.##...TBB....",
        "...BBBBBBB##B...",
        ".BBBBTWT###WWB..",
        "B.#SB..##BSBWB..",
        "BB#WB.##B###BTB.",
        "B.#BB##.B###BTB.",
        ".BB.SBSSB.B.BTB.",
        "..BBBBBBBBBBBBB."
    )

    // [ID 1206] Ganimon (CHILD)
    val GANIMON_ROM = arrayOf(
        ".........BBBBB..",
        "........BSSTW#B.",
        "........BBBB#STB",
        ".BBB.....B##B#TB",
        "B#SBB....B###BSB",
        "BSB#B.....B###SB",
        "BB##B......BBB#B",
        "B##B.BB..BB.B##B",
        ".B##B##BBWWB##B.",
        "..BB.TTTTSSSBB..",
        "..BTTBTSSSBS#B..",
        ".BBTTS#SS#S##BB.",
        "BWBWSSSSSS##WBWB",
        "BBSB##BBBB##BSBB",
        "BS##B######B##SB",
        "BBBB.BBBBBB.BBBB"
    )

    // [ID 1207] Junkmon (CHILD)
    val JUNKMON_ROM = arrayOf(
        "................",
        ".....BBB........",
        "....BT#TB.......",
        "....BTWTWBB.....",
        ".....BWTW#WB....",
        "...BBTWTWTWTB...",
        "...BTTTTTT#TTBB.",
        "...BTTTTT##TTTTB",
        "..BTT####TT#TTTB",
        "..BTTTTTTTT#TTTB",
        "...BTTTT###TTTB.",
        "..BSSTTTTTTTTTTB",
        "..BWWWWWWSWWSWTB",
        "..BWWWWWSWWWWWTB",
        "..BWWWWWWBBWWWTB",
        "..BBBBBBB..BBBB."
    )

    // [ID 1208] Pomumon (CHILD)
    val POMUMON_ROM = arrayOf(
        "................",
        ".....BBB..BB....",
        "....BT#TBB#B....",
        "...BW#WT##WTBB..",
        "..BTW#WT#TWTTTB.",
        "..BT#TWT#TWTTTB.",
        ".BTTTTTT##TTTTTB",
        ".BTTTTT###TTTTTB",
        ".BBTT#TT#TTT#TTB",
        "...BTTTTTT#TT#BB",
        "...BTSSSSSSSTB..",
        "...BTTTTTTTTTTBB",
        "...BWWWWWSWWWWTB",
        "...BWWWWWWWWWWB.",
        "...BWWWBBBWWWWB.",
        "...BBBB...BBBBB."
    )

    // [ID 1209] Sangomon (CHILD)
    val SANGOMON_ROM = arrayOf(
        "................",
        "................",
        ".......T.T.T....",
        "...BBBBB.BB#BBB.",
        "...BWTWTBTWTTTB.",
        "...BWTWTWTWTTTB.",
        "...BTTTTTTTTTTB.",
        "...BTTT##TTTTB..",
        "...BTTTT#TTTB...",
        "...B########TB..",
        "...BTTTTTTTTTB..",
        ".BBTTSTTSTSTTTB.",
        ".BWWWWWWWSWWWWTB",
        ".BWBWWWWBWWWBWTB",
        ".BB.BWWB.BWB.BBB",
        "....BBB..BBB...."
    )

    // [ID 1210] Sunarizamon (CHILD)
    val SUNARIZAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "...........BBB..",
        "...........BTTB.",
        "...BBBB.#..BTTTB",
        "..BTTTTBB.B#TTTB",
        ".BTT#TTT#BTTT#TB",
        ".BT#TTTT###TT#TB",
        ".BTTTT######TTTB",
        ".BTTTTTTSTTTTTTB",
        "..BWWWWWSSWWSWB.",
        "..BWWWWWSSWWSWB.",
        "..BWWWWWWWBWWWB.",
        "..BBBBBBBB.BBB.."
    )

    // [ID 1211] Ghostmon (CHILD)
    val GHOSTMON_ROM = arrayOf(
        "................",
        "........#.#.....",
        ".......B#BB.....",
        "....BBBTWTB.BB..",
        "...BWTWTWT#BTB..",
        "...BWTWTWTWTB...",
        "..BTTTTTTTTTB...",
        "..BTTTTTTTTBB...",
        "...BTTTTTTB.....",
        "...BTT##TB......",
        "....BTTTTSB.....",
        "...BTTTTTTTB....",
        "...BBWWWWSWB....",
        "...W.BWWWWSB....",
        "......BBBBB.....",
        "..........W....."
    )

    // [ID 1212] Algomon Child (CHILD)
    val ALGOMON_CHILD_ROM = arrayOf(
        "................",
        "................",
        "...BBB....BB....",
        "...BWTBBBBWB....",
        "...BWTWT##WB....",
        "...BWTWTW##TB...",
        "...BTTTTTT#TB...",
        "...BTTTT#T#TB...",
        "...BTTTT#T#B....",
        "....BT##T#TB....",
        ".....BTTTTTTB...",
        "..BBBTTTTTTTTBB.",
        "..BSWWWWWWSWSSTB",
        "..BWWWWWWWSWWWTB",
        "...BWWWWBWWWBBB.",
        "....BBBB.BBB...."
    )

    // [ID 1213] Terriermon X (CHILD)
    val TERRIERMON_X_ROM = arrayOf(
        "................",
        "....BBB.........",
        "...BWTWBB.BB....",
        "..B#WT#T#B#TBB..",
        "..BTWT#TW##TTB..",
        "..BTWT##W##TTB..",
        "..B#TTT#T##TB...",
        "..B#TTTT#T#TB...",
        "...B#####TT#TB..",
        "...BTT#TT#T#TB..",
        "...B#######T#TB.",
        "..BTTSSSSTTTSTB.",
        ".BWWWSWWSSWWWWTB",
        ".BSWWWWWWSWWWWTB",
        ".BWWWWWBWWWWWWTB",
        "..BBBBB.BBBBBBB."
    )

    // [ID 1214] Lopmon X (CHILD)
    val LOPMON_X_ROM = arrayOf(
        "................",
        "....BBB.........",
        "...BWTWBBB......",
        "...BWT#T#TBB....",
        "...BWT##W##TB...",
        "..BTWT##W##TB...",
        "..B#TTT#T#TTTB..",
        "..B#TTTT##T#TB..",
        "...B#######TTTB.",
        "...BTTTTT#T##TB.",
        "...BTT#####T#TB.",
        "..BTTTTTSTTTSTB.",
        ".BWWWSWWSSWWWWTB",
        ".BSWWWWWWSWWWWTB",
        ".BWWWWWBWWWWWWTB",
        "..BBBBB.BBBBBBB."
    )

    // [ID 1215] KoKabuterimon (CHILD)
    val KOKABUTERIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "...BBB#.........",
        "...BBB..........",
        ".....BBBB.......",
        ".....BTTTBBB....",
        ".....BTTTTTB....",
        ".....BTTTTTTB...",
        "....BWWWWWWWB...",
        "....BWWWWWWB....",
        "....BBBBBBBB....",
        "................"
    )

    // [ID 1216] Liollmon (CHILD)
    val LIOLLMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "....###B....#B..",
        "...WB#BSB..##...",
        "...B#WBB#.BS....",
        "...B#BBW#...#...",
        "..B#B###WTB#B...",
        "...S#B##BS#B#...",
        ".....###T#ST#B..",
        ".....#B.#TBBT#..",
        "....B#B.BSB..B..",
        "................",
        "................",
        "................"
    )

    // [ID 1217] Paledramon (CHILD)
    val PALEDRAMON_ROM = arrayOf(
        "................",
        "...BBBBBB..BBBBB",
        "...BWTW#WBBT##TB",
        "...BW##T##WT#TB.",
        "..BT#TWT#TWTTTTB",
        "..B#WTWT#TWTTTTB",
        ".BT#TTT#TTTTTTB.",
        ".B##TTTTTT#TTB..",
        "..BTT##T###T#TB.",
        "...B#TTT##TT##TB",
        "..BTTT###TT###TB",
        "..BTTTTTTSTTTTB.",
        "...BWWWWSWWWWB..",
        "..BWWWWWWWWWWWB.",
        "..BWWWWBWWWWWWB.",
        "..BBBBB.BBBBBBB."
    )

    // [ID 1218] Zubamon (CHILD)
    val ZUBAMON_ROM = arrayOf(
        "................",
        "...BBB..........",
        "..B#WB..........",
        "..B#WTBBBBBBB...",
        "..BTWTWTW#WTB...",
        "..BTW##TWT#TB...",
        "..B####TTT#TB...",
        "..BTTT#TTT#TB...",
        "...B#######B....",
        "...BTTT##T#B....",
        "...BTT##T##TB...",
        "...BTTTSTTTTTB..",
        "...BWWWWWWWSWB..",
        "...BWWWWWWWWWWB.",
        "...BWWWBWWWWWWB.",
        "...BBBB.BBBBBBB."
    )

    // [ID 1219] Koemon (CHILD)
    val KOEMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        ".......B........",
        "......SBSW......",
        ".....BBB#SB.....",
        ".....###B#......",
        ".....##.#SB.....",
        "....#T#T##WWT...",
        ".....###B##B....",
        ".....B###BBB....",
        "....BB#B##......",
        "....#B####B.....",
        "................",
        "................",
        "................"
    )

    // [ID 1220] Ludomon (CHILD)
    val LUDOMON_ROM = arrayOf(
        "................",
        "................",
        ".....BBBBBB.....",
        "...BBT#####BBB..",
        "..BTW######TTTB.",
        "..BTWTWTWTWTTTB.",
        "..BTTTTTTTTTTBB.",
        "...BTTT#T##TB...",
        "...BT#TT#TTTTB..",
        "...BTT#TT###TB..",
        "...BTTTT##TT#TB.",
        "..BSTTTTSTSTTTB.",
        "..BWWWWWSWWWWWB.",
        "..BWWWWWWSWWWWB.",
        "..BWWWWBBWWWWB..",
        "..BBBBB..BBBBB.."
    )

    // [ID 1221] Herissmon (CHILD)
    val HERISSMON_ROM = arrayOf(
        "................",
        "....BBBBBBBBB...",
        "...BWTW####TB...",
        ".BBTWTWTWTWTTBB.",
        ".BTTWTWTW#WTTTTB",
        ".BT###WTW##TTTTB",
        ".BTTTTTT###TTTTB",
        "..BTTTTT###TTTB.",
        "...BTTTT###TTTT#",
        "...BTTTT##TTTTB.",
        "..B#########TTT#",
        "..BTTTTTTSTTTTB.",
        "..BWWWWWSSWWWWTW",
        "..BWWWWWWWWWWWB.",
        "..BWWWWBBWWWWWB.",
        "..BBBBB..BBBBBB."
    )

    // [ID 1222] Morphomon (CHILD)
    val MORPHOMON_ROM = arrayOf(
        "................",
        "...BBB....BBBBB.",
        "...B#TB..B###TB.",
        "...BWTWBB##T#TB.",
        "...BWT#####TTTB.",
        "...BWTW##TWTTB..",
        "...BTTT##TTTTB..",
        "...BTTT##TTTTTB.",
        "..BTT##T###TT#TB",
        "..BBTT#####BBBBB",
        "....BTTSSTB.....",
        "...BTTSSSSSBB...",
        "...BWWWSSSWWB...",
        "...BWWWWWSWWB...",
        "....BWWWBWWB....",
        "....BBBB.BBB...."
    )

    // [ID 1223] Strabimon (CHILD)
    val STRABIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "......B.B.......",
        ".....TB#S.......",
        "....B#TS........",
        "....BSBW#.BBT...",
        "....BWWSBBB.....",
        "....BBB.TBB.....",
        "...BB.#W#.B#....",
        "..BB#.###..B....",
        "...B.##B#B.B....",
        ".....B#.##......",
        "................",
        "................",
        "................"
    )

    // [ID 1224] Plotmon Color (CHILD)
    val PLOTMON_COLOR_ROM = arrayOf(
        "................",
        "................",
        ".....BBBBB......",
        "...BBT####BBB...",
        "..BT#######TTB..",
        "..BTWT##WT##TTB.",
        ".BTTTT##TTTTTTB.",
        ".B#TTT##TTTTTTTB",
        ".BT###TT####TTTB",
        ".BT#TTTTT##TTTTB",
        ".B#T######TTTTTB",
        ".BBBTTTTTTSTTTTB",
        "....BWWSSSWWWWB.",
        "...BWWWWWWWWWWB.",
        "...BWWWWBWWWWWB.",
        "....BBBB.BBBBBB."
    )

    // [ID 1225] Bokomon (CHILD)
    val BOKOMON_ROM = arrayOf(
        "................",
        ".....BB...BB....",
        "....B##B.B##B...",
        "....B###B###B...",
        "....B#######B...",
        "...B##W###W##B..",
        "...B##K###K##B..",
        "...B#########B..",
        "...B###BBB###B..",
        "....B#######B...",
        "...BBTTTTTTTBB..",
        "..B#STTTTTTTS#B.",
        "..B##SSSSSSS##B.",
        "...BB#######BB..",
        "....B##B.B##B...",
        ".....BB...BB...."
    )

    // [ID 1301] Darklizamon (ADULT)
    val DARKLIZAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "......BB........",
        "......#S.WB.....",
        ".....###B#......",
        "....B####BB#B...",
        "...B######W#B.B.",
        "...##B###TB#S.W.",
        "...BBBB#BTBBTT#.",
        ".....BTWW###S.W.",
        "....TWSBS##TW.TB",
        "...BSST##BBSBS#.",
        "..BB#B#BBB#BSTB.",
        "......##B.BBB...",
        "....B#BBB.WSB..."
    )

    // [ID 1302] Deltamon (ADULT)
    val DELTAMON_ROM = arrayOf(
        ".....BBBBBBBB...",
        "....B######B....",
        "...B####BBB...BB",
        "..B.####WB...BTB",
        "..B.###WWWBBBT#B",
        "..BBBB..WB..T#B.",
        ".B####B#SBTTB#B.",
        "B#S##SBSB.TBB##B",
        "BSSSSSB#BTTTTT#B",
        "BBBBBB..BBBBBT#B",
        "..BB#.##..BWT#B.",
        "....B#..S.#WWB..",
        "....B#...S##WB..",
        "..BBB##SS#BBBBB.",
        ".B#B#BBBBB#B#B#B",
        ".BBBBBB..BBBBBBB"
    )

    // [ID 1303] Tortamon (ADULT)
    val TORTAMON_ROM = arrayOf(
        "................",
        "...........B....",
        "...B......BB....",
        "...BB....B.B....",
        "...B.B..BSTB...B",
        "...B#.BBS.BB..BB",
        "...BB#S#B#TBBB.B",
        "..BS##BB###SS.TB",
        "..B.#B.SB###STB.",
        ".B#BB.###B#BBSB.",
        ".BW..SW#SB##TBSB",
        ".BB.S#B#SBBBST#B",
        ".BSSS##TTBTS###B",
        ".BBBBBB##BT#TBB.",
        "B#BTTTTTBT#BBT#B",
        "BB.BBBBB.BBB.BBB"
    )

    // [ID 1304] Snimon (ADULT)
    val SNIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "..BB..#.#..B.#..",
        "..BB##B##B##BB..",
        "..#SB#T##S#B##..",
        ".B#B####T##S##B.",
        ".B#B#T#B##WSW#B.",
        "..###B###BB###..",
        "..########BB###.",
        "..#.TBW###..B#B.",
        "...BBS###S##SBB.",
        "....BB#BBS##BB..",
        "...BS#BB.SS###B."
    )

    // [ID 1305] Gokimon (ADULT)
    val GOKIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "...BB#...#.#T...",
        "...B..B.T....B..",
        ".....TB##B...#..",
        "...B.BB##B#..#S.",
        ".B#.B#####B#.TB.",
        ".B#..#B##B##BBB.",
        ".BW#BBT##BB#B#.B",
        "...##.B###TB#BBB",
        ".SBBBT#B#B##BBW#",
        ".B#S#B#B#####B#B",
        ".....B#.##BB#B##",
        ".....SS..B#SS.B#",
        "...BSWW...#WWSW."
    )

    // [ID 1306] Cockatrimon (ADULT)
    val COCKATRIMON_ROM = arrayOf(
        "###.###..##.###.",
        "###############.",
        ".####STW#S##S##.",
        "..#SS#BTW#SSW###",
        ".#SSB#STW#SW####",
        ".###SSSTW#TW#S##",
        "#T###STW#TW#SS##",
        "####SSSSTSSTW##.",
        ".##SSSSSSSTW####",
        "..#SSSSSTTTSSS##",
        "..#TTTTTTTWWW##.",
        "...#WWWWWW###...",
        "....########....",
        "..#####..###....",
        ".######.#####...",
        ".######.#####..."
    )

    // [ID 1307] Kiwimon (ADULT)
    val KIWIMON_ROM = arrayOf(
        ".......BBBB.....",
        "......B..##B....",
        ".....B.#####B...",
        ".....B######B...",
        ".BBBB#B#####B...",
        "B...##BBBBBBBB..",
        "BBBBBBB#BBB#B#B.",
        "....B#B#B#B#B#BB",
        "....B#BBB#BBBBBB",
        "....BB#BBBBBWBWB",
        ".....B#B#B#BWBWB",
        "......BB#BWBBBB.",
        "......B#BBB#BB..",
        "......B#B.B#B...",
        "...##SSS...SSS##",
        "...T.T.T...T.T.T"
    )

    // [ID 1308] Musyamon (ADULT)
    val MUSYAMON_ROM = arrayOf(
        "...BBBB..BBB....",
        "..B.###BB###B...",
        ".B######SB#B....",
        ".B#######SB.....",
        ".B#####B#S#BBB..",
        "..B##B###S#B##B.",
        "..BSSSS#S#B#BB#B",
        "...BBB###BSBT#BB",
        "..BB###BB##SBTB.",
        ".BBWBBB#TB##SB#B",
        "BSBTW##T#SBSBTTB",
        "BB#B#####BBB#BTB",
        ".B#BWWW###B##BTB",
        "..BW##B####BB.B.",
        ".B###B.B####B...",
        ".BBBB...BBBBB..."
    )

    // [ID 1309] Rukamon (ADULT)
    val RUKAMON_ROM = arrayOf(
        ".........BBB....",
        "..#.....BW#B....",
        ".......BW##B....",
        "....BBBBB##B....",
        "#..BWW#W###B....",
        "..BW##WTT###B...",
        ".BB###TBBT###B..",
        "B..T#BT#BT###B..",
        "BBBBBS.##T####B.",
        "BS........T###B.",
        ".BSSSSSSSSSS##B.",
        "..BBBBB##BBBS#B.",
        "..B#B..B#B..B#B.",
        "...BB...BB.BS##B",
        "..........BS#B#B",
        "..........BBB.BB"
    )

    // [ID 1310] Agnimon (ADULT)
    val AGNIMON_ROM = arrayOf(
        "................",
        "..BB...BBBB.....",
        "..BTBBBT#T#BBB..",
        "..BTWTWTWT#TTTB.",
        "...BWTWTWTWTTTB.",
        "...BWTWTWTWTTB..",
        "...BTTTTTT#TTB..",
        "....BTT#TTTTTB..",
        "....BT#TTT#TTB..",
        ".....BTTT#T#TB..",
        "....BTTTTTTTTB..",
        "...BTTTTTTSTB...",
        "...BWWWWWWWWB...",
        "...BWWWWWWWWWB..",
        "...BWWWBBWWWWB..",
        "....BBB..BBBBB.."
    )

    // [ID 1311] Wolfmon (ADULT)
    val WOLFMON_ROM = arrayOf(
        "................",
        ".......BB..BB...",
        ".....BBTWBBTB...",
        "...BBT##WT#TB...",
        "...BWT#TWT#TTB..",
        "...BW##TW##TTTB.",
        "...BTTTTT##TTTTB",
        "...BTTTTTTTTTTTB",
        "...BTT##TT##TTTB",
        "...BTTTTTTTTT#TB",
        "..BTTTTT#TTTTTBB",
        "..BSTTTTTTSSTB..",
        "..BWWWWWWWWWWB..",
        "...BWWWWWSWWB...",
        "...BWWWBWWWWB...",
        "...BBBB.BBBBB..."
    )

    // [ID 1312] Fairimon (ADULT)
    val FAIRIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        ".....BBB.##.....",
        "....BB##BS#.....",
        "...B.BBB#S.BS#..",
        "...B.WW#####SS..",
        "...B.B###S###...",
        "...B.WBBW.##B...",
        ".....BB#BW#S#...",
        ".....##BBB##....",
        "....#B#B####....",
        "....S##BB####...",
        ".....B#T##B#....",
        "......TT#.......",
        "......TBT......."
    )

    // [ID 1313] Blitzmon (ADULT)
    val BLITZMON_ROM = arrayOf(
        "................",
        "................",
        ".....B..........",
        ".....#BB........",
        ".....TB#BBB.....",
        ".....BW#TW......",
        "...B..BBBB...T..",
        "...B#B.##TWTBB..",
        "..BB##S###W###..",
        "..##BBSS#BTB##..",
        "..####B##BBB###.",
        "...WSBSS###SWSB.",
        "...B..B##B#W.B..",
        "......#BBB#T....",
        "......SB##......",
        ".....S#..#S....."
    )

    // [ID 1314] Chackmon (ADULT)
    val CHACKMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "........T.......",
        ".....BBBWB......",
        "....BTTTTB......",
        "....BTTTTTB.....",
        "....BTTTTTTB....",
        "....BTTTTTTT#...",
        "....BTTTTTTB....",
        "...BTTTTTTTB....",
        "...BWWWWWSB.....",
        "....BWWWWWB.....",
        "....BBBBBBB.....",
        "................"
    )

    // [ID 1315] Duskmon (ADULT)
    val DUSKMON_ROM = arrayOf(
        ".......BB.......",
        "...BB.BTTB..BB..",
        "...BTTB.BTTTTB..",
        "...BB.BTB..BTB..",
        "....BTTTTTTTB...",
        "....B....B.BB...",
        "....BB..BB.B.B..",
        "....B......B.B..",
        "....BB....B...B.",
        ".....B######B.B.",
        "...B#B.BB.B##B..",
        "...B##B..B###B..",
        "..B.BSSSSSB.B.B.",
        "..BB.BSSSSSB.BB.",
        "....BSSB.BSSB...",
        "...BBBB...BBBB.."
    )

    // [ID 1316] Baboongamon (ADULT)
    val BABOONGAMON_ROM = arrayOf(
        "................",
        "...........BBB..",
        "...........B#TB.",
        "..T.BBBBBB.BTTB.",
        ".BTBWTWTWTBTTTTB",
        ".B#TWTWTWTWTTTTB",
        ".BTTTTTTTTTTTTTB",
        ".BTTT#TTTTT#TTTB",
        ".B####TTTTT#TTTB",
        "..BTTTTTTT##TTB.",
        "...B##TT#TT#TB..",
        "...BTTTTTTTTB...",
        "...BWWWWWWWWB...",
        "..BWWWWBBWSWWB..",
        "..BWWWB..BWWWB..",
        "..BBBBB..BBBBB.."
    )

    // [ID 1317] Kaiser Leomon (ADULT)
    val KAISER_LEOMON_ROM = arrayOf(
        "................",
        "....BBBBB.......",
        "...BW###WBBB....",
        "..BTWTWTWTWB....",
        ".BTTWTWTWTWB.BBB",
        ".BTTWTWTWTWTBTTB",
        ".BTTTTTTTTTTTTTB",
        ".BTTTTTTTTTTTTTB",
        "..BTTTT####TTTB.",
        "...B#TTTT##TTTB.",
        "...BTTTTTTTTT#TB",
        "....BTTTTTTTTTTB",
        "...BWWWWWWBWWWTB",
        "..BWWWWWWB.BWWTB",
        "..BWWWWWWB.BWWTB",
        "..BBBBBBBB.BBBB."
    )

    // [ID 1318] Aegiomon (ADULT)
    val AEGIOMON_ROM = arrayOf(
        "..BB.BBB.BBB....",
        "..B#BS.SB###B...",
        ".B.B#SS.##BB....",
        ".BS#S#TS.###B...",
        ".B#T#T#TS###B...",
        "..BWTW##T##B....",
        "..BWWW#W#T##B...",
        "..B#WW##T##B....",
        "...B#####BB#BB..",
        "..B########B##B.",
        ".B#B#####B#BBBB.",
        ".BSB#W##BS#B....",
        "..BB#####BB.....",
        "...BSTST.#B.....",
        "..B##TSB###B....",
        "..BBBBB.BBBB...."
    )

    // [ID 1319] Shadramon (ADULT)
    val SHADRAMON_ROM = arrayOf(
        "................",
        "................",
        "...BBBBB.BB..BBB",
        "...BWT#TBTB.BTTB",
        "..BT##WTWT#BTTTB",
        "..B##TWT#TWTTTTB",
        "..B##TTT#TT#TTTB",
        "..BTTTT#TTT#TTB.",
        "...B#TTTTTTTTB..",
        "..BTTTT#T##TTB..",
        ".B#TT##T###TTB..",
        ".BSTTTTTTTSTB...",
        ".BWWWWWWWWWWB...",
        "..BWWWWWWWWWB...",
        "..BWWWBBWWWWB...",
        "..BBBB..BBBBB..."
    )

    // [ID 1320] Flybeemon (ADULT)
    val FLYBEEMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "........B.......",
        "....###B........",
        "...####B....B#W.",
        "...B#BB#BBB#W...",
        "....#BBT#B#WB#B.",
        "....#T##B.WW.#B.",
        ".....BBST#......",
        "...BT#BB##......",
        "...B##SBBBSB....",
        "......#B.BS##T..",
        ".......T.B#.SSB.",
        "........BB......"
    )

    // [ID 1321] Harpymon (ADULT)
    val HARPYMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "....BB#B..#.....",
        "....B..#B.......",
        "....#S##B#......",
        ".....###T#......",
        ".....###T##.....",
        ".....ST#S#B.....",
        "......#.#B......",
        ".....#.BT.S.B#B.",
        "...B...S#.B.#...",
        "..B....B..##..#.",
        "...SB.#.B..B#...",
        "..B...BB.#B.B##B",
        "..BB#WWB.BW#B..."
    )

    // [ID 1322] Manbomon (ADULT)
    val MANBOMON_ROM = arrayOf(
        "................",
        "............BBB.",
        ".....BBBB..BTTB.",
        "....BTW##BBT#TB.",
        "...BWTWT##WT#TB.",
        "...B#TWTW##TTTB.",
        "...BTT#TTT#TTTB.",
        "...BTT#TTT#TTTB.",
        "...B########TB..",
        "...BTTT####T#TB.",
        "...BT#TT#TTTTTB.",
        "..BSTTTTTSSTTTB.",
        "..BSWWBBWSSWWWB.",
        "..BWWB..BWWBBBB.",
        "...BB....BB.....",
        "................"
    )

    // [ID 1323] Pipismon (ADULT)
    val PIPISMON_ROM = arrayOf(
        "................",
        "................",
        "...............B",
        ".B.S.........W#.",
        "..T#B...B..BB#..",
        "..##S..B#.###B..",
        "..##B.B###WW#...",
        "..#BSBW#B#BW##..",
        ".T##SS#BB.S#B#..",
        "..BB#.#TBW##S...",
        "...BBBBTSB###...",
        "....B#BB.B...B#.",
        ".....#BB#TSSW..#",
        "....BB##BBT#.#BB",
        "...BB#.##BT.#...",
        "........B..#...."
    )

    // [ID 1324] Baromon (ADULT)
    val BAROMON_ROM = arrayOf(
        "................",
        "................",
        "......B....B....",
        "......BBB#BWB...",
        ".....#######W...",
        "....B#######BB..",
        "....B#B##B#BB...",
        "....BB#B####BB..",
        "....B#BTB#BWBB..",
        ".....#T##B##W...",
        "....###TT#BSBB..",
        "...B######BB#W..",
        "...S#BT#STB##B..",
        "...BB######BBB..",
        "....S#BSSS###...",
        "....B##BB####B.."
    )

    // [ID 1325] Pteranomon (ADULT)
    val PTERANOMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        ".......B........",
        ".#B..BS.........",
        "..BBS#.B..BBS##S",
        "..##BTWBSBB#TTB.",
        ".#T#S##BTTBB#...",
        ".#.#B.B###BS..B.",
        "....B.#B#SBT##B.",
        "......SBB#WB....",
        "......B#.BW.....",
        "......B#.W#.....",
        "......BB.B#.....",
        "................"
    )

    // [ID 1326] Marin Bullmon (ADULT)
    val MARIN_BULLMON_ROM = arrayOf(
        "##.B............",
        "######..........",
        ".######.........",
        "..#####.........",
        ".##S####...#....",
        "##S#SST##.###...",
        "######ST#####...",
        "#...#W#S######..",
        ".##W..W#T#####..",
        "...#W..########.",
        "..#T#.##ST#####.",
        ".##W#.#######...",
        ".###..W#T#TT###.",
        "..###W##SS###TS#",
        ".#.W#####W..####",
        ".######.#####..."
    )

    // [ID 1327] Kongoumon (ADULT)
    val KONGOUMON_ROM = arrayOf(
        "................",
        "................",
        "....BB..........",
        "....SB.BBSTBB...",
        "..B#...B#BT.B#..",
        ".BBB.#TT###TBBTB",
        "BB.SB.TBW#B#WBB#",
        ".BBB#.TBBB#WSBB.",
        ".B#..BBS#B#B##B.",
        ".TS#BBBB#B#B#S#.",
        ".T#B.B.BTBT#B#T.",
        "..BBSBB##.S#BB..",
        "..BSSWB##S#SSB..",
        "..S.T#####TB.T..",
        ".BS.BWWWW.SS.SB.",
        ".....B###.BW...."
    )

    // [ID 1328] Peacockmon (ADULT)
    val PEACOCKMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        ".#.............#",
        ".##B.........###",
        ".S.B##B.....TB.S",
        ".###WT#BB..TB###",
        "...###W#BB#BSS..",
        "..#B.###BBBB.#S.",
        "...##S#####.#S..",
        "...B.##B#..#....",
        ".....#BTB##.....",
        "....BBT#BB##B...",
        "....B#W..BW#BBB.",
        ".....B#..B#W....",
        "...B##....##B..."
    )

    // [ID 1329] Moosemon (ADULT)
    val MOOSEMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "..BB...T.T......",
        "..BTB.BTBB......",
        "..BTWBWTWB......",
        "...B##TTTB......",
        "...B#TTTT#B.....",
        "...BTTTTTTB.#...",
        "...BTTTTTTTBB...",
        "....BTTTT#TTB...",
        ".....BTTTSSTTB..",
        ".....BWWWWWWWB..",
        ".....BWWWSWWWBW.",
        ".....BBBBBBBB...",
        "................"
    )

    // [ID 1330] Fladramon (ADULT)
    val FLADRAMON_ROM = arrayOf(
        "................",
        "................",
        "..B.............",
        "..BS...#..#.....",
        "...#B.###BB.....",
        "...B#B###B......",
        "....B#####......",
        "....T##T#W......",
        "......#.B.......",
        "....B.B###......",
        "...B##TT####B...",
        "..BB##W#S###B#..",
        "...#BBB##SS.....",
        "...B##W.B##B....",
        "................",
        "................"
    )

    // [ID 1401] Tonosama Gekomon (PERFECT)
    val TONOSAMA_GEKOMON_ROM = arrayOf(
        "........BBBBB...",
        "........B####B..",
        ".........B###B..",
        "..........BTB...",
        "....BB.BBBTSB...",
        "...B#TBTTBSB....",
        "...B#.B#.BB.....",
        "..BB#.B#.SBB....",
        ".B##TSSS##SBB...",
        "B#TTSSSS##SB#B..",
        ".BBBBBBSSS##BBB.",
        "..B##WWB####B#B.",
        "...B##WWB####BB.",
        "...B##WBB##BB##B",
        "..BSBBB###B####B",
        "..BBB.BBBB.BBBBB"
    )

    // [ID 1402] Dagomon (PERFECT)
    val DAGOMON_ROM = arrayOf(
        "....BBBBBBB.....",
        "...BTS#S#W#B....",
        "...BSTW#W###B...",
        "..BTW####T##B...",
        "..BWT###T###B...",
        "..B#########B...",
        "..B#TTT######B..",
        "..B#TB####S#SB..",
        "..B#T###S#S#SBB.",
        "..BS####S#SSSS#B",
        ".B#S#SS#SSSSBS#B",
        "BS#SSSS#SSSBS#B.",
        "BSSSBWSSSSS#BB..",
        ".BBB###SS##S#BB.",
        ".B#WS#BBBBWSS##B",
        "..BBBBB...BBBBBB"
    )

    // [ID 1403] Pukumon (PERFECT)
    val PUKUMON_ROM = arrayOf(
        ".B..B..B..B..B..",
        "..B..B.B.B..B...",
        "B..B.BB#BB.B..B.",
        ".B.BBSTSTWBB.B..",
        "..B#STSTS#TWB...",
        "BBS##STS##STWBBB",
        ".BTST###TSTWTB..",
        "BBSTS###STWSWBBB",
        ".BTWT###TWS#SB..",
        ".B#S#####S#W#BBB",
        "BB##W###W###WB..",
        ".B###W#W#####BBB",
        "BB##########B...",
        "..B#B###B##B.B..",
        ".B.BBBBBBBBB..B.",
        "B...B.B.B.B.B..B"
    )

    // [ID 1404] Hangyomon (PERFECT)
    val HANGYOMON_ROM = arrayOf(
        "BB...BBBBB...BB.",
        "BSB.B#...#B.BSB.",
        "BSSB##..###BSSB.",
        "BBSB##...##BSBB.",
        "BSBSBBBBBBS#BSB.",
        "BSBB#WWWW#B#BSB.",
        ".BB#BBBBBB#BBB..",
        "BBBB#WWWW##B..BB",
        "B.SBBBBBBBBTBB.B",
        "BSBT##.###T##BSB",
        ".BBT######T##BB.",
        "..BB##.####BBB..",
        "...B#######B....",
        "..BBT####TT#B...",
        "..B##TTBT###B...",
        "BBBBBBB.BBBBBBB."
    )

    // [ID 1405] Anomalocarimon (PERFECT)
    val ANOMALOCARIMON_ROM = arrayOf(
        ".......BBBBB....",
        "......B.B##SB...",
        "......BB.BBBTB..",
        "..........B#TB..",
        "........BBBBTB..",
        ".......B###SBB..",
        "......BBBBBSTB..",
        "..BB.B###STBTBB.",
        ".B#WB###SSS#BWWB",
        ".B.BB###SBBS#B#B",
        ".BBB#B#SB#BSTB.B",
        "B#SBBBBBBBBTTBB.",
        "B.B####SSBTTBBTB",
        "BB#SBBBSSTBB.B.B",
        ".B#B...B#WB..BB.",
        "..BB...BBB......"
    )

    // [ID 1406] Scorpiomon (PERFECT)
    val SCORPIOMON_ROM = arrayOf(
        "................",
        "..........BBBBB.",
        "......BB.B#TT#TB",
        "....BBWB.BWTTTTB",
        "...BWTWTB##TTTTB",
        ".BBTWTWTWTW#TTTB",
        ".BTTTTTTTTTTTTTB",
        ".BB#TTTT#TTTTTTB",
        "...B####T##T#TB.",
        "...BTTTT##T##TB.",
        "...BTTTT##TTTTB.",
        "...BTTTTTTTTTTB.",
        "...BWWWWWWWWWB..",
        "...BWWWWBWWWWWB.",
        "...BWWWB.BWWWWB.",
        "...BBBBB.BBBBBB."
    )

    // [ID 1407] Blossomon (PERFECT)
    val BLOSSOMON_ROM = arrayOf(
        ".......BBB......",
        "....BBB###BBB...",
        "...B##BS##B##B..",
        "..B#S#BBBBB#S#B.",
        "..B##BT###TB##B.",
        "...BBB#SSTBBBB..",
        "..B#B#BSSB#TB#B.",
        ".B#SBT#SS#TTBS#B",
        ".B##B##S####B##B",
        "..BBBT##S#TTBBB.",
        ".B###B###TTB###B",
        ".B#BBBBBBBB#BB#B",
        "..B##B#B#B#B##B.",
        "..BWBWBW#WB#BWB.",
        ".BWWBWBBBBBWBWWB",
        ".BBBBBB...BBBBBB"
    )

    // [ID 1408] Asuramon (PERFECT)
    val ASURAMON_ROM = arrayOf(
        ".BB.BBB.BBB.BB..",
        ".B#B##BBB##B#B..",
        "BB##BB#B#BB##BB.",
        "B#BB##BBB##BB#B.",
        "BB###BWB#####BB.",
        ".BB#B#WB#WW#WB..",
        "BB###WWB#####BB.",
        "BBBBBBWB#S#BTBTB",
        "BBSS##BTBS##TBTB",
        "BTBBBBT#TBBBB#BB",
        "B##BSSBS##BB##BB",
        "B#BBBBBBBBS#B#B.",
        "BSSBTB#WBBSS##B.",
        ".BBB#B#WB#BBBB..",
        "BST#BBBBBBST#TB.",
        "BBBBB....BBBBBB."
    )

    // [ID 1409] Aegiochusmon (PERFECT)
    val AEGIOCHUSMON_ROM = arrayOf(
        "..##.##S#...##..",
        "..###...W#####..",
        "...###.W.W###...",
        "..#W##TBT.W#....",
        "..##W.B.B.#.#...",
        "..###WBB#.T.#...",
        "..#######W#WT#..",
        "...#######STT#..",
        "..###S######TT#.",
        "..###########T#.",
        "...#S#####S##T#.",
        "..###########S..",
        "..###.#..####...",
        "...#.#.##.##....",
        "..S#########S...",
        "..S##S##.#SS#..."
    )

    // [ID 1410] Vritramon (PERFECT)
    val VRITRAMON_ROM = arrayOf(
        "................",
        ".BB.....BBBB....",
        ".B#BBBBB###B....",
        ".B#TWTWTWTW#BBB.",
        ".BTTWTWT#TWTTTTB",
        ".BT#WTWTWTWTTTTB",
        ".BTTTTTTTTTTTTTB",
        "..BTTTTTTTTTTTTB",
        "...BTTTTTT#TT#TB",
        "...BTTTTTTTTTTTB",
        "...BTTTTTTT#TTTB",
        "..BSTTTTTTSSTTTB",
        "..BWWWWWWWWWWWTB",
        "..BWWWWWBWWWWWB.",
        "..BWWWWB.BWWWWB.",
        "..BBBBB..BBBBBB."
    )

    // [ID 1411] Aldamon (PERFECT)
    val ALDAMON_ROM = arrayOf(
        "................",
        "...#...BBBB.....",
        "...BBBBT#T#BBB..",
        "...BWTWTWT#TTTB.",
        "...BWTWTWTWTTTB.",
        "...BWTWTWTWTTB..",
        "...BTTTTTT#TTTB.",
        "...BTTT#TTTTTTB.",
        "....BT#TTTTTTTB.",
        ".....BTTT#T#TB..",
        "....BTTT#TTTTTB.",
        "...BTTTTTTTSTTB.",
        "...BWWWWWWWWWB..",
        "...BWWWWBWWWWB..",
        "...BWWWB.BWWWB..",
        "....BBB...BBBB.."
    )

    // [ID 1412] Beowolfmon (PERFECT)
    val BEOWOLFMON_ROM = arrayOf(
        "................",
        "......BB..BB....",
        ".....BWTBBWTB...",
        "...BBT#TW#WTB...",
        "...BWT#TW##TB...",
        "...BWTWTW##TB...",
        "...BTTTTTTTTTB..",
        "...BTTTT#TTTTTB.",
        "...BTT##TT###TB.",
        "...BTTTTTTTTTB..",
        "...BTTTTT#TTTTB.",
        "...BSTTTTTTSSTB.",
        "...BWWWWWWWWWWB.",
        "...BWWWWWWWWWB..",
        "...BWWWBBWWWWB..",
        "...BBBB..BBBBB.."
    )

    // [ID 1413] Velgrmon (PERFECT)
    val VELGRMON_ROM = arrayOf(
        "................",
        "....BBBBB...BBB.",
        "...BWTWTB..BTTTB",
        "...BWTWTWBBTTTTB",
        "..BTWTWT#TWTTTTB",
        ".BTTWTWTWT#TTTTB",
        ".BT#TTTTTT#TTTB.",
        ".B##TTTTTT#TTBB.",
        ".B#TTTTTTTTTB...",
        ".B#T#TTT##TTTB..",
        ".BBBBBBTTT#TTTB.",
        ".......BTTTSTTTB",
        "........BWWWWWBB",
        "........BSWWWB..",
        "........BWWWWB..",
        ".........BBBBB.."
    )

    // [ID 1414] Raihimon (PERFECT)
    val RAIHIMON_ROM = arrayOf(
        "................",
        ".....BBBBBB.....",
        "...BBTW#W##B....",
        "..BTWTWTWTWTB...",
        "..BTWTWTWTWTTBB.",
        "...BWTWTWTWTTTB.",
        "...B#TTTTTTTTTTB",
        "...BTTTTTTTTTTTB",
        "....BT##T##TTTTB",
        "...BTTTTT#TTTTTB",
        "...BTT#TTTTTTTTB",
        "..BSTTTTSTSTTBTB",
        "..BWWWWWWWWWB.BB",
        "...BWWWBWWWB....",
        "...BWWB.BWWB....",
        "...BBB...BBB...."
    )

    // [ID 1415] Fantomon (PERFECT)
    val FANTOMON_ROM = arrayOf(
        ".#####..........",
        ".######.........",
        ".#.....BBBBB....",
        ".#....B####TB...",
        ".#...B#TTTTSTB..",
        ".#..B#BBB#SSS#B.",
        ".#..BBBBBBB#SSB.",
        ".BBB#B#BB#BB#S#B",
        "B.B.TBBBBBBB#S#B",
        "B.BWWTTTSSSS#B#B",
        ".BB#W#W#.##BBB.B",
        ".#.BWB#.T.#B..#B",
        ".#.BWB#W.W##B##B",
        ".#.B#BBW#W###B#B",
        ".#.BB.B#B#B#BB#B",
        ".#..B.BB.B.BB.BB"
    )

    // [ID 1416] Marin Devimon (PERFECT)
    val MARIN_DEVIMON_ROM = arrayOf(
        ".......BB.......",
        "......B##B......",
        ".....BS###B.....",
        "....BST####B....",
        ".B.BSST#####B.B.",
        ".BBS#STS#####BB.",
        ".BSS##TT######B.",
        "..BSS#TT#####B..",
        "...BSSTTT##BB...",
        "..BWB#####BW#B..",
        ".B##BBBBBBBBB#B.",
        ".B#BBW####B..#B.",
        ".B..BWW####BBBB.",
        "B#BBWBWW##BW#B#B",
        "B...##BBBB##...B",
        ".BBBBBB...BBBBB."
    )

    // [ID 1417] Mega Seadramon (PERFECT)
    val MEGA_SEADRAMON_ROM = arrayOf(
        "BB.......BBBBB..",
        "B.BB.BBBBS#WB...",
        ".B..B###SSSBBBB.",
        "..B##BBBS#WW#B..",
        ".B#SB#BSSBBBB...",
        "B#SSSSSS#WWW#B..",
        "BSBBBB##BBWBBBB.",
        "BB########W#B...",
        "..BBBBB##BBBBB..",
        "....BB#B##B...BB",
        "...B#T##BBBB.BWB",
        "..B#T###B....BB.",
        "..BT#T###BBBB#B.",
        "..B#T#T######B..",
        "...B#T#T#T##B...",
        "....BBBBBBBB...."
    )

    // [ID 1418] Waru Seadramon (PERFECT)
    val WARU_SEADRAMON_ROM = arrayOf(
        "................",
        ".BB......BBBB#..",
        ".BTBBBBBBTWTB...",
        "..BTWTWTWTWTTBT.",
        "..BTWTWTWTWTTB..",
        ".BTTWTWTWTWTTB..",
        ".BTTTTTTTTTTTTB.",
        ".BBTTTTTTTTTTTB.",
        "...BTTTTTTTBBB..",
        "....BTTTTTB.....",
        "...BTTTT#TTB.BBB",
        "...BTTTTTTTTBTTB",
        "...BWWWWWWWWWWB.",
        "...BWWWWWWWWWB..",
        "...BWWWWWWWWB...",
        "....BBBBBBBB...."
    )

    // [ID 1419] Aero V-dramon (PERFECT)
    val AERO_V_DRAMON_ROM = arrayOf(
        ".....BBBBBBBBBB.",
        "BB..BTTTTB#SS#B.",
        "B#BBT####TB#BB..",
        "BB#.####.#BB##B.",
        "B.......##B####B",
        "BBBBBB..##TB###B",
        ".B###SSS#T#B#B#B",
        "..BBBBB#..S#B.BB",
        "....BTT.W.#B.B.B",
        "..BBW..WWSBB.BB.",
        ".B#BWWWW.B#.#BBB",
        ".BSB.WW.#BS#BBTB",
        "..BB#..##TBBSBTB",
        ".BB.B##SSTB.SSB.",
        "B#BSSBBBBB.B.B#B",
        "BBBBBBB..BBBBBBB"
    )

    // [ID 1420] Arachnemon (PERFECT)
    val ARACHNEMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        ".....BBBBB..T...",
        "..BBBTWTWTBB....",
        "..BTWTWTWTWTB...",
        "...BTTTTTTTTB...",
        "....BTTTTTTB....",
        "....BTTTTTB.....",
        "..BBTTTTTTTBB...",
        ".#TTTTTTTTTTTB..",
        "..BTTBTTTTTTTTB.",
        "...BB.BWWWWWWWB.",
        "...BB.BWWWWWBWB.",
        "....W.BBBBBB.BB.",
        "................"
    )

    // [ID 1421] Lucemon Falldown Mode (PERFECT)
    val LUCEMON_FALLDOWN_MODE_ROM = arrayOf(
        "................",
        "...BBBBBB.BBB#..",
        "..BT#TW##BWTB...",
        "..B#WTWTWTWT#BB.",
        "..BTWT#T#T#TTTB.",
        "..BTWTWT#T#TTTB.",
        "..BTTTTT#T#TTTB.",
        "...BT###TTTTTB..",
        "....BTTTTT#TTB..",
        "...BTT##TTT#TTB.",
        "...BTT#TT#T#TTB.",
        "...BTTTTTTTTTTB.",
        "...BWWWWWWSWWB..",
        "...BWWWWSWWWWB..",
        "...BWWWWWWWBWB..",
        "...BBBBBBBB.BB.."
    )

    // [ID 1422] Cerberumon (PERFECT)
    val CERBERUMON_ROM = arrayOf(
        "................",
        "..BBB...BBB.....",
        "...B#BBB##B.....",
        "..BTT###TB#B.BB.",
        "BBT###BSB##B..BB",
        "BS#TB##SB#BB..BB",
        "BWWWWWWB#BB#B.BB",
        "B######B####BBSB",
        ".B#####B#####BB.",
        "..BBBBBW#####BB.",
        "....BSSSW#T##BSB",
        "....BTBTSWWWWTSB",
        "....B#B#TBBB##SB",
        "..BBTB#TSB..B#SB",
        ".B##B#B#B..B#B#B",
        ".BBBBBBBB..BBBBB"
    )

    // [ID 1423] Duramon (PERFECT)
    val DURAMON_ROM = arrayOf(
        "................",
        "..BB............",
        "..BTB.....BB....",
        "..BTWBBBBBWB....",
        "...BWTWTW#WB....",
        "...BWTWTW##B....",
        "...B##TT##TTBBB.",
        "...BTTTTTTTTTTB.",
        "...B###T#TT#TB..",
        "...BTTTTTT#TTB..",
        "...BTT#TT##TTTB.",
        ".BBSTTTTSSTTTSTB",
        ".BSWWWWWSWWWWWTB",
        ".BWWWWWWWWWWBBBB",
        ".BBWWWWBWWWB....",
        "...BBBB.BBBB...."
    )

    // [ID 1424] Mephismon (PERFECT)
    val MEPHISMON_ROM = arrayOf(
        "................",
        "................",
        ".........T......",
        ".....BB.BTBB....",
        "....BTWBWTWTB...",
        "...BWTWTW#WTTB..",
        "...BTTTTTTTTTB..",
        "...BTTTTTTTTBB..",
        "...BBTTTTTTB.#..",
        "...#.BTTTTB..#..",
        ".....BTTTTTB....",
        "....BTTTTTTB....",
        "....BWWWWWWWW...",
        "....BWWWBWWB....",
        ".....BBB.BBB....",
        "................"
    )

    // [ID 1425] Mihiramon (PERFECT)
    val MIHIRAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "......####..#T..",
        "....##W#.##WT###",
        "...####.#W#####.",
        "....######W##.#.",
        "##.#BB##T#T#..#W",
        ".##T#T###B..##.#",
        ".####T##W#B#..#T",
        ".##.T#T####S##.#",
        ".#BB##BB##SSBSSS",
        "....#T##.TSS##S.",
        "...#.#S#..S#.##.",
        ".....SW..SW..#W.",
        "...##BB##BB#B##."
    )

    // [ID 1426] Vajramon (PERFECT)
    val VAJRAMON_ROM = arrayOf(
        "................",
        "................",
        ".......B........",
        "...####.#.......",
        "....B#BT#.......",
        "...###T#TS......",
        "...SSB#SB#..##..",
        "...#TSB####..##.",
        "...###WWW##...#.",
        "...WWWBW.T.#..#.",
        "...#BTS#W#B##S..",
        "..##B####WSSS#..",
        ".#B#######TTS#..",
        "...#T###TT#.TS#.",
        "....#W..#W.#.##.",
        "...SBB.SBB..STT."
    )

    // [ID 1427] Andiramon Deva (PERFECT)
    val ANDIRAMON_DEVA_ROM = arrayOf(
        "......BB.BBBBB..",
        "...BBBS#BSSS#B..",
        "...B.BBBS###B...",
        "....B.###BBB....",
        "...B..#B#BWB....",
        "...B#####BWB....",
        "...BBBBBBWB#B...",
        "...B#WWWW##BB...",
        "...BB.W.##B##B..",
        "..BSBBB###BS#B..",
        "..BSB.B#TB.BS.B.",
        ".B.#BB###TBBS.B.",
        "B.S#BTTTTTBBS#.B",
        "BS#BBTBBB#TBS##B",
        "B#B#TTB.BTTB#B#B",
        ".BBBBBB.BBBBBBB."
    )

    // [ID 1428] Cerberumon Werewolf Mode (PERFECT)
    val CERBERUMON_WEREWOLF_MODE_ROM = arrayOf(
        ".....BB..BBB....",
        "....B#BBBT#B....",
        "...BS#TT#SB.....",
        ".BBS#TBB#SBB....",
        ".BS##B##SB#B.BB.",
        ".BBBBBBSB#BBB#B.",
        "..B###WB#BBS#B..",
        "...BWWBB#B#BB#B.",
        ".BBTBB##BB#TT#B.",
        "BT##B###BTT###B.",
        "BBB#WB##BBB##WB.",
        "B##WWBBBB###WB..",
        ".BBBBT###BBBBBB.",
        "..BT##SBS##TBS#B",
        ".B#B#SB.BS#B#BBB",
        ".BBBBB...BBBBB.."
    )

    // [ID 1429] Justimon Blitz Arm (PERFECT)
    val JUSTIMON_BLITZ_ARM_ROM = arrayOf(
        "................",
        "..........BBB...",
        ".....BBBBBWTB...",
        "....BT##W##TTB..",
        "...BW##TWTWTTTB.",
        "...BWTWTWTWTTTB.",
        "...BTTTTTTTTTBB.",
        "....BTTTTTTTB...",
        "....BTT#TT#TB...",
        "....BTTT#TTB....",
        "...BTTT##T#TB...",
        "...BTTTTTTTTB...",
        "...BWWWWWWWB....",
        "...BWSWWWSSWB...",
        "...BWWWBWWWWB...",
        "...BBBB.BBBBB..."
    )

    // [ID 1430] Thunderballmon (PERFECT)
    val THUNDERBALLMON_ROM = arrayOf(
        ".......BBBB.....",
        "......B..B......",
        ".....B##B.......",
        "....B###BBB.....",
        "...B#BB##B#B....",
        "..B##B.#B###B...",
        "..B##B.BWTT##B..",
        "..B###B###W#B#B.",
        ".BBS####TSW#BB#B",
        "B.BWTTTTWW#B.#BB",
        "B#B#W.#####B###B",
        "BB.B######B#BBB.",
        "....BBBBBBBB....",
        "..BBSSB.BSS#BB..",
        ".B.B#SB.B#SB.#B.",
        ".BBBBB...BBBBBB."
    )

    // [ID 1431] Crys Paledramon (PERFECT)
    val CRYS_PALEDRAMON_ROM = arrayOf(
        "................",
        "....BBBB.BB.BB..",
        "...BWTWTB#WBTTB.",
        "...BWTWTWTWTTTB.",
        "...BWTWTW#WTTTTB",
        "..BTWTWT#TWTTTTB",
        ".BT#TTT##TTT##TB",
        ".BTTTT##TTTTT#TB",
        "..B##TTT##TTTTTB",
        "...BTT##TTTTTTBB",
        "...BTTTT##T#TB..",
        "...BTTTTTTSSTTBB",
        "...BWWWWWWWWWWTB",
        "..BWWWWWSWWWWWB.",
        "..BWWWWWWWWWWB..",
        "..BBBBBBBBBBBB.."
    )

    // [ID 1432] Metal Fantomon (PERFECT)
    val METAL_FANTOMON_ROM = arrayOf(
        "................",
        "..BBBB..........",
        "..BTWTB.........",
        "..B##TWBBBBBBB..",
        "..B#WTWTWTWTTTB.",
        "..B#WTWT##WTTTB.",
        "..B#TTTT###TTTTB",
        "..BTTTTT##TTTTTB",
        ".B#TTTT####TTTTB",
        ".B#TTTTTTTTTTTTB",
        ".B#TT#TTTT#TTTTB",
        "..BTTTTTTSSTTTTB",
        "..BWWWWWWWSWWWTB",
        "..BWWWWWSWSWWWTB",
        "..BBWWWWWBWWWWTB",
        "..W.BBBBB.BBBBB."
    )

    // [ID 1433] Blizzarmon (PERFECT)
    val BLIZZARMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "........B#TT....",
        ".......BB#TBB.B.",
        "....#WS##TS#.BBB",
        "..S#.S##WBS#S.B.",
        ".....B#..T#S#B..",
        "...##.B###B#WW#.",
        "...B####W#B.###.",
        "..B#B##.#B##B##B",
        "...B#TTBB##.B##.",
        ".B##B.####B##BB.",
        ".##BB.W##BBB##B.",
        "..B#B.##B#.WBB#.",
        "....#BB##..BB##."
    )

    // [ID 1434] Algomon Perfect (PERFECT)
    val ALGOMON_PERFECT_ROM = arrayOf(
        "................",
        "....BBB.........",
        "....BTWB........",
        "....BTWTBB.BBB..",
        "...BWTWTWB.BTB..",
        "...BWTWTWTB#TB..",
        "...BTTTTTTTTTB..",
        "...BTTTTTTTTTB..",
        "...BT###TTTT#T#.",
        "....BT#TT#T#TB..",
        "....BTTTTTT##T#.",
        "...BTTTTTSTTBB..",
        "...BWWWWWWWB....",
        "...BWWWWWWB.....",
        "...BWWBBWWWB....",
        "...BBB..BBBB...."
    )

    // [ID 1501] Marsmon (ULTIMATE)
    val MARSMON_ROM = arrayOf(
        ".BB.BB..BBBBB...",
        ".B#B#BBBW###B...",
        ".B##BT#S#TBB....",
        "..B###SBBWTB....",
        "..B##TB#BSTWB...",
        ".BS##S#SS#WTSB..",
        ".BSSS#BT#WTSB...",
        "..BBBB.B##BB....",
        "..B##B.BSBB#B...",
        "...BB#B#B####B..",
        "..BB#######B##B.",
        ".B##B#####B###B.",
        ".B##B####TB##B#B",
        "..BB#WTBTW#BB##B",
        "..B###B.B###BBBB",
        "..BBBB...BBB...."
    )

    // [ID 1502] Minervamon (ULTIMATE)
    val MINERVAMON_ROM = arrayOf(
        "................",
        "................",
        "..T.......BB....",
        "..BBBBBBBBWB....",
        "..BTWTWTWTWB....",
        "..BTWTWTWTB.....",
        "..BTTTTTTB......",
        "..BTTTTTTTB.....",
        "...BTTTTTTTB....",
        "...BTTTTTTTTBBB.",
        "...BTTTTTTTTTTB.",
        "..BTTTTTTTTTTB..",
        "..BWWWWWWWWBBB..",
        "..BWWWWWWWB.....",
        "..BWWWBBWWB.....",
        "..BBBB..BBB....."
    )

    // [ID 1503] Neptunemon (ULTIMATE)
    val NEPTUNEMON_ROM = arrayOf(
        "................",
        ".....BBBBB......",
        "...BBTWTWTBBB...",
        "..BTWTWTWTWTB...",
        "..BTWTWTWTWTB...",
        ".BTTWTWTWTWB....",
        ".BTTTTTTTTB.....",
        ".BBTTTTTTTTB....",
        "...BTTTTTTTB....",
        "..#TTTTTTTTTBB..",
        "...BTTTTTTTTTB..",
        "..BTTTTTTTTTTTB.",
        "..BWWWWWWWWWWWB.",
        "...BWWWWWWWWBBB.",
        "...BBWWWWWWB....",
        ".....BBBBBB....."
    )

    // [ID 1504] Vulcanusmon (ULTIMATE)
    val VULCANUSMON_ROM = arrayOf(
        ".....#####......",
        "....#.#WW##.....",
        "...#.#W#W###....",
        "..#.#W#########.",
        "..##W#######B#T#",
        "..#####.####BBSB",
        "..##W#.#####B#B.",
        ".BBWW#######BT#B",
        "B##BB#####BB#BSB",
        "BSBB#####B#BT#B.",
        ".BT#STBT#BT#BSB.",
        ".BSBBBT#S#BSBB..",
        "..B..B#####B....",
        "....BT##B#TB....",
        "...BS##B.B#SB...",
        "...BBBB...BBB..."
    )

    // [ID 1505] Ceresmon (ULTIMATE)
    val CERESMON_ROM = arrayOf(
        "........BBBBB...",
        ".......B####SB..",
        "......B######SB.",
        "...BB..BSSSSSB..",
        "..BWBB..BWT#B...",
        ".BSTBWBB#BBBSB..",
        ".B#S##B#####SSB.",
        "B##S##B##B##B#SB",
        "B#BW##TB#B##TB#B",
        "B#B##SB#TB##TB#B",
        "BS#WT#BB#B#T#BTB",
        "B#WB#B..BB#WB.BB",
        "BWBTB..B#BWB..BB",
        ".BBB...B##B..B#B",
        "........BB...B#B",
        "..............B."
    )

    // [ID 1506] Bacchusmon (ULTIMATE)
    val BACCHUSMON_ROM = arrayOf(
        ".##...#.##......",
        ".###.#######....",
        ".#####ST######..",
        "..###ST########.",
        ".#######B#S#####",
        "##S####SWW#S####",
        "#S##SSSSTW#WS###",
        "#S######STW#TS##",
        "###.#.#.#TW#####",
        "#########TW####.",
        "#####SSSTW#####.",
        "####T#WW########",
        "#########S######",
        ".##..###WTS##ST#",
        "......####TTTT#.",
        ".......#######.."
    )

    // [ID 1507] Jupitermon (ULTIMATE)
    val JUPITERMON_ROM = arrayOf(
        "..B.....BB......",
        "..BB.BBB..B.....",
        "..B.BWB.TB......",
        "..BT##.TB.BB....",
        "..BTS.T##BT.B...",
        "...B#T###BBB.B..",
        "..BB###SBBS#BB..",
        ".B##B#SB#B#S#B..",
        ".BS#SBBW###SWB..",
        "..BB#S###BB#BB..",
        ".B#SB###B#SB#B..",
        ".BWSB#S#BWS#B.B.",
        "..BBW#W#SBBBT.B.",
        "...BS#BB#WBTTT.B",
        "..BWTTB.BTWBBBBB",
        "..BBBB...BBB...."
    )

    // [ID 1508] Junomon (ULTIMATE)
    val JUNOMON_ROM = arrayOf(
        ".B.B....B.......",
        "B#BSBBBBSB......",
        "B#B#T##TWBB.....",
        "BT#T###T#T#B....",
        ".B######T##BBB..",
        ".B#########BWSB.",
        ".B########B#SB..",
        ".B..####BB##B...",
        "..B..##B#SBB....",
        "...BBBB##B#B....",
        ".BBSWTSWB.B#B...",
        "BS#BT#TBS#B#B...",
        "BW#B###BW#B##B..",
        ".BB#.B.#BB###B..",
        ".BSW#BB#WSBBBB..",
        ".BBBB..BBB......"
    )

    // [ID 1509] Plutomon (ULTIMATE)
    val PLUTOMON_ROM = arrayOf(
        "................",
        ".#.#..BB.BB.BB..",
        ".BBTBBWTBTWBTB..",
        ".BTTWTWTWTWTTB..",
        ".BTTWTWTWTWTTB..",
        "..BTWTWTWTWTTTB.",
        "..BTTTTTTTTTTTTB",
        ".BTTTTTTTTTTTTTB",
        ".BTTTTTTTTTTTTTB",
        ".BBTTTTTTTTTTTBB",
        "...BTTTTTTTTTB..",
        "..BTTTTTTTTTTTB.",
        "..BWWWWWWWWWWWTB",
        "...BWWWWWWWWWWTB",
        "...BWWBBWWWWBBBB",
        "...BBB..BBBB...."
    )

    // [ID 1510] Venusmon (ULTIMATE)
    val VENUSMON_ROM = arrayOf(
        ".........BBB....",
        "...BB.BBB#T#B...",
        "..B##B###B#TBB..",
        ".B##WSWW##BB.SB.",
        ".BTW..S#WTB.#B..",
        ".BTW####W#TBBTB.",
        ".B#W.####W#B#TB.",
        ".B##.####W#BTB..",
        "..B#B###B#B#TB..",
        "...B#BBB#BB##B..",
        "...B.S#.SB#B#TB.",
        "..B#B####BS.B#B.",
        "..BSB.SB#B#SB#B.",
        "...BB.SB#.BB.B..",
        "..BSBS#BBS.B....",
        "..BBBBB..BBB...."
    )

    // [ID 1511] Titamon (ULTIMATE)
    val TITAMON_ROM = arrayOf(
        "..BB..BB.BBB.BB.",
        ".BWB.BWB.BWBBWB.",
        "B#B.B#B...B#BTB.",
        "BWBBBBBBBBBWBBB.",
        "B#TB###T#BT#B#B.",
        "BBB##TBBBTBBB#B.",
        "BSB##B#B##B####.",
        "##SBBBB#BB#SSB..",
        ".BSB###BSBS##B..",
        ".BBBBBB#SSBBBB..",
        "B###B####SB##B..",
        "B#T#BB.#SBW##B..",
        ".#BB##B#BB#WB...",
        "..BSS#B#BSBBSB..",
        ".B#B#BTBB#B#B#B.",
        ".BBBBBBBBBBBBBB."
    )

    // [ID 1512] Apollomon Whispered (ULTIMATE)
    val APOLLOMON_WHISPERED_ROM = arrayOf(
        ".....BBBBBBBBBB.",
        "...BBSSSSS####B.",
        "..BSSBBBBB#WWB..",
        "..BBB#TTTTBB#BBB",
        ".BB###TBBT#B###B",
        ".B###TB#T#BSW#B.",
        ".BBBBTTS#BSWBWBB",
        ".B##T##BB#WB#BWB",
        "..BBBBB##WB#SBWB",
        "..BWB##BBBS##BB.",
        ".B#B####B###B...",
        "B##B####B.#B....",
        "B.BB.####BB.....",
        ".BB##BB####B....",
        ".B.B#B.B##B.B...",
        ".BBBBB.BBBBBB..."
    )

    // [ID 1513] Ancient Greymon (ULTIMATE)
    val ANCIENT_GREYMON_ROM = arrayOf(
        "................",
        ".........#.BBB..",
        "..BB.BBBB#BT#TB.",
        ".B#TBT#TWTW#TTTB",
        ".B#TWTWTWTW#TTTB",
        ".B#TWTWT#TWT#TTB",
        ".B##TTT#TTTTTTTB",
        ".B#TTTT#TTT#TTTB",
        "..B####TTTT##TB.",
        "...BTTTTTT#TT#TB",
        "....BTT#T##TTTTB",
        "....BTTTSTTSTTB.",
        "...BWWWWSWWWWWB.",
        "..BWWWWWSWBWWWB.",
        "..BWWWWWWB.BWWB.",
        "..BBBBBBB..BBBB."
    )

    // [ID 1514] Ancient Garurumon (ULTIMATE)
    val ANCIENT_GARURUMON_ROM = arrayOf(
        "................",
        "......BB.BB.....",
        ".....BWTBT#B....",
        "..BBBT#TW##TB...",
        "..BTWTWTW##TTBB.",
        "..BT##WT##WTTTTB",
        "..BTTTTT#TT#TTTB",
        "...BTTTTTT#TTBB.",
        "...BTTTT#TTTB...",
        "...BTTT##TT#TB..",
        "...B#TTTTT##TB..",
        "...BTTTTTTTTB...",
        "...BWWWWWWWWB...",
        "..BWWWWWWWSWWB..",
        "..BWWWWBWWWWWB..",
        "..BBBBB.BBBBBB.."
    )

    // [ID 1515] Ancient Beatmon (ULTIMATE)
    val ANCIENT_BEATMON_ROM = arrayOf(
        "................",
        ".#.#..BBB.......",
        ".BB..BW#B.......",
        "..BBBTWTB...BBB.",
        "..BTWTWTWB.BTTTB",
        "...BWTWTWTBTTTTB",
        "...BTTTTTTTTTTTB",
        "...BTTTTTTTTTTB.",
        "..BTTTT#TTT#TB..",
        ".B#TT###TT##TB..",
        ".B#TTTTTT##TB...",
        "..BTTTTTTTTTTB..",
        "..BWWWWWWWWWWWB.",
        ".BWWWBWWWSWWWWB.",
        ".BWBB.BBWWBWWWB.",
        ".BB.....BB.BBBB."
    )

    // [ID 1516] Ancient Irismon (ULTIMATE)
    val ANCIENT_IRISMON_ROM = arrayOf(
        "................",
        "....BBBB........",
        "...BW#WTBBB.....",
        "...BWTW####BBB..",
        "...BWT#TWT#TTTB.",
        "...BWTWTWTWTTTB.",
        "...BTTTTT#TTTB..",
        "....BTTTTTT#TB..",
        ".....BTTTTT##TB.",
        "....BTTT#T#T##TB",
        "...BTTTTT##TT#TB",
        "...BTTTSTTTBBTTB",
        "....BWWWWWB..BBB",
        "...BWWWWWSWB....",
        "...BWWWBWWWB....",
        "....BBB.BBBB...."
    )

    // [ID 1517] Ancient Megatheriumon (ULTIMATE)
    val ANCIENT_MEGATHERIUMON_ROM = arrayOf(
        "................",
        ".....BBBBBB.....",
        "....BTWT###B....",
        "..BBWTW#W#WTB...",
        ".BTTWTWTWTWTTB..",
        ".BTT###TWT#TTB..",
        ".BTTTTT#T###TB..",
        ".B#TTTTTT#T#TB..",
        "..B#TTTTT#T##TB.",
        "..BTT#TT##TTTTB.",
        "..B##TTT##TTTTB.",
        "..BTTTTTSSSTTTB.",
        "...BWWSWWSSWWWTB",
        "...BWWWWWWWWWWTB",
        "...BWWWWWWBWWWB.",
        "...BBBBBBB.BBBB."
    )

    // [ID 1518] Ancient Sphinxmon (ULTIMATE)
    val ANCIENT_SPHINXMON_ROM = arrayOf(
        "................",
        "...BBBBB....BBBB",
        "..BTWTWTBBBBT#TB",
        ".BT#WTWTWT#TTTB.",
        ".BTTWTWTWTWTTTTB",
        ".B#TWTWTWTWTTTTB",
        ".B#TTTT#TTTTTTTB",
        ".BTTTT#TTTTTTTB.",
        ".B####TTT#TTTTB.",
        "..BTTTT####TT#TB",
        "..B#TTTT#T#TT#TB",
        "...BTTTTTTTTTTTB",
        "...BWWWWWWBWWWTB",
        "..BWWWWWWB.BWWTB",
        "..BWWWWWWB.BWWTB",
        "..BBBBBBBB.BBBB."
    )

    // [ID 1519] Ancient Volcamon (ULTIMATE)
    val ANCIENT_VOLCAMON_ROM = arrayOf(
        "................",
        "................",
        "...........BBB..",
        "..BB.BB.BBBTTTB.",
        "..BTBTWBW#WT#TTB",
        "..BTWTWT###TTTTB",
        "..BTTTTTT##TTTTB",
        "...BTTTTT##TTTTB",
        "...BTTTTT##TTTTB",
        "...BTT#TT##TT#TB",
        "...BTTTTT#TTTTTB",
        "...BTTTTSSTTTTB.",
        "...BWWWWWWWWWB..",
        "...BWWWWBBWWWWB.",
        "...BWWWB..BWWWB.",
        "....BBBB..BBBBB."
    )

    // [ID 1520] Ancient Troiamon (ULTIMATE)
    val ANCIENT_TROIAMON_ROM = arrayOf(
        "................",
        "....BBBB........",
        "....BT##B.......",
        "....BTWT#B......",
        "...BWTWTWTBBBB..",
        "..BTWTWTWTWTTB..",
        ".BTTTTTTTTTTB...",
        ".B#T###TTTTTTB..",
        "..BTTTTTTTTTTB..",
        "...BT##T####TTB.",
        "...BTTTT#T#TTTTB",
        "...BTTTTTSSSTTTB",
        "...BWWWWWWWWWWB.",
        "...BWWWWWBBWWWB.",
        "...BWWWWB..BWWB.",
        "...BBBBBB..BBBB."
    )

    // [ID 1521] Ancient Mermaimon (ULTIMATE)
    val ANCIENT_MERMAIMON_ROM = arrayOf(
        "................",
        "......BB........",
        ".....BWB........",
        "...BBTWTBB......",
        "..BTWTWTWB......",
        "..BTWTWT#TBBB...",
        "..BTTTTTTTTTB...",
        "...BT#TTTTTB....",
        "...BTTTTT#B.....",
        "...BTTTTTB......",
        "..BTT##T##B.BBBB",
        "..BTTTTTTTB.BTTB",
        "...BWWWWWWWBWWTB",
        "...BWWWWWWWWWWB.",
        "....BWWWWWWWBB..",
        ".....BBBBBBB...."
    )

    // [ID 1522] Ancient Wisemon (ULTIMATE)
    val ANCIENT_WISEMON_ROM = arrayOf(
        "................",
        ".......#........",
        "..TBBBBTBBBBB.BT",
        "...BWTWTW##TTTB.",
        "...BWTWTWTWTB...",
        "...BWTWTWTWTB...",
        "...BTTTTTTTTB...",
        "...BTTTTTTTTB...",
        "...BTT####TTB...",
        "...B#TT##T##T#..",
        "...BTTTTTTTTB...",
        "...BTTTTSTSTTB..",
        "...BWWWWWWSWWB..",
        "...BWWWWWWWWWB..",
        "...BWWWWWWWWWB..",
        "...BBBBBBBBBBB.."
    )

    // [ID 1523] Kaiser Greymon (ULTIMATE)
    val KAISER_GREYMON_ROM = arrayOf(
        "................",
        "...BB......#....",
        "...BWBBBB.BTB...",
        "...BWTWTWBWTB...",
        "...BW#WTW#WTTBB.",
        "...BWTWTW#WTTTTB",
        "...BTTTTT#TTTTTB",
        "...BT##TTTTTTTTB",
        "..##TTTTT#T###TB",
        "...BTTTT#TTTTTB.",
        "...BTT##TT#TTB..",
        "...BTTTTTTTTB...",
        "...BWWWWWWWWB...",
        "..BWWWWWWWWWWB..",
        "..BWWWWBBWWWWB..",
        "..BBBBB..BBBBB.."
    )

    // [ID 1524] Magna Garurumon (ULTIMATE)
    val MAGNA_GARURUMON_ROM = arrayOf(
        "................",
        "......BBB.......",
        ".....BWT#B.BBB..",
        "....BTWT#TB#TB..",
        "...BWTWT#TWTTTBB",
        "...BWTWT##WTTTTB",
        "...B#TTT#TTT##TB",
        "...BTT##TTTTTBB.",
        "....BTTT#T#TB...",
        "...BT####TTTB...",
        "..B#TT#TT###TB..",
        "..BTTTTTTTTTTB..",
        "...BWWWWWSWWB...",
        "..BWWWWBWWSWWB..",
        "..BWWWB.BWWWWB..",
        "..BBBBB..BBBBB.."
    )

    // [ID 1525] Baihumon (ULTIMATE)
    val BAIHUMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "...B#SB#B.....BW",
        "..#S#SBBTB##B##.",
        ".BBBS#BBSBB.#BS.",
        "...T##W#S.#B.#.B",
        ".##BB##BSTWW...#",
        "...BBW##..B#W#B#",
        "..B#BB#BBSWW.ST#",
        "...#BB#B.TBT.#T.",
        "...B.B###B#B#TT.",
        "....BB#.###B.BBB",
        "....S#BBBBBB.###",
        "..BB##B#.#BB.BBB",
        ".###.B#B##BB#B.B"
    )

    // [ID 1526] Qinglongmon (ULTIMATE)
    val QINGLONGMON_ROM = arrayOf(
        "................",
        "................",
        "...........#....",
        ".....##.#.S#....",
        "...B..S#S#S.#...",
        "...#...ST#T.S#S#",
        "...#BW.S#####TS.",
        "..B#######T#T##.",
        "..BBWWW#T#T#S#T.",
        "..WW#B##B###TT#T",
        ".#####B##S#####.",
        ".##B###BS#T###T#",
        "..B##.#...##.SS.",
        "...B##B....#...#",
        "....###B........",
        ".......B..B....."
    )

    // [ID 1527] Xuanwumon (ULTIMATE)
    val XUANWUMON_ROM = arrayOf(
        "................",
        "................",
        "......BB.B.B....",
        "....##S#####S#B.",
        "...######WW####B",
        "...SB#SW##WW##BB",
        "...S##S##WSWSSBB",
        "...BW##B##B##BB.",
        "....SBBB#SBSSSB.",
        "...##.##T##BT...",
        "..##.####TT##.#T",
        ".####BB####TBB#.",
        "BB#######B#T#T#.",
        ".BBB########BB#.",
        "...BBB#########B",
        "..BB##.BBB##BBB."
    )

    // [ID 1528] Zhuqiaomon (ULTIMATE)
    val ZHUQIAOMON_ROM = arrayOf(
        "................",
        "................",
        "...............B",
        "..B.....S#....#.",
        "..BBB#BB.#S.BS#B",
        "...B###BBSBWWW#.",
        "...B#B##.BW#TT#.",
        "..####B#B#WT#BB.",
        "..#S#BSSBWT###..",
        ".##B.#BB####.#T.",
        "......###T#SBS##",
        "......WTBBSB#WB.",
        ".....BBBWBB##B..",
        ".....B#T#.B###T.",
        ".....BBTBBBSWTB.",
        ".........B...BBB"
    )

    // [ID 1529] Huanglongmon (ULTIMATE)
    val HUANGLONGMON_ROM = arrayOf(
        "................",
        "....BBBBB..BB...",
        "...BWTWTB..BTB..",
        "...BWTWB...BTTB.",
        "...BWTWB...BTTTB",
        "..BTWTWB.T.BTTTB",
        "..B#TTTTBTBTTTTB",
        ".BTTT#T#T#TTT#TB",
        ".B###TT#######TB",
        ".B##TT####TTTTB.",
        "..BBT####T#TTTB.",
        "....BTTTTTTTTTB.",
        "...BWWWWWBWWSWB.",
        "...BWWWWB.BWWWB.",
        "...BWWWWB.BWWWB.",
        "...BBBBBB.BBBBB."
    )

    // [ID 1530] Metal Etemon (ULTIMATE)
    val METAL_ETEMON_ROM = arrayOf(
        ".....BBBBBB.....",
        "....BB.BTWBB....",
        ".BBBBBBBBBBBBBB.",
        "B..BB..T###BB.TB",
        "B.TBBBBBBBBB.TTB",
        "B.BB.BBB.BBBBB#B",
        "B#SBBBSBBBBBT#SB",
        ".BSB..B##SWB#SB.",
        "..BB..T#SSWBBB..",
        ".B.BBBBBBBBBT#B.",
        "B..#SBBBBBT#SBB.",
        "BBBBWBBBBBWBBSB.",
        ".BBBBBBBBBBBBB..",
        "...BBWBBBBBBB...",
        "..BSBBWBB#SBBB..",
        "..BBBBB..BBBBB.."
    )

    // [ID 1531] King Etemon (ULTIMATE)
    val KING_ETEMON_ROM = arrayOf(
        ".......B........",
        ".....BTTTB......",
        "....B.BTB.B.....",
        "....B.B.B.B.....",
        ".....BTTTB......",
        "..BTB.....BTB...",
        ".B.B#####B.B.B..",
        ".B.B.B#B.B##B.B.",
        ".B.B#######B..B.",
        "..B##B.B##B..B..",
        "...B..B...B#B...",
        "..B.B....B..BB..",
        ".B..BSSSSSB..BB.",
        ".BSSB.BB..BSSSSB",
        "...B.B...B..BSB.",
        "...BBBBBBBBBB..."
    )

    // [ID 1532] Ebemon (ULTIMATE)
    val EBEMON_ROM = arrayOf(
        "......BBBBBB....",
        ".....B######B...",
        "....B####..##B..",
        "....B###..#WWB..",
        "S..B.##.##B#B...",
        "WS.B...TWB.#.B.B",
        ".WT.B.WS#B#...B.",
        ".TTTB###B....B#B",
        ".TTT.BBB#BS..B#B",
        "..S.B..##B.S..B.",
        "..SS.B##BB..SS..",
        "....B.B.B#B.....",
        ".....BB.BB......",
        "...BBB#B#BBB....",
        "..B#B##B##B#B...",
        "..BB.BB.BB.BB..."
    )

    // [ID 1533] Belial Vamdemon (ULTIMATE)
    val BELIAL_VAMDEMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        ".......WBWWB....",
        "..BTTBB#B#WTTB..",
        ".WBBBBBB#W###BT.",
        "....B#BB###B....",
        "....#B#BB###....",
        "..BT#BBS#TB#....",
        "..BB##BS#SB##T..",
        "....BB#B.BBB#B..",
        "...BS.#B######B.",
        "..SSS#B#BBB#B#B.",
        "..###B####BB#B..",
        ".#.BSB#.B#BBBB..",
        "..BB##B..B#.B..."
    )

    // [ID 1534] Venom Vamdemon (ULTIMATE)
    val VENOM_VAMDEMON_ROM = arrayOf(
        "BB..BBBB...BB...",
        "B#BB####BBB#B.B.",
        "BTB######B#TBB#B",
        ".B#######BBB.B#B",
        "..B######B...BBB",
        "..B#####WB.BB##B",
        ".BBW...WWBBB###B",
        "BTSB###BBTTSBB#B",
        "BSB#BBBSSBSBB.BB",
        ".BB##BTTS#BSSB.B",
        "BWBW###WW#BBWB..",
        "BSSB######BSSB..",
        "BTB#B#####BTSB..",
        ".B#########BBB..",
        "B#####BB######B.",
        "BBBBBBB.BBBBBBB."
    )

    // [ID 1535] Aegisdramon (ULTIMATE)
    val AEGISDRAMON_ROM = arrayOf(
        ".......#####....",
        "......###T###...",
        "..#####TTTTT##..",
        ".#WSS##########.",
        "..#T###WWS##W###",
        "#..####W###S####",
        "##.#WS#S########",
        "###WSB#########.",
        "#WSS##########..",
        ".##############.",
        ".#TTT##WS#######",
        "..####T##.##S#S#",
        "....###..######.",
        "...##########...",
        "....#####.......",
        "................"
    )

    // [ID 1536] Belphemon Rage Mode (ULTIMATE)
    val BELPHEMON_RAGE_MODE_ROM = arrayOf(
        "................",
        "...BBBBBBBB.BB..",
        "...BWTWT##WBTTB.",
        "..BTWTWT#T#TTTB.",
        ".BTT#TWT#TWTTTTB",
        ".B###TWT##WTTTTB",
        ".BTTTTT###TTTTTB",
        ".BBTTTT####TTTTB",
        "...B#####T##TTTB",
        "..BTTTT#T###TTTB",
        ".B#TT##T#T##TTTB",
        ".BTTTSSTTTTTTTTB",
        ".BWWWWSSWWWWWWTB",
        ".BWWWWWWWWWWWWB.",
        ".BWWWWWBWWWWWWB.",
        ".BBBBBB.BBBBBBB."
    )

    // [ID 1537] Ogudomon (ULTIMATE)
    val OGUDOMON_ROM = arrayOf(
        "................",
        ".......#........",
        "...T..BTB..BB...",
        "..TTBBWTWB.BTT..",
        "...BWTWTWTBTB...",
        "...BWTWTWTWTB...",
        "..BTTTTTTTTTTB..",
        "..BTTTTTTTTTTB..",
        "...BTT###TTTB...",
        "..BTBTTTTTBTTB..",
        ".BTB.BBBBB.BTTB.",
        ".BTB........BTB.",
        "..BWBB....BBWB..",
        ".BWWWWB..BWWWWB.",
        ".BWWWWB..BWBWWB.",
        ".BBBBBB..BB.BBB."
    )

    // [ID 1538] Jesmon X (ULTIMATE)
    val JESMON_X_ROM = arrayOf(
        "................",
        "..BB.....BBB#...",
        "..BTBBBBB##B....",
        "..BTWTWT#TWTBT..",
        "...BWTWT#T#TB...",
        "...BWTWT#TWTTBBB",
        "..BT#TTTTTTTTTTB",
        "..BTTTT#TTTTTTB.",
        "...B####TT#TTB..",
        "...BTTTTTTT##TB.",
        "...BTTTT#TTTTTB.",
        ".BBTTTTTTTTSTTB.",
        ".BSWWWWWSWWWSWB.",
        ".BWBWWWWWWWWWWB.",
        ".BB.BWWBBWWBBBB.",
        ".....BB..BB....."
    )

    // [ID 1539] Gankoomon X (ULTIMATE)
    val GANKOOMON_X_ROM = arrayOf(
        "................",
        ".....BBBBBBB#...",
        "...BBTWTWTWB....",
        "...BWTWTW#WTB...",
        "...BWTWT##WTB...",
        "...BWTW#WTWTTB..",
        "...BTTTTTTTTTB..",
        "...BTTTT#TTTTB..",
        "..BTTTTTT##TTB..",
        ".B#TTTTT##TTTTB.",
        ".B#TT####TTT#TB.",
        ".BTSTTTTTTTSTTB.",
        "..BWWWWWSSWWWB..",
        "...BWSWWWSSWB...",
        "...BWWWBBWWWB...",
        "...BBBB..BBBB..."
    )

    // [ID 1540] Dynasmon X (ULTIMATE)
    val DYNASMON_X_ROM = arrayOf(
        "................",
        ".BB...BB........",
        ".B#B..BTBB...BBB",
        ".BTTBBWTWTB.BTTB",
        "..BT##WTWTWBTTB.",
        "..B##TWT#TWTTTTB",
        "..BTTTTTTTTTTTTB",
        "..BTTTTTT##TTBB.",
        "...BTTTT##TTB...",
        "..BT###TTT#TTB..",
        ".B#TTTTT#T#TTTB.",
        ".BTTTTTTTTTTTTTB",
        "..BWWWWWWWWSBBBB",
        "..BWWWWWSWWB....",
        "..BWWWWWWWWB....",
        "..BBBBBBBBBB...."
    )

    // [ID 1541] Craniummon X (ULTIMATE)
    val CRANIUMMON_X_ROM = arrayOf(
        "................",
        "...BBBBB.BBB....",
        "...BWTWTB##TBB..",
        "....BTWTWTWTTB..",
        ".BBBWT#TWT#TTTBB",
        ".BTTWT#TW##TTTTB",
        ".B#TTTTTT#TTTTTB",
        ".B#TTTTTTTTTT#TB",
        "..BTTTTTT#TTTTB.",
        ".B#TTTTTT##TTTTB",
        ".B#TT#TT##TT##TB",
        ".BTTTTTTTTSTTTTB",
        "..BWWWWWWWSWWWB.",
        "..BWWWWSWWSWWB..",
        "..BWWWWWWBWWWB..",
        "..BBBBBBB.BBBB.."
    )

    // [ID 1542] Sleipmon X (ULTIMATE)
    val SLEIPMON_X_ROM = arrayOf(
        "................",
        ".BB...BBBBB.....",
        ".B#BBBW#W#WBB...",
        "..BTWTWT#TWTB...",
        "..BTWTWT#TWB....",
        ".BT#WTWTWTB.....",
        ".B##TTTTTTTBBBB.",
        ".BBTTTTTTTTTTTTB",
        "...B#TTT##T#TTTB",
        "..B#TTTTT#TTTTB.",
        "..B###TT##T#TTTB",
        ".BTTTTTTTTTTTTTB",
        ".BWWWWWWWWWWWWTB",
        "..BWWWWWWWWWWWB.",
        "..BWWWWWWWWWWWB.",
        "..BBBBBBBBBBBBB."
    )

    // [ID 1543] Duftmon X (ULTIMATE)
    val DUFTMON_X_ROM = arrayOf(
        "................",
        "....BBBBBBBB....",
        "...BWTWTW#WTB...",
        "...BWTWTW#WTTBB.",
        "..BTWTWT#TWTTTB.",
        "..B##TWT#TWTTB..",
        "..BTTTTT#TTTTB..",
        "..BTTTTTTT#TTTB.",
        "...BTTTT#T##TTTB",
        "...B#TT#T#TTT#TB",
        "..BTTTTTTTTTT#TB",
        ".BSTTTTTTTSTTSTB",
        ".BWWWWWWWWWWWWTB",
        ".BWWWWWWWWWBBWTB",
        ".BWWWWWWWWB..BBB",
        ".BBBBBBBBBB....."
    )

    // [ID 1544] Magnamon X (ULTIMATE)
    val MAGNAMON_X_ROM = arrayOf(
        "................",
        "....BBBBB.......",
        "...BWTWTB.......",
        "...BWTWTWBBB....",
        "...BWTW##TWTBBB.",
        "...BWTWT#TWTTTB.",
        "...BTTTT#TTTTTB.",
        "...BTTTTTT##TB..",
        "...BTTTTTT#TB...",
        "...BT##TTTTTB...",
        "..BTT#TT##TTB...",
        "..BTTTTTTTTB....",
        "...BWWWWWWWB....",
        "..BWWWWWSWSWB...",
        "..BWWWBWWWWWB...",
        "..BBBB.BBBBBB..."
    )

    // [ID 1545] Chaosdramon X (ULTIMATE)
    val CHAOSDRAMON_X_ROM = arrayOf(
        "................",
        ".BBBBBBBBB.BBBB.",
        ".B#TW#####BT#TTB",
        ".BTT#TWTWTWTTTTB",
        ".B#TWTWTWTWTTTTB",
        ".BTTWTW##T#TTTB.",
        ".BTTTTTTTTTTTTB.",
        "..BTTTTTTTTTTTB.",
        "...BT###TT#TTTTB",
        "..B#TT#T##TTTTTB",
        ".B#TTTTT#T#TTTTB",
        ".BTTTTTTTSTTTTB.",
        ".BWWWWWWWWWWWB..",
        ".BWWWWWWWWWWWB..",
        ".BWWWWWBWWWWB...",
        ".BBBBBB.BBBBB..."
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
            133 -> POROMON_ROM
            134 -> UPAMON_ROM

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
            251 -> PULSEMON_ROM
            252 -> GAMMAMON_ROM
            253 -> ANGORAMON_ROM
            254 -> JELLYMON_ROM
            255 -> LOOGAMON_ROM
            256 -> SHOUTMON_ROM
            257 -> GUMDRAMON_ROM
            258 -> AGUNIMON_ROM
            259 -> LOBOMON_ROM
            260 -> GAZIMON_ROM

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
            373 -> GEOGREYMON_ROM
            374 -> BULKMON_ROM
            375 -> BETELGAMMAMON_ROM
            376 -> KAUSGAMMAMON_ROM
            377 -> WEZENGAMMAMON_ROM
            378 -> GULUSGAMMAMON_ROM
            379 -> SYMBAREANGORAMON_ROM
            380 -> TESLAJELLYMON_ROM
            381 -> LOOGARMON_ROM
            382 -> PEGASUSMON_ROM
            383 -> NEFERTIMON_ROM
            384 -> STRIKEDRAMON_ROM
            385 -> DOBERMON_ROM
            386 -> SANGLOUPMON_ROM
            387 -> DARKKNIGHTMON_ROM

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
            465 -> RIZEGREYMON_ROM
            466 -> MACHGAOGAMON_ROM
            467 -> BOUTMON_ROM
            468 -> CANOWEISSMON_ROM
            469 -> REGULUSMON_ROM
            470 -> LAMORTEMON_ROM
            471 -> THETISMON_ROM
            472 -> SOLOOGARMON_ROM
            473 -> BAALMON_ROM
            474 -> JEWELBEEMON_ROM
            475 -> DINOBEEMON_ROM
            476 -> SKULLMERAMON_ROM
            477 -> CERBERUSMON_ROM

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
            583 -> SIRIUSMON_ROM
            584 -> DIARBBITMON_ROM
            585 -> AMPHIMON_ROM
            586 -> FENRILOOGAMON_ROM
            587 -> KAZUCHIMON_ROM
            588 -> ARCTURUSMON_ROM
            589 -> SHINEGREYMON_BURST_MODE_ROM
            590 -> MIRAGEGAOGAMON_BURST_MODE_ROM
            591 -> BEELZEMON_BLAST_MODE_ROM
            592 -> OMNIMON_MERCIFUL_MODE_ROM
            1101 -> COTSUCOMON_ROM
            1102 -> KAKKINMON_ROM
            1103 -> SAKUTTOMON_ROM
            1104 -> FUFUMON_ROM
            1105 -> CHIBIMON_ROM
            1106 -> ALGOMON_BABY_I_ROM
            1107 -> ALGOMON_BABY_II_ROM
            1108 -> DOKIMON_ROM
            1109 -> BIBIMON_ROM
            1110 -> WANYAMON_ROM
            1111 -> CHOROMON_ROM
            1112 -> CAPRIMON_COLOR_ROM
            1113 -> BUBBMON_COLOR_ROM
            1114 -> MOCHIMON_COLOR_ROM
            1115 -> MINOMON_COLOR_ROM
            1116 -> BABYDMON_ROM
            1201 -> AGUMON_X_ROM
            1202 -> AGUMON_BLACK_X_ROM
            1203 -> GABUMON_X_ROM
            1204 -> DRACOMON_X_ROM
            1205 -> BEARMON_ROM
            1206 -> GANIMON_ROM
            1207 -> JUNKMON_ROM
            1208 -> POMUMON_ROM
            1209 -> SANGOMON_ROM
            1210 -> SUNARIZAMON_ROM
            1211 -> GHOSTMON_ROM
            1212 -> ALGOMON_CHILD_ROM
            1213 -> TERRIERMON_X_ROM
            1214 -> LOPMON_X_ROM
            1215 -> KOKABUTERIMON_ROM
            1216 -> LIOLLMON_ROM
            1217 -> PALEDRAMON_ROM
            1218 -> ZUBAMON_ROM
            1219 -> KOEMON_ROM
            1220 -> LUDOMON_ROM
            1221 -> HERISSMON_ROM
            1222 -> MORPHOMON_ROM
            1223 -> STRABIMON_ROM
            1224 -> PLOTMON_COLOR_ROM
            1225 -> BOKOMON_ROM
            1301 -> DARKLIZAMON_ROM
            1302 -> DELTAMON_ROM
            1303 -> TORTAMON_ROM
            1304 -> SNIMON_ROM
            1305 -> GOKIMON_ROM
            1306 -> COCKATRIMON_ROM
            1307 -> KIWIMON_ROM
            1308 -> MUSYAMON_ROM
            1309 -> RUKAMON_ROM
            1310 -> AGNIMON_ROM
            1311 -> WOLFMON_ROM
            1312 -> FAIRIMON_ROM
            1313 -> BLITZMON_ROM
            1314 -> CHACKMON_ROM
            1315 -> DUSKMON_ROM
            1316 -> BABOONGAMON_ROM
            1317 -> KAISER_LEOMON_ROM
            1318 -> AEGIOMON_ROM
            1319 -> SHADRAMON_ROM
            1320 -> FLYBEEMON_ROM
            1321 -> HARPYMON_ROM
            1322 -> MANBOMON_ROM
            1323 -> PIPISMON_ROM
            1324 -> BAROMON_ROM
            1325 -> PTERANOMON_ROM
            1326 -> MARIN_BULLMON_ROM
            1327 -> KONGOUMON_ROM
            1328 -> PEACOCKMON_ROM
            1329 -> MOOSEMON_ROM
            1330 -> FLADRAMON_ROM
            1401 -> TONOSAMA_GEKOMON_ROM
            1402 -> DAGOMON_ROM
            1403 -> PUKUMON_ROM
            1404 -> HANGYOMON_ROM
            1405 -> ANOMALOCARIMON_ROM
            1406 -> SCORPIOMON_ROM
            1407 -> BLOSSOMON_ROM
            1408 -> ASURAMON_ROM
            1409 -> AEGIOCHUSMON_ROM
            1410 -> VRITRAMON_ROM
            1411 -> ALDAMON_ROM
            1412 -> BEOWOLFMON_ROM
            1413 -> VELGRMON_ROM
            1414 -> RAIHIMON_ROM
            1415 -> FANTOMON_ROM
            1416 -> MARIN_DEVIMON_ROM
            1417 -> MEGA_SEADRAMON_ROM
            1418 -> WARU_SEADRAMON_ROM
            1419 -> AERO_V_DRAMON_ROM
            1420 -> ARACHNEMON_ROM
            1421 -> LUCEMON_FALLDOWN_MODE_ROM
            1422 -> CERBERUMON_ROM
            1423 -> DURAMON_ROM
            1424 -> MEPHISMON_ROM
            1425 -> MIHIRAMON_ROM
            1426 -> VAJRAMON_ROM
            1427 -> ANDIRAMON_DEVA_ROM
            1428 -> CERBERUMON_WEREWOLF_MODE_ROM
            1429 -> JUSTIMON_BLITZ_ARM_ROM
            1430 -> THUNDERBALLMON_ROM
            1431 -> CRYS_PALEDRAMON_ROM
            1432 -> METAL_FANTOMON_ROM
            1433 -> BLIZZARMON_ROM
            1434 -> ALGOMON_PERFECT_ROM
            1501 -> MARSMON_ROM
            1502 -> MINERVAMON_ROM
            1503 -> NEPTUNEMON_ROM
            1504 -> VULCANUSMON_ROM
            1505 -> CERESMON_ROM
            1506 -> BACCHUSMON_ROM
            1507 -> JUPITERMON_ROM
            1508 -> JUNOMON_ROM
            1509 -> PLUTOMON_ROM
            1510 -> VENUSMON_ROM
            1511 -> TITAMON_ROM
            1512 -> APOLLOMON_WHISPERED_ROM
            1513 -> ANCIENT_GREYMON_ROM
            1514 -> ANCIENT_GARURUMON_ROM
            1515 -> ANCIENT_BEATMON_ROM
            1516 -> ANCIENT_IRISMON_ROM
            1517 -> ANCIENT_MEGATHERIUMON_ROM
            1518 -> ANCIENT_SPHINXMON_ROM
            1519 -> ANCIENT_VOLCAMON_ROM
            1520 -> ANCIENT_TROIAMON_ROM
            1521 -> ANCIENT_MERMAIMON_ROM
            1522 -> ANCIENT_WISEMON_ROM
            1523 -> KAISER_GREYMON_ROM
            1524 -> MAGNA_GARURUMON_ROM
            1525 -> BAIHUMON_ROM
            1526 -> QINGLONGMON_ROM
            1527 -> XUANWUMON_ROM
            1528 -> ZHUQIAOMON_ROM
            1529 -> HUANGLONGMON_ROM
            1530 -> METAL_ETEMON_ROM
            1531 -> KING_ETEMON_ROM
            1532 -> EBEMON_ROM
            1533 -> BELIAL_VAMDEMON_ROM
            1534 -> VENOM_VAMDEMON_ROM
            1535 -> AEGISDRAMON_ROM
            1536 -> BELPHEMON_RAGE_MODE_ROM
            1537 -> OGUDOMON_ROM
            1538 -> JESMON_X_ROM
            1539 -> GANKOOMON_X_ROM
            1540 -> DYNASMON_X_ROM
            1541 -> CRANIUMMON_X_ROM
            1542 -> SLEIPMON_X_ROM
            1543 -> DUFTMON_X_ROM
            1544 -> MAGNAMON_X_ROM
            1545 -> CHAOSDRAMON_X_ROM

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
            133 -> SpritePalette(SpritePalette.rgb(248, 185, 205), SpritePalette.rgb(225, 45, 55), SpritePalette.rgb(255, 215, 65), SpritePalette.rgb(40, 180, 220)) // Poromon
            134 -> SpritePalette(SpritePalette.rgb(252, 245, 175), SpritePalette.rgb(230, 200, 110), SpritePalette.rgb(245, 130, 150), SpritePalette.rgb(25, 25, 30)) // Upamon

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
            251 -> SpritePalette(SpritePalette.rgb(235, 245, 60), SpritePalette.rgb(45, 215, 235), SpritePalette.rgb(35, 40, 48), SpritePalette.rgb(255, 255, 255)) // Pulsemon
            252 -> SpritePalette(SpritePalette.rgb(248, 250, 255), SpritePalette.rgb(235, 230, 215), SpritePalette.rgb(225, 35, 45), SpritePalette.rgb(30, 45, 95)) // Gammamon
            253 -> SpritePalette(SpritePalette.rgb(215, 195, 160), SpritePalette.rgb(248, 242, 230), SpritePalette.rgb(150, 95, 60), SpritePalette.rgb(55, 165, 85)) // Angoramon
            254 -> SpritePalette(SpritePalette.rgb(110, 220, 240), SpritePalette.rgb(175, 240, 255), SpritePalette.rgb(250, 220, 75), SpritePalette.rgb(140, 65, 180)) // Jellymon
            255 -> SpritePalette(SpritePalette.rgb(65, 55, 80), SpritePalette.rgb(220, 30, 45), SpritePalette.rgb(210, 215, 230), SpritePalette.rgb(250, 205, 45)) // Loogamon
            256 -> SpritePalette(SpritePalette.rgb(235, 45, 40), SpritePalette.rgb(255, 225, 45), SpritePalette.rgb(190, 195, 205), SpritePalette.rgb(255, 255, 255)) // Shoutmon
            257 -> SpritePalette(SpritePalette.rgb(135, 75, 185), SpritePalette.rgb(245, 185, 40), SpritePalette.rgb(220, 40, 50), SpritePalette.rgb(250, 215, 70)) // Gumdramon
            258 -> SpritePalette(SpritePalette.rgb(215, 55, 35), SpritePalette.rgb(250, 205, 55), SpritePalette.rgb(160, 30, 25), SpritePalette.rgb(45, 185, 245)) // Agunimon
            259 -> SpritePalette(SpritePalette.rgb(45, 95, 190), SpritePalette.rgb(230, 235, 245), SpritePalette.rgb(60, 195, 240), SpritePalette.rgb(245, 190, 45)) // Lobomon
            260 -> SpritePalette(SpritePalette.rgb(75, 78, 88), SpritePalette.rgb(165, 145, 180), SpritePalette.rgb(240, 240, 245), SpritePalette.rgb(220, 30, 35)) // Gazimon

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
            373 -> SpritePalette(SpritePalette.rgb(240, 125, 35), SpritePalette.rgb(195, 35, 40), SpritePalette.rgb(45, 55, 80), SpritePalette.rgb(45, 190, 110)) // GeoGreymon
            374 -> SpritePalette(SpritePalette.rgb(245, 225, 45), SpritePalette.rgb(45, 200, 235), SpritePalette.rgb(65, 70, 80), SpritePalette.rgb(220, 35, 45)) // Bulkmon
            375 -> SpritePalette(SpritePalette.rgb(225, 45, 45), SpritePalette.rgb(255, 210, 50), SpritePalette.rgb(35, 40, 50), SpritePalette.rgb(40, 195, 235)) // BetelGammamon
            376 -> SpritePalette(SpritePalette.rgb(45, 105, 210), SpritePalette.rgb(70, 210, 240), SpritePalette.rgb(215, 220, 235), SpritePalette.rgb(245, 205, 45)) // KausGammamon
            377 -> SpritePalette(SpritePalette.rgb(75, 135, 75), SpritePalette.rgb(45, 85, 45), SpritePalette.rgb(165, 115, 60), SpritePalette.rgb(225, 40, 45)) // WezenGammamon
            378 -> SpritePalette(SpritePalette.rgb(35, 35, 42), SpritePalette.rgb(115, 65, 145), SpritePalette.rgb(180, 185, 195), SpritePalette.rgb(245, 215, 55)) // GulusGammamon
            379 -> SpritePalette(SpritePalette.rgb(180, 125, 75), SpritePalette.rgb(135, 35, 55), SpritePalette.rgb(240, 195, 85), SpritePalette.rgb(50, 175, 100)) // SymbareAngoramon
            380 -> SpritePalette(SpritePalette.rgb(85, 205, 215), SpritePalette.rgb(250, 230, 65), SpritePalette.rgb(35, 50, 85), SpritePalette.rgb(155, 70, 195)) // TeslaJellymon
            381 -> SpritePalette(SpritePalette.rgb(55, 58, 68), SpritePalette.rgb(50, 185, 235), SpritePalette.rgb(225, 230, 242), SpritePalette.rgb(245, 200, 45)) // Loogarmon
            382 -> SpritePalette(SpritePalette.rgb(250, 250, 255), SpritePalette.rgb(245, 210, 55), SpritePalette.rgb(100, 180, 240), SpritePalette.rgb(35, 110, 215)) // Pegasusmon
            383 -> SpritePalette(SpritePalette.rgb(245, 215, 60), SpritePalette.rgb(40, 75, 160), SpritePalette.rgb(215, 225, 240), SpritePalette.rgb(215, 35, 45)) // Nefertimon
            384 -> SpritePalette(SpritePalette.rgb(130, 140, 155), SpritePalette.rgb(215, 45, 40), SpritePalette.rgb(75, 80, 90), SpritePalette.rgb(245, 205, 50)) // Strikedramon
            385 -> SpritePalette(SpritePalette.rgb(40, 42, 48), SpritePalette.rgb(195, 200, 210), SpritePalette.rgb(195, 35, 45), SpritePalette.rgb(240, 190, 40)) // Dobermon
            386 -> SpritePalette(SpritePalette.rgb(48, 45, 58), SpritePalette.rgb(195, 25, 40), SpritePalette.rgb(160, 165, 180), SpritePalette.rgb(245, 235, 120)) // Sangloupmon
            387 -> SpritePalette(SpritePalette.rgb(35, 36, 42), SpritePalette.rgb(215, 30, 45), SpritePalette.rgb(235, 195, 55), SpritePalette.rgb(145, 50, 185)) // DarkKnightmon

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
            465 -> SpritePalette(SpritePalette.rgb(235, 120, 30), SpritePalette.rgb(205, 215, 230), SpritePalette.rgb(200, 35, 40), SpritePalette.rgb(45, 205, 240)) // RizeGreymon
            466 -> SpritePalette(SpritePalette.rgb(45, 80, 165), SpritePalette.rgb(195, 205, 220), SpritePalette.rgb(240, 240, 248), SpritePalette.rgb(245, 195, 45)) // MachGaogamon
            467 -> SpritePalette(SpritePalette.rgb(245, 220, 50), SpritePalette.rgb(35, 38, 45), SpritePalette.rgb(45, 160, 235), SpritePalette.rgb(220, 35, 45)) // Boutmon
            468 -> SpritePalette(SpritePalette.rgb(245, 248, 255), SpritePalette.rgb(245, 215, 60), SpritePalette.rgb(215, 40, 45), SpritePalette.rgb(50, 140, 235)) // Canoweissmon
            469 -> SpritePalette(SpritePalette.rgb(36, 36, 44), SpritePalette.rgb(205, 30, 40), SpritePalette.rgb(120, 45, 140), SpritePalette.rgb(245, 205, 45)) // Regulusmon
            470 -> SpritePalette(SpritePalette.rgb(155, 105, 60), SpritePalette.rgb(235, 235, 240), SpritePalette.rgb(75, 75, 85), SpritePalette.rgb(235, 110, 35)) // Lamortemon
            471 -> SpritePalette(SpritePalette.rgb(45, 145, 165), SpritePalette.rgb(175, 225, 75), SpritePalette.rgb(220, 230, 245), SpritePalette.rgb(145, 65, 175)) // Thetismon
            472 -> SpritePalette(SpritePalette.rgb(70, 55, 95), SpritePalette.rgb(55, 175, 235), SpritePalette.rgb(225, 230, 242), SpritePalette.rgb(245, 200, 50)) // Soloogarmon
            473 -> SpritePalette(SpritePalette.rgb(65, 45, 85), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(200, 205, 220), SpritePalette.rgb(245, 195, 45)) // Baalmon
            474 -> SpritePalette(SpritePalette.rgb(45, 185, 95), SpritePalette.rgb(215, 40, 50), SpritePalette.rgb(245, 215, 55), SpritePalette.rgb(0, 215, 245)) // JewelBeemon
            475 -> SpritePalette(SpritePalette.rgb(42, 45, 52), SpritePalette.rgb(235, 40, 45), SpritePalette.rgb(240, 200, 50), SpritePalette.rgb(45, 185, 95)) // Dinobeemon
            476 -> SpritePalette(SpritePalette.rgb(50, 115, 225), SpritePalette.rgb(175, 185, 200), SpritePalette.rgb(60, 65, 75), SpritePalette.rgb(225, 40, 45)) // SkullMeramon
            477 -> SpritePalette(SpritePalette.rgb(48, 50, 60), SpritePalette.rgb(180, 190, 205), SpritePalette.rgb(215, 35, 40), SpritePalette.rgb(245, 200, 45)) // Cerberusmon

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
            583 -> SpritePalette(SpritePalette.rgb(248, 250, 255), SpritePalette.rgb(220, 35, 45), SpritePalette.rgb(245, 210, 55), SpritePalette.rgb(40, 205, 245)) // Siriusmon
            584 -> SpritePalette(SpritePalette.rgb(245, 245, 252), SpritePalette.rgb(45, 85, 185), SpritePalette.rgb(215, 225, 240), SpritePalette.rgb(220, 35, 45)) // Diarbbitmon
            585 -> SpritePalette(SpritePalette.rgb(215, 225, 240), SpritePalette.rgb(45, 195, 225), SpritePalette.rgb(40, 55, 85), SpritePalette.rgb(245, 210, 50)) // Amphimon
            586 -> SpritePalette(SpritePalette.rgb(225, 230, 242), SpritePalette.rgb(45, 170, 235), SpritePalette.rgb(240, 195, 45), SpritePalette.rgb(225, 35, 45)) // Fenriloogamon
            587 -> SpritePalette(SpritePalette.rgb(245, 215, 50), SpritePalette.rgb(45, 175, 95), SpritePalette.rgb(50, 55, 65), SpritePalette.rgb(0, 225, 255)) // Kazuchimon
            588 -> SpritePalette(SpritePalette.rgb(35, 25, 38), SpritePalette.rgb(195, 25, 45), SpritePalette.rgb(165, 45, 215), SpritePalette.rgb(255, 175, 45)) // Arcturusmon: GRB Obsidian + Abyssal Crimson + Toxic Plasma + Amber
            589 -> SpritePalette(SpritePalette.rgb(235, 55, 35), SpritePalette.rgb(255, 165, 40), SpritePalette.rgb(245, 240, 220), SpritePalette.rgb(0, 235, 255)) // ShineGreymon Burst Mode
            590 -> SpritePalette(SpritePalette.rgb(55, 135, 235), SpritePalette.rgb(145, 220, 255), SpritePalette.rgb(245, 245, 255), SpritePalette.rgb(245, 205, 45)) // MirageGaogamon Burst Mode
            591 -> SpritePalette(SpritePalette.rgb(38, 38, 48), SpritePalette.rgb(215, 35, 45), SpritePalette.rgb(215, 220, 235), SpritePalette.rgb(45, 195, 105)) // Beelzemon Blast Mode
            592 -> SpritePalette(SpritePalette.rgb(250, 252, 255), SpritePalette.rgb(165, 215, 250), SpritePalette.rgb(245, 215, 60), SpritePalette.rgb(0, 220, 255)) // Omnimon Merciful Mode
            1101 -> SpritePalette(SpritePalette.rgb(232, 234, 238), SpritePalette.rgb(172, 178, 188), SpritePalette.rgb(42, 182, 162), SpritePalette.rgb(32, 182, 62)) // Cotsucomon
            1102 -> SpritePalette(SpritePalette.rgb(206, 212, 222), SpritePalette.rgb(146, 156, 172), SpritePalette.rgb(226, 46, 56), SpritePalette.rgb(22, 22, 26)) // Kakkinmon
            1103 -> SpritePalette(SpritePalette.rgb(222, 226, 236), SpritePalette.rgb(152, 162, 176), SpritePalette.rgb(246, 202, 46), SpritePalette.rgb(222, 42, 42)) // Sakuttomon
            1104 -> SpritePalette(SpritePalette.rgb(252, 186, 206), SpritePalette.rgb(226, 136, 166), SpritePalette.rgb(186, 142, 216), SpritePalette.rgb(36, 32, 46)) // Fufumon
            1105 -> SpritePalette(SpritePalette.rgb(66, 136, 226), SpritePalette.rgb(42, 96, 186), SpritePalette.rgb(246, 246, 252), SpritePalette.rgb(226, 42, 52)) // Chibimon
            1106 -> SpritePalette(SpritePalette.rgb(126, 202, 52), SpritePalette.rgb(76, 136, 32), SpritePalette.rgb(226, 36, 46), SpritePalette.rgb(246, 226, 52)) // Algomon Baby I
            1107 -> SpritePalette(SpritePalette.rgb(112, 186, 46), SpritePalette.rgb(66, 122, 26), SpritePalette.rgb(222, 52, 42), SpritePalette.rgb(246, 212, 46)) // Algomon Baby II
            1108 -> SpritePalette(SpritePalette.rgb(180, 180, 180), SpritePalette.rgb(33, 255, 0), SpritePalette.rgb(220, 220, 220), SpritePalette.rgb(0, 0, 0)) // Dokimon
            1109 -> SpritePalette(SpritePalette.rgb(245, 220, 45), SpritePalette.rgb(45, 48, 55), SpritePalette.rgb(45, 215, 235), SpritePalette.rgb(225, 45, 45)) // Bibimon
            1110 -> SpritePalette(SpritePalette.rgb(86, 176, 236), SpritePalette.rgb(46, 122, 196), SpritePalette.rgb(248, 248, 246), SpritePalette.rgb(42, 92, 182)) // Wanyamon
            1111 -> SpritePalette(SpritePalette.rgb(166, 172, 182), SpritePalette.rgb(112, 116, 126), SpritePalette.rgb(246, 246, 252), SpritePalette.rgb(236, 42, 42)) // Choromon
            1112 -> SpritePalette(SpritePalette.rgb(82, 85, 156), SpritePalette.rgb(197, 215, 239), SpritePalette.rgb(173, 182, 189), SpritePalette.rgb(0, 0, 0)) // Caprimon Color
            1113 -> SpritePalette(SpritePalette.rgb(192, 246, 216), SpritePalette.rgb(132, 216, 186), SpritePalette.rgb(96, 182, 152), SpritePalette.rgb(26, 56, 42)) // Bubbmon Color
            1114 -> SpritePalette(SpritePalette.rgb(239, 190, 197), SpritePalette.rgb(247, 215, 214), SpritePalette.rgb(49, 0, 0), SpritePalette.rgb(49, 0, 0)) // Mochimon Color
            1115 -> SpritePalette(SpritePalette.rgb(169, 209, 153), SpritePalette.rgb(194, 222, 163), SpritePalette.rgb(208, 136, 93), SpritePalette.rgb(0, 0, 0)) // Minomon Color
            1116 -> SpritePalette(SpritePalette.rgb(92, 196, 82), SpritePalette.rgb(56, 142, 52), SpritePalette.rgb(246, 196, 52), SpritePalette.rgb(232, 46, 46)) // Babydmon
            1201 -> SpritePalette(SpritePalette.rgb(245, 140, 30), SpritePalette.rgb(205, 100, 20), SpritePalette.rgb(50, 175, 230), SpritePalette.rgb(40, 140, 220)) // Agumon X
            1202 -> SpritePalette(SpritePalette.rgb(56, 60, 68), SpritePalette.rgb(36, 38, 44), SpritePalette.rgb(226, 36, 46), SpritePalette.rgb(250, 202, 42)) // Agumon (Black) X
            1203 -> SpritePalette(SpritePalette.rgb(246, 243, 236), SpritePalette.rgb(56, 116, 212), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(226, 46, 56)) // Gabumon X
            1204 -> SpritePalette(SpritePalette.rgb(62, 162, 236), SpritePalette.rgb(36, 106, 186), SpritePalette.rgb(250, 206, 52), SpritePalette.rgb(232, 42, 42)) // Dracomon X
            1205 -> SpritePalette(SpritePalette.rgb(92, 77, 186), SpritePalette.rgb(166, 160, 147), SpritePalette.rgb(111, 100, 81), SpritePalette.rgb(0, 0, 0)) // Bearmon
            1206 -> SpritePalette(SpritePalette.rgb(123, 158, 214), SpritePalette.rgb(156, 186, 230), SpritePalette.rgb(197, 215, 247), SpritePalette.rgb(8, 4, 33)) // Ganimon
            1207 -> SpritePalette(SpritePalette.rgb(176, 126, 82), SpritePalette.rgb(126, 86, 52), SpritePalette.rgb(76, 186, 212), SpritePalette.rgb(246, 206, 52)) // Junkmon
            1208 -> SpritePalette(SpritePalette.rgb(246, 216, 66), SpritePalette.rgb(116, 196, 62), SpritePalette.rgb(222, 52, 112), SpritePalette.rgb(36, 32, 36)) // Pomumon
            1209 -> SpritePalette(SpritePalette.rgb(236, 86, 96), SpritePalette.rgb(56, 186, 196), SpritePalette.rgb(246, 246, 252), SpritePalette.rgb(42, 52, 82)) // Sangomon
            1210 -> SpritePalette(SpritePalette.rgb(226, 186, 116), SpritePalette.rgb(176, 136, 76), SpritePalette.rgb(46, 202, 222), SpritePalette.rgb(232, 52, 42)) // Sunarizamon
            1211 -> SpritePalette(SpritePalette.rgb(216, 236, 252), SpritePalette.rgb(72, 86, 136), SpritePalette.rgb(255, 196, 46), SpritePalette.rgb(236, 46, 56)) // Ghostmon
            1212 -> SpritePalette(SpritePalette.rgb(96, 166, 46), SpritePalette.rgb(52, 106, 32), SpritePalette.rgb(226, 42, 46), SpritePalette.rgb(250, 222, 52)) // Algomon Child
            1213 -> SpritePalette(SpritePalette.rgb(248, 246, 236), SpritePalette.rgb(66, 156, 76), SpritePalette.rgb(46, 186, 122), SpritePalette.rgb(62, 136, 86)) // Terriermon X
            1214 -> SpritePalette(SpritePalette.rgb(156, 106, 72), SpritePalette.rgb(246, 232, 206), SpritePalette.rgb(232, 116, 152), SpritePalette.rgb(76, 146, 96)) // Lopmon X
            1215 -> SpritePalette(SpritePalette.rgb(62, 76, 156), SpritePalette.rgb(36, 46, 106), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(226, 42, 42)) // KoKabuterimon
            1216 -> SpritePalette(SpritePalette.rgb(254, 197, 35), SpritePalette.rgb(223, 165, 54), SpritePalette.rgb(255, 217, 96), SpritePalette.rgb(0, 0, 0)) // Liollmon
            1217 -> SpritePalette(SpritePalette.rgb(116, 192, 242), SpritePalette.rgb(66, 136, 196), SpritePalette.rgb(246, 252, 255), SpritePalette.rgb(242, 206, 46)) // Paledramon
            1218 -> SpritePalette(SpritePalette.rgb(250, 206, 46), SpritePalette.rgb(206, 152, 26), SpritePalette.rgb(226, 42, 46), SpritePalette.rgb(42, 152, 232)) // Zubamon
            1219 -> SpritePalette(SpritePalette.rgb(82, 153, 49), SpritePalette.rgb(106, 170, 74), SpritePalette.rgb(245, 224, 189), SpritePalette.rgb(0, 0, 0)) // Koemon
            1220 -> SpritePalette(SpritePalette.rgb(206, 216, 226), SpritePalette.rgb(46, 86, 176), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(226, 42, 42)) // Ludomon
            1221 -> SpritePalette(SpritePalette.rgb(246, 240, 222), SpritePalette.rgb(86, 62, 126), SpritePalette.rgb(250, 216, 46), SpritePalette.rgb(42, 146, 226)) // Herissmon
            1222 -> SpritePalette(SpritePalette.rgb(96, 186, 242), SpritePalette.rgb(176, 156, 226), SpritePalette.rgb(250, 212, 52), SpritePalette.rgb(52, 112, 216)) // Morphomon
            1223 -> SpritePalette(SpritePalette.rgb(56, 61, 61), SpritePalette.rgb(137, 124, 180), SpritePalette.rgb(167, 156, 203), SpritePalette.rgb(0, 0, 0)) // Strabimon
            1224 -> SpritePalette(SpritePalette.rgb(248, 242, 222), SpritePalette.rgb(192, 136, 82), SpritePalette.rgb(186, 196, 212), SpritePalette.rgb(46, 132, 212)) // Plotmon Color
            1225 -> SpritePalette(SpritePalette.rgb(240, 225, 180), SpritePalette.rgb(175, 110, 50), SpritePalette.rgb(230, 85, 115), SpritePalette.rgb(65, 160, 80)) // Bokomon
            1301 -> SpritePalette(SpritePalette.rgb(82, 33, 16), SpritePalette.rgb(58, 16, 0), SpritePalette.rgb(132, 49, 33), SpritePalette.rgb(0, 0, 0)) // Darklizamon
            1302 -> SpritePalette(SpritePalette.rgb(41, 101, 181), SpritePalette.rgb(123, 130, 123), SpritePalette.rgb(255, 251, 206), SpritePalette.rgb(8, 4, 33)) // Deltamon
            1303 -> SpritePalette(SpritePalette.rgb(230, 215, 33), SpritePalette.rgb(239, 231, 189), SpritePalette.rgb(156, 130, 58), SpritePalette.rgb(25, 20, 0)) // Tortamon
            1304 -> SpritePalette(SpritePalette.rgb(58, 123, 66), SpritePalette.rgb(123, 197, 132), SpritePalette.rgb(0, 132, 49), SpritePalette.rgb(0, 0, 0)) // Snimon
            1305 -> SpritePalette(SpritePalette.rgb(74, 41, 16), SpritePalette.rgb(82, 66, 58), SpritePalette.rgb(90, 74, 58), SpritePalette.rgb(0, 0, 0)) // Gokimon
            1306 -> SpritePalette(SpritePalette.rgb(255, 251, 197), SpritePalette.rgb(239, 231, 148), SpritePalette.rgb(222, 210, 107), SpritePalette.rgb(0, 0, 0)) // Cockatrimon
            1307 -> SpritePalette(SpritePalette.rgb(206, 198, 189), SpritePalette.rgb(33, 28, 25), SpritePalette.rgb(140, 8, 49), SpritePalette.rgb(25, 20, 0)) // Kiwimon
            1308 -> SpritePalette(SpritePalette.rgb(255, 32, 16), SpritePalette.rgb(165, 36, 24), SpritePalette.rgb(173, 146, 99), SpritePalette.rgb(0, 0, 0)) // Musyamon
            1309 -> SpritePalette(SpritePalette.rgb(49, 69, 156), SpritePalette.rgb(181, 194, 206), SpritePalette.rgb(214, 223, 230), SpritePalette.rgb(8, 4, 33)) // Rukamon
            1310 -> SpritePalette(SpritePalette.rgb(226, 52, 42), SpritePalette.rgb(250, 196, 46), SpritePalette.rgb(56, 52, 62), SpritePalette.rgb(46, 192, 242)) // Agnimon
            1311 -> SpritePalette(SpritePalette.rgb(226, 236, 246), SpritePalette.rgb(46, 96, 196), SpritePalette.rgb(246, 196, 52), SpritePalette.rgb(226, 46, 52)) // Wolfmon
            1312 -> SpritePalette(SpritePalette.rgb(71, 168, 178), SpritePalette.rgb(183, 232, 230), SpritePalette.rgb(146, 178, 82), SpritePalette.rgb(0, 0, 0)) // Fairimon
            1313 -> SpritePalette(SpritePalette.rgb(93, 90, 166), SpritePalette.rgb(253, 214, 60), SpritePalette.rgb(171, 168, 211), SpritePalette.rgb(0, 0, 0)) // Blitzmon
            1314 -> SpritePalette(SpritePalette.rgb(246, 248, 252), SpritePalette.rgb(86, 186, 226), SpritePalette.rgb(246, 176, 46), SpritePalette.rgb(36, 136, 206)) // Chackmon
            1315 -> SpritePalette(SpritePalette.rgb(56, 45, 75), SpritePalette.rgb(195, 44, 45), SpritePalette.rgb(95, 55, 104), SpritePalette.rgb(245, 205, 70)) // Duskmon
            1316 -> SpritePalette(SpritePalette.rgb(146, 152, 162), SpritePalette.rgb(222, 56, 46), SpritePalette.rgb(106, 86, 76), SpritePalette.rgb(250, 212, 52)) // Baboongamon
            1317 -> SpritePalette(SpritePalette.rgb(46, 50, 58), SpritePalette.rgb(96, 62, 122), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(226, 46, 52)) // Kaiser Leomon
            1318 -> SpritePalette(SpritePalette.rgb(171, 170, 170), SpritePalette.rgb(215, 215, 215), SpritePalette.rgb(99, 99, 100), SpritePalette.rgb(1, 0, 0)) // Aegiomon
            1319 -> SpritePalette(SpritePalette.rgb(232, 56, 42), SpritePalette.rgb(156, 36, 42), SpritePalette.rgb(246, 212, 56), SpritePalette.rgb(42, 182, 232)) // Shadramon
            1320 -> SpritePalette(SpritePalette.rgb(222, 41, 25), SpritePalette.rgb(140, 25, 8), SpritePalette.rgb(181, 25, 16), SpritePalette.rgb(0, 0, 0)) // Flybeemon
            1321 -> SpritePalette(SpritePalette.rgb(214, 214, 214), SpritePalette.rgb(197, 197, 197), SpritePalette.rgb(189, 58, 58), SpritePalette.rgb(0, 0, 0)) // Harpymon
            1322 -> SpritePalette(SpritePalette.rgb(76, 186, 216), SpritePalette.rgb(246, 206, 52), SpritePalette.rgb(246, 248, 252), SpritePalette.rgb(36, 36, 46)) // Manbomon
            1323 -> SpritePalette(SpritePalette.rgb(197, 8, 8), SpritePalette.rgb(0, 58, 140), SpritePalette.rgb(8, 49, 99), SpritePalette.rgb(0, 0, 0)) // Pipismon
            1324 -> SpritePalette(SpritePalette.rgb(49, 41, 8), SpritePalette.rgb(214, 181, 58), SpritePalette.rgb(115, 25, 8), SpritePalette.rgb(0, 0, 0)) // Baromon
            1325 -> SpritePalette(SpritePalette.rgb(74, 82, 115), SpritePalette.rgb(107, 107, 156), SpritePalette.rgb(49, 49, 74), SpritePalette.rgb(0, 0, 0)) // Pteranomon
            1326 -> SpritePalette(SpritePalette.rgb(255, 32, 173), SpritePalette.rgb(206, 32, 148), SpritePalette.rgb(189, 199, 206), SpritePalette.rgb(0, 0, 0)) // Marin Bullmon
            1327 -> SpritePalette(SpritePalette.rgb(191, 159, 39), SpritePalette.rgb(148, 127, 44), SpritePalette.rgb(238, 225, 187), SpritePalette.rgb(0, 0, 0)) // Kongoumon
            1328 -> SpritePalette(SpritePalette.rgb(224, 236, 186), SpritePalette.rgb(206, 239, 118), SpritePalette.rgb(47, 77, 132), SpritePalette.rgb(0, 0, 0)) // Peacockmon
            1329 -> SpritePalette(SpritePalette.rgb(52, 66, 126), SpritePalette.rgb(96, 196, 236), SpritePalette.rgb(216, 226, 242), SpritePalette.rgb(246, 196, 46)) // Moosemon
            1330 -> SpritePalette(SpritePalette.rgb(253, 245, 156), SpritePalette.rgb(91, 89, 88), SpritePalette.rgb(199, 75, 53), SpritePalette.rgb(0, 0, 0)) // Fladramon
            1401 -> SpritePalette(SpritePalette.rgb(140, 0, 0), SpritePalette.rgb(206, 57, 58), SpritePalette.rgb(255, 113, 115), SpritePalette.rgb(0, 32, 8)) // Tonosama Gekomon
            1402 -> SpritePalette(SpritePalette.rgb(90, 89, 156), SpritePalette.rgb(165, 162, 206), SpritePalette.rgb(181, 0, 0), SpritePalette.rgb(8, 4, 33)) // Dagomon
            1403 -> SpritePalette(SpritePalette.rgb(33, 77, 132), SpritePalette.rgb(189, 186, 189), SpritePalette.rgb(214, 215, 214), SpritePalette.rgb(0, 0, 0)) // Pukumon
            1404 -> SpritePalette(SpritePalette.rgb(49, 89, 173), SpritePalette.rgb(206, 231, 255), SpritePalette.rgb(173, 0, 0), SpritePalette.rgb(8, 4, 33)) // Hangyomon
            1405 -> SpritePalette(SpritePalette.rgb(222, 202, 148), SpritePalette.rgb(197, 166, 82), SpritePalette.rgb(181, 130, 25), SpritePalette.rgb(25, 20, 0)) // Anomalocarimon
            1406 -> SpritePalette(SpritePalette.rgb(42, 96, 186), SpritePalette.rgb(146, 156, 172), SpritePalette.rgb(246, 126, 36), SpritePalette.rgb(250, 216, 52)) // Scorpiomon
            1407 -> SpritePalette(SpritePalette.rgb(255, 255, 33), SpritePalette.rgb(255, 255, 197), SpritePalette.rgb(255, 223, 0), SpritePalette.rgb(0, 32, 8)) // Blossomon
            1408 -> SpritePalette(SpritePalette.rgb(148, 146, 148), SpritePalette.rgb(206, 206, 206), SpritePalette.rgb(82, 81, 82), SpritePalette.rgb(0, 0, 0)) // Asuramon
            1409 -> SpritePalette(SpritePalette.rgb(34, 16, 6), SpritePalette.rgb(165, 162, 162), SpritePalette.rgb(209, 207, 207), SpritePalette.rgb(1, 0, 0)) // Aegiochusmon
            1410 -> SpritePalette(SpritePalette.rgb(232, 52, 36), SpritePalette.rgb(62, 50, 52), SpritePalette.rgb(250, 196, 46), SpritePalette.rgb(52, 202, 246)) // Vritramon
            1411 -> SpritePalette(SpritePalette.rgb(226, 46, 36), SpritePalette.rgb(242, 182, 46), SpritePalette.rgb(222, 226, 236), SpritePalette.rgb(46, 186, 236)) // Aldamon
            1412 -> SpritePalette(SpritePalette.rgb(226, 236, 246), SpritePalette.rgb(42, 86, 186), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(226, 42, 46)) // Beowolfmon
            1413 -> SpritePalette(SpritePalette.rgb(46, 42, 56), SpritePalette.rgb(226, 36, 46), SpritePalette.rgb(126, 66, 156), SpritePalette.rgb(250, 206, 46)) // Velgrmon
            1414 -> SpritePalette(SpritePalette.rgb(246, 202, 46), SpritePalette.rgb(52, 54, 62), SpritePalette.rgb(52, 216, 246), SpritePalette.rgb(42, 212, 242)) // Raihimon
            1415 -> SpritePalette(SpritePalette.rgb(107, 105, 99), SpritePalette.rgb(132, 24, 16), SpritePalette.rgb(173, 53, 41), SpritePalette.rgb(0, 0, 0)) // Fantomon
            1416 -> SpritePalette(SpritePalette.rgb(33, 45, 132), SpritePalette.rgb(99, 125, 189), SpritePalette.rgb(66, 85, 165), SpritePalette.rgb(8, 4, 33)) // Marin Devimon
            1417 -> SpritePalette(SpritePalette.rgb(206, 223, 230), SpritePalette.rgb(214, 186, 107), SpritePalette.rgb(82, 113, 107), SpritePalette.rgb(0, 0, 0)) // Mega Seadramon
            1418 -> SpritePalette(SpritePalette.rgb(176, 36, 46), SpritePalette.rgb(52, 54, 62), SpritePalette.rgb(242, 192, 46), SpritePalette.rgb(236, 42, 42)) // Waru Seadramon
            1419 -> SpritePalette(SpritePalette.rgb(222, 219, 222), SpritePalette.rgb(189, 186, 189), SpritePalette.rgb(82, 65, 189), SpritePalette.rgb(8, 4, 33)) // Aero V-dramon
            1420 -> SpritePalette(SpritePalette.rgb(205, 35, 45), SpritePalette.rgb(52, 54, 62), SpritePalette.rgb(242, 242, 246), SpritePalette.rgb(245, 195, 45)) // Arachnemon
            1421 -> SpritePalette(SpritePalette.rgb(246, 244, 236), SpritePalette.rgb(46, 46, 56), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(222, 36, 46)) // Lucemon Falldown Mode
            1422 -> SpritePalette(SpritePalette.rgb(85, 86, 90), SpritePalette.rgb(40, 42, 44), SpritePalette.rgb(58, 58, 63), SpritePalette.rgb(0, 0, 0)) // Cerberumon
            1423 -> SpritePalette(SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(212, 222, 232), SpritePalette.rgb(226, 36, 46), SpritePalette.rgb(42, 162, 242)) // Duramon
            1424 -> SpritePalette(SpritePalette.rgb(66, 70, 78), SpritePalette.rgb(166, 36, 46), SpritePalette.rgb(216, 206, 186), SpritePalette.rgb(246, 196, 42)) // Mephismon
            1425 -> SpritePalette(SpritePalette.rgb(240, 140, 35), SpritePalette.rgb(85, 56, 146), SpritePalette.rgb(245, 205, 45), SpritePalette.rgb(225, 40, 45)) // Mihiramon
            1426 -> SpritePalette(SpritePalette.rgb(78, 90, 114), SpritePalette.rgb(0, 0, 0), SpritePalette.rgb(61, 70, 88), SpritePalette.rgb(0, 0, 0)) // Vajramon
            1427 -> SpritePalette(SpritePalette.rgb(165, 134, 82), SpritePalette.rgb(198, 170, 123), SpritePalette.rgb(189, 162, 231), SpritePalette.rgb(8, 0, 0)) // Andiramon Deva
            1428 -> SpritePalette(SpritePalette.rgb(74, 74, 82), SpritePalette.rgb(49, 49, 58), SpritePalette.rgb(107, 107, 115), SpritePalette.rgb(0, 0, 0)) // Cerberumon Werewolf Mode
            1429 -> SpritePalette(SpritePalette.rgb(216, 226, 236), SpritePalette.rgb(232, 46, 42), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(46, 186, 242)) // Justimon Blitz Arm
            1430 -> SpritePalette(SpritePalette.rgb(33, 40, 90), SpritePalette.rgb(181, 49, 58), SpritePalette.rgb(90, 101, 156), SpritePalette.rgb(8, 4, 33)) // Thunderballmon
            1431 -> SpritePalette(SpritePalette.rgb(82, 196, 242), SpritePalette.rgb(46, 96, 182), SpritePalette.rgb(246, 252, 255), SpritePalette.rgb(250, 206, 46)) // Crys Paledramon
            1432 -> SpritePalette(SpritePalette.rgb(196, 206, 216), SpritePalette.rgb(96, 62, 136), SpritePalette.rgb(226, 42, 46), SpritePalette.rgb(246, 206, 46)) // Metal Fantomon
            1433 -> SpritePalette(SpritePalette.rgb(188, 190, 193), SpritePalette.rgb(51, 32, 0), SpritePalette.rgb(190, 221, 242), SpritePalette.rgb(0, 0, 0)) // Blizzarmon
            1434 -> SpritePalette(SpritePalette.rgb(76, 176, 52), SpritePalette.rgb(42, 92, 36), SpritePalette.rgb(226, 42, 46), SpritePalette.rgb(250, 226, 52)) // Algomon Perfect
            1501 -> SpritePalette(SpritePalette.rgb(172, 184, 218), SpritePalette.rgb(215, 223, 237), SpritePalette.rgb(132, 145, 194), SpritePalette.rgb(0, 0, 0)) // Marsmon
            1502 -> SpritePalette(SpritePalette.rgb(96, 146, 76), SpritePalette.rgb(206, 156, 66), SpritePalette.rgb(226, 42, 52), SpritePalette.rgb(46, 146, 226)) // Minervamon
            1503 -> SpritePalette(SpritePalette.rgb(46, 82, 166), SpritePalette.rgb(196, 212, 226), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(42, 206, 236)) // Neptunemon
            1504 -> SpritePalette(SpritePalette.rgb(18, 63, 58), SpritePalette.rgb(90, 89, 87), SpritePalette.rgb(165, 23, 17), SpritePalette.rgb(35, 15, 5)) // Vulcanusmon
            1505 -> SpritePalette(SpritePalette.rgb(45, 89, 23), SpritePalette.rgb(28, 64, 10), SpritePalette.rgb(131, 96, 38), SpritePalette.rgb(3, 25, 14)) // Ceresmon
            1506 -> SpritePalette(SpritePalette.rgb(229, 5, 140), SpritePalette.rgb(195, 5, 119), SpritePalette.rgb(159, 9, 100), SpritePalette.rgb(16, 6, 36)) // Bacchusmon
            1507 -> SpritePalette(SpritePalette.rgb(151, 135, 42), SpritePalette.rgb(208, 195, 84), SpritePalette.rgb(157, 166, 191), SpritePalette.rgb(25, 20, 5)) // Jupitermon
            1508 -> SpritePalette(SpritePalette.rgb(170, 151, 73), SpritePalette.rgb(247, 231, 176), SpritePalette.rgb(102, 81, 14), SpritePalette.rgb(25, 20, 5)) // Junomon
            1509 -> SpritePalette(SpritePalette.rgb(56, 60, 70), SpritePalette.rgb(216, 176, 66), SpritePalette.rgb(52, 216, 236), SpritePalette.rgb(226, 42, 46)) // Plutomon
            1510 -> SpritePalette(SpritePalette.rgb(149, 147, 110), SpritePalette.rgb(193, 205, 237), SpritePalette.rgb(218, 216, 162), SpritePalette.rgb(25, 20, 5)) // Venusmon
            1511 -> SpritePalette(SpritePalette.rgb(234, 203, 120), SpritePalette.rgb(37, 72, 18), SpritePalette.rgb(172, 149, 89), SpritePalette.rgb(1, 0, 0)) // Titamon
            1512 -> SpritePalette(SpritePalette.rgb(33, 130, 231), SpritePalette.rgb(66, 199, 255), SpritePalette.rgb(123, 121, 140), SpritePalette.rgb(0, 8, 8)) // Apollomon Whispered
            1513 -> SpritePalette(SpritePalette.rgb(226, 46, 36), SpritePalette.rgb(246, 186, 42), SpritePalette.rgb(250, 126, 32), SpritePalette.rgb(46, 186, 236)) // Ancient Greymon
            1514 -> SpritePalette(SpritePalette.rgb(222, 232, 244), SpritePalette.rgb(46, 96, 196), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(226, 42, 46)) // Ancient Garurumon
            1515 -> SpritePalette(SpritePalette.rgb(56, 76, 146), SpritePalette.rgb(226, 176, 56), SpritePalette.rgb(62, 216, 246), SpritePalette.rgb(246, 206, 46)) // Ancient Beatmon
            1516 -> SpritePalette(SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(96, 196, 242), SpritePalette.rgb(46, 196, 116), SpritePalette.rgb(226, 46, 96)) // Ancient Irismon
            1517 -> SpritePalette(SpritePalette.rgb(246, 248, 252), SpritePalette.rgb(86, 186, 226), SpritePalette.rgb(246, 182, 52), SpritePalette.rgb(46, 136, 216)) // Ancient Megatheriumon
            1518 -> SpritePalette(SpritePalette.rgb(52, 54, 62), SpritePalette.rgb(236, 186, 46), SpritePalette.rgb(226, 42, 46), SpritePalette.rgb(46, 206, 236)) // Ancient Sphinxmon
            1519 -> SpritePalette(SpritePalette.rgb(86, 82, 86), SpritePalette.rgb(236, 66, 36), SpritePalette.rgb(250, 196, 46), SpritePalette.rgb(255, 222, 56)) // Ancient Volcamon
            1520 -> SpritePalette(SpritePalette.rgb(176, 126, 76), SpritePalette.rgb(86, 166, 126), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(232, 56, 42)) // Ancient Troiamon
            1521 -> SpritePalette(SpritePalette.rgb(82, 196, 192), SpritePalette.rgb(246, 242, 248), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(56, 126, 216)) // Ancient Mermaimon
            1522 -> SpritePalette(SpritePalette.rgb(186, 142, 86), SpritePalette.rgb(116, 72, 166), SpritePalette.rgb(226, 42, 52), SpritePalette.rgb(246, 206, 46)) // Ancient Wisemon
            1523 -> SpritePalette(SpritePalette.rgb(226, 42, 36), SpritePalette.rgb(246, 186, 42), SpritePalette.rgb(222, 232, 244), SpritePalette.rgb(46, 192, 246)) // Kaiser Greymon
            1524 -> SpritePalette(SpritePalette.rgb(222, 232, 244), SpritePalette.rgb(46, 86, 176), SpritePalette.rgb(226, 42, 46), SpritePalette.rgb(46, 186, 236)) // Magna Garurumon
            1525 -> SpritePalette(SpritePalette.rgb(140, 132, 197), SpritePalette.rgb(90, 82, 165), SpritePalette.rgb(206, 206, 230), SpritePalette.rgb(0, 0, 0)) // Baihumon
            1526 -> SpritePalette(SpritePalette.rgb(65, 135, 230), SpritePalette.rgb(165, 205, 240), SpritePalette.rgb(245, 205, 45), SpritePalette.rgb(225, 40, 45)) // Qinglongmon
            1527 -> SpritePalette(SpritePalette.rgb(0, 74, 16), SpritePalette.rgb(0, 41, 8), SpritePalette.rgb(115, 82, 66), SpritePalette.rgb(0, 0, 0)) // Xuanwumon
            1528 -> SpritePalette(SpritePalette.rgb(206, 25, 25), SpritePalette.rgb(255, 197, 49), SpritePalette.rgb(156, 16, 16), SpritePalette.rgb(0, 0, 0)) // Zhuqiaomon
            1529 -> SpritePalette(SpritePalette.rgb(250, 206, 46), SpritePalette.rgb(216, 156, 36), SpritePalette.rgb(136, 76, 186), SpritePalette.rgb(46, 206, 246)) // Huanglongmon
            1530 -> SpritePalette(SpritePalette.rgb(165, 166, 165), SpritePalette.rgb(140, 138, 140), SpritePalette.rgb(197, 194, 197), SpritePalette.rgb(0, 0, 0)) // Metal Etemon
            1531 -> SpritePalette(SpritePalette.rgb(183, 150, 105), SpritePalette.rgb(225, 184, 55), SpritePalette.rgb(165, 175, 190), SpritePalette.rgb(226, 35, 45)) // King Etemon
            1532 -> SpritePalette(SpritePalette.rgb(58, 49, 41), SpritePalette.rgb(99, 101, 99), SpritePalette.rgb(33, 32, 33), SpritePalette.rgb(0, 0, 0)) // Ebemon
            1533 -> SpritePalette(SpritePalette.rgb(140, 148, 156), SpritePalette.rgb(206, 222, 230), SpritePalette.rgb(49, 49, 123), SpritePalette.rgb(0, 0, 0)) // Belial Vamdemon
            1534 -> SpritePalette(SpritePalette.rgb(33, 32, 33), SpritePalette.rgb(148, 20, 16), SpritePalette.rgb(214, 49, 25), SpritePalette.rgb(33, 12, 0)) // Venom Vamdemon
            1535 -> SpritePalette(SpritePalette.rgb(247, 215, 115), SpritePalette.rgb(148, 138, 140), SpritePalette.rgb(239, 223, 173), SpritePalette.rgb(0, 0, 0)) // Aegisdramon
            1536 -> SpritePalette(SpritePalette.rgb(46, 46, 54), SpritePalette.rgb(216, 42, 46), SpritePalette.rgb(236, 182, 46), SpritePalette.rgb(250, 42, 42)) // Belphemon Rage Mode
            1537 -> SpritePalette(SpritePalette.rgb(96, 96, 106), SpritePalette.rgb(226, 36, 46), SpritePalette.rgb(106, 56, 146), SpritePalette.rgb(250, 206, 46)) // Ogudomon
            1538 -> SpritePalette(SpritePalette.rgb(246, 248, 252), SpritePalette.rgb(226, 42, 46), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(46, 186, 236)) // Jesmon X
            1539 -> SpritePalette(SpritePalette.rgb(196, 146, 96), SpritePalette.rgb(232, 52, 42), SpritePalette.rgb(52, 54, 62), SpritePalette.rgb(250, 206, 46)) // Gankoomon X
            1540 -> SpritePalette(SpritePalette.rgb(226, 236, 246), SpritePalette.rgb(56, 126, 226), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(232, 46, 46)) // Dynasmon X
            1541 -> SpritePalette(SpritePalette.rgb(56, 60, 68), SpritePalette.rgb(226, 42, 46), SpritePalette.rgb(246, 186, 42), SpritePalette.rgb(250, 212, 52)) // Craniummon X
            1542 -> SpritePalette(SpritePalette.rgb(216, 46, 46), SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(246, 248, 252), SpritePalette.rgb(46, 186, 236)) // Sleipmon X
            1543 -> SpritePalette(SpritePalette.rgb(246, 196, 46), SpritePalette.rgb(246, 246, 252), SpritePalette.rgb(226, 42, 46), SpritePalette.rgb(46, 186, 236)) // Duftmon X
            1544 -> SpritePalette(SpritePalette.rgb(250, 206, 46), SpritePalette.rgb(46, 96, 196), SpritePalette.rgb(222, 232, 244), SpritePalette.rgb(226, 42, 46)) // Magnamon X
            1545 -> SpritePalette(SpritePalette.rgb(196, 36, 42), SpritePalette.rgb(46, 50, 58), SpritePalette.rgb(246, 186, 42), SpritePalette.rgb(52, 216, 246)) // Chaosdramon X

            else -> null
        }
    }
}


