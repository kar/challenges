package gs.kar.justeatrecruitmenttest.userstory;

import android.support.annotation.Nullable;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import gs.kar.justeatrecruitmenttest.action.DisplayErrorAction;
import gs.kar.justeatrecruitmenttest.action.DisplayRestaurantsAction;
import gs.kar.justeatrecruitmenttest.action.FetchLocationAction;
import gs.kar.justeatrecruitmenttest.action.FetchRestaurantsAction;
import gs.kar.justeatrecruitmenttest.model.Location;
import gs.kar.justeatrecruitmenttest.model.Restaurant;

/**
 * ViewListOfRestaurantsTest tests the main user story logic.
 */
public class ViewListOfRestaurantsTest {

	/**
	 * testFailFlow checks if we reach DisplayErrorAction when first action fails.
	 */
	@Test public void testFailFlow() {
		FetchLocationAction fLocA = new FetchLocationAction() {
			@Override public void perform(@Nullable Void input, @Nullable Result<Location> result) {
				result.fail("fetching location failed");
			}
		};

		FetchRestaurantsAction fResA = new FetchRestaurantsAction() {
			@Override public void perform(@Nullable Location input, @Nullable Result<List<Restaurant>> result) {
				Assert.fail("should never fetch restaurants");
			}
		};

		DisplayRestaurantsAction dResA = new DisplayRestaurantsAction() {
			@Override public void perform(@Nullable List<Restaurant> input, @Nullable Result<Void> result) {
				Assert.fail("should never display restaurants");
			}
		};

		DisplayErrorAction dErrA = Mockito.mock(DisplayErrorAction.class);

		UserStory story = new ViewListOfRestaurants(fLocA, fResA, dResA, dErrA);
		story.perform();
		Mockito.verify(dErrA).perform("fetching location failed", null);
	}

	/**
	 * testFailFlow2 checks if we reach DisplayErrorAction when last action fails.
	 */
	@Test public void testFailFlow2() {
		FetchLocationAction fLocA = new FetchLocationAction() {
			@Override public void perform(@Nullable Void input, @Nullable Result<Location> result) {
				result.on(new Location("mocked"));
			}
		};

		FetchRestaurantsAction fResA = new FetchRestaurantsAction() {
			@Override public void perform(@Nullable Location input, @Nullable Result<List<Restaurant>> result) {
				result.on(new LinkedList<Restaurant>());
			}
		};

		DisplayRestaurantsAction dResA = new DisplayRestaurantsAction() {
			@Override public void perform(@Nullable List<Restaurant> input, @Nullable Result<Void> result) {
				result.fail("could not display restaurants");
			}
		};

		DisplayErrorAction dErrA = Mockito.mock(DisplayErrorAction.class);

		UserStory story = new ViewListOfRestaurants(fLocA, fResA, dResA, dErrA);
		story.perform();
		Mockito.verify(dErrA).perform("could not display restaurants", null);
	}

	/**
	 * testArgumentsPassed checks if arguments are passed correctly between actions.
	 */
	private boolean reachedLastAction;
	private Location mockedLocation = new Location("mocked");
	private List<Restaurant> mockedRestaurants = new LinkedList<>();
	@Test public void testArgumentsPassed() {
		reachedLastAction = false;
		FetchLocationAction fLocA = new FetchLocationAction() {
			@Override public void perform(@Nullable Void input, @Nullable Result<Location> result) {
				result.on(mockedLocation);
			}
		};

		FetchRestaurantsAction fResA = new FetchRestaurantsAction() {
			@Override public void perform(@Nullable Location input, @Nullable Result<List<Restaurant>> result) {
				Assert.assertEquals(mockedLocation, input);
				result.on(mockedRestaurants);
			}
		};

		DisplayRestaurantsAction dResA = new DisplayRestaurantsAction() {
			@Override public void perform(@Nullable List<Restaurant> input, @Nullable Result<Void> result) {
				Assert.assertEquals(mockedRestaurants, input);
				reachedLastAction = true;
				result.on(null);
			}
		};

		DisplayErrorAction dErrA = Mockito.mock(DisplayErrorAction.class);

		UserStory story = new ViewListOfRestaurants(fLocA, fResA, dResA, dErrA);
		story.perform();
		Assert.assertTrue(reachedLastAction);
	}
}
