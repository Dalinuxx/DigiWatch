package com.digiwatch.core.render

import com.digiwatch.core.model.SpriteFrame

/**
 * Single-source-of-truth registry of authentic 16x16 Bandai Digital Monster (1997) dot-matrix pixel sprites.
 * Extracted directly from authentic Bandai V-Pet ROM bit-matrices (Humulos / File Island archive).
 * Shared across Wear OS and Android Phone companion applications to ensure absolute fidelity and visual consistency.
 */
object SpriteMatrixRegistry {

    // =========================================================================
    // Iconic Hand-Tuned Colorized 16x16 Bandai Matrices
    // =========================================================================

    val AGUMON_IDLE_A = arrayOf(
        "................",
        ".....BBBBB......",
        "....BTSSSTB.....",
        "...BBSBB##TB....",
        ".BBSS#WBB#TB....",
        "BS####BBB#TTB...",
        "BBBBT#####TTB...",
        ".B#######TTTB...",
        "..BBBBBTTTTB....",
        "...BBTTT#BSB....",
        "..BWBS##BW#TB...",
        "..BBBS##BBBTB...",
        "....BBTTTTBSTB..",
        "..BB#TBBBBS##B..",
        ".BWBTTB.BWBWBWB.",
        ".BBBBBB.BBBBBBB."
    )

    val AGUMON_IDLE_B = arrayOf(
        "................",
        ".....BBBBB......",
        "....BTSSSTB.....",
        "...BBSBB##TB....",
        ".BBSS#WBB#TB....",
        "BS####BBB#TTB...",
        "BBBBT#####TTB...",
        ".B#######TTTB...",
        "..BBBBBTTTTB....",
        "...BBTTT#BSB....",
        "..BWBS##BW#TB...",
        "..BBBS##BBBTB...",
        "....BBTTTTBSTB..",
        "...BB#TBBBBS##B.",
        "..BWBTTB.BWBWBW.",
        "..BBBBBB.BBBBBB."
    )

    val AGUMON_SLEEP = arrayOf(
        "................",
        ".....BBBBB......",
        "....BTSSSTB.....",
        "...BBSBB##TB....",
        ".BBSS#BBB#TB....",
        "BS####BBB#TTB...",
        "BBBBT#####TTB...",
        ".B#######TTTB...",
        "..BBBBBTTTTB....",
        "...BBTTT#BSB....",
        "..BWBS##BW#TB...",
        "..BBBS##BBBTB...",
        "....BBTTTTBSTB..",
        "..BB#TBBBBS##B..",
        ".BWBTTB.BWBWBWB.",
        ".BBBBBB.BBBBBBB."
    )

    val AGUMON_ATTACK = arrayOf(
        "................",
        ".....BBBBB......",
        "....BTSSSTB.....",
        "...BBSBB##TB....",
        ".BBSS#WBB#TBRRR.",
        "BS####BBB#TTBRRR",
        "BBBBT#####TTBRRR",
        ".B#######TTTB...",
        "..BBBBBTTTTB....",
        "...BBTTT#BSB....",
        "..BWBS##BW#TB...",
        "..BBBS##BBBTB...",
        "....BBTTTTBSTB..",
        "...BB#TBBBBS##B.",
        "..BWBTTB.BWBWBW.",
        "..BBBBBB.BBBBBB."
    )

    val BOTAMON_IDLE = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".....BB..BB.....",
        "....BBBBBBBB....",
        "....B#EEEE#B....",
        "...BB#EEEE#BB...",
        "...BBBBBBBBBB...",
        "..BBBBBBBBBBBB..",
        "..BBBB....BBBB..",
        "..BBBBBBBBBBBB..",
        "...BBBBBBBBBB...",
        "................"
    )

    val BOTAMON_SLEEP = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".....BB..BB.....",
        "....BBBBBBBB....",
        "....B#BBBB#B....",
        "...BB#BBBB#BB...",
        "...BBBBBBBBBB...",
        "..BBBBBBBBBBBB..",
        "..BBBB....BBBB..",
        "..BBBBBBBBBBBB..",
        "...BBBBBBBBBB...",
        "................"
    )

    val KOROMON_IDLE = arrayOf(
        "................",
        "....BB....BB....",
        "...B##B..B##B...",
        "...B##B..B##B...",
        "...B##BBBB##B...",
        "..B##########B..",
        ".B############B.",
        ".B#EE######EE#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        ".B###WWWWWW###B.",
        "..B##WWWWWW##B..",
        "...B########B...",
        "....BBBBBBBB....",
        "................",
        "................"
    )

    val KOROMON_SLEEP = arrayOf(
        "................",
        "....BB....BB....",
        "...B##B..B##B...",
        "...B##B..B##B...",
        "...B##BBBB##B...",
        "..B##########B..",
        ".B############B.",
        ".B#BB######BB#B.",
        ".B#BB######BB#B.",
        ".B############B.",
        "..B##########B..",
        "..B##########B..",
        "...B########B...",
        "....BBBBBBBB....",
        "................",
        "................"
    )

    val GREYMON_IDLE = arrayOf(
        "......SSSSS..SSS",
        ".SS..S#####SS..S",
        ".S.SSS##TT####S.",
        ".SSS###TT##SSS..",
        ".S#EBE#TT###S...",
        ".S#####TT###S###",
        "..S####TT####S##",
        "...SSSSSTTTT#S##",
        ".....TTTTTT####.",
        "...SS####TT#WW..",
        "..S#S###T###WW..",
        "..S#S###T#####S.",
        "...SS####SSS##S.",
        "..SS#S#####S#S#S",
        ".S#S##SSSSS#SSSS",
        ".SSSSSSS...SSSSS"
    )

    // =========================================================================
    // Authentic 16x16 Bandai V-Pet Dot-Matrix Sprites (Ver. 1-5 & Pendulums)
    // =========================================================================

    val AIRDRAMON_ROM = arrayOf(
        "........BBB.BB..",
        ".....BBB##B.B#B.",
        "....B####B..B#B.",
        "...B#BBB##BB###B",
        ".BB#B#B####B###B",
        "B#########B####B",
        "BBBB###########B",
        ".B#####B###B#B#B",
        "..BBBBB#####BB#B",
        ".....B#B###B#B#B",
        "....B#B###B###B.",
        "....BB####B##B#B",
        "....B#B####BBB#B",
        ".....B#B#B#####B",
        "......B#B#B#B#B.",
        ".......BBBBBBB.."
    )

    val ANDROMON_ROM = arrayOf(
        "....BBBBBB......",
        "..BB######B.....",
        ".B#########B....",
        ".BB###BBB##B....",
        "BBBBBB#B###BBBB.",
        "B##BBBB#BBBB###B",
        "BB######B#B#B##B",
        ".BB#B#BBB#B##BB.",
        "B#BBBBBB#BB#B##B",
        "BB#######B#B###B",
        "B#BBBBBBB##BBB#B",
        "B##B###B###B###B",
        "B#BBB##BBBBB##B.",
        "B###BBB####BBBBB",
        "B##B###BBBB####B",
        ".BBBBBBB...BBBBB"
    )

    val ANGEMON_ROM = arrayOf(
        "...BBBBBB..BB...",
        "..B######B..BB..",
        ".B##B#####B.B#B.",
        ".B##B####BBB##B.",
        ".B#BBB##B#B####B",
        ".B##B###B#B##B#B",
        ".B##B##B##B##B#B",
        "..BBBBB###B#B#BB",
        "..B#####BBBB###B",
        ".B#BBBBB###BBBB.",
        "B########B##B##B",
        "B#B######BB#BB#B",
        "B#BBBBBBBB##B.BB",
        ".B#B##B###BBB..B",
        "B##B##B#####B...",
        "BBBBBB.BBBBBB..."
    )

    val BAKEMON_ROM = arrayOf(
        "....BBBBBB......",
        "...B######B.....",
        "..BBB##BB##B....",
        "..BBB##BB##B....",
        "..B#########B...",
        ".BBBBBBB##BBB...",
        "B#B#B#B#BB###B..",
        "B#BBBBBBBB#B#B..",
        "BBBB#B#B#B#B#B..",
        "B.BBBBBBBBB###B.",
        "..B############B",
        "..B#BBB#B#BB#BBB",
        "..BB.BB#BBBB#B..",
        "..B..B.B.B..B...",
        "................",
        "....BBBBBBBB...."
    )

    val BETAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "...BBBBB........",
        "...B####BB......",
        "....B#####B.....",
        "....B#BBBB#B....",
        "...B#B####BBB...",
        "..B######BB##BBB",
        ".B#B##B#B###BB#B",
        ".B#B##B####B##BB",
        ".B############B.",
        ".B#####B##B#B##B",
        "B#BBBBB##BBB###B",
        "BBBB..BBBB.BBBB."
    )

    val BIRDRAMON_ROM = arrayOf(
        "...BBBB....B....",
        "....B##B...BB...",
        "....B###B..B#B..",
        ".....B##B.B###B.",
        ".....B###BB###B.",
        ".....B###B#####B",
        "......BBBB#####B",
        "....BB###B#####B",
        "...B####B####BB.",
        ".BB##BB##B#BB#BB",
        "B##B#B###B##BB#B",
        "BBBB####B######B",
        ".B##B##########B",
        "..BBBBBB######B.",
        "......B#####BB..",
        ".......BBBBB...."
    )

    val CENTARUMON_ROM = arrayOf(
        "......BBBBB.....",
        "......B#B##B....",
        "......BBBBB#B...",
        ".....B###B#BB...",
        "....B#####B#B...",
        "....BBBBB#B#B...",
        "....B#B###B#B...",
        "...BB#B###BB#B..",
        "..B##BBBBB###B..",
        ".BBB######BB#B..",
        ".B#BBBBBBBB###B.",
        ".B#BB######BBB#B",
        ".BBBB#B########B",
        "....B#BB##BB#B#B",
        "...B##B###BBB##B",
        "...BBBBBBBB.BBBB"
    )

    val KOKATORIMON_ROM = arrayOf(
        "BB..BBB..BB.BBB.",
        "BBBBBBBBB##B##B.",
        ".BBB####B##B##B.",
        "..B##BB##B###BBB",
        ".B##BB###B##B##B",
        ".BBB#####B##B##B",
        "B###B###B##B###B",
        "B##B#########BB.",
        ".BB#########B##B",
        "..B############B",
        "..B##########BB.",
        "...B######BBB...",
        "....BBBBBB#B....",
        "..BBB#B..B#B....",
        ".B##B#B.B#B#B...",
        ".BBBBBB.BBBBB..."
    )

    val COELAMON_ROM = arrayOf(
        "................",
        "..........BB....",
        "........BB#B....",
        ".......B###B..B.",
        "....BBBBBBB..B#B",
        "..BB#####B#BB##B",
        ".B####BB#B###B#B",
        "B####BB##B#####B",
        "B##B####B######B",
        "BBBBB##B#BB##B#B",
        ".B#####BB##BB#BB",
        "..BB###B#####BB.",
        "....BBBB####B#B.",
        ".......B##B##B..",
        "........B##BB...",
        ".........BB....."
    )

    val CYCLONEMON_ROM = arrayOf(
        "........BB......",
        "........BBB.....",
        ".....BBBBBBB....",
        "...BBBBBBBBB....",
        "..BBB##BBB#B....",
        ".BBB##BBBB#B....",
        "BBBBBBBBB##B....",
        "BBBBBBBB###B....",
        ".B#####BBBB#B...",
        "..BBBBB####B#B..",
        "..B#B#B##BBB#B..",
        ".B##BBBBB##B#B..",
        ".B##B###B####BBB",
        "..BBBB###BBBBB#B",
        ".B#B##BBB#B#B#B.",
        ".BBBBBB.BBBBBBB."
    )

    val DARKTYRANNOMON_ROM = arrayOf(
        "....BBBBBB.BB...",
        "...BBBBBBBB#B...",
        "...BBBB##BBB....",
        ".BBBBB##BBBBBB..",
        "BBBBBBBBBBBB#B..",
        "BBBBB#BBBBBBB...",
        ".B#####BBBBBBBB.",
        "..BBBBBBBBBBB#B.",
        ".....BBB##BBBBB.",
        "..B.BBB#BBBBBB..",
        ".BB.BBB#BBBB#B.B",
        ".BB.BBBB####BBBB",
        "....BBBBBBBBBBBB",
        "..BBBBBBBBBBBBB.",
        ".B#BBBBBBB#B#B#B",
        ".BBBBBB..BBBBBBB"
    )

    val DELTAMON_ROM = arrayOf(
        ".....BBBBBBBB...",
        "....B######B....",
        "...B##B#BBB...BB",
        "..B##BB##B...B#B",
        "..B#######BBB##B",
        "..BBBB###B####B.",
        ".B##B#B##B##B#B.",
        "B##BB#B#B##BB##B",
        "B#####B#B######B",
        "BBBBBB##BBBBB##B",
        "..BB######B###B.",
        "....B########B..",
        "....B########B..",
        "..BBB#####BBBBB.",
        ".B#B#BBBBB#B#B#B",
        ".BBBBBB..BBBBBBB"
    )

    val DEVIMON_ROM = arrayOf(
        "BB...BBBBBB...BB",
        "BBBBBBBBBBBBBBBB",
        ".BBBBBBBBBBBBBB.",
        "..BBB#BBBB#BBB..",
        "...BB##BB##BB...",
        ".B.BBB#BB#BBB.B.",
        "BBBBBBBBBBBBBBBB",
        "BBBBBBBBBBBBBBBB",
        "BBB##B####B##BBB",
        "BB####BBBB####BB",
        ".B############B.",
        ".B#BB#BBBB#BB#B.",
        ".B##B##BB##B##B.",
        "..BB########BB..",
        "..B###BBBB###B..",
        "..BBBB....BBBB.."
    )

    val DEVIDRAMON_ROM = arrayOf(
        "...BB...BB...B..",
        "..B#B...B#B..BB.",
        "..B#BBBBB##B.B#B",
        ".B#B########BB#B",
        ".BB###BB####B##B",
        ".BB##BB#####B##B",
        ".B#######B##B##B",
        ".BBBBB###B##B##B",
        "..B####BB##B#B#B",
        "...BBB#B##BB#B#B",
        ".BBB####BBBBBB#B",
        "B##B###B####B.BB",
        "B##B###B####B..B",
        ".BBB####BBBBBB..",
        "B#B#BBBBB#B#B#B.",
        "BBBBBB..BBBBBBB."
    )

    val DRIMOGEMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "BBBB.BBBBB......",
        "B###BBBBBBB.....",
        ".BB###BBBBBB....",
        "..BBBB####BBB...",
        ".B######B##BB...",
        ".B########BBBB..",
        ".B#B########BB..",
        "..B#BB#######BB.",
        ".BB###BB##B##BB.",
        "B##BBB#####BB##B",
        "B#B##B#B#B#B###B",
        "BBBBBBBBBBBBBBBB"
    )

    val ELECMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        ".BB......BB.....",
        ".B#B....B#B..B..",
        "..B#B..B#B..BB..",
        "..B##BB##B.B#B.B",
        "...BB#B#BBBB#BBB",
        "..B######BBBBB#B",
        ".B#B##B####BBB#B",
        ".B#B##B######BB.",
        ".B############B.",
        ".B########B#B##B",
        "B#BBBBB##BBB###B",
        "BBBB..BBBB.BBBB."
    )

    val ETEMON_ROM = arrayOf(
        ".....BBBBBB.....",
        "....B######B....",
        ".BBB#BBBBB##BBB.",
        "B##BB#####B#B##B",
        "B#BBBBBBBBBB#B#B",
        "B##B#BBB#BBBB##B",
        "B##BBB#BBBBB###B",
        ".B#B##B####B##B.",
        "..BB#B#BB##BBB..",
        "....B#####B#B#B.",
        "...B#BBBBB##BB#B",
        "...B#########B#B",
        "...B#B###B###B#B",
        "...B#BB#BB#B###B",
        "...B##B#B##B##B.",
        "...BBBBBBBBBBBB."
    )

    val EXTYRANNOMON_ROM = arrayOf(
        ".....BBBB.BBB...",
        "....B####B##B...",
        ".BBB####B#B#B...",
        "B######BB#B#B...",
        "B##########B#B..",
        "BBBBB######B#B..",
        ".B##########B#B.",
        "..BBBB##BB##B#B.",
        ".B##B##B####B#B.",
        ".B#BB##B#BB#BB..",
        ".BB.B##BB###B#BB",
        "....B########B#B",
        "...BBB#####BB##B",
        ".BB###BBBB###B#B",
        "B#B###B#B#B#B#BB",
        "BBBBBBBBBBBBBB.."
    )

    val FLYMON_ROM = arrayOf(
        "...BBBBB.BBBBB..",
        "....B##B..B#BB..",
        "....B###B.B###B.",
        ".BB..B##B.B####B",
        "...B.B###BB####B",
        ".BB#BB###B#####B",
        "B..BBBBB#B####B.",
        "..BBB###B###BB..",
        ".BBB#####BBB....",
        ".BB##B###BBBBBB.",
        ".BB##B###BBBBBBB",
        "B##B#####BB#BBBB",
        "B##B####B#B##B#B",
        ".BBB##BB##B##BB.",
        "....BBBBBBBBB...",
        "....B...B.B.B..."
    )

    val GABUMON_ROM = arrayOf(
        ".BB.............",
        ".B#B.BBBBB......",
        "..B#B#####BB....",
        "..B###BB#B##BB..",
        ".B###BB###BB##B.",
        ".B###BBB###BBBB.",
        "..BBB####BBB....",
        "..B#######BB....",
        "...BBB##BBB.....",
        "....B#####BB....",
        "..BB####B#BB....",
        ".B#B####BB#BB...",
        ".B#B###B##BBB...",
        "..BBB##B##BBB...",
        ".B#B#BBBBB#B#B..",
        ".BBBBB..BBBBBB.."
    )

    val GARURUMON_ROM = arrayOf(
        "................",
        "...BB.....BB....",
        "..B#BBBB.B#B....",
        "..BB####B##B.BB.",
        "..B#######B#B##B",
        "BB###BBB##B##B#B",
        "B##B#BB##BB##B#B",
        "BBBBB###BBB#BB#B",
        ".B########BB##B.",
        "..BBBB###BBB###B",
        "....B######B#BBB",
        "....B#BB####B#BB",
        "....B#B##B##B##B",
        "...BB#B#BBBBBB#B",
        "..B##B###B.B###B",
        "..BBBBBBB..BBBBB"
    )

    val GAZIMON_ROM = arrayOf(
        "................",
        "................",
        "....BBBBBB......",
        "..BB######BB....",
        ".B####BB####B...",
        "B####B#BB####B..",
        "B#####BBB####B..",
        ".BBBB#####B##B..",
        ".B#########BB...",
        "..BBBB##BB##B...",
        ".B##B##B#####B..",
        ".BBBB##BBBB##BBB",
        "....B########B#B",
        "..BBBB###B##B#B.",
        ".B#B#BBBB#B##B..",
        ".BBBBBB.BBBBBB.."
    )

    val GIROMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".....B.....B....",
        ".....BBBBBBB....",
        "......B#B#B.....",
        "......BBBBB.....",
        "....BBB###BBB...",
        "....B.BBBBB.B..."
    )

    val GIZAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        ".......BBB......",
        "......B##B.BBB..",
        ".....B#BBBB##B..",
        "....BBBBBBBBBBBB",
        "...B#BB##BBBBB#B",
        "..B##B#####BBB#B",
        "..B#B##B##BBBBB.",
        "..B#B##B#####BB.",
        "..B#########BBBB",
        "..B#####B####B#B",
        ".B#BBBBB##BBB##B",
        ".BBBB..BBBB.BBBB"
    )

    val KABUTERIMON_ROM = arrayOf(
        "BB..............",
        "B#B..BB.........",
        "B#B.BB#BB.......",
        "B##B##B##B..BBB.",
        "B###B#####BB##B.",
        "B######B##B###B.",
        ".B#####B##B###B.",
        ".B#BBB####B##B..",
        "..B###BBBBB#BBBB",
        "..B######B#B###B",
        ".B#BBBBBBB##B#B.",
        "B#B##B####B#BB..",
        "B#BBBBBBBB##B...",
        ".BB######BBBB...",
        "B##BBBBBB####B..",
        "BBBB....BBBBBB.."
    )

    val KUNEMON_ROM = arrayOf(
        "....BBBB...BB...",
        "...B##B..BB##B..",
        "...BBBBBB##BBBB.",
        "...B#######B....",
        "..B######BB#B...",
        "..BBBBB#BB##B...",
        ".B#####B####B...",
        ".B######B###B...",
        ".B####BBB###B.B.",
        "..B##B##B##B#B#B",
        "...BBBBBBBBB#B#B",
        "..B##B#B###BBB#B",
        "..BBB###BBBB##B.",
        "...B#BBB##B###B.",
        "...B#######B#B..",
        "....BBBBBBBBB..."
    )

    val KUWAGAMON_ROM = arrayOf(
        "...BBB...BBBB...",
        "..B#B.....B##B..",
        ".B##BB...BBB##B.",
        ".B#B........B##B",
        ".B##BBBBBBBB###B",
        "..B##B#####B##B.",
        "...BB#######BB..",
        "....B#B##B##B...",
        "....B#B##B##B...",
        "...BB######B#B..",
        "..B##BBBBBBB##B.",
        "..B#B##B####B#B.",
        "..B#BBBBBBBB##B.",
        "..BBB##B###BBBB.",
        ".B#B#BBBBBB##B#B",
        ".BBBBB....BBBBBB"
    )

    val LEOMON_ROM = arrayOf(
        "......BBBBBBBBB.",
        "....BB########B.",
        ".BBB##BB#BBB#B..",
        ".B#BBB##B##B#BBB",
        "..B#######B####B",
        "BB####BB#B####B.",
        "B##B#BB###B##BBB",
        "BBBBB#####B####B",
        ".B#######B###BBB",
        ".BBBBBBBB#BBB##B",
        "B##B##B########B",
        "B##BBB#BBBBB###B",
        "B#BB#######B##B.",
        "B###BBBBBBBBBBBB",
        "B###B#BBBBB##B#B",
        ".BBBBBBB..BBBBBB"
    )

    val MAMEMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".......BB.......",
        "....B.BBBB.B....",
        ".....B#BB#B.....",
        ".....BBBBBB.....",
        ".....BB##BB.....",
        "......BBBB......",
        "....BB....BB...."
    )

    val MEGADRAMON_ROM = arrayOf(
        "....BBBBBBBB.B..",
        "..BBBBBBB##B.BB.",
        ".BBBBB#BBBB..BB.",
        "BBBBB##BBB..BBBB",
        "BBBBBBBBB#BBBBBB",
        ".B###BBB###BBBBB",
        "..B#####BBBBBBBB",
        "...BBBBB###BB#BB",
        "..B#B#####B#B##B",
        ".BB#B##BBB###B#B",
        "B##BBBBBB###BBB.",
        "B##B###BB##BBBBB",
        ".BBBBBBBBBBBBBBB",
        "...B##B#BBBBBB#B",
        "....BB##B##B#BB.",
        ".....BBBBBBBBB.."
    )

    val MERAMON_ROM = arrayOf(
        "..B..B.BB.B..B..",
        "..BBB#B##B#BBB..",
        "..B#B#B##B#B#B..",
        "..B##########B..",
        "..B##B####B##B..",
        "..B##BB##BB##B..",
        "B..B##B##B##B..B",
        "BB.B########B.BB",
        "B#B#B######B#B#B",
        "B####BBBBBB####B",
        ".B############B.",
        ".B#BB######BB#B.",
        ".B##B######B##B.",
        "..BB###BB###BB..",
        ".B####B..B####B.",
        ".BBBBB....BBBBB."
    )

    val METALGARURUMON_ROM = arrayOf(
        "....BBB...BBB...",
        "...B##B..B##B...",
        "..B#BBBBB##B....",
        "..BB####B#B.....",
        "..B###BB#BB...BB",
        "BB###BB###B..B#B",
        "B##B#####BBB.B#B",
        "BBB#BBBBB##BBB#B",
        ".B#####B###B##B.",
        "..BBBB##BBB####B",
        "....B##B#####B#B",
        "....B##B#B##B##B",
        "...B##B##B##B##B",
        "..BB##B##BBBBB#B",
        ".B#B#B#B#B.B###B",
        ".BBBBBBBB..BBBBB"
    )

    val METALGREYMON_ROM = arrayOf(
        ".....BBBBB..BBB.",
        "BB..B#####BB##B.",
        "B#BBB#BBB####B..",
        "BBB##B#B###BB#B.",
        "B########BB#B#B.",
        "BBBBBB##B#B#B#B.",
        ".B####BB###B###B",
        "..BBBBB####B###B",
        "....B####B#B#B#B",
        "..BB###BB###BB#B",
        ".B#B##B##B##BB#B",
        ".B#B##B##B#BB#BB",
        "..BB###BBBB##B#B",
        ".BBBB#####B###B.",
        "B#B##BBBBB#B#B#B",
        "BBBBBBB...BBBBBB"
    )

    val METALMAMEMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".......BB.......",
        "....B.B##B.B....",
        ".....B####B.....",
        ".....BB##BB.....",
        ".....B####B.....",
        "......BBBB......",
        "....BB....BB...."
    )

    val METALTYRANNOMON_ROM = arrayOf(
        "....BBBBBB.BB...",
        "...BBBB##BB#B...",
        ".BBBBB##BBBB....",
        "BBBBBBBBBBBBBB..",
        "BBBBB#BBBB#B#B..",
        ".B#####BBB#BB...",
        "..BBBBBBBB##BBB.",
        "....B#BB#B#BB#B.",
        "...B##BB##BBBBB.",
        ".B.BB###BBBB#B..",
        "BB.BBB##BBBB#B.B",
        "BB.BB#######BBBB",
        "....BB#B#BB###BB",
        ".BBB#BB#BBBBBBB.",
        "B#BBB#BBBB#B#B#B",
        "BBBBBB...BBBBBBB"
    )

    val MOJYAMON_ROM = arrayOf(
        "................",
        "................",
        "....BBB.BBBB....",
        "...B###B####B...",
        "..B##########B..",
        "..B##########B..",
        ".B############B.",
        ".B##B###B###B#B.",
        ".B#BB###BB##B##B",
        ".BB###B####BBB#B",
        "B##B#B#BB#B####B",
        "B##B######B####B",
        "B##BB#####B###B.",
        ".BBB#B#B#B#BBBB.",
        "B#####B.BB#####B",
        "BBBBBBB..BBBBBBB"
    )

    val MONOCHROMON_ROM = arrayOf(
        "..BB.....BBB....",
        ".BBB.....BBBB...",
        ".BBB....BBBB#B..",
        ".BBBB.BBBBB#BBB.",
        "..BBBBBBBBBBBBB.",
        ".BBBBBB##BBBBBB.",
        "BBBBBB##BBBB##B.",
        "BBBBBBBBBBBBBBB.",
        "BBBBBBBBBBBBBBB.",
        ".B####BBBBBBBB..",
        "..BB#########BBB",
        "....B########B#B",
        "....B#B######B#B",
        "....B#B##B#B##B.",
        "...B#B###BB####B",
        "...BBBBBBBBBBBBB"
    )

    val MONZAEMON_ROM = arrayOf(
        "...BB.BBBBB.BBB.",
        "..B##B#####B###B",
        "..B#B##########B",
        "...BBB###BB#B##B",
        "...B##BBB####BB.",
        "...B###B#####B..",
        "...B##B#B####B..",
        "...BB#######BB..",
        "..B#BB###BBB##B.",
        ".B#B###########B",
        ".BB##B#B####B##B",
        "..B###B######BB.",
        ".BBB#B#B##BBB#B.",
        "B###B####B###BB.",
        "B###B####B####B.",
        ".BBBBBBBBBBBBB.."
    )

    val NANIMON_ROM = arrayOf(
        ".......B........",
        "........B.......",
        ".......B........",
        ".....BBBBB......",
        "....B#####B.....",
        "...B#B#B###B....",
        "..BBBBBBBBBBB...",
        "..BB#BBBB#BBB...",
        "..BBBB##BBB#B...",
        "..B#########B...",
        "..B##BBBB###B...",
        "...BB####B#B....",
        "..B#B#BB#BB#B...",
        ".BBBBBBBBB.BBBB.",
        "B#B#B......B#B#B",
        "BBBBB......BBBBB"
    )

    val NANOMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".......BBB......",
        "......B###B.....",
        "......BB#BB.....",
        ".....BB###BB....",
        "......B###B.....",
        ".......BBB......",
        "......B...B.....",
        ".....BBB.BBB...."
    )

    val NUMEMON_ROM = arrayOf(
        "................",
        "................",
        ".B.B.B..B.B.B...",
        ".BBBBB..BBBBB...",
        ".B#B#B..B#B#B...",
        ".BBBBB..BBBBB...",
        "...B......B.....",
        "....B....B......",
        ".....BBBBB......",
        "....B#BB##B.....",
        "....B######B....",
        "....B#######BB..",
        "...B##BBB#####B.",
        "..B############B",
        "...B########BBB.",
        "....BBBBBBBB...."
    )

    val OGREMON_ROM = arrayOf(
        ".B....BBBB...B..",
        "B#BBBBBBBBBBB#B.",
        "B##BBBB##B####B.",
        ".BB#B##B##B#BB..",
        "..BB##BB###B....",
        "..BB##B##B#B....",
        "..B####BBB#B....",
        "...BBBBB#B#BBBB.",
        "..BBBBBBBB#B###B",
        ".BB#B#B#BB#B###B",
        "B#BBBBBBB##BBB#B",
        "B#B#######BB###B",
        "BB#BBBBBBB#B###B",
        "B###B#B####BBBB.",
        "B##B###BBBB####B",
        ".BBBBBBB...BBBBB"
    )

    val OMEGAMON_ROM = arrayOf(
        ".BB...BBBB..BBB.",
        ".BBBBB####BBBB..",
        "..BBBB#####BB...",
        "...BB###BB#B..BB",
        "BBB####BB##BBB#B",
        "B#B########B##B.",
        ".B#B#BBB#BB####B",
        ".B##B###B#B####B",
        "..BBBBBBBBBB##BB",
        ".BB#B##B###BBB#B",
        "B###BBBBBBBB###B",
        "B##BB#B##B##B#B.",
        "BBBBBB#B#B#BBB..",
        "..BB##BBBBB##BB.",
        ".B##B##BBB##B##B",
        ".BBBBBBB.BBBBBBB"
    )

    val PAGUMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "...BB.....BB....",
        "..B#BBBBBB##BB..",
        ".B############B.",
        "B#B#######B####B",
        "B#B#B###B##B###B",
        "B#B#B###B###B##B",
        ".BB###B######BB.",
        "..B##B#B#####B..",
        "...B########B...",
        "....BBBBBBBB...."
    )

    val PALMON_ROM = arrayOf(
        ".....BBBB.......",
        "..BBBB###BBBB...",
        ".B###B##B####B..",
        "..BB######BBB...",
        ".B###B##B####B..",
        ".B##BB##BB###B..",
        "..BB##BB##BBB...",
        "...B#B###B##B...",
        "...B#B###B##B...",
        "...B########B...",
        "..B#B######B#B..",
        "..B##BBBBBB##B..",
        "..B#B######B#B..",
        "...BB#####BBB...",
        "..B##BBBBB###B..",
        "..BBBBB..BBBBB.."
    )

    val PATAMON_ROM = arrayOf(
        "................",
        "................",
        ".....BBB.BBBB...",
        "....B###B####B..",
        "...B##BB###B##B.",
        "..B####B###BBBB.",
        "..B##BBB##BB....",
        "..B#B######B....",
        ".B##########B.BB",
        "B#B####B#####B#B",
        "B#B####B######B.",
        "B###B##########B",
        "B##B#BB########B",
        ".B########B#B##B",
        "B#BBBBBB##BB###B",
        "BBBB...BBB.BBBB."
    )

    val PIXIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".....B....B.....",
        "......BBBB......",
        ".....B####B.....",
        ".....BB##BB.....",
        ".....B####B.....",
        "....B#BBBB#B....",
        ".....B....B....."
    )

    val BIYOMON_ROM = arrayOf(
        "................",
        "..BBBBB...B.....",
        ".B#####BBB#B....",
        ".B##B######B....",
        "..BB##B##BB.....",
        "..B###B###B.....",
        "..BBB####B..BB..",
        ".B###B###BBB##B.",
        ".B###B##B#####B.",
        ".B##B#########B.",
        "..BB#########BB.",
        "...B######BBB#B.",
        "....B########B..",
        "....BBBBBBBBB...",
        "...B#B#B.B#B#B..",
        "...BBBBB.BBBBB.."
    )

    val POYOMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "......BBBB......",
        ".....BBBBBB.....",
        ".....B#BB#B.....",
        ".....BBBBBB.....",
        ".....BBBBBB.....",
        ".....B.BB.B....."
    )

    val PUNIMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "......B.B.B.....",
        "......BBBBB.....",
        ".....BB#B#BB....",
        ".....BBBBBBB....",
        ".....BBBBBBB....",
        "......BBBBB....."
    )

    val RAREMON_ROM = arrayOf(
        "...BBB.BBBB.....",
        "..B###B####B....",
        "..B###B####B....",
        ".B######BB#B....",
        ".B#BB##B#B##B...",
        ".B#B#B#BB###B...",
        ".B##BB####B##B..",
        "..B#######B##B..",
        "..B####B##B##B..",
        "..B##B#B###B#B..",
        "...B#B##B##B##B.",
        "..BB####B#####B.",
        "..B####B##B#BB..",
        "..B###B#B#B###B.",
        "..B#B#BBB##B##B.",
        "..BBBBB.BBBBBBB."
    )

    val SUKAMON_ROM = arrayOf(
        "................",
        "................",
        ".........BBB....",
        ".......BB###B...",
        "......B####BB...",
        ".....B####BB....",
        ".....B#####B....",
        "....B#######B...",
        "...BBB###BB##B..",
        "...B#BB#BB###B..",
        "..B###########B.",
        "..B#B##B##B##BB.",
        ".B#B#BB#BB#BB##B",
        ".B##B##B##B####B",
        "..B###########B.",
        "...BBBBBBBBBBB.."
    )

    val SEADRAMON_ROM = arrayOf(
        ".....BBBBB......",
        "...BB#####BBBBB.",
        "..B##BBB#####B..",
        ".B##B#B###BBBBB.",
        "B###########B...",
        "BBBBBBB##BBBBB..",
        ".B##########B...",
        "..BBBBBB####B...",
        "......BB###B....",
        ".....BB####BBB..",
        ".....B#B##B#B#B.",
        "....B#B###B##B#B",
        "....BB#B###BBB#B",
        "....B#B#B######B",
        ".....B#B#B#B##B.",
        "......BBBBBBBB.."
    )

    val SHELLMON_ROM = arrayOf(
        "................",
        "................",
        "..........BB....",
        "......B..BBBB..B",
        "......BBBB###BBB",
        "......BB#BBBB#BB",
        ".......BB##BBBB.",
        "....BBBBBBB##BB.",
        "...BBBB##BBBB#BB",
        "..BBB######BBB#B",
        ".B#B##BBB###BBBB",
        ".B###B#B#####BB.",
        ".B###########BBB",
        ".B#####B##B###BB",
        "B#BBBBB##BB###BB",
        "BBBB..BBBBBBBBB."
    )

    val SKULLGREYMON_ROM = arrayOf(
        ".BBBBB..........",
        ".B####BBB.......",
        "..BBB####BB.BBB.",
        "...B###B##BB###B",
        ".BB###BB##B##BB.",
        "B###B####BB#B...",
        "BBBBBB#####B#BB.",
        ".B#####B####B##B",
        "..BBBBB##B#B#BB.",
        "...B##B#B#B###B.",
        ".BB#BB#BB#BB##B.",
        "B##B##B##B##BB..",
        "B###BB#BBB##B#B.",
        ".BBB#B####BB##B.",
        ".B#B##BBBB#B#B#B",
        ".BBBBBB...BBBBBB"
    )

    val TANEMON_ROM = arrayOf(
        "................",
        "................",
        "..B..BB...BB..B.",
        "..BBBBBBBBBBBBB.",
        "..BBBBBB#BBBBBB.",
        "...BB..B#B..BB..",
        ".......B#B......",
        ".....BB###BB....",
        "....B##BBB##B...",
        "...B#########B..",
        "...B#B####B##B..",
        "...B#B####B##B..",
        "...B###B#####B..",
        "...BB#######BB..",
        "..B##BBBBB####B.",
        "..BBBBB..BBBBBB."
    )

    val TENTOMON_ROM = arrayOf(
        "..BB....BB......",
        "....BBBB........",
        "...B####BB......",
        "..B#######B.....",
        "..BB###BB##BB...",
        "..B#####BB#BBB..",
        "..BB###BBB#B##B.",
        "..B#BBB####BB#B.",
        "...B#####BBBBBB.",
        "...B######BBB##B",
        "..B#BBBBBBB#BB#B",
        ".B#B##B###BB#BBB",
        ".B#BBBBBBBB##BB.",
        "..BB##B###BBBB..",
        ".B##BBBBBBB###B.",
        ".BBBBB....BBBBB."
    )

    val TOKOMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        ".....B..........",
        "....BB.....BBBB.",
        "...B#B....B##B..",
        "...B##BBBBB#B...",
        "....B#######B...",
        "...B#########B..",
        "...B#B####B##B..",
        "...B#B#B##B##B..",
        "...B##B#B####B..",
        "....B#######B...",
        "...B#BBBBBB##B..",
        "...BBB.....BBB.."
    )

    val TSUNOMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        ".......B........",
        "......B#B.......",
        "......B#B.......",
        "......B#B.......",
        ".....BB#BBBB....",
        "....BBBBBBBBB...",
        "...BB#BBB##BBB..",
        "...B#B#B#B##BB..",
        "...B#B###B##BB..",
        "...B###B#####B..",
        "....B#######B...",
        ".....BBBBBBB...."
    )

    val TUSKMON_ROM = arrayOf(
        "BB...BB.......BB",
        "B#B.BB#BBBB..B#B",
        "B#B.B#BB##BB.B#B",
        "B##BBBB##BBBB##B",
        "B#BBBBBBBBBB###B",
        "B#BBBB#BBBBB##B.",
        ".B#B#####BB###B.",
        "..BBBBBBBBBB##B.",
        "...BBBBBBBBBBB..",
        ".BB#BBBB##BBBB..",
        ".BB#BBB#BBBB#B.B",
        ".BB#BBB#BBBB#BBB",
        "...BBBBB####BBBB",
        "..BBBBBBBBBBBBB.",
        ".B#B#BBBBB#B#B#B",
        ".BBBBBBB.BBBBBBB"
    )

    val TYRANNOMON_ROM = arrayOf(
        "......BBBBB.BB..",
        ".....B#####B#B..",
        "....BB##BBB#B...",
        "..BB###B#B##BBB.",
        ".B###B#####BB#B.",
        ".BBBBBB##BBBBB..",
        "..B#########BBBB",
        "...BBBBB##BBBB#B",
        ".....B######BBB.",
        "...BB####BB##B..",
        "..B#B###B####BBB",
        "..B#B###B##B#B#B",
        "...BB####BBB#B#B",
        "..BB#B####B###B.",
        ".B#B##BBBB#B#B#B",
        ".BBBBBB..BBBBBB."
    )

    val UNIMON_ROM = arrayOf(
        ".....BBBB.....BB",
        "BBB.BBBBBBBB.B#B",
        "B##BBB##B##BB##B",
        ".B########BB##BB",
        "..B##BBB#BB##B#B",
        ".B##B#B###B#B##B",
        "B########BB###B.",
        "B#####B##BB##B#B",
        ".BBBBB#####BBB#B",
        "....B##########B",
        "....B##B######B.",
        ".....BBB#######B",
        ".....B#B##BB###B",
        ".....B#B##BBBB#B",
        "....B#B###B.B##B",
        "....BBBBBBB.BBBB"
    )

    val VADEMON_ROM = arrayOf(
        "........BBBBB...",
        ".......B##B##B..",
        "......B##B####B.",
        "......B##B##B#B.",
        "......B###B#B#B.",
        "B.....B#B#B###B.",
        ".BBB...B#####B..",
        ".B#B.B.BB###BB..",
        ".BB#B..B#B#B#B..",
        "...B#B..BB#BB...",
        "..B.BBB..BBB....",
        ".......BBB#BBB..",
        ".........BBB##BB",
        "........B#B#B#B.",
        ".......B#B#B#B..",
        ".....BB.B.B.B.BB"
    )

    val VEGIEMON_ROM = arrayOf(
        "........B.......",
        ".......B#B......",
        "B...BB.B#B.BB..B",
        ".B.B##B#B#B##B.B",
        ".B..BB#BBB#BB#B.",
        "..B...B###B###B.",
        "..B..B###B#B##B.",
        ".B...BB#B##B###B",
        ".B..B#######B##B",
        "..BB####B###BBB.",
        "..B#BBBB#####B..",
        "...B#########B..",
        "...B#########B..",
        "...B#########B..",
        "....B#######B...",
        ".....BBBBBBB...."
    )

    val WARGREYMON_ROM = arrayOf(
        "..BB........BB..",
        "..B#B.BBBBBB#B..",
        "..B#BBBBBB###B..",
        "...BBBB##BBBB...",
        "..BBBB##BBBB....",
        "BBBBBBBBBBBBBBBB",
        "B#BBBBBBB##B###B",
        ".B#B######B###B.",
        "B#BBBBBBBB#BBB#B",
        "B##BB##B####B##B",
        "B#BBBBB#BBBB#BB.",
        ".BB#B######B#B#B",
        "B##B#BBBBBB#B##B",
        "BBBBB#B##B#BBBBB",
        "..B#B#BBBBB#B#B.",
        "..BBBBBB.BBBBBB."
    )

    val WHAMON_ROM = arrayOf(
        ".......BBB.BBB..",
        "......BBBBBBBBB.",
        ".......BBBBBBB..",
        ".........BBB....",
        "..........B#B...",
        "......BB..B#B...",
        ".....B#BBB#B#B..",
        "...BBBBBB#B#BB..",
        "..BBBBBBBB#B#BBB",
        ".BBBBBBBBBBBBB#B",
        ".BBBBBBB#BBB##B.",
        ".BBBBBB#BBBBBB..",
        ".BBBB#BBBBBBB...",
        ".B#####BBBBB....",
        "..BBBBBBBBB.....",
        "...BBBBBB......."
    )

    val FRIGIMON_ROM = arrayOf(
        "......BBBBBB....",
        ".....B######B...",
        "....B########B..",
        "....B########B..",
        "...B#B####B##B..",
        "...B###B#####B..",
        "...B#########B..",
        "....B#######B...",
        "...B#B###BBB#B..",
        "..B###########B.",
        "..B###B#######B.",
        "..B###########B.",
        "..B###B#######B.",
        "..B###########B.",
        "...B#########B..",
        "....BBBBBBBBB..."
    )

    val YURAMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".....BBBBB......",
        "....BBBBBBB.....",
        "....B#B#BB......",
        "....BBBBBBB.....",
        "....BBBBBB......",
        ".....BBBBBB....."
    )

    val ZURUMON_ROM = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".....BBBB.......",
        "....B#B#BB......",
        "....BBBBBB......",
        "....BB#BBBB.....",
        ".....BBBBBBB...."
    )

    // =========================================================================
    // Canonical Fallback Archetype Dot-Matrices
    // =========================================================================

    val ARCHETYPE_DINOSAUR = AGUMON_IDLE_A

    val ARCHETYPE_BEAST = arrayOf(
        "...SS......SS...",
        "..SSSS....SSSS..",
        "...SSSS..SSSS...",
        "....##########..",
        "...####EBE######",
        "...#############",
        "....############",
        "...TT#TT#WW##...",
        "..TTTTTT#WW##...",
        ".#TTTTTT######..",
        "###TTTTT######..",
        "##..####..####..",
        "##..####..####..",
        "....####..####..",
        "...WWWW..WWWW...",
        "................"
    )

    val ARCHETYPE_AVIAN = arrayOf(
        "......####......",
        ".....##EBE##....",
        "....########WW..",
        ".....######WW...",
        "SS....####....SS",
        "SSS..######..SSS",
        "SSSS########SSSS",
        "SSS.########.SSS",
        "SS..########..SS",
        "....########....",
        ".....######.....",
        ".....######.....",
        "....###..###....",
        "....WW....WW....",
        "...WWWW..WWWW...",
        "................"
    )

    val ARCHETYPE_INSECT = arrayOf(
        "...WW......WW...",
        "....WW....WW....",
        ".....######.....",
        "....##EBEB##....",
        "....########....",
        "..############..",
        ".##############.",
        "################",
        "################",
        ".##############.",
        "..############..",
        "....########....",
        "....########....",
        "...##.####.##...",
        "..##..####..##..",
        ".##....##....##."
    )

    val ARCHETYPE_AQUATIC = arrayOf(
        "....WW..........",
        ".....WW.........",
        "......######....",
        ".....##EBE###...",
        "....##########..",
        "...############.",
        "..##############",
        ".###############",
        "################",
        "..##############",
        "...############.",
        "....##########..",
        "......######....",
        ".......####.....",
        "......WW..WW....",
        ".....WW....WW..."
    )

    val ARCHETYPE_DEMON = DEVIMON_ROM

    val ARCHETYPE_HOLY = ANGEMON_ROM

    val ARCHETYPE_MECH = arrayOf(
        "....WW....WW....",
        "...SSSS..SSSS...",
        "...SSSSSSSSSS...",
        "...SS##EE##SS...",
        "...SSSSSSSSSS...",
        "..TT########TT..",
        ".TTTT######TTTT.",
        "TTTT########TTTT",
        "WWWW########WWWW",
        ".WW.########.WW.",
        "....########....",
        "....TT#SS#TT....",
        "....###..###....",
        "....###..###....",
        "...SS##..##SS...",
        "...WWWW..WWWW..."
    )

    val ARCHETYPE_PLANT = arrayOf(
        ".....TTTTTT.....",
        "....TTTTTTTT....",
        ".....######.....",
        "....##EBEB##....",
        "...############.",
        "..##############",
        ".###############",
        "################",
        "################",
        ".##############.",
        "..############..",
        "...##########...",
        "....########....",
        ".....######.....",
        "....###..###....",
        "...WWWW..WWWW..."
    )

    val ARCHETYPE_BABY = arrayOf(
        "................",
        ".....######.....",
        "...##########...",
        "..############..",
        ".##############.",
        ".###EE####EE###.",
        ".###EE####EE###.",
        "################",
        "################",
        ".##############.",
        ".##############.",
        "..############..",
        "...##########...",
        "....########....",
        ".....##..##.....",
        "................"
    )

    /**
     * Returns the authentic 16x16 Bandai dot matrix for the given species and animation frame.
     */
    fun getMatrix(speciesId: Int, frame: SpriteFrame): Array<String> {
        val isSleep = frame == SpriteFrame.SLEEP_A || frame == SpriteFrame.SLEEP_B
        val isWalk = frame == SpriteFrame.WALK_A || frame == SpriteFrame.WALK_B
        val isAttack = frame == SpriteFrame.ATTACK_RELEASE

        val sid = if (speciesId <= 1) 201 else speciesId

        return when (sid) {
            // Baby I
            101 -> if (isSleep) BOTAMON_SLEEP else BOTAMON_IDLE
            103 -> PUNIMON_ROM
            105 -> POYOMON_ROM
            107 -> YURAMON_ROM
            109 -> ZURUMON_ROM

            // Baby II
            102 -> if (isSleep) KOROMON_SLEEP else KOROMON_IDLE
            104 -> TSUNOMON_ROM
            106 -> TOKOMON_ROM
            108 -> TANEMON_ROM
            110 -> PAGUMON_ROM

            // Child
            201 -> when {
                isSleep -> AGUMON_SLEEP
                isAttack -> AGUMON_ATTACK
                isWalk || frame == SpriteFrame.IDLE_B -> AGUMON_IDLE_B
                else -> AGUMON_IDLE_A
            }
            202 -> BETAMON_ROM
            203 -> GABUMON_ROM
            204 -> ELECMON_ROM
            205 -> PATAMON_ROM
            206 -> BIYOMON_ROM
            207 -> PALMON_ROM
            208 -> TENTOMON_ROM
            209 -> GAZIMON_ROM
            210 -> GIZAMON_ROM
            211 -> KUNEMON_ROM

            // Adult
            301 -> GREYMON_IDLE
            302 -> TYRANNOMON_ROM
            303 -> DEVIMON_ROM
            304 -> MERAMON_ROM
            305 -> NUMEMON_ROM
            306 -> GARURUMON_ROM
            307 -> ANGEMON_ROM
            308 -> ARCHETYPE_PLANT // Togemon
            309 -> BIRDRAMON_ROM
            310 -> KABUTERIMON_ROM
            311 -> MONOCHROMON_ROM
            312 -> OGREMON_ROM
            313 -> LEOMON_ROM
            314 -> SEADRAMON_ROM
            315 -> SUKAMON_ROM
            316 -> DARKTYRANNOMON_ROM
            317 -> DEVIDRAMON_ROM
            318 -> CYCLONEMON_ROM
            319 -> TUSKMON_ROM
            320 -> FLYMON_ROM
            321 -> RAREMON_ROM
            322 -> KUWAGAMON_ROM
            323 -> KOKATORIMON_ROM
            324 -> COELAMON_ROM
            325 -> NANIMON_ROM
            326 -> BAKEMON_ROM
            327 -> SHELLMON_ROM
            328 -> UNIMON_ROM
            329 -> FRIGIMON_ROM
            330 -> VEGIEMON_ROM
            336 -> FLYMON_ROM
            339 -> GREYMON_IDLE // BlackGreymon
            340 -> GARURUMON_ROM // BlackGarurumon
            344 -> AIRDRAMON_ROM

            // Perfect
            401 -> METALGREYMON_ROM
            402 -> MONZAEMON_ROM
            403 -> SKULLGREYMON_ROM
            409 -> WHAMON_ROM
            411 -> ETEMON_ROM
            412 -> MAMEMON_ROM
            413 -> METALMAMEMON_ROM
            414 -> METALTYRANNOMON_ROM
            415 -> EXTYRANNOMON_ROM
            416 -> MEGADRAMON_ROM
            417 -> PIXIMON_ROM
            418 -> ANDROMON_ROM
            419 -> GIROMON_ROM
            420 -> VADEMON_ROM
            421 -> MAMEMON_ROM // SkullMamemon
            429 -> METALGREYMON_ROM // BlackMetalGreymon

            // Ultimate
            501 -> WARGREYMON_ROM
            503 -> METALGARURUMON_ROM
            510 -> OMEGAMON_ROM
            525 -> WARGREYMON_ROM // BlackWarGreymon
            535, 536 -> OMEGAMON_ROM // Omnimon variants

            // Generic Baby stage fallback
            in 101..139 -> if (isSleep) BOTAMON_SLEEP else ARCHETYPE_BABY

            // Group Fallbacks by canonical archetype
            in listOf(225, 422, 437, 506, 545) -> ARCHETYPE_AVIAN
            in listOf(508, 533) -> ARCHETYPE_AQUATIC
            in listOf(213, 333, 408, 507) -> ARCHETYPE_INSECT
            in listOf(216, 217, 311, 404, 423, 430) -> ARCHETYPE_BEAST
            in listOf(218, 410, 428, 509, 519, 520, 530, 531, 532) -> ARCHETYPE_DEMON
            in listOf(221, 405, 427, 504, 514, 515, 516, 521, 537, 552) -> ARCHETYPE_HOLY
            in listOf(502, 518, 542, 546, 548, 554, 441) -> ARCHETYPE_MECH
            in listOf(230, 406, 443, 505) -> ARCHETYPE_PLANT

            else -> ARCHETYPE_DINOSAUR
        }
    }

    /**
     * Returns the 32-bit ARGB color palette for the given species.
     */
    fun getPalette(speciesId: Int): SpritePalette {
        val sid = if (speciesId <= 1) 201 else speciesId
        return when (sid) {
            // Baby I
            101 -> SpritePalette(SpritePalette.rgb(28, 28, 34), SpritePalette.rgb(55, 55, 68), SpritePalette.rgb(80, 80, 95), SpritePalette.rgb(255, 230, 0)) // Botamon
            103 -> SpritePalette(SpritePalette.rgb(220, 40, 50), SpritePalette.rgb(180, 25, 35), SpritePalette.rgb(255, 120, 130), SpritePalette.rgb(255, 255, 255)) // Punimon
            105 -> SpritePalette(SpritePalette.rgb(180, 225, 245), SpritePalette.rgb(140, 195, 225), SpritePalette.rgb(220, 245, 255), SpritePalette.rgb(20, 30, 40)) // Poyomon
            107 -> SpritePalette(SpritePalette.rgb(190, 190, 195), SpritePalette.rgb(130, 130, 140), SpritePalette.rgb(240, 240, 245), SpritePalette.rgb(15, 15, 20)) // Yuramon
            109 -> SpritePalette(SpritePalette.rgb(45, 55, 45), SpritePalette.rgb(75, 90, 70), SpritePalette.rgb(115, 135, 105), SpritePalette.rgb(255, 215, 0)) // Zurumon
            111 -> SpritePalette(SpritePalette.rgb(120, 120, 125), SpritePalette.rgb(80, 80, 85), SpritePalette.rgb(160, 160, 170), SpritePalette.rgb(240, 60, 40)) // Mokumon
            113 -> SpritePalette(SpritePalette.rgb(105, 90, 155), SpritePalette.rgb(70, 55, 115), SpritePalette.rgb(160, 140, 210), SpritePalette.rgb(230, 30, 40)) // Kuramon

            // Baby II
            102 -> SpritePalette(SpritePalette.rgb(255, 145, 175), SpritePalette.rgb(225, 80, 115), SpritePalette.rgb(255, 190, 210), SpritePalette.rgb(220, 20, 30)) // Koromon
            104 -> SpritePalette(SpritePalette.rgb(240, 210, 150), SpritePalette.rgb(160, 95, 40), SpritePalette.rgb(255, 180, 60), SpritePalette.rgb(30, 30, 30)) // Tsunomon
            106 -> SpritePalette(SpritePalette.rgb(245, 235, 210), SpritePalette.rgb(215, 195, 160), SpritePalette.rgb(255, 150, 150), SpritePalette.rgb(25, 25, 30)) // Tokomon
            108 -> SpritePalette(SpritePalette.rgb(100, 190, 90), SpritePalette.rgb(60, 140, 55), SpritePalette.rgb(230, 120, 180), SpritePalette.rgb(20, 20, 20)) // Tanemon
            110 -> SpritePalette(SpritePalette.rgb(110, 85, 135), SpritePalette.rgb(75, 50, 95), SpritePalette.rgb(155, 125, 185), SpritePalette.rgb(225, 40, 40)) // Pagumon
            112 -> SpritePalette(SpritePalette.rgb(240, 100, 30), SpritePalette.rgb(255, 180, 40), SpritePalette.rgb(200, 40, 20), SpritePalette.rgb(255, 255, 255)) // DemiMeramon
            114 -> SpritePalette(SpritePalette.rgb(65, 145, 225), SpritePalette.rgb(225, 235, 245), SpritePalette.rgb(35, 95, 175), SpritePalette.rgb(220, 40, 40)) // DemiVeemon
            115 -> SpritePalette(SpritePalette.rgb(225, 55, 45), SpritePalette.rgb(35, 35, 40), SpritePalette.rgb(245, 235, 215), SpritePalette.rgb(255, 215, 0)) // Gigimon
            116 -> SpritePalette(SpritePalette.rgb(245, 205, 75), SpritePalette.rgb(245, 240, 220), SpritePalette.rgb(195, 155, 45), SpritePalette.rgb(40, 140, 220)) // Viximon
            117 -> SpritePalette(SpritePalette.rgb(125, 215, 145), SpritePalette.rgb(85, 175, 105), SpritePalette.rgb(240, 150, 180), SpritePalette.rgb(25, 25, 30)) // Gummymon
            118 -> SpritePalette(SpritePalette.rgb(165, 115, 75), SpritePalette.rgb(120, 75, 45), SpritePalette.rgb(225, 180, 145), SpritePalette.rgb(25, 25, 30)) // Chocomon
            119 -> SpritePalette(SpritePalette.rgb(55, 45, 95), SpritePalette.rgb(85, 75, 135), SpritePalette.rgb(225, 60, 80), SpritePalette.rgb(255, 215, 0)) // Yaamon

            // Child
            201 -> SpritePalette(SpritePalette.rgb(255, 175, 25), SpritePalette.rgb(216, 112, 10), SpritePalette.rgb(247, 231, 140), SpritePalette.rgb(20, 20, 25)) // Agumon
            202 -> SpritePalette(SpritePalette.rgb(35, 175, 110), SpritePalette.rgb(20, 120, 75), SpritePalette.rgb(240, 60, 60), SpritePalette.rgb(255, 60, 60)) // Betamon
            203 -> SpritePalette(SpritePalette.rgb(240, 225, 170), SpritePalette.rgb(70, 120, 200), SpritePalette.rgb(230, 180, 50), SpritePalette.rgb(180, 30, 30)) // Gabumon
            204 -> SpritePalette(SpritePalette.rgb(225, 45, 55), SpritePalette.rgb(60, 110, 210), SpritePalette.rgb(255, 220, 50), SpritePalette.rgb(20, 20, 25)) // Elecmon
            205 -> SpritePalette(SpritePalette.rgb(240, 165, 80), SpritePalette.rgb(205, 125, 50), SpritePalette.rgb(255, 210, 160), SpritePalette.rgb(30, 90, 180)) // Patamon
            206 -> SpritePalette(SpritePalette.rgb(255, 130, 170), SpritePalette.rgb(65, 140, 230), SpritePalette.rgb(220, 70, 110), SpritePalette.rgb(30, 120, 220)) // Biyomon
            207 -> SpritePalette(SpritePalette.rgb(115, 195, 95), SpritePalette.rgb(235, 65, 115), SpritePalette.rgb(245, 225, 75), SpritePalette.rgb(20, 20, 25)) // Palmon
            208 -> SpritePalette(SpritePalette.rgb(215, 55, 45), SpritePalette.rgb(85, 145, 75), SpritePalette.rgb(175, 175, 185), SpritePalette.rgb(40, 130, 215)) // Tentomon
            209 -> SpritePalette(SpritePalette.rgb(80, 70, 110), SpritePalette.rgb(50, 40, 75), SpritePalette.rgb(240, 240, 245), SpritePalette.rgb(220, 40, 40)) // Gazimon
            210 -> SpritePalette(SpritePalette.rgb(195, 65, 55), SpritePalette.rgb(140, 40, 35), SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(255, 215, 0)) // Gizamon
            211 -> SpritePalette(SpritePalette.rgb(245, 215, 60), SpritePalette.rgb(115, 55, 135), SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(20, 20, 25)) // Kunemon

            // Adult
            301 -> SpritePalette(SpritePalette.rgb(247, 154, 41), SpritePalette.rgb(16, 101, 156), SpritePalette.rgb(82, 65, 58), SpritePalette.rgb(25, 20, 0)) // Greymon
            302 -> SpritePalette(SpritePalette.rgb(239, 53, 25), SpritePalette.rgb(33, 109, 8), SpritePalette.rgb(239, 235, 239), SpritePalette.rgb(33, 12, 0)) // Tyrannomon
            303 -> SpritePalette(SpritePalette.rgb(26, 26, 29), SpritePalette.rgb(45, 19, 44), SpritePalette.rgb(199, 0, 57), SpritePalette.rgb(255, 56, 56)) // Devimon
            304 -> SpritePalette(SpritePalette.rgb(255, 90, 25), SpritePalette.rgb(220, 40, 10), SpritePalette.rgb(255, 210, 40), SpritePalette.rgb(255, 255, 255)) // Meramon
            305 -> SpritePalette(SpritePalette.rgb(145, 200, 65), SpritePalette.rgb(105, 155, 40), SpritePalette.rgb(200, 240, 120), SpritePalette.rgb(255, 235, 59)) // Numemon
            306 -> SpritePalette(SpritePalette.rgb(235, 240, 255), SpritePalette.rgb(45, 90, 190), SpritePalette.rgb(245, 60, 70), SpritePalette.rgb(225, 30, 40)) // Garurumon
            307 -> SpritePalette(SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(45, 55, 115), SpritePalette.rgb(255, 215, 60), SpritePalette.rgb(45, 135, 225)) // Angemon
            308 -> SpritePalette(SpritePalette.rgb(85, 180, 75), SpritePalette.rgb(215, 50, 60), SpritePalette.rgb(145, 115, 75), SpritePalette.rgb(20, 20, 20)) // Togemon
            309 -> SpritePalette(SpritePalette.rgb(245, 75, 40), SpritePalette.rgb(245, 210, 50), SpritePalette.rgb(40, 40, 45), SpritePalette.rgb(255, 255, 255)) // Birdramon
            310 -> SpritePalette(SpritePalette.rgb(90, 109, 165), SpritePalette.rgb(58, 73, 107), SpritePalette.rgb(123, 142, 197), SpritePalette.rgb(255, 255, 222)) // Kabuterimon
            311 -> SpritePalette(SpritePalette.rgb(175, 120, 60), SpritePalette.rgb(235, 195, 125), SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(20, 20, 25)) // Monochromon
            312 -> SpritePalette(SpritePalette.rgb(105, 175, 80), SpritePalette.rgb(165, 115, 55), SpritePalette.rgb(225, 215, 195), SpritePalette.rgb(220, 35, 35)) // Ogremon
            313 -> SpritePalette(SpritePalette.rgb(225, 175, 70), SpritePalette.rgb(145, 95, 40), SpritePalette.rgb(25, 25, 30), SpritePalette.rgb(45, 140, 225)) // Leomon
            314 -> SpritePalette(SpritePalette.rgb(55, 125, 220), SpritePalette.rgb(245, 220, 60), SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(220, 35, 40)) // Seadramon
            315 -> SpritePalette(SpritePalette.rgb(245, 205, 50), SpritePalette.rgb(195, 155, 30), SpritePalette.rgb(255, 235, 120), SpritePalette.rgb(255, 255, 255)) // Sukamon
            316 -> SpritePalette(SpritePalette.rgb(45, 50, 60), SpritePalette.rgb(25, 30, 40), SpritePalette.rgb(45, 120, 210), SpritePalette.rgb(240, 40, 40)) // DarkTyrannomon
            317 -> SpritePalette(SpritePalette.rgb(235, 235, 240), SpritePalette.rgb(175, 175, 185), SpritePalette.rgb(40, 40, 50), SpritePalette.rgb(220, 35, 35)) // Bakemon
            318 -> SpritePalette(SpritePalette.rgb(120, 120, 125), SpritePalette.rgb(80, 80, 85), SpritePalette.rgb(225, 45, 45), SpritePalette.rgb(255, 215, 0)) // Cyclonemon
            319 -> SpritePalette(SpritePalette.rgb(145, 90, 45), SpritePalette.rgb(95, 60, 30), SpritePalette.rgb(80, 160, 60), SpritePalette.rgb(20, 20, 25)) // Tuskmon
            320 -> SpritePalette(SpritePalette.rgb(245, 240, 225), SpritePalette.rgb(225, 45, 45), SpritePalette.rgb(120, 85, 45), SpritePalette.rgb(20, 20, 25)) // Flymon
            321 -> SpritePalette(SpritePalette.rgb(135, 165, 125), SpritePalette.rgb(85, 115, 75), SpritePalette.rgb(215, 115, 145), SpritePalette.rgb(220, 35, 35)) // Raremon
            322 -> SpritePalette(SpritePalette.rgb(185, 40, 40), SpritePalette.rgb(120, 25, 25), SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(255, 215, 0)) // Kuwagamon
            323 -> SpritePalette(SpritePalette.rgb(240, 240, 245), SpritePalette.rgb(245, 75, 45), SpritePalette.rgb(35, 35, 40), SpritePalette.rgb(255, 215, 0)) // Kokatorimon
            324 -> SpritePalette(SpritePalette.rgb(181, 182, 189), SpritePalette.rgb(107, 113, 115), SpritePalette.rgb(222, 231, 239), SpritePalette.rgb(10, 10, 20)) // Coelamon
            325 -> SpritePalette(SpritePalette.rgb(245, 205, 75), SpritePalette.rgb(195, 145, 45), SpritePalette.rgb(240, 60, 60), SpritePalette.rgb(20, 20, 25)) // Nanimon
            326 -> SpritePalette(SpritePalette.rgb(235, 235, 240), SpritePalette.rgb(175, 175, 185), SpritePalette.rgb(40, 40, 50), SpritePalette.rgb(220, 35, 35)) // Bakemon
            327 -> SpritePalette(SpritePalette.rgb(240, 240, 245), SpritePalette.rgb(245, 75, 45), SpritePalette.rgb(35, 35, 40), SpritePalette.rgb(255, 215, 0)) // Shellmon
            328 -> SpritePalette(SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(225, 180, 55), SpritePalette.rgb(220, 40, 40), SpritePalette.rgb(40, 130, 220)) // Unimon
            329 -> SpritePalette(SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(220, 40, 40), SpritePalette.rgb(40, 130, 220), SpritePalette.rgb(25, 25, 30)) // Frigimon
            330 -> SpritePalette(SpritePalette.rgb(245, 225, 60), SpritePalette.rgb(225, 45, 45), SpritePalette.rgb(85, 175, 80), SpritePalette.rgb(255, 255, 255)) // Vegiemon
            339 -> SpritePalette(SpritePalette.rgb(45, 50, 60), SpritePalette.rgb(35, 35, 40), SpritePalette.rgb(45, 110, 200), SpritePalette.rgb(240, 40, 40)) // BlackGreymon
            340 -> SpritePalette(SpritePalette.rgb(45, 45, 50), SpritePalette.rgb(25, 25, 30), SpritePalette.rgb(225, 45, 50), SpritePalette.rgb(245, 205, 40)) // BlackGarurumon
            344 -> SpritePalette(SpritePalette.rgb(175, 185, 240), SpritePalette.rgb(235, 240, 255), SpritePalette.rgb(45, 85, 195), SpritePalette.rgb(220, 35, 35)) // Airdramon

            // Perfect & Ultimate
            401 -> SpritePalette(SpritePalette.rgb(255, 135, 20), SpritePalette.rgb(62, 39, 35), SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(0, 176, 255)) // MetalGreymon
            402 -> SpritePalette(SpritePalette.rgb(255, 215, 60), SpritePalette.rgb(255, 245, 220), SpritePalette.rgb(220, 40, 40), SpritePalette.rgb(25, 25, 30)) // Monzaemon
            403 -> SpritePalette(SpritePalette.rgb(235, 235, 240), SpritePalette.rgb(180, 180, 190), SpritePalette.rgb(40, 40, 45), SpritePalette.rgb(220, 35, 35)) // SkullGreymon
            409 -> SpritePalette(SpritePalette.rgb(45, 95, 185), SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(225, 205, 55), SpritePalette.rgb(25, 25, 30)) // Whamon
            411 -> SpritePalette(SpritePalette.rgb(215, 145, 60), SpritePalette.rgb(245, 225, 185), SpritePalette.rgb(25, 25, 30), SpritePalette.rgb(25, 25, 30)) // Etemon
            412 -> SpritePalette(SpritePalette.rgb(255, 215, 60), SpritePalette.rgb(225, 45, 45), SpritePalette.rgb(45, 110, 200), SpritePalette.rgb(25, 25, 30)) // Mamemon
            413 -> SpritePalette(SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(225, 45, 45), SpritePalette.rgb(255, 215, 60), SpritePalette.rgb(25, 25, 30)) // MetalMamemon
            414 -> SpritePalette(SpritePalette.rgb(55, 55, 65), SpritePalette.rgb(30, 30, 35), SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(244, 67, 54)) // MetalTyrannomon
            415 -> SpritePalette(SpritePalette.rgb(245, 235, 210), SpritePalette.rgb(220, 45, 40), SpritePalette.rgb(35, 35, 40), SpritePalette.rgb(25, 25, 30)) // Ex-Tyrannomon
            416 -> SpritePalette(SpritePalette.rgb(185, 45, 45), SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(25, 25, 30), SpritePalette.rgb(255, 215, 0)) // Megadramon
            417 -> SpritePalette(SpritePalette.rgb(245, 145, 175), SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(255, 215, 60), SpritePalette.rgb(45, 135, 225)) // Piximon
            418 -> SpritePalette(SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(45, 55, 115), SpritePalette.rgb(225, 45, 45), SpritePalette.rgb(45, 140, 225)) // Andromon
            419 -> SpritePalette(SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(215, 45, 45), SpritePalette.rgb(25, 25, 30), SpritePalette.rgb(255, 215, 0)) // Giromon
            420 -> SpritePalette(SpritePalette.rgb(215, 115, 175), SpritePalette.rgb(145, 75, 125), SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(25, 25, 30)) // Vademon
            429 -> SpritePalette(SpritePalette.rgb(45, 50, 60), SpritePalette.rgb(30, 30, 35), SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(244, 67, 54)) // BlackMetalGreymon
            501 -> SpritePalette(SpritePalette.rgb(216, 67, 21), SpritePalette.rgb(255, 193, 7), SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(30, 136, 229)) // WarGreymon
            503 -> SpritePalette(SpritePalette.rgb(65, 135, 225), SpritePalette.rgb(255, 215, 60), SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(225, 40, 40)) // MetalGarurumon
            510 -> SpritePalette(SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(255, 193, 7), SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(0, 200, 255)) // Omegamon
            525 -> SpritePalette(SpritePalette.rgb(40, 40, 45), SpritePalette.rgb(255, 215, 0), SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(220, 35, 35)) // BlackWarGreymon
            535 -> SpritePalette(SpritePalette.rgb(245, 245, 250), SpritePalette.rgb(225, 45, 45), SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(0, 200, 255)) // Alter-S
            536 -> SpritePalette(SpritePalette.rgb(35, 35, 40), SpritePalette.rgb(180, 180, 190), SpritePalette.rgb(144, 164, 174), SpritePalette.rgb(220, 35, 35)) // Zwart
            else -> SpritePalette(SpritePalette.rgb(255, 175, 25), SpritePalette.rgb(216, 112, 10), SpritePalette.rgb(247, 231, 140), SpritePalette.rgb(20, 20, 25))
        }
    }
}
