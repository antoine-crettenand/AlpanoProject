package ch.epfl.alpano;

/**
 * Two methods used to convert distances between two points on the surface
 * of the Earth from meters to radians and conversely
 */

public interface Distance {
	double EARTH_RADIUS=6371000;

	/**
	 * Convert a distance in meters into a distance in radian
	 * @param distanceInMeters
	 * @return a distance in radian
	 */
	static double toRadians (double distanceInMeters){
		return distanceInMeters/EARTH_RADIUS;
	}

	/**
	 * Convert a distance in radian into a distance in meters
	 * @param distanceInRadians
	 * @return a distance in meters
	 */
	static double toMeters (double distanceInRadians){
		return distanceInRadians*EARTH_RADIUS;
	}
}
