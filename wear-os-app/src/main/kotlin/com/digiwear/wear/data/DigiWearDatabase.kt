package com.digiwear.wear.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
    fun getPetStateFlow(): Flow<PetEntity?>

    @Query("SELECT * FROM pet_state WHERE id = 1 LIMIT 1")
    suspend fun getPetState(): PetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePet(pet: PetEntity)

    @Query("SELECT * FROM adventure_state WHERE id = 1 LIMIT 1")
    fun getAdventureStateFlow(): Flow<AdventureEntity?>

    @Query("SELECT * FROM adventure_state WHERE id = 1 LIMIT 1")
    suspend fun getAdventureState(): AdventureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAdventure(adventure: AdventureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemorial(memorial: MemorialEntity): Long

    @Query("SELECT * FROM memorial_graveyard ORDER BY timestampUtc DESC")
    fun getAllMemorialsFlow(): Flow<List<MemorialEntity>>

    @Query("SELECT * FROM memorial_graveyard ORDER BY timestampUtc DESC")
    suspend fun getAllMemorials(): List<MemorialEntity>
}

@Database(entities = [PetEntity::class, AdventureEntity::class, MemorialEntity::class], version = 3, exportSchema = false)
abstract class DigiWearDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
}
