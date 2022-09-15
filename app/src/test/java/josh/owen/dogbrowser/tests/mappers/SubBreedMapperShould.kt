package josh.owen.dogbrowser.tests.mappers

import josh.owen.dogbrowser.base.BaseUnitTest
import josh.owen.dogbrowser.data.SubBreed
import josh.owen.dogbrowser.mappers.SubBreedMapper
import josh.owen.dogbrowser.retrofit.apis.dog.responses.DogBreedImagesApiResponse
import junit.framework.TestCase.*
import org.junit.Test

class SubBreedMapperShould : BaseUnitTest() {

    private val mapper = SubBreedMapper()

    private val dogImageURL0 = "https://images.dog.ceo/breeds/hound-walker/n02089867_1988.jpg"
    private val dogImageURL1 = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg"
    private val dogImageURL2 = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1007.jpg"
    private val dogImageURL3 = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1023.jpg"
    private val dogImageURL4 = "https://images.dog.ceo/breeds/hound-afghan/n02088094_10263.jpg"
    private val dogImageURL5 = "https://images.dog.ceo/breeds/hound-walker/n02089867_1988.jpg"
    private val dogImageURL6 = "https://images.dog.ceo/breeds/hound-walker/n02089867_1988.jpg"
    private val dogImageURL7 = "https://images.dog.ceo/breeds/hound-walker/n02089867_1988.jpg"
    private val dogImageURL8 = "https://images.dog.ceo/breeds/hound-walker/n02089867_1988.jpg"
    private val dogImageURL9 = "https://images.dog.ceo/breeds/hound-walker/n02089867_1988.jpg"


    private val preProcessedUrls =
        listOf(
            dogImageURL0,
            dogImageURL1,
            dogImageURL2,
            dogImageURL3,
            dogImageURL4,
            dogImageURL5,
            dogImageURL6,
            dogImageURL7,
            dogImageURL8,
            dogImageURL9
        )


    private val preProcessedApiResponse: DogBreedImagesApiResponse =
        DogBreedImagesApiResponse(preProcessedUrls, true)

    private val mappedResponse = mapper.invoke(preProcessedApiResponse)

    private val expectedResponse: List<SubBreed> = listOf(
        SubBreed(dogImageURL0),
        SubBreed(dogImageURL1),
        SubBreed(dogImageURL2),
        SubBreed(dogImageURL3),
        SubBreed(dogImageURL4),
        SubBreed(dogImageURL5),
        SubBreed(dogImageURL6),
        SubBreed(dogImageURL7),
        SubBreed(dogImageURL8),
        SubBreed(dogImageURL9),
    )

    @Test
    fun hasExpectedDataPreMapping() {
        assertEquals(mappedResponse, expectedResponse)
    }

    @Test
    fun doesMapIncorrectly() {
        assertNotSame(listOf<SubBreed>(), expectedResponse)
    }

    @Test
    fun doesMapUrl0Correctly() {
        assertTrue(preProcessedUrls[0] == expectedResponse[0].url)
    }

    @Test
    fun doesMapUrl1Correctly() {
        assertTrue(preProcessedUrls[1] == expectedResponse[1].url)
    }

    @Test
    fun doesMapUrl2Correctly() {
        assertTrue(preProcessedUrls[2] == expectedResponse[2].url)
    }

    @Test
    fun doesMapUrl3Correctly() {
        assertTrue(preProcessedUrls[3] == expectedResponse[3].url)
    }

    @Test
    fun doesMapUrl4Correctly() {
        assertTrue(preProcessedUrls[4] == expectedResponse[4].url)
    }

    @Test
    fun doesMapUrl5Correctly() {
        assertTrue(preProcessedUrls[5] == expectedResponse[5].url)
    }

    @Test
    fun doesMapUrl6Correctly() {
        assertTrue(preProcessedUrls[6] == expectedResponse[6].url)
    }

    @Test
    fun doesMapUrl7Correctly() {
        assertTrue(preProcessedUrls[7] == expectedResponse[7].url)
    }

    @Test
    fun doesMapUrl8Correctly() {
        assertTrue(preProcessedUrls[8] == expectedResponse[8].url)
    }

    @Test
    fun doesMapUrl9Correctly() {
        assertTrue(preProcessedUrls[9] == expectedResponse[9].url)
    }
}