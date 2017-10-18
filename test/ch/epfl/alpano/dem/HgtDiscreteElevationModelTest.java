package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval1D;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by AntoineCrettenand on 08/03/2017.
 */

public class HgtDiscreteElevationModelTest {

	@Test public void checkNameWorksOnCasualValues() {

		String name = "S45W234.hgt";
		String name2 = "N45RW946.hgt";

		assertTrue(checkName(name));
		assertFalse(checkName(name2));
	}

	private boolean checkName(String string) {

		return (string.charAt(0) == 'N' || string.charAt(0) == 'S')
				&& new Interval1D(0, 100)
				.contains(Integer.parseInt(string.substring(1, 3))) && (
				string.charAt(3) == 'E' || string.charAt(3) == 'W')
				&& new Interval1D(0, 1000)
				.contains(Integer.parseInt(string.substring(4, 7))) && (string
				.substring(7, 11).equals(".hgt"));

	}

}