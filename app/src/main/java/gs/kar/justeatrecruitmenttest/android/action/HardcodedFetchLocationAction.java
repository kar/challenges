package gs.kar.justeatrecruitmenttest.android.action;

import android.support.annotation.Nullable;

import gs.kar.justeatrecruitmenttest.action.FetchLocationAction;
import gs.kar.justeatrecruitmenttest.model.Location;

/**
 * HardcodedFetchLocationAction always returns hardcoded postcode.
 */
public class HardcodedFetchLocationAction implements FetchLocationAction {

	private final String hardcodedPostcode;

	public HardcodedFetchLocationAction(String hardcodedPostcode) {
		this.hardcodedPostcode = hardcodedPostcode;
	}

	@Override public void perform(@Nullable Void input, @Nullable Result<Location> result) {
		result.on(new Location(hardcodedPostcode));
	}
}
