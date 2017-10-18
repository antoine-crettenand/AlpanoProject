package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

/**
 * This Class compute the values of distance, GeoPoint, altitude and slope of a Panorama in order for the Builder to
 * build it. The values are computed from a ContinuousElevationModel
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public final class PanoramaComputer {
	private ContinuousElevationModel dem;

	private final static double coeff = ((1 - 0.13d) / (2 * Distance.EARTH_RADIUS));
	private final static double epsilon = 4;
	private final static double dx = 64;

	/**
	 * Creates a new instance of the class
	 *
	 * @param dem the digital elevation model to calculate
	 * @throws NullPointerException if {@param dem} is null
	 */
	public PanoramaComputer(ContinuousElevationModel dem) {
		Objects.requireNonNull(dem);
		this.dem = dem;
	}

	/**
	 * @param profile  ElevationProfile
	 * @param ray0     the starting altitude of the rayon
	 * @param raySlope the slope of the ray
	 * @return the function that returns the distance between the ray and the profile
	 */
	public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope) {
		return (double x) -> ray0 + x * raySlope - profile.elevationAt(x) + coeff * Math2.sq(x);
	}

	/**
	 * Compute the Panorama
	 *
	 * @param params the parameters
	 * @return the Panorama specified with the parameters and the ContinuousElevationModel
	 */
	public Panorama computePanorama(PanoramaParameters params) {

		Panorama.Builder builder = new Panorama.Builder(params);

		//We declare variable before the double for loop to
		ElevationProfile profile;
		double ray0;
		double raySlope;

		double interval;
		GeoPoint position;

		for (int i = 0; i < params.width(); i++) {

			profile = new ElevationProfile(dem, params.observerPosition(), params.azimuthForX(i), params.maxDistance());
			double x = 0;

			for (int j = params.height() - 1; j >= 0; j--) {

				ray0 = params.observerElevation();
				raySlope = params.altitudeForY(j); //altitudeForY()

				//Define Function
				DoubleUnaryOperator f = rayToGroundDistance(profile, ray0, Math.tan(raySlope));

				//Find Interval
				interval = Math2.firstIntervalContainingRoot(f, x, params.maxDistance(), dx);

				if (interval != Double.POSITIVE_INFINITY) {

					//Find x
					x = Math2.improveRoot(f, interval, interval + dx, epsilon);

					//Give Builder, values
					position = profile.positionAt(x);
					builder.setDistanceAt(i, j, (float) (x / Math.cos(params.altitudeForY(j))))
							.setLongitudeAt(i, j, (float) position.longitude())
							.setLatitudeAt(i, j, (float) position.latitude())
							.setElevationAt(i, j, (float) profile.elevationAt(x))
							.setSlopeAt(i, j, (float) profile.slopeAt(x));
				} else
					break;
			}
		}
		return builder.build();
	}
}



