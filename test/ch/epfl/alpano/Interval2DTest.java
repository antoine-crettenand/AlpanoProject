package ch.epfl.alpano;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 *
 */
public class Interval2DTest {
	
	private Interval1D i1D1=new Interval1D(1,4);
	private Interval1D i1D2=new Interval1D(-3,4);
	private Interval1D i1D3=new Interval1D(-3,1);
	private Interval1D i1D4=new Interval1D(0,2);
	private Interval2D i1 = new Interval2D(i1D1,i1D2);
	private Interval2D i2 = new Interval2D(i1D3,i1D4);
	private Interval1D nI1D1 = new Interval1D(-3,4);
	private Interval1D nI1D2 = new Interval1D(3,7);
	private Interval1D nI1D3 = new Interval1D(7,7);
	private Interval1D nI1D4 = new Interval1D(-3,-1);
	private Interval2D nonIntersecting1 = new Interval2D(nI1D1, nI1D2);
	private Interval2D nonIntersecting2 = new Interval2D(nI1D3, nI1D4);
	
	@Test(expected=NullPointerException.class)
	public void NullIntervalInConstructor() {
		Interval2D i = new Interval2D(null, null);
		i.iX();
	}
	
	@Test
	public void getFirstInterval(){
		assertEquals(i1D1, i1.iX());
	}
	
	@Test
	public void getSecondInterval(){
		assertEquals(i1D2, i1.iY());
	}
	
	@Test
	public void containsPair(){
		Interval2D i = new Interval2D(new Interval1D(-3,4), new Interval1D(3,7));
		assertEquals(true,i.contains(-3, 7));
		assertEquals(true,i.contains(4, 3));
		assertEquals(false,i.contains(-3, 8));
		assertEquals(false,i.contains(-4, 7));
		assertEquals(false,i.contains(-8, 10));
	}
	
	@Test
	public void randomSize(){
		Random rdm=new Random();
		int inf1=rdm.nextInt(1000)-500;
		int inf2=rdm.nextInt(1000)-500;
		int sup1=inf1+rdm.nextInt(20);
		int sup2=inf2+rdm.nextInt(20);
		Interval2D i = new Interval2D(new Interval1D(inf1,sup1), new Interval1D(inf2, sup2));
		assertEquals((sup1-inf1+1)*(sup2-inf2+1),i.size());
	}
	
	@Test
	public void nullSizeOfIntersection(){
		assertEquals(0,nonIntersecting1.sizeOfIntersectionWith(nonIntersecting2));
	}
	
	@Test
	public void sizeOfIntersection(){
		assertEquals(i1D1.sizeOfIntersectionWith(i1D3)*i1D2.sizeOfIntersectionWith(i1D4),
				i1.sizeOfIntersectionWith(i2));
	}
	
	@Test
	public void boundingUnion(){
		assertEquals(new Interval2D(i1D1.boundingUnion(i1D3),i1D2.boundingUnion(i1D4)),
				i1.boundingUnion(i2));
	}
	
	@Test
	public void equalsTest(){
		assertEquals(true,new Interval2D(i1D1,i1D2).equals(i1));
		assertEquals(false,i1.equals(i2));
		assertFalse(i1.equals(null));
	}
	
	@Test
	public void hashCodeTest(){
		assertEquals(i1.hashCode(),new Interval2D(i1D1,i1D2).hashCode());
		assertEquals(false,i1.hashCode()==i2.hashCode());
	}
	
	@Test
	public void toStringTest(){
		assertEquals("[1..4]x[-3..4]",i1.toString());
	}
	
	
	
}
