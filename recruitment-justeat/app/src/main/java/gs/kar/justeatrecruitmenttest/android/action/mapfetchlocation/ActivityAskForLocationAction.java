package gs.kar.justeatrecruitmenttest.android.action.mapfetchlocation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import gs.kar.justeatrecruitmenttest.android.MapsActivity;
import gs.kar.justeatrecruitmenttest.model.Location;

/**
 * ActivityAskForLocationAction asks user to pick location on map using MapActivity.
 *
 * For ease of implementation, we are using a static field based callback here, so only one instance can be
 * executed at a time.
 */
public class ActivityAskForLocationAction implements MapFetchLocationAction.AskAction {

	public static Result<Location> result;

	private final Context context;

	public ActivityAskForLocationAction(Context context) {
		this.context = context;
	}

	@Override public void perform(@Nullable Void input, @Nullable Result<Location> result) {
		this.result = result;
		context.startActivity(new Intent(context, MapsActivity.class));
	}

}
