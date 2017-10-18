package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

import static java.util.Objects.requireNonNull;

/**
 * Created by AntoineCrettenand on 27/02/2017.
 */

public final class CompositeDiscreteElevationModel implements DiscreteElevationModel {
	private final DiscreteElevationModel dem1;
	private final DiscreteElevationModel dem2;
	

	public CompositeDiscreteElevationModel(DiscreteElevationModel dem1,
			DiscreteElevationModel dem2) {

		this.dem1 = requireNonNull(dem1);
		this.dem2 = requireNonNull(dem2);
	}

	@Override public Interval2D extent() {
		return dem1.extent().union(dem2.extent());
	}

	@Override public double elevationSample(int x, int y) {
		if (dem1.extent().contains(x,y))
			return dem1.elevationSample(x,y);
		else return dem2.elevationSample(x,y);

	}

	@Override public void close() throws Exception {
		dem1.close();
		dem2.close();
	}
}
