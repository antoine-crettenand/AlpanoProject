package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

import java.util.EnumMap;
import java.util.Objects;

import static ch.epfl.alpano.gui.UserParameter.*;

/**
 * Parameters changed by the users within the GUI
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public final class PanoramaUserParameters {
	private final EnumMap<UserParameter, Integer> map;
	private final static float PRECISION = 10_000f;
	private final static int MAX_HORIZONTAL_VIEW = 170;
	private final static int KILOMETERS = 1000;

	/**
	 * Create an instance of the class with {@code EnumMap}
	 *
	 * @param map EnumMap
	 * @see EnumMap
	 */
	public PanoramaUserParameters(EnumMap<UserParameter, Integer> map) {
		map.replaceAll(UserParameter::sanitize); // Make sure every value are in bounds
		map.replace(HEIGHT, sanitizeHeight(map)); // Make sure height is within vertical field of view
		this.map = map;
	}

	/**
	 * Create an instance of the class with {@param parameter} foreach {@code UserParameter} explicitely
	 */
	public PanoramaUserParameters(int longitude, int latitude, int elevation, int centerAzimuth,
			int horizontalFieldOfView, int maxDistance, int width, int height, int superSamplingExponent) {
		this(generateEnumMap(longitude, latitude, elevation, centerAzimuth, horizontalFieldOfView, maxDistance, width,
				height, superSamplingExponent));
	}

	private static EnumMap<UserParameter, Integer> generateEnumMap(int longitude, int latitude, int elevation,
			int centerAzimuth, int horizontalFieldOfView, int maxDistance, int width, int height,
			int superSamplingExponent) {

		EnumMap<UserParameter, Integer> m = new EnumMap<>(UserParameter.class);

		m.put(OBSERVER_LONGITUDE, longitude);
		m.put(OBSERVER_LATITUDE, latitude);
		m.put(OBSERVER_ELEVATION, elevation);
		m.put(CENTER_AZIMUTH, centerAzimuth);
		m.put(HORIZONTAL_FIELD_OF_VIEW, horizontalFieldOfView);
		m.put(MAX_DISTANCE, maxDistance);
		m.put(WIDTH, width);
		m.put(HEIGHT, height);
		m.put(SUPER_SAMPLING_EXPONENT, superSamplingExponent);

		return m;
	}

	private Integer sanitizeHeight(EnumMap<UserParameter, Integer> map) {
		return Math.min(map.get(HEIGHT),
				MAX_HORIZONTAL_VIEW * (int) ((float) (map.get(WIDTH) - 1) / map.get(HORIZONTAL_FIELD_OF_VIEW)) + 1);
	}

	/*
	 * List of getters
	 */

	public int get(UserParameter parameter) {
		return map.get(parameter);
	}

	public int observerLongitude() {
		return map.get(OBSERVER_LONGITUDE);
	}

	public int observerLatitude() {
		return map.get(OBSERVER_LATITUDE);
	}

	public int observerElevation() {
		return map.get(OBSERVER_ELEVATION);
	}

	public int centerAzimuth() {
		return map.get(CENTER_AZIMUTH);
	}

	public int horizontalFieldOfView() {
		return map.get(HORIZONTAL_FIELD_OF_VIEW);
	}

	public int maxDistance() {
		return map.get(MAX_DISTANCE);
	}

	public int width() {
		return map.get(WIDTH);
	}

	public int height() {
		return map.get(HEIGHT);
	}

	public int superSamplingExponent() {
		return map.get(SUPER_SAMPLING_EXPONENT);
	}

	/**
	 * @return PanoramaParameters such as they are computed
	 * @see PanoramaParameters
	 */
	public PanoramaParameters panoramaParameters() {
		return new PanoramaParameters(new GeoPoint(Math.toRadians(get(OBSERVER_LONGITUDE) / PRECISION),
				Math.toRadians(get(OBSERVER_LATITUDE) / PRECISION)), get(OBSERVER_ELEVATION),
				Math.toRadians(get(CENTER_AZIMUTH)), Math.toRadians(get(HORIZONTAL_FIELD_OF_VIEW)), get(MAX_DISTANCE)
				* KILOMETERS,
				(int) Math.scalb(get(WIDTH), get(SUPER_SAMPLING_EXPONENT)),
				(int) Math.scalb(get(HEIGHT), get(SUPER_SAMPLING_EXPONENT)));
	}

	/**
	 * @return PanoramaParameters such as they are displayed
	 * @see PanoramaParameters
	 */
	public PanoramaParameters panoramaDisplayParameters() {
		return new PanoramaParameters(new GeoPoint(Math.toRadians(get(OBSERVER_LONGITUDE) / PRECISION),
				Math.toRadians(get(OBSERVER_LATITUDE) / PRECISION)), get(OBSERVER_ELEVATION),
				Math.toRadians(get(CENTER_AZIMUTH)), Math.toRadians(get(HORIZONTAL_FIELD_OF_VIEW)), get(MAX_DISTANCE)
				* KILOMETERS,
				get(WIDTH), get(HEIGHT));
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		PanoramaUserParameters that = (PanoramaUserParameters) o;

		return map.equals(that.map);
	}

	@Override public int hashCode() {
		return Objects.hash(map);
	}
}
