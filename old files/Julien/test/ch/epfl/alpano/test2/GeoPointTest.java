package ch.epfl.alpano.test2;

import ch.epfl.alpano.GeoPoint;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by AntoineCrettenand on 21/02/2017.
 */

public class GeoPointTest {

	@Test(expected = IllegalArgumentException.class) public void constructorDoesntWorkOnWrongInputs() {
		GeoPoint test = new GeoPoint(-46, 2500);
	}

	@Test public void distanceToWorksOnNormalValues() {
		GeoPoint lausanne = new GeoPoint(Math.toRadians(6.631),
				Math.toRadians(46.521));
		GeoPoint moscou = new GeoPoint(Math.toRadians(37.623),
				Math.toRadians(55.753));

		assertEquals(2370000, lausanne.distanceTo(moscou), 3000);

		GeoPoint brasilia = new GeoPoint(Math.toRadians(-47.92),
				Math.toRadians(-15.77));

		assertEquals(8800000, lausanne.distanceTo(brasilia), 11000);

		GeoPoint paris = new GeoPoint(Math.toRadians(2.34),
				Math.toRadians(48.86));

		assertEquals(413300, lausanne.distanceTo(paris), 10000);

		GeoPoint tokyo = new GeoPoint(Math.toRadians(139.8),
				Math.toRadians(35.67));

		assertEquals(9771000, lausanne.distanceTo(tokyo), 100000);
	}

	@Test public void distanceToWorksOnLimits() {
		GeoPoint nul = new GeoPoint(0, 0);

		assertEquals(0, nul.distanceTo(nul), 0);

	}

	@Test public void azimuthToWorksOnNormalValues() {
		GeoPoint lausanne = new GeoPoint(Math.toRadians(6.631),
				Math.toRadians(46.521));
		GeoPoint moscou = new GeoPoint(Math.toRadians(37.623),
				Math.toRadians(55.753));

		assertEquals(52.95, Math.toDegrees(lausanne.azimuthTo(moscou)), 0.1);

		GeoPoint paris = new GeoPoint(Math.toRadians(2.34),
				Math.toRadians(48.86));

	}

	@Test public void ToStringWorks() {
		GeoPoint lausanne = new GeoPoint(Math.toRadians(-7.6543),
				Math.toRadians(54.3210));

		assertEquals("(-7.6543,54.3210)", lausanne.toString());

	}
}
