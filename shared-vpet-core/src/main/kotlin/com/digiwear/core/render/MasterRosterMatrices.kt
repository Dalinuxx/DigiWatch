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
            else -> null
        }
    }
}


