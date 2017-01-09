package gs.kar.justeatrecruitmenttest.android.action.justeat;

import android.support.annotation.Nullable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import gs.kar.justeatrecruitmenttest.action.FetchRestaurantsAction;
import gs.kar.justeatrecruitmenttest.model.Cuisine;
import gs.kar.justeatrecruitmenttest.model.Location;
import gs.kar.justeatrecruitmenttest.model.Logo;
import gs.kar.justeatrecruitmenttest.model.Restaurant;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * JustEatApiFetchRestaurantsAction uses Just Eat API to fetch restaurants for given location.
 *
 * It does error handling and input validation as well. In actual solution, it'd be better to split those responsibilities
 * to another component.
 */
public class JustEatApiFetchRestaurantsAction implements FetchRestaurantsAction {

	private JustEatService justEat;

	@Override public void perform(@Nullable Location location, @Nullable final Result<List<Restaurant>> result) {
		if (justEat == null) {
			justEat = prepareService();
		}

		try {
			justEat.getRestaurants(location.getPostcode(), new Callback<JsonGetRestaurants>() {
				@Override public void success(JsonGetRestaurants restaurants, Response response) {
					result.on(convert(restaurants));
				}

				@Override public void failure(RetrofitError error) {
					result.fail(String.format("API error: %s", error.toString()));
				}
			});
		} catch (Exception e) { // TODO: check the actual exception type
			result.fail(String.format("API error: %s", e.getMessage()));
		}
	}

	private JustEatService prepareService() {
		final RestAdapter adapter = new RestAdapter.Builder()
				.setEndpoint("http://api-interview.just-eat.com")
				.setLog(new RestAdapter.Log() {
					@Override public void log(String message) {
						Log.w("justeat", String.format("Retrofit: %s", message));
					}
				})
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.build();

		return adapter.create(JustEatService.class);
	}

	private List<Restaurant> convert(JsonGetRestaurants response) {
		List<Restaurant> restaurants = new ArrayList<>();
		if (response.Restaurants == null || response.Restaurants.length == 0) {
			return restaurants;
		}

		for (JsonRestaurant r : response.Restaurants) {
			Restaurant res = convertRestaurant(r);
			if (res != null) {
				restaurants.add(res);
			}
		}

		return restaurants;
	}

	private @Nullable Restaurant convertRestaurant(JsonRestaurant r) {
		Logo logo = convertLogo(r.Logo);
		float rating = convertRating(r.RatingStars, r.NumberOfRatings);
		Cuisine[] typesOfFood = convertTypesOfFood(r.CuisineTypes);

		if (logo == null || typesOfFood.length == 0) {
			return null;
		}

		return new Restaurant(r.Id, r.Name.trim(), logo, rating, typesOfFood);
	}

	private @Nullable Logo convertLogo(@Nullable JsonLogo[] l) {
		if (l == null || l.length == 0) {
			return null;
		}

		try {
			return new Logo(new URL(l[0].StandardResolutionURL));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private float convertRating(@Nullable Float rating, @Nullable Integer count) {
		// Sample input validation to avoid displaying embarrassing results. We assume max rating is 10.
		if (rating == null || rating < 0 || rating > 10 || count == null || count < 0) {
			return 0; // Could be a magic number for "unknown"
		}

		return rating;
	}

	private Cuisine[] convertTypesOfFood(@Nullable JsonCuisine[] cus) {
		List<Cuisine> cuisines = new LinkedList<>();
		if (cus == null || cus.length == 0) {
		}

		for (JsonCuisine c : cus) {
			Cuisine cuisine = convertCuisine(c);
			if (cuisine != null) {
				cuisines.add(cuisine);
			}
		}

		if (cuisines.isEmpty()) {
			// Sample fallback approach
			cuisines.add(new Cuisine("unknown", "Unknown"));
		}

		Cuisine[] result = new Cuisine[cuisines.size()];
		cuisines.toArray(result);
		return result;
	}

	private @Nullable Cuisine convertCuisine(JsonCuisine c) {
		return new Cuisine(c.Id, c.Name.trim());
	}
}
