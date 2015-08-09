package gs.kar.justeatrecruitmenttest.android.di;

import android.content.Context;

import java.util.List;

import gs.kar.justeatrecruitmenttest.action.DisplayErrorAction;
import gs.kar.justeatrecruitmenttest.action.DisplayRestaurantsAction;
import gs.kar.justeatrecruitmenttest.action.FetchLocationAction;
import gs.kar.justeatrecruitmenttest.action.FetchRestaurantsAction;
import gs.kar.justeatrecruitmenttest.android.action.DialogFetchLocationAction;
import gs.kar.justeatrecruitmenttest.android.action.ToastDisplayErrorAction;
import gs.kar.justeatrecruitmenttest.android.action.justeat.JustEatApiFetchRestaurantsAction;
import gs.kar.justeatrecruitmenttest.model.Location;
import gs.kar.justeatrecruitmenttest.model.Restaurant;
import gs.kar.justeatrecruitmenttest.userstory.ViewListOfRestaurants;

/**
 * SimpleModule is a DI module which assembles user story together with the simplest implementation of it.
 *
 * This is used for step-by-step implementation of mocked parts (iterative approach).
 */
public class SimpleModule implements Module {
	@Override public ViewListOfRestaurants inject(Context context) {
		// return fetchLocationWithDialog(context);
		return fetchFromApi(context);
	}

	/**
	 * fetchLocationWithDialog will fetch location by asking user for the postcode using AlertDialog, and then error out :).
	 */
	private ViewListOfRestaurants fetchLocationWithDialog(Context context) {
		FetchLocationAction fLocA = new DialogFetchLocationAction(context);

		FetchRestaurantsAction fResA = new FetchRestaurantsAction() {
			@Override public void perform(Location input, Result<List<Restaurant>> result) {
				result.fail(String.format("Got location: %s. Fetching restaurants not implemented.", input.getPostCode()));
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
	 * fetchFromAPi will fetch location by asking user for the postcode using AlertDialog, and then use the postcode to
	 * call Just Eat API.
	 */
	private ViewListOfRestaurants fetchFromApi(Context context) {
		FetchLocationAction fLocA = new DialogFetchLocationAction(context);

		FetchRestaurantsAction fResA = new JustEatApiFetchRestaurantsAction();

		DisplayRestaurantsAction dResA = new DisplayRestaurantsAction() {
			@Override public void perform(List<Restaurant> input, Result<Void> result) {
				result.fail("Got restaurants. Displaying not implemented.");
			}
		};

		DisplayErrorAction dErrA = new ToastDisplayErrorAction(context);

		return new ViewListOfRestaurants(fLocA, fResA, dResA, dErrA);
	}

}
