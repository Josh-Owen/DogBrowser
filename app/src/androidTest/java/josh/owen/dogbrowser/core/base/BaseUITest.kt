package josh.owen.dogbrowser.core.base

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import josh.owen.dogbrowser.dispatchers.SuccessDispatcher
import josh.owen.dogbrowser.ui.MainActivity
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseUITest {

    var activityRule = ActivityScenarioRule(MainActivity::class.java)


    val mockWebServer = MockWebServer()

    var idlingRegistry = IdlingRegistry.getInstance()

    @Before
    fun setup() {
        mockWebServer.dispatcher = SuccessDispatcher()
        mockWebServer.start(8080)
        idlingRegistry = IdlingRegistry.getInstance()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        idlingRegistry.unregister()
        activityRule.scenario?.close()
    }
}