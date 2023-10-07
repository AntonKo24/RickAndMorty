package com.tonyk.android.rickandmorty.data.repository.repositoryimpl

import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.CharactersDao
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterLocation
import com.tonyk.android.rickandmorty.model.character.CharacterOrigin
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharactersRepositoryImplTest {

    private lateinit var charactersRepository: CharactersRepositoryImpl
    private val api: RickAndMortyApi = mockk()
    private val charactersDao: CharactersDao = mockk()

    @Before
    fun setup() {
        charactersRepository = CharactersRepositoryImpl(api, charactersDao)
    }

    @Test
    fun `getCharacterByID should return CharacterEntity`() = runBlocking {
        val fakeCharacter = CharacterEntity(
            id = 1,
            name = "Fake Character",
            species = "Human",
            status = "Alive",
            gender = "Male",
            image = "https://example.com/image.png",
            type = "Main Character",
            episode = listOf("S01E01", "S01E02"),
            origin = CharacterOrigin("Earth", "https://example.com/earth"),
            location = CharacterLocation("Home", "https://example.com/home")
        )
        val fakeCharacter2 = CharacterEntity(
            id = 1,
            name = "Fake Character2",
            species = "Human",
            status = "Alive",
            gender = "Male",
            image = "https://example.com/image.png",
            type = "Main Character",
            episode = listOf("S01E01", "S01E02"),
            origin = CharacterOrigin("Earth", "https://example.com/earth"),
            location = CharacterLocation("Home", "https://example.com/home")
        )

        val fakeCharacterId = 1
        val fakeCharacterId2 = 2
        val status = true

        coEvery { api.fetchCharacterByID(fakeCharacterId) } returns fakeCharacter
        coEvery { charactersDao.insertCharacter(fakeCharacter) } just runs
        coEvery { charactersDao.getCharacterByID(fakeCharacterId) } returns fakeCharacter

        val result = charactersRepository.getCharacterByID(fakeCharacterId, status)

        assertEquals(fakeCharacter, result)
    }
}
// Не относится к проекту, просто решил посмотреть, как что может работать.