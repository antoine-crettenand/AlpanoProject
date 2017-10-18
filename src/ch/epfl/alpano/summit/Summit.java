package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;

/**
 * Class represents a Summit
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public final class Summit {

	private final String name;
	private final GeoPoint position;
	private final int elevation;

	/**
	 * Creates a new instance of the class
	 *
	 * @param name of the summit
	 * @param position GeoPoint of the summit on Earth
	 * @param elevation altitude of the summit
	 * @throws NullPointerException if name or position are null
	 */
	public Summit(String name, GeoPoint position, int elevation) {

		if(name == null || position == null){
			throw new NullPointerException("name or position are null");
		}

		this.name = name;
		this.position = position;
		this.elevation = elevation;
	}

	/**
	 * Simple getters
	 * @return the associated variable
	 */
	public String name() {
		return name;
	}
	public GeoPoint position() {
		return position;
	}
	public int elevation() {
		return elevation;
	}

	@Override public String toString() {
		return name.toUpperCase() + " " + position.toString() + " " + elevation;
	}

}
