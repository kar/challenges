package gs.kar.justeatrecruitmenttest.android.action.mapfetchlocation;

import android.content.Context;
import android.support.annotation.Nullable;

import gs.kar.justeatrecruitmenttest.action.AsyncAction;
import gs.kar.justeatrecruitmenttest.action.FetchLocationAction;
import gs.kar.justeatrecruitmenttest.model.Location;

/**
 * MapFetchLocationAction fetches postcode for location pointed on map by user, and defaults to current location.
 *
 * This class is example of action which is utilizing AsyncAction interface itself to abstract it's own details and
 * perform asynchronous actions requiring user input.
 */
public class MapFetchLocationAction implements FetchLocationAction {

	interface AskAction extends AsyncAction<Void, Location> {}
	interface GeocodeAction extends AsyncAction<Location, Location> {}

	private final AskAction askAction;
	private final GeocodeAction geocodeAction;

	private Result<Location> finalResult;

	public MapFetchLocationAction(Context context) {
		this(new ActivityAskForLocationAction(context), new DefaultGeocodeLocationAction(context));
	}

	public MapFetchLocationAction(AskAction askAction, GeocodeAction geocodeAction) {
		this.askAction = askAction;
		this.geocodeAction = geocodeAction;
	}

	@Override public void perform(@Nullable Void input, @Nullable Result<Location> result) {
		this.finalResult = result;
		askAction.perform(null, actOnAskForLocation);
	}

	private Result<Location> actOnAskForLocation = new Result<Location>() {
		@Override public void on(Location result) {
			if (result.hasPostcode()) {
				finalResult.on(result);
			} else {
				geocodeAction.perform(result, finalResult);
			}
		}

		@Override public void fail(String reason) {
			finalResult.fail(reason);
		}
	};
}
