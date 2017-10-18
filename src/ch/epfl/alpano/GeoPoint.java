package ch.epfl.alpano;

import java.util.Locale;
import java.util.Objects;

import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.Azimuth.fromMath;
import static ch.epfl.alpano.Math2.haversin;
import static java.lang.Math.*;

/**
 * A point on the surface of the earth, described in a spherical coordinate
 * system with two variable coordinates, longitude and latitude in rad, and
 * radius constant = EARTH_RADIUS (defined in Distance.class)
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */
public final class GeoPoint {
    private final double longitude;
    private final double latitude;

    /**
     * @param longitude in rad
     * @param latitude  in rad
	 * @throws IllegalArgumentException if {@param longitude} not between -PI and PI or if {@param latitude} not
	 * between -PI/2 and PI/2
	 */
    public GeoPoint(double longitude, double latitude) {
        Preconditions.checkArgument(
                (longitude >= -PI && longitude <= PI) && (latitude >= -PI / 2
                        && latitude <= PI / 2));

        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return longitude
     */
    public double longitude() {
        return longitude;
    }

    /**
     * @return latitude
     */
    public double latitude() {
        return latitude;
    }

    /**
     * Compute the distance along the surface of the earth between two GeoPoints
     *
     * @param that GeoPoint to compute distance to
     * @return distance in meters
     * @throws NullPointerException if input == null
     */
    public double distanceTo(GeoPoint that) {
		Objects.requireNonNull(that);
        double alpha = 2 * asin(sqrt(haversin(latitude() - that.latitude())
                + cos(latitude()) * cos(that.latitude()) * haversin(
                longitude() - that.longitude())));

        return Distance.toMeters(alpha);
    }

    /**
     * Compute the azimuth angle between two GeoPoints
     *
     * @param that GeoPoint to compute azimuth angle to
     * @return azimuth angle in rad
     * @throws NullPointerException if input == null
     */
    public double azimuthTo(GeoPoint that) {
        if (that == null)
            throw new NullPointerException();

        double b1 =
                sin(this.longitude() - that.longitude()) * cos(that.latitude());
        double b2 = cos(this.latitude()) * sin(that.latitude())
                - sin(this.latitude()) * cos(that.latitude) * cos(
                this.longitude() - that.longitude());
        return fromMath(canonicalize(atan2(b1, b2)));

    }

    @Override
    public String toString() {
        Locale l = null;
        return String.format(l, "(%.4f,%.4f)", toDegrees(longitude()),
                toDegrees(latitude()));
    }
}
