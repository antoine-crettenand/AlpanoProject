package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

/**
 * Created by AntoineCrettenand on 27/02/2017.
 */

public interface DiscreteElevationModel extends AutoCloseable {

	int SAMPLES_PER_DEGREE = 3600;
	double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE / Math.toRadians(1);

	default DiscreteElevationModel union(DiscreteElevationModel that) {
		return new CompositeDiscreteElevationModel(this, that);
	}

	static double sampleIndex(double radAngle) {
		return radAngle * SAMPLES_PER_RADIAN;
	}

	Interval2D extent();

	double elevationSample(int x, int y);

}
