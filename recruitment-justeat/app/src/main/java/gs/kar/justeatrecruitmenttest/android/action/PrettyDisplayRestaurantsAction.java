package gs.kar.justeatrecruitmenttest.android.action;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.LinkedList;
import java.util.List;

import gs.kar.justeatrecruitmenttest.R;
import gs.kar.justeatrecruitmenttest.action.DisplayRestaurantsAction;
import gs.kar.justeatrecruitmenttest.model.Restaurant;

/**
 * PrettyDisplayRestaurantsAction displays restaurants using a bit Polished ;) UI.
 */
public class PrettyDisplayRestaurantsAction implements DisplayRestaurantsAction {
	private final ListView listView;

	public PrettyDisplayRestaurantsAction(ListView listView) {
		this.listView = listView;
	}

	@Override public void perform(@Nullable List<Restaurant> restaurants, @Nullable Result<Void> result) {
		if (restaurants.isEmpty()) {
			populateEmptyView(listView);
		} else {
			populateListView(listView, restaurants);
		}
	}

	private void populateListView(ListView listView, List<Restaurant> restaurants) {
		ArrayAdapter<Restaurant> adapter = new PrettyDisplayAdapter(listView.getContext(), restaurants);
		listView.setAdapter(adapter);
	}

	private void populateEmptyView(ListView listView) {
		ArrayAdapter<Restaurant> adapter = new EmptyAdapter(listView.getContext());
		listView.setAdapter(adapter);
	}

	private static class PrettyDisplayAdapter extends ArrayAdapter<Restaurant> {

		private static final int itemLayout = R.layout.item_restaurant;

		public PrettyDisplayAdapter(Context context, List<Restaurant> restaurants) {
			super(context, itemLayout, restaurants);
		}

		@Override public View getView(int position, @Nullable View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(itemLayout, null);
			}

			Restaurant r = getItem(position);
			getItem(convertView, R.id.item_name).setText(r.getName());
			getItem(convertView, R.id.item_rating).setText(String.format("%.2f", r.getRating()));
			getItem(convertView, R.id.item_cuisines).setText(r.makeCuisinesString());

			// Fresco does the heavy lifting around memory management for images.
			((SimpleDraweeView) convertView.findViewById(R.id.item_logo)).setImageURI(Uri.parse(r.getLogo().getUrlPath()));

			return convertView;
		}

		private TextView getItem(View view, int id) {
			return (TextView) view.findViewById(id);
		}
	}

	/**
	 * EmptyAdapter displays the "no results" view. It's a bit of a hack.
	 */
	private static class EmptyAdapter extends ArrayAdapter<Restaurant> {

		private static final int itemLayout = R.layout.item_empty;

		public EmptyAdapter(Context context) {
			super(context, itemLayout, new Restaurant[]{null});
		}

		@Override public View getView(int position, @Nullable View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(itemLayout, null);
			}
			return convertView;
		}

	}
}
