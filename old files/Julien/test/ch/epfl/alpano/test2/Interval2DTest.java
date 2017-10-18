package ch.epfl.alpano.test2;

import org.junit.Test;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

import static org.junit.Assert.*;

/**
 * Created by AntoineCrettenand on 22/02/2017.
 */

public class Interval2DTest {
	final Interval2D a = new Interval2D(new Interval1D(3,6),new Interval1D(4,7));
	final Interval2D b = new Interval2D(new Interval1D(-2,0),new Interval1D(10,12));
	final Interval2D c = new Interval2D(new Interval1D(0,3), new Interval1D(3,5));
	final Interval2D d = new Interval2D(new Interval1D(-2,2),new Interval1D(4,6));
	final Interval2D e = new Interval2D(new Interval1D(3,6), new Interval1D(4,7));
	final Interval2D f = new Interval2D(new Interval1D(0,0),new Interval1D(0,0));

	@Test public void sizeWorksWithCasualInputs(){

		assertEquals(16,a.size());
		assertEquals(9,b.size());
		assertEquals(12,c.size());


	}

	@Test public void containsWorksWithCasualValues() throws Exception {
		Interval2D d = new Interval2D(new Interval1D(-2,2),new Interval1D(4,6));

		assertTrue(d.contains(1,4));
		assertTrue(d.contains(0,6));
		assertFalse(d.contains(-4,4));
		assertFalse(d.contains(0,2));
		assertFalse(d.contains(-10,10));

	}

	@Test public void sizeOfIntersectionWith() throws Exception {

		assertEquals(2,a.sizeOfIntersectionWith(c));
		assertEquals(0,a.sizeOfIntersectionWith(b));
		assertEquals(0,f.sizeOfIntersectionWith(c));

	}

	@Test public void boundingUnion() throws Exception {
		assertEquals(new Interval2D(new Interval1D(-2,6),new Interval1D(4,12)),
				a.boundingUnion(b));

		assertEquals(new Interval2D(new Interval1D(0,6),new Interval1D(3,7)),
				a.boundingUnion(c));

		assertEquals(new Interval2D(new Interval1D(-2,2),new Interval1D(4,12)),
				b.boundingUnion(d));
	}

	@Test public void isUnionableWithWorksWithCasualInputs() throws Exception {

		assertFalse("1",a.isUnionableWith(b));
		assertFalse("2",a.isUnionableWith(c));
		assertFalse("3",b.isUnionableWith(c));
		assertFalse("4",a.isUnionableWith(d));

	}

	@Test public void union() throws Exception {

	}

	@Test public void equals() throws Exception {
		assertTrue(a.equals(e));

	}

	@Test public void toStringWorksOnCasualValues() throws  Exception {
		assertEquals("[3..6]x[4..7]",a.toString());

	}

}