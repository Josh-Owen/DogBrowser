package josh.owen.dogbrowser.dispatchers

import android.net.Uri
import josh.owen.dogbrowser.retrofit.apis.dog.DOG_API_FETCH_ALL_DOG_BY_BREED
import josh.owen.dogbrowser.retrofit.apis.dog.DOG_API_FETCH_IMAGES_BY_BREED
import josh.owen.dogbrowser.utils.fileio.FileReader
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class SuccessDispatcher : Dispatcher() {

    private val responseFilesByPath: Map<String, String> = mapOf(
        "/$DOG_API_FETCH_IMAGES_BY_BREED" to "network_files/Fetchdogbreedimages200.json",
        "/$DOG_API_FETCH_ALL_DOG_BY_BREED" to "network_files/Fetchdogbreeds200.json"
    )

    override fun dispatch(request: RecordedRequest): MockResponse {
        val errorResponse = MockResponse().setResponseCode(404)
        val pathWithoutQueryParams = Uri.parse(request.path).path ?: return errorResponse
        val responseFile = responseFilesByPath[pathWithoutQueryParams]

        return if (responseFile != null) {
            val responseBody = FileReader.readStringFromFile(responseFile)
            MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
        } else {
            errorResponse
        }
    }
}