package com.tonyk.android.rickandmorty.database

import android.util.Log
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tonyk.android.rickandmorty.model.CharacterEntity

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters WHERE " +
            "(:name IS NULL OR name = :name) AND " +
            "(:status IS NULL OR status = :status) AND " +
            "(:species IS NULL OR species = :species) AND " +
            "(:type IS NULL OR type = :type) AND " +
            "(:gender IS NULL OR gender = :gender)")
    fun getAllCharacters(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): PagingSource<Int, CharacterEntity>




    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()

    @Transaction
    suspend fun refresh(characters: List<CharacterEntity>) {
        clearAllCharacters()
        insertCharacters(characters)
    }
}

