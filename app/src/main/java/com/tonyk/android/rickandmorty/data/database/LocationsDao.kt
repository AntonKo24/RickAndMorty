package com.tonyk.android.rickandmorty.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tonyk.android.rickandmorty.model.location.LocationEntity

@Dao
interface LocationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(characters: List<LocationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(locationEntity: LocationEntity)

    @Query(
        "SELECT * FROM locations WHERE " +
                "(:name IS NULL OR lower(name) LIKE '%' || lower(:name) || '%') AND " +
                "(:type IS NULL OR lower(type) = lower(:type)) AND " +
                "(:dimension IS NULL OR lower(dimension) = lower(:dimension))"
    )
    fun getAllLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): PagingSource<Int, LocationEntity>

    @Query("SELECT * FROM locations WHERE id IN (:id) ")
    suspend fun getLocationByID(id: Int): LocationEntity?

}