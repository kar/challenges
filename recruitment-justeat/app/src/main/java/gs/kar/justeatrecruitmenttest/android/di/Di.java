package gs.kar.justeatrecruitmenttest.android.di;

/**
 * Di is a fictional dependency injection framework.
 */
public class Di {
	public static Module getDefault() {
		return new HardcodedLocationModule();
	}

	public static Module getForDialogLocation() {
		return new DialogLocationModule();
	}

	public static Module getForMapLocation() {
		return new MapLocationModule();
	}
}
