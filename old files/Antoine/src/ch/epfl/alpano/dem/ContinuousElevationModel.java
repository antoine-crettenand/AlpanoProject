package ch.epfl.alpano.dem;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

import static java.util.Objects.requireNonNull;

/**
 * Created by AntoineCrettenand on 27/02/2017.
 */

public final class ContinuousElevationModel {
	private final DiscreteElevationModel dem;
	private final static double d = Distance
			.toMeters(1 / DiscreteElevationModel.SAMPLES_PER_RADIAN);

	public ContinuousElevationModel(DiscreteElevationModel dem) {
		this.dem = requireNonNull(dem);
	}

	public double elevationAt(GeoPoint p) {

		int x = (int) DiscreteElevationModel.sampleIndex(p.longitude());
		int y = (int) DiscreteElevationModel.sampleIndex(p.latitude());

		return Math2.bilerp(elevationAt(x, y), elevationAt(x, y + 1),
				elevationAt(x + 1, y), elevationAt(x + 1, y + 1), p.longitude(),
				p.latitude());
	}

	public double slopeAt(GeoPoint p) {
		int x = (int) DiscreteElevationModel.sampleIndex(p.longitude());
		int y = (int) DiscreteElevationModel.sampleIndex(p.latitude());

		return Math2.bilerp(slopeAt(x, y), slopeAt(x, y + 1), slopeAt(x + 1, y),
				slopeAt(x + 1, y + 1), p.longitude(), p.latitude());

	}

	private double elevationAt(int x, int y) {
		return dem.elevationSample(x, y);
	}

	private double slopeAt(int x, int y) {
		double dza = elevationAt(x + 1, y) - elevationAt(x, y);

		double dzb = elevationAt(x, y + 1) - elevationAt(x, y);

		System.out.println(Math2.sq(dza) + Math2.sq(dzb) + Math2.sq(d));

		return Math.acos(d / Math
				.sqrt(Math2.sq(dza) + Math2.sq(dzb) + Math2.sq(d)));
	}
}
