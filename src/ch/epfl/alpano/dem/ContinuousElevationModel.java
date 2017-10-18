package ch.epfl.alpano.dem;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

import static java.util.Objects.requireNonNull;

/**
 * Class emulates a continuous digital elevation model based on a discrete one (DEM)
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public final class ContinuousElevationModel {
	private final static double d = Distance.toMeters(1 / DiscreteElevationModel.SAMPLES_PER_RADIAN);
	private final DiscreteElevationModel dem;

	/**
	 * Creates a new instance of the class based on a dem
	 *
	 * @param dem DEM
	 * @throws NullPointerException if {@param dem} is null
	 */
	public ContinuousElevationModel(DiscreteElevationModel dem) {
		this.dem = requireNonNull(dem);
	}

	/**
	 * Compute the altitude value at any given GeoPoint. If the GeoPoint is not
	 * in the DEM, then its altitude value is computed with a billinear
	 * interpolation and the nearest known GeoPoints altitude values
	 * <p>
	 * Note : the altitude of a GeoPoint which is not in the DEM extent is 0
	 *
	 * @param p any GeoPoint in
	 * @return the altitude of the geopoint p (in meters)
	 */
	public double elevationAt(GeoPoint p) {

		double x = DiscreteElevationModel.sampleIndex(p.longitude());
		double y = DiscreteElevationModel.sampleIndex(p.latitude());

		int[] z = squareContainingGeoPoint(p);

		int x0 = z[0];
		int y0 = z[1];
		int x1 = z[2];
		int y1 = z[3];

		return Math2.bilerp(elevationAt(x0, y0), elevationAt(x1, y0), elevationAt(x0, y1), elevationAt(x1, y1), x - x0,
				y - y0);
	}

	/**
	 * Compute the the slope value at any given GeoPoint. If the GeoPoint is not
	 * in the DEM, then it's slope value is computed with a billinear
	 * interpolation and the nearest known GeoPoints slope values
	 *
	 * @param p any GeoPoint
	 * @return the slope of the geopoint p
	 */
	public double slopeAt(GeoPoint p) {

		double x = DiscreteElevationModel.sampleIndex(p.longitude());
		double y = DiscreteElevationModel.sampleIndex(p.latitude());

		int[] z = squareContainingGeoPoint(p);

		int x0 = z[0];
		int y0 = z[1];
		int x1 = z[2];
		int y1 = z[3];

		return Math2.bilerp(slopeAt(x0, y0), slopeAt(x1, y0), slopeAt(x0, y1), slopeAt(x1, y1), x - x0, y - y0);
	}

	private int[] squareContainingGeoPoint(GeoPoint p) {

		int[] zone = new int[4];

		zone[0] = (int) Math.floor(DiscreteElevationModel.sampleIndex(p.longitude()));

		zone[1] = (int) Math.floor(DiscreteElevationModel.sampleIndex(p.latitude()));

		zone[2] = (int) Math.ceil(DiscreteElevationModel.sampleIndex(p.longitude()));

		zone[3] = (int) Math.ceil(DiscreteElevationModel.sampleIndex(p.latitude()));

		return zone;

	}

	private double elevationAt(int x, int y) {
		return (dem.extent().contains(x, y) ? dem.elevationSample(x, y) : 0);
	}

	private double slopeAt(int x, int y) {
		double dza = elevationAt(x + 1, y) - elevationAt(x, y);

		double dzb = elevationAt(x, y + 1) - elevationAt(x, y);

		return Math.acos(d / Math.sqrt(Math2.sq(dza) + Math2.sq(dzb) + Math2.sq(d)));
	}

	public DiscreteElevationModel dem(){return dem;}

}
