package josh.owen.dogbrowser.tests

import android.content.res.Resources
import android.view.View
import android.widget.ProgressBar
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import josh.owen.dogbrowser.R
import josh.owen.dogbrowser.core.SELECTED_BREED
import josh.owen.dogbrowser.core.base.BaseUITest
import josh.owen.dogbrowser.dispatchers.LoadGalleryErrorDispatcher
import josh.owen.dogbrowser.utils.nthChildOf
import josh.owen.dogbrowser.utils.views.ViewVisibilityIdlingResource
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
@LargeTest
class BreedGalleryFragmentTest : BaseUITest() {

    //region Variables & Class Members
    @get:Rule
    var hiltRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this)).around(activityRule)

    //endregion

    //region Tests

    @Test
    fun doesDisplayScreenTitle() {
        navigateToSelectedBreedGallery()
        assertDisplayed(R.string.breed_gallery_page_title)
    }

    @Test
    fun doesDisplayCorrectSelectionFormatText() {
        navigateToSelectedBreedGallery()
        val resources: Resources =
            InstrumentationRegistry.getInstrumentation().targetContext.resources
        assertDisplayed(
            String.format(
                resources.getString(
                    R.string.breed_gallery_selected_breed,
                    SELECTED_BREED
                )
            )
        )
    }

    @Test
    fun doesDisplayProgressBar() {
        navigateToSelectedBreedGallery()
        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById<ProgressBar>(R.id.lavLoadingBreedImages),
                    View.VISIBLE
                )
            )
        }
    }

    @Test
    fun doesProgressBarDisappearAfterAppStateIsPropagated() {
        navigateToSelectedBreedGallery()
        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById<ProgressBar>(R.id.lavLoadingBreedImages),
                    View.GONE
                )
            )
        }
    }

    @Test
    fun doesDisplaySnackBarAfterFailingToFetchImageUrls() {
        mockWebServer.dispatcher = LoadGalleryErrorDispatcher()
        navigateToSelectedBreedGallery()
        Thread.sleep(1000)
        assertDisplayed(R.string.generic_network_error)
    }


    @Test
    fun doesDisplayGalleryImages() {
        navigateToSelectedBreedGallery()
        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById<ProgressBar>(R.id.lavLoadingBreedImages),
                    View.GONE
                )
            )
        }
        assertRecyclerViewItemCount(R.id.rvDogBreedImages, 10)
    }

    @Test
    fun isAbleToNavigateBackToBreedDetails() {
        navigateToSelectedBreedGallery()
        Espresso.pressBack()
        assertDisplayed(R.string.list_of_breeds_page_title)
    }

    @Test
    fun doesClickingDisplayRetryButtonDisplayProgressBar() {
        mockWebServer.dispatcher = LoadGalleryErrorDispatcher()
        navigateToSelectedBreedGallery()
        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById(R.id.btnRetryLoadImageUrls),
                    View.VISIBLE
                )
            )
        }
        onView(withId(R.id.btnRetryLoadImageUrls)).perform(ViewActions.click())
        assertDisplayed(R.id.lavLoadingBreedImages)
    }

    //endregion

    //region Navigation
    private fun navigateToSelectedBreedGallery() {
        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById<ProgressBar>(R.id.lavLoadingBreedNames),
                    View.GONE
                )
            )
        }

        onView(
            AllOf.allOf(
                ViewMatchers.withId(R.id.tvDogBreed),
                ViewMatchers.isDescendantOfA(
                    nthChildOf(
                        ViewMatchers.withId(R.id.rvDogBreedNames),
                        1
                    )
                )
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText(SELECTED_BREED)))
            .perform(ViewActions.click())

    }
    //endregion

}