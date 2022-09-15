package josh.owen.dogbrowser.tests.mappers

import josh.owen.dogbrowser.base.BaseUnitTest
import josh.owen.dogbrowser.data.DogBreed
import josh.owen.dogbrowser.mappers.DogBreedMapper
import josh.owen.dogbrowser.retrofit.apis.dog.responses.DogBreedsApiResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotSame
import org.junit.Test

class DogBreedMapperShould : BaseUnitTest() {

    private val mapper = DogBreedMapper()

    private val dogBreed1 = "affenpinscher"
    private val dogBreed2 = "african"
    private val dogBreed3 = "airedale"
    private val dogBreed4 = "australian"

    private val subBreed1 = "shepherd"

    private val preProcessedApiResponse: DogBreedsApiResponse = DogBreedsApiResponse(
        mapOf(
            Pair(dogBreed1, listOf()),
            Pair(dogBreed2, listOf()),
            Pair(dogBreed3, listOf()),
            Pair(dogBreed4, listOf(subBreed1)),
        ), true
    )

    private val mappedResponse = mapper.invoke(preProcessedApiResponse)

    private val expectedResponse: List<DogBreed> = listOf(
        DogBreed(dogBreed1, listOf()),
        DogBreed(dogBreed2, listOf()),
        DogBreed(dogBreed3, listOf()),
        DogBreed(dogBreed4, listOf(subBreed1))
    )

    @Test
    fun hasExpectedDataPreMapping() {
        assertEquals(mappedResponse, expectedResponse)
    }

    @Test
    fun doesMapIncorrectly() {
        assertNotSame(listOf<DogBreed>(), expectedResponse)
    }

    @Test
    fun doesMapCorrectDog1() {
        assertEquals(
            expectedResponse.find { it.breedName == dogBreed1 },
            mappedResponse.find { it.breedName == dogBreed1 })
    }

    @Test
    fun doesMapCorrectDog2() {
        assertEquals(
            expectedResponse.find { it.breedName == dogBreed2 },
            mappedResponse.find { it.breedName == dogBreed2 })
    }

    @Test
    fun doesMapCorrectDog3() {
        assertEquals(
            expectedResponse.find { it.breedName == dogBreed3 },
            mappedResponse.find { it.breedName == dogBreed3 })
    }

    @Test
    fun doesMapCorrectDog4() {
        assertEquals(
            expectedResponse.find { it.breedName == dogBreed4 },
            mappedResponse.find { it.breedName == dogBreed4 })
    }
}