package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;

/**
 * Set of practical methods to compute mathematical functions and operations
 */

public interface Math2 {
	double PI2 = 2 * Math.PI;

	/**
	 * @return value of {@param x} squared
	 */
	static double sq(double x) {
		return x * x;
	}

	/**
	 * @return remainder of the division by default of {@param x} by {@param y}
	 */
	static double floorMod(double x, double y) {
		return x - y * Math.floor(x / y);
	}

	/**
	 * Compute the value of haversine(x)
	 *
	 * @param x the value to compute
	 * @return haversine(x)
	 */
	static double haversin(double x) {
		return sq(Math.sin(x / 2));
	}

	/**
	 * Compute the difference angle between two angles {@param a1} and {@param a2}
	 * Both angles in rad
	 *
	 * @return angle include in [-pi;+pi[
	 */
	static double angularDistance(double a1, double a2) {
		return floorMod(a2 - a1 + Math.PI, Math2.PI2) - Math.PI;
	}

	/**
	 * Compute the value of f(x) by linear interpolation on the interval [0,1]
	 *
	 * @param y0 value of the function for x=0, f(0)=y0
	 * @param y1 value of the function for x=1, f(1)=y1
	 * @param x  any real number to compute
	 * @return value of f(x)
	 */
	static double lerp(double y0, double y1, double x) {
		if (x == 0.5)
			return (y0 + y1) / 2;

		return (y1 - y0) * x + y0;
	}

	/**
	 * Compute the value of f(x,y) by bilinear interpolation on the grid [0,1] x [0,1]
	 *
	 * @param z00 value of f(0,0)
	 * @param z01 value of f(0,1)
	 * @param z10 value of f(1,0)
	 * @param z11 value of f(1,1)
	 * @param x   real number
	 * @param y   real number
	 * @return value of f(x,y)
	 */
	static double bilerp(double z00, double z01, double z10, double z11,
			double x, double y) {
		return lerp(lerp(z00, z10, y), lerp(z01, z11, y), x);
	}

	/**
	 * Compute an interval of size {@param dX} containing a root of the function
	 * From left to right : Tries for interval [minX,minX+dX], then [minX+dX,minX+2*dX]
	 * etc until an interval with a root is found.
	 *
	 * @param f    the function
	 * @param minX starting value, low endpoint of the interval
	 * @param maxX max value
	 * @param dX   length of the interval
	 * @return the minimum of the first interval found containing a root
	 * if no interval is found (=minX+i*dX reaches maxdX), return Double.POSITIVE_INFINITY
	 */
	static double firstIntervalContainingRoot(DoubleUnaryOperator f,
			double minX, double maxX, double dX) {
		int i = 0;
		while (minX + i * dX <= maxX) {
			if (f.applyAsDouble(minX + i * dX) * f
					.applyAsDouble(minX + dX + i * dX) < 0)
				return minX + i * dX;
			i++;
		}
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * The method is used to numerically solve the equation f(x) = 0 for the real variable x
	 *
	 * @param f       the function
	 * @param x1      minimum (endpoint) of the interval
	 * @param x2      maximum (endpoint) of the interval
	 * @param epsilon precision of approximation
	 * @return an aproximation of the root
	 */
	static double improveRoot(DoubleUnaryOperator f, double x1, double x2,
			double epsilon) {
		Preconditions
				.checkArgument(f.applyAsDouble(x1) * f.applyAsDouble(x2) < 0);

		while (Math.abs(x2 - x1) > epsilon) {
			double middle = (x1 + x2) / 2;
			if (f.applyAsDouble(middle) * f.applyAsDouble(x1) < 0) {
				x2 = middle;
			} else if (f.applyAsDouble(middle) == 0)
				return middle;
			else
				x1 = middle;
		}
		return x1;
	}

}
