package josh.owen.dogbrowser.core.base

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import josh.owen.dogbrowser.dispatchers.SuccessDispatcher
import josh.owen.dogbrowser.ui.MainActivity
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.RuleChain
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