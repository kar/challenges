package gs.kar.justeatrecruitmenttest.model;

import android.support.annotation.Nullable;

/**
 * Location model represents physical location of user.
 *
 * Location can be created from current position, or directly from postcode. Either way, postcode is the only thing that
 * matters in our example, as we need it for API requests. Latitude & longitude is set if we got user's coordinates and
 * have to get the postcode.
 */
public class Location {

	@Nullable private final String postcode;
	@Nullable private final Double latitude;
	@Nullable private final Double longitude;

	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.postcode = null;
	}

	public Location(String postcode) {
		this.postcode = postcode;
		this.latitude = null;
		this.longitude = null;
	}

	public boolean hasPostcode() {
		return postcode != null;
	}

	@Nullable public String getPostcode() {
		return postcode;
	}

	public boolean hasCoordinates() {
		return latitude != null && longitude != null;
	}

	@Nullable public Double getLatitude() {
		return latitude;
	}

	@Nullable public Double getLongitude() {
		return longitude;
	}
}
