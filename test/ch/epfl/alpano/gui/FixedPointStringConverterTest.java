package ch.epfl.alpano.gui;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by AntoineCrettenand on 03/05/2017.
 */
public class FixedPointStringConverterTest {
	@Test public void toStringWorksWithCasualValues() throws Exception {

		FixedPointStringConverter c = new FixedPointStringConverter(1);

		assertEquals("67.8", c.toString(678) );
	}

	@Test public void fromStringWorksWithCasualValues() throws Exception {

		FixedPointStringConverter c = new FixedPointStringConverter(1);

		System.out.println(c.fromString("12"));
		System.out.println(c.fromString("12.3"));
		System.out.println(c.fromString("12.34"));
		System.out.println(c.fromString("12.35"));
		System.out.println(c.fromString("12.36789"));
	}

	@Test (expected = IllegalArgumentException.class) public void methodsThrowsGoodExceptions() throws Exception {

		FixedPointStringConverter c = new FixedPointStringConverter(1);

		c.fromString("5.6.7");


	}

}