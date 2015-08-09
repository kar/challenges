package gs.kar.justeatrecruitmenttest.android.action;

import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import gs.kar.justeatrecruitmenttest.action.DisplayRestaurantsAction;
import gs.kar.justeatrecruitmenttest.model.Restaurant;

/**
 * SimpleDisplayRestaurantsAction displays restaurants using simple list UI.
 */
public class SimpleDisplayRestaurantsAction implements DisplayRestaurantsAction {
	private final ListView listView;

	public SimpleDisplayRestaurantsAction(ListView listView) {
		this.listView = listView;
	}

	@Override public void perform(@Nullable List<Restaurant> restaurants, @Nullable Result<Void> result) {
		populateListView(listView, restaurants);
	}

	private void populateListView(ListView listView, List<Restaurant> restaurants) {
		ArrayAdapter<Restaurant> adapter = new ArrayAdapter<Restaurant>(listView.getContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1, restaurants);
		listView.setAdapter(adapter);
	}
}
