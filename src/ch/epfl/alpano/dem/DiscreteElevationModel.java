package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

/**
 * Class emulates a discrete digital elevation model (DEM) of a fraction of
 * the surface of the earth. The surface is discrete and each
 * GeoPoint (with integer coordinates) composing the surface is assigned an
 * altitude value. Each discrete GeoPoint is separated by 1'' (a second of arc).
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public interface DiscreteElevationModel extends AutoCloseable {

    int SAMPLES_PER_DEGREE = 3600;
    double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE / Math.toRadians(1);

    /**
	 * We can compute the index of an angle since
	 * "Each discrete GeoPoint is separated by 1'' (a second of arc)"
	 *
	 * @param radAngle angle in radian
	 * @return index related to radAngle
     */
    static double sampleIndex(double radAngle) {
        return radAngle * SAMPLES_PER_RADIAN;
    }

    /**
	 * Compute the union of two DEM.
	 *
	 * @param that DEM
	 * @return union of this discret digital elevation model (DEM) with that
	 * @throws IllegalArgumentException if {this} is not unionable with
	 */
    default DiscreteElevationModel union(DiscreteElevationModel that) {
        Preconditions.checkArgument(extent().isUnionableWith(that.extent()));
        return new CompositeDiscreteElevationModel(this, that);
    }

    /**
	 * Since every DEM covers only a fraction of the surface of the earth,
	 *  we need to compute the "extent" of a DEM, i.e. the grid the DEM covers
	 *
	 * @return the extent of the DEM
	 */
    Interval2D extent();

    /**
	 * Compute the altitude value at a given GeoPoint(x,y) with integer coordinates
	 *
     * @param x x-axis coordonate
     * @param y y-axis corrdonate
     * @return altitude of the point at (x,y) coordonnates
     */
    double elevationSample(int x, int y);

}
