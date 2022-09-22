package josh.owen.dogbrowser.dispatchers

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class ErrorDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return MockResponse()
            .setResponseCode(404)
            .setBody(DISPATCHER_GENERIC_ERROR)
    }

    companion object {
        const val DISPATCHER_GENERIC_ERROR = "Client Error"
    }
}