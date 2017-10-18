package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval1DTest {
    Interval1D testInterval = new Interval1D(-20, 20);
    Interval1D testInterval2 = new Interval1D(-20, 20);
    Interval1D testIntersection1 = new Interval1D(0, 20);
    Interval1D testIntersection2 = new Interval1D(-20, 0);
    Interval1D testIntersection3 = new Interval1D(-20, 20);
    Interval1D testIntersection4 = new Interval1D(-1000, -20);
    Interval1D testIntersection5 = new Interval1D(20, 1000);
    Interval1D testIntersection6 = new Interval1D(21, 1000);
    Interval1D testIntersection7 = new Interval1D(-1000, -21);
    Interval1D testBoundingUnion1 = new Interval1D(-1000, 0);
    Interval1D testBoundingUnion2 = new Interval1D(5, 1000);
    Interval1D testBoundingUnion3 = new Interval1D(-1000, 1000);
    
    @Test (expected = IllegalArgumentException.class)
    public void ConstructorThrowsExceptionTest() {
        Interval1D a = new Interval1D(52, 20);
    }
    
    
    @Test
    public void returnMinAndMaxElementTest(){
        assertEquals(-20, testInterval.includedFrom());
        assertEquals(20, testInterval.includedTo());
    }
    
    
    @Test
    public void containsTest(){
        assertEquals(true, testInterval.contains(0));
        assertEquals(true, testInterval.contains(-20));
        assertEquals(true, testInterval.contains(20));
        assertEquals(false, testInterval.contains(-21));
        assertEquals(false, testInterval.contains(-21));
        assertEquals(false, testInterval.contains(24890));
        assertEquals(false, testInterval.contains(-24890));
    }
    
    @Test
    public void sizeTest(){
        assertEquals(41, testInterval.size());
    }
    
    @Test
    public void sizeIntersectionTest()
    {
        assertEquals(21, testInterval.sizeOfIntersectionWith(testIntersection1));
        assertEquals(21, testInterval.sizeOfIntersectionWith(testIntersection2));
        assertEquals(41, testInterval.sizeOfIntersectionWith(testIntersection3));
        assertEquals(1, testInterval.sizeOfIntersectionWith(testIntersection4));
        assertEquals(1, testInterval.sizeOfIntersectionWith(testIntersection5));
        assertEquals(0, testInterval.sizeOfIntersectionWith(testIntersection6));
        assertEquals(0, testInterval.sizeOfIntersectionWith(testIntersection7));    
    }
    
    @Test
    public void boundingUnionTest()
    {
        assertEquals(testInterval, testIntersection1.boundingUnion(testIntersection2));
        assertEquals(testInterval, testIntersection1.boundingUnion(testIntersection3));
        assertEquals(testBoundingUnion3, testBoundingUnion1.boundingUnion(testBoundingUnion2));
    }
    
    @Test
    public void UnionTest()
    {
        assertEquals(testInterval, testIntersection1.boundingUnion(testIntersection2));
        assertEquals(testInterval, testIntersection1.boundingUnion(testIntersection3));
        assertEquals(testBoundingUnion3, testBoundingUnion1.boundingUnion(testBoundingUnion2));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void UnionExceptionTest()
    {
        assertEquals(testBoundingUnion3, testBoundingUnion1.union(testBoundingUnion2));
    }
    
    @Test
    public void equalsTest()
    {
        assertEquals(true, testInterval2.equals(testInterval));
    }
    
    @Test
    public void hashCodeTest()
    {
        assertEquals(true, testInterval2.hashCode() == testInterval.hashCode());
        assertEquals(false, testIntersection4.hashCode() == testInterval2.hashCode());
        assertEquals(false, testIntersection7.hashCode() == testInterval.hashCode());
        
    }
    
    @Test
    public void toStringTest()
    {
        String test1 = new String("[-20..20]");
        assertEquals(test1, testInterval.toString());
        String test2 = new String("[5..1000]");
        assertEquals(test2, testBoundingUnion2.toString());
        String test3 = new String("[-20..0]");
        assertEquals(test3, testIntersection2.toString());
    }

}
