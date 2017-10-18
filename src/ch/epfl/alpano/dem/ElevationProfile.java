package ch.epfl.alpano.dem;

import ch.epfl.alpano.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.util.Objects;

import static java.lang.Math.*;

/**
 * This class represents an altimetric profile following an arc of circle.
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public final class ElevationProfile {

	private final ContinuousElevationModel elevationModel;
	private final GeoPoint origin;
	private final double azimuth;
	private final GeoPoint[] geoPoints;
	private final int precision = 4096;
	private final double length;
	private final static int SCALE_FACTOR = 12;

	/**
	 * Creates a new instance of the class
	 *
	 * @param elevationModel the continuous elevation model to load
	 * @param origin         the origin of the profile
	 * @param azimuth        the azimuth of the profile
	 * @param length         the horizontal length of the profile
	 */
	public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length) {

		Preconditions.checkArgument(Azimuth.isCanonical(azimuth), "azimuth is not canonical");
		Preconditions.checkArgument(length > 0, "length is not positive");
		Objects.requireNonNull(elevationModel,"elevation model is null");
		Objects.requireNonNull(origin, "originis null");

		this.elevationModel = elevationModel;
		this.origin = origin;
		this.length = length;
		this.azimuth = azimuth;

		this.geoPoints = calculateValues();

	}

	private GeoPoint[] calculateValues() {

		int size = (int) Math.ceil(length / precision) + 1;

		GeoPoint[] geoPoints = new GeoPoint[size];

		double lambda0 = origin.longitude();
		double phi0 = origin.latitude();
		double alpha = Azimuth.toMath(azimuth);

		for (int i = 0; i < geoPoints.length; i++) {
			double x = Distance.toRadians(i * precision);

			double phi = asin(sin(phi0) * cos(x) + cos(phi0) * sin(x) * cos(alpha));
			double lambda = (lambda0 - asin((sin(alpha) * sin(x)) / cos(phi)) + PI) % (2 * PI) - PI;

			geoPoints[i] = new GeoPoint(lambda, phi);

		}
		return geoPoints;

	}

	/**
	 * @param x distance from {@param origin}
	 * @return the geopoint at the given position of the profile
	 * @throws IllegalArgumentException if {@param x} is not in profile
	 */
	public GeoPoint positionAt(double x) {
		Preconditions.checkArgument(x >= 0 && x <= this.length);

		double index = Math.scalb(x, -SCALE_FACTOR);

		int lowIndex = (int) Math.floor(index);
		int upIndex = (int) Math.ceil(index);
		double diff = index - lowIndex;

		double lambda = Math2.lerp(geoPoints[lowIndex].longitude(), geoPoints[upIndex].longitude(), diff);

		double phi = Math2.lerp(geoPoints[lowIndex].latitude(), geoPoints[upIndex].latitude(), diff);
		return new GeoPoint(lambda, phi);

	}

	/**
	 * Searches in the continuous elevation model for altitude at a given distance of the {@param origin}
	 *
	 * @param x distance from {@param origin}
	 * @return the altitude of the given position of the profile
	 * @throws IllegalArgumentException if {@param x} is not contained in the profile
	 */
	public double elevationAt(double x) {
		Preconditions.checkArgument(x >= 0 && x <= this.length,
				"the position is negative or bigger than the length of the profile");
		return elevationModel.elevationAt(positionAt(x));
	}

	/**
	 * Searches in the continuous elevation model for slope at a given distance of the {@param origin}
	 *
	 * @param x distance from {@param origin}
	 * @return the slope of the given position of the profile
	 * @throws IllegalArgumentException if {@param x} is not contained in the profile
	 */
	public double slopeAt(double x) {
		Preconditions.checkArgument(x >= 0 && x <= this.length,
				"the position is negative or bigger than the length of the profile");
		return elevationModel.slopeAt(positionAt(x));
	}
}
