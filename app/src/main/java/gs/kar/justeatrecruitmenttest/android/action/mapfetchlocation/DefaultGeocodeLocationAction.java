package gs.kar.justeatrecruitmenttest.android.action.mapfetchlocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import gs.kar.justeatrecruitmenttest.model.Location;

/**
 * DefaultGeocodeLocationAction geocodes coordinates to postcode using Geocoder.
 */
public class DefaultGeocodeLocationAction implements MapFetchLocationAction.GeocodeAction {

	private final Geocoder geocoder;

	public DefaultGeocodeLocationAction(Context context) {
		this.geocoder = new Geocoder(context);
	}

	@Override public void perform(@Nullable Location location, @Nullable Result<Location> result) {
		if (location.hasPostcode()) {
			// Nothing to do.
			result.on(location);
			return;
		} else if (!location.hasCoordinates()) {
			result.fail("No coordinates provided");
			return;
		}

		try {
			List<Address> matches = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (matches.isEmpty()) {
				result.fail("No match");
			} else {
				result.on(new Location(matches.get(0).getPostalCode()));
			}
		} catch (IOException e) {
			result.fail(e.getMessage());
		}
	}
}
