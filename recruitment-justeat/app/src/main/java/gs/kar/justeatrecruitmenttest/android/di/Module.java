package gs.kar.justeatrecruitmenttest.android.di;

import android.app.Activity;

import gs.kar.justeatrecruitmenttest.userstory.ViewListOfRestaurants;

/**
 * Module is an interface for replaceable module configuring user stories.
 *
 * The possibility of replacing configuration of various components comes in handy for both development and
 * automated testing.
 */
public interface Module {
	ViewListOfRestaurants inject(Activity context);
}
