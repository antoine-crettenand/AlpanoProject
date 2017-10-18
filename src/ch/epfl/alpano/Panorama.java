package ch.epfl.alpano;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents the generated panorama
 *
 * Generating a Panorama is done in two steps :
 * - class PanoramaComputer computes the Panorama with a ContinuousElevationModel and PanoramParameters
 * - And Panorama.Builder builds it
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 * @date 20.03.2017
 */
public final class Panorama {

	private final PanoramaParameters params;
	private final float[] distance, longitude, latitude, altitude, slope;

	/**
	 * Constructor is private since the only way to create an instance is to build it with Panorama.Builder
	 *
	 * @param params    parameters of the generated Panorama
	 * @param distance  values of distance at each point of the panorama
	 * @param longitude values of longitude at each point of the panorama
	 * @param latitude  values of latitude at each point of the panorama
	 * @param altitude  values of altitude at each point of the panorama
	 * @param slope     values of slope at each point of the panorama
	 */
	private Panorama(PanoramaParameters params, float[] distance, float[] longitude, float[] latitude, float[] altitude,
			float[] slope) {
		this.params = params;
		this.distance = distance;
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.slope = slope;

	}

    /**
     * Simple Getter
     * @return the parameters
     */
	public PanoramaParameters parameters() {
		return params;
	}

	/**
	 *
	 * @param x -axis index
	 * @param y -axis index
	 * @return distance value at given index in distance table
	 * @see #distance
	 * @throws IndexOutOfBoundsException if ({@param x}, {@param y}) is not valid index
	 */
	public float distanceAt(int x, int y) {
		isValidSample(x, y);
		return distance[params.linearSampleIndex(x, y)];
	}

	/**
	 * @param x -axis index
	 * @param y -axis index
	 * @param d default value to return
	 * @return distanceAt(x,y) if it is valid or (d) otherwise
     * @see Panorama#distanceAt(int, int)
     */
	public float distanceAt(int x, int y, float d) {
		return (params.isValidSampleIndex(x, y) ? distanceAt(x, y) : d);
	}

	public float longitudeAt(int x, int y) {
		isValidSample(x, y);
		return longitude[params.linearSampleIndex(x, y)];
	}

	public float latitudeAt(int x, int y) {
		isValidSample(x, y);
		return latitude[params.linearSampleIndex(x, y)];
	}

	public float elevationAt(int x, int y) {
		isValidSample(x, y);
		return altitude[params.linearSampleIndex(x, y)];
	}

	public float slopeAt(int x, int y) {
		isValidSample(x, y);
		return slope[params.linearSampleIndex(x, y)];
	}

	private void isValidSample(int x, int y){
		if (!params.isValidSampleIndex(x, y))
			throw new IndexOutOfBoundsException();
	}

	/**
	 * Class building the Panorama with PanoramaParameters and distance, GeoPoint, altitude, slope
	 * values (computed by PanoramaComputer)
	 */
	public static final class Builder {
		private final PanoramaParameters param;
		private boolean built = false;
		private float[] distance, longitude, latitude, altitude, slope;

		/**
		 * Creates a new instance of the class
		 *
		 * @param param parameters of the panorama
		 * @throws NullPointerException if {@param param} is null
		 */
		public Builder(PanoramaParameters param) {
			Objects.requireNonNull(param, "Parameters are null");

			this.param = param;

			distance = new float[param.width() * param.height()];
			longitude = new float[param.width() * param.height()];
			latitude = new float[param.width() * param.height()];
			altitude = new float[param.width() * param.height()];
			slope = new float[param.width() * param.height()];

			Arrays.fill(distance, Float.POSITIVE_INFINITY);
		}

		/**
		 * @param x -axis index
		 * @param y    -axis index
		 * @param distance in meters to set at given index in table
		 * @return this, to chain methods
		 * @see #distance
		 * @throws IllegalStateException if panorama already built
		 * @see #built
		 * @throws IndexOutOfBoundsException if ({@param x}, {@param y}) is not valid index
		 */
		public Builder setDistanceAt(int x, int y, float distance) {
			isBuiltAndSampleValid(x, y);
			this.distance[param.linearSampleIndex(x, y)] = distance;
			return this;
		}

		/**
		 * @param x -axis index
		 * @param y -axis index
		 * @param longitude in rad, to set at given index in table
		 * @return this, to chain methods
		 * @see #longitude
		 * @throws IllegalStateException if panorama already built
		 * @see #built
		 * @throws IndexOutOfBoundsException if ({@param x}, {@param y}) is not valid index
		 */
		public Builder setLongitudeAt(int x, int y, float longitude) {
			isBuiltAndSampleValid(x, y);
			this.longitude[param.linearSampleIndex(x, y)] = longitude;
			return this;
		}

		/**
		 * @param x -axis index
		 * @param y -axis index
		 * @param latitude in rad, to set at given index in table
		 * @return this, to chain methods
		 * @see #latitude
		 * @throws IllegalStateException if panorama already built
		 * @see #built
		 * @throws IndexOutOfBoundsException if ({@param x}, {@param y}) is not valid index
		 */
		public Builder setLatitudeAt(int x, int y, float latitude) {
			isBuiltAndSampleValid(x, y);
			this.latitude[param.linearSampleIndex(x, y)] = latitude;
			return this;
		}

		/**
		 * @param x -axis index
		 * @param y -axis index
		 * @param altitude in meters, to set at given index in table
		 * @return this to chain methods
		 * @see #altitude
		 * @throws IllegalStateException if panorama already built
		 * @see #built
		 * @throws IndexOutOfBoundsException if ({@param x}, {@param y}) is not valid index
		 */
		public Builder setElevationAt(int x, int y, float altitude) {
			isBuiltAndSampleValid(x, y);
			this.altitude[param.linearSampleIndex(x, y)] = altitude;
			return this;
		}

		/**
		 * @param x -axis index
		 * @param y -axis index
		 * @param slope in rad, to set at given index in table
		 * @return this to chain methods
		 * @see #slope
		 * @throws IllegalStateException if panorama already built
		 * @see #built
		 * @throws IndexOutOfBoundsException if ({@param x}, {@param y}) is not valid index
		 */
		public Builder setSlopeAt(int x, int y, float slope) {
			isBuiltAndSampleValid(x, y);
			this.slope[param.linearSampleIndex(x, y)] = slope;
			return this;
		}


		private void isBuiltAndSampleValid(int x, int y){
			if (built) {
				throw new IllegalStateException("panorama already built");
			}
			if (!param.isValidSampleIndex(x, y)) {
				throw new IndexOutOfBoundsException();
			}
		}

		/**
		 * Build the panorama
		 *
		 * @return built Panorama
		 * @throws IllegalStateException if built is true
		 * @see #built
		 */
		public Panorama build() {
			if (built) {
				throw new IllegalStateException("panorama already built");
			}
			built = true;

			Panorama panorama = new Panorama(param, distance, longitude, latitude, altitude, slope);

			distance = null;
			longitude = null;
			latitude = null;
			altitude = null;
			slope = null;

			return panorama;
		}
	}

}
