package ch.epfl.alpano.test2;

import ch.epfl.alpano.Interval1D;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by AntoineCrettenand on 21/02/2017.
 */

public class Interval1DTest {

	@Test(expected = IllegalArgumentException.class) public void constructorThrowsExceptionOnWrongInput() {
		Interval1D test = new Interval1D(3, 2);
	}

	@Test public void containsWorksOnCasualInputs() {
		Interval1D a = new Interval1D(1, 5);
		for (int i = a.includedFrom(); i < a.includedTo() + 1; i++) {
			assertTrue(a.contains(i));
			assertFalse(a.contains(i + 10));
		}
	}

	@Test public void sizeWorksOnCasualInputs() {
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(-2, 2);
		Interval1D c = new Interval1D(5, 8);

		assertEquals(4, a.size());
		assertEquals(5, b.size());
		assertEquals(4, c.size());
	}

	@Test public void sizeOfIntersectionWithWorksOnCasualInputs() {
		Interval1D a = new Interval1D(1, 10);
		Interval1D b = new Interval1D(3, 7);

		assertEquals(5, a.sizeOfIntersectionWith(b), 0);

		Interval1D c = new Interval1D(-4, 5);
		assertEquals(5, a.sizeOfIntersectionWith(c), 0);

		Interval1D d = new Interval1D(-2, 12);
		assertEquals(10, a.sizeOfIntersectionWith(d));
	}

	@Test public void boundingUnionWorksWithCasualInputs() {
		Interval1D a = new Interval1D(0, 7);

		Interval1D b = new Interval1D(3, 5);

		assertEquals(new Interval1D(0, 7), a.boundingUnion(b));

		Interval1D c = new Interval1D(-6, 2);

		assertEquals(new Interval1D(-6, 7), a.boundingUnion(c));

		Interval1D d = new Interval1D(6, 10);

		assertEquals(new Interval1D(0, 10), a.boundingUnion(d));


	}

	@Test public void IsUnionableWithWorksOnGoodInputs() {
		Interval1D a = new Interval1D(0, 3);
		Interval1D b = new Interval1D(-3, 1);
		Interval1D c = new Interval1D(2, 6);
		Interval1D d = new Interval1D(0, 1);
		Interval1D e = new Interval1D(10, 20);
		Interval1D f = new Interval1D(1, 3);
		Interval1D g = new Interval1D(0,0);

		assertTrue(a.isUnionableWith(b));
		assertTrue(a.isUnionableWith(c));
		assertTrue(a.isUnionableWith(d));
		assertFalse(a.isUnionableWith(e));
		assertTrue(c.isUnionableWith(d));
		assertTrue(d.isUnionableWith(f));
		assertTrue(a.isUnionableWith(g));
		assertFalse(c.isUnionableWith(g));
	}

	@Test public void equalsWorks() {
		Interval1D a = new Interval1D(1, 5);
		Interval1D b = new Interval1D(1, 5);
		Interval1D c = new Interval1D(0,5);

		assertTrue(b.equals(a));
		assertFalse(a.equals(c));

	}

	@Test public void toStringWorks() {
		Interval1D a = new Interval1D(3, 8);
		assertEquals("[3..8]", a.toString());

		Interval1D b = new Interval1D(-3, 5);
		assertEquals("[-3..5]", b.toString());

		Interval1D c = new Interval1D(10, 14);
		assertEquals("[10..14]", c.toString());
	}

}

