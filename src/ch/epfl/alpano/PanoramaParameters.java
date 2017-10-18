package ch.epfl.alpano;

/**
 * Class are parameters of the computed Panorama
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */
public final class PanoramaParameters {
	private final GeoPoint observerPosition;
	private final int observerElevation;
	private final double centerAzimuth;
	private final double horizontalFieldOfView;
	private final double verticalFieldOfView;
	private final int maxDistance;
	private final int width, height;

	/**
	 * Creates a new instance of the class
	 *
	 * @param observerPosition      not null
	 * @param observerElevation    Geopoint position
	 * @param centerAzimuth         canonical azimuth, in rad
	 * @param horizontalFieldOfView between 2PI (inclusive) and 0 (exclusive)
	 * @param maxDistance           positive in km
	 * @param width                 positive, pixel
	 * @param height                positive, pixel
	 * @throws NullPointerException if {@param observerPosition} is null
	 * @throws IllegalArgumentException if {@param centerAzimuth} is not canonical or if {@param horizontalFieldOfView}
	 * is not between 0 and 2*PI or if {@param maxDistance}, {@param width} or {@param height} are not positive
	 */
	public PanoramaParameters(GeoPoint observerPosition, int observerElevation,
			double centerAzimuth, double horizontalFieldOfView, int maxDistance,
			int width, int height) {

		if (observerPosition == null)
			throw new NullPointerException("observateur est null");
		Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth),
				"azimut n'est pas canonique");
		Preconditions.checkArgument(
				horizontalFieldOfView > 0 && horizontalFieldOfView <= Math2.PI2,
				"vue horizontale n'est pas compris entre 2pi et 0");
		Preconditions.checkArgument(maxDistance > 0 && width > 0 && height > 0,
				"largeur, hauteur ou distance maximale n'est pas strictement positive");

		this.observerPosition = observerPosition;
		this.observerElevation = observerElevation;
		this.centerAzimuth = centerAzimuth;
		this.horizontalFieldOfView = horizontalFieldOfView;
		this.verticalFieldOfView = horizontalFieldOfView * (height - 1) / (double) (width - 1);
		this.maxDistance = maxDistance;
		this.width = width;
		this.height = height;
	}

	/**
	 * Simple getters
	 *
	 * @return the associated variable
	 */
	public GeoPoint observerPosition() {
		return observerPosition;
	}

	public int observerElevation() {
		return observerElevation;
	}

	public double centerAzimuth() {
		return centerAzimuth;
	}

	public double horizontalFieldOfView() {
		return horizontalFieldOfView;
	}

	public int maxDistance() {
		return maxDistance;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	/**
	 * Compute verticalFieldOfView based on the other parameters
	 *
	 * @return double verticalFieldOfView
	 */
	public double verticalFieldOfView() {
		return verticalFieldOfView;
	}

	/**
	 * @param x horizontal pixel indice (between 0 and width-1)
	 * @return the associated azimuth
	 * @throws IllegalArgumentException if pixel indice x is not between 0 and width
	 * @see PanoramaParameters#width()
	 */
	public double azimuthForX(double x) {
		Preconditions.checkArgument((x >= 0) && (x <= width() - 1),
				"indice out of bounds");

		double delta = horizontalFieldOfView() / (width() - 1);

		double azimuth = delta * x - horizontalFieldOfView() / 2 + centerAzimuth();

		return Azimuth.canonicalize(azimuth);

	}

	/**
	 * @param a azimuth
	 * @return the associated horizontal pixel indice
	 * @throws IllegalArgumentException
	 */
	public double xForAzimuth(double a) {
		Preconditions.checkArgument(
				Math.abs(Math2.angularDistance(centerAzimuth(), a))
						<= horizontalFieldOfView / 2, "azimuth out of bound");

		double delta = horizontalFieldOfView() / (width() - 1);

		return (a + horizontalFieldOfView() / 2 - centerAzimuth()) / delta;
	}

	/**
	 * @param y vertical pixel indice (between 0 ant height-1)
	 * @return the associated altitude
	 * @throws IllegalArgumentException
	 * @see PanoramaParameters#height()
	 */
	public double altitudeForY(double y) {
		Preconditions.checkArgument((y >= 0) && (y <= height - 1),
				"indice out of bounds");

		double delta = horizontalFieldOfView() / (width() - 1);

		return verticalFieldOfView() / 2 - delta * y;
	}

	/**
	 * @param a azimuth
	 * @return the associated horizontal pixel indice
	 * @throws IllegalArgumentException
	 */
	public double yForAltitude(double a) {
		Preconditions.checkArgument(a >= -verticalFieldOfView() / 2
				&& a <= verticalFieldOfView() / 2);

		double delta = horizontalFieldOfView() / (width() - 1);

		return (verticalFieldOfView() / 2 - a) / delta;
	}

	/**
	 * @param x horizontal index
	 * @param y vertical index
	 * @return true if x and y are valid index, included in the range.
	 */
	public boolean isValidSampleIndex(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	/**
	 * @param x horizontal index
	 * @param y vertical index
	 * @return
	 */
	int linearSampleIndex(int x, int y) {
		return y * width + x;
	}
}
