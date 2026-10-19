package com.digiwear.companion.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface PhoneDao {
    @Query("SELECT * FROM synced_pet_history ORDER BY id DESC")
    fun getAllSnapshotsFlow(): Flow<List<SyncedPetEntity>>

    @Query("SELECT * FROM synced_pet_history ORDER BY id DESC LIMIT 1")
    suspend fun getLatestSnapshot(): SyncedPetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: SyncedPetEntity)

    @Query("SELECT * FROM custom_campaigns")
    fun getAllCampaignsFlow(): Flow<List<CustomCampaignEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCampaign(campaign: CustomCampaignEntity)

    @Query("DELETE FROM custom_campaigns WHERE campaignId = :campaignId")
    suspend fun deleteCampaign(campaignId: String)

    @Query("SELECT * FROM digi_vault ORDER BY storedTimestampUtc DESC")
    fun getAllVaultPetsFlow(): Flow<List<StoredDigimonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaultPet(pet: StoredDigimonEntity): Long

    @Query("DELETE FROM digi_vault WHERE id = :id")
    suspend fun deleteVaultPet(id: Long)
}

@Database(
    entities = [SyncedPetEntity::class, CustomCampaignEntity::class, StoredDigimonEntity::class],
    version = 2,
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
