package gs.kar.justeatrecruitmenttest.android.di;

import android.app.Activity;
import android.widget.ListView;

import gs.kar.justeatrecruitmenttest.R;
import gs.kar.justeatrecruitmenttest.action.DisplayErrorAction;
import gs.kar.justeatrecruitmenttest.action.DisplayRestaurantsAction;
import gs.kar.justeatrecruitmenttest.action.FetchLocationAction;
import gs.kar.justeatrecruitmenttest.action.FetchRestaurantsAction;
import gs.kar.justeatrecruitmenttest.android.action.PrettyDisplayRestaurantsAction;
import gs.kar.justeatrecruitmenttest.android.action.ToastDisplayErrorAction;
import gs.kar.justeatrecruitmenttest.android.action.justeat.JustEatApiFetchRestaurantsAction;
import gs.kar.justeatrecruitmenttest.android.action.mapfetchlocation.MapFetchLocationAction;
import gs.kar.justeatrecruitmenttest.userstory.ViewListOfRestaurants;

/**
 * MapLocationModule is a DI module which assembles user story together with location being fetched using map.
 */
public class MapLocationModule implements Module {

	/**
	 * inject will fetch location from map, call Just Eat API and display the results with pretty UI.
	 */
	@Override public ViewListOfRestaurants inject(Activity context) {
		FetchLocationAction fLocA = new MapFetchLocationAction(context);

		FetchRestaurantsAction fResA = new JustEatApiFetchRestaurantsAction();

		ListView listView = (ListView) context.findViewById(R.id.main_list);
		DisplayRestaurantsAction dResA = new PrettyDisplayRestaurantsAction(listView);

		DisplayErrorAction dErrA = new ToastDisplayErrorAction(context);

		return new ViewListOfRestaurants(fLocA, fResA, dResA, dErrA);
	}
}
