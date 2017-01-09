package gs.kar.justeatrecruitmenttest.android.action.justeat;

import android.support.annotation.Nullable;

/**
 * JsonRestaurant represents restaurant json result from Just Eat Api.
 *
 * Some fields are assumed optional, however I haven't seen the Api docs so it might be wrong.
 */
public class JsonRestaurant {
	String Id;
	String Name;
	@Nullable String Address;
	String Postcode;
	String City;
	JsonCuisine[] CuisineTypes;
	@Nullable Float RatingStars;
	@Nullable Integer NumberOfRatings;
	@Nullable JsonLogo[] Logo;
}
