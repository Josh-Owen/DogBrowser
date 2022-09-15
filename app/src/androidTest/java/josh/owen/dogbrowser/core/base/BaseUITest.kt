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

    lateinit var idlingRegistry: IdlingRegistry

    @Before
    fun setup() {
        idlingRegistry = IdlingRegistry.getInstance()
        mockWebServer.dispatcher = SuccessDispatcher()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        idlingRegistry.unregister()
        activityRule.scenario?.close()
        mockWebServer.shutdown()
    }
}