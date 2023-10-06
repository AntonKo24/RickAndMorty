package com.tonyk.android.rickandmorty.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity

@Dao
interface EpisodesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<EpisodeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: EpisodeEntity)

    @Query(
        "SELECT * FROM episodes WHERE " +
                "(:name IS NULL OR lower(name) LIKE '%' || lower(:name) || '%') AND " +
                "(:episode IS NULL OR lower(episode) = lower(:episode)) "
    )
    fun getAllEpisodes(
        name: String?,
        episode: String?
    ): PagingSource<Int, EpisodeEntity>

    @Query("SELECT * FROM episodes WHERE id IN (:id) ")
    fun getEpisodesByID(id: List<String>): PagingSource<Int, EpisodeEntity>

    @Query("SELECT * FROM episodes WHERE id IN (:id)")
    fun getEpisodeByID(id : Int) : EpisodeEntity

}
