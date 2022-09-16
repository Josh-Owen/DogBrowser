package josh.owen.dogbrowser.tests

import android.view.View
import android.widget.ProgressBar
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.airbnb.lottie.LottieAnimationView
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import josh.owen.dogbrowser.R
import josh.owen.dogbrowser.core.base.BaseUITest
import josh.owen.dogbrowser.dispatchers.ErrorDispatcher
import josh.owen.dogbrowser.utils.nthChildOf
import josh.owen.dogbrowser.utils.views.ViewVisibilityIdlingResource
import kotlinx.coroutines.delay
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
@LargeTest
class BreedsListFragmentTest : BaseUITest() {

    //region Variables & Class Members
    @get:Rule
    var hiltRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this)).around(activityRule)

    //endregion

    //region Tests
    @Test
    fun doesDisplayScreenTitle() {
        assertDisplayed(R.string.list_of_breeds_page_title)
    }

    @Test
    fun doesDisplayProgressBar() {
        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById<ProgressBar>(R.id.lavLoadingBreedNames),
                    View.VISIBLE
                )
            )
        }
    }

    @Test
    fun isProgressBarHiddenAfterLoading() {
        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById<ProgressBar>(R.id.lavLoadingBreedNames),
                    View.VISIBLE
                )
            )
        }
        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById<ProgressBar>(R.id.lavLoadingBreedNames),
                    View.GONE
                )
            )
        }
    }

    @Test
    fun doesDisplayRetryButtonAfterFailingToFetchNames() {
        mockWebServer.dispatcher = ErrorDispatcher()
        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById<ProgressBar>(R.id.btnRetryLoadingBreedList),
                    View.VISIBLE
                )
            )
        }
    }

    @Test
    fun doesDisplaySnackBarAfterFailingToFetchNames() {
        mockWebServer.dispatcher = ErrorDispatcher()
        Thread.sleep(1000)
        assertDisplayed(R.string.generic_network_error)
    }

    @Test
    fun doesDisplayListOfDogBreeds() {
        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById<ProgressBar>(R.id.lavLoadingBreedNames),
                    View.GONE
                )
            )
        }
        assertRecyclerViewItemCount(R.id.rvDogBreedNames, 97)

        Espresso.onView(
            AllOf.allOf(
                withId(R.id.tvDogBreedTitle),
                ViewMatchers.isDescendantOfA(nthChildOf(withId(R.id.rvDogBreedNames), 0))
            )
        ).check(ViewAssertions.matches(withText(R.string.list_of_breeds_breeds_title)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(
            AllOf.allOf(
                withId(R.id.tvSubBreedsTitle),
                ViewMatchers.isDescendantOfA(nthChildOf(withId(R.id.rvDogBreedNames), 0))
            )
        ).check(ViewAssertions.matches(withText(R.string.list_of_breeds_sub_breed_title)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(
            AllOf.allOf(
                withId(R.id.tvDogBreed),
                ViewMatchers.isDescendantOfA(nthChildOf(withId(R.id.rvDogBreedNames), 1))
            )
        ).check(ViewAssertions.matches(withText("affenpinscher")))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(
            AllOf.allOf(
                withId(R.id.tvDogBreed),
                ViewMatchers.isDescendantOfA(nthChildOf(withId(R.id.rvDogBreedNames), 11))
            )
        ).check(ViewAssertions.matches(withText("bouvier")))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(
            AllOf.allOf(
                withId(R.id.tvDogBreed),
                ViewMatchers.isDescendantOfA(nthChildOf(withId(R.id.rvDogBreedNames), 3))
            )
        ).check(ViewAssertions.matches(withText("airedale")))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(
            AllOf.allOf(
                withId(R.id.tvDogBreed),
                ViewMatchers.isDescendantOfA(nthChildOf(withId(R.id.rvDogBreedNames), 4))
            )
        ).check(ViewAssertions.matches(withText("akita")))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(
            AllOf.allOf(
                withId(R.id.tvDogBreed),
                ViewMatchers.isDescendantOfA(nthChildOf(withId(R.id.rvDogBreedNames), 5))
            )
        ).check(ViewAssertions.matches(withText("appenzeller")))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    //endregion
}