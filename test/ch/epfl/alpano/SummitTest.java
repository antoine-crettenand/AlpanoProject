package ch.epfl.alpano;

import ch.epfl.alpano.summit.Summit;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Test de ch.epfl.alpano.summit.Summit
 *
 * @author Julien Sahli
 * @date 22.03.2017
 */
public class SummitTest {

    private GeoPoint standardGeopoint = new GeoPoint(0.0,0.0);

    @Test (expected = NullPointerException.class)
    public void nullNameThrowsNullPointerException(){
        String s = null;
        new Summit(s,standardGeopoint,0);
    }

    @Test (expected = NullPointerException.class)
    public void nullGeopointThrowsNullPointerException(){
        GeoPoint g=null;
        new Summit("1",g,1);
    }
    @Test
    public void nameSustainability(){
        String s = new String(Integer.toString(new Random().nextInt(1000)));
        Assert.assertEquals(new Summit(s,standardGeopoint,0).name(), s);
    }
    @Test
    public void positionSustainability(){
        GeoPoint g = new GeoPoint(new Random().nextDouble(),new Random().nextDouble());
        Assert.assertEquals(new Summit("1",g,0).position(), g);
    }
    @Test
    public void elevationSustainability(){
        int i = new Random().nextInt(10000);
        Assert.assertEquals(new Summit("1",standardGeopoint,i).elevation(), i);
    }
    @Test
    public void checkToString(){
        String s = new String(Integer.toString(new Random().nextInt(1000)));
        GeoPoint g = new GeoPoint(new Random().nextDouble(),new Random().nextDouble());
        int i = new Random().nextInt(10000);
        Assert.assertEquals(new Summit(s,g,i).toString(), (s.toUpperCase() + " " + g.toString() + " " + i));
    }

}
