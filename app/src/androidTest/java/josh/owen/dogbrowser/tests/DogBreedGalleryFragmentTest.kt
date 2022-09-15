package josh.owen.dogbrowser.tests

import android.content.res.Resources
import android.view.View
import android.widget.ProgressBar
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import josh.owen.dogbrowser.R
import josh.owen.dogbrowser.core.base.BaseUITest
import josh.owen.dogbrowser.utils.nthChildOf
import josh.owen.dogbrowser.utils.views.ViewVisibilityIdlingResource
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
@LargeTest
class DogBreedGalleryFragmentTest : BaseUITest() {

    @get:Rule
    var hiltRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this)).around(activityRule)


    private val selectedDogBreed = "affenpinscher"

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
                    selectedDogBreed
                )
            )
        )
    }

    @Test
    fun doesDisplayGalleryImages() {

    }

    //endregion

    // Navigation
    private fun navigateToSelectedBreedGallery() {

        activityRule.scenario.onActivity {
            idlingRegistry.register(
                ViewVisibilityIdlingResource(
                    it.findViewById<ProgressBar>(R.id.pbLoadingBreedNames),
                    View.GONE
                )
            )
        }

        Espresso.onView(
            AllOf.allOf(
                ViewMatchers.withId(R.id.tvDogBreed),
                ViewMatchers.isDescendantOfA(
                    nthChildOf(
                        ViewMatchers.withId(R.id.rvDogBreedNames),
                        1
                    )
                )
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText(selectedDogBreed)))
            .perform(ViewActions.click())

    }
    //endregion

}