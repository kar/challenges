package gpslocation.kar.gs.gpslocationapikey;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.TimeZoneApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Basic implementation of the following task:
 *
 * Develop an Android mobile application to show the user’s current location on a map based on the phones GPS.
 * Show the user in a ‘bubble’ the following attributes:
 * - Latitude / longitude
 * - Timezone based on location (e.g. Pacific/Auckland)
 * - Current UTC time (e.g. 10:00:00)
 * - Current local time (e.g. 20:00:00)
 */
public class MainActivity extends FragmentActivity {

	private GoogleMap mMap; // Might be null if Google Play services APK is not available.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		if (mMap == null) {
			Toast.makeText(this, "Initialization problem", Toast.LENGTH_LONG).show();
			return;
		}
		displayCurrentLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
	 * installed) and the map has not already been instantiated.. This will ensure that we only ever
	 * call {@link #setUpMap()} once when {@link #mMap} is not null.
	 * <p/>
	 * If it isn't installed {@link SupportMapFragment} (and
	 * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
	 * install/update the Google Play services APK on their device.
	 * <p/>
	 * A user can return to this FragmentActivity after following the prompt and correctly
	 * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
	 * have been completely destroyed during this process (it is likely that it would only be
	 * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
	 * method in {@link #onResume()} to guarantee that it will be called.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the camera. In this case, we
	 * just add a marker near Africa.
	 * <p/>
	 * This should only be called once and when we are sure that {@link #mMap} is not null.
	 */
	private void setUpMap() {
		mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
	}

	/**
	 * Displays current location with information about timezone etc.
	 */
	private void displayCurrentLocation() {
		final double[] loc = getLocation();
		if (loc == null) {
			Toast.makeText(this, "Could not fetch location", Toast.LENGTH_LONG).show();
			return;
		}

		final View infoWindow = LayoutInflater.from(this).inflate(R.layout.infowindow, null);

		mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
			@Override public View getInfoWindow(Marker marker) {
				return null;
			}

			@Override public View getInfoContents(Marker marker) {
				populateInfoWindow(loc, infoWindow);
				return infoWindow;
			}
		});

		Marker me = mMap.addMarker(new MarkerOptions()
				.position(new LatLng(loc[0], loc[1]))
				.title("Current Location")
				.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_on)));

		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc[0], loc[1]), 5));
		me.showInfoWindow();
	}

	/**
	 * Fetches last known location from GPS. Can be null.
	 */
	public double[] getLocation() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		Location l = null;
		for (int i = 0; i < providers.size(); i++) {
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}
		double[] gps = new double[2];

		if (l != null) {
			gps[0] = l.getLatitude();
			gps[1] = l.getLongitude();
		}
		return gps;
	}

	/**
	 * Displays time and location information in info window.
	 */
	private void populateInfoWindow(double[] location, View infoWindow) {
		TimeZone t = getTimezone(location);
		((TextView) infoWindow.findViewById(R.id.latlong)).setText(String.format("Lat / Lng: %f, %f", location[0], location[1]));
		if (t == null) {
			((TextView) infoWindow.findViewById(R.id.timezone)).setText("Unknown");
			return;
		}

		((TextView) infoWindow.findViewById(R.id.timezone)).setText("Timezone: " + t.getDisplayName());
		((TextView) infoWindow.findViewById(R.id.localtime)).setText("Local time: " + getTimeString(t));
		((TextView) infoWindow.findViewById(R.id.currenttime)).setText("UTC time: " + getTimeString(TimeZone.getTimeZone("UTC")));
	}

	/**
	 * Gets timezone for location using Google TimeZone API, or null on failure.
	 */
	private TimeZone getTimezone(double[] loc) {
		// XXX: Could use the key from resources
		GeoApiContext c = new GeoApiContext().setApiKey("AIzaSyBdW_5Eqzhd-rlACQod2F8DVmbPOvtK9bg");
		PendingResult<TimeZone> res = TimeZoneApi.getTimeZone(c, new com.google.maps.model.LatLng(loc[0], loc[1]));
		try {
			return res.await();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Gets current time for given timezone.
	 */
	private String getTimeString(TimeZone t) {
		sdf.setTimeZone(t);
		return sdf.format(new Date());
	}

	private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

}
