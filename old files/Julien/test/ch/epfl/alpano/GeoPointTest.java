package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;
import static ch.epfl.alpano.Distance.*
;
// Test pas tout à fait fiable
public class GeoPointTest {
    GeoPoint northPole = new GeoPoint(Math.toRadians(0.), Math.toRadians(90.));
    GeoPoint southPole = new GeoPoint(Math.toRadians(0.), Math.toRadians(-90.));
    GeoPoint lausanne = new GeoPoint(Math.toRadians(6.631), Math.toRadians(46.521));
    GeoPoint moscou = new GeoPoint(Math.toRadians(37.623), Math.toRadians(55.753));       
    GeoPoint toronto = new GeoPoint(Math.toRadians(-79.381667), Math.toRadians(43.6525));       
    GeoPoint toString = new GeoPoint(Math.toRadians(-7.6543), Math.toRadians(54.3210));
    
    @Test(expected=IllegalArgumentException.class)
    public void longInInitOver180(){
        GeoPoint g= new GeoPoint(Math.nextUp(180),0);
    }    @Test(expected=IllegalArgumentException.class)

    public void longInInitUnderMinus180(){
        GeoPoint g= new GeoPoint(Math.nextDown(-180),0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void latInInitOver90(){
   	 GeoPoint g= new GeoPoint(0,Math.nextUp(90));
    }

    @Test(expected=IllegalArgumentException.class)
    public void latInInitUnderMinus90(){
        GeoPoint g= new GeoPoint(0,Math.nextDown(-90));
    }
    
    @Test
    public void returnLongitudeInRadTest() {        
        assertEquals(Math.toRadians(0.), northPole.longitude(), 1E-4);
    }
    
    @Test
    public void returnLatitudeInRadTest() {
        assertEquals(Math.toRadians(90.), northPole.latitude(), 1E-4);
    }
    
    @Test
    public void distanceToTest() {
        assertEquals(0., northPole.distanceTo(northPole), 1E-4);
        assertEquals(EARTH_RADIUS*Math.PI, northPole.distanceTo(southPole), 1E-4);
        assertEquals(2367148., lausanne.distanceTo(moscou), 1);
    }
    
    @Test
    public void azimuthToTest() {
        assertEquals(Math.toRadians(52.95), lausanne.azimuthTo(moscou), 1E-2);
    }
    @Test (expected = IllegalArgumentException.class)
    public void azimuthToExcpetionTest() {
        assertEquals(Math.toRadians(52.95), lausanne.azimuthTo(lausanne), 1E-2);
    }
    
    @Test
    public void toStringTest () {
        assertEquals("(-7.6543,54.3210)", toString.toString());
    }
    
    
}
