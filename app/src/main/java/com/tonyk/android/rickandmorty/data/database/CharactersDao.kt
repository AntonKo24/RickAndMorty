package com.tonyk.android.rickandmorty.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tonyk.android.rickandmorty.model.character.CharacterEntity

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)
    @Query("SELECT * FROM characters WHERE " +
            "(:name IS NULL OR lower(name) LIKE '%' || lower(:name) || '%') AND " +
            "(:status IS NULL OR lower(status) = lower(:status)) AND " +
            "(:species IS NULL OR lower(species) = lower(:species)) AND " +
            "(:type IS NULL OR lower(type) = lower(:type)) AND " +
            "(:gender IS NULL OR lower(gender) = lower(:gender))")
    fun getAllCharacters(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): PagingSource<Int, CharacterEntity>
}

