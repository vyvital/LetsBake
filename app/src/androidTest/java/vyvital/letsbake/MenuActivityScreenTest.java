package vyvital.letsbake;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import vyvital.letsbake.fragments.RecipeFrag;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MenuActivityScreenTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void init() {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content, new RecipeFrag()).commit();

    }

    @Test
    public void clickGridViewItem_OpensOrderActivity() throws InterruptedException {
      //  Thread.sleep(500);
        Espresso.registerIdlingResources(mActivityTestRule.getActivity().getIdlingResource());
        onView(allOf(withId(R.id.recipeRV), hasFocus())).perform(RecyclerViewActions.scrollToPosition(0), click());
        onView(withId(R.id.stepsID)).perform(RecyclerViewActions
                .actionOnItem(hasDescendant(withText("Starting prep")),
                        click()));


    }


}
