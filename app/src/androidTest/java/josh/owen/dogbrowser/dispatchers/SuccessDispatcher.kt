package josh.owen.dogbrowser.dispatchers

import android.net.Uri
import josh.owen.dogbrowser.base.DEFAULT_NUMBER_OF_DOGS_IN_GALLERY
import josh.owen.dogbrowser.core.SELECTED_BREED
import josh.owen.dogbrowser.utils.fileio.FileReader
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class SuccessDispatcher : Dispatcher() {

    private val responseFilesByPath: Map<String, String> = mapOf(
        "/breed/$SELECTED_BREED/images/random/$DEFAULT_NUMBER_OF_DOGS_IN_GALLERY" to "network_files/Fetchdogbreedimages200.json",
        "/breeds/list/all" to "network_files/Fetchdogbreeds200.json"
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