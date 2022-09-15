package josh.owen.dogbrowser.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import josh.owen.dogbrowser.dispatchers.TestDispatchers
import josh.owen.dogbrowser.rules.CoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

open class BaseUnitTest {

    var testDispatchers: TestDispatchers = TestDispatchers()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineScopeRule(testDispatchers.testDispatcher)

}