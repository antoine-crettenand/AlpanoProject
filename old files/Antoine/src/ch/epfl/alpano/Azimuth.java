package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.PI2;

/**
 * Set of methods operating on azimuth angles
 */

public interface Azimuth {

	/**
	 * @param azimuth angle in rad
	 * @return true if angle is positive and less than 2*PI
	 */
	static boolean isCanonical(double azimuth) {
		return (azimuth >= 0 && azimuth < PI2);
	}

	/**
	 * Compute canonical angle of the input
	 *
	 * @param azimuth angle in rad
	 * @return an angle contained in [0,2pi[
	 */
	static double canonicalize(double azimuth) {
		return Math2.floorMod(azimuth, PI2);
	}

	/**
	 * Convert azimuth angle into mathematical angle
	 *
	 * @param azimuth angle in rad
	 * @return mathematical angle
	 */
	static double toMath(double azimuth) {
		Preconditions.checkArgument(isCanonical(azimuth),
				"azimuth is not canonical");
		return canonicalize(-azimuth);
	}

	/**
	 * Convert mathematical angle into azimuth angle
	 *
	 * @param angle angle in rad
	 * @return azimuth angle
	 */
	static double fromMath(double angle) {
		Preconditions.checkArgument(isCanonical(angle),
				"azimuth is not canonical");
		return canonicalize(-angle);
	}

	/**
	 * Compute the octant in which the azimuth angle is
	 *
	 * @param azimuth angle in rad
	 * @param n       North
	 * @param e       East
	 * @param s       South
	 * @param w       West
	 * @return a String representing the octant in which the azimuth is
	 */
	static String toOctantString(double azimuth, String n, String e, String s,
			String w) {
		Preconditions.checkArgument(isCanonical(azimuth),
				"azimuth is not canonical");
		if (azimuth < Math.PI / 8 || azimuth > 15 * Math.PI / 8)
			return n;
		else if (azimuth < 3 * Math.PI / 8)
			return n + e;
		else if (azimuth < 5 * Math.PI / 8)
			return e;
		else if (azimuth < 7 * Math.PI / 8)
			return s + e;
		else if (azimuth < 9 * Math.PI / 8)
			return s;
		else if (azimuth < 11 * Math.PI / 8)
			return s + w;
		else if (azimuth < 13 * Math.PI / 8)
			return w;
		else
			return n + w;

	}

}
