package gs.kar.justeatrecruitmenttest.android.action.justeat;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * JustEatService describes Just Eat API for Retrofit.
 */
public interface JustEatService {
	@Headers({
			"Accept-Tenant: uk",
			"Accept-Language: en-GB",
			"Authorization: Basic VGVjaFRlc3RBUEk6dXNlcjI=",
			"Host: api-interview.just-eat.com"
	})
	@GET("/restaurants")
	void getRestaurants(@Query("q") String postcode, Callback<JsonGetRestaurants> callback);
}
