package com.digiwear.wear.data

import androidx.sqlite.db.SupportSQLiteDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.reflect.Proxy

class DigiWearDatabaseMigrationTest {

    private fun createRecordingDb(executedSql: MutableList<String>): SupportSQLiteDatabase {
        return Proxy.newProxyInstance(
            SupportSQLiteDatabase::class.java.classLoader,
            arrayOf(SupportSQLiteDatabase::class.java)
        ) { _, method, args ->
            if (method.name == "execSQL" && args != null && args.isNotEmpty()) {
                executedSql.add(args[0] as String)
            }
            null
        } as SupportSQLiteDatabase
    }

    @Test
    fun testMigrationVersionSequencing() {
        assertEquals("MIGRATION_1_2 must transition v1 to v2", 1, MIGRATION_1_2.startVersion)
        assertEquals("MIGRATION_1_2 must transition v1 to v2", 2, MIGRATION_1_2.endVersion)

        assertEquals("MIGRATION_2_3 must transition v2 to v3", 2, MIGRATION_2_3.startVersion)
        assertEquals("MIGRATION_2_3 must transition v2 to v3", 3, MIGRATION_2_3.endVersion)

        assertEquals("MIGRATION_3_4 must transition v3 to v4", 3, MIGRATION_3_4.startVersion)
        assertEquals("MIGRATION_3_4 must transition v3 to v4", 4, MIGRATION_3_4.endVersion)
    }

    @Test
    fun testMigration1To2CreatesAdventureState() {
        val sqlList = mutableListOf<String>()
        val db = createRecordingDb(sqlList)

        MIGRATION_1_2.migrate(db)

        assertEquals(1, sqlList.size)
        assertTrue("Must create adventure_state table", sqlList[0].contains("adventure_state"))
        assertTrue("adventure_state must have id integer primary key", sqlList[0].contains("id INTEGER PRIMARY KEY NOT NULL"))
        assertTrue("adventure_state must have campaignId", sqlList[0].contains("campaignId TEXT NOT NULL"))
        assertTrue("adventure_state must have currentNodeIndex", sqlList[0].contains("currentNodeIndex INTEGER NOT NULL"))
    }

    @Test
    fun testMigration2To3CreatesMemorialGraveyard() {
        val sqlList = mutableListOf<String>()
        val db = createRecordingDb(sqlList)

        MIGRATION_2_3.migrate(db)

        assertEquals(1, sqlList.size)
        assertTrue("Must create memorial_graveyard table", sqlList[0].contains("memorial_graveyard"))
        assertTrue("memorial_graveyard must have autoincrement id", sqlList[0].contains("id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"))
        assertTrue("memorial_graveyard must contain timestampUtc", sqlList[0].contains("timestampUtc INTEGER NOT NULL"))
    }

    @Test
    fun testMigration3To4AddsAdventureColumns() {
        val sqlList = mutableListOf<String>()
        val db = createRecordingDb(sqlList)

        MIGRATION_3_4.migrate(db)

        assertEquals(2, sqlList.size)
        assertTrue(
            "Must alter adventure_state to add highestUnlockedNodeIndex",
            sqlList[0].contains("ALTER TABLE adventure_state ADD COLUMN highestUnlockedNodeIndex INTEGER NOT NULL DEFAULT 0")
        )
        assertTrue(
            "Must alter adventure_state to add sagaStatesJson",
            sqlList[1].contains("ALTER TABLE adventure_state ADD COLUMN sagaStatesJson TEXT NOT NULL DEFAULT '{}'")
        )
    }
}
