package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.lang.Math.*;
import static org.junit.Assert.assertEquals;

public class CheckHgtDEM {
	final static File HGT_FILE = new File("N46E006.hgt");
	final static double ORIGIN_LON = toRadians(6.25);
	final static double ORIGIN_LAT = toRadians(46.25);
	final static double WIDTH = toRadians(0.5);
	final static int IMAGE_SIZE = 300;
	final static double MIN_ELEVATION = 200;
	final static double MAX_ELEVATION = 1_500;

	private static int gray(double v) {
		double clampedV = max(0, min(v, 1));
		return (int) (255.9999 * clampedV);
	}

	@Test public void checkHgtDem() throws Exception {
		BufferedImage i = ImageIO.read(new File("TestRessources/dem_prof.png"));

		DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE);
		ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);

		double step = WIDTH / (IMAGE_SIZE - 1);
		for (int x = 0; x < IMAGE_SIZE; ++x) {
			double lon = ORIGIN_LON + x * step;
			for (int y = 0; y < IMAGE_SIZE; ++y) {
				double lat = ORIGIN_LAT + y * step;
				GeoPoint p = new GeoPoint(lon, lat);
				double el =
						(cDEM.elevationAt(p) - MIN_ELEVATION) / (MAX_ELEVATION
								- MIN_ELEVATION);
				i.setRGB(x, IMAGE_SIZE - 1 - y, gray(el));

				assertEquals(
						"Incorrect value at " + x + " " + (IMAGE_SIZE - 1 - y),
						gray(el), i.getRGB(x, IMAGE_SIZE - 1 - y) & 0xFF);
			}
		}
		dDEM.close();
	}
}