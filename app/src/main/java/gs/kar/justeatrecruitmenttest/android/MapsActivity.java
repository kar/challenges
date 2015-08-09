package gs.kar.justeatrecruitmenttest.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import gs.kar.justeatrecruitmenttest.R;
import gs.kar.justeatrecruitmenttest.action.AsyncAction;
import gs.kar.justeatrecruitmenttest.android.action.mapfetchlocation.ActivityAskForLocationAction;
import gs.kar.justeatrecruitmenttest.android.action.mapfetchlocation.DefaultGeocodeLocationAction;

/**
 * MapActivity is Android Studio generated activity to display a map and let user choose location.
 *
 * By default, user's current location is displayed.
 */
public class MapsActivity extends FragmentActivity {

	private GoogleMap mMap; // Might be null if Google Play services APK is not available.
	private LatLng currentLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		setUpMapIfNeeded();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		if (mMap == null) {
			fail("Map initialization failure");
			return;
		}
		displayCurrentLocation();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		}
	}

	/**
	 * Displays user's current location.
	 */
	private void displayCurrentLocation() {
		final Location location = getLocation();
		if (location == null) {
			Toast.makeText(this, "No current location available.", Toast.LENGTH_LONG).show();
			return;
		}

		currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
		mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your position"));
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15), new GoogleMap.CancelableCallback() {
			@Override public void onFinish() {
				displayGeocodedLocation();
			}

			@Override public void onCancel() {
				displayGeocodedLocation();
			}
		});
	}

	private void displayGeocodedLocation() {
		if (currentLocation == null) {
			updateMapsTextView("Unknown location");
		} else {
			// Sample use of another instance of Action. TODO: remove unnecessary type wrapping.
			DefaultGeocodeLocationAction geocode = new DefaultGeocodeLocationAction(this);
			geocode.perform(new gs.kar.justeatrecruitmenttest.model.Location(currentLocation.latitude,
					currentLocation.longitude), new AsyncAction.Result<gs.kar.justeatrecruitmenttest.model.Location>() {
				@Override public void on(gs.kar.justeatrecruitmenttest.model.Location result) {
					updateMapsTextView(String.format("Postcode: %s", result.getPostcode()));
				}

				@Override public void fail(String reason) {
					updateMapsTextView("Unknown postcode");
				}
			});
		}
	}
	private void updateMapsTextView(String message) {
		((TextView) findViewById(R.id.maps_location)).setText(message);
		findViewById(R.id.maps_select).setEnabled(true);
	}

	/**
	 * Fetches last known location from GPS. Can be null.
	 */
	public Location getLocation() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		Location location = null;
		for (int i = 0; i < providers.size(); i++) {
			location = lm.getLastKnownLocation(providers.get(i));
			if (location != null) {
				break;
			}
		}
		return location;
	}

	public void onSelectLocation(View v) {
		if (currentLocation != null) {
			ActivityAskForLocationAction.result.on(new gs.kar.justeatrecruitmenttest.model.Location(
					currentLocation.latitude, currentLocation.longitude));
			finish();
		} else {
			fail("Could not fetch location");
		}
	}

	public void onCancel(View v) {
		fail("Cancelled");
	}

	private void fail(String reason) {
		ActivityAskForLocationAction.result.fail(reason);
		finish();
	}
}
