package gs.kar.justeatrecruitmenttest.userstory;

import java.util.List;

import gs.kar.justeatrecruitmenttest.action.AsyncAction;
import gs.kar.justeatrecruitmenttest.action.DisplayErrorAction;
import gs.kar.justeatrecruitmenttest.action.DisplayRestaurantsAction;
import gs.kar.justeatrecruitmenttest.action.FetchLocationAction;
import gs.kar.justeatrecruitmenttest.action.FetchRestaurantsAction;
import gs.kar.justeatrecruitmenttest.model.Location;
import gs.kar.justeatrecruitmenttest.model.Restaurant;

/**
 * ViewListOfRestaurants describes the user story of viewing a list of restaurants for given location.
 * <p/>
 * - Location (postcode) can be manually inputted or fetched from current location (depends on FetchLocationAction).
 * - List of restaurants have basic information about them and logo (in Restaurant model).
 * - Handle error cases.
 * <p/>
 * Note that this class is fully independent of implementation of each steps and has no Android dependencies, which makes
 * it easily configurable, testable and mockable.
 */
public class ViewListOfRestaurants implements UserStory {

	private final FetchLocationAction fetchLocationAction;
	private final FetchRestaurantsAction fetchRestaurantsAction;
	private final DisplayRestaurantsAction displayRestaurantsAction;
	private final DisplayErrorAction displayErrorAction;

	public ViewListOfRestaurants(FetchLocationAction fetchLocationAction, FetchRestaurantsAction fetchRestaurantsAction,
	                             DisplayRestaurantsAction displayRestaurantsAction, DisplayErrorAction displayErrorAction) {
		this.fetchLocationAction = fetchLocationAction;
		this.fetchRestaurantsAction = fetchRestaurantsAction;
		this.displayRestaurantsAction = displayRestaurantsAction;
		this.displayErrorAction = displayErrorAction;
	}

	@Override public void perform() {
		fetchLocationAction.perform(null, actOnFetchLocation);
	}

	private final AsyncAction.Result<Location> actOnFetchLocation = new AsyncAction.Result<Location>() {
		@Override public void on(Location location) {
			fetchRestaurantsAction.perform(location, actOnFetchRestaurants);
		}

		@Override public void fail(String reason) {
			displayErrorAction.perform(reason, null); // This action never fails.
		}

	};

	private final AsyncAction.Result<List<Restaurant>> actOnFetchRestaurants = new AsyncAction.Result<List<Restaurant>>() {
		@Override public void on(List<Restaurant> restaurants) {
			displayRestaurantsAction.perform(restaurants, actOnDisplayRestaurants);
		}

		@Override public void fail(String reason) {
			displayErrorAction.perform(reason, null); // This action never fails.
		}

	};

	private final AsyncAction.Result<Void> actOnDisplayRestaurants = new AsyncAction.Result<Void>() {
		@Override public void on(Void result) {
			// Everything went fine, no need to do anything.
		}

		@Override public void fail(String reason) {
			displayErrorAction.perform(reason, null); // This action never fails.
		}

	};
}
