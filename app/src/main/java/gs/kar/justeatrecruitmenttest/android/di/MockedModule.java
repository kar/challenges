package gs.kar.justeatrecruitmenttest.android.di;

import android.app.Activity;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import gs.kar.justeatrecruitmenttest.action.DisplayErrorAction;
import gs.kar.justeatrecruitmenttest.action.DisplayRestaurantsAction;
import gs.kar.justeatrecruitmenttest.action.FetchLocationAction;
import gs.kar.justeatrecruitmenttest.action.FetchRestaurantsAction;
import gs.kar.justeatrecruitmenttest.android.action.ToastDisplayErrorAction;
import gs.kar.justeatrecruitmenttest.model.Location;
import gs.kar.justeatrecruitmenttest.model.Restaurant;
import gs.kar.justeatrecruitmenttest.userstory.ViewListOfRestaurants;

/**
 * MockedModule is a DI module which assembles user story together and mocks various parts of it.
 *
 * This could easily be a basis for automated unit tests, however right now it's really useful for development itself.
 * We could go a step further and create a mocked module which has no Android dependencies, and easily incorporate it
 * into unit tests, without having to run anything on actual device.
 */
public class MockedModule implements Module {
	@Override public ViewListOfRestaurants inject(Activity context) {
		// return failOnFetchingLocation(context);
		// return failOnFetchingRestaurants(context);
		return failOnDisplayingRestaurants(context);
	}

	/**
	 * failOnFetchingLocation is a mocked user story which fails on fetching location and displays error message as toast.
	 */
	private ViewListOfRestaurants failOnFetchingLocation(Context context) {
		FetchLocationAction fLocA = new FetchLocationAction() {
			@Override public void perform(Void input, Result<Location> result) {
				result.fail("fetching location failed");
			}
		};

		FetchRestaurantsAction fResA = new FetchRestaurantsAction() {
			@Override public void perform(Location input, Result<List<Restaurant>> result) {
				result.fail("we shouldn't get this far");
			}
		};

		DisplayRestaurantsAction dResA = new DisplayRestaurantsAction() {
			@Override public void perform(List<Restaurant> input, Result<Void> result) {
				result.fail("we shouldn't get this far");
			}
		};

		DisplayErrorAction dErrA = new ToastDisplayErrorAction(context);

		return new ViewListOfRestaurants(fLocA, fResA, dResA, dErrA);
	}

	/**
	 * failOnFetchingRestaurants is a mocked user story which fails on fetching restaurants (a second step of the user
	 * story) and displays error message as toast.
	 */
	private ViewListOfRestaurants failOnFetchingRestaurants(Context context) {
		FetchLocationAction fLocA = new FetchLocationAction() {
			@Override public void perform(Void input, Result<Location> result) {
				result.on(new Location("fake-postcode"));
			}
		};

		FetchRestaurantsAction fResA = new FetchRestaurantsAction() {
			@Override public void perform(Location input, Result<List<Restaurant>> result) {
				result.fail("fetching restaurants failed");
			}
		};

		DisplayRestaurantsAction dResA = new DisplayRestaurantsAction() {
			@Override public void perform(List<Restaurant> input, Result<Void> result) {
				result.fail("we shouldn't get this far");
			}
		};

		DisplayErrorAction dErrA = new ToastDisplayErrorAction(context);

		return new ViewListOfRestaurants(fLocA, fResA, dResA, dErrA);
	}

	/**
	 * failOnDisplayingRestaurants is a mocked user story which fails on displaying restaurants (a third step of the user
	 * story) and displays error message as toast.
	 */
	private ViewListOfRestaurants failOnDisplayingRestaurants(Context context) {
		FetchLocationAction fLocA = new FetchLocationAction() {
			@Override public void perform(Void input, Result<Location> result) {
				result.on(new Location("fake-postcode"));
			}
		};

		FetchRestaurantsAction fResA = new FetchRestaurantsAction() {
			@Override public void perform(Location input, Result<List<Restaurant>> result) {
				result.on(new LinkedList<Restaurant>());
			}
		};

		DisplayRestaurantsAction dResA = new DisplayRestaurantsAction() {
			@Override public void perform(List<Restaurant> input, Result<Void> result) {
				result.fail("displaying restaurants failed");
			}
		};

		DisplayErrorAction dErrA = new ToastDisplayErrorAction(context);

		return new ViewListOfRestaurants(fLocA, fResA, dResA, dErrA);
	}
}
