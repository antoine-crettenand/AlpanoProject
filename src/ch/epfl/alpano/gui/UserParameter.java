package ch.epfl.alpano.gui;

/**
 *	Enumeration of all user parameters
 *	These are the parameters, the user will have to define in order to generate a {@code Panorama}
 *
 * @author Antoine Crettenand (SCIPER 261768),
 * @author Julien Sahli (SCIPER 272452)
 */
public enum UserParameter {

	/**
	 * Each Parameter is bounded, especially Longitude, Latitude, Observer_Elevation and Max_Distance since we can
	 * only cover a fraction of swiss/french mountain Panoramas
	 */
	OBSERVER_LONGITUDE (60_000, 120_000), // between 6 and 12 degrees of arc
	OBSERVER_LATITUDE (450_000, 480_000), // between 45 and 48 degrees of arc
	OBSERVER_ELEVATION (300, 10_000), // in meters
	CENTER_AZIMUTH (0, 360), // in degrees
	HORIZONTAL_FIELD_OF_VIEW (1, 360), // in degrees
	MAX_DISTANCE (10, 600), // in kilometers
	WIDTH (30, 16_000),
	HEIGHT (10, 4_000),
	SUPER_SAMPLING_EXPONENT (0, 2);

	private int min, max;

	UserParameter(int min, int max){
		this.min = min;
		this.max = max;
	}

	public int min() {
		return min;
	}
	public float max() {
		return max;
	}

	/**
	 * Clamp the value of the enumeration
	 *
	 * @param value to clamp
	 * @return {@param value},
	 * 	if value exceeds max, return max
	 * 	if value is below min, return min
	 */
	public int sanitize(int value){
		if (value > max) return max;
		if (value < min) return min;
		else return value;
	}
}
