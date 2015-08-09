package gs.kar.justeatrecruitmenttest.model;

import android.support.annotation.Nullable;

/**
 * Location model represents physical location of user.
 *
 * Location can be created from current position, or directly from postcode.
 * Either way, postcode is the only thing that matters in our example, as we
 * need it for API requests. Latitude & longitude can be not set. In actual
 * solution, they would be useful.
 */
public class Location {

	private final String postCode;
	@Nullable private final Long latitude;
	@Nullable private final Long longitude;

	public Location(long latitude, long longitude, String postCode) {
		this.postCode = postCode;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Location(String postCode) {
		this.postCode = postCode;
		this.latitude = null;
		this.longitude = null;
	}

	public String getPostCode() {
		return postCode;
	}
}
