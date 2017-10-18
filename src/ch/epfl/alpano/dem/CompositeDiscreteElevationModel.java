package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

import static java.util.Objects.requireNonNull;

/**
 * Class represents a discrete elevation model as the union of two discrete elevation models (DEM)
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public final class CompositeDiscreteElevationModel implements DiscreteElevationModel {
	private final DiscreteElevationModel dem1;
	private final DiscreteElevationModel dem2;
	private final Interval2D union;

	/**
	 * Creates a new instance of the class
	 *
	 * @param dem1 discrete elevation model
	 * @param dem2 discrete elevation model
	 * @throws NullPointerException if {@param dem1} or {@param dem2} is null
	 */
	public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2) {

		this.dem1 = requireNonNull(dem1, "Discrete Elevation Model is null");
		this.dem2 = requireNonNull(dem2, "Discrete Elevation Model is null");
		union = dem1.extent().union(dem2.extent());
	}

	@Override public Interval2D extent() {
		return union;
	}

	@Override public double elevationSample(int x, int y) {

		if (dem1.extent().contains(x, y))
			return dem1.elevationSample(x, y);
		else if (dem2.extent().contains(x, y))
			return dem2.elevationSample(x, y);
		else
			throw new IllegalArgumentException("Both Discrete Elevation Models does not contain the sample");
	}

	@Override public void close() throws Exception {
		dem1.close();
		dem2.close();
	}
}
