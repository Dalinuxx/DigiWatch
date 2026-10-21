package com.digiwear.companion.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PhoneDao {
    @Query("SELECT * FROM synced_pet_history ORDER BY id DESC LIMIT 50")
    fun getAllSnapshotsFlow(): Flow<List<SyncedPetEntity>>

    @Query("SELECT * FROM synced_pet_history ORDER BY id DESC LIMIT 1")
    suspend fun getLatestSnapshot(): SyncedPetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshotInternal(snapshot: SyncedPetEntity)

    @Transaction
    suspend fun insertSnapshot(snapshot: SyncedPetEntity) {
        insertSnapshotInternal(snapshot)
        recordEncounter(snapshot.speciesId, isRaised = true)
    }

    @Query("SELECT * FROM custom_campaigns")
    fun getAllCampaignsFlow(): Flow<List<CustomCampaignEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCampaign(campaign: CustomCampaignEntity)

    @Query("DELETE FROM custom_campaigns WHERE campaignId = :campaignId")
    suspend fun deleteCampaign(campaignId: String)

    @Query("SELECT * FROM digi_vault ORDER BY storedTimestampUtc DESC")
    fun getAllVaultPetsFlow(): Flow<List<StoredDigimonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaultPetInternal(pet: StoredDigimonEntity): Long

    @Transaction
    suspend fun insertVaultPet(pet: StoredDigimonEntity): Long {
        val id = insertVaultPetInternal(pet)
        recordEncounter(pet.speciesId, isRaised = true)
        return id
    }

    @Query("DELETE FROM digi_vault WHERE id = :id")
    suspend fun deleteVaultPet(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemorial(memorial: CompanionMemorialEntity): Long

    @Query("SELECT * FROM companion_memorial_graveyard ORDER BY timestampUtc DESC")
    fun getAllMemorialsFlow(): Flow<List<CompanionMemorialEntity>>

    @Query("SELECT * FROM companion_memorial_graveyard ORDER BY timestampUtc DESC")
    suspend fun getAllMemorials(): List<CompanionMemorialEntity>

    // 550-Species DigiDex Discovery Tracking
    @Query("SELECT * FROM discovered_species")
    fun getAllDiscoveredFlow(): Flow<List<DiscoveredSpeciesEntity>>

    @Query("SELECT * FROM discovered_species")
    suspend fun getAllDiscovered(): List<DiscoveredSpeciesEntity>

    @Query("SELECT * FROM discovered_species WHERE speciesId = :speciesId LIMIT 1")
    suspend fun getDiscovered(speciesId: Int): DiscoveredSpeciesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordDiscovery(entity: DiscoveredSpeciesEntity)

    suspend fun recordEncounter(speciesId: Int, isRaised: Boolean = false, isDefeated: Boolean = false) {
        val existing = getDiscovered(speciesId)
        val now = System.currentTimeMillis() / 1000L
        if (existing == null) {
            recordDiscovery(
                DiscoveredSpeciesEntity(
                    speciesId = speciesId,
                    discoveredTimestampUtc = now,
                    timesRaised = if (isRaised) 1 else 0,
                    timesDefeated = if (isDefeated) 1 else 0
                )
            )
        } else {
            recordDiscovery(
                existing.copy(
                    timesRaised = existing.timesRaised + (if (isRaised) 1 else 0),
                    timesDefeated = existing.timesDefeated + (if (isDefeated) 1 else 0)
                )
            )
        }
    }
}

@Database(
    entities = [
        SyncedPetEntity::class,
        CustomCampaignEntity::class,
        StoredDigimonEntity::class,
        CompanionMemorialEntity::class,
        DiscoveredSpeciesEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class PhoneDatabase : RoomDatabase() {
    abstract fun phoneDao(): PhoneDao

    companion object {
        @Volatile
        private var instance: PhoneDatabase? = null

        fun getInstance(context: Context): PhoneDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PhoneDatabase::class.java,
                    "digiwear_companion.db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }
}
